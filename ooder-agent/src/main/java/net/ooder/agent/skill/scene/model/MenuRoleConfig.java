package net.ooder.mvp.skill.scene.model;

import java.util.List;

/**
 * 菜单角色配置模型
 * 用于存储菜单与角色的映射关系
 */
public class MenuRoleConfig {
    
    private String id;
    private String roleId;
    private String roleName;
    private List<MenuItemConfig> menus;
    private long createdAt;
    private long updatedAt;
    
    public MenuRoleConfig() {
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getRoleId() {
        return roleId;
    }
    
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public List<MenuItemConfig> getMenus() {
        return menus;
    }
    
    public void setMenus(List<MenuItemConfig> menus) {
        this.menus = menus;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    
    public long getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    /**
     * 菜单项配置
     */
    public static class MenuItemConfig {
        private String id;
        private String name;
        private String url;
        private String icon;
        private int sort;
        private boolean active;
        private List<MenuItemConfig> children;
        
        public MenuItemConfig() {}
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
        
        public String getIcon() {
            return icon;
        }
        
        public void setIcon(String icon) {
            this.icon = icon;
        }
        
        public int getSort() {
            return sort;
        }
        
        public void setSort(int sort) {
            this.sort = sort;
        }
        
        public boolean isActive() {
            return active;
        }
        
        public void setActive(boolean active) {
            this.active = active;
        }
        
        public List<MenuItemConfig> getChildren() {
            return children;
        }
        
        public void setChildren(List<MenuItemConfig> children) {
            this.children = children;
        }
    }
}
