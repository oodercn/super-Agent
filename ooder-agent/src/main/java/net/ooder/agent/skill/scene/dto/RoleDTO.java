package net.ooder.mvp.skill.scene.dto;

import java.util.List;

public class RoleDTO {
    private String id;
    private String name;
    private String description;
    private String icon;
    private String orgRole;
    private String defaultUsername;
    private String defaultPassword;
    private List<String> permissions;
    private List<String> menuIds;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getOrgRole() { return orgRole; }
    public void setOrgRole(String orgRole) { this.orgRole = orgRole; }
    public String getDefaultUsername() { return defaultUsername; }
    public void setDefaultUsername(String defaultUsername) { this.defaultUsername = defaultUsername; }
    public String getDefaultPassword() { return defaultPassword; }
    public void setDefaultPassword(String defaultPassword) { this.defaultPassword = defaultPassword; }
    public List<String> getPermissions() { return permissions; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
    public List<String> getMenuIds() { return menuIds; }
    public void setMenuIds(List<String> menuIds) { this.menuIds = menuIds; }
}
