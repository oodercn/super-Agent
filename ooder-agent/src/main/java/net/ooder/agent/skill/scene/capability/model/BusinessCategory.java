package net.ooder.mvp.skill.scene.capability.model;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "business_category", name = "业务领域", description = "技能的业务领域分类")
public enum BusinessCategory implements DictItem {

    OFFICE_COLLABORATION("OFFICE_COLLABORATION", "办公协作", "团队协作、日志、会议、审批", "ri-team-line", 1),
    HUMAN_RESOURCE("HUMAN_RESOURCE", "人力资源", "招聘、绩效、培训、员工管理", "ri-user-add-line", 2),
    AI_ASSISTANT("AI_ASSISTANT", "AI助手", "AI对话、知识问答、智能客服", "ri-robot-line", 3),
    DATA_PROCESSING("DATA_PROCESSING", "数据处理", "报表、分析、同步、可视化", "ri-bar-chart-line", 4),
    MARKETING_OPERATIONS("MARKETING_OPERATIONS", "营销运营", "内容发布、社媒管理、活动", "ri-megaphone-line", 5),
    SYSTEM_TOOLS("SYSTEM_TOOLS", "系统工具", "存储、通知、定时任务、备份", "ri-tools-line", 6),
    SYSTEM_MONITOR("SYSTEM_MONITOR", "系统监控", "监控告警、日志收集、健康检查", "ri-pulse-line", 7),
    SECURITY_AUDIT("SECURITY_AUDIT", "安全审计", "访问控制、审计日志、安全检测", "ri-shield-check-line", 8),
    INFRASTRUCTURE("INFRASTRUCTURE", "基础设施", "调度服务、网络服务、认证服务", "ri-server-line", 9),
    FINANCE_ACCOUNTING("FINANCE_ACCOUNTING", "财务会计", "财务报表、账务处理、成本管理", "ri-money-cny-box-line", 10);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    BusinessCategory(String code, String name, String description, String icon, int sort) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.sort = sort;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    @Override
    public String getCode() { return code; }

    @Override
    public String getIcon() { return icon; }

    @Override
    public int getSort() { return sort; }

    public static BusinessCategory fromCode(String code) {
        if (code == null) return null;
        for (BusinessCategory cat : values()) {
            if (cat.code.equalsIgnoreCase(code)) {
                return cat;
            }
        }
        return null;
    }
    
    public boolean isInternal() {
        return this == SYSTEM_MONITOR || this == SECURITY_AUDIT || this == INFRASTRUCTURE;
    }
    
    public boolean isPublic() {
        return !isInternal();
    }
}
