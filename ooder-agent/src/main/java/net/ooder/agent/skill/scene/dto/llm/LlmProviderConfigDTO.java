package net.ooder.mvp.skill.scene.dto.llm;

import java.util.List;
import java.util.Map;

public class LlmProviderConfigDTO {
    
    private String providerId;
    private String name;
    private String type;
    private String defaultModel;
    private boolean enabled;
    private long timeout;
    private int maxRetries;
    private boolean functionCallingEnabled;
    private int maxIterations;
    private List<ModelConfigDTO> models;
    private Map<String, Object> providerConfig;
    private long createTime;
    private long updateTime;
    private boolean configured;
    
    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDefaultModel() { return defaultModel; }
    public void setDefaultModel(String defaultModel) { this.defaultModel = defaultModel; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public long getTimeout() { return timeout; }
    public void setTimeout(long timeout) { this.timeout = timeout; }
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public boolean isFunctionCallingEnabled() { return functionCallingEnabled; }
    public void setFunctionCallingEnabled(boolean functionCallingEnabled) { this.functionCallingEnabled = functionCallingEnabled; }
    public int getMaxIterations() { return maxIterations; }
    public void setMaxIterations(int maxIterations) { this.maxIterations = maxIterations; }
    public List<ModelConfigDTO> getModels() { return models; }
    public void setModels(List<ModelConfigDTO> models) { this.models = models; }
    public Map<String, Object> getProviderConfig() { return providerConfig; }
    public void setProviderConfig(Map<String, Object> providerConfig) { this.providerConfig = providerConfig; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public long getUpdateTime() { return updateTime; }
    public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
    public boolean isConfigured() { return configured; }
    public void setConfigured(boolean configured) { this.configured = configured; }
    
    public static class ModelConfigDTO {
        private String modelId;
        private String displayName;
        private int maxTokens;
        private double temperature;
        private boolean supportsFunctionCalling;
        private boolean supportsMultimodal;
        private boolean supportsEmbedding;
        private int embeddingDimension;
        private double costPer1kTokens;
        
        public String getModelId() { return modelId; }
        public void setModelId(String modelId) { this.modelId = modelId; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public int getMaxTokens() { return maxTokens; }
        public void setMaxTokens(int maxTokens) { this.maxTokens = maxTokens; }
        public double getTemperature() { return temperature; }
        public void setTemperature(double temperature) { this.temperature = temperature; }
        public boolean isSupportsFunctionCalling() { return supportsFunctionCalling; }
        public void setSupportsFunctionCalling(boolean supportsFunctionCalling) { this.supportsFunctionCalling = supportsFunctionCalling; }
        public boolean isSupportsMultimodal() { return supportsMultimodal; }
        public void setSupportsMultimodal(boolean supportsMultimodal) { this.supportsMultimodal = supportsMultimodal; }
        public boolean isSupportsEmbedding() { return supportsEmbedding; }
        public void setSupportsEmbedding(boolean supportsEmbedding) { this.supportsEmbedding = supportsEmbedding; }
        public int getEmbeddingDimension() { return embeddingDimension; }
        public void setEmbeddingDimension(int embeddingDimension) { this.embeddingDimension = embeddingDimension; }
        public double getCostPer1kTokens() { return costPer1kTokens; }
        public void setCostPer1kTokens(double costPer1kTokens) { this.costPer1kTokens = costPer1kTokens; }
    }
}
