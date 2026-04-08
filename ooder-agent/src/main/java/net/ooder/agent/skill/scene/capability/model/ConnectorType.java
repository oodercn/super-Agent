package net.ooder.mvp.skill.scene.capability.model;

public enum ConnectorType {
    HTTP("HTTP/HTTPS", "标准HTTP协议"),
    GRPC("gRPC", "高性能RPC协议"),
    WEBSOCKET("WebSocket", "双向通信协议"),
    LOCAL_JAR("本地JAR", "本地Java调用"),
    UDP("UDP", "UDP协议"),
    INTERNAL("内部调用", "进程内调用");

    private final String name;
    private final String description;

    ConnectorType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
