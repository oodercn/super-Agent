
package net.ooder.sdk.common.enums;

public enum MemberRole {
    PRIMARY("primary", "Primary agent with full control"),
    BACKUP("backup", "Backup agent ready for failover"),
    OBSERVER("observer", "Observer agent with read-only access");
    
    private final String code;
    private final String description;
    
    MemberRole(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isPrimary() {
        return this == PRIMARY;
    }
    
    public boolean isBackup() {
        return this == BACKUP;
    }
    
    public static MemberRole fromCode(String code) {
        for (MemberRole role : values()) {
            if (role.code.equalsIgnoreCase(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown member role: " + code);
    }
}
