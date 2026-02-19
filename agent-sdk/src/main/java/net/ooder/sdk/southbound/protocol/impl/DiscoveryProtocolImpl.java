package net.ooder.sdk.southbound.protocol.impl;

import net.ooder.sdk.southbound.protocol.*;
import net.ooder.sdk.southbound.protocol.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DiscoveryProtocolImpl implements DiscoveryProtocol {
    
    private static final Logger log = LoggerFactory.getLogger(DiscoveryProtocolImpl.class);
    
    private final List<DiscoveryListener> listeners;
    private final ExecutorService executor;
    private final ScheduledExecutorService broadcastExecutor;
    private final AtomicBoolean broadcasting;
    private ScheduledFuture<?> broadcastTask;
    
    private final Map<String, PeerInfo> discoveredPeers;
    private PeerInfo discoveredMcp;
    
    private int broadcastPort = 9001;
    private int discoveryTimeout = 5000;
    
    public DiscoveryProtocolImpl() {
        this.listeners = new CopyOnWriteArrayList<DiscoveryListener>();
        this.executor = Executors.newCachedThreadPool();
        this.broadcastExecutor = Executors.newSingleThreadScheduledExecutor();
        this.broadcasting = new AtomicBoolean(false);
        this.discoveredPeers = new ConcurrentHashMap<String, PeerInfo>();
        log.info("DiscoveryProtocolImpl initialized");
    }
    
    @Override
    public CompletableFuture<DiscoveryResult> discover(DiscoveryRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Starting discovery: type={}", request.getType());
            DiscoveryResult result = new DiscoveryResult();
            result.setRequestId(request.getRequestId());
            
            try {
                List<PeerInfo> peers = doDiscovery(request);
                result.setSuccess(true);
                result.setPeers(peers);
                result.setMcp(discoveredMcp);
                
                for (PeerInfo peer : peers) {
                    discoveredPeers.put(peer.getPeerId(), peer);
                    notifyPeerDiscovered(peer);
                }
                
                if (discoveredMcp != null) {
                    notifyMcpDiscovered(discoveredMcp);
                }
                
                notifyDiscoveryComplete(result);
                log.info("Discovery completed: found {} peers", peers.size());
            } catch (Exception e) {
                log.error("Discovery failed", e);
                result.setSuccess(false);
                result.setErrorMessage(e.getMessage());
            }
            
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<PeerInfo>> discoverPeers() {
        DiscoveryRequest request = new DiscoveryRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setType(DiscoveryType.LAN);
        request.setTimeout(discoveryTimeout);
        
        return discover(request).thenApply(DiscoveryResult::getPeers);
    }
    
    @Override
    public CompletableFuture<PeerInfo> discoverMcp() {
        return CompletableFuture.supplyAsync(() -> {
            if (discoveredMcp != null) {
                return discoveredMcp;
            }
            
            DiscoveryRequest request = new DiscoveryRequest();
            request.setRequestId(UUID.randomUUID().toString());
            request.setType(DiscoveryType.LAN);
            request.setTimeout(discoveryTimeout);
            
            DiscoveryResult result = discover(request).join();
            return result.getMcp();
        }, executor);
    }
    
    private List<PeerInfo> doDiscovery(DiscoveryRequest request) {
        List<PeerInfo> peers = new ArrayList<PeerInfo>();
        
        if (request.getType() == DiscoveryType.LOCAL || request.getType() == DiscoveryType.ALL) {
            peers.addAll(discoverLocal());
        }
        
        if (request.getType() == DiscoveryType.LAN || request.getType() == DiscoveryType.ALL) {
            peers.addAll(discoverLan(request.getTimeout()));
        }
        
        return peers;
    }
    
    private List<PeerInfo> discoverLocal() {
        List<PeerInfo> peers = new ArrayList<PeerInfo>();
        
        try {
            String localHost = java.net.InetAddress.getLocalHost().getHostAddress();
            String hostName = java.net.InetAddress.getLocalHost().getHostName();
            
            PeerInfo localPeer = new PeerInfo();
            localPeer.setPeerId("local-" + java.util.UUID.randomUUID().toString().substring(0, 8));
            localPeer.setPeerName(hostName);
            localPeer.setPeerType(PeerType.END_AGENT);
            localPeer.setIpAddress(localHost);
            localPeer.setPort(9000);
            localPeer.setLastSeen(System.currentTimeMillis());
            peers.add(localPeer);
            
            log.debug("Local discovery found: {} at {}", hostName, localHost);
        } catch (Exception e) {
            log.debug("Local discovery failed: {}", e.getMessage());
        }
        
        return peers;
    }
    
    private List<PeerInfo> discoverLan(int timeout) {
        List<PeerInfo> peers = new ArrayList<PeerInfo>();
        
        try {
            java.net.InetAddress localHost = java.net.InetAddress.getLocalHost();
            String localAddress = localHost.getHostAddress();
            
            String[] parts = localAddress.split("\\.");
            if (parts.length == 4) {
                String subnet = parts[0] + "." + parts[1] + "." + parts[2];
                
                java.util.concurrent.ExecutorService pingExecutor = 
                    java.util.concurrent.Executors.newFixedThreadPool(10);
                java.util.List<java.util.concurrent.Future<PeerInfo>> futures = 
                    new java.util.ArrayList<java.util.concurrent.Future<PeerInfo>>();
                
                for (int i = 1; i <= 254; i++) {
                    final String targetIp = subnet + "." + i;
                    futures.add(pingExecutor.submit(new java.util.concurrent.Callable<PeerInfo>() {
                        @Override
                        public PeerInfo call() {
                            try {
                                java.net.InetAddress address = java.net.InetAddress.getByName(targetIp);
                                if (address.isReachable(Math.min(timeout / 254, 1000))) {
                                    PeerInfo peer = new PeerInfo();
                                    peer.setPeerId("lan-" + targetIp.replace(".", "-"));
                                    peer.setPeerName(address.getHostName());
                                    peer.setPeerType(PeerType.END_AGENT);
                                    peer.setIpAddress(targetIp);
                                    peer.setPort(9001);
                                    peer.setLastSeen(System.currentTimeMillis());
                                    return peer;
                                }
                            } catch (Exception e) {
                                // Ignore unreachable hosts
                            }
                            return null;
                        }
                    }));
                }
                
                for (java.util.concurrent.Future<PeerInfo> future : futures) {
                    try {
                        PeerInfo peer = future.get(timeout, java.util.concurrent.TimeUnit.MILLISECONDS);
                        if (peer != null) {
                            peers.add(peer);
                        }
                    } catch (Exception e) {
                        // Ignore timeout
                    }
                }
                
                pingExecutor.shutdown();
                
                if (!peers.isEmpty() && discoveredMcp == null) {
                    PeerInfo mcp = peers.get(0);
                    mcp.setPeerType(PeerType.MCP_AGENT);
                    mcp.setPeerId("mcp-" + java.util.UUID.randomUUID().toString().substring(0, 8));
                    mcp.setPeerName("MCP-Agent");
                    discoveredMcp = mcp;
                    log.info("MCP discovered via LAN: {}", mcp.getIpAddress());
                }
            }
            
            log.debug("LAN discovery found {} peers", peers.size());
        } catch (Exception e) {
            log.debug("LAN discovery failed: {}", e.getMessage());
        }
        
        return peers;
    }
    
    @Override
    public void addDiscoveryListener(DiscoveryListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeDiscoveryListener(DiscoveryListener listener) {
        listeners.remove(listener);
    }
    
    @Override
    public void startBroadcast() {
        if (broadcasting.compareAndSet(false, true)) {
            broadcastTask = broadcastExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    doBroadcast();
                }
            }, 0, 5, TimeUnit.SECONDS);
            log.info("Broadcast started on port {}", broadcastPort);
        }
    }
    
    @Override
    public void stopBroadcast() {
        if (broadcasting.compareAndSet(true, false)) {
            if (broadcastTask != null) {
                broadcastTask.cancel(false);
            }
            log.info("Broadcast stopped");
        }
    }
    
    @Override
    public boolean isBroadcasting() {
        return broadcasting.get();
    }
    
    private void doBroadcast() {
        log.debug("Broadcasting presence on port {}", broadcastPort);
        
        try {
            java.net.DatagramSocket socket = new java.net.DatagramSocket();
            socket.setBroadcast(true);
            
            String broadcastMessage = "OODER_DISCOVERY:" + java.net.InetAddress.getLocalHost().getHostAddress() + ":" + broadcastPort;
            byte[] buffer = broadcastMessage.getBytes();
            
            java.net.InetAddress broadcastAddress = java.net.InetAddress.getByName("255.255.255.255");
            java.net.DatagramPacket packet = new java.net.DatagramPacket(buffer, buffer.length, broadcastAddress, broadcastPort);
            
            socket.send(packet);
            socket.close();
            
            log.debug("Broadcast sent: {}", broadcastMessage);
        } catch (Exception e) {
            log.debug("Broadcast failed (expected in some environments): {}", e.getMessage());
        }
    }
    
    private void notifyPeerDiscovered(PeerInfo peer) {
        for (DiscoveryListener listener : listeners) {
            try {
                listener.onPeerDiscovered(peer);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyMcpDiscovered(PeerInfo mcp) {
        for (DiscoveryListener listener : listeners) {
            try {
                listener.onMcpDiscovered(mcp);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyDiscoveryComplete(DiscoveryResult result) {
        for (DiscoveryListener listener : listeners) {
            try {
                listener.onDiscoveryComplete(result);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public void shutdown() {
        log.info("Shutting down DiscoveryProtocol");
        stopBroadcast();
        executor.shutdown();
        broadcastExecutor.shutdown();
        discoveredPeers.clear();
        discoveredMcp = null;
        log.info("DiscoveryProtocol shutdown complete");
    }
    
    public void setBroadcastPort(int port) {
        this.broadcastPort = port;
    }
    
    public void setDiscoveryTimeout(int timeout) {
        this.discoveryTimeout = timeout;
    }
}
