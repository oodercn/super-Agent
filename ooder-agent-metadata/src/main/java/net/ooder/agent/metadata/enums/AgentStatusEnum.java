package net.ooder.agent.metadata.enums;

/**
 * Agent状态枚举
 */
public enum AgentStatusEnum {
    PENDING("PENDING", "待加入"),
    JOINED("JOINED", "已加入"),
    CONNECTED("CONNECTED", "已连接"),
    DISCONNECTED("DISCONNECTED", "已断开"),
    LEAVING("LEAVING", "正在离开"),
    LEFT("LEFT", "已离开"),
    ERROR("ERROR", "错误");

    private String code;
    private String description;

    AgentStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static AgentStatusEnum fromCode(String code) {
        for (AgentStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}