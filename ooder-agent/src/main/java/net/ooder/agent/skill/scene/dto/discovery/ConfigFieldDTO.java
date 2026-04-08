package net.ooder.mvp.skill.scene.dto.discovery;

public class ConfigFieldDTO {
    
    private String name;
    private String label;
    private String type;
    private String placeholder;
    
    public ConfigFieldDTO() {
    }
    
    public ConfigFieldDTO(String name, String label, String type, String placeholder) {
        this.name = name;
        this.label = label;
        this.type = type;
        this.placeholder = placeholder;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getPlaceholder() { return placeholder; }
    public void setPlaceholder(String placeholder) { this.placeholder = placeholder; }
}
