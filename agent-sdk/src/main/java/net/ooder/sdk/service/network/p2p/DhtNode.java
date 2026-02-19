
package net.ooder.sdk.service.network.p2p;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DhtNode {
    
    private static final Logger log = LoggerFactory.getLogger(DhtNode.class);
    
    private final String nodeId;
    private final InetSocketAddress address;
    private final Map<String, String> dataStore;
    private final Map<String, DhtNode> routingTable;
    private final List<DhtNodeListener> listeners;
    private volatile boolean running;
    
    public DhtNode(String nodeId, int port) {
        this.nodeId = nodeId;
        this.address = new InetSocketAddress(port);
        this.dataStore = new ConcurrentHashMap<>();
        this.routingTable = new ConcurrentHashMap<>();
        this.listeners = new ArrayList<>();
        this.running = false;
    }
    
    public void start() {
        running = true;
        log.info("DHT Node started: {} on port {}", nodeId, address.getPort());
    }
    
    public void stop() {
        running = false;
        routingTable.clear();
        log.info("DHT Node stopped: {}", nodeId);
    }
    
    public void put(String key, String value) {
        dataStore.put(key, value);
        log.debug("Stored key: {} in DHT", key);
        notifyListeners("PUT", key, value);
    }
    
    public String get(String key) {
        String value = dataStore.get(key);
        if (value == null) {
            value = lookupInNetwork(key);
        }
        return value;
    }
    
    public void remove(String key) {
        dataStore.remove(key);
        notifyListeners("REMOVE", key, null);
    }
    
    private String lookupInNetwork(String key) {
        for (DhtNode node : routingTable.values()) {
            String value = node.get(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }
    
    public void joinNetwork(DhtNode bootstrapNode) {
        routingTable.put(bootstrapNode.getNodeId(), bootstrapNode);
        log.info("Joined DHT network via bootstrap node: {}", bootstrapNode.getNodeId());
    }
    
    public void addNode(DhtNode node) {
        routingTable.put(node.getNodeId(), node);
        log.debug("Added node to routing table: {}", node.getNodeId());
    }
    
    public void removeNode(String nodeId) {
        routingTable.remove(nodeId);
        log.debug("Removed node from routing table: {}", nodeId);
    }
    
    public List<DhtNode> getKnownNodes() {
        return new ArrayList<>(routingTable.values());
    }
    
    public void addListener(DhtNodeListener listener) {
        listeners.add(listener);
    }
    
    private void notifyListeners(String operation, String key, String value) {
        for (DhtNodeListener listener : listeners) {
            try {
                listener.onDataChanged(operation, key, value);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public String getNodeId() { return nodeId; }
    public InetSocketAddress getAddress() { return address; }
    public boolean isRunning() { return running; }
    
    public interface DhtNodeListener {
        void onDataChanged(String operation, String key, String value);
    }
}
