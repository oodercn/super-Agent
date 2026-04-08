package net.ooder.skill.menu.controller;

import net.ooder.skill.menu.dto.*;
import net.ooder.skill.menu.model.*;
import net.ooder.skill.menu.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/scene-menu")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class SceneMenuController {

    private static final Logger log = LoggerFactory.getLogger(SceneMenuController.class);

    @Autowired(required = false)
    private MenuRoleConfigService menuRoleConfigService;

    @GetMapping("/config")
    public ResultModel<Map<String, Object>> getMenuConfig() {
        log.info("[getMenuConfig] Loading menu config");
        
        Map<String, Object> result = new HashMap<String, Object>();
        
        if (menuRoleConfigService != null) {
            result.put("roles", menuRoleConfigService.getAllRolesAsDTO());
        } else {
            result.put("roles", new ArrayList<Object>());
        }
        result.put("updatedAt", System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    @GetMapping("/library")
    public ResultModel<List<MenuItemDTO>> getMenuLibrary() {
        log.info("[getMenuLibrary] Loading menu library");
        
        if (menuRoleConfigService == null) {
            return ResultModel.success(new ArrayList<MenuItemDTO>());
        }
        
        List<MenuItemDTO> library = menuRoleConfigService.getMenusByRole("admin");
        
        return ResultModel.success(library);
    }

    @GetMapping("/roles/{roleId}/menu-tree")
    public ResultModel<List<MenuItemDTO>> getRoleMenuTree(@PathVariable String roleId) {
        log.info("[getRoleMenuTree] Loading menu tree for role: {}", roleId);
        
        if (menuRoleConfigService == null) {
            return ResultModel.success(new ArrayList<MenuItemDTO>());
        }
        
        List<MenuItemDTO> menuTree = menuRoleConfigService.getMenuTreeByRole(roleId);
        
        return ResultModel.success(menuTree);
    }

    @GetMapping("/roles/{roleId}/menus")
    public ResultModel<List<MenuItemDTO>> getRoleMenus(@PathVariable String roleId) {
        log.info("[getRoleMenus] Loading menus for role: {}", roleId);
        
        if (menuRoleConfigService == null) {
            return ResultModel.success(new ArrayList<MenuItemDTO>());
        }
        
        List<MenuItemDTO> menus = menuRoleConfigService.getMenusByRole(roleId);
        
        return ResultModel.success(menus);
    }

    @PostMapping("/roles/{roleId}/menus/{parentId}/children")
    public ResultModel<MenuItemDTO> addChildMenu(
            @PathVariable String roleId,
            @PathVariable String parentId,
            @RequestBody MenuItemDTO menu) {
        
        log.info("[addChildMenu] Adding child menu to role: {}, parentId: {}", roleId, parentId);
        
        if (menuRoleConfigService != null) {
            String actualParentId = "null".equals(parentId) ? null : parentId;
            menuRoleConfigService.addChildMenu(roleId, actualParentId, menu);
        }
        
        return ResultModel.success(menu);
    }

    @PutMapping("/roles/{roleId}/menus/{menuId}")
    public ResultModel<Void> updateMenu(
            @PathVariable String roleId,
            @PathVariable String menuId,
            @RequestBody MenuItemDTO menu) {
        
        log.info("[updateMenu] Updating menu: {} in role: {}", menuId, roleId);
        
        if (menuRoleConfigService != null) {
            menuRoleConfigService.updateMenu(roleId, menuId, menu);
        }
        
        return ResultModel.success(null);
    }

    @DeleteMapping("/roles/{roleId}/menus/{menuId}/tree")
    public ResultModel<Void> deleteMenuTree(
            @PathVariable String roleId,
            @PathVariable String menuId) {
        
        log.info("[deleteMenuTree] Deleting menu tree: {} in role: {}", menuId, roleId);
        
        if (menuRoleConfigService != null) {
            menuRoleConfigService.deleteMenuWithChildren(roleId, menuId);
        }
        
        return ResultModel.success(null);
    }

    @PutMapping("/roles/{roleId}/menus/{menuId}/move")
    public ResultModel<Void> moveMenu(
            @PathVariable String roleId,
            @PathVariable String menuId,
            @RequestParam(required = false) String newParentId,
            @RequestParam(defaultValue = "1") int newSort) {
        
        log.info("[moveMenu] Moving menu: {} in role: {} to parentId: {}, sort: {}", 
            menuId, roleId, newParentId, newSort);
        
        if (menuRoleConfigService != null) {
            menuRoleConfigService.moveMenu(roleId, menuId, newParentId, newSort);
        }
        
        return ResultModel.success(null);
    }

    @PutMapping("/roles/{roleId}/menus")
    public ResultModel<Void> setRoleMenus(
            @PathVariable String roleId,
            @RequestBody List<MenuItemDTO> menus) {
        
        log.info("[setRoleMenus] Setting menus for role: {}, count: {}", roleId, menus.size());
        
        if (menuRoleConfigService != null) {
            menuRoleConfigService.updateRoleMenus(roleId, menus);
        }
        
        return ResultModel.success(null);
    }

    @GetMapping("/users/{userId}/menus")
    public ResultModel<List<MenuItemDTO>> getUserMenus(@PathVariable String userId) {
        log.info("[getUserMenus] Loading menus for user: {}", userId);
        
        if (menuRoleConfigService == null) {
            return ResultModel.success(new ArrayList<MenuItemDTO>());
        }
        
        List<MenuItemDTO> menus = menuRoleConfigService.getUserMenusAsDTO(userId);
        
        return ResultModel.success(menus);
    }

    @PutMapping("/users/{userId}/menus")
    public ResultModel<Void> setUserMenus(
            @PathVariable String userId,
            @RequestBody List<MenuItemDTO> menus) {
        
        log.info("[setUserMenus] Setting menus for user: {}, count: {}", userId, menus.size());
        
        if (menuRoleConfigService != null) {
            menuRoleConfigService.updateUserMenus(userId, menus);
        }
        
        return ResultModel.success(null);
    }

    @GetMapping("/export")
    public ResultModel<String> exportConfig() {
        log.info("[exportConfig] Exporting menu config");
        
        if (menuRoleConfigService == null) {
            return ResultModel.success("{}");
        }
        
        String config = menuRoleConfigService.exportConfig();
        
        return ResultModel.success(config);
    }

    @PostMapping("/import")
    public ResultModel<Void> importConfig(@RequestBody String jsonContent) {
        log.info("[importConfig] Importing menu config");
        
        if (menuRoleConfigService != null) {
            menuRoleConfigService.importConfig(jsonContent);
        }
        
        return ResultModel.success(null);
    }
}
