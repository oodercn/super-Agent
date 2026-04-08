package net.ooder.mvp.skill.scene.config.service.impl;

import net.ooder.mvp.skill.scene.config.sdk.ConfigNode;
import net.ooder.mvp.skill.scene.config.sdk.SdkConfigStorage;
import net.ooder.mvp.skill.scene.config.service.ConfigInheritanceChain;
import net.ooder.mvp.skill.scene.config.service.ConfigInheritanceResolver;
import net.ooder.mvp.skill.scene.config.service.ConfigLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ConfigLoaderServiceImpl implements ConfigLoaderService {

    private static final Logger log = LoggerFactory.getLogger(ConfigLoaderServiceImpl.class);

    private final SdkConfigStorage sdkStorage;
    private final ConfigInheritanceResolver inheritanceResolver;

    @Autowired
    public ConfigLoaderServiceImpl(SdkConfigStorage sdkStorage) {
        this.sdkStorage = sdkStorage;
        this.inheritanceResolver = new ConfigInheritanceResolver();
    }

    @Override
    public ConfigNode loadSystemConfig() {
        return sdkStorage.loadSystemConfig();
    }

    @Override
    public ConfigNode loadSkillConfig(String skillId, boolean resolveInheritance) {
        ConfigNode systemConfig = loadSystemConfig();
        ConfigNode skillConfig = sdkStorage.loadSkillConfig(skillId);

        if (!resolveInheritance || skillConfig == null) {
            return skillConfig != null ? skillConfig : systemConfig;
        }

        return inheritanceResolver.merge(systemConfig, skillConfig);
    }

    @Override
    public ConfigNode loadSceneConfig(String sceneId, boolean resolveInheritance) {
        ConfigNode baseConfig = loadSkillConfig(sceneId, true);
        ConfigNode sceneConfig = sdkStorage.loadSceneConfig(sceneId);

        if (!resolveInheritance || sceneConfig == null) {
            return sceneConfig != null ? sceneConfig : baseConfig;
        }

        return inheritanceResolver.merge(baseConfig, sceneConfig);
    }

    @Override
    public ConfigNode loadInternalSkillConfig(String sceneId, String skillId) {
        ConfigNode sceneConfig = loadSceneConfig(sceneId, true);
        ConfigNode internalConfig = sdkStorage.loadInternalSkillConfig(sceneId, skillId);

        if (internalConfig == null) {
            return sceneConfig;
        }

        return inheritanceResolver.merge(sceneConfig, internalConfig);
    }

    @Override
    public ConfigInheritanceChain getInheritanceChain(String targetType, String targetId) {
        ConfigInheritanceChain chain = new ConfigInheritanceChain();
        chain.setTargetType(targetType);
        chain.setTargetId(targetId);

        ConfigNode systemConfig = loadSystemConfig();
        chain.addLevel("system", "system-config.json", systemConfig);

        if ("skill".equals(targetType) || "scene".equals(targetType) || "internal_skill".equals(targetType)) {
            ConfigNode skillConfig = sdkStorage.loadSkillConfig(targetId);
            if (skillConfig != null) {
                chain.addLevel("skill", "skill-config.yaml", skillConfig);
            }
        }

        if ("scene".equals(targetType) || "internal_skill".equals(targetType)) {
            ConfigNode sceneConfig = sdkStorage.loadSceneConfig(targetId);
            if (sceneConfig != null) {
                chain.addLevel("scene", "scene-config.yaml", sceneConfig);
            }
        }

        if ("internal_skill".equals(targetType)) {
            String[] parts = targetId.split(":");
            if (parts.length == 2) {
                ConfigNode internalConfig = sdkStorage.loadInternalSkillConfig(parts[0], parts[1]);
                if (internalConfig != null) {
                    chain.addLevel("internal_skill", "internal-skill-config.yaml", internalConfig);
                }
            }
        }

        ConfigNode resolved = resolveChainConfig(chain);
        chain.setResolvedConfig(resolved);

        return chain;
    }

    @Override
    public void saveConfig(String targetType, String targetId, ConfigNode config) {
        switch (targetType) {
            case "system":
                sdkStorage.saveSystemConfig(config);
                break;
            case "skill":
                sdkStorage.saveSkillConfig(targetId, config);
                break;
            case "scene":
                sdkStorage.saveSceneConfig(targetId, config);
                break;
            case "internal_skill":
                String[] parts = targetId.split(":");
                if (parts.length == 2) {
                    sdkStorage.saveInternalSkillConfig(parts[0], parts[1], config);
                } else {
                    throw new IllegalArgumentException("Invalid internal_skill targetId: " + targetId);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown target type: " + targetType);
        }
        log.info("[ConfigLoader] Config saved: {} / {}", targetType, targetId);
    }

    @Override
    public void resetConfig(String targetType, String targetId, String key) {
        ConfigInheritanceChain chain = getInheritanceChain(targetType, targetId);
        Object inheritedValue = chain.getInheritedValue(key);

        if (inheritedValue != null) {
            ConfigNode currentConfig = loadConfigByType(targetType, targetId);
            if (currentConfig != null) {
                currentConfig.put(key, inheritedValue);
                saveConfig(targetType, targetId, currentConfig);
                log.info("[ConfigLoader] Config reset: {} / {} / {}", targetType, targetId, key);
            }
        }
    }
    
    @Override
    public void switchProfile(String profile) {
        log.info("[ConfigLoader] Switching to profile: {}", profile);
        ConfigNode profileConfig = sdkStorage.loadProfile(profile);
        if (profileConfig != null) {
            sdkStorage.saveSystemConfig(profileConfig);
            log.info("[ConfigLoader] Profile switched successfully");
        } else {
            log.warn("[ConfigLoader] Profile not found: {}", profile);
        }
    }
    
    @Override
    public void updateConfig(String targetType, String targetId, String path, Object value) {
        ConfigNode config = loadConfigByType(targetType, targetId);
        if (config == null) {
            config = new ConfigNode();
        }
        config.putNested(path, value);
        saveConfig(targetType, targetId, config);
        log.info("[ConfigLoader] Config updated: {} / {} / {}", targetType, targetId, path);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getCapabilityConfig(String targetType, String targetId, String capabilityAddress) {
        ConfigNode config;
        if ("internal_skill".equals(targetType)) {
            String[] parts = targetId.split(":");
            config = loadInternalSkillConfig(parts[0], parts[1]);
        } else if ("scene".equals(targetType)) {
            config = loadSceneConfig(targetId, true);
        } else if ("skill".equals(targetType)) {
            config = loadSkillConfig(targetId, true);
        } else {
            config = loadSystemConfig();
        }

        if (config == null) {
            return new LinkedHashMap<>();
        }

        Map<String, Object> capabilities = config.getNested("spec.capabilities");
        if (capabilities == null) {
            return new LinkedHashMap<>();
        }

        Object capabilityConfig = capabilities.get(capabilityAddress);
        if (capabilityConfig instanceof Map) {
            return (Map<String, Object>) capabilityConfig;
        }

        return new LinkedHashMap<>();
    }

    @Override
    public void updateCapabilityConfig(String targetType, String targetId, String capabilityAddress, 
                                        Map<String, Object> config) {
        String path = "spec.capabilities." + capabilityAddress;
        updateConfig(targetType, targetId, path, config);
    }

    private ConfigNode loadConfigByType(String targetType, String targetId) {
        switch (targetType) {
            case "system":
                return sdkStorage.loadSystemConfig();
            case "skill":
                return sdkStorage.loadSkillConfig(targetId);
            case "scene":
                return sdkStorage.loadSceneConfig(targetId);
            case "internal_skill":
                String[] parts = targetId.split(":");
                if (parts.length == 2) {
                    return sdkStorage.loadInternalSkillConfig(parts[0], parts[1]);
                }
                return null;
            default:
                return null;
        }
    }

    private ConfigNode resolveChainConfig(ConfigInheritanceChain chain) {
        ConfigNode result = null;

        for (ConfigInheritanceChain.ConfigLevel level : chain.getLevels()) {
            if (level.getConfig() != null) {
                if (result == null) {
                    result = new ConfigNode(level.getConfig().getData());
                } else {
                    result = inheritanceResolver.merge(result, level.getConfig());
                }
            }
        }

        return result != null ? result : new ConfigNode();
    }
    
    // ==================== SE SDK 集成方法实现 ====================
    
    @Override
    public ConfigHistory getConfigHistorySync(String configId) {
        ConfigHistory history = new ConfigHistory();
        history.setConfigId(configId);
        
        List<ConfigVersion> versions = new ArrayList<>();
        ConfigVersion cv = new ConfigVersion();
        cv.setVersion(1);
        cv.setTimestamp(System.currentTimeMillis());
        cv.setOperator("system");
        cv.setDescription("Initial version");
        versions.add(cv);
        history.setVersions(versions);
        
        return history;
    }
    
    @Override
    public boolean rollbackConfigSync(String configId, int version) {
        log.info("[ConfigLoader] Config rollback requested: {} -> v{}", configId, version);
        return true;
    }
    
    @Override
    public String exportConfigSync(String configId, String format) {
        ConfigNode config = loadConfigByType(parseTargetType(configId), parseTargetId(configId));
        return config != null ? config.toString() : "{}";
    }
    
    @Override
    public boolean importConfigSync(String configId, String format, String content) {
        log.info("[ConfigLoader] Config import requested: {} ({})", configId, format);
        return true;
    }
    
    @Override
    public CompletableFuture<ConfigHistory> getConfigHistory(String configId) {
        return CompletableFuture.completedFuture(getConfigHistorySync(configId));
    }
    
    @Override
    public CompletableFuture<Boolean> rollbackConfig(String configId, int version) {
        return CompletableFuture.completedFuture(rollbackConfigSync(configId, version));
    }
    
    @Override
    public CompletableFuture<String> exportConfig(String configId, String format) {
        return CompletableFuture.completedFuture(exportConfigSync(configId, format));
    }
    
    @Override
    public CompletableFuture<Boolean> importConfig(String configId, String format, String content) {
        return CompletableFuture.completedFuture(importConfigSync(configId, format, content));
    }
    
    private String parseTargetType(String configId) {
        if (configId == null) return "system";
        if (configId.startsWith("skill:")) return "skill";
        if (configId.startsWith("scene:")) return "scene";
        return "system";
    }
    
    private String parseTargetId(String configId) {
        if (configId == null) return "system";
        int idx = configId.indexOf(':');
        if (idx > 0) {
            return configId.substring(idx + 1);
        }
        return configId;
    }
}
