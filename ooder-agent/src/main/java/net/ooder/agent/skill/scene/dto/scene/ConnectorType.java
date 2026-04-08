package net.ooder.mvp.skill.scene.dto.scene;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "connector_type", name = "连接器类型", description = "能力连接器的类型")
public enum ConnectorType implements DictItem {
    
    HTTP("HTTP", "HTTP连接", "HTTP/HTTPS协议连接", "ri-global-line", 1),
    GRPC("GRPC", "gRPC连接", "gRPC协议连接", "ri-git-branch-line", 2),
    WEBSOCKET("WEBSOCKET", "WebSocket连接", "WebSocket协议连接", "ri-plug-line", 3),
    LOCAL_JAR("LOCAL_JAR", "本地JAR", "本地JAR包调用", "ri-archive-line", 4),
    UDP("UDP", "UDP连接", "UDP协议连接", "ri-wifi-line", 5),
    INTERNAL("INTERNAL", "内部调用", "内部方法调用", "ri-cpu-line", 6);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    ConnectorType(String code, String name, String description, String icon, int sort) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.sort = sort;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public int getSort() {
        return sort;
    }
}
