package net.ooder.agent.metadata.enums;

/**
 * Agent类型枚举
 * 支持endAgent、routeAgent、mapAgent等
 */
public enum AgentTypeEnum {
    END_AGENT("endAgent", "终端代理"),
    ROUTE_AGENT("routeAgent", "路由代理"),
    MAP_AGENT("mapAgent", "地图代理"),
    MCP_AGENT("mcpAgent", "主控代理"),
    SKILL_AGENT("skillAgent", "技能代理");

    private String code;
    private String description;

    AgentTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static AgentTypeEnum fromCode(String code) {
        for (AgentTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}