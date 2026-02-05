package net.ooder.sdk.route.model;

public enum RouteStatus {
    AVAILABLE("available", "可用"),
    UNAVAILABLE("unavailable", "不可用"),
    PENDING("pending", "待处理"),
    ACTIVE("active", "活跃"),
    INACTIVE("inactive", "非活跃"),
    DEGRADED("degraded", "降级"),
    ERROR("error", "错误");
    
    private final String value;
    private final String description;
    
    RouteStatus(String value, String description) {
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
