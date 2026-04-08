package net.ooder.skill.menu.service;

import net.ooder.skill.menu.dto.MenuDTO;
import java.util.List;

public interface MenuService {
    
    MenuDTO createMenu(MenuDTO menuDTO);
    
    MenuDTO updateMenu(String menuId, MenuDTO menuDTO);
    
    void deleteMenu(String menuId);
    
    MenuDTO getMenu(String menuId);
    
    List<MenuDTO> getAllMenus();
    
    List<MenuDTO> getMenuTree();
    
    List<MenuDTO> getMenusByCategory(String category);
    
    void initializeDefaultMenus();
}
