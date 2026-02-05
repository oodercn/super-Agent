package net.ooder.sdk.terminal.model;

public enum TerminalStatus {
    ONLINE("online", "在线"),
    OFFLINE("offline", "离线"),
    CONNECTING("connecting", "连接中"),
    DISCONNECTING("disconnecting", "断开中"),
    ERROR("error", "错误"),
    MAINTENANCE("maintenance", "维护中");
    
    private final String value;
    private final String description;
    
    TerminalStatus(String value, String description) {
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
