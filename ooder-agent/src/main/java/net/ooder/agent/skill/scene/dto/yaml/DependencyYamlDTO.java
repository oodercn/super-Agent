package net.ooder.mvp.skill.scene.dto.yaml;

public class DependencyYamlDTO {
    
    private String id;
    private String version;
    private Boolean required;
    private Boolean autoInstall;
    private String description;
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public Boolean getRequired() { return required; }
    public void setRequired(Boolean required) { this.required = required; }
    
    public Boolean getAutoInstall() { return autoInstall; }
    public void setAutoInstall(Boolean autoInstall) { this.autoInstall = autoInstall; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
