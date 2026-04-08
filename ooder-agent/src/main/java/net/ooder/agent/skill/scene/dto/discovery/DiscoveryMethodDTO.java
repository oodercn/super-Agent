package net.ooder.mvp.skill.scene.dto.discovery;

import java.util.List;

public class DiscoveryMethodDTO {
    
    private String id;
    private String name;
    private String icon;
    private String desc;
    private String color;
    private boolean requiresConfig;
    private List<ConfigFieldDTO> configFields;
    
    public DiscoveryMethodDTO() {
    }
    
    public DiscoveryMethodDTO(String id, String name, String icon, String desc, String color, boolean requiresConfig) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.desc = desc;
        this.color = color;
        this.requiresConfig = requiresConfig;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public boolean isRequiresConfig() { return requiresConfig; }
    public void setRequiresConfig(boolean requiresConfig) { this.requiresConfig = requiresConfig; }
    
    public List<ConfigFieldDTO> getConfigFields() { return configFields; }
    public void setConfigFields(List<ConfigFieldDTO> configFields) { this.configFields = configFields; }
}
