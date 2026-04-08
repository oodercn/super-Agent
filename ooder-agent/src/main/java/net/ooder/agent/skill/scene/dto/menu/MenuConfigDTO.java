package net.ooder.mvp.skill.scene.dto.menu;

import java.util.List;

public class MenuConfigDTO {
    private String id;
    private String name;
    private String icon;
    private String url;
    private int order;
    private boolean visible;
    private List<MenuConfigDTO> children;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public int getOrder() { return order; }
    public void setOrder(int order) { this.order = order; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public List<MenuConfigDTO> getChildren() { return children; }
    public void setChildren(List<MenuConfigDTO> children) { this.children = children; }

    public static class MenuRoleDTO {
        private String id;
        private String name;
        private String description;
        private String icon;
        private List<MenuConfigDTO> menus;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        public List<MenuConfigDTO> getMenus() { return menus; }
        public void setMenus(List<MenuConfigDTO> menus) { this.menus = menus; }
    }
}
