package net.ooder.agent.metadata.enums;

/**
 * 命令类型枚举
 */
public enum CommandTypeEnum {
    // 系统命令
    SYSTEM_PING("system.ping", "系统心跳"),
    SYSTEM_INFO("system.info", "系统信息"),
    SYSTEM_LOG("system.log", "系统日志"),
    
    // Agent命令
    AGENT_REGISTER("agent.register", "Agent注册"),
    AGENT_DEREGISTER("agent.deregister", "Agent注销"),
    AGENT_STATUS("agent.status", "Agent状态查询"),
    AGENT_UPDATE("agent.update", "Agent信息更新"),
    AGENT_LIST("agent.list", "Agent列表查询"),
    
    // Skill命令
    SKILL_DISCOVER("skill.discover", "Skill发现"),
    SKILL_INVOKE("skill.invoke", "Skill调用"),
    SKILL_REGISTER("skill.register", "Skill注册"),
    SKILL_DEREGISTER("skill.deregister", "Skill注销"),
    
    // 场景命令
    SCENE_CREATE("scene.create", "场景创建"),
    SCENE_UPDATE("scene.update", "场景更新"),
    SCENE_DELETE("scene.delete", "场景删除"),
    SCENE_JOIN("scene.join", "加入场景"),
    SCENE_LEAVE("scene.leave", "离开场景"),
    SCENE_LIST("scene.list", "场景列表"),
    SCENE_INFO("scene.info", "场景信息查询"),
    
    // 组命令
    GROUP_CREATE("group.create", "组创建"),
    GROUP_UPDATE("group.update", "组更新"),
    GROUP_DELETE("group.delete", "组删除"),
    GROUP_ADD_MEMBER("group.add_member", "添加组成员"),
    GROUP_REMOVE_MEMBER("group.remove_member", "移除组成员"),
    GROUP_LIST("group.list", "组列表"),
    GROUP_INFO("group.info", "组信息查询"),
    
    // MCP透传命令
    MCP_PASSTHROUGH("mcp.passthrough", "MCP透传命令"),
    
    // SkillFlow命令
    SKILLFLOW_EXECUTE("skillflow.execute", "SkillFlow执行"),
    SKILLFLOW_STATUS("skillflow.status", "SkillFlow状态查询"),
    SKILLFLOW_CANCEL("skillflow.cancel", "SkillFlow取消");
    
    private String code;
    private String description;
    
    CommandTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static CommandTypeEnum fromCode(String code) {
        for (CommandTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
