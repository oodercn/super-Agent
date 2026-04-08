package net.ooder.mvp.skill.scene.dto.llm;

import java.util.List;
import java.util.Map;

public class LlmConfigTemplateDTO {
    
    private Long id;
    private String name;
    private String description;
    private String providerType;
    private String model;
    private Map<String, Object> providerConfig;
    private Map<String, Object> options;
    private List<String> tags;
    private boolean isDefault;
    private boolean isBuiltin;
    private long createdAt;
    private long updatedAt;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getProviderType() { return providerType; }
    public void setProviderType(String providerType) { this.providerType = providerType; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public Map<String, Object> getProviderConfig() { return providerConfig; }
    public void setProviderConfig(Map<String, Object> providerConfig) { this.providerConfig = providerConfig; }
    
    public Map<String, Object> getOptions() { return options; }
    public void setOptions(Map<String, Object> options) { this.options = options; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }
    
    public boolean isBuiltin() { return isBuiltin; }
    public void setBuiltin(boolean builtin) { isBuiltin = builtin; }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
    
    public LlmConfigDTO toConfigDTO(String level, String scopeId) {
        LlmConfigDTO config = new LlmConfigDTO();
        config.setName(this.name);
        config.setDescription(this.description);
        config.setProviderType(this.providerType);
        config.setModel(this.model);
        config.setProviderConfig(this.providerConfig != null ? new java.util.HashMap<>(this.providerConfig) : null);
        config.setOptions(this.options != null ? new java.util.HashMap<>(this.options) : null);
        config.setLevel(level);
        config.setScopeId(scopeId);
        config.setEnabled(true);
        return config;
    }
}
