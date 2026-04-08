package net.ooder.skill.menu.dto;

import java.util.List;

public class MenuConfigDTO {
    
    private String roleId;
    private String roleName;
    private List<MenuItemDTO> menus;
    private long updatedAt;

    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public List<MenuItemDTO> getMenus() { return menus; }
    public void setMenus(List<MenuItemDTO> menus) { this.menus = menus; }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
