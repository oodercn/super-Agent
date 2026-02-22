package net.ooder.nexus.domain.model;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单实体类
 * 对应 menu-config.json 中的菜单项
 */
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 菜单ID */
    private String id;

    /** 菜单名称 */
    private String name;

    /** 图标 */
    private String icon;

    /** 菜单层级 */
    private Integer level;

    /** 链接地址 */
    private String url;

    /** 优先级 */
    private String priority;

    /** 状态：implemented/planned */
    private String status;

    /** 角标信息 */
    private MenuBadge badge;

    /** 子菜单 */
    private List<Menu> children;

    /** 角色权限 */
    private List<String> roles;

    public Menu() {
    }

    // ==================== Getter 和 Setter ====================

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MenuBadge getBadge() {
        return badge;
    }

    public void setBadge(MenuBadge badge) {
        this.badge = badge;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * 是否有子菜单
     */
    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", status='" + status + '\'' +
                '}';
    }
}
