package net.ooder.sdk.topology;

import net.ooder.sdk.topology.model.*;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface TopologyManager {
    // 拓扑构建相关方法
    void buildTopology();
    void rebuildTopology();
    boolean isTopologyBuilt();
    
    // 节点管理相关方法
    NetworkNode addNode(NetworkNode node);
    void removeNode(String nodeId);
    NetworkNode updateNode(String nodeId, Map<String, Object> updates);
    NetworkNode getNode(String nodeId);
    List<NetworkNode> getAllNodes();
    List<NetworkNode> getNodesByType(NodeType nodeType);
    List<NetworkNode> getNodesByStatus(NodeStatus status);
    
    // 链路管理相关方法
    NetworkLink addLink(NetworkLink link);
    void removeLink(String linkId);
    NetworkLink updateLink(String linkId, Map<String, Object> updates);
    NetworkLink getLink(String linkId);
    List<NetworkLink> getAllLinks();
    List<NetworkLink> getLinksByNode(String nodeId);
    List<NetworkLink> getLinksByType(LinkType linkType);
    List<NetworkLink> getLinksByStatus(LinkStatus status);
    
    // 拓扑查询相关方法
    List<NetworkNode> getNeighbors(String nodeId);
    List<NetworkLink> getPaths(String sourceNodeId, String destinationNodeId);
    Map<String, Object> getTopologyStatistics();
    String getTopologyGraph();
    
    // 拓扑分析相关方法
    Map<String, Object> analyzeTopology();
    Map<String, Object> analyzeNode(String nodeId);
    Map<String, Object> analyzeLink(String linkId);
    List<String> findBottlenecks();
    List<String> findSinglePointsOfFailure();
    
    // 拓扑变化处理相关方法
    void handleNodeChange(String nodeId, TopologyEvent event);
    void handleLinkChange(String linkId, TopologyEvent event);
    void handleTopologyChange();
    
    // 拓扑事件相关方法
    void publishTopologyEvent(TopologyEvent event);
    void subscribeToTopologyEvents(Consumer<TopologyEvent> eventHandler);
    void unsubscribeFromTopologyEvents(Consumer<TopologyEvent> eventHandler);
    List<TopologyEvent> getRecentTopologyEvents(int limit);
    
    // 拓扑监控相关方法
    void startTopologyMonitoring();
    void stopTopologyMonitoring();
    Map<String, Object> getTopologyHealth();
    Map<String, Object> getNodeHealth(String nodeId);
    Map<String, Object> getLinkHealth(String linkId);
    
    // 拓扑持久化相关方法
    void saveTopology(String path);
    void loadTopology(String path);
}
