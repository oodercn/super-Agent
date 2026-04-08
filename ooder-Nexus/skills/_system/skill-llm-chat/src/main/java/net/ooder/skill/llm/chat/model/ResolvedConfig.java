package net.ooder.skill.llm.chat.model;

import java.util.Map;

public class ResolvedConfig {
    
    private String providerType;
    private String model;
    private String apiKey;
    private String baseUrl;
    private Map<String, Object> options;
    private Map<String, Object> providerConfig;
    private ConfigSource source;
    private int priority;

    public static class ConfigSource {
        private LlmConfig.ConfigLevel level;
        private String scopeId;

        public ConfigSource(LlmConfig.ConfigLevel level, String scopeId) {
            this.level = level;
            this.scopeId = scopeId;
        }

        public LlmConfig.ConfigLevel getLevel() { return level; }
        public String getScopeId() { return scopeId; }
    }

    public String getProviderType() { return providerType; }
    public void setProviderType(String providerType) { this.providerType = providerType; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public Map<String, Object> getOptions() { return options; }
    public void setOptions(Map<String, Object> options) { this.options = options; }
    public Map<String, Object> getProviderConfig() { return providerConfig; }
    public void setProviderConfig(Map<String, Object> providerConfig) { this.providerConfig = providerConfig; }
    public ConfigSource getSource() { return source; }
    public void setSource(ConfigSource source) { this.source = source; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
}
