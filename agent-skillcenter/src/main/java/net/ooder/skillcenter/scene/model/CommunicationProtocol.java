package net.ooder.skillcenter.scene.model;

/**
 * 通信协议枚举 - 符合v0.7.0协议规范
 */
public enum CommunicationProtocol {
    
    HTTP("http", "HTTP协议"),
    WEBSOCKET("websocket", "WebSocket协议"),
    GRPC("grpc", "gRPC协议"),
    MQTT("mqtt", "MQTT协议");
    
    private final String value;
    private final String description;
    
    CommunicationProtocol(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public String getValue() { return value; }
    public String getDescription() { return description; }
    
    public static CommunicationProtocol fromValue(String value) {
        if (value == null) return null;
        for (CommunicationProtocol protocol : values()) {
            if (protocol.value.equals(value)) {
                return protocol;
            }
        }
        return null;
    }
}
