package net.ooder.sdk.api.llm;

import java.util.Map;

public class LlmConfig {
    
    private String apiKey;
    private String baseUrl;
    private String defaultModel;
    private int maxTokens;
    private double temperature;
    private int timeout;
    private Map<String, Object> extraConfig;
    
    public LlmConfig() {
        this.defaultModel = "gpt-4";
        this.maxTokens = 4096;
        this.temperature = 0.7;
        this.timeout = 60000;
    }
    
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    
    public String getDefaultModel() { return defaultModel; }
    public void setDefaultModel(String defaultModel) { this.defaultModel = defaultModel; }
    
    public int getMaxTokens() { return maxTokens; }
    public void setMaxTokens(int maxTokens) { this.maxTokens = maxTokens; }
    
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    
    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
    
    public Map<String, Object> getExtraConfig() { return extraConfig; }
    public void setExtraConfig(Map<String, Object> extraConfig) { this.extraConfig = extraConfig; }
}
