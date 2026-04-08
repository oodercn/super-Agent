package net.ooder.mvp.skill.scene.dto.knowledge;

public class KbBindingDTO {
    
    private String kbId;
    private String type;
    private String id;
    private String name;
    private String purpose;
    private String layer;
    private Integer priority;
    
    public KbBindingDTO() {
    }
    
    public String getKbId() { return kbId; }
    public void setKbId(String kbId) { this.kbId = kbId; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    
    public String getLayer() { return layer; }
    public void setLayer(String layer) { this.layer = layer; }
    
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
}
