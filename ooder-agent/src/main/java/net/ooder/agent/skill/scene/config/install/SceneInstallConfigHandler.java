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
import java.util.List;
import java.util.Map;

@Service
public class SceneInstallConfigHandler {

    private static final Logger log = LoggerFactory.getLogger(SceneInstallConfigHandler.class);

    private final ConfigLoaderService configLoader;
    private final SdkConfigStorage sdkStorage;
    private final SkillInstallConfigHandler skillConfigHandler;

    @Autowired
    public SceneInstallConfigHandler(ConfigLoaderService configLoader, 
                                       SdkConfigStorage sdkStorage,
                                       SkillInstallConfigHandler skillConfigHandler) {
        this.configLoader = configLoader;
        this.sdkStorage = sdkStorage;
        this.skillConfigHandler = skillConfigHandler;
    }

    public void onSceneInstall(String sceneId, SceneInstallRequest request) {
        log.info("[SceneConfig] Installing scene config: {}", sceneId);

        ConfigNode sceneConfig = createSceneConfig(sceneId, request);
        sdkStorage.saveSceneConfig(sceneId, sceneConfig);

        if (request.getInternalSkills() != null) {
            for (String internalSkillId : request.getInternalSkills()) {
                ConfigNode internalConfig = createInternalSkillConfig(
                    sceneId, internalSkillId, 
                    request.getInternalSkillConfig(internalSkillId)
                );
                sdkStorage.saveInternalSkillConfig(sceneId, internalSkillId, internalConfig);
            }
        }

        log.info("[SceneConfig] Scene config saved: {}", sceneId);
    }

    public void onSceneUninstall(String sceneId) {
        log.info("[SceneConfig] Uninstalling scene config: {}", sceneId);

        sdkStorage.deleteConfig("scene", sceneId);

        log.info("[SceneConfig] Scene config deleted: {}", sceneId);
    }

    public void updateSceneConfig(String sceneId, ConfigNode config) {
        sdkStorage.saveSceneConfig(sceneId, config);
        log.info("[SceneConfig] Scene config updated: {}", sceneId);
    }

    private ConfigNode createSceneConfig(String sceneId, SceneInstallRequest request) {
        Map<String, Object> config = new LinkedHashMap<>();
        config.put("apiVersion", "skills.ooder.io/v1");
        config.put("kind", "SceneRuntimeConfig");

        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("sceneId", sceneId);
        metadata.put("installedAt", Instant.now().toString());
        metadata.put("updatedAt", Instant.now().toString());
        if (request.getSceneType() != null) {
            metadata.put("sceneType", request.getSceneType());
        }
        config.put("metadata", metadata);

        Map<String, Object> spec = new LinkedHashMap<>();
        spec.put("inheritFrom", "skill");

        if (request.getConfigOverrides() != null) {
            spec.put("overrides", request.getConfigOverrides());
        }
        if (request.getParticipants() != null) {
            spec.put("participants", request.getParticipants());
        }
        if (request.getDriverConditions() != null) {
            spec.put("driverConditions", request.getDriverConditions());
        }
        if (request.getLlmConfig() != null) {
            spec.put("llmConfig", request.getLlmConfig());
        }
        if (request.getKnowledgeConfig() != null) {
            spec.put("knowledgeConfig", request.getKnowledgeConfig());
        }

        config.put("spec", spec);

        return new ConfigNode(config);
    }

    private ConfigNode createInternalSkillConfig(String sceneId, String skillId, 
                                                   Map<String, Object> userConfig) {
        Map<String, Object> config = new LinkedHashMap<>();
        config.put("apiVersion", "skills.ooder.io/v1");
        config.put("kind", "InternalSkillRuntimeConfig");

        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("sceneId", sceneId);
        metadata.put("skillId", skillId);
        metadata.put("installedAt", Instant.now().toString());
        config.put("metadata", metadata);

        Map<String, Object> spec = new LinkedHashMap<>();
        spec.put("inheritFrom", "scene");
        if (userConfig != null) {
            spec.put("overrides", userConfig);
        }
        config.put("spec", spec);

        return new ConfigNode(config);
    }

    public static class SceneInstallRequest {
        private String sceneType;
        private List<String> internalSkills;
        private Map<String, Object> configOverrides;
        private Map<String, Object> participants;
        private Map<String, Object> driverConditions;
        private Map<String, Object> llmConfig;
        private Map<String, Object> knowledgeConfig;
        private Map<String, Map<String, Object>> internalSkillConfigs;

        public String getSceneType() { return sceneType; }
        public void setSceneType(String sceneType) { this.sceneType = sceneType; }
        public List<String> getInternalSkills() { return internalSkills; }
        public void setInternalSkills(List<String> internalSkills) { this.internalSkills = internalSkills; }
        public Map<String, Object> getConfigOverrides() { return configOverrides; }
        public void setConfigOverrides(Map<String, Object> configOverrides) { this.configOverrides = configOverrides; }
        public Map<String, Object> getParticipants() { return participants; }
        public void setParticipants(Map<String, Object> participants) { this.participants = participants; }
        public Map<String, Object> getDriverConditions() { return driverConditions; }
        public void setDriverConditions(Map<String, Object> driverConditions) { this.driverConditions = driverConditions; }
        public Map<String, Object> getLlmConfig() { return llmConfig; }
        public void setLlmConfig(Map<String, Object> llmConfig) { this.llmConfig = llmConfig; }
        public Map<String, Object> getKnowledgeConfig() { return knowledgeConfig; }
        public void setKnowledgeConfig(Map<String, Object> knowledgeConfig) { this.knowledgeConfig = knowledgeConfig; }
        public Map<String, Map<String, Object>> getInternalSkillConfigs() { return internalSkillConfigs; }
        public void setInternalSkillConfigs(Map<String, Map<String, Object>> internalSkillConfigs) { this.internalSkillConfigs = internalSkillConfigs; }
        public Map<String, Object> getInternalSkillConfig(String skillId) {
            return internalSkillConfigs != null ? internalSkillConfigs.get(skillId) : null;
        }
    }
}
