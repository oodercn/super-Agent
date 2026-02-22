package net.ooder.nexus.skillcenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ooder.nexus.skillcenter.dto.menu.MenuItemDTO;
import net.ooder.nexus.skillcenter.dto.menu.MenuRequestDTO;
import net.ooder.nexus.skillcenter.model.ResultModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class MenuController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResultModel<List<MenuItemDTO>> getMenu(@RequestBody(required = false) MenuRequestDTO request) {
        try {
            ClassPathResource resource = new ClassPathResource("static/console/menu-config.json");
            if (resource.exists()) {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
                );
                String content = reader.lines().collect(Collectors.joining("\n"));
                reader.close();
                
                List<MenuItemDTO> menuItems = parseMenuData(content);
                return ResultModel.success(menuItems);
            } else {
                return ResultModel.notFound("菜单配置文件不存在");
            }
        } catch (Exception e) {
            return ResultModel.error("加载菜单失败: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private List<MenuItemDTO> parseMenuData(String jsonContent) {
        try {
            Map<String, Object> config = objectMapper.readValue(jsonContent, Map.class);
            List<Map<String, Object>> menuList = (List<Map<String, Object>>) config.get("navigation");
            if (menuList == null) {
                menuList = (List<Map<String, Object>>) config.get("menu");
            }
            if (menuList == null) {
                return java.util.Collections.emptyList();
            }
            return menuList.stream()
                .map(this::mapToMenuItemDTO)
                .collect(Collectors.toList());
        } catch (Exception e) {
            return java.util.Collections.emptyList();
        }
    }
    
    @SuppressWarnings("unchecked")
    private MenuItemDTO mapToMenuItemDTO(Map<String, Object> map) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setId((String) map.get("id"));
        dto.setName((String) map.get("name"));
        dto.setIcon((String) map.get("icon"));
        String url = (String) map.get("url");
        if (url == null) {
            url = (String) map.get("path");
        }
        dto.setUrl(url);
        
        List<String> roles = (List<String>) map.get("roles");
        dto.setRoles(roles);
        
        List<Map<String, Object>> children = (List<Map<String, Object>>) map.get("children");
        if (children != null && !children.isEmpty()) {
            List<MenuItemDTO> childDTOs = children.stream()
                .map(this::mapToMenuItemDTO)
                .collect(Collectors.toList());
            dto.setChildren(childDTOs);
        }
        
        return dto;
    }
}
