package net.ooder.skill.discovery.model;

public enum SkillDirectory {
    
    SYSTEM("_system", "系统级服务", "ri-server-line", "#64748b"),
    DRIVERS("_drivers", "驱动适配器", "ri-steering-line", "#52c41a"),
    BUSINESS("_business", "业务服务", "ri-briefcase-line", "#f59e0b"),
    CAPABILITIES("capabilities", "能力组件", "ri-cpu-line", "#1890ff"),
    TOOLS("tools", "工具服务", "ri-tools-line", "#8b5cf6"),
    SCENES("scenes", "场景应用", "ri-layout-grid-line", "#722ed1");
    
    private final String code;
    private final String displayName;
    private final String icon;
    private final String color;
    
    SkillDirectory(String code, String displayName, String icon, String color) {
        this.code = code;
        this.displayName = displayName;
        this.icon = icon;
        this.color = color;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public String getColor() {
        return color;
    }
    
    public static SkillDirectory fromCode(String code) {
        if (code == null) {
            return BUSINESS;
        }
        for (SkillDirectory dir : values()) {
            if (dir.code.equalsIgnoreCase(code)) {
                return dir;
            }
        }
        return BUSINESS;
    }
    
    public static SkillDirectory[] getDisplayOrder() {
        return new SkillDirectory[]{SYSTEM, DRIVERS, BUSINESS, CAPABILITIES, TOOLS, SCENES};
    }
}
