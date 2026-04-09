package net.ooder.enexus.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.ooder.enexus.dto.menu.MenuItemDTO;
import net.ooder.enexus.service.MenuRoleConfigService;
import net.ooder.enexus.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@RestController("authMenuController")
@RequestMapping("/api/v1/scene-auth")
public class AuthMenuController {

    private static final Logger log = LoggerFactory.getLogger(AuthMenuController.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private MenuRoleConfigService menuRoleConfigService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
}
