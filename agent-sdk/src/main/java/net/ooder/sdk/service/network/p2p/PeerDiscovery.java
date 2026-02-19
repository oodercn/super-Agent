
package net.ooder.sdk.service.network.p2p;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.service.network.udp.UdpClient;
import net.ooder.sdk.service.network.udp.UdpServer;

public class PeerDiscovery {
    
    private static final Logger log = LoggerFactory.getLogger(PeerDiscovery.class);
    
    private static final String DISCOVERY_MULTICAST = "239.255.255.250";
    private static final int DISCOVERY_PORT = 1900;
    private static final String DISCOVERY_MESSAGE = "OODER_PEER_DISCOVERY";
    
    private final String localNodeId;
    private final int localPort;
    private final Map<String, PeerInfo> discoveredPeers;
    private final List<PeerDiscoveryListener> listeners;
    private final ScheduledExecutorService scheduler;
    
    private UdpServer udpServer;
    private UdpClient udpClient;
    private volatile boolean running;
    private long discoveryInterval = 30000;
    private long peerTimeout = 120000;
    
    public PeerDiscovery(String localNodeId, int localPort) {
        this.localNodeId = localNodeId;
        this.localPort = localPort;
        this.discoveredPeers = new ConcurrentHashMap<>();
        this.listeners = new ArrayList<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }
    
    public void start() {
        running = true;
        
        try {
            udpServer = new UdpServer(DISCOVERY_PORT);
            udpServer.setDefaultHandler((data, source, context) -> {
                handleDiscoveryPacket(data, source);
            });
            udpServer.start();
            
            udpClient = new UdpClient();
            udpClient.connect();
            
            scheduler.scheduleAtFixedRate(this::broadcastDiscovery, 0, discoveryInterval, TimeUnit.MILLISECONDS);
            scheduler.scheduleAtFixedRate(this::cleanupStalePeers, 0, 60000, TimeUnit.MILLISECONDS);
            
            log.info("Peer discovery started on port {}", DISCOVERY_PORT);
        } catch (Exception e) {
            log.error("Failed to start peer discovery", e);
        }
    }
    
    public void stop() {
        running = false;
        scheduler.shutdown();
        
        if (udpServer != null) {
            udpServer.stop();
        }
        if (udpClient != null) {
            udpClient.disconnect();
        }
        
        log.info("Peer discovery stopped");
    }
    
    private void broadcastDiscovery() {
        if (!running) return;
        
        try {
            String message = buildDiscoveryMessage();
            byte[] data = message.getBytes("UTF-8");
            
            udpClient.send(data, DISCOVERY_MULTICAST, DISCOVERY_PORT);
            
            log.debug("Broadcast discovery message");
        } catch (Exception e) {
            log.warn("Failed to broadcast discovery", e);
        }
    }
    
    private void handleDiscoveryPacket(byte[] data, String source) {
        try {
            String message = new String(data, "UTF-8");
            
            if (message.startsWith(DISCOVERY_MESSAGE)) {
                PeerInfo peerInfo = parseDiscoveryMessage(message);
                
                if (!peerInfo.getNodeId().equals(localNodeId)) {
                    discoveredPeers.put(peerInfo.getNodeId(), peerInfo);
                    notifyPeerDiscovered(peerInfo);
                    log.debug("Discovered peer: {} at {}:{}", 
                        peerInfo.getNodeId(), peerInfo.getHost(), peerInfo.getPort());
                }
            }
        } catch (Exception e) {
            log.warn("Failed to handle discovery packet", e);
        }
    }
    
    private String buildDiscoveryMessage() {
        return DISCOVERY_MESSAGE + "|" + localNodeId + "|" + localPort + "|" + System.currentTimeMillis();
    }
    
    private PeerInfo parseDiscoveryMessage(String message) {
        String[] parts = message.split("\\|");
        PeerInfo info = new PeerInfo();
        info.setNodeId(parts[1]);
        info.setPort(Integer.parseInt(parts[2]));
        info.setLastSeen(Long.parseLong(parts[3]));
        return info;
    }
    
    private void cleanupStalePeers() {
        long now = System.currentTimeMillis();
        discoveredPeers.entrySet().removeIf(entry -> 
            now - entry.getValue().getLastSeen() > peerTimeout);
    }
    
    public List<PeerInfo> getDiscoveredPeers() {
        return new ArrayList<>(discoveredPeers.values());
    }
    
    public void addListener(PeerDiscoveryListener listener) {
        listeners.add(listener);
    }
    
    private void notifyPeerDiscovered(PeerInfo peer) {
        for (PeerDiscoveryListener listener : listeners) {
            try {
                listener.onPeerDiscovered(peer);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public void setDiscoveryInterval(long interval) { this.discoveryInterval = interval; }
    public void setPeerTimeout(long timeout) { this.peerTimeout = timeout; }
    
    public static class PeerInfo {
        private String nodeId;
        private String host;
        private int port;
        private long lastSeen;
        
        public String getNodeId() { return nodeId; }
        public void setNodeId(String nodeId) { this.nodeId = nodeId; }
        
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        
        public long getLastSeen() { return lastSeen; }
        public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }
    }
    
    public interface PeerDiscoveryListener {
        void onPeerDiscovered(PeerInfo peer);
    }
}
