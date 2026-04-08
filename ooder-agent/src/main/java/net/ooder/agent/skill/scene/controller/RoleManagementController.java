package net.ooder.mvp.skill.scene.controller;

import net.ooder.skill.org.base.UserInfo;
import net.ooder.mvp.skill.scene.dto.CreateUserRequest;
import net.ooder.mvp.skill.scene.dto.menu.MenuItemDTO;
import net.ooder.mvp.skill.scene.dto.RoleDTO;
import net.ooder.mvp.skill.scene.dto.SetPasswordRequest;
import net.ooder.skill.common.model.UserSession;
import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.service.RoleManagementService;
import net.ooder.skill.common.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/role-management")
public class RoleManagementController {

    private static final Logger log = LoggerFactory.getLogger(RoleManagementController.class);

    @Autowired
    private RoleManagementService roleManagementService;

    @Autowired
    private AuthService authService;

    @PostMapping("/roles")
    public ResultModel<RoleDTO> createRole(@RequestBody RoleDTO role) {
        log.info("[createRole] Creating role: {}", role.getName());
        RoleDTO created = roleManagementService.createRole(role);
        return ResultModel.success(created);
    }

    @GetMapping("/roles")
    public ResultModel<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleManagementService.getAllRoles();
        return ResultModel.success(roles);
    }

    @GetMapping("/roles/{roleId}")
    public ResultModel<RoleDTO> getRole(@PathVariable String roleId) {
        RoleDTO role = roleManagementService.getRole(roleId);
        if (role != null) {
            return ResultModel.success(role);
        }
        return ResultModel.error(404, "Role not found");
    }

    @PutMapping("/roles/{roleId}")
    public ResultModel<RoleDTO> updateRole(@PathVariable String roleId, @RequestBody RoleDTO role) {
        RoleDTO updated = roleManagementService.updateRole(roleId, role);
        if (updated != null) {
            return ResultModel.success(updated);
        }
        return ResultModel.error(404, "Role not found");
    }

    @DeleteMapping("/roles/{roleId}")
    public ResultModel<Void> deleteRole(@PathVariable String roleId) {
        if (roleManagementService.deleteRole(roleId)) {
            return ResultModel.success(null);
        }
        return ResultModel.error(404, "Role not found");
    }

    @GetMapping("/roles/{roleId}/menus")
    public ResultModel<List<MenuItemDTO>> getRoleMenus(@PathVariable String roleId) {
        List<MenuItemDTO> menus = roleManagementService.getMenusByRole(roleId);
        return ResultModel.success(menus);
    }

    @PostMapping("/roles/{roleId}/menus")
    public ResultModel<MenuItemDTO> addMenuToRole(@PathVariable String roleId, @RequestBody MenuItemDTO menu) {
        MenuItemDTO added = roleManagementService.addMenuToRole(roleId, menu);
        return ResultModel.success(added);
    }

    @PutMapping("/roles/{roleId}/menus")
    public ResultModel<Void> setRoleMenus(@PathVariable String roleId, @RequestBody List<MenuItemDTO> menus) {
        roleManagementService.setMenusForRole(roleId, menus);
        return ResultModel.success(null);
    }

    @DeleteMapping("/roles/{roleId}/menus/{menuId}")
    public ResultModel<Void> removeMenuFromRole(@PathVariable String roleId, @PathVariable String menuId) {
        if (roleManagementService.removeMenuFromRole(roleId, menuId)) {
            return ResultModel.success(null);
        }
        return ResultModel.error(404, "Menu not found");
    }

    @PostMapping("/users")
    public ResultModel<UserInfo> createUser(@RequestBody CreateUserRequest request) {
        log.info("[createUser] Creating user: name={}, orgRole={}", request.getName(), request.getOrgRole());

        UserInfo user = roleManagementService.createUser(
            request.getName(), 
            request.getEmail(), 
            request.getOrgRole(), 
            request.getDepartmentId()
        );
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            roleManagementService.setUserPassword(user.getUserId(), request.getPassword());
        }
        return ResultModel.success(user);
    }

    @GetMapping("/users")
    public ResultModel<List<UserInfo>> getAllUsers() {
        List<UserInfo> users = roleManagementService.getAllUsers();
        return ResultModel.success(users);
    }

    @GetMapping("/users/{userId}")
    public ResultModel<UserInfo> getUser(@PathVariable String userId) {
        UserInfo user = roleManagementService.getUserById(userId);
        if (user != null) {
            return ResultModel.success(user);
        }
        return ResultModel.error(404, "User not found");
    }

    @PostMapping("/users/{userId}/bind-role/{roleId}")
    public ResultModel<UserInfo> bindUserToRole(@PathVariable String userId, @PathVariable String roleId) {
        log.info("[bindUserToRole] Binding user {} to role {}", userId, roleId);
        UserInfo user = roleManagementService.bindUserToRole(userId, roleId);
        if (user != null) {
            return ResultModel.success(user);
        }
        return ResultModel.error(404, "User or Role not found");
    }

    @PostMapping("/users/{userId}/password")
    public ResultModel<Void> setUserPassword(@PathVariable String userId, @RequestBody SetPasswordRequest request) {
        if (roleManagementService.setUserPassword(userId, request.getPassword()) != null) {
            return ResultModel.success(null);
        }
        return ResultModel.error(404, "User not found");
    }

    @GetMapping("/roles/{roleId}/users")
    public ResultModel<List<UserInfo>> getUsersByRole(@PathVariable String roleId) {
        List<UserInfo> users = roleManagementService.getUsersByRole(roleId);
        return ResultModel.success(users);
    }

    @GetMapping("/roles/{roleId}/full")
    public ResultModel<Map<String, Object>> getRoleWithUsers(@PathVariable String roleId) {
        Map<String, Object> data = roleManagementService.getRoleWithUsers(roleId);
        if (data.isEmpty()) {
            return ResultModel.error(404, "Role not found");
        }
        return ResultModel.success(data);
    }

    @GetMapping("/config")
    public ResultModel<Map<String, Object>> getFullConfig() {
        Map<String, Object> config = roleManagementService.getFullConfig();
        return ResultModel.success(config);
    }

    @GetMapping("/my-menus")
    public ResultModel<List<MenuItemDTO>> getMyMenus(HttpServletRequest request) {
        UserSession user = authService.getCurrentUser(request);
        if (user == null) {
            return ResultModel.error(401, "Not authenticated");
        }

        String roleId = user.getRoleType();
        List<MenuItemDTO> menus = roleManagementService.getMenusByRole(roleId);
        return ResultModel.success(menus);
    }

    @PostMapping("/test/init-data")
    public ResultModel<Map<String, Object>> initTestData() {
        log.info("[initTestData] Initializing test data...");

        UserInfo testLeader = roleManagementService.createUser(
            "测试主导者", "test-leader@ooder.local", "manager", "root");
        roleManagementService.setUserPassword(testLeader.getUserId(), "test123");

        UserInfo testCollaborator = roleManagementService.createUser(
            "测试协作者", "test-collaborator@ooder.local", "employee", "root");
        roleManagementService.setUserPassword(testCollaborator.getUserId(), "test123");

        roleManagementService.bindUserToRole(testLeader.getUserId(), "leader");
        roleManagementService.bindUserToRole(testCollaborator.getUserId(), "collaborator");

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("testLeader", testLeader);
        result.put("testCollaborator", testCollaborator);
        result.put("message", "Test data initialized successfully");

        log.info("[initTestData] Test data initialized: leader={}, collaborator={}",
            testLeader.getUserId(), testCollaborator.getUserId());

        return ResultModel.success(result);
    }
}
