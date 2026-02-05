package net.ooder.sdk.topology.model;

public enum TopologyEventType {
    TOPOLOGY_BUILT("topology.built", "拓扑构建完成"),
    TOPOLOGY_REBUILT("topology.rebuilt", "拓扑重建完成"),
    TOPOLOGY_CHANGED("topology.changed", "拓扑发生变化"),
    NODE_ADDED("node.added", "节点添加"),
    NODE_REMOVED("node.removed", "节点移除"),
    NODE_UPDATED("node.updated", "节点更新"),
    NODE_STATUS_CHANGED("node.status.changed", "节点状态变更"),
    LINK_ADDED("link.added", "链路添加"),
    LINK_REMOVED("link.removed", "链路移除"),
    LINK_UPDATED("link.updated", "链路更新"),
    LINK_STATUS_CHANGED("link.status.changed", "链路状态变更"),
    TOPOLOGY_HEALTH_DEGRADED("topology.health.degraded", "拓扑健康状态降级"),
    TOPOLOGY_OPTIMIZED("topology.optimized", "拓扑优化"),
    BOTTLENECK_DETECTED("bottleneck.detected", "检测到瓶颈"),
    SINGLE_POINT_OF_FAILURE_DETECTED("spof.detected", "检测到单点故障");
    
    private final String value;
    private final String description;
    
    TopologyEventType(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return value;
    }
}
