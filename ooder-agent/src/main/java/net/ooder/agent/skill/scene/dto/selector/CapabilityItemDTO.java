package net.ooder.mvp.skill.scene.dto.selector;

public class CapabilityItemDTO {
    
    private String id;
    private String name;
    private String description;
    private String type;
    private String status;
    
    public CapabilityItemDTO() {
    }
    
    public CapabilityItemDTO(String id, String name, String description, String type, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.status = status;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
