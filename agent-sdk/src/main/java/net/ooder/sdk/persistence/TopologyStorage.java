package net.ooder.sdk.persistence;

import net.ooder.sdk.topology.model.NetworkNode;
import net.ooder.sdk.topology.model.NetworkLink;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface TopologyStorage {
    // 存储网络节点
    CompletableFuture<Boolean> saveNode(NetworkNode node);
    CompletableFuture<Boolean> saveNodes(List<NetworkNode> nodes);
    
    // 加载网络节点
    CompletableFuture<NetworkNode> loadNode(String nodeId);
    CompletableFuture<List<NetworkNode>> loadAllNodes();
    CompletableFuture<List<NetworkNode>> loadNodesByType(String nodeType);
    CompletableFuture<List<NetworkNode>> loadNodesByStatus(String status);
    
    // 删除网络节点
    CompletableFuture<Boolean> deleteNode(String nodeId);
    CompletableFuture<Boolean> deleteNodes(List<String> nodeIds);
    CompletableFuture<Boolean> deleteAllNodes();
    
    // 检查网络节点
    CompletableFuture<Boolean> existsNode(String nodeId);
    CompletableFuture<Long> countNodes();
    CompletableFuture<Long> countNodesByType(String nodeType);
    CompletableFuture<Long> countNodesByStatus(String status);
    
    // 存储网络链路
    CompletableFuture<Boolean> saveLink(NetworkLink link);
    CompletableFuture<Boolean> saveLinks(List<NetworkLink> links);
    
    // 加载网络链路
    CompletableFuture<NetworkLink> loadLink(String linkId);
    CompletableFuture<List<NetworkLink>> loadAllLinks();
    CompletableFuture<List<NetworkLink>> loadLinksBySource(String sourceNodeId);
    CompletableFuture<List<NetworkLink>> loadLinksByDestination(String destinationNodeId);
    CompletableFuture<List<NetworkLink>> loadLinksByStatus(String status);
    
    // 删除网络链路
    CompletableFuture<Boolean> deleteLink(String linkId);
    CompletableFuture<Boolean> deleteLinks(List<String> linkIds);
    CompletableFuture<Boolean> deleteAllLinks();
    
    // 检查网络链路
    CompletableFuture<Boolean> existsLink(String linkId);
    CompletableFuture<Long> countLinks();
    CompletableFuture<Long> countLinksByStatus(String status);
    
    // 拓扑查询
    CompletableFuture<List<NetworkNode>> searchNodes(Map<String, Object> criteria);
    CompletableFuture<List<NetworkLink>> searchLinks(Map<String, Object> criteria);
    CompletableFuture<NetworkLink> findLink(String sourceNodeId, String destinationNodeId);
    CompletableFuture<List<NetworkLink>> findLinksBetweenNodes(String sourceNodeId, String destinationNodeId);
    
    // 拓扑统计
    CompletableFuture<Map<String, Long>> getNodeStatusCounts();
    CompletableFuture<Map<String, Long>> getNodeTypeCounts();
    CompletableFuture<Map<String, Long>> getLinkStatusCounts();
    CompletableFuture<Map<String, Object>> getTopologyStatistics();
    
    // 拓扑更新
    CompletableFuture<Boolean> updateNodeStatus(String nodeId, String status);
    CompletableFuture<Boolean> updateLinkStatus(String linkId, String status);
    CompletableFuture<Boolean> updateNodeAttributes(String nodeId, Map<String, Object> attributes);
    CompletableFuture<Boolean> updateLinkAttributes(String linkId, Map<String, Object> attributes);
}
