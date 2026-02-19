
package net.ooder.sdk.core.scene.capability;

public enum CapabilityType {
    
    COMPUTATION("computation", "Computational capability"),
    STORAGE("storage", "Storage capability"),
    COMMUNICATION("communication", "Communication capability"),
    SENSING("sensing", "Sensing capability"),
    ACTUATION("actuation", "Actuation capability"),
    AI("ai", "AI/ML capability"),
    DATA_PROCESSING("data_processing", "Data processing capability"),
    EXTERNAL("external", "External service capability");
    
    private final String code;
    private final String description;
    
    CapabilityType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() { return code; }
    public String getDescription() { return description; }
}
