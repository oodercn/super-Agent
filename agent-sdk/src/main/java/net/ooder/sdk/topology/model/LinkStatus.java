package net.ooder.sdk.topology.model;

public enum LinkStatus {
    UP("up", "正常"),
    DOWN("down", "故障"),
    TESTING("testing", "测试中"),
    DORMANT("dormant", "休眠"),
    NOTPRESENT("notpresent", "不存在"),
    LOWERLAYERDOWN("lowerlayerdown", "下层故障"),
    UNKNOWN("unknown", "未知");
    
    private final String value;
    private final String description;
    
    LinkStatus(String value, String description) {
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
