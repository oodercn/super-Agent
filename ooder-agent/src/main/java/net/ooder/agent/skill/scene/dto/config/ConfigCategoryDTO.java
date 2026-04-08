package net.ooder.mvp.skill.scene.dto.config;

public class ConfigCategoryDTO {
    
    private String code;
    private String name;
    private String icon;
    private String color;
    private Boolean userFacing;
    
    public ConfigCategoryDTO() {
    }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public Boolean getUserFacing() { return userFacing; }
    public void setUserFacing(Boolean userFacing) { this.userFacing = userFacing; }
}
