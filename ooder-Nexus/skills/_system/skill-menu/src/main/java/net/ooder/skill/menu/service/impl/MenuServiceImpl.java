package net.ooder.skill.menu.service.impl;

import net.ooder.skill.menu.dto.MenuDTO;
import net.ooder.skill.menu.entity.Menu;
import net.ooder.skill.menu.repository.MenuRepository;
import net.ooder.skill.menu.service.MenuService;
import net.ooder.nexus.skill.registry.SkillRegistry;
import net.ooder.nexus.skill.registry.SkillRegistry.SkillInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);
    
    @Autowired
    private MenuRepository menuRepository;
    
    @Autowired(required = false)
    private SkillRegistry skillRegistry;
    
    @PostConstruct
    public void init() {
        long count = menuRepository.count();
        if (count == 0) {
            log.info("[init] No menus found, initializing default menus");
            initializeDefaultMenus();
        } else {
            log.info("[init] Found {} existing menus", count);
        }
    }
    
    @Override
    @Transactional
    public void initializeDefaultMenus() {
        List<Menu> defaultMenus = Arrays.asList(
            createMenuEntity("dashboard", "首页", "首页", "/console/pages/dashboard.html", "ri-home-line", null, 0, "system", null),
            createMenuEntity("capability-center", "能力中心", "能力中心", null, "ri-puzzle-line", null, 1, "system", null),
            createMenuEntity("capability-discovery", "能力发现", "能力发现", "/console/pages/capability-discovery.html", "ri-search-line", "capability-center", 0, "system", null),
            createMenuEntity("plugins-management", "插件管理", "插件管理", "/console/pages/plugins-management.html", "ri-plug-line", "capability-center", 1, "system", null),
            createMenuEntity("llm-service", "LLM 服务", "LLM 服务", null, "ri-robot-line", null, 2, "system", null),
            createMenuEntity("llm-chat", "LLM 对话", "LLM 对话", "/console/pages/workbench.html", "ri-chat-3-line", "llm-service", 0, "system", "skill-llm-chat"),
            createMenuEntity("llm-config", "LLM 配置", "LLM 配置", "/console/pages/llm-config.html", "ri-settings-3-line", "llm-service", 1, "system", null),
            createMenuEntity("system-management", "系统管理", "系统管理", null, "ri-settings-4-line", null, 3, "system", null),
            createMenuEntity("system-config", "系统配置", "系统配置", "/console/pages/config-system.html", "ri-tools-line", "system-management", 0, "system", null),
            createMenuEntity("db-config", "数据库配置", "数据库配置", "/console/pages/db-config.html", "ri-database-2-line", "system-management", 1, "system", null)
        );
        
        menuRepository.saveAll(defaultMenus);
        log.info("[initializeDefaultMenus] Initialized {} default menus", defaultMenus.size());
    }
    
    private Menu createMenuEntity(String menuId, String name, String title, String url, 
                                   String icon, String parentId, int sort, String category, String requiredSkill) {
        Menu menu = new Menu();
        menu.setMenuId(menuId);
        menu.setName(name);
        menu.setTitle(title);
        menu.setUrl(url);
        menu.setIcon(icon);
        menu.setParentId(parentId);
        menu.setSort(sort);
        menu.setCategory(category);
        menu.setRequiredSkill(requiredSkill);
        menu.setVisible(true);
        menu.setEnabled(true);
        menu.setDeleted(false);
        return menu;
    }
    
    @Override
    @Transactional
    public MenuDTO createMenu(MenuDTO menuDTO) {
        if (menuRepository.existsByMenuId(menuDTO.getMenuId())) {
            throw new RuntimeException("Menu already exists: " + menuDTO.getMenuId());
        }
        
        Menu menu = convertToEntity(menuDTO);
        menu = menuRepository.save(menu);
        log.info("[createMenu] Created menu: {}", menu.getMenuId());
        return convertToDTO(menu);
    }
    
    @Override
    @Transactional
    public MenuDTO updateMenu(String menuId, MenuDTO menuDTO) {
        Menu menu = menuRepository.findByMenuId(menuId)
            .orElseThrow(() -> new RuntimeException("Menu not found: " + menuId));
        
        menu.setName(menuDTO.getName());
        menu.setTitle(menuDTO.getTitle());
        menu.setUrl(menuDTO.getUrl());
        menu.setIcon(menuDTO.getIcon());
        menu.setParentId(menuDTO.getParentId());
        menu.setSort(menuDTO.getSort());
        menu.setCategory(menuDTO.getCategory());
        menu.setRequiredSkill(menuDTO.getRequiredSkill());
        menu.setVisible(menuDTO.getVisible());
        menu.setEnabled(menuDTO.getEnabled());
        menu.setDescription(menuDTO.getDescription());
        
        menu = menuRepository.save(menu);
        log.info("[updateMenu] Updated menu: {}", menuId);
        return convertToDTO(menu);
    }
    
    @Override
    @Transactional
    public void deleteMenu(String menuId) {
        Menu menu = menuRepository.findByMenuId(menuId)
            .orElseThrow(() -> new RuntimeException("Menu not found: " + menuId));
        
        menu.setDeleted(true);
        menuRepository.save(menu);
        log.info("[deleteMenu] Deleted menu: {}", menuId);
    }
    
    @Override
    public MenuDTO getMenu(String menuId) {
        return menuRepository.findByMenuId(menuId)
            .map(this::convertToDTO)
            .orElse(null);
    }
    
    @Override
    public List<MenuDTO> getAllMenus() {
        return menuRepository.findAllNotDeleted().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<MenuDTO> getMenuTree() {
        Set<String> installedSkills = getInstalledSkills();
        log.info("[getMenuTree] Installed skills: {}", installedSkills);
        
        List<Menu> allMenus = menuRepository.findRootMenus();
        
        List<MenuDTO> rootMenus = allMenus.stream()
            .filter(menu -> shouldShowMenu(menu, installedSkills))
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        for (MenuDTO rootMenu : rootMenus) {
            loadChildren(rootMenu, installedSkills);
        }
        
        return rootMenus;
    }
    
    private void loadChildren(MenuDTO parentMenu, Set<String> installedSkills) {
        List<Menu> children = menuRepository.findVisibleByParentId(parentMenu.getMenuId());
        
        List<MenuDTO> childDTOs = children.stream()
            .filter(menu -> shouldShowMenu(menu, installedSkills))
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        for (MenuDTO childDTO : childDTOs) {
            loadChildren(childDTO, installedSkills);
        }
        
        parentMenu.setChildren(childDTOs);
    }
    
    @Override
    public List<MenuDTO> getMenusByCategory(String category) {
        return menuRepository.findByCategoryOrderBySortAsc(category).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    private Set<String> getInstalledSkills() {
        Set<String> installedSkills = new HashSet<>();
        
        if (skillRegistry != null) {
            try {
                List<SkillInfo> skills = skillRegistry.getInstalledSkills();
                if (skills != null) {
                    for (SkillInfo skill : skills) {
                        if (skill.getSkillId() != null) {
                            installedSkills.add(skill.getSkillId());
                        }
                    }
                }
                log.info("[getInstalledSkills] Found {} installed skills", installedSkills.size());
            } catch (Exception e) {
                log.error("[getInstalledSkills] Failed to get installed skills: {}", e.getMessage());
            }
        }
        
        return installedSkills;
    }
    
    private boolean shouldShowMenu(Menu menu, Set<String> installedSkills) {
        if (menu.getRequiredSkill() == null || menu.getRequiredSkill().isEmpty()) {
            return true;
        }
        
        return installedSkills.contains(menu.getRequiredSkill());
    }
    
    private Menu convertToEntity(MenuDTO dto) {
        Menu menu = new Menu();
        menu.setMenuId(dto.getMenuId());
        menu.setName(dto.getName());
        menu.setTitle(dto.getTitle());
        menu.setUrl(dto.getUrl());
        menu.setIcon(dto.getIcon());
        menu.setParentId(dto.getParentId());
        menu.setSort(dto.getSort() != null ? dto.getSort() : 0);
        menu.setCategory(dto.getCategory());
        menu.setRequiredSkill(dto.getRequiredSkill());
        menu.setVisible(dto.getVisible() != null ? dto.getVisible() : true);
        menu.setEnabled(dto.getEnabled() != null ? dto.getEnabled() : true);
        menu.setDescription(dto.getDescription());
        menu.setDeleted(false);
        return menu;
    }
    
    private MenuDTO convertToDTO(Menu menu) {
        MenuDTO dto = new MenuDTO();
        dto.setId(menu.getId());
        dto.setMenuId(menu.getMenuId());
        dto.setName(menu.getName());
        dto.setTitle(menu.getTitle());
        dto.setUrl(menu.getUrl());
        dto.setIcon(menu.getIcon());
        dto.setParentId(menu.getParentId());
        dto.setSort(menu.getSort());
        dto.setCategory(menu.getCategory());
        dto.setRequiredSkill(menu.getRequiredSkill());
        dto.setVisible(menu.getVisible());
        dto.setEnabled(menu.getEnabled());
        dto.setDescription(menu.getDescription());
        return dto;
    }
}
