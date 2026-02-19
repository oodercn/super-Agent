
package net.ooder.sdk.common.enums;

public enum SkillStatus {
    PENDING("pending", "Skill is pending installation"),
    INSTALLING("installing", "Skill is being installed"),
    INSTALLED("installed", "Skill is installed and ready"),
    ACTIVE("active", "Skill is currently active"),
    INACTIVE("inactive", "Skill is installed but inactive"),
    ERROR("error", "Skill has an error"),
    UNINSTALLING("uninstalling", "Skill is being uninstalled"),
    UNINSTALLED("uninstalled", "Skill has been uninstalled");
    
    private final String code;
    private final String description;
    
    SkillStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isReady() {
        return this == INSTALLED || this == ACTIVE;
    }
    
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    public static SkillStatus fromCode(String code) {
        for (SkillStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown skill status: " + code);
    }
}
