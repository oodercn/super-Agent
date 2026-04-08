package net.ooder.mvp.skill.scene.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.ooder.mvp.skill.scene.dto.LoginRequest;
import net.ooder.mvp.skill.scene.dto.UserSessionDTO;
import net.ooder.mvp.skill.scene.dto.menu.MenuItemDTO;
import net.ooder.mvp.skill.scene.dto.OrgUserDTO;
import net.ooder.mvp.skill.scene.service.MenuRoleConfigService;
import net.ooder.mvp.skill.scene.adapter.OrgWebAdapter;
import net.ooder.mvp.skill.scene.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@RestController("sceneAuthController")
@RequestMapping("/api/v1/scene-auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthMenuController {

    private static final Logger log = LoggerFactory.getLogger(AuthMenuController.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private MenuRoleConfigService menuRoleConfigService;

    @Autowired
    private OrgWebAdapter orgWebAdapter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/login")
    public ResultModel<UserSessionDTO> login(@RequestBody LoginRequest request, HttpSession session) {
        log.info("[login] Login attempt: username={}, role={}", request.getUsername(), request.getRole());
        
        String role = request.getRole();
        if (role == null || role.isEmpty()) {
            role = "collaborator";
        }
        
        final String finalRole = role;
        
        List<OrgUserDTO> users = orgWebAdapter.getUsersByRole(finalRole);
        OrgUserDTO matchedUser = null;
        
        if (users != null && !users.isEmpty()) {
            matchedUser = users.get(0);
        }
        
        if (matchedUser == null) {
            matchedUser = orgWebAdapter.getAllUsers().stream()
                .filter(u -> finalRole.equals(u.getRole()))
                .findFirst()
                .orElse(null);
        }
        
        if (matchedUser == null) {
            matchedUser = new OrgUserDTO();
            matchedUser.setUserId("user-" + finalRole + "-001");
            matchedUser.setName(getRoleDisplayName(finalRole));
            matchedUser.setEmail(finalRole + "@ooder.local");
            matchedUser.setRole(finalRole);
            matchedUser.setActive(true);
        }
        
        UserSessionDTO sessionDTO = new UserSessionDTO();
        sessionDTO.setUserId(matchedUser.getUserId());
        sessionDTO.setUsername(request.getUsername());
        sessionDTO.setName(matchedUser.getName());
        sessionDTO.setEmail(matchedUser.getEmail());
        sessionDTO.setRole(role);
        sessionDTO.setRoleType(role);
        sessionDTO.setDepartmentId(matchedUser.getDepartmentId());
        sessionDTO.setDepartmentName(matchedUser.getDepartmentName());
        sessionDTO.setTitle(matchedUser.getTitle());
        sessionDTO.setLoginTime(System.currentTimeMillis());
        sessionDTO.setToken(UUID.randomUUID().toString());
        
        session.setAttribute("user", sessionDTO);
        
        log.info("[login] Login successful: userId={}, name={}, role={}", 
            sessionDTO.getUserId(), sessionDTO.getName(), sessionDTO.getRoleType());
        
        return ResultModel.success(sessionDTO);
    }
    
    @PostMapping("/logout")
    public ResultModel<Void> logout(HttpSession session) {
        log.info("[logout] User logging out");
        session.invalidate();
        return ResultModel.success(null);
    }
    
    @GetMapping("/session")
    public ResultModel<UserSessionDTO> getSession(HttpSession session) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        
        if (user == null) {
            log.info("[getSession] No active session, creating default admin session");
            
            user = new UserSessionDTO();
            user.setUserId("user-admin-001");
            user.setUsername("admin");
            user.setName("系统管理员");
            user.setEmail("admin@ooder.local");
            user.setRole("admin");
            user.setRoleType("admin");
            user.setDepartmentId("dept-it");
            user.setDepartmentName("信息技术部");
            user.setTitle("系统管理员");
            user.setLoginTime(System.currentTimeMillis());
            user.setToken(UUID.randomUUID().toString());
            
            session.setAttribute("user", user);
        }
        
        log.info("[getSession] Session user: userId={}, name={}, role={}", 
            user.getUserId(), user.getName(), user.getRoleType());
        
        return ResultModel.success(user);
    }
    
    private String getRoleDisplayName(String role) {
        Map<String, String> roleNames = new HashMap<>();
        roleNames.put("admin", "系统管理员");
        roleNames.put("installer", "系统安装者");
        roleNames.put("leader", "主导者");
        roleNames.put("collaborator", "协作者");
        roleNames.put("manager", "管理者");
        roleNames.put("employee", "员工");
        roleNames.put("hr", "HR");
        roleNames.put("developer", "开发者");
        roleNames.put("user", "普通用户");
        
        return roleNames.getOrDefault(role, role);
    }

    @GetMapping("/menu-config")
    public ResultModel<List<Map<String, Object>>> getMenuConfig(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String userId,
            HttpServletRequest request) {
        
        String roleType = role;
        if (roleType == null) {
            roleType = "collaborator";
        }
        
        String actualUserId = userId;
        if (actualUserId == null) {
            actualUserId = request.getHeader("X-User-Id");
        }
        if (actualUserId == null) {
            actualUserId = "default-user";
        }
        
        log.info("[getMenuConfig] Loading menus for role: {}, userId: {}", roleType, actualUserId);
        
        try {
            List<Map<String, Object>> staticMenus = loadAndFilterMenuConfig(roleType);
            
            List<MenuItemDTO> dynamicMenus = menuRoleConfigService.getFinalMenusForUserWithScene(actualUserId, roleType);
            
            List<Map<String, Object>> mergedMenus = mergeMenus(staticMenus, dynamicMenus);
            
            log.info("[getMenuConfig] Returning {} menus (static: {}, dynamic: {})", 
                mergedMenus.size(), staticMenus.size(), dynamicMenus.size());
            
            return ResultModel.success(mergedMenus);
        } catch (IOException e) {
            log.error("[getMenuConfig] Failed to load menu config: {}", e.getMessage());
            return ResultModel.error("加载菜单配置失败: " + e.getMessage());
        }
    }

    private List<Map<String, Object>> loadAndFilterMenuConfig(String roleType) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/console/menu-config.json");
        JsonNode rootNode = objectMapper.readTree(resource.getInputStream());
        JsonNode menuNode = rootNode.get("menu");
        
        List<Map<String, Object>> result = new ArrayList<>();
        if (menuNode != null && menuNode.isArray()) {
            for (JsonNode item : menuNode) {
                Map<String, Object> filteredItem = filterMenuItem(item, roleType);
                if (filteredItem != null) {
                    result.add(filteredItem);
                }
            }
        }
        
        return result;
    }

    private Map<String, Object> filterMenuItem(JsonNode item, String roleType) {
        JsonNode rolesNode = item.get("roles");
        if (rolesNode == null || !rolesNode.isArray()) {
            return null;
        }
        
        boolean hasRole = false;
        for (JsonNode r : rolesNode) {
            if (r.asText().equals(roleType)) {
                hasRole = true;
                break;
            }
        }
        
        if (!hasRole) {
            return null;
        }
        
        JsonNode statusNode = item.get("status");
        if (statusNode != null && !"implemented".equals(statusNode.asText())) {
            return null;
        }
        
        Map<String, Object> result = new LinkedHashMap<>();
        
        if (item.has("id")) result.put("id", item.get("id").asText());
        if (item.has("name")) result.put("name", item.get("name").asText());
        if (item.has("url")) result.put("url", item.get("url").asText());
        if (item.has("icon")) result.put("icon", item.get("icon").asText());
        if (item.has("sort")) result.put("sort", item.get("sort").asInt());
        if (item.has("order")) result.put("order", item.get("order").asInt());
        result.put("visible", true);
        result.put("active", false);
        
        JsonNode childrenNode = item.get("children");
        if (childrenNode != null && childrenNode.isArray()) {
            List<Map<String, Object>> children = new ArrayList<>();
            for (JsonNode child : childrenNode) {
                Map<String, Object> filteredChild = filterMenuItem(child, roleType);
                if (filteredChild != null) {
                    children.add(filteredChild);
                }
            }
            result.put("children", children);
        } else {
            result.put("children", new ArrayList<>());
        }
        
        return result;
    }

    private List<Map<String, Object>> mergeMenus(List<Map<String, Object>> staticMenus, List<MenuItemDTO> dynamicMenus) {
        log.info("[mergeMenus] 动态菜单不添加到一级目录，共 {} 项", dynamicMenus.size());
        return staticMenus;
    }

    private Map<String, Object> convertDtoToMap(MenuItemDTO dto) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", dto.getId());
        map.put("name", dto.getName());
        map.put("url", dto.getUrl());
        map.put("icon", dto.getIcon());
        map.put("sort", dto.getSort() > 0 ? dto.getSort() : 999);
        map.put("order", dto.getSort() > 0 ? dto.getSort() : 999);
        map.put("visible", dto.isVisible());
        map.put("active", dto.isActive());
        
        if (dto.getChildren() != null && !dto.getChildren().isEmpty()) {
            List<Map<String, Object>> children = new ArrayList<>();
            for (MenuItemDTO child : dto.getChildren()) {
                children.add(convertDtoToMap(child));
            }
            map.put("children", children);
        } else {
            map.put("children", new ArrayList<>());
        }
        
        return map;
    }
}
