package net.ooder.mvp.skill.scene.config.controller;

import net.ooder.mvp.skill.scene.config.sdk.ConfigNode;
import net.ooder.mvp.skill.scene.config.service.ConfigInheritanceChain;
import net.ooder.mvp.skill.scene.config.service.ConfigLoaderService;
import net.ooder.mvp.skill.scene.dto.config.ConfigCategoryDTO;
import net.ooder.mvp.skill.scene.dto.config.ConfigDriverDTO;
import net.ooder.mvp.skill.scene.dto.config.ConfigAddressDTO;
import net.ooder.mvp.skill.scene.model.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/config")
public class ConfigController {

    private final ConfigLoaderService configLoader;

    @Autowired
    public ConfigController(ConfigLoaderService configLoader) {
        this.configLoader = configLoader;
    }

    @GetMapping("/categories")
    public ResultModel<List<ConfigCategoryDTO>> getCategories() {
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
        List<ConfigDriverDTO> drivers = new ArrayList<>();
        
        drivers.add(createDriver("skill-llm-aliyun-bailian", "阿里云百炼LLM", "llm", "1.0.0", true));
        drivers.add(createDriver("skill-llm-openai", "OpenAI LLM", "llm", "1.0.0", true));
        drivers.add(createDriver("skill-db-mysql", "MySQL数据库", "db", "1.0.0", true));
        drivers.add(createDriver("skill-db-sqlite", "SQLite数据库", "db", "1.0.0", true));
        drivers.add(createDriver("skill-vfs-local", "本地文件系统", "vfs", "1.0.0", true));
        drivers.add(createDriver("skill-org-local", "本地组织管理", "org", "1.0.0", true));
        drivers.add(createDriver("skill-know-rag", "RAG知识库", "knowledge", "1.0.0", true));
        drivers.add(createDriver("skill-comm-notify", "通知服务", "comm", "1.0.0", true));
        
        return ResultModel.success(drivers);
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

    @GetMapping("/addresses")
    public ResultModel<List<ConfigAddressDTO>> getAddresses() {
        List<ConfigAddressDTO> addresses = new ArrayList<>();
        
        addresses.add(createAddress("llm://aliyun-bailian", "阿里云百炼", "llm", "skill-llm-aliyun-bailian", true));
        addresses.add(createAddress("llm://openai", "OpenAI", "llm", "skill-llm-openai", true));
        addresses.add(createAddress("db://mysql-local", "本地MySQL", "db", "skill-db-mysql", true));
        addresses.add(createAddress("db://sqlite-local", "本地SQLite", "db", "skill-db-sqlite", true));
        addresses.add(createAddress("vfs://local", "本地文件系统", "vfs", "skill-vfs-local", true));
        
        return ResultModel.success(addresses);
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
    
    private ConfigCategoryDTO createCategory(String code, String name, String icon, String color, boolean userFacing) {
        ConfigCategoryDTO cat = new ConfigCategoryDTO();
        cat.setCode(code);
        cat.setName(name);
        cat.setIcon(icon);
        cat.setColor(color);
        cat.setUserFacing(userFacing);
        return cat;
    }

    @GetMapping("/system")
    public ResponseEntity<ConfigNode> getSystemConfig() {
        ConfigNode config = configLoader.loadSystemConfig();
        return ResponseEntity.ok(config);
    }

    @GetMapping("/system/capabilities")
    public ResultModel<Map<String, Object>> getAllCapabilities() {
        ConfigNode config = configLoader.loadSystemConfig();
        Map<String, Object> capabilities = config.getNested("spec.capabilities");
        if (capabilities == null) {
            capabilities = new LinkedHashMap<>();
        }
        return ResultModel.success(capabilities);
    }

    @PutMapping("/system")
    public ResultModel<Void> saveSystemConfig(@RequestBody ConfigNode config) {
        configLoader.saveConfig("system", "system", config);
        return ResultModel.success("保存成功", null);
    }

    @PutMapping("/system/profile")
    public ResultModel<Void> switchProfile(@RequestBody Map<String, String> request) {
        String profile = request.get("profile");
        if (profile != null) {
            configLoader.switchProfile(profile);
        }
        return ResultModel.success("Profile切换成功", null);
    }

    @PostMapping("/system/reset")
    public ResultModel<Void> resetSystemConfig() {
        configLoader.resetConfig("system", "system", null);
        return ResultModel.success("重置成功", null);
    }

    @GetMapping("/system/capabilities/{address}")
    public ResponseEntity<Map<String, Object>> getCapabilityConfig(@PathVariable String address) {
        Map<String, Object> config = configLoader.getCapabilityConfig("system", "system", address);
        return ResponseEntity.ok(config);
    }

    @PutMapping("/system/capabilities/{address}")
    public ResponseEntity<Void> updateCapabilityConfig(
            @PathVariable String address,
            @RequestBody Map<String, Object> config) {
        configLoader.updateCapabilityConfig("system", "system", address, config);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/skills/{skillId}")
    public ResponseEntity<ConfigNode> getSkillConfig(
            @PathVariable String skillId,
            @RequestParam(defaultValue = "true") boolean resolveInheritance) {
        ConfigNode config = configLoader.loadSkillConfig(skillId, resolveInheritance);
        return ResponseEntity.ok(config);
    }

    @GetMapping("/skills/{skillId}/inheritance")
    public ResponseEntity<ConfigInheritanceChain> getSkillInheritanceChain(@PathVariable String skillId) {
        ConfigInheritanceChain chain = configLoader.getInheritanceChain("skill", skillId);
        return ResponseEntity.ok(chain);
    }

    @PutMapping("/skills/{skillId}")
    public ResponseEntity<Void> updateSkillConfig(
            @PathVariable String skillId,
            @RequestBody ConfigNode config) {
        configLoader.saveConfig("skill", skillId, config);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/skills/{skillId}/keys/{key}")
    public ResponseEntity<Void> resetSkillConfig(
            @PathVariable String skillId,
            @PathVariable String key) {
        configLoader.resetConfig("skill", skillId, key);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/scenes/{sceneId}")
    public ResponseEntity<ConfigNode> getSceneConfig(
            @PathVariable String sceneId,
            @RequestParam(defaultValue = "true") boolean resolveInheritance) {
        ConfigNode config = configLoader.loadSceneConfig(sceneId, resolveInheritance);
        return ResponseEntity.ok(config);
    }

    @GetMapping("/scenes/{sceneId}/inheritance")
    public ResponseEntity<ConfigInheritanceChain> getSceneInheritanceChain(@PathVariable String sceneId) {
        ConfigInheritanceChain chain = configLoader.getInheritanceChain("scene", sceneId);
        return ResponseEntity.ok(chain);
    }

    @GetMapping("/scenes/{sceneId}/skills/{skillId}")
    public ResponseEntity<ConfigNode> getInternalSkillConfig(
            @PathVariable String sceneId,
            @PathVariable String skillId) {
        ConfigNode config = configLoader.loadInternalSkillConfig(sceneId, skillId);
        return ResponseEntity.ok(config);
    }

    @PutMapping("/scenes/{sceneId}")
    public ResponseEntity<Void> updateSceneConfig(
            @PathVariable String sceneId,
            @RequestBody ConfigNode config) {
        configLoader.saveConfig("scene", sceneId, config);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/scenes/{sceneId}/keys/{key}")
    public ResponseEntity<Void> resetSceneConfig(
            @PathVariable String sceneId,
            @PathVariable String key) {
        configLoader.resetConfig("scene", sceneId, key);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/inheritance/{targetType}/{targetId}")
    public ResponseEntity<ConfigInheritanceChain> getInheritanceChain(
            @PathVariable String targetType,
            @PathVariable String targetId) {
        ConfigInheritanceChain chain = configLoader.getInheritanceChain(targetType, targetId);
        return ResponseEntity.ok(chain);
    }

    @PostMapping("/preview")
    public ResponseEntity<ConfigNode> previewMergedConfig(@RequestBody ConfigPreviewRequest request) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidationResult> validateConfig(@RequestBody ConfigValidateRequest request) {
        return ResponseEntity.ok().build();
    }

    public static class ConfigPreviewRequest {
        private String targetType;
        private String targetId;
        private ConfigNode config;

        public String getTargetType() { return targetType; }
        public void setTargetType(String targetType) { this.targetType = targetType; }
        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public ConfigNode getConfig() { return config; }
        public void setConfig(ConfigNode config) { this.config = config; }
    }

    public static class ConfigValidateRequest {
        private String targetType;
        private String targetId;
        private ConfigNode config;

        public String getTargetType() { return targetType; }
        public void setTargetType(String targetType) { this.targetType = targetType; }
        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public ConfigNode getConfig() { return config; }
        public void setConfig(ConfigNode config) { this.config = config; }
    }

    public static class ValidationResult {
        private boolean valid;
        private List<String> errors;
        private List<String> warnings;

        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    }
}
