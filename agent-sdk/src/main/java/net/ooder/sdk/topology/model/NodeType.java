package net.ooder.sdk.topology.model;

public enum NodeType {
    END_AGENT("end_agent", "终端代理"),
    ROUTE_AGENT("route_agent", "路由代理"),
    MCP_AGENT("mcp_agent", "主控代理"),
    SWITCH("switch", "交换机"),
    ROUTER("router", "路由器"),
    GATEWAY("gateway", "网关"),
    SERVER("server", "服务器"),
    CLIENT("client", "客户端"),
    SENSOR("sensor", "传感器"),
    ACTUATOR("actuator", "执行器"),
    UNKNOWN("unknown", "未知");
    
    private final String value;
    private final String description;
    
    NodeType(String value, String description) {
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
