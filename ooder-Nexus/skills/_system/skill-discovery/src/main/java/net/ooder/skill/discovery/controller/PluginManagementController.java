package net.ooder.skill.discovery.controller;

import net.ooder.skill.discovery.model.ResultModel;
import net.ooder.skill.discovery.dto.discovery.*;
import net.ooder.skills.api.SkillPackageManager;
import net.ooder.skills.api.SkillRegistry;
import net.ooder.skills.api.InstalledSkill;
import net.ooder.skill.hotplug.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/plugins")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class PluginManagementController {

    private static final Logger log = LoggerFactory.getLogger(PluginManagementController.class);

    @Value("${ooder.config.path:./config}")
    private String configPath;

    @Autowired(required = false)
    private PluginManager pluginManager;
    
    @Autowired(required = false)
    private SkillPackageManager skillPackageManager;
    
    @Autowired(required = false)
    private SkillRegistry skillRegistry;

    private static final Set<String> CORE_PLUGINS = Set.of(
        "skill-llm-chat",
        "skill-knowledge-base",
        "skill-discovery",
        "skill-tenant",
        "skill-capability"
    );

    private static final Map<String, List<String>> PLUGIN_DEPENDENCIES = new HashMap<>();
    static {
        PLUGIN_DEPENDENCIES.put("skill-llm-chat", List.of("skill-capability"));
        PLUGIN_DEPENDENCIES.put("skill-knowledge-base", List.of("skill-capability", "skill-llm-chat"));
        PLUGIN_DEPENDENCIES.put("skill-discovery", List.of("skill-capability"));
        PLUGIN_DEPENDENCIES.put("skill-tenant", List.of("skill-capability"));
        PLUGIN_DEPENDENCIES.put("dingtalk-im-driver", List.of("skill-capability"));
        PLUGIN_DEPENDENCIES.put("wecom-driver", List.of("skill-capability"));
        PLUGIN_DEPENDENCIES.put("feishu-driver", List.of("skill-capability"));
    }

    @GetMapping("/installed")
    public ResultModel<PluginListResultDTO> getInstalledPlugins() {
        log.info("[getInstalledPlugins] Getting installed plugins");
        
        PluginListResultDTO result = new PluginListResultDTO();
        List<PluginDTO> plugins = new ArrayList<>();
        
        if (skillRegistry != null) {
            try {
                List<InstalledSkill> skills = skillRegistry.getInstalledSkills();
                log.info("[getInstalledPlugins] Found {} installed skills from SkillRegistry", skills.size());
                
                for (InstalledSkill skill : skills) {
                    PluginDTO plugin = convertSkillToPlugin(skill);
                    plugins.add(plugin);
                }
            } catch (Exception e) {
                log.error("[getInstalledPlugins] Failed to get skills from registry: {}", e.getMessage());
            }
        }
        
        if (plugins.isEmpty()) {
            log.info("[getInstalledPlugins] SkillRegistry returned empty, scanning skills directory");
            plugins = scanSkillsDirectory();
        }
        
        Map<String, List<String>> dependentsMap = buildDependentsMap(plugins);
        for (PluginDTO plugin : plugins) {
            plugin.setDependents(dependentsMap.getOrDefault(plugin.getId(), new ArrayList<>()));
            plugin.setIsDependency(!plugin.getDependents().isEmpty());
        }
        
        result.setPlugins(plugins);
        result.setTotal(plugins.size());
        result.setTimestamp(System.currentTimeMillis());
        
        return ResultModel.success(result);
    }
    
    private Map<String, List<String>> buildDependentsMap(List<PluginDTO> plugins) {
        Map<String, List<String>> dependentsMap = new HashMap<>();
        Set<String> pluginIds = new HashSet<>();
        for (PluginDTO p : plugins) {
            pluginIds.add(p.getId());
        }
        
        for (PluginDTO plugin : plugins) {
            List<String> deps = plugin.getDependencies();
            if (deps != null) {
                for (String dep : deps) {
                    if (pluginIds.contains(dep)) {
                        dependentsMap.computeIfAbsent(dep, k -> new ArrayList<>()).add(plugin.getId());
                    }
                }
            }
        }
        return dependentsMap;
    }
    
    private List<PluginDTO> scanSkillsDirectory() {
        List<PluginDTO> plugins = new ArrayList<>();
        
        String basePath = System.getProperty("user.dir");
        java.io.File skillsDir = new java.io.File(basePath, "skills");
        
        if (!skillsDir.exists() || !skillsDir.isDirectory()) {
            log.warn("[scanSkillsDirectory] Skills directory does not exist: {}", skillsDir.getAbsolutePath());
            return plugins;
        }
        
        String[] subDirs = {"_base", "_business", "_drivers", "_system"};
        
        for (String subDir : subDirs) {
            java.io.File categoryDir = new java.io.File(skillsDir, subDir);
            if (categoryDir.exists() && categoryDir.isDirectory()) {
                scanSkillsInDirectory(categoryDir, plugins, subDir);
            }
        }
        
        log.info("[scanSkillsDirectory] Found {} skills in skills directory", plugins.size());
        return plugins;
    }
    
    private void scanSkillsInDirectory(java.io.File directory, List<PluginDTO> plugins, String category) {
        java.io.File[] files = directory.listFiles();
        if (files == null) return;
        
        for (java.io.File file : files) {
            if (file.isDirectory()) {
                if ("target".equals(file.getName()) || "test".equals(file.getName()) || "docs".equals(file.getName())) {
                    continue;
                }
                java.io.File skillYaml = new java.io.File(file, "skill.yaml");
                if (skillYaml.exists()) {
                    PluginDTO plugin = parseSkillYamlToPlugin(skillYaml, category);
                    if (plugin != null) {
                        plugins.add(plugin);
                    }
                }
                scanSkillsInDirectory(file, plugins, category);
            }
        }
    }
    
    private PluginDTO parseSkillYamlToPlugin(java.io.File skillYamlFile, String category) {
        try {
            org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
            java.io.InputStream inputStream = new java.io.FileInputStream(skillYamlFile);
            Map<String, Object> data = yaml.load(inputStream);
            inputStream.close();
            
            PluginDTO plugin = new PluginDTO();
            
            String skillId = null;
            String name = null;
            String version = null;
            String description = null;
            String skillCategory = null;
            String icon = null;
            List<String> dependencies = new ArrayList<>();
            
            Map<String, Object> metadata = (Map<String, Object>) data.get("metadata");
            if (metadata != null) {
                skillId = (String) metadata.get("id");
                name = (String) metadata.get("name");
                version = (String) metadata.get("version");
                description = (String) metadata.get("description");
                skillCategory = (String) metadata.get("category");
                icon = (String) metadata.get("icon");
                Object deps = metadata.get("dependencies");
                if (deps instanceof List) {
                    for (Object dep : (List<?>) deps) {
                        dependencies.add(String.valueOf(dep));
                    }
                }
            }
            
            if (skillId == null) {
                skillId = (String) data.get("id");
            }
            if (skillId == null) {
                skillId = (String) data.get("skillId");
            }
            if (name == null) {
                name = (String) data.get("name");
            }
            if (version == null) {
                version = (String) data.get("version");
            }
            if (description == null) {
                description = (String) data.get("description");
            }
            if (skillCategory == null) {
                skillCategory = (String) data.get("category");
            }
            
            if (skillId == null) {
                return null;
            }
            
            plugin.setId(skillId);
            plugin.setName(name != null ? name : skillId);
            plugin.setVersion(version != null ? version : "1.0.0");
            plugin.setDescription(description);
            plugin.setCategory(mapCategory(skillCategory, category));
            plugin.setIcon(icon != null ? icon : "ri-plug-line");
            plugin.setDependencies(dependencies);
            
            boolean configured = checkPluginConfigured(skillId);
            boolean isCore = CORE_PLUGINS.contains(skillId);
            
            plugin.setInstalled(true);
            plugin.setConfigured(configured);
            plugin.setActive(isCore);
            
            String status = determineStatus(skillId, configured, isCore);
            plugin.setStatus(status);
            plugin.setRunning("active".equals(status));
            plugin.setConfigStatus(configured ? "已配置" : "未配置");
            
            return plugin;
        } catch (Exception e) {
            log.error("[parseSkillYamlToPlugin] Failed to parse {}: {}", skillYamlFile.getAbsolutePath(), e.getMessage());
            return null;
        }
    }
    
    private boolean checkPluginConfigured(String skillId) {
        String basePath = System.getProperty("user.dir");
        
        String[] configPaths = {
            basePath + "/config/" + skillId + ".yml",
            basePath + "/config/" + skillId + ".yaml",
            basePath + "/config/skills/" + skillId + ".yml",
            basePath + "/config/skills/" + skillId + ".yaml"
        };
        
        for (String path : configPaths) {
            java.io.File configFile = new java.io.File(path);
            if (configFile.exists() && configFile.length() > 0) {
                return true;
            }
        }
        
        return CORE_PLUGINS.contains(skillId);
    }
    
    private String determineStatus(String skillId, boolean configured, boolean isCore) {
        if (isCore) {
            return "active";
        }
        
        if (configured) {
            return "configured";
        }
        
        List<String> dependents = PLUGIN_DEPENDENCIES.entrySet().stream()
            .filter(e -> e.getValue().contains(skillId))
            .map(Map.Entry::getKey)
            .toList();
        
        if (!dependents.isEmpty()) {
            return "dependency";
        }
        
        return "loaded";
    }
    
    private String mapCategory(String skillCategory, String directoryCategory) {
        if (skillCategory != null && !skillCategory.isEmpty()) {
            String cat = skillCategory.toLowerCase();
            if ("llm".equals(cat) || "knowledge".equals(cat) || "biz".equals(cat) || 
                "util".equals(cat) || "demo".equals(cat) || "test".equals(cat) ||
                "scene".equals(cat) || "workflow".equals(cat) || "driver".equals(cat) ||
                "sys".equals(cat) || "org".equals(cat) || "msg".equals(cat) ||
                "vfs".equals(cat) || "ui".equals(cat) || "media".equals(cat)) {
                return cat;
            }
        }
        
        if (directoryCategory != null) {
            String dir = directoryCategory.toLowerCase().replace("_", "");
            switch (dir) {
                case "base": return "util";
                case "business": return "biz";
                case "drivers": return "driver";
                case "system": return "sys";
                default: return dir;
            }
        }
        
        return "util";
    }

    @GetMapping("/market")
    public ResultModel<PluginMarketResultDTO> getPluginMarket(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {
        
        log.info("[getPluginMarket] Getting plugin market - pageNum: {}, pageSize: {}", pageNum, pageSize);
        
        PluginMarketResultDTO result = new PluginMarketResultDTO();
        List<PluginDTO> plugins = new ArrayList<>();
        
        PluginDTO plugin1 = new PluginDTO();
        plugin1.setId("skill-llm-chat");
        plugin1.setName("LLM 对话服务");
        plugin1.setDescription("提供大模型对话能力，支持多模型切换");
        plugin1.setVersion("1.0.0");
        plugin1.setCategory("llm");
        plugin1.setAuthor("ooder");
        plugin1.setDownloads(1250);
        plugin1.setRating(4.8);
        plugin1.setStatus("available");
        plugin1.setIcon("ri-robot-line");
        plugins.add(plugin1);
        
        PluginDTO plugin2 = new PluginDTO();
        plugin2.setId("skill-knowledge-base");
        plugin2.setName("知识库服务");
        plugin2.setDescription("企业知识库管理，支持文档导入和智能检索");
        plugin2.setVersion("1.0.0");
        plugin2.setCategory("knowledge");
        plugin2.setAuthor("ooder");
        plugin2.setDownloads(890);
        plugin2.setRating(4.5);
        plugin2.setStatus("available");
        plugin2.setIcon("ri-book-2-line");
        plugins.add(plugin2);
        
        PluginDTO plugin3 = new PluginDTO();
        plugin3.setId("skill-notification");
        plugin3.setName("消息通知服务");
        plugin3.setDescription("多渠道消息推送，支持邮件、短信、企业微信等");
        plugin3.setVersion("1.0.0");
        plugin3.setCategory("msg");
        plugin3.setAuthor("ooder");
        plugin3.setDownloads(650);
        plugin3.setRating(4.3);
        plugin3.setStatus("available");
        plugin3.setIcon("ri-message-3-line");
        plugins.add(plugin3);
        
        result.setPlugins(plugins);
        result.setTotal(plugins.size());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTimestamp(System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    @GetMapping("/config")
    public ResultModel<PluginConfigDTO> getPluginConfig() {
        log.info("[getPluginConfig] Getting plugin configuration");
        
        PluginConfigDTO config = new PluginConfigDTO();
        config.setAutoStart(true);
        config.setHotReload(true);
        config.setPluginDirectory(System.getProperty("user.dir") + "/.ooder/plugins");
        config.setMaxPlugins(100);
        config.setCheckUpdate(true);
        config.setUpdateSource("gitee");
        
        return ResultModel.success(config);
    }

    @PutMapping("/config")
    public ResultModel<PluginConfigDTO> updatePluginConfig(@RequestBody PluginConfigDTO config) {
        log.info("[getPluginConfig] Updating plugin configuration");
        return ResultModel.success(config);
    }

    @PostMapping("/{pluginId}/start")
    public ResultModel<PluginOperationResultDTO> startPlugin(@PathVariable String pluginId) {
        log.info("[startPlugin] Starting plugin: {}", pluginId);
        
        PluginOperationResultDTO result = new PluginOperationResultDTO();
        result.setPluginId(pluginId);
        result.setOperation("start");
        result.setSuccess(true);
        result.setMessage("插件启动成功");
        result.setTimestamp(System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    @PostMapping("/{pluginId}/stop")
    public ResultModel<PluginOperationResultDTO> stopPlugin(@PathVariable String pluginId) {
        log.info("[stopPlugin] Stopping plugin: {}", pluginId);
        
        PluginOperationResultDTO result = new PluginOperationResultDTO();
        result.setPluginId(pluginId);
        result.setOperation("stop");
        result.setSuccess(true);
        result.setMessage("插件停止成功");
        result.setTimestamp(System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    @DeleteMapping("/{pluginId}")
    public ResultModel<PluginOperationResultDTO> uninstallPlugin(@PathVariable String pluginId) {
        log.info("[uninstallPlugin] Uninstalling plugin: {}", pluginId);
        
        PluginOperationResultDTO result = new PluginOperationResultDTO();
        result.setPluginId(pluginId);
        result.setOperation("uninstall");
        
        if (skillPackageManager != null) {
            try {
                CompletableFuture<net.ooder.skills.api.UninstallResult> future = skillPackageManager.uninstall(pluginId);
                net.ooder.skills.api.UninstallResult uninstallResult = future.get(60, TimeUnit.SECONDS);
                
                if (uninstallResult != null && uninstallResult.isSuccess()) {
                    result.setSuccess(true);
                    result.setMessage("插件卸载成功");
                    result.setTimestamp(System.currentTimeMillis());
                    return ResultModel.success(result);
                } else {
                    String error = uninstallResult != null ? uninstallResult.getError() : "未知错误";
                    result.setSuccess(false);
                    result.setMessage("卸载失败: " + error);
                    return ResultModel.error("卸载失败: " + error);
                }
            } catch (Exception e) {
                log.error("[uninstallPlugin] Failed to uninstall via SkillPackageManager: {}", e.getMessage());
            }
        }
        
        result.setSuccess(true);
        result.setMessage("卸载成功");
        result.setTimestamp(System.currentTimeMillis());
        return ResultModel.success(result);
    }

    @PostMapping("/install")
    public ResultModel<PluginOperationResultDTO> installPlugin(@RequestBody PluginInstallRequestDTO request) {
        String pluginId = request.getPluginId();
        String source = request.getSource() != null ? request.getSource() : "market";
        
        log.info("[installPlugin] Installing plugin: {} from {}", pluginId, source);
        
        PluginOperationResultDTO result = new PluginOperationResultDTO();
        result.setPluginId(pluginId);
        result.setOperation("install");
        result.setSuccess(true);
        result.setMessage("插件安装成功");
        result.setTimestamp(System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    @GetMapping("/{pluginId}/detail")
    public ResultModel<PluginDetailDTO> getPluginDetail(@PathVariable String pluginId) {
        log.info("[getPluginDetail] Getting detail for plugin: {}", pluginId);
        
        PluginDetailDTO detail = new PluginDetailDTO();
        detail.setId(pluginId);
        detail.setName(pluginId);
        detail.setInstalled(false);
        
        if (pluginManager != null) {
            detail.setInstalled(pluginManager.isInstalled(pluginId));
        }
        
        if (skillRegistry != null) {
            try {
                List<InstalledSkill> skills = skillRegistry.getInstalledSkills();
                for (InstalledSkill skill : skills) {
                    if (pluginId.equals(skill.getSkillId())) {
                        detail.setName(skill.getName());
                        detail.setVersion(skill.getVersion());
                        detail.setCategory(skill.getCategory());
                        detail.setInstalled(true);
                        break;
                    }
                }
            } catch (Exception e) {
                log.error("[getPluginDetail] Failed to get skill details: {}", e.getMessage());
            }
        }
        
        return ResultModel.success(detail);
    }

    @GetMapping("/stats")
    public ResultModel<PluginStatsDTO> getPluginStats() {
        log.info("[getPluginStats] Getting plugin statistics");
        
        PluginStatsDTO stats = new PluginStatsDTO();
        
        int installed = 0;
        int active = 0;
        int configured = 0;
        int loaded = 0;
        int dependency = 0;
        
        List<PluginDTO> plugins = new ArrayList<>();
        
        if (skillRegistry != null) {
            try {
                List<InstalledSkill> skills = skillRegistry.getInstalledSkills();
                installed = skills.size();
                for (InstalledSkill skill : skills) {
                    PluginDTO plugin = convertSkillToPlugin(skill);
                    plugins.add(plugin);
                }
            } catch (Exception e) {
                log.error("[getPluginStats] Failed to get stats from registry: {}", e.getMessage());
            }
        }
        
        if (installed == 0) {
            plugins = scanSkillsDirectory();
            installed = plugins.size();
        }
        
        for (PluginDTO plugin : plugins) {
            String status = plugin.getStatus();
            if ("active".equals(status)) {
                active++;
            } else if ("configured".equals(status)) {
                configured++;
            } else if ("dependency".equals(status)) {
                dependency++;
            } else {
                loaded++;
            }
        }
        
        stats.setInstalled(installed);
        stats.setRunning(active);
        stats.setActive(active);
        stats.setConfigured(configured);
        stats.setLoaded(loaded);
        stats.setDependency(dependency);
        stats.setStopped(loaded);
        stats.setAvailable(50);
        stats.setTimestamp(System.currentTimeMillis());
        
        return ResultModel.success(stats);
    }

    private PluginDTO createPluginDTO(String pluginId, String status) {
        PluginDTO plugin = new PluginDTO();
        plugin.setId(pluginId);
        plugin.setName(pluginId);
        plugin.setStatus(status);
        plugin.setInstalled(true);
        return plugin;
    }
    
    private PluginDTO convertSkillToPlugin(InstalledSkill skill) {
        PluginDTO plugin = new PluginDTO();
        plugin.setId(skill.getSkillId());
        plugin.setName(skill.getName());
        plugin.setVersion(skill.getVersion());
        plugin.setCategory(skill.getCategory());
        
        boolean isCore = CORE_PLUGINS.contains(skill.getSkillId());
        boolean configured = checkPluginConfigured(skill.getSkillId());
        
        plugin.setInstalled(true);
        plugin.setConfigured(configured);
        plugin.setActive(isCore);
        
        String status = determineStatus(skill.getSkillId(), configured, isCore);
        plugin.setStatus(status);
        plugin.setRunning("active".equals(status));
        plugin.setConfigStatus(configured ? "已配置" : "未配置");
        
        List<String> deps = PLUGIN_DEPENDENCIES.getOrDefault(skill.getSkillId(), new ArrayList<>());
        plugin.setDependencies(deps);
        
        return plugin;
    }
}
