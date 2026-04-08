package net.ooder.skill.menu.service;

import java.util.List;
import java.util.Map;
import net.ooder.skill.menu.dto.*;

public interface MenuRoleConfigService {
    
    List<Map<String, Object>> getAllRolesAsDTO();
    
    List<MenuItemDTO> getMenusByRole(String roleId);
    
    List<MenuItemDTO> getMenuTreeByRole(String roleId);
    
    void addChildMenu(String roleId, String parentId, MenuItemDTO menu);
    
    void updateMenu(String roleId, String menuId, MenuItemDTO menu);
    
    void deleteMenuWithChildren(String roleId, String menuId);
    
    void moveMenu(String roleId, String menuId, String newParentId, int newSort);
    
    void updateRoleMenus(String roleId, List<MenuItemDTO> menus);
    
    List<MenuItemDTO> getUserMenusAsDTO(String userId);
    
    void updateUserMenus(String userId, List<MenuItemDTO> menus);
    
    String exportConfig();
    
    void importConfig(String jsonContent);
}
