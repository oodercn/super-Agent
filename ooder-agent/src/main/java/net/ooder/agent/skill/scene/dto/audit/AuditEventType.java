package net.ooder.mvp.skill.scene.dto.audit;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "audit_event_type", name = "审计事件类型", description = "系统审计事件的分类类型")
public enum AuditEventType implements DictItem {
    
    KEY_MANAGEMENT("KEY_MANAGEMENT", "密钥管理", "密钥创建、删除、更新等操作", "ri-key-line", 1),
    KEY_ACCESS("KEY_ACCESS", "密钥访问", "密钥查询、使用等操作", "ri-key-2-line", 2),
    
    PERMISSION_CHANGE("PERMISSION_CHANGE", "权限变更", "权限授予、撤销等操作", "ri-shield-user-line", 10),
    PERMISSION_CHECK("PERMISSION_CHECK", "权限检查", "权限验证操作", "ri-shield-check-line", 11),
    
    AGENT_INVOKE("AGENT_INVOKE", "Agent调用", "Agent能力调用操作", "ri-robot-line", 20),
    AGENT_CREATE("AGENT_CREATE", "Agent创建", "Agent实例创建操作", "ri-robot-2-line", 21),
    AGENT_DELETE("AGENT_DELETE", "Agent删除", "Agent实例删除操作", "ri-delete-bin-line", 22),
    
    SCENE_CREATE("SCENE_CREATE", "场景创建", "场景实例创建操作", "ri-artboard-line", 30),
    SCENE_START("SCENE_START", "场景启动", "场景启动执行操作", "ri-play-line", 31),
    SCENE_STOP("SCENE_STOP", "场景停止", "场景停止执行操作", "ri-stop-line", 32),
    SCENE_DELETE("SCENE_DELETE", "场景删除", "场景实例删除操作", "ri-delete-bin-line", 33),
    
    CAPABILITY_INVOKE("CAPABILITY_INVOKE", "能力调用", "能力执行调用操作", "ri-flashlight-line", 40),
    CAPABILITY_BIND("CAPABILITY_BIND", "能力绑定", "能力绑定到场景组操作", "ri-link", 41),
    CAPABILITY_UNBIND("CAPABILITY_UNBIND", "能力解绑", "能力从场景组解绑操作", "ri-link-unlink", 42),
    
    LLM_CALL("LLM_CALL", "LLM调用", "大语言模型调用操作", "ri-brain-line", 50),
    LLM_RESPONSE("LLM_RESPONSE", "LLM响应", "大语言模型响应操作", "ri-message-2-line", 51),
    
    USER_LOGIN("USER_LOGIN", "用户登录", "用户登录系统操作", "ri-login-box-line", 60),
    USER_LOGOUT("USER_LOGOUT", "用户登出", "用户登出系统操作", "ri-logout-box-line", 61),
    
    CONFIG_CHANGE("CONFIG_CHANGE", "配置变更", "系统配置变更操作", "ri-settings-3-line", 70),
    CONFIG_DB("CONFIG_DB", "数据库配置", "数据库连接配置变更", "ri-database-2-line", 71),
    CONFIG_LLM("CONFIG_LLM", "LLM配置", "大语言模型配置变更", "ri-robot-line", 72),
    CONFIG_ORG("CONFIG_ORG", "组织配置", "组织管理配置变更", "ri-team-line", 73),
    CONFIG_AUTH("CONFIG_AUTH", "认证配置", "认证授权配置变更", "ri-shield-line", 74),
    CONFIG_VFS("CONFIG_VFS", "存储配置", "文件系统配置变更", "ri-folder-line", 75),
    CONFIG_COMM("CONFIG_COMM", "通信配置", "消息通知配置变更", "ri-message-line", 76),
    CONFIG_KNOWLEDGE("CONFIG_KNOWLEDGE", "知识库配置", "知识库配置变更", "ri-book-line", 77),
    
    SECURITY_EVENT("SECURITY_EVENT", "安全事件", "安全相关事件", "ri-shield-line", 80),
    
    DATA_ACCESS("DATA_ACCESS", "数据访问", "数据读取操作", "ri-database-2-line", 90),
    DATA_MODIFY("DATA_MODIFY", "数据修改", "数据写入操作", "ri-edit-line", 91);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    AuditEventType(String code, String name, String description, String icon, int sort) {
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
