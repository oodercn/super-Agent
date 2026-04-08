package net.ooder.mvp.skill.scene.capability.model;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "capability_type", name = "能力类型", description = "能力的分类类型 v2.3")
public enum CapabilityType implements DictItem {
    
    ATOMIC("ATOMIC", "原子能力", "单一功能，不可分解", "ri-flashlight-line", 1),
    COMPOSITE("COMPOSITE", "组合能力", "组合多个原子能力", "ri-links-line", 2),
    SCENE("SCENE", "场景能力", "自驱型SuperAgent能力，可涌现新行为", "ri-layout-grid-line", 3),
    DRIVER("DRIVER", "驱动能力", "意图/时间/事件驱动能力", "ri-timer-line", 4),
    COLLABORATIVE("COLLABORATIVE", "协作能力", "跨场景协作能力", "ri-team-line", 5),
    
    SERVICE("SERVICE", "服务能力", "业务服务、API服务", "ri-server-line", 10),
    AI("AI", "AI能力", "LLM、机器学习", "ri-brain-line", 11),
    TOOL("TOOL", "工具能力", "工具类功能", "ri-tools-line", 12),
    CONNECTOR("CONNECTOR", "连接器能力", "连接协议类", "ri-plug-line", 13),
    DATA("DATA", "数据能力", "数据存储、处理", "ri-database-2-line", 14),
    MANAGEMENT("MANAGEMENT", "管理能力", "配置管理、监控管理", "ri-settings-3-line", 15),
    COMMUNICATION("COMMUNICATION", "通信能力", "消息、通知", "ri-message-3-line", 16),
    SECURITY("SECURITY", "安全能力", "认证、加密", "ri-shield-check-line", 17),
    MONITORING("MONITORING", "监控能力", "日志、指标", "ri-pulse-line", 18),
    
    SKILL("SKILL", "技能能力", "可安装的技能包", "ri-flashlight-line", 20),
    SCENE_GROUP("SCENE_GROUP", "场景组能力", "场景组作为能力", "ri-layout-grid-line", 21),
    CAPABILITY_CHAIN("CAPABILITY_CHAIN", "能力链能力", "能力调用链", "ri-link", 22),
    CUSTOM("CUSTOM", "自定义能力", "用户自定义能力", "ri-tools-line", 99);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;
    private final CapabilityCategory category;

    CapabilityType(String code, String name, String description, String icon, int sort) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.sort = sort;
        this.category = determineCategory(sort);
    }

    private static CapabilityCategory determineCategory(int sort) {
        if (sort >= 1 && sort <= 5) {
            return CapabilityCategory.CORE;
        } else if (sort >= 10 && sort <= 18) {
            return CapabilityCategory.BUSINESS;
        } else if (sort >= 20 && sort <= 22) {
            return CapabilityCategory.SYSTEM;
        } else {
            return CapabilityCategory.CUSTOM;
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public int getSort() {
        return sort;
    }

    public CapabilityCategory getCategory() {
        return category;
    }

    public boolean isCoreType() {
        return category == CapabilityCategory.CORE;
    }

    public boolean isBusinessType() {
        return category == CapabilityCategory.BUSINESS;
    }

    public boolean isSystemType() {
        return category == CapabilityCategory.SYSTEM;
    }

    public boolean isSelfDriven() {
        return this == SCENE || this == DRIVER;
    }

    public boolean canBeComposed() {
        return this == ATOMIC || this == SERVICE || this == TOOL;
    }

    public enum CapabilityCategory {
        CORE("核心能力类型", "能力驱动架构核心类型"),
        BUSINESS("业务能力类型", "业务场景能力类型"),
        SYSTEM("系统能力类型", "系统级能力类型"),
        CUSTOM("自定义能力类型", "用户自定义能力类型");

        private final String name;
        private final String description;

        CapabilityCategory(String name, String description) {
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
}
