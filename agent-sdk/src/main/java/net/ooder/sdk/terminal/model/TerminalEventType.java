package net.ooder.sdk.terminal.model;

public enum TerminalEventType {
    DEVICE_DISCOVERED("device.discovered", "设备发现"),
    DEVICE_REGISTERED("device.registered", "设备注册"),
    DEVICE_DEREGISTERED("device.deregistered", "设备注销"),
    DEVICE_ONLINE("device.online", "设备上线"),
    DEVICE_OFFLINE("device.offline", "设备下线"),
    DEVICE_STATUS_CHANGED("device.status.changed", "设备状态变更"),
    DEVICE_ERROR("device.error", "设备错误"),
    DEVICE_METADATA_UPDATED("device.metadata.updated", "设备元数据更新"),
    DEVICE_CONNECTED("device.connected", "设备连接"),
    DEVICE_DISCONNECTED("device.disconnected", "设备断开"),
    DEVICE_HEARTBEAT("device.heartbeat", "设备心跳");
    
    private final String value;
    private final String description;
    
    TerminalEventType(String value, String description) {
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
