package net.ooder.nexus.adapter.inbound.controller;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.dto.config.ConfigCategoryDTO;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConfigCategoryController {

    @GetMapping("/categories")
    public ResultModel<List<ConfigCategoryDTO>> getCategories() {
        List<ConfigCategoryDTO> categories = new ArrayList<>();
        
        categories.add(createCategory("network", "网络配置", "ri-wifi-line", 5));
        categories.add(createCategory("security", "安全配置", "ri-shield-line", 8));
        categories.add(createCategory("system", "系统配置", "ri-settings-3-line", 12));
        categories.add(createCategory("storage", "存储配置", "ri-database-2-line", 3));
        categories.add(createCategory("skill", "技能配置", "ri-lightbulb-line", 6));
        
        return ResultModel.success("获取成功", categories);
    }

    private ConfigCategoryDTO createCategory(String id, String name, String icon, int itemCount) {
        ConfigCategoryDTO category = new ConfigCategoryDTO();
        category.setId(id);
        category.setName(name);
        category.setIcon(icon);
        category.setItemCount(itemCount);
        return category;
    }
}
