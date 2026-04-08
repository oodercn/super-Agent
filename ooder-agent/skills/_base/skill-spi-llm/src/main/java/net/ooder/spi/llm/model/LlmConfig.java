package net.ooder.spi.llm.model;

import lombok.Data;
import java.util.Map;

@Data
public class LlmConfig {
    
    private String providerId;
    
    private String modelId;
    
    private String apiKey;
    
    private String baseUrl;
    
    private Double temperature;
    
    private Integer maxTokens;
    
    private Double topP;
    
    private Map<String, Object> extraParams;
    
    public static LlmConfig defaultConfig(String providerId, String modelId) {
        LlmConfig config = new LlmConfig();
        config.setProviderId(providerId);
        config.setModelId(modelId);
        config.setTemperature(0.7);
        config.setMaxTokens(2000);
        config.setTopP(1.0);
        return config;
    }
}
