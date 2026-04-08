package net.ooder.mvp.skill.scene.config.service;

import net.ooder.mvp.skill.scene.config.sdk.ConfigNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigInheritanceChain {

    private String targetType;
    private String targetId;
    private List<ConfigLevel> levels = new ArrayList<>();
    private ConfigNode resolvedConfig;

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public List<ConfigLevel> getLevels() {
        return levels;
    }

    public void setLevels(List<ConfigLevel> levels) {
        this.levels = levels;
    }

    public ConfigNode getResolvedConfig() {
        return resolvedConfig;
    }

    public void setResolvedConfig(ConfigNode resolvedConfig) {
        this.resolvedConfig = resolvedConfig;
    }

    public void addLevel(String level, String source, ConfigNode config) {
        levels.add(new ConfigLevel(level, source, config));
    }

    public Object getInheritedValue(String key) {
        for (int i = levels.size() - 2; i >= 0; i--) {
            ConfigLevel level = levels.get(i);
            if (level.getConfig() != null && level.getConfig().containsKey(key)) {
                return level.getConfig().get(key);
            }
        }
        return null;
    }

    public Map<String, String> getValueSources() {
        Map<String, String> sources = new LinkedHashMap<>();
        for (ConfigLevel level : levels) {
            if (level.getConfig() != null) {
                for (String key : level.getConfig().getData().keySet()) {
                    if (!sources.containsKey(key)) {
                        sources.put(key, level.getLevel());
                    }
                }
            }
        }
        return sources;
    }

    public static class ConfigLevel {
        private String level;
        private String source;
        private ConfigNode config;
        private Map<String, Object> overriddenKeys;

        public ConfigLevel(String level, String source, ConfigNode config) {
            this.level = level;
            this.source = source;
            this.config = config;
            this.overriddenKeys = new LinkedHashMap<>();
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public ConfigNode getConfig() {
            return config;
        }

        public void setConfig(ConfigNode config) {
            this.config = config;
        }

        public Map<String, Object> getOverriddenKeys() {
            return overriddenKeys;
        }

        public void setOverriddenKeys(Map<String, Object> overriddenKeys) {
            this.overriddenKeys = overriddenKeys;
        }

        public void addOverride(String key, Object value) {
            overriddenKeys.put(key, value);
        }
    }
}
