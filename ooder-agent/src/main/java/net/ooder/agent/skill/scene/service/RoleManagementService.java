package net.ooder.mvp.skill.scene.service;

import net.ooder.skill.org.base.OrgSkill;
import net.ooder.skill.org.base.UserInfo;
import net.ooder.skill.org.base.OrgInfo;
import net.ooder.mvp.skill.scene.dto.menu.MenuItemDTO;
import net.ooder.mvp.skill.scene.dto.RoleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;

@Service
public class RoleManagementService {

    private static final Logger log = LoggerFactory.getLogger(RoleManagementService.class);

    @Autowired
    private OrgSkill orgSkill;

    private final Map<String, RoleDTO> roles = new LinkedHashMap<>();
    private final Map<String, List<MenuItemDTO>> roleMenus = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        initDefaultRoles();
        initDefaultMenus();
        initDefaultUsers();
        log.info("RoleManagementService initialized with {} roles", roles.size());
    }

    private void initDefaultRoles() {
        createRole("admin", "管理员", "系统运维、能力管理、用户管理", "ri-admin-line",
            "admin", Arrays.asList(
                "system:init", "system:config",
                "capability:discover", "capability:install", "capability:distribute", "capability:manage",
                "scene:create", "scene:manage",
                "user:manage", "org:manage",
                "llm:config", "knowledge:manage",
                "audit:view"
            ));

        createRole("user", "普通用户", "场景参与、任务执行、业务流程", "ri-user-line",
            "user", Arrays.asList(
                "scene:view", "scene:activate", "scene:participate",
                "task:view", "task:execute", "task:submit",
                "todo:view", "todo:process",
                "history:view", "key:manage"
            ));

        createRole("developer", "开发者", "能力开发、测试、发布", "ri-code-line",
            "developer", Arrays.asList(
                "capability:create", "capability:edit", "capability:test", "capability:publish", "capability:view",
                "llm:execute", "arch:check"
            ));
    }

    private void initDefaultMenus() {
        List<MenuItemDTO> adminMenus = Arrays.asList(
            createMenuItem("menu-admin-1", "工作台", "/console/pages/role-admin.html", "ri-home-line", 1, true),
            createMenuItem("menu-admin-2", "能力市场", "/console/pages/capability-discovery.html", "ri-store-2-line", 2, false),
            createMenuItem("menu-admin-3", "已安装能力", "/console/pages/installed-scene-capabilities.html", "ri-download-cloud-line", 3, false),
            createMenuItem("menu-admin-4", "场景管理", "/console/pages/scene-group-management.html", "ri-folder-line", 4, false),
            createMenuItem("menu-admin-5", "组织管理", "/console/pages/org-management.html", "ri-organization-chart", 5, false),
            createMenuItem("menu-admin-6", "系统配置", "/console/pages/llm-config.html", "ri-settings-3-line", 6, false),
            createMenuItem("menu-admin-7", "系统监控", "/console/pages/llm-monitor.html", "ri-line-chart-line", 7, false),
            createMenuItem("menu-admin-8", "审计日志", "/console/pages/audit-logs.html", "ri-file-list-3-line", 8, false)
        );
        roleMenus.put("admin", adminMenus);

        List<MenuItemDTO> userMenus = Arrays.asList(
            createMenuItem("menu-user-1", "工作台", "/console/pages/role-user.html", "ri-home-line", 1, true),
            createMenuItem("menu-user-2", "我的待办", "/console/pages/my-todos.html", "ri-task-line", 2, false),
            createMenuItem("menu-user-3", "我的场景", "/console/pages/my-scenes.html", "ri-artboard-line", 3, false),
            createMenuItem("menu-user-4", "历史记录", "/console/pages/my-history.html", "ri-history-line", 4, false),
            createMenuItem("menu-user-5", "密钥管理", "/console/pages/key-management.html", "ri-key-2-line", 5, false),
            createMenuItem("menu-user-6", "入网审批", "/console/pages/network-approval.html", "ri-user-add-line", 6, false)
        );
        roleMenus.put("user", userMenus);

        List<MenuItemDTO> developerMenus = Arrays.asList(
            createMenuItem("menu-developer-1", "工作台", "/console/pages/role-developer.html", "ri-home-line", 1, true),
            createMenuItem("menu-developer-2", "我的能力", "/console/pages/my-capabilities.html", "ri-puzzle-line", 2, false),
            createMenuItem("menu-developer-3", "创建能力", "/console/pages/capability-create.html", "ri-add-circle-line", 3, false),
            createMenuItem("menu-developer-4", "架构检查", "/console/pages/arch-check.html", "ri-shield-check-line", 4, false),
            createMenuItem("menu-developer-5", "能力统计", "/console/pages/capability-stats.html", "ri-bar-chart-box-line", 5, false)
        );
        roleMenus.put("developer", developerMenus);
        
        List<MenuItemDTO> collaboratorMenus = Arrays.asList(
            createMenuItem("menu-collab-1", "工作台", "/console/pages/role-admin.html", "ri-home-line", 1, true),
            createMenuItem("menu-collab-2", "我的能力", "/console/pages/my-capabilities.html", "ri-puzzle-line", 2, false),
            createMenuItem("menu-collab-3", "能力发现", "/console/pages/capability-discovery.html", "ri-search-line", 3, false),
            createMenuItem("menu-collab-4", "场景管理", "/console/pages/scene-group-management.html", "ri-folder-line", 4, false),
            createMenuItem("menu-collab-5", "LLM配置", "/console/pages/llm-config.html", "ri-robot-line", 5, false)
        );
        roleMenus.put("collaborator", collaboratorMenus);
    }

    private void initDefaultUsers() {
        initUserIfNotExists("admin", "管理员", "admin@ooder.local", "root", Arrays.asList("admin"));
        initUserIfNotExists("user", "普通用户", "user@ooder.local", "root", Arrays.asList("user"));
        initUserIfNotExists("developer", "开发者", "developer@ooder.local", "root", Arrays.asList("developer"));
    }

    private void initUserIfNotExists(String username, String nickname, String email, String orgId, List<String> roles) {
        UserInfo existing = orgSkill.getUserByAccount(username);
        if (existing == null) {
            UserInfo user = new UserInfo();
            user.setUsername(username);
            user.setNickname(nickname);
            user.setEmail(email);
            user.setOrgId(orgId);
            user.setRoles(roles);
            user.setStatus("active");
            orgSkill.registerUser(user);
            log.info("Created default user: {}", username);
        }
    }

    private RoleDTO createRole(String id, String name, String description, String icon,
                               String orgRole, List<String> permissions) {
        RoleDTO role = new RoleDTO();
        role.setId(id);
        role.setName(name);
        role.setDescription(description);
        role.setIcon(icon);
        role.setOrgRole(orgRole);
        role.setPermissions(permissions);
        roles.put(id, role);
        return role;
    }

    private MenuItemDTO createMenuItem(String id, String name, String url, String icon, int sort, boolean active) {
        MenuItemDTO item = new MenuItemDTO();
        item.setId(id);
        item.setName(name);
        item.setUrl(url);
        item.setIcon(icon);
        item.setSort(sort);
        item.setOrder(sort);
        item.setVisible(true);
        item.setActive(active);
        return item;
    }

    public RoleDTO createRole(RoleDTO role) {
        if (role.getId() == null || role.getId().isEmpty()) {
            role.setId("role-" + UUID.randomUUID().toString().substring(0, 8));
        }
        roles.put(role.getId(), role);
        log.info("Created role: {}", role.getId());
        return role;
    }

    public RoleDTO getRole(String roleId) {
        return roles.get(roleId);
    }

    public List<RoleDTO> getAllRoles() {
        return new ArrayList<>(roles.values());
    }

    public RoleDTO updateRole(String roleId, RoleDTO role) {
        if (!roles.containsKey(roleId)) {
            return null;
        }
        role.setId(roleId);
        roles.put(roleId, role);
        log.info("Updated role: {}", roleId);
        return role;
    }

    public boolean deleteRole(String roleId) {
        if (roles.remove(roleId) != null) {
            roleMenus.remove(roleId);
            log.info("Deleted role: {}", roleId);
            return true;
        }
        return false;
    }

    public List<MenuItemDTO> getMenusByRole(String roleId) {
        return roleMenus.getOrDefault(roleId, new ArrayList<>());
    }

    public void setMenusForRole(String roleId, List<MenuItemDTO> menus) {
        roleMenus.put(roleId, menus);
        log.info("Set {} menus for role: {}", menus.size(), roleId);
    }

    public MenuItemDTO addMenuToRole(String roleId, MenuItemDTO menu) {
        List<MenuItemDTO> menus = roleMenus.computeIfAbsent(roleId, k -> new ArrayList<>());
        if (menu.getId() == null || menu.getId().isEmpty()) {
            menu.setId("menu-" + roleId + "-" + (menus.size() + 1));
        }
        menu.setParentRoleId(roleId);
        menus.add(menu);
        log.info("Added menu {} to role {}", menu.getId(), roleId);
        return menu;
    }

    public boolean removeMenuFromRole(String roleId, String menuId) {
        List<MenuItemDTO> menus = roleMenus.get(roleId);
        if (menus != null) {
            return menus.removeIf(m -> menuId.equals(m.getId()));
        }
        return false;
    }

    public UserInfo createUser(String name, String email, String orgRole, String departmentId) {
        UserInfo user = new UserInfo();
        user.setUsername(name.toLowerCase().replace(" ", ""));
        user.setNickname(name);
        user.setEmail(email);
        user.setOrgId(departmentId);
        user.setRoles(Arrays.asList(orgRole));
        user.setStatus("active");

        RoleDTO matchedRole = findRoleByOrgRole(orgRole);
        if (matchedRole != null) {
            user.setRoles(Arrays.asList(matchedRole.getOrgRole()));
        }

        UserInfo created = orgSkill.registerUser(user);
        log.info("Created user: {} with orgRole: {}", created.getUserId(), orgRole);
        return created;
    }

    public UserInfo bindUserToRole(String userId, String roleId) {
        UserInfo user = orgSkill.getUser(userId);
        if (user == null) {
            log.warn("User not found: {}", userId);
            return null;
        }

        RoleDTO role = roles.get(roleId);
        if (role == null) {
            log.warn("Role not found: {}", roleId);
            return null;
        }

        List<String> currentRoles = user.getRoles() != null ? new ArrayList<>(user.getRoles()) : new ArrayList<>();
        if (!currentRoles.contains(role.getOrgRole())) {
            currentRoles.add(role.getOrgRole());
        }
        user.setRoles(currentRoles);
        
        UserInfo updated = orgSkill.updateUser(userId, user);
        log.info("Bound user {} to role {}", userId, roleId);
        return updated;
    }

    public UserInfo setUserPassword(String userId, String password) {
        UserInfo user = orgSkill.getUser(userId);
        if (user == null) {
            return null;
        }
        log.info("Password update requested for user: {} (note: password storage depends on OrgSkill implementation)", userId);
        return user;
    }

    private RoleDTO findRoleByOrgRole(String orgRole) {
        for (RoleDTO role : roles.values()) {
            if (orgRole.equals(role.getOrgRole())) {
                return role;
            }
        }
        return null;
    }

    public List<UserInfo> getUsersByRole(String roleId) {
        RoleDTO role = roles.get(roleId);
        if (role != null) {
            List<OrgInfo> orgTree = orgSkill.getOrgTree();
            List<UserInfo> allUsers = new ArrayList<>();
            for (OrgInfo org : orgTree) {
                allUsers.addAll(orgSkill.getOrgUsers(org.getOrgId()));
            }
            List<UserInfo> filtered = new ArrayList<>();
            for (UserInfo user : allUsers) {
                if (user.getRoles() != null && user.getRoles().contains(role.getOrgRole())) {
                    filtered.add(user);
                }
            }
            return filtered;
        }
        return new ArrayList<>();
    }

    public List<UserInfo> getAllUsers() {
        List<UserInfo> allUsers = new ArrayList<>();
        for (UserInfo user : orgSkill.getOrgUsers("root")) {
            allUsers.add(user);
        }
        return allUsers;
    }
    
    public UserInfo getUserById(String userId) {
        return orgSkill.getUser(userId);
    }
    
    public Map<String, Object> getRoleWithUsers(String roleId) {
        Map<String, Object> result = new HashMap<>();
        RoleDTO role = roles.get(roleId);
        if (role != null) {
            result.put("role", role);
            result.put("menus", getMenusByRole(roleId));
            result.put("users", getUsersByRole(roleId));
        }
        return result;
    }
    
    public Map<String, Object> getFullConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("roles", getAllRoles());
        
        Map<String, Object> roleConfigs = new HashMap<>();
        for (String roleId : roles.keySet()) {
            roleConfigs.put(roleId, getRoleWithUsers(roleId));
        }
        config.put("roleConfigs", roleConfigs);
        
        return config;
    }
}
