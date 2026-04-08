package net.ooder.spi.llm.model;

import lombok.Data;

@Data
public class LlmModel {
    
    private String id;
    
    private String name;
    
    private String description;
    
    private Integer maxTokens;
    
    private boolean supportsStreaming;
    
    private boolean supportsFunctionCalling;
    
    private boolean supportsVision;
    
    public LlmModel() {
    }
    
    public LlmModel(String id, String name, Integer maxTokens) {
        this.id = id;
        this.name = name;
        this.maxTokens = maxTokens;
        this.supportsStreaming = true;
        this.supportsFunctionCalling = false;
        this.supportsVision = false;
    }
    
    public LlmModel(String id, String name, String description, Integer maxTokens) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxTokens = maxTokens;
        this.supportsStreaming = true;
        this.supportsFunctionCalling = false;
        this.supportsVision = false;
    }
}
