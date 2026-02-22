package net.ooder.nexus.skillcenter.dto.menu;

import java.util.List;

/**
 * 菜单项DTO
 */
public class MenuItemDTO {
    
    private String id;
    private String name;
    private String icon;
    private String url;
    private List<String> roles;
    private List<MenuItemDTO> children;

    public MenuItemDTO() {
    }

    public MenuItemDTO(String id, String name, String icon, String url) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.url = url;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<MenuItemDTO> getChildren() {
        return children;
    }

    public void setChildren(List<MenuItemDTO> children) {
        this.children = children;
    }
}
