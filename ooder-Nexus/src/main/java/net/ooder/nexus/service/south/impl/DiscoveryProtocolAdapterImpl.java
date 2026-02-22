package net.ooder.nexus.service.south.impl;

import net.ooder.nexus.service.south.DiscoveryProtocolAdapter;
import net.ooder.sdk.api.OoderSDK;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class DiscoveryProtocolAdapterImpl implements DiscoveryProtocolAdapter {

    private static final Logger log = LoggerFactory.getLogger(DiscoveryProtocolAdapterImpl.class);

    private static final int DEFAULT_BROADCAST_PORT = 9001;
    private static final int DEFAULT_TIMEOUT = 5000;

    private final OoderSDK sdk;
    private final List<DiscoveryEventListener> listeners = new CopyOnWriteArrayList<DiscoveryEventListener>();
    private final Map<String, PeerDTO> discoveredPeers = new ConcurrentHashMap<String, PeerDTO>();
    
    private DatagramSocket broadcastSocket;
    private boolean broadcasting = false;
    private String localNodeId;
    private String localNodeName;

    @Autowired
    public DiscoveryProtocolAdapterImpl(@Autowired(required = false) OoderSDK sdk) {
        this.sdk = sdk;
        this.localNodeId = UUID.randomUUID().toString();
        this.localNodeName = "Nexus-" + localNodeId.substring(0, 8);
        log.info("DiscoveryProtocolAdapter initialized with SDK 0.7.2");
    }

    @Override
    public CompletableFuture<DiscoveryResultDTO> discoverPeers(DiscoveryRequestDTO request) {
        log.info("Discovering peers with request: {}", request.getRequestId());
        
        return CompletableFuture.supplyAsync(() -> {
            DiscoveryResultDTO result = new DiscoveryResultDTO();
            result.setRequestId(request.getRequestId());
            
            try {
                int timeout = request.getTimeout() > 0 ? request.getTimeout() : DEFAULT_TIMEOUT;
                
                sendDiscoveryBroadcast();
                
                Thread.sleep(Math.min(timeout, 3000));
                
                List<PeerDTO> peers = new ArrayList<PeerDTO>(discoveredPeers.values());
                result.setSuccess(true);
                result.setPeers(peers);
                
                notifyDiscoveryComplete(result);
                
            } catch (Exception e) {
                log.error("Discovery failed", e);
                result.setSuccess(false);
                result.setErrorMessage(e.getMessage());
            }
            
            return result;
        });
    }

    @Override
    public CompletableFuture<List<PeerDTO>> listDiscoveredPeers() {
        log.info("Listing discovered peers");
        
        return CompletableFuture.supplyAsync(() -> {
            List<PeerDTO> peers = new ArrayList<PeerDTO>();
            
            long now = System.currentTimeMillis();
            for (PeerDTO peer : discoveredPeers.values()) {
                if (now - peer.getLastSeen() < 60000) {
                    peer.setOnline(true);
                    peers.add(peer);
                }
            }
            
            return peers;
        });
    }

    @Override
    public CompletableFuture<PeerDTO> discoverMcp() {
        log.info("Discovering MCP");
        
        return CompletableFuture.supplyAsync(() -> {
            for (PeerDTO peer : discoveredPeers.values()) {
                if ("MCP".equals(peer.getPeerType())) {
                    return peer;
                }
            }
            
            PeerDTO mcp = new PeerDTO();
            mcp.setPeerId("mcp-local");
            mcp.setPeerName("Local MCP");
            mcp.setPeerType("MCP");
            mcp.setIpAddress("127.0.0.1");
            mcp.setPort(8080);
            mcp.setOnline(true);
            mcp.setLastSeen(System.currentTimeMillis());
            
            return mcp;
        });
    }

    @Override
    public void addDiscoveryListener(DiscoveryEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeDiscoveryListener(DiscoveryEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void startBroadcast() {
        if (broadcasting) {
            return;
        }
        
        log.info("Starting broadcast");
        
        try {
            broadcastSocket = new DatagramSocket();
            broadcastSocket.setBroadcast(true);
            broadcasting = true;
            
            Thread broadcastThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (broadcasting) {
                        try {
                            sendHeartbeatBroadcast();
                            Thread.sleep(5000);
                        } catch (Exception e) {
                            if (broadcasting) {
                                log.warn("Broadcast error: {}", e.getMessage());
                            }
                        }
                    }
                }
            }, "discovery-broadcast");
            broadcastThread.setDaemon(true);
            broadcastThread.start();
            
        } catch (Exception e) {
            log.error("Failed to start broadcast", e);
        }
    }

    @Override
    public void stopBroadcast() {
        log.info("Stopping broadcast");
        broadcasting = false;
        
        if (broadcastSocket != null) {
            broadcastSocket.close();
            broadcastSocket = null;
        }
    }

    @Override
    public boolean isBroadcasting() {
        return broadcasting;
    }

    private void sendDiscoveryBroadcast() {
        try {
            String message = "DISCOVERY:" + localNodeId + ":" + localNodeName;
            byte[] data = message.getBytes();
            
            DatagramPacket packet = new DatagramPacket(
                data, 
                data.length, 
                InetAddress.getByName("255.255.255.255"), 
                DEFAULT_BROADCAST_PORT
            );
            
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            socket.send(packet);
            socket.close();
            
            log.debug("Discovery broadcast sent");
        } catch (Exception e) {
            log.warn("Failed to send discovery broadcast: {}", e.getMessage());
        }
    }

    private void sendHeartbeatBroadcast() {
        try {
            String message = "HEARTBEAT:" + localNodeId + ":" + localNodeName + ":" + System.currentTimeMillis();
            byte[] data = message.getBytes();
            
            DatagramPacket packet = new DatagramPacket(
                data, 
                data.length, 
                InetAddress.getByName("255.255.255.255"), 
                DEFAULT_BROADCAST_PORT
            );
            
            broadcastSocket.send(packet);
            
            log.debug("Heartbeat broadcast sent");
        } catch (Exception e) {
            log.warn("Failed to send heartbeat broadcast: {}", e.getMessage());
        }
    }

    void handleIncomingDiscoveryMessage(String message, String sourceAddress) {
        try {
            String[] parts = message.split(":");
            if (parts.length < 3) return;
            
            String type = parts[0];
            String peerId = parts[1];
            String peerName = parts[2];
            
            if (peerId.equals(localNodeId)) return;
            
            PeerDTO peer = discoveredPeers.get(peerId);
            if (peer == null) {
                peer = new PeerDTO();
                peer.setPeerId(peerId);
                peer.setPeerName(peerName);
                peer.setPeerType("NEXUS");
                discoveredPeers.put(peerId, peer);
                notifyPeerDiscovered(peer);
            }
            
            peer.setIpAddress(sourceAddress);
            peer.setPort(DEFAULT_BROADCAST_PORT);
            peer.setLastSeen(System.currentTimeMillis());
            peer.setOnline(true);
            
        } catch (Exception e) {
            log.warn("Failed to handle discovery message: {}", e.getMessage());
        }
    }

    private void notifyPeerDiscovered(PeerDTO peer) {
        for (DiscoveryEventListener listener : listeners) {
            try {
                listener.onPeerDiscovered(peer);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyPeerLost(String peerId) {
        for (DiscoveryEventListener listener : listeners) {
            try {
                listener.onPeerLost(peerId);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyDiscoveryComplete(DiscoveryResultDTO result) {
        for (DiscoveryEventListener listener : listeners) {
            try {
                listener.onDiscoveryComplete(result);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }
}
