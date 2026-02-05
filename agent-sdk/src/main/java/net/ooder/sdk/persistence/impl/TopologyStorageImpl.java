package net.ooder.sdk.persistence.impl;

import net.ooder.sdk.persistence.StorageManager;
import net.ooder.sdk.persistence.TopologyStorage;
import net.ooder.sdk.topology.model.NetworkNode;
import net.ooder.sdk.topology.model.NetworkLink;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TopologyStorageImpl implements TopologyStorage {
    private static final String NODE_PREFIX = "node_";
    private static final String LINK_PREFIX = "link_";
    private final StorageManager storageManager;
    
    public TopologyStorageImpl(StorageManager storageManager) {
        this.storageManager = storageManager;
    }
    
    // 网络节点相关方法
    @Override
    public CompletableFuture<Boolean> saveNode(NetworkNode node) {
        String key = NODE_PREFIX + node.getNodeId();
        return storageManager.save(key, node);
    }
    
    @Override
    public CompletableFuture<Boolean> saveNodes(List<NetworkNode> nodes) {
        Map<String, NetworkNode> entries = new HashMap<>();
        for (NetworkNode node : nodes) {
            String key = NODE_PREFIX + node.getNodeId();
            entries.put(key, node);
        }
        return storageManager.saveAll(entries);
    }
    
    @Override
    public CompletableFuture<NetworkNode> loadNode(String nodeId) {
        String key = NODE_PREFIX + nodeId;
        return storageManager.load(key, NetworkNode.class);
    }
    
    @Override
    public CompletableFuture<List<NetworkNode>> loadAllNodes() {
        return storageManager.loadAll(NetworkNode.class)
            .thenApply(map -> new ArrayList<>(map.values()));
    }
    
    @Override
    public CompletableFuture<List<NetworkNode>> loadNodesByType(String nodeType) {
        return loadAllNodes()
            .thenApply(nodes -> nodes.stream()
                .filter(node -> nodeType.equals(node.getNodeType().toString()))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<List<NetworkNode>> loadNodesByStatus(String status) {
        return loadAllNodes()
            .thenApply(nodes -> nodes.stream()
                .filter(node -> status.equals(node.getStatus().toString()))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<Boolean> deleteNode(String nodeId) {
        String key = NODE_PREFIX + nodeId;
        return storageManager.delete(key);
    }
    
    @Override
    public CompletableFuture<Boolean> deleteNodes(List<String> nodeIds) {
        List<String> keys = nodeIds.stream()
            .map(id -> NODE_PREFIX + id)
            .collect(Collectors.toList());
        return storageManager.deleteAll(keys);
    }
    
    @Override
    public CompletableFuture<Boolean> deleteAllNodes() {
        return loadAllNodes()
            .thenCompose(nodes -> {
                List<String> keys = nodes.stream()
                    .map(node -> NODE_PREFIX + node.getNodeId())
                    .collect(Collectors.toList());
                return storageManager.deleteAll(keys);
            });
    }
    
    @Override
    public CompletableFuture<Boolean> existsNode(String nodeId) {
        String key = NODE_PREFIX + nodeId;
        return storageManager.exists(key);
    }
    
    @Override
    public CompletableFuture<Long> countNodes() {
        return loadAllNodes()
            .thenApply(nodes -> (long) nodes.size());
    }
    
    @Override
    public CompletableFuture<Long> countNodesByType(String nodeType) {
        return loadNodesByType(nodeType)
            .thenApply(nodes -> (long) nodes.size());
    }
    
    @Override
    public CompletableFuture<Long> countNodesByStatus(String status) {
        return loadNodesByStatus(status)
            .thenApply(nodes -> (long) nodes.size());
    }
    
    // 网络链路相关方法
    @Override
    public CompletableFuture<Boolean> saveLink(NetworkLink link) {
        String key = LINK_PREFIX + link.getLinkId();
        return storageManager.save(key, link);
    }
    
    @Override
    public CompletableFuture<Boolean> saveLinks(List<NetworkLink> links) {
        Map<String, NetworkLink> entries = new HashMap<>();
        for (NetworkLink link : links) {
            String key = LINK_PREFIX + link.getLinkId();
            entries.put(key, link);
        }
        return storageManager.saveAll(entries);
    }
    
    @Override
    public CompletableFuture<NetworkLink> loadLink(String linkId) {
        String key = LINK_PREFIX + linkId;
        return storageManager.load(key, NetworkLink.class);
    }
    
    @Override
    public CompletableFuture<List<NetworkLink>> loadAllLinks() {
        return storageManager.loadAll(NetworkLink.class)
            .thenApply(map -> new ArrayList<>(map.values()));
    }
    
    @Override
    public CompletableFuture<List<NetworkLink>> loadLinksBySource(String sourceNodeId) {
        return loadAllLinks()
            .thenApply(links -> links.stream()
                .filter(link -> sourceNodeId.equals(link.getSourceNodeId()))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<List<NetworkLink>> loadLinksByDestination(String destinationNodeId) {
        return loadAllLinks()
            .thenApply(links -> links.stream()
                .filter(link -> destinationNodeId.equals(link.getDestinationNodeId()))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<List<NetworkLink>> loadLinksByStatus(String status) {
        return loadAllLinks()
            .thenApply(links -> links.stream()
                .filter(link -> status.equals(link.getStatus().toString()))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<Boolean> deleteLink(String linkId) {
        String key = LINK_PREFIX + linkId;
        return storageManager.delete(key);
    }
    
    @Override
    public CompletableFuture<Boolean> deleteLinks(List<String> linkIds) {
        List<String> keys = linkIds.stream()
            .map(id -> LINK_PREFIX + id)
            .collect(Collectors.toList());
        return storageManager.deleteAll(keys);
    }
    
    @Override
    public CompletableFuture<Boolean> deleteAllLinks() {
        return loadAllLinks()
            .thenCompose(links -> {
                List<String> keys = links.stream()
                    .map(link -> LINK_PREFIX + link.getLinkId())
                    .collect(Collectors.toList());
                return storageManager.deleteAll(keys);
            });
    }
    
    @Override
    public CompletableFuture<Boolean> existsLink(String linkId) {
        String key = LINK_PREFIX + linkId;
        return storageManager.exists(key);
    }
    
    @Override
    public CompletableFuture<Long> countLinks() {
        return loadAllLinks()
            .thenApply(links -> (long) links.size());
    }
    
    @Override
    public CompletableFuture<Long> countLinksByStatus(String status) {
        return loadLinksByStatus(status)
            .thenApply(links -> (long) links.size());
    }
    
    // 拓扑查询方法
    @Override
    public CompletableFuture<List<NetworkNode>> searchNodes(Map<String, Object> criteria) {
        return loadAllNodes()
            .thenApply(nodes -> nodes.stream()
                .filter(node -> matchesNodeCriteria(node, criteria))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<List<NetworkLink>> searchLinks(Map<String, Object> criteria) {
        return loadAllLinks()
            .thenApply(links -> links.stream()
                .filter(link -> matchesLinkCriteria(link, criteria))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<NetworkLink> findLink(String sourceNodeId, String destinationNodeId) {
        return loadAllLinks()
            .thenApply(links -> links.stream()
                .filter(link -> sourceNodeId.equals(link.getSourceNodeId()) && destinationNodeId.equals(link.getDestinationNodeId()))
                .findFirst()
                .orElse(null));
    }
    
    @Override
    public CompletableFuture<List<NetworkLink>> findLinksBetweenNodes(String sourceNodeId, String destinationNodeId) {
        return loadAllLinks()
            .thenApply(links -> links.stream()
                .filter(link -> (sourceNodeId.equals(link.getSourceNodeId()) && destinationNodeId.equals(link.getDestinationNodeId())) ||
                               (sourceNodeId.equals(link.getDestinationNodeId()) && destinationNodeId.equals(link.getSourceNodeId())))
                .collect(Collectors.toList()));
    }
    
    // 拓扑统计方法
    @Override
    public CompletableFuture<Map<String, Long>> getNodeStatusCounts() {
        return loadAllNodes()
            .thenApply(nodes -> {
                Map<String, Long> counts = new HashMap<>();
                for (NetworkNode node : nodes) {
                    String status = node.getStatus().toString();
                    counts.put(status, counts.getOrDefault(status, 0L) + 1);
                }
                return counts;
            });
    }
    
    @Override
    public CompletableFuture<Map<String, Long>> getNodeTypeCounts() {
        return loadAllNodes()
            .thenApply(nodes -> {
                Map<String, Long> counts = new HashMap<>();
                for (NetworkNode node : nodes) {
                    String type = node.getNodeType().toString();
                    counts.put(type, counts.getOrDefault(type, 0L) + 1);
                }
                return counts;
            });
    }
    
    @Override
    public CompletableFuture<Map<String, Long>> getLinkStatusCounts() {
        return loadAllLinks()
            .thenApply(links -> {
                Map<String, Long> counts = new HashMap<>();
                for (NetworkLink link : links) {
                    String status = link.getStatus().toString();
                    counts.put(status, counts.getOrDefault(status, 0L) + 1);
                }
                return counts;
            });
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> getTopologyStatistics() {
        return CompletableFuture.allOf(
            loadAllNodes(),
            loadAllLinks()
        ).thenCompose(v -> {
            CompletableFuture<List<NetworkNode>> nodesFuture = loadAllNodes();
            CompletableFuture<List<NetworkLink>> linksFuture = loadAllLinks();
            
            return CompletableFuture.allOf(nodesFuture, linksFuture)
                .thenApply(v2 -> {
                    List<NetworkNode> nodes = nodesFuture.join();
                    List<NetworkLink> links = linksFuture.join();
                    
                    Map<String, Object> stats = new HashMap<>();
                    stats.put("totalNodes", nodes.size());
                    stats.put("totalLinks", links.size());
                    
                    Map<String, Long> nodeStatusCounts = new HashMap<>();
                    Map<String, Long> nodeTypeCounts = new HashMap<>();
                    Map<String, Long> linkStatusCounts = new HashMap<>();
                    
                    for (NetworkNode node : nodes) {
                        String status = node.getStatus().toString();
                        nodeStatusCounts.put(status, nodeStatusCounts.getOrDefault(status, 0L) + 1);
                        
                        String type = node.getNodeType().toString();
                        nodeTypeCounts.put(type, nodeTypeCounts.getOrDefault(type, 0L) + 1);
                    }
                    
                    for (NetworkLink link : links) {
                        String status = link.getStatus().toString();
                        linkStatusCounts.put(status, linkStatusCounts.getOrDefault(status, 0L) + 1);
                    }
                    
                    stats.put("nodeStatusCounts", nodeStatusCounts);
                    stats.put("nodeTypeCounts", nodeTypeCounts);
                    stats.put("linkStatusCounts", linkStatusCounts);
                    
                    return stats;
                });
        });
    }
    
    // 拓扑更新方法
    @Override
    public CompletableFuture<Boolean> updateNodeStatus(String nodeId, String status) {
        return loadNode(nodeId)
            .thenCompose(node -> {
                if (node == null) {
                    return CompletableFuture.completedFuture(false);
                }
                try {
                    node.setStatus(net.ooder.sdk.topology.model.NodeStatus.valueOf(status.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return CompletableFuture.completedFuture(false);
                }
                return saveNode(node);
            });
    }
    
    @Override
    public CompletableFuture<Boolean> updateLinkStatus(String linkId, String status) {
        return loadLink(linkId)
            .thenCompose(link -> {
                if (link == null) {
                    return CompletableFuture.completedFuture(false);
                }
                try {
                    link.setStatus(net.ooder.sdk.topology.model.LinkStatus.valueOf(status.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return CompletableFuture.completedFuture(false);
                }
                return saveLink(link);
            });
    }
    
    @Override
    public CompletableFuture<Boolean> updateNodeAttributes(String nodeId, Map<String, Object> attributes) {
        return loadNode(nodeId)
            .thenCompose(node -> {
                if (node == null) {
                    return CompletableFuture.completedFuture(false);
                }
                
                // 更新属性
                if (attributes.containsKey("nodeType")) {
                    try {
                        node.setNodeType(net.ooder.sdk.topology.model.NodeType.valueOf(((String) attributes.get("nodeType")).toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        // 忽略无效的节点类型
                    }
                }
                if (attributes.containsKey("status")) {
                    try {
                        node.setStatus(net.ooder.sdk.topology.model.NodeStatus.valueOf(((String) attributes.get("status")).toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        // 忽略无效的状态值
                    }
                }
                if (attributes.containsKey("position")) {
                    // 假设position是一个包含x和y坐标的Map
                    Map<String, Object> positionMap = (Map<String, Object>) attributes.get("position");
                    if (positionMap != null) {
                        net.ooder.sdk.topology.model.NodePosition position = new net.ooder.sdk.topology.model.NodePosition();
                        if (positionMap.containsKey("x")) {
                            position.setX(((Number) positionMap.get("x")).doubleValue());
                        }
                        if (positionMap.containsKey("y")) {
                            position.setY(((Number) positionMap.get("y")).doubleValue());
                        }
                        node.setPosition(position);
                    }
                }
                if (attributes.containsKey("properties")) {
                    node.setProperties((Map<String, Object>) attributes.get("properties"));
                }
                
                return saveNode(node);
            });
    }
    
    @Override
    public CompletableFuture<Boolean> updateLinkAttributes(String linkId, Map<String, Object> attributes) {
        return loadLink(linkId)
            .thenCompose(link -> {
                if (link == null) {
                    return CompletableFuture.completedFuture(false);
                }
                
                // 更新属性
                if (attributes.containsKey("linkType")) {
                    try {
                        link.setLinkType(net.ooder.sdk.topology.model.LinkType.valueOf(((String) attributes.get("linkType")).toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        // 忽略无效的链路类型
                    }
                }
                if (attributes.containsKey("status")) {
                    try {
                        link.setStatus(net.ooder.sdk.topology.model.LinkStatus.valueOf(((String) attributes.get("status")).toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        // 忽略无效的状态值
                    }
                }
                if (attributes.containsKey("metrics")) {
                    // 假设metrics是一个包含各种度量值的Map
                    Map<String, Object> metricsMap = (Map<String, Object>) attributes.get("metrics");
                    if (metricsMap != null) {
                        net.ooder.sdk.topology.model.LinkMetrics metrics = new net.ooder.sdk.topology.model.LinkMetrics();
                        if (metricsMap.containsKey("bandwidth")) {
                            metrics.setBandwidth(((Number) metricsMap.get("bandwidth")).intValue());
                        }
                        if (metricsMap.containsKey("latency")) {
                            metrics.setLatency(((Number) metricsMap.get("latency")).longValue());
                        }
                        if (metricsMap.containsKey("packetLoss")) {
                            metrics.setPacketLoss(((Number) metricsMap.get("packetLoss")).doubleValue());
                        }
                        if (metricsMap.containsKey("jitter")) {
                            metrics.setJitter(((Number) metricsMap.get("jitter")).longValue());
                        }
                        link.setMetrics(metrics);
                    }
                }
                if (attributes.containsKey("properties")) {
                    link.setProperties((Map<String, Object>) attributes.get("properties"));
                }
                
                return saveLink(link);
            });
    }
    
    // 辅助方法：检查节点是否匹配查询条件
    private boolean matchesNodeCriteria(NetworkNode node, Map<String, Object> criteria) {
        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            switch (key) {
                case "nodeId":
                    if (!node.getNodeId().equals(value)) {
                        return false;
                    }
                    break;
                case "nodeType":
                    if (!node.getNodeType().toString().equals(value)) {
                        return false;
                    }
                    break;
                case "status":
                    if (!node.getStatus().toString().equals(value)) {
                        return false;
                    }
                    break;
                // 可以添加更多属性的匹配逻辑
            }
        }
        return true;
    }
    
    // 辅助方法：检查链路是否匹配查询条件
    private boolean matchesLinkCriteria(NetworkLink link, Map<String, Object> criteria) {
        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            switch (key) {
                case "linkId":
                    if (!link.getLinkId().equals(value)) {
                        return false;
                    }
                    break;
                case "sourceNodeId":
                    if (!link.getSourceNodeId().equals(value)) {
                        return false;
                    }
                    break;
                case "targetNodeId":
                    if (!link.getDestinationNodeId().equals(value)) {
                        return false;
                    }
                    break;
                case "linkType":
                    if (!link.getLinkType().toString().equals(value)) {
                        return false;
                    }
                    break;
                case "status":
                    if (!link.getStatus().toString().equals(value)) {
                        return false;
                    }
                    break;
                // 可以添加更多属性的匹配逻辑
            }
        }
        return true;
    }
}
