package net.ooder.skill.llm.chat.model;

import java.util.Map;

public class LlmConfig {
    
    private String id;
    private String name;
    private ConfigLevel level;
    private String scopeId;
    private String providerType;
    private String model;
    private Map<String, Object> options;
    private Map<String, Object> providerConfig;
    private long createdAt;
    private long updatedAt;
    private String createdBy;
    private boolean enabled;

    public enum ConfigLevel {
        ENTERPRISE("enterprise", "企业级"),
        DEPARTMENT("department", "部门级"),
        SCENE("scene", "场景级"),
        PERSONAL("personal", "个人级");

        private final String code;
        private final String description;

        ConfigLevel(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() { return code; }
        public String getDescription() { return description; }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ConfigLevel getLevel() { return level; }
    public void setLevel(ConfigLevel level) { this.level = level; }
    public String getScopeId() { return scopeId; }
    public void setScopeId(String scopeId) { this.scopeId = scopeId; }
    public String getProviderType() { return providerType; }
    public void setProviderType(String providerType) { this.providerType = providerType; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public Map<String, Object> getOptions() { return options; }
    public void setOptions(Map<String, Object> options) { this.options = options; }
    public Map<String, Object> getProviderConfig() { return providerConfig; }
    public void setProviderConfig(Map<String, Object> providerConfig) { this.providerConfig = providerConfig; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
