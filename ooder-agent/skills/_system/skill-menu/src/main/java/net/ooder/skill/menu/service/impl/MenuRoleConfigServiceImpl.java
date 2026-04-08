package net.ooder.skill.menu.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import net.ooder.skill.menu.dto.MenuConfigDTO;
import net.ooder.skill.menu.dto.MenuItemDTO;
import net.ooder.skill.menu.service.MenuRoleConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class MenuRoleConfigServiceImpl implements MenuRoleConfigService {

    private static final Logger log = LoggerFactory.getLogger(MenuRoleConfigServiceImpl.class);
    
    private static final String CONFIG_DIR = "data/config";
    private static final String CONFIG_FILE = "menu-role-config.json";
    
    private JSONObject menuConfig = null;
    
    @PostConstruct
    public void init() {
        loadConfig();
        log.info("[MenuRoleConfigServiceImpl] Initialized");
    }
    
    private void loadConfig() {
        Path configPath = Paths.get(CONFIG_DIR, CONFIG_FILE);
        
        if (Files.exists(configPath)) {
            try {
                String content = new String(Files.readAllBytes(configPath), StandardCharsets.UTF_8);
                menuConfig = JSON.parseObject(content);
                log.info("Loaded menu config from {}", configPath);
            } catch (Exception e) {
                log.error("Failed to load menu config: {}", e.getMessage());
                initDefaultConfig();
            }
        } else {
            initDefaultConfig();
            saveConfig();
        }
    }
    
    private void saveConfig() {
        try {
            Path configDir = Paths.get(CONFIG_DIR);
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
            
            Path configPath = Paths.get(CONFIG_DIR, CONFIG_FILE);
            String content = JSON.toJSONString(menuConfig, JSONWriter.Feature.PrettyFormat);
            Files.write(configPath, content.getBytes(StandardCharsets.UTF_8));
            
            log.info("Saved menu config to {}", configPath);
        } catch (Exception e) {
            log.error("Failed to save menu config: {}", e.getMessage());
        }
    }
    
    private void initDefaultConfig() {
        menuConfig = new JSONObject();
        menuConfig.put("version", "2.0");
        menuConfig.put("updatedAt", System.currentTimeMillis());
        
        JSONObject roles = new JSONObject();
        
        roles.put("admin", createRoleConfig("admin", "管理员", getAdminMenus()));
        roles.put("user", createRoleConfig("user", "普通用户", getUserMenus()));
        roles.put("developer", createRoleConfig("developer", "开发者", getDeveloperMenus()));
        
        menuConfig.put("roles", roles);
    }
    
    private JSONObject createRoleConfig(String id, String name, JSONArray menus) {
        JSONObject role = new JSONObject();
        role.put("id", id);
        role.put("name", name);
        role.put("menus", menus);
        return role;
    }
    
    private JSONArray getAdminMenus() {
        JSONArray menus = new JSONArray();
        
        JSONObject workbench = createMenuItem("menu-admin-1", "工作台", "/console/pages/role-admin.html", "ri-home-line", 1, true);
        menus.add(workbench);
        
        JSONObject capabilityCenter = createMenuItem("menu-admin-2", "能力中心", "#", "ri-puzzle-line", 2, false);
        JSONArray capChildren = new JSONArray();
        capChildren.add(createChildMenuItem("menu-admin-2-1", "能力市场", "/console/pages/capability-discovery.html", "ri-store-2-line", 1, "menu-admin-2"));
        capChildren.add(createChildMenuItem("menu-admin-2-2", "已安装能力", "/console/pages/installed-scene-capabilities.html", "ri-download-cloud-line", 2, "menu-admin-2"));
        capChildren.add(createChildMenuItem("menu-admin-2-3", "能力管理", "/console/pages/capability-management.html", "ri-settings-4-line", 3, "menu-admin-2"));
        capabilityCenter.put("children", capChildren);
        menus.add(capabilityCenter);
        
        JSONObject knowledgeCenter = createMenuItem("menu-admin-3", "知识中心", "#", "ri-book-3-line", 3, false);
        JSONArray knowChildren = new JSONArray();
        knowChildren.add(createChildMenuItem("menu-admin-3-1", "知识库管理", "/console/pages/knowledge-base.html", "ri-database-2-line", 1, "menu-admin-3"));
        knowChildren.add(createChildMenuItem("menu-admin-3-2", "知识中心", "/console/pages/knowledge-center.html", "ri-folder-3-line", 2, "menu-admin-3"));
        knowledgeCenter.put("children", knowChildren);
        menus.add(knowledgeCenter);
        
        menus.add(createMenuItem("menu-admin-4", "场景管理", "/console/pages/scene-group-management.html", "ri-folder-line", 4, false));
        menus.add(createMenuItem("menu-admin-5", "工作流管理", "/console/pages/bpm-workflow.html", "ri-flow-chart", 5, false));
        menus.add(createMenuItem("menu-admin-6", "组织管理", "/console/pages/org-management.html", "ri-team-line", 6, false));
        
        JSONObject systemConfig = createMenuItem("menu-admin-7", "系统配置", "#", "ri-settings-3-line", 7, false);
        JSONArray sysChildren = new JSONArray();
        sysChildren.add(createChildMenuItem("menu-admin-7-1", "LLM配置", "/console/pages/llm-config.html", "ri-robot-line", 1, "menu-admin-7"));
        sysChildren.add(createChildMenuItem("menu-admin-7-2", "系统监控", "/console/pages/llm-monitor.html", "ri-line-chart-line", 2, "menu-admin-7"));
        sysChildren.add(createChildMenuItem("menu-admin-7-3", "审计日志", "/console/pages/audit-logs.html", "ri-file-list-3-line", 3, "menu-admin-7"));
        systemConfig.put("children", sysChildren);
        menus.add(systemConfig);
        
        return menus;
    }
    
    private JSONObject createChildMenuItem(String id, String name, String url, String icon, int sort, String parentId) {
        JSONObject item = createMenuItem(id, name, url, icon, sort, false);
        item.put("parentId", parentId);
        item.put("level", 1);
        return item;
    }
    
    private JSONArray getUserMenus() {
        JSONArray menus = new JSONArray();
        menus.add(createMenuItem("menu-user-1", "工作台", "/console/pages/role-user.html", "ri-home-line", 1, true));
        menus.add(createMenuItem("menu-user-2", "我的待办", "/console/pages/my-todos.html", "ri-task-line", 2, false));
        menus.add(createMenuItem("menu-user-3", "我的场景", "/console/pages/my-scenes.html", "ri-artboard-line", 3, false));
        menus.add(createMenuItem("menu-user-4", "历史记录", "/console/pages/my-history.html", "ri-history-line", 4, false));
        menus.add(createMenuItem("menu-user-5", "密钥管理", "/console/pages/key-management.html", "ri-key-2-line", 5, false));
        return menus;
    }
    
    private JSONArray getDeveloperMenus() {
        JSONArray menus = new JSONArray();
        menus.add(createMenuItem("menu-developer-1", "工作台", "/console/pages/role-developer.html", "ri-home-line", 1, true));
        menus.add(createMenuItem("menu-developer-2", "我的能力", "/console/pages/my-capabilities.html", "ri-puzzle-line", 2, false));
        menus.add(createMenuItem("menu-developer-3", "创建能力", "/console/pages/capability-create.html", "ri-add-circle-line", 3, false));
        menus.add(createMenuItem("menu-developer-4", "架构检查", "/console/pages/arch-check.html", "ri-shield-check-line", 4, false));
        menus.add(createMenuItem("menu-developer-5", "能力统计", "/console/pages/capability-stats.html", "ri-bar-chart-box-line", 5, false));
        return menus;
    }
    
    private JSONObject createMenuItem(String id, String name, String url, String icon, int sort, boolean active) {
        JSONObject item = new JSONObject();
        item.put("id", id);
        item.put("name", name);
        item.put("url", url);
        item.put("icon", icon);
        item.put("sort", sort);
        item.put("active", active);
        item.put("parentId", null);
        item.put("level", 0);
        return item;
    }
    
    @Override
    public List<Map<String, Object>> getAllRolesAsDTO() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        if (menuConfig == null) {
            return result;
        }
        
        JSONObject roles = menuConfig.getJSONObject("roles");
        if (roles == null) {
            return result;
        }
        
        for (String roleId : roles.keySet()) {
            JSONObject roleJson = roles.getJSONObject(roleId);
            Map<String, Object> roleDTO = new HashMap<>();
            roleDTO.put("id", roleJson.getString("id"));
            roleDTO.put("name", roleJson.getString("name"));
            roleDTO.put("description", roleJson.getString("description"));
            roleDTO.put("icon", roleJson.getString("icon"));
            result.add(roleDTO);
        }
        
        return result;
    }
    
    @Override
    public List<MenuItemDTO> getMenusByRole(String roleId) {
        List<MenuItemDTO> items = new ArrayList<>();
        
        if (menuConfig == null) {
            return items;
        }
        
        JSONObject roles = menuConfig.getJSONObject("roles");
        if (roles == null) {
            return items;
        }
        
        JSONObject role = roles.getJSONObject(roleId);
        if (role == null) {
            return items;
        }
        
        JSONArray menus = role.getJSONArray("menus");
        if (menus == null) {
            return items;
        }
        
        for (int i = 0; i < menus.size(); i++) {
            JSONObject menu = menus.getJSONObject(i);
            items.add(convertToMenuItem(menu));
        }
        
        return items;
    }
    
    @Override
    public List<MenuItemDTO> getMenuTreeByRole(String roleId) {
        List<MenuItemDTO> allMenus = getMenusByRole(roleId);
        return buildMenuTree(allMenus);
    }
    
    private List<MenuItemDTO> buildMenuTree(List<MenuItemDTO> allMenus) {
        Map<String, MenuItemDTO> menuMap = new LinkedHashMap<>();
        List<MenuItemDTO> rootMenus = new ArrayList<>();
        
        for (MenuItemDTO menu : allMenus) {
            menuMap.put(menu.getId(), menu);
        }
        
        for (MenuItemDTO menu : allMenus) {
            String parentId = menu.getParentId();
            if (parentId == null || parentId.isEmpty()) {
                rootMenus.add(menu);
            } else {
                MenuItemDTO parent = menuMap.get(parentId);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(menu);
                } else {
                    rootMenus.add(menu);
                }
            }
        }
        
        sortMenuTree(rootMenus);
        return rootMenus;
    }
    
    private void sortMenuTree(List<MenuItemDTO> menus) {
        if (menus == null || menus.isEmpty()) {
            return;
        }
        
        menus.sort(Comparator.comparingInt(MenuItemDTO::getSort));
        
        for (MenuItemDTO menu : menus) {
            if (menu.getChildren() != null) {
                sortMenuTree(menu.getChildren());
            }
        }
    }
    
    @Override
    public void addChildMenu(String roleId, String parentId, MenuItemDTO menu) {
        if (menuConfig == null) {
            return;
        }
        
        JSONObject roles = menuConfig.getJSONObject("roles");
        if (roles == null) {
            return;
        }
        
        JSONObject role = roles.getJSONObject(roleId);
        if (role == null) {
            return;
        }
        
        JSONArray menus = role.getJSONArray("menus");
        if (menus == null) {
            menus = new JSONArray();
            role.put("menus", menus);
        }
        
        int level = 1;
        int sort = 1;
        
        if (parentId != null && !parentId.isEmpty()) {
            for (int i = 0; i < menus.size(); i++) {
                JSONObject m = menus.getJSONObject(i);
                if (parentId.equals(m.getString("id"))) {
                    level = m.getIntValue("level") + 1;
                    break;
                }
            }
            
            int maxSort = 0;
            for (int i = 0; i < menus.size(); i++) {
                JSONObject m = menus.getJSONObject(i);
                if (parentId.equals(m.getString("parentId"))) {
                    maxSort = Math.max(maxSort, m.getIntValue("sort"));
                }
            }
            sort = maxSort + 1;
        } else {
            int maxSort = 0;
            for (int i = 0; i < menus.size(); i++) {
                JSONObject m = menus.getJSONObject(i);
                if (m.getString("parentId") == null || m.getString("parentId").isEmpty()) {
                    maxSort = Math.max(maxSort, m.getIntValue("sort"));
                }
            }
            sort = maxSort + 1;
        }
        
        JSONObject menuJson = convertFromMenuItem(menu);
        menuJson.put("parentId", parentId);
        menuJson.put("level", level);
        menuJson.put("sort", sort);
        menus.add(menuJson);
        
        menuConfig.put("updatedAt", System.currentTimeMillis());
        saveConfig();
        log.info("Added child menu {} to role {}, parentId: {}", menu.getId(), roleId, parentId);
    }
    
    @Override
    public void updateMenu(String roleId, String menuId, MenuItemDTO menu) {
        if (menuConfig == null) {
            return;
        }
        
        JSONObject roles = menuConfig.getJSONObject("roles");
        if (roles == null) {
            return;
        }
        
        JSONObject role = roles.getJSONObject(roleId);
        if (role == null) {
            return;
        }
        
        JSONArray menus = role.getJSONArray("menus");
        if (menus == null) {
            return;
        }
        
        for (int i = 0; i < menus.size(); i++) {
            JSONObject m = menus.getJSONObject(i);
            if (menuId.equals(m.getString("id"))) {
                m.put("name", menu.getName());
                m.put("url", menu.getUrl());
                m.put("icon", menu.getIcon());
                m.put("active", menu.isActive());
                if (menu.getSort() > 0) {
                    m.put("sort", menu.getSort());
                }
                break;
            }
        }
        
        menuConfig.put("updatedAt", System.currentTimeMillis());
        saveConfig();
        log.info("Updated menu {} in role {}", menuId, roleId);
    }
    
    @Override
    public void deleteMenuWithChildren(String roleId, String menuId) {
        if (menuConfig == null) {
            return;
        }
        
        JSONObject roles = menuConfig.getJSONObject("roles");
        if (roles == null) {
            return;
        }
        
        JSONObject role = roles.getJSONObject(roleId);
        if (role == null) {
            return;
        }
        
        JSONArray menus = role.getJSONArray("menus");
        if (menus == null) {
            return;
        }
        
        Set<String> toDelete = new HashSet<>();
        toDelete.add(menuId);
        
        boolean found = true;
        while (found) {
            found = false;
            for (int i = 0; i < menus.size(); i++) {
                JSONObject m = menus.getJSONObject(i);
                String parentId = m.getString("parentId");
                if (parentId != null && toDelete.contains(parentId) && !toDelete.contains(m.getString("id"))) {
                    toDelete.add(m.getString("id"));
                    found = true;
                }
            }
        }
        
        JSONArray newMenus = new JSONArray();
        for (int i = 0; i < menus.size(); i++) {
            JSONObject m = menus.getJSONObject(i);
            if (!toDelete.contains(m.getString("id"))) {
                newMenus.add(m);
            }
        }
        
        role.put("menus", newMenus);
        menuConfig.put("updatedAt", System.currentTimeMillis());
        saveConfig();
        log.info("Deleted menu {} and {} children in role {}", menuId, toDelete.size() - 1, roleId);
    }
    
    @Override
    public void moveMenu(String roleId, String menuId, String newParentId, int newSort) {
        if (menuConfig == null) {
            return;
        }
        
        JSONObject roles = menuConfig.getJSONObject("roles");
        if (roles == null) {
            return;
        }
        
        JSONObject role = roles.getJSONObject(roleId);
        if (role == null) {
            return;
        }
        
        JSONArray menus = role.getJSONArray("menus");
        if (menus == null) {
            return;
        }
        
        for (int i = 0; i < menus.size(); i++) {
            JSONObject m = menus.getJSONObject(i);
            if (menuId.equals(m.getString("id"))) {
                int newLevel = 0;
                
                if (newParentId != null && !newParentId.isEmpty()) {
                    for (int j = 0; j < menus.size(); j++) {
                        JSONObject parent = menus.getJSONObject(j);
                        if (newParentId.equals(parent.getString("id"))) {
                            newLevel = parent.getIntValue("level") + 1;
                            break;
                        }
                    }
                }
                
                m.put("parentId", newParentId);
                m.put("level", newLevel);
                m.put("sort", newSort);
                break;
            }
        }
        
        menuConfig.put("updatedAt", System.currentTimeMillis());
        saveConfig();
        log.info("Moved menu {} in role {} to parentId: {}, sort: {}", menuId, roleId, newParentId, newSort);
    }
    
    @Override
    public void updateRoleMenus(String roleId, List<MenuItemDTO> menus) {
        if (menuConfig == null) {
            return;
        }
        
        JSONObject roles = menuConfig.getJSONObject("roles");
        if (roles == null) {
            return;
        }
        
        JSONObject role = roles.getJSONObject(roleId);
        if (role == null) {
            role = new JSONObject();
            role.put("id", roleId);
            role.put("name", roleId);
            roles.put(roleId, role);
        }
        
        JSONArray menuArray = convertFromMenuItemList(menus);
        role.put("menus", menuArray);
        menuConfig.put("updatedAt", System.currentTimeMillis());
        saveConfig();
    }
    
    @Override
    public List<MenuItemDTO> getUserMenusAsDTO(String userId) {
        List<MenuItemDTO> items = new ArrayList<>();
        
        if (menuConfig == null) {
            return items;
        }
        
        JSONObject users = menuConfig.getJSONObject("users");
        if (users == null || !users.containsKey(userId)) {
            return items;
        }
        
        JSONObject user = users.getJSONObject(userId);
        JSONArray menus = user.getJSONArray("menus");
        if (menus == null) {
            return items;
        }
        
        for (int i = 0; i < menus.size(); i++) {
            JSONObject menu = menus.getJSONObject(i);
            items.add(convertToMenuItem(menu));
        }
        
        return items;
    }
    
    @Override
    public void updateUserMenus(String userId, List<MenuItemDTO> menus) {
        if (menuConfig == null) {
            return;
        }
        
        JSONObject users = menuConfig.getJSONObject("users");
        if (users == null) {
            users = new JSONObject();
            menuConfig.put("users", users);
        }
        
        JSONObject user = users.getJSONObject(userId);
        if (user == null) {
            user = new JSONObject();
            user.put("userId", userId);
            users.put(userId, user);
        }
        
        JSONArray menuArray = convertFromMenuItemList(menus);
        user.put("menus", menuArray);
        menuConfig.put("updatedAt", System.currentTimeMillis());
        saveConfig();
        log.info("Updated menus for user: {}", userId);
    }
    
    @Override
    public String exportConfig() {
        if (menuConfig == null) {
            return "{}";
        }
        return JSON.toJSONString(menuConfig, JSONWriter.Feature.PrettyFormat);
    }
    
    @Override
    public void importConfig(String jsonContent) {
        try {
            menuConfig = JSON.parseObject(jsonContent);
            saveConfig();
            log.info("Imported menu config");
        } catch (Exception e) {
            log.error("Failed to import menu config: {}", e.getMessage());
            throw new RuntimeException("导入配置失败: " + e.getMessage());
        }
    }
    
    private MenuItemDTO convertToMenuItem(JSONObject menu) {
        MenuItemDTO item = new MenuItemDTO();
        item.setId(menu.getString("id"));
        item.setParentId(menu.getString("parentId"));
        item.setName(menu.getString("name"));
        item.setUrl(menu.getString("url"));
        item.setIcon(menu.getString("icon"));
        item.setSort(menu.getIntValue("sort"));
        item.setActive(menu.getBooleanValue("active"));
        item.setLevel(menu.getIntValue("level"));
        
        JSONArray children = menu.getJSONArray("children");
        if (children != null && !children.isEmpty()) {
            List<MenuItemDTO> childList = new ArrayList<>();
            for (int i = 0; i < children.size(); i++) {
                childList.add(convertToMenuItem(children.getJSONObject(i)));
            }
            item.setChildren(childList);
        }
        
        return item;
    }
    
    private JSONObject convertFromMenuItem(MenuItemDTO item) {
        JSONObject json = new JSONObject();
        json.put("id", item.getId());
        json.put("parentId", item.getParentId());
        json.put("name", item.getName());
        json.put("url", item.getUrl());
        json.put("icon", item.getIcon());
        json.put("sort", item.getSort());
        json.put("active", item.isActive());
        json.put("level", item.getLevel());
        
        if (item.getChildren() != null && !item.getChildren().isEmpty()) {
            JSONArray childrenArray = new JSONArray();
            for (MenuItemDTO child : item.getChildren()) {
                childrenArray.add(convertFromMenuItem(child));
            }
            json.put("children", childrenArray);
        }
        
        return json;
    }
    
    private JSONArray convertFromMenuItemList(List<MenuItemDTO> items) {
        JSONArray array = new JSONArray();
        if (items == null) {
            return array;
        }
        for (MenuItemDTO item : items) {
            array.add(convertFromMenuItem(item));
        }
        return array;
    }
}
