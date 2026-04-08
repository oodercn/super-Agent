package net.ooder.mvp.skill.scene.config.install;

import net.ooder.mvp.skill.scene.config.sdk.ConfigNode;
import net.ooder.mvp.skill.scene.config.sdk.SdkConfigStorage;
import net.ooder.mvp.skill.scene.config.service.ConfigLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class SkillInstallConfigHandler {

    private static final Logger log = LoggerFactory.getLogger(SkillInstallConfigHandler.class);

    private final ConfigLoaderService configLoader;
    private final SdkConfigStorage sdkStorage;

    @Autowired
    public SkillInstallConfigHandler(ConfigLoaderService configLoader, SdkConfigStorage sdkStorage) {
        this.configLoader = configLoader;
        this.sdkStorage = sdkStorage;
    }

    public void onSkillInstall(String skillId, Map<String, Object> userConfig) {
        log.info("[SkillConfig] Installing skill config: {}", skillId);

        ConfigNode systemConfig = configLoader.loadSystemConfig();
        ConfigNode skillConfig = createSkillConfig(skillId, systemConfig, userConfig);

        sdkStorage.saveSkillConfig(skillId, skillConfig);
        log.info("[SkillConfig] Skill config saved: {}", skillId);
    }

    public void onSkillUninstall(String skillId) {
        log.info("[SkillConfig] Uninstalling skill config: {}", skillId);
        sdkStorage.deleteConfig("skill", skillId);
    }

    public void onSkillUpgrade(String skillId, Map<String, Object> newConfig) {
        log.info("[SkillConfig] Upgrading skill config: {}", skillId);

        ConfigNode existingConfig = sdkStorage.loadSkillConfig(skillId);
        if (existingConfig != null) {
            ConfigNode mergedConfig = mergeConfigs(existingConfig, newConfig);
            sdkStorage.saveSkillConfig(skillId, mergedConfig);
        } else {
            onSkillInstall(skillId, newConfig);
        }
    }

    public void updateSkillConfig(String skillId, ConfigNode config) {
        sdkStorage.saveSkillConfig(skillId, config);
        log.info("[SkillConfig] Skill config updated: {}", skillId);
    }

    private ConfigNode createSkillConfig(String skillId, ConfigNode systemConfig, 
                                          Map<String, Object> userConfig) {
        Map<String, Object> config = new LinkedHashMap<>();
        config.put("apiVersion", "skills.ooder.io/v1");
        config.put("kind", "SkillRuntimeConfig");

        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("skillId", skillId);
        metadata.put("installedAt", Instant.now().toString());
        metadata.put("updatedAt", Instant.now().toString());
        config.put("metadata", metadata);

        Map<String, Object> spec = new LinkedHashMap<>();
        spec.put("inheritFrom", "system");
        spec.put("overrides", extractOverrides(userConfig));
        spec.put("userConfig", userConfig != null ? userConfig : new LinkedHashMap<>());
        config.put("spec", spec);

        return new ConfigNode(config);
    }

    private Map<String, Object> extractOverrides(Map<String, Object> userConfig) {
        Map<String, Object> overrides = new LinkedHashMap<>();

        if (userConfig == null) {
            return overrides;
        }

        for (Map.Entry<String, Object> entry : userConfig.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("capabilities.")) {
                String[] parts = key.split("\\.", 3);
                if (parts.length >= 3) {
                    String capability = parts[1];
                    String configKey = parts[2];

                    @SuppressWarnings("unchecked")
                    Map<String, Object> capOverrides = (Map<String, Object>) 
                        overrides.computeIfAbsent(capability, k -> new LinkedHashMap<>());
                    capOverrides.put(configKey, entry.getValue());
                }
            } else if (key.startsWith("llmConfig.")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> llmOverrides = (Map<String, Object>) 
                    overrides.computeIfAbsent("llm", k -> new LinkedHashMap<>());
                llmOverrides.put(key.substring("llmConfig.".length()), entry.getValue());
            } else if (key.startsWith("knowledgeConfig.")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> knowOverrides = (Map<String, Object>) 
                    overrides.computeIfAbsent("know", k -> new LinkedHashMap<>());
                knowOverrides.put(key.substring("knowledgeConfig.".length()), entry.getValue());
            }
        }

        return overrides;
    }

    @SuppressWarnings("unchecked")
    private ConfigNode mergeConfigs(ConfigNode existing, Map<String, Object> newConfig) {
        Map<String, Object> config = new LinkedHashMap<>(existing.getData());

        Map<String, Object> metadata = (Map<String, Object>) config.get("metadata");
        if (metadata != null) {
            metadata.put("updatedAt", Instant.now().toString());
        }

        Map<String, Object> spec = (Map<String, Object>) config.get("spec");
        if (spec != null && newConfig != null) {
            Map<String, Object> userConfig = (Map<String, Object>) spec.get("userConfig");
            if (userConfig == null) {
                userConfig = new LinkedHashMap<>();
                spec.put("userConfig", userConfig);
            }
            userConfig.putAll(newConfig);

            Map<String, Object> overrides = extractOverrides(newConfig);
            Map<String, Object> existingOverrides = (Map<String, Object>) spec.get("overrides");
            if (existingOverrides == null) {
                spec.put("overrides", overrides);
            } else {
                existingOverrides.putAll(overrides);
            }
        }

        return new ConfigNode(config);
    }
}
