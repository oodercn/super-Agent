package net.ooder.nexus.adapter.inbound.console;

import com.alibaba.fastjson.JSON;
import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.domain.model.Menu;
import net.ooder.nexus.domain.model.MenuConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单控制器
 * 提供动态菜单获取接口，所有角色过滤在后端完成
 */
@RestController
@RequestMapping(value = "/api/menu", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class MenuController {

    private static final Logger log = LoggerFactory.getLogger(MenuController.class);

    private static final String MENU_CONFIG_PATH = "static/console/menu-config.json";

    private MenuConfig menuConfig;

    /**
     * 初始化时加载菜单配置
     */
    @PostConstruct
    public void init() {
        loadMenuConfig();
    }

    /**
     * 加载菜单配置文件
     */
    private void loadMenuConfig() {
        try {
            ClassPathResource resource = new ClassPathResource(MENU_CONFIG_PATH);
            if (!resource.exists()) {
                log.error("菜单配置文件不存在: {}", MENU_CONFIG_PATH);
                menuConfig = new MenuConfig();
                menuConfig.setMenu(new ArrayList<>());
                return;
            }

            try (InputStream is = resource.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                String json = reader.lines().collect(Collectors.joining("\n"));
                menuConfig = JSON.parseObject(json, MenuConfig.class);
                log.info("菜单配置加载成功，共 {} 个菜单项",
                    menuConfig.getMenu() != null ? menuConfig.getMenu().size() : 0);
            }
        } catch (IOException e) {
            log.error("加载菜单配置失败", e);
            menuConfig = new MenuConfig();
            menuConfig.setMenu(new ArrayList<>());
        }
    }

    /**
     * 获取当前用户的菜单
     * 从 menu-config.json 读取，根据用户角色过滤
     * @return 菜单数据
     */
    @PostMapping
    @ResponseBody
    public ResultModel<List<Menu>> getMenu() {
        log.info("========== 进入 getMenu 方法 ==========");
        log.info("menuConfig is null: {}", menuConfig == null);
        log.info("menuConfig.getMenu() is null: {}", menuConfig == null ? "N/A" : (menuConfig.getMenu() == null));
        
        if (menuConfig == null || menuConfig.getMenu() == null) {
            log.error("菜单配置未加载");
            return ResultModel.error("菜单配置未加载", new ArrayList<>(), 500);
        }

        try {
            // 获取当前用户角色（暂时返回所有已实现的功能）
            String currentRole = getCurrentUserRole();
            log.info("当前用户角色: {}", currentRole);
            log.info("原始菜单项数量: {}", menuConfig.getMenu().size());

            // 根据角色过滤菜单
            List<Menu> filteredMenu = filterMenuByRole(menuConfig.getMenu(), currentRole);
            log.info("过滤后菜单项数量: {}", filteredMenu.size());
            log.info("========== getMenu 方法执行成功 ==========");

            return ResultModel.success("菜单获取成功", filteredMenu);

        } catch (Exception e) {
            log.error("获取菜单失败", e);
            return ResultModel.error("菜单加载失败: " + e.getMessage(), new ArrayList<>(), 500);
        }
    }

    /**
     * 获取完整菜单（所有已实现的功能）
     * 用于开发调试或管理员查看
     * @return 完整菜单数据
     */
    @PostMapping("/all")
    @ResponseBody
    public ResultModel<List<Menu>> getAllMenu() {
        if (menuConfig == null || menuConfig.getMenu() == null) {
            return ResultModel.error("菜单配置未加载", new ArrayList<>(), 500);
        }

        try {
            // 只过滤掉未实现的功能，不根据角色过滤
            List<Menu> filteredMenu = filterImplementedOnly(menuConfig.getMenu());

            return ResultModel.success("完整菜单获取成功", filteredMenu);

        } catch (Exception e) {
            log.error("获取完整菜单失败", e);
            return ResultModel.error("菜单加载失败: " + e.getMessage(), new ArrayList<>(), 500);
        }
    }

    /**
     * 获取菜单树结构
     * @return 树形菜单数据
     */
    @PostMapping("/tree")
    @ResponseBody
    public ResultModel<List<Menu>> getMenuTree() {
        if (menuConfig == null || menuConfig.getMenu() == null) {
            return ResultModel.error("菜单配置未加载", new ArrayList<>(), 500);
        }

        try {
            String currentRole = getCurrentUserRole();
            List<Menu> filteredMenu = filterMenuByRole(menuConfig.getMenu(), currentRole);

            return ResultModel.success("菜单树获取成功", filteredMenu);

        } catch (Exception e) {
            log.error("获取菜单树失败", e);
            return ResultModel.error("菜单树加载失败: " + e.getMessage(), new ArrayList<>(), 500);
        }
    }

    /**
     * 重新加载菜单配置
     * @return 加载结果
     */
    @PostMapping("/reload")
    @ResponseBody
    public ResultModel<String> reloadMenu() {
        loadMenuConfig();
        if (menuConfig != null && menuConfig.getMenu() != null) {
            return ResultModel.success("菜单配置重新加载成功", "共 " + menuConfig.getMenu().size() + " 个菜单项");
        } else {
            return ResultModel.error("菜单配置重新加载失败", 500);
        }
    }

    /**
     * 获取当前用户角色
     * 暂时返回默认角色，后续从 SecurityContext 获取
     * @return 用户角色
     */
    private String getCurrentUserRole() {
        // TODO: 集成 Spring Security 后从 SecurityContext 获取
        // return SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        // 临时返回默认角色，让所有已实现的功能都可见
        return "personal";
    }

    /**
     * 根据角色过滤菜单
     * @param menuItems 菜单项列表
     * @param role 用户角色
     * @return 过滤后的菜单
     */
    private List<Menu> filterMenuByRole(List<Menu> menuItems, String role) {
        if (menuItems == null) return new ArrayList<>();

        List<Menu> result = new ArrayList<>();

        for (Menu item : menuItems) {
            // 检查状态
            if (!"implemented".equals(item.getStatus())) {
                continue;
            }

            // 检查角色权限
            List<String> roles = item.getRoles();
            if (roles != null && !roles.contains(role)) {
                continue;
            }

            // 复制菜单项
            Menu filteredItem = copyMenu(item);

            // 递归处理子菜单
            if (item.hasChildren()) {
                List<Menu> filteredChildren = filterMenuByRole(item.getChildren(), role);
                if (!filteredChildren.isEmpty()) {
                    filteredItem.setChildren(filteredChildren);
                } else {
                    filteredItem.setChildren(null);
                }
            }

            result.add(filteredItem);
        }

        return result;
    }

    /**
     * 只过滤未实现的功能
     * @param menuItems 菜单项列表
     * @return 过滤后的菜单
     */
    private List<Menu> filterImplementedOnly(List<Menu> menuItems) {
        if (menuItems == null) return new ArrayList<>();

        List<Menu> result = new ArrayList<>();

        for (Menu item : menuItems) {
            if (!"implemented".equals(item.getStatus())) {
                continue;
            }

            Menu filteredItem = copyMenu(item);

            if (item.hasChildren()) {
                List<Menu> filteredChildren = filterImplementedOnly(item.getChildren());
                if (!filteredChildren.isEmpty()) {
                    filteredItem.setChildren(filteredChildren);
                } else {
                    filteredItem.setChildren(null);
                }
            }

            result.add(filteredItem);
        }

        return result;
    }

    /**
     * 复制菜单对象
     * @param source 源菜单
     * @return 复制的菜单
     */
    private Menu copyMenu(Menu source) {
        Menu copy = new Menu();
        copy.setId(source.getId());
        copy.setName(source.getName());
        copy.setIcon(source.getIcon());
        copy.setLevel(source.getLevel());
        copy.setUrl(source.getUrl());
        copy.setPriority(source.getPriority());
        copy.setStatus(source.getStatus());
        copy.setBadge(source.getBadge());
        copy.setRoles(source.getRoles());
        return copy;
    }
}
