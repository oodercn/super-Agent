package net.ooder.skill.menu.controller;

import net.ooder.skill.menu.dto.MenuDTO;
import net.ooder.skill.menu.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menus")
public class MenuController {

    private static final Logger log = LoggerFactory.getLogger(MenuController.class);
    
    @Autowired
    private MenuService menuService;
    
    @PostMapping
    public ResponseEntity<MenuDTO> createMenu(@RequestBody MenuDTO menuDTO) {
        log.info("[createMenu] Creating menu: {}", menuDTO.getMenuId());
        MenuDTO created = menuService.createMenu(menuDTO);
        return ResponseEntity.ok(created);
    }
    
    @PutMapping("/{menuId}")
    public ResponseEntity<MenuDTO> updateMenu(
            @PathVariable String menuId,
            @RequestBody MenuDTO menuDTO) {
        log.info("[updateMenu] Updating menu: {}", menuId);
        MenuDTO updated = menuService.updateMenu(menuId, menuDTO);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable String menuId) {
        log.info("[deleteMenu] Deleting menu: {}", menuId);
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDTO> getMenu(@PathVariable String menuId) {
        MenuDTO menu = menuService.getMenu(menuId);
        if (menu == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(menu);
    }
    
    @GetMapping
    public ResponseEntity<List<MenuDTO>> getAllMenus() {
        List<MenuDTO> menus = menuService.getAllMenus();
        return ResponseEntity.ok(menus);
    }
    
    @GetMapping("/tree")
    public ResponseEntity<List<MenuDTO>> getMenuTree() {
        log.info("[getMenuTree] Loading menu tree");
        List<MenuDTO> menuTree = menuService.getMenuTree();
        return ResponseEntity.ok(menuTree);
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuDTO>> getMenusByCategory(@PathVariable String category) {
        List<MenuDTO> menus = menuService.getMenusByCategory(category);
        return ResponseEntity.ok(menus);
    }
    
    @PostMapping("/init")
    public ResponseEntity<Void> initializeDefaultMenus() {
        log.info("[initializeDefaultMenus] Initializing default menus");
        menuService.initializeDefaultMenus();
        return ResponseEntity.ok().build();
    }
}
