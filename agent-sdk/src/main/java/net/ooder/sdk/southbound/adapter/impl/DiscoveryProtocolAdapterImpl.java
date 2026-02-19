
package net.ooder.sdk.southbound.adapter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.ooder.sdk.southbound.adapter.DiscoveryProtocolAdapter;
import net.ooder.sdk.southbound.adapter.model.DiscoveryConfig;
import net.ooder.sdk.southbound.adapter.model.DiscoveredNode;
import net.ooder.sdk.southbound.protocol.DiscoveryProtocol;
import net.ooder.sdk.southbound.protocol.model.DiscoveryListener;
import net.ooder.sdk.southbound.protocol.model.DiscoveryRequest;
import net.ooder.sdk.southbound.protocol.model.DiscoveryResult;
import net.ooder.sdk.southbound.protocol.model.PeerInfo;

public class DiscoveryProtocolAdapterImpl implements DiscoveryProtocolAdapter {
    
    private final DiscoveryProtocol discoveryProtocol;
    private final List<DiscoveredNode> discoveredNodes = new CopyOnWriteArrayList<>();
    private final Map<String, DiscoveredNode> nodeMap = new ConcurrentHashMap<>();
    private final List<DiscoveryListener> listeners = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    private volatile boolean discovering = false;
    private DiscoveryConfig currentConfig;
    
    public DiscoveryProtocolAdapterImpl(DiscoveryProtocol discoveryProtocol) {
        this.discoveryProtocol = discoveryProtocol;
        setupInternalListener();
    }
    
    private void setupInternalListener() {
        discoveryProtocol.addDiscoveryListener(new DiscoveryListener() {
            @Override
            public void onPeerDiscovered(PeerInfo peer) {
                DiscoveredNode node = convertToNode(peer);
                addOrUpdateNode(node);
                notifyNodeDiscovered(node);
            }
            
            @Override
            public void onPeerLost(String peerId) {
                DiscoveredNode node = nodeMap.remove(peerId);
                if (node != null) {
                    discoveredNodes.remove(node);
                    notifyNodeLost(node);
                }
            }
            
            @Override
            public void onMcpDiscovered(PeerInfo mcp) {
                DiscoveredNode node = convertToNode(mcp);
                node.setNodeType("MCP");
                addOrUpdateNode(node);
            }
            
            @Override
            public void onDiscoveryComplete(DiscoveryResult result) {
                if (result != null && result.getPeers() != null) {
                    for (PeerInfo peer : result.getPeers()) {
                        DiscoveredNode node = convertToNode(peer);
                        addOrUpdateNode(node);
                    }
                }
            }
        });
    }
    
    @Override
    public void startDiscovery(DiscoveryConfig config) {
        this.currentConfig = config;
        this.discovering = true;
        
        DiscoveryRequest request = convertToRequest(config);
        discoveryProtocol.discover(request);
        
        if (config.isAutoRefresh() && config.getInterval() > 0) {
            scheduler.scheduleAtFixedRate(() -> {
                if (discovering) {
                    discoveryProtocol.discover(request);
                }
            }, config.getInterval(), config.getInterval(), TimeUnit.MILLISECONDS);
        }
    }
    
    @Override
    public void stopDiscovery() {
        this.discovering = false;
        discoveryProtocol.stopBroadcast();
    }
    
    @Override
    public boolean isDiscovering() {
        return discovering;
    }
    
    @Override
    public List<DiscoveredNode> getDiscoveredNodes() {
        return new ArrayList<>(discoveredNodes);
    }
    
    @Override
    public CompletableFuture<List<DiscoveredNode>> discoverOnce(DiscoveryConfig config) {
        DiscoveryRequest request = convertToRequest(config);
        return discoveryProtocol.discover(request)
            .thenApply(result -> {
                List<DiscoveredNode> nodes = new ArrayList<>();
                if (result != null && result.getPeers() != null) {
                    for (PeerInfo peer : result.getPeers()) {
                        nodes.add(convertToNode(peer));
                    }
                }
                return nodes;
            });
    }
    
    @Override
    public void addDiscoveryListener(DiscoveryListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }
    
    @Override
    public void removeDiscoveryListener(DiscoveryListener listener) {
        listeners.remove(listener);
    }
    
    private void addOrUpdateNode(DiscoveredNode node) {
        nodeMap.put(node.getNodeId(), node);
        if (!discoveredNodes.contains(node)) {
            discoveredNodes.add(node);
        }
    }
    
    private DiscoveredNode convertToNode(PeerInfo peer) {
        DiscoveredNode node = new DiscoveredNode();
        node.setNodeId(peer.getPeerId());
        node.setNodeName(peer.getPeerName());
        node.setNodeType(peer.getPeerType() != null ? peer.getPeerType().name() : "UNKNOWN");
        node.setAddress(peer.getIpAddress());
        node.setPort(peer.getPort());
        node.setDiscoveredTime(System.currentTimeMillis());
        node.setLastSeenTime(peer.getLastSeen());
        node.setStatus("online");
        return node;
    }
    
    private DiscoveryRequest convertToRequest(DiscoveryConfig config) {
        DiscoveryRequest request = new DiscoveryRequest();
        request.setTimeout(config.getTimeout());
        return request;
    }
    
    private void notifyNodeDiscovered(DiscoveredNode node) {
        PeerInfo peer = convertToPeer(node);
        for (DiscoveryListener listener : listeners) {
            try {
                listener.onPeerDiscovered(peer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void notifyNodeLost(DiscoveredNode node) {
        for (DiscoveryListener listener : listeners) {
            try {
                listener.onPeerLost(node.getNodeId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private PeerInfo convertToPeer(DiscoveredNode node) {
        PeerInfo peer = new PeerInfo();
        peer.setPeerId(node.getNodeId());
        peer.setPeerName(node.getNodeName());
        peer.setIpAddress(node.getAddress());
        peer.setPort(node.getPort());
        return peer;
    }
    
    public void shutdown() {
        stopDiscovery();
        scheduler.shutdown();
    }
}
