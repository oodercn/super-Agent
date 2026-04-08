package net.ooder.mvp.skill.scene.dto.selector;

public class CapabilityTypeDTO {
    
    private String id;
    private String name;
    private String description;
    private int count;
    
    public CapabilityTypeDTO() {
    }
    
    public CapabilityTypeDTO(String id, String name, String description, int count) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.count = count;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}
