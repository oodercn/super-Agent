package net.ooder.sdk.capability.model;

public class Output {
    
    private String name;
    private ParameterType type;
    private String description;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public ParameterType getType() { return type; }
    public void setType(ParameterType type) { this.type = type; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
