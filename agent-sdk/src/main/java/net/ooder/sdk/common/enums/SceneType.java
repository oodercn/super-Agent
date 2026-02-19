
package net.ooder.sdk.common.enums;

public enum SceneType {
    PRIMARY("primary", "Primary scene with core functionality"),
    COLLABORATIVE("collaborative", "Collaborative scene as dependency");
    
    private final String code;
    private final String description;
    
    SceneType(String code, String description) {
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
    
    public boolean isCollaborative() {
        return this == COLLABORATIVE;
    }
    
    public static SceneType fromCode(String code) {
        for (SceneType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown scene type: " + code);
    }
}
