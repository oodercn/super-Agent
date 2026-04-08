package net.ooder.mvp.skill.scene.dto.llm;

import java.util.List;
import java.util.Map;

public class LlmProviderMeta {
    
    private String type;
    private String name;
    private String icon;
    private String description;
    private String website;
    private List<ModelInfo> supportedModels;
    private Map<String, ConfigField> configSchema;
    private List<String> capabilities;
    private boolean requiresApiKey;
    private String authType;
    
    public static class ModelInfo {
        private String id;
        private String name;
        private String type;
        private int contextWindow;
        private int maxOutputTokens;
        private double inputPricePer1k;
        private double outputPricePer1k;
        private List<String> capabilities;
        private boolean available;
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public int getContextWindow() { return contextWindow; }
        public void setContextWindow(int contextWindow) { this.contextWindow = contextWindow; }
        public int getMaxOutputTokens() { return maxOutputTokens; }
        public void setMaxOutputTokens(int maxOutputTokens) { this.maxOutputTokens = maxOutputTokens; }
        public double getInputPricePer1k() { return inputPricePer1k; }
        public void setInputPricePer1k(double inputPricePer1k) { this.inputPricePer1k = inputPricePer1k; }
        public double getOutputPricePer1k() { return outputPricePer1k; }
        public void setOutputPricePer1k(double outputPricePer1k) { this.outputPricePer1k = outputPricePer1k; }
        public List<String> getCapabilities() { return capabilities; }
        public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }
    }
    
    public static class ConfigField {
        private String name;
        private String label;
        private String type;
        private boolean required;
        private String defaultValue;
        private String description;
        private List<String> options;
        private String validation;
        private boolean secret;
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
        public String getDefaultValue() { return defaultValue; }
        public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public List<String> getOptions() { return options; }
        public void setOptions(List<String> options) { this.options = options; }
        public String getValidation() { return validation; }
        public void setValidation(String validation) { this.validation = validation; }
        public boolean isSecret() { return secret; }
        public void setSecret(boolean secret) { this.secret = secret; }
    }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public List<ModelInfo> getSupportedModels() { return supportedModels; }
    public void setSupportedModels(List<ModelInfo> supportedModels) { this.supportedModels = supportedModels; }
    public Map<String, ConfigField> getConfigSchema() { return configSchema; }
    public void setConfigSchema(Map<String, ConfigField> configSchema) { this.configSchema = configSchema; }
    public List<String> getCapabilities() { return capabilities; }
    public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
    public boolean isRequiresApiKey() { return requiresApiKey; }
    public void setRequiresApiKey(boolean requiresApiKey) { this.requiresApiKey = requiresApiKey; }
    public String getAuthType() { return authType; }
    public void setAuthType(String authType) { this.authType = authType; }
}
