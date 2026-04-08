package net.ooder.agent.dto.llm;

public class LlmModelDTO {
    private String modelId;
    private String displayName;
    private Integer maxTokens;
    private Double defaultTemperature;
    private Boolean supportsFunctionCalling;
    private Boolean supportsMultimodal;
    private Boolean supportsEmbedding;
    private Double costPer1kTokens;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public Double getDefaultTemperature() {
        return defaultTemperature;
    }

    public void setDefaultTemperature(Double defaultTemperature) {
        this.defaultTemperature = defaultTemperature;
    }

    public Boolean getSupportsFunctionCalling() {
        return supportsFunctionCalling;
    }

    public void setSupportsFunctionCalling(Boolean supportsFunctionCalling) {
        this.supportsFunctionCalling = supportsFunctionCalling;
    }

    public Boolean getSupportsMultimodal() {
        return supportsMultimodal;
    }

    public void setSupportsMultimodal(Boolean supportsMultimodal) {
        this.supportsMultimodal = supportsMultimodal;
    }

    public Boolean getSupportsEmbedding() {
        return supportsEmbedding;
    }

    public void setSupportsEmbedding(Boolean supportsEmbedding) {
        this.supportsEmbedding = supportsEmbedding;
    }

    public Double getCostPer1kTokens() {
        return costPer1kTokens;
    }

    public void setCostPer1kTokens(Double costPer1kTokens) {
        this.costPer1kTokens = costPer1kTokens;
    }
}
