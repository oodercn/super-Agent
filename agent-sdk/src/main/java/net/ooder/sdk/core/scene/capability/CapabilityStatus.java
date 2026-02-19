
package net.ooder.sdk.core.scene.capability;

public enum CapabilityStatus {
    
    AVAILABLE("available", "Capability is available for use"),
    BUSY("busy", "Capability is currently busy"),
    UNAVAILABLE("unavailable", "Capability is not available"),
    DEPRECATED("deprecated", "Capability is deprecated"),
    ERROR("error", "Capability is in error state");
    
    private final String code;
    private final String description;
    
    CapabilityStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() { return code; }
    public String getDescription() { return description; }
}
