package net.ooder.mvp.skill.scene.dto.llm;

import java.util.List;
import java.util.Map;

public class LlmConfigDTO {
    
    public static final String LEVEL_SYSTEM = "SYSTEM";
    public static final String LEVEL_ENTERPRISE = "ENTERPRISE";
    public static final String LEVEL_AGENT = "AGENT";
    public static final String LEVEL_SCENE = "SCENE";
    public static final String LEVEL_USER = "USER";
    
    private Long id;
    private String name;
    private String level;
    private String scopeId;
    private String providerType;
    private String model;
    private Map<String, Object> providerConfig;
    private Map<String, Object> options;
    private Boolean enabled;
    private Long updatedAt;
    private Long createdAt;
    private String createdBy;
    
    private String agentId;
    private List<String> models;
    private Map<String, Object> rateLimits;
    private Map<String, Object> costConfig;
    private boolean fallbackEnabled;
    private String fallbackConfigId;
    private long lastUsedTime;
    private long totalTokens;
    private long totalRequests;
    private double totalCost;
    private String description;
    private List<String> tags;
    private int priority;
    private Map<String, Object> extendedConfig;
    
    public LlmConfigDTO() {
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    
    public String getScopeId() { return scopeId; }
    public void setScopeId(String scopeId) { this.scopeId = scopeId; }
    
    public String getProviderType() { return providerType; }
    public void setProviderType(String providerType) { this.providerType = providerType; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public Map<String, Object> getProviderConfig() { return providerConfig; }
    public void setProviderConfig(Map<String, Object> providerConfig) { this.providerConfig = providerConfig; }
    
    public Map<String, Object> getOptions() { return options; }
    public void setOptions(Map<String, Object> options) { this.options = options; }
    
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    
    public Long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Long updatedAt) { this.updatedAt = updatedAt; }
    
    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    
    public List<String> getModels() { return models; }
    public void setModels(List<String> models) { this.models = models; }
    
    public Map<String, Object> getRateLimits() { return rateLimits; }
    public void setRateLimits(Map<String, Object> rateLimits) { this.rateLimits = rateLimits; }
    
    public Map<String, Object> getCostConfig() { return costConfig; }
    public void setCostConfig(Map<String, Object> costConfig) { this.costConfig = costConfig; }
    
    public boolean isFallbackEnabled() { return fallbackEnabled; }
    public void setFallbackEnabled(boolean fallbackEnabled) { this.fallbackEnabled = fallbackEnabled; }
    
    public String getFallbackConfigId() { return fallbackConfigId; }
    public void setFallbackConfigId(String fallbackConfigId) { this.fallbackConfigId = fallbackConfigId; }
    
    public long getLastUsedTime() { return lastUsedTime; }
    public void setLastUsedTime(long lastUsedTime) { this.lastUsedTime = lastUsedTime; }
    
    public long getTotalTokens() { return totalTokens; }
    public void setTotalTokens(long totalTokens) { this.totalTokens = totalTokens; }
    
    public long getTotalRequests() { return totalRequests; }
    public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
    
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    
    public Map<String, Object> getExtendedConfig() { return extendedConfig; }
    public void setExtendedConfig(Map<String, Object> extendedConfig) { this.extendedConfig = extendedConfig; }
    
    public int getLevelPriority() {
        switch (level) {
            case LEVEL_USER: return 100;
            case LEVEL_SCENE: return 80;
            case LEVEL_AGENT: return 60;
            case LEVEL_ENTERPRISE: return 40;
            case LEVEL_SYSTEM: return 20;
            default: return 0;
        }
    }
    
    public boolean isHigherPriorityThan(LlmConfigDTO other) {
        if (other == null) return true;
        return this.getLevelPriority() > other.getLevelPriority();
    }
}
