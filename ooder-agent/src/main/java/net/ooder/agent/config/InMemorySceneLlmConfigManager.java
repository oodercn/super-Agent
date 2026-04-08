package net.ooder.agent.config;

import net.ooder.scene.llm.config.SceneLlmConfigInfo;
import net.ooder.scene.llm.config.SceneLlmConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySceneLlmConfigManager implements SceneLlmConfigManager {

    private static final Logger log = LoggerFactory.getLogger(InMemorySceneLlmConfigManager.class);

    private final Map<String, SceneLlmConfigInfo> configs = new ConcurrentHashMap<>();
    private SceneLlmConfigInfo defaultConfig;

    @Override
    public SceneLlmConfigInfo getLlmConfig(String sceneGroupId) {
        SceneLlmConfigInfo config = configs.get(sceneGroupId);
        if (config == null) {
            return getDefaultConfig();
        }
        return config;
    }

    @Override
    public void setLlmConfig(String sceneGroupId, SceneLlmConfigInfo config) {
        configs.put(sceneGroupId, config);
        log.info("Set LLM config for scene group {}", sceneGroupId);
    }

    @Override
    public void updateLlmConfig(String sceneGroupId, SceneLlmConfigInfo config) {
        SceneLlmConfigInfo existing = configs.get(sceneGroupId);
        if (existing != null) {
            if (config.getProvider() != null) {
                existing.setProvider(config.getProvider());
            }
            if (config.getModel() != null) {
                existing.setModel(config.getModel());
            }
            existing.setTemperature(config.getTemperature());
            existing.setMaxTokens(config.getMaxTokens());
            if (config.getExtensions() != null) {
                existing.getExtensions().putAll(config.getExtensions());
            }
        } else {
            setLlmConfig(sceneGroupId, config);
        }
    }

    @Override
    public void resetLlmConfig(String sceneGroupId) {
        configs.remove(sceneGroupId);
        log.info("Reset LLM config for scene group {}", sceneGroupId);
    }

    @Override
    public boolean hasCustomConfig(String sceneGroupId) {
        return configs.containsKey(sceneGroupId);
    }

    @Override
    public SceneLlmConfigInfo getDefaultConfig() {
        if (defaultConfig == null) {
            defaultConfig = new SceneLlmConfigInfo("default");
            defaultConfig.setProvider("openai");
            defaultConfig.setModel("gpt-4");
            defaultConfig.setTemperature(0.7);
            defaultConfig.setMaxTokens(2048);
        }
        return defaultConfig;
    }

    @Override
    public void setDefaultConfig(SceneLlmConfigInfo config) {
        this.defaultConfig = config;
        log.info("Set default LLM config: provider={}, model={}", 
            config.getProvider(), config.getModel());
    }
}
