package net.ooder.skill.discovery.dto.discovery;

import java.util.List;

public class DirectoryTreeNodeDTO {
    
    private String name;
    private String description;
    private String icon;
    private String color;
    private List<DirectoryTreeChildDTO> children;
    private int count;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public List<DirectoryTreeChildDTO> getChildren() { return children; }
    public void setChildren(List<DirectoryTreeChildDTO> children) { this.children = children; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}
