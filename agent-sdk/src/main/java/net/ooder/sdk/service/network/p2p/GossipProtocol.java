
package net.ooder.sdk.service.network.p2p;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GossipProtocol {
    
    private static final Logger log = LoggerFactory.getLogger(GossipProtocol.class);
    private static final SecureRandom RANDOM = new SecureRandom();
    
    private final String localNodeId;
    private final Map<String, GossipMessage> messageStore;
    private final Map<String, Long> seenMessages;
    private final List<GossipPeer> peers;
    private final List<GossipListener> listeners;
    private final ScheduledExecutorService scheduler;
    
    private int fanout = 3;
    private long messageTtl = 300000;
    private volatile boolean running;
    
    public GossipProtocol(String localNodeId) {
        this.localNodeId = localNodeId;
        this.messageStore = new HashMap<>();
        this.seenMessages = new HashMap<>();
        this.peers = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.running = false;
    }
    
    public void start() {
        running = true;
        scheduler.scheduleAtFixedRate(this::gossipMessages, 0, 1, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::cleanupOldMessages, 0, 60, TimeUnit.SECONDS);
        log.info("Gossip protocol started for node: {}", localNodeId);
    }
    
    public void stop() {
        running = false;
        scheduler.shutdown();
        log.info("Gossip protocol stopped");
    }
    
    public void addPeer(GossipPeer peer) {
        peers.add(peer);
        log.debug("Added gossip peer: {}", peer.getNodeId());
    }
    
    public void removePeer(String nodeId) {
        peers.removeIf(p -> p.getNodeId().equals(nodeId));
    }
    
    public void broadcast(String topic, byte[] payload) {
        GossipMessage message = new GossipMessage();
        message.setMessageId(generateMessageId());
        message.setSourceNodeId(localNodeId);
        message.setTopic(topic);
        message.setPayload(payload);
        message.setTimestamp(System.currentTimeMillis());
        message.setHopCount(0);
        
        messageStore.put(message.getMessageId(), message);
        seenMessages.put(message.getMessageId(), message.getTimestamp());
        
        log.debug("Broadcasting message: {} on topic: {}", message.getMessageId(), topic);
        spreadMessage(message);
    }
    
    public void receive(GossipMessage message) {
        if (seenMessages.containsKey(message.getMessageId())) {
            return;
        }
        
        seenMessages.put(message.getMessageId(), message.getTimestamp());
        
        notifyListeners(message);
        
        if (message.getHopCount() < 10) {
            message.incrementHopCount();
            spreadMessage(message);
        }
    }
    
    private void spreadMessage(GossipMessage message) {
        if (peers.isEmpty()) {
            return;
        }
        
        List<GossipPeer> selectedPeers = selectPeers();
        for (GossipPeer peer : selectedPeers) {
            try {
                peer.send(message);
            } catch (Exception e) {
                log.warn("Failed to send message to peer: {}", peer.getNodeId(), e);
            }
        }
    }
    
    private List<GossipPeer> selectPeers() {
        List<GossipPeer> selected = new ArrayList<>();
        List<GossipPeer> available = new ArrayList<>(peers);
        
        int count = Math.min(fanout, available.size());
        for (int i = 0; i < count && !available.isEmpty(); i++) {
            int index = RANDOM.nextInt(available.size());
            selected.add(available.remove(index));
        }
        
        return selected;
    }
    
    private void gossipMessages() {
        if (!running) return;
        
        for (GossipMessage message : messageStore.values()) {
            if (System.currentTimeMillis() - message.getTimestamp() < messageTtl) {
                spreadMessage(message);
            }
        }
    }
    
    private void cleanupOldMessages() {
        long now = System.currentTimeMillis();
        messageStore.entrySet().removeIf(e -> now - e.getValue().getTimestamp() > messageTtl);
        seenMessages.entrySet().removeIf(e -> now - e.getValue() > messageTtl);
    }
    
    private String generateMessageId() {
        return localNodeId + "-" + System.currentTimeMillis() + "-" + RANDOM.nextInt(10000);
    }
    
    public void addListener(GossipListener listener) {
        listeners.add(listener);
    }
    
    private void notifyListeners(GossipMessage message) {
        for (GossipListener listener : listeners) {
            try {
                listener.onMessage(message);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public void setFanout(int fanout) { this.fanout = fanout; }
    public void setMessageTtl(long ttl) { this.messageTtl = ttl; }
    
    public static class GossipMessage {
        private String messageId;
        private String sourceNodeId;
        private String topic;
        private byte[] payload;
        private long timestamp;
        private int hopCount;
        
        public String getMessageId() { return messageId; }
        public void setMessageId(String messageId) { this.messageId = messageId; }
        
        public String getSourceNodeId() { return sourceNodeId; }
        public void setSourceNodeId(String sourceNodeId) { this.sourceNodeId = sourceNodeId; }
        
        public String getTopic() { return topic; }
        public void setTopic(String topic) { this.topic = topic; }
        
        public byte[] getPayload() { return payload; }
        public void setPayload(byte[] payload) { this.payload = payload; }
        
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        
        public int getHopCount() { return hopCount; }
        public void setHopCount(int hopCount) { this.hopCount = hopCount; }
        
        public void incrementHopCount() { this.hopCount++; }
    }
    
    public interface GossipPeer {
        String getNodeId();
        void send(GossipMessage message);
    }
    
    public interface GossipListener {
        void onMessage(GossipMessage message);
    }
}
