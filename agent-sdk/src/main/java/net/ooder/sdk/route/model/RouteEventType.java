package net.ooder.sdk.route.model;

public enum RouteEventType {
    ROUTE_CREATED("route.created", "路由创建"),
    ROUTE_UPDATED("route.updated", "路由更新"),
    ROUTE_DELETED("route.deleted", "路由删除"),
    ROUTE_STATUS_CHANGED("route.status.changed", "路由状态变更"),
    ROUTE_METRICS_UPDATED("route.metrics.updated", "路由度量更新"),
    ROUTE_QUALITY_DEGRADED("route.quality.degraded", "路由质量降级"),
    ROUTE_FAILED("route.failed", "路由失败"),
    ROUTE_RESTORED("route.restored", "路由恢复"),
    ROUTE_OPTIMIZED("route.optimized", "路由优化"),
    TOPOLOGY_CHANGED("topology.changed", "拓扑变更");
    
    private final String value;
    private final String description;
    
    RouteEventType(String value, String description) {
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
