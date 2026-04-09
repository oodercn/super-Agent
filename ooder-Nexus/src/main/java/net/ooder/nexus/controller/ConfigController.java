package net.ooder.nexus.controller;

import net.ooder.nexus.model.ResultModel;
import net.ooder.nexus.dto.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/config-meta")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class ConfigController {

    private static final Logger log = LoggerFactory.getLogger(ConfigController.class);

    @GetMapping("/categories")
    public ResultModel<List<ConfigCategoryDTO>> getCategories() {
        log.info("[getCategories] Getting config categories");
        
        List<ConfigCategoryDTO> categories = new ArrayList<>();
        categories.add(createCategory("org", "组织服务", "ri-team-line", "#8b5cf6", false));
        categories.add(createCategory("vfs", "存储服务", "ri-database-2-line", "#f5970b", false));
        categories.add(createCategory("llm", "LLM服务", "ri-brain-line", "#9334ff", true));
        categories.add(createCategory("knowledge", "知识服务", "ri-book-line", "#10b981", true));
        categories.add(createCategory("biz", "业务场景", "ri-briefcase-line", "#f97316", true));
        categories.add(createCategory("sys", "系统管理", "ri-settings-3-line", "#6366f1", false));
        categories.add(createCategory("msg", "消息通讯", "ri-message-3-line", "#f97b72", false));
        categories.add(createCategory("ui", "UI生成", "ri-palette-line", "#ec4899", false));
        categories.add(createCategory("payment", "支付服务", "ri-bank-card-line", "#8b5cf6", false));
        categories.add(createCategory("media", "媒体发布", "ri-edit-line", "#f5970b", false));
        categories.add(createCategory("util", "工具服务", "ri-tools-line", "#4f46e5", true));
        categories.add(createCategory("nexus-ui", "Nexus界面", "ri-layout-line", "#6366f1", false));
        
        return ResultModel.success(categories);
    }

    @GetMapping("/drivers")
    public ResultModel<List<ConfigDriverDTO>> getDrivers() {
        log.info("[getDrivers] Getting config drivers");
        
        List<ConfigDriverDTO> drivers = new ArrayList<>();
        drivers.add(createDriver("skill-llm-aliyun-bailian", "阿里云百炼LLM", "llm", "1.0.0", true));
        drivers.add(createDriver("skill-llm-openai", "OpenAI LLM", "llm", "1.0.0", true));
        drivers.add(createDriver("skill-llm-deepseek", "DeepSeek LLM", "llm", "1.0.0", true));
        drivers.add(createDriver("skill-llm-ollama", "Ollama LLM", "llm", "1.0.0", true));
        drivers.add(createDriver("skill-db-mysql", "MySQL数据库", "db", "1.0.0", true));
        drivers.add(createDriver("skill-db-sqlite", "SQLite数据库", "db", "1.0.0", true));
        drivers.add(createDriver("skill-vfs-local", "本地文件系统", "vfs", "1.0.0", true));
        drivers.add(createDriver("skill-org-local", "本地组织管理", "org", "1.0.0", true));
        drivers.add(createDriver("skill-know-rag", "RAG知识库", "knowledge", "1.0.0", true));
        drivers.add(createDriver("skill-comm-notify", "通知服务", "comm", "1.0.0", true));
        
        return ResultModel.success(drivers);
    }

    @GetMapping("/addresses")
    public ResultModel<List<ConfigAddressDTO>> getAddresses() {
        log.info("[getAddresses] Getting config addresses");
        
        List<ConfigAddressDTO> addresses = new ArrayList<>();
        addresses.add(createAddress("llm://aliyun-bailian", "阿里云百炼", "llm", "skill-llm-aliyun-bailian", true));
        addresses.add(createAddress("llm://openai", "OpenAI", "llm", "skill-llm-openai", true));
        addresses.add(createAddress("llm://deepseek", "DeepSeek", "llm", "skill-llm-deepseek", true));
        addresses.add(createAddress("llm://ollama", "Ollama", "llm", "skill-llm-ollama", true));
        addresses.add(createAddress("db://mysql-local", "本地MySQL", "db", "skill-db-mysql", true));
        addresses.add(createAddress("db://sqlite-local", "本地SQLite", "db", "skill-db-sqlite", true));
        addresses.add(createAddress("vfs://local", "本地文件系统", "vfs", "skill-vfs-local", true));
        
        return ResultModel.success(addresses);
    }

    @GetMapping("/inheritance-detail/{targetType}/{targetId}")
    public ResultModel<ConfigInheritanceDetailDTO> getInheritanceDetail(
            @PathVariable String targetType,
            @PathVariable String targetId) {
        log.info("[getInheritanceDetail] Getting inheritance detail for type: {}, id: {}", targetType, targetId);
        
        ConfigInheritanceDetailDTO result = new ConfigInheritanceDetailDTO();
        List<ConfigInheritanceChainDTO> chain = new ArrayList<>();
        
        ConfigInheritanceChainDTO systemLevel = new ConfigInheritanceChainDTO();
        systemLevel.setLevel("system");
        systemLevel.setSource("系统默认配置");
        systemLevel.setConfigId("system-default");
        systemLevel.setActive(true);
        chain.add(systemLevel);
        
        ConfigInheritanceChainDTO skillLevel = new ConfigInheritanceChainDTO();
        skillLevel.setLevel("skill");
        skillLevel.setSource("skill-" + targetType);
        skillLevel.setConfigId("skill-" + targetType + "-config");
        skillLevel.setActive(true);
        chain.add(skillLevel);
        
        if (targetId != null && !targetId.isEmpty()) {
            ConfigInheritanceChainDTO sceneLevel = new ConfigInheritanceChainDTO();
            sceneLevel.setLevel("scene");
            sceneLevel.setSource(targetId);
            sceneLevel.setConfigId(targetId + "-config");
            sceneLevel.setActive(true);
            chain.add(sceneLevel);
        }
        
        result.setChain(chain);
        result.setTargetType(targetType);
        result.setTargetId(targetId);
        result.setCurrentLevel(targetId != null && !targetId.isEmpty() ? "scene" : "skill");
        
        return ResultModel.success(result);
    }

    private ConfigCategoryDTO createCategory(String code, String name, String icon, String color, boolean userFacing) {
        ConfigCategoryDTO cat = new ConfigCategoryDTO();
        cat.setCode(code);
        cat.setName(name);
        cat.setIcon(icon);
        cat.setColor(color);
        cat.setUserFacing(userFacing);
        return cat;
    }

    private ConfigDriverDTO createDriver(String id, String name, String category, String version, boolean active) {
        ConfigDriverDTO driver = new ConfigDriverDTO();
        driver.setId(id);
        driver.setName(name);
        driver.setCategory(category);
        driver.setVersion(version);
        driver.setActive(active);
        driver.setStatus(active ? "ACTIVE" : "INACTIVE");
        return driver;
    }

    private ConfigAddressDTO createAddress(String address, String name, String category, String driver, boolean active) {
        ConfigAddressDTO addr = new ConfigAddressDTO();
        addr.setAddress(address);
        addr.setName(name);
        addr.setCategory(category);
        addr.setDriver(driver);
        addr.setActive(active);
        addr.setStatus(active ? "ACTIVE" : "INACTIVE");
        return addr;
    }
}
