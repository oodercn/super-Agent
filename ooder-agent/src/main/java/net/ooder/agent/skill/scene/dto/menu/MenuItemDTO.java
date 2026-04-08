package net.ooder.mvp.skill.scene.dto.menu;

import java.util.List;

public class MenuItemDTO {
    private String id;
    private String parentId;
    private String parentRoleId;
    private String name;
    private String icon;
    private String url;
    private int order;
    private int sort;
    private boolean visible;
    private boolean active;
    private int level;
    private List<MenuItemDTO> children;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
    public String getParentRoleId() { return parentRoleId; }
    public void setParentRoleId(String parentRoleId) { this.parentRoleId = parentRoleId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public int getOrder() { return order; }
    public void setOrder(int order) { this.order = order; }
    public int getSort() { return sort; }
    public void setSort(int sort) { this.sort = sort; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public List<MenuItemDTO> getChildren() { return children; }
    public void setChildren(List<MenuItemDTO> children) { this.children = children; }
}
