package net.ooder.nexus.dto.llm;

import java.util.List;

public class LlmProviderDTO {
    private String id;
    private String code;
    private String name;
    private String displayName;
    private String description;
    private Boolean enabled;
    private Boolean isCurrent;
    private Boolean isConfigured;
    private String defaultBaseUrl;
    private List<String> models;
    private List<LlmModelDTO> modelDetails;
    private Integer modelCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public Boolean getIsConfigured() {
        return isConfigured;
    }

    public void setIsConfigured(Boolean isConfigured) {
        this.isConfigured = isConfigured;
    }

    public String getDefaultBaseUrl() {
        return defaultBaseUrl;
    }

    public void setDefaultBaseUrl(String defaultBaseUrl) {
        this.defaultBaseUrl = defaultBaseUrl;
    }

    public List<String> getModels() {
        return models;
    }

    public void setModels(List<String> models) {
        this.models = models;
    }

    public List<LlmModelDTO> getModelDetails() {
        return modelDetails;
    }

    public void setModelDetails(List<LlmModelDTO> modelDetails) {
        this.modelDetails = modelDetails;
    }

    public Integer getModelCount() {
        return modelCount;
    }

    public void setModelCount(Integer modelCount) {
        this.modelCount = modelCount;
    }
}
