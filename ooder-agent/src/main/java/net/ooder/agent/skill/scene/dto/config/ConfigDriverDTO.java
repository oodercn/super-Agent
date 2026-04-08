package net.ooder.mvp.skill.scene.dto.config;

public class ConfigDriverDTO {
    
    private String id;
    private String name;
    private String category;
    private String version;
    private Boolean active;
    private String status;
    
    public ConfigDriverDTO() {
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
