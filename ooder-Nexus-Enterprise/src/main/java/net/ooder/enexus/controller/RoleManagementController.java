package net.ooder.enexus.controller;

import net.ooder.enexus.dto.menu.MenuItemDTO;
import net.ooder.enexus.dto.role.RoleDTO;
import net.ooder.enexus.service.MenuRoleConfigService;
import net.ooder.enexus.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role-management")
public class RoleManagementController {

    private static final Logger log = LoggerFactory.getLogger(RoleManagementController.class);

    @Autowired
    private MenuRoleConfigService menuRoleConfigService;

    @GetMapping("/roles")
    public ResultModel<List<RoleDTO>> getAllRoles() {
        log.info("[getAllRoles] Getting all roles");
        List<RoleDTO> roles = menuRoleConfigService.getAllRolesAsDTO().values().stream()
            .map(this::convertToRoleDTO)
            .toList();
        return ResultModel.success(roles);
    }

    @GetMapping("/roles/{roleId}")
    public ResultModel<RoleDTO> getRole(@PathVariable String roleId) {
        log.info("[getRole] Getting role: {}", roleId);
        var roleDTO = menuRoleConfigService.getAllRolesAsDTO().get(roleId);
        if (roleDTO != null) {
            return ResultModel.success(convertToRoleDTO(roleDTO));
        }
        return ResultModel.error(404, "Role not found");
    }

    @GetMapping("/roles/{roleId}/menus")
    public ResultModel<List<MenuItemDTO>> getRoleMenus(@PathVariable String roleId) {
        log.info("[getRoleMenus] Getting menus for role: {}", roleId);
        List<MenuItemDTO> menus = menuRoleConfigService.getMenusByRole(roleId);
        return ResultModel.success(menus);
    }

    @PostMapping("/roles/{roleId}/menus")
    public ResultModel<MenuItemDTO> addMenuToRole(@PathVariable String roleId, @RequestBody MenuItemDTO menu) {
        log.info("[addMenuToRole] Adding menu to role {}: {}", roleId, menu.getName());
        try {
            MenuItemDTO added = menuRoleConfigService.addMenuToRole(roleId, menu);
            return ResultModel.success(added);
        } catch (Exception e) {
            log.error("[addMenuToRole] Failed to add menu: {}", e.getMessage());
            return ResultModel.error("添加菜单失败: " + e.getMessage());
        }
    }

    @PutMapping("/roles/{roleId}/menus")
    public ResultModel<Void> setRoleMenus(@PathVariable String roleId, @RequestBody List<MenuItemDTO> menus) {
        log.info("[setRoleMenus] Setting menus for role {}: {} items", roleId, menus.size());
        try {
            menuRoleConfigService.setMenusForRole(roleId, menus);
            return ResultModel.success(null);
        } catch (Exception e) {
            log.error("[setRoleMenus] Failed to set menus: {}", e.getMessage());
            return ResultModel.error("设置菜单失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/roles/{roleId}/menus/{menuId}")
    public ResultModel<Void> removeMenuFromRole(@PathVariable String roleId, @PathVariable String menuId) {
        log.info("[removeMenuFromRole] Removing menu {} from role {}", menuId, roleId);
        try {
            boolean removed = menuRoleConfigService.removeMenuFromRole(roleId, menuId);
            if (removed) {
                return ResultModel.success(null);
            }
            return ResultModel.error(404, "Menu not found");
        } catch (Exception e) {
            log.error("[removeMenuFromRole] Failed to remove menu: {}", e.getMessage());
            return ResultModel.error("删除菜单失败: " + e.getMessage());
        }
    }

    @GetMapping("/config")
    public ResultModel<Object> getFullConfig() {
        log.info("[getFullConfig] Getting full config");
        return ResultModel.success(menuRoleConfigService.getAllRolesAsDTO());
    }

    private RoleDTO convertToRoleDTO(net.ooder.enexus.dto.menu.MenuConfigDTO.MenuRoleDTO dto) {
        RoleDTO role = new RoleDTO();
        role.setId(dto.getId());
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setIcon(dto.getIcon());
        return role;
    }
}
