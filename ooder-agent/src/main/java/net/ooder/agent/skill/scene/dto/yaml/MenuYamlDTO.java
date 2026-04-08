package net.ooder.mvp.skill.scene.dto.yaml;

import java.util.List;

public class MenuYamlDTO {
    
    private String id;
    private String name;
    private String icon;
    private String url;
    private Integer order;
    private Boolean visible;
    private List<MenuYamlDTO> children;
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public Integer getOrder() { return order; }
    public void setOrder(Integer order) { this.order = order; }
    
    public Boolean getVisible() { return visible; }
    public void setVisible(Boolean visible) { this.visible = visible; }
    
    public List<MenuYamlDTO> getChildren() { return children; }
    public void setChildren(List<MenuYamlDTO> children) { this.children = children; }
}
