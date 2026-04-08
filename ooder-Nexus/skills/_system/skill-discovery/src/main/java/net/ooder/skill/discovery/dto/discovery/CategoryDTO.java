package net.ooder.skill.discovery.dto.discovery;

public class CategoryDTO {
    
    private String id;
    private String name;
    private String icon;
    private String color;
    private boolean userFacing;
    private int count;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public boolean isUserFacing() { return userFacing; }
    public void setUserFacing(boolean userFacing) { this.userFacing = userFacing; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}
