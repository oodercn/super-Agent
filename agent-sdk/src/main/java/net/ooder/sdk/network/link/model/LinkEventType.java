package net.ooder.sdk.network.link.model;

public enum LinkEventType {
    LINK_CREATED("link.created", "链路创建"),
    LINK_DELETED("link.deleted", "链路删除"),
    LINK_STATUS_CHANGED("link.status.changed", "链路状态变更"),
    LINK_QUALITY_CHANGED("link.quality.changed", "链路质量变更"),
    LINK_METRICS_UPDATED("link.metrics.updated", "链路指标更新"),
    LINK_PERFORMANCE_DEGRADED("link.performance.degraded", "链路性能降级"),
    LINK_PERFORMANCE_IMPROVED("link.performance.improved", "链路性能改善"),
    LINK_BANDWIDTH_CHANGED("link.bandwidth.changed", "链路带宽变更"),
    LINK_LATENCY_CHANGED("link.latency.changed", "链路延迟变更"),
    LINK_PACKET_LOSS_CHANGED("link.packet_loss.changed", "链路丢包率变更"),
    LINK_UTILIZATION_HIGH("link.utilization.high", "链路利用率高"),
    LINK_ERROR_RATE_HIGH("link.error_rate.high", "链路错误率高"),
    LINK_UP("link.up", "链路上线"),
    LINK_DOWN("link.down", "链路下线"),
    LINK_FLAPPING("link.flapping", "链路频繁波动"),
    LINK_SATURATED("link.saturated", "链路饱和"),
    LINK_RESTORED("link.restored", "链路恢复"),
    LINK_MONITORING_STARTED("link.monitoring.started", "链路监控开始"),
    LINK_MONITORING_STOPPED("link.monitoring.stopped", "链路监控停止");
    
    private final String value;
    private final String description;
    
    LinkEventType(String value, String description) {
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
