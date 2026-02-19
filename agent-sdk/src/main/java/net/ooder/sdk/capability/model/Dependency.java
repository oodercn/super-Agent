package net.ooder.sdk.capability.model;

public class Dependency {
    
    private String dependencyId;
    private String name;
    private String versionRange;
    private boolean required;
    private DependencyType type;
    
    public String getDependencyId() { return dependencyId; }
    public void setDependencyId(String dependencyId) { this.dependencyId = dependencyId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getVersionRange() { return versionRange; }
    public void setVersionRange(String versionRange) { this.versionRange = versionRange; }
    
    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }
    
    public DependencyType getType() { return type; }
    public void setType(DependencyType type) { this.type = type; }
}
