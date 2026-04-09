package net.ooder.enexus.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.ooder.enexus.dto.menu.MenuConfigDTO;
import net.ooder.enexus.dto.menu.MenuItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class MenuRoleConfigService {

    private static final Logger log = LoggerFactory.getLogger(MenuRoleConfigService.class);
    
    private static final String CONFIG_DIR = "data/config";
    private static final String CONFIG_FILE = "menu-role-config.json";
    
    private JSONObject menuConfig = null;
    
    @PostConstruct
    public void init() {
        loadConfig();
        log.info("MenuRoleConfigService initialized");
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
            String content = JSON.toJSONString(menuConfig, true);
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
        roles.put("installer", createRoleConfig("installer", "安装者", getInstallerMenus()));
        roles.put("leader", createRoleConfig("leader", "主导者", getLeaderMenus()));
        roles.put("collaborator", createRoleConfig("collaborator", "协作者", getCollaboratorMenus()));
        
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
        menus.add(createMenuItem("menu-admin-1", "工作台", "/console/pages/role-admin.html", "ri-home-line", 1, true));
        menus.add(createMenuItem("menu-admin-2", "能力市场", "/console/pages/capability-discovery.html", "ri-store-2-line", 2, false));
        menus.add(createMenuItem("menu-admin-3", "已安装能力", "/console/pages/my-capabilities.html", "ri-download-cloud-line", 3, false));
        menus.add(createMenuItem("menu-admin-4", "场景管理", "/console/pages/scene-group-management.html", "ri-folder-line", 4, false));
        menus.add(createMenuItem("menu-admin-5", "组织管理", "/console/pages/org-management.html", "ri-team-line", 5, false));
        menus.add(createMenuItem("menu-admin-6", "系统配置", "/console/pages/llm-config.html", "ri-settings-3-line", 6, false));
        menus.add(createMenuItem("menu-admin-7", "审计日志", "/console/pages/audit-logs.html", "ri-file-list-line", 7, false));
        return menus;
    }
    
    private JSONArray getInstallerMenus() {
        JSONArray menus = new JSONArray();
        menus.add(createMenuItem("menu-installer-1", "工作台", "/console/pages/role-installer.html", "ri-home-line", 1, true));
        menus.add(createMenuItem("menu-installer-2", "能力市场", "/console/pages/capability-discovery.html", "ri-store-2-line", 2, false));
        menus.add(createMenuItem("menu-installer-3", "已安装能力", "/console/pages/my-capabilities.html", "ri-download-cloud-line", 3, false));
        menus.add(createMenuItem("menu-installer-4", "场景管理", "/console/pages/scene-group-management.html", "ri-folder-line", 4, false));
        menus.add(createMenuItem("menu-installer-5", "系统配置", "/console/pages/llm-config.html", "ri-settings-3-line", 5, false));
        return menus;
    }
    
    private JSONArray getLeaderMenus() {
        JSONArray menus = new JSONArray();
        menus.add(createMenuItem("menu-leader-1", "工作台", "/console/pages/role-leader.html", "ri-home-line", 1, true));
        menus.add(createMenuItem("menu-leader-2", "能力市场", "/console/pages/capability-discovery.html", "ri-store-2-line", 2, false));
        menus.add(createMenuItem("menu-leader-3", "我的能力", "/console/pages/my-capabilities.html", "ri-puzzle-line", 3, false));
        menus.add(createMenuItem("menu-leader-4", "我的场景", "/console/pages/my-scenes.html", "ri-artboard-line", 4, false));
        menus.add(createMenuItem("menu-leader-5", "我的待办", "/console/pages/my-todos.html", "ri-task-line", 5, false));
        return menus;
    }
    
    private JSONArray getCollaboratorMenus() {
        JSONArray menus = new JSONArray();
        menus.add(createMenuItem("menu-collab-1", "工作台", "/console/pages/role-collaborator.html", "ri-home-line", 1, true));
        menus.add(createMenuItem("menu-collab-2", "能力市场", "/console/pages/capability-discovery.html", "ri-store-2-line", 2, false));
        menus.add(createMenuItem("menu-collab-3", "我的场景", "/console/pages/my-scenes.html", "ri-artboard-line", 3, false));
        menus.add(createMenuItem("menu-collab-4", "我的待办", "/console/pages/my-todos.html", "ri-task-line", 4, false));
        menus.add(createMenuItem("menu-collab-5", "历史记录", "/console/pages/my-history.html", "ri-history-line", 5, false));
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
            role = roles.getJSONObject("collaborator");
        }
        
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
    
    public Map<String, MenuConfigDTO.MenuRoleDTO> getAllRolesAsDTO() {
        Map<String, MenuConfigDTO.MenuRoleDTO> result = new HashMap<>();
        
        if (menuConfig == null) {
            return result;
        }
        
        JSONObject roles = menuConfig.getJSONObject("roles");
        if (roles == null) {
            return result;
        }
        
        for (String roleId : roles.keySet()) {
            JSONObject roleJson = roles.getJSONObject(roleId);
            MenuConfigDTO.MenuRoleDTO roleDTO = new MenuConfigDTO.MenuRoleDTO();
            roleDTO.setId(roleJson.getString("id"));
            roleDTO.setName(roleJson.getString("name"));
            roleDTO.setDescription(roleJson.getString("description"));
            roleDTO.setIcon(roleJson.getString("icon"));
            roleDTO.setMenus(convertToMenuConfigDTOList(roleJson.getJSONArray("menus")));
            result.put(roleId, roleDTO);
        }
        
        return result;
    }
    
    private List<MenuConfigDTO> convertToMenuConfigDTOList(JSONArray menus) {
        List<MenuConfigDTO> items = new ArrayList<>();
        if (menus == null) return items;
        
        for (int i = 0; i < menus.size(); i++) {
            JSONObject menu = menus.getJSONObject(i);
            items.add(convertToMenuConfigDTO(menu));
        }
        return items;
    }
    
    private MenuConfigDTO convertToMenuConfigDTO(JSONObject menu) {
        MenuConfigDTO item = new MenuConfigDTO();
        item.setId(menu.getString("id"));
        item.setName(menu.getString("name"));
        item.setUrl(menu.getString("url"));
        item.setIcon(menu.getString("icon"));
        item.setOrder(menu.getIntValue("sort"));
        item.setVisible(true);
        return item;
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
        return item;
    }
    
    public List<MenuItemDTO> getFinalMenusForUserWithScene(String userId, String roleId) {
        return getMenusByRole(roleId);
    }
    
    public String exportConfig() {
        if (menuConfig == null) return "{}";
        return JSON.toJSONString(menuConfig, true);
    }
    
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
    
    public MenuItemDTO addMenuToRole(String roleId, MenuItemDTO menu) {
        if (menuConfig == null) {
            throw new RuntimeException("菜单配置未初始化");
        }
        
        JSONObject roles = menuConfig.getJSONObject("roles");
        if (roles == null) {
            throw new RuntimeException("角色配置不存在");
        }
        
        JSONObject role = roles.getJSONObject(roleId);
        if (role == null) {
            role = roles.getJSONObject("collaborator");
            if (role == null) {
                throw new RuntimeException("角色不存在: " + roleId);
            }
        }
        
        JSONArray menus = role.getJSONArray("menus");
        if (menus == null) {
            menus = new JSONArray();
            role.put("menus", menus);
        }
        
        JSONObject newMenu = new JSONObject();
        newMenu.put("id", menu.getId() != null ? menu.getId() : "menu-" + System.currentTimeMillis());
        newMenu.put("name", menu.getName());
        newMenu.put("url", menu.getUrl());
        newMenu.put("icon", menu.getIcon());
        newMenu.put("sort", menu.getSort());
        newMenu.put("active", false);
        newMenu.put("parentId", null);
        newMenu.put("level", 0);
        
        menus.add(newMenu);
        saveConfig();
        
        log.info("[addMenuToRole] Added menu {} to role {}", menu.getName(), roleId);
        
        return convertToMenuItem(newMenu);
    }
    
    public void setMenusForRole(String roleId, List<MenuItemDTO> menuItems) {
        if (menuConfig == null) {
            throw new RuntimeException("菜单配置未初始化");
        }
        
        JSONObject roles = menuConfig.getJSONObject("roles");
        if (roles == null) {
            throw new RuntimeException("角色配置不存在");
        }
        
        JSONObject role = roles.getJSONObject(roleId);
        if (role == null) {
            role = roles.getJSONObject("collaborator");
            if (role == null) {
                throw new RuntimeException("角色不存在: " + roleId);
            }
        }
        
        JSONArray menus = new JSONArray();
        for (MenuItemDTO item : menuItems) {
            JSONObject menu = new JSONObject();
            menu.put("id", item.getId());
            menu.put("name", item.getName());
            menu.put("url", item.getUrl());
            menu.put("icon", item.getIcon());
            menu.put("sort", item.getSort());
            menu.put("active", item.isActive());
            menu.put("parentId", item.getParentId());
            menu.put("level", item.getLevel());
            menus.add(menu);
        }
        
        role.put("menus", menus);
        saveConfig();
        
        log.info("[setMenusForRole] Set {} menus for role {}", menuItems.size(), roleId);
    }
    
    public boolean removeMenuFromRole(String roleId, String menuId) {
        if (menuConfig == null) {
            return false;
        }
        
        JSONObject roles = menuConfig.getJSONObject("roles");
        if (roles == null) {
            return false;
        }
        
        JSONObject role = roles.getJSONObject(roleId);
        if (role == null) {
            role = roles.getJSONObject("collaborator");
            if (role == null) {
                return false;
            }
        }
        
        JSONArray menus = role.getJSONArray("menus");
        if (menus == null) {
            return false;
        }
        
        for (int i = 0; i < menus.size(); i++) {
            JSONObject menu = menus.getJSONObject(i);
            if (menuId.equals(menu.getString("id"))) {
                menus.remove(i);
                saveConfig();
                log.info("[removeMenuFromRole] Removed menu {} from role {}", menuId, roleId);
                return true;
            }
        }
        
        return false;
    }
}
