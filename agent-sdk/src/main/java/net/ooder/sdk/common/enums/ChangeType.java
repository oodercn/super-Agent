
package net.ooder.sdk.common.enums;

public enum ChangeType {
    SKILL_INSTALL("skill_install", "Skill installed"),
    SKILL_UNINSTALL("skill_uninstall", "Skill uninstalled"),
    SKILL_UPDATE("skill_update", "Skill updated"),
    SCENE_JOIN("scene_join", "Agent joined scene"),
    SCENE_LEAVE("scene_leave", "Agent left scene"),
    FAILOVER("failover", "Failover occurred"),
    ROLE_CHANGE("role_change", "Member role changed"),
    CONFIG_UPDATE("config_update", "Configuration updated"),
    CAP_REGISTER("cap_register", "Capability registered");
    
    private final String code;
    private final String description;
    
    ChangeType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static ChangeType fromCode(String code) {
        for (ChangeType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown change type: " + code);
    }
}
