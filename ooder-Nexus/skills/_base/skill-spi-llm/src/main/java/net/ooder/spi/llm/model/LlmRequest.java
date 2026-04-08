package net.ooder.spi.llm.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class LlmRequest {
    
    private String providerId;
    
    private String modelId;
    
    private List<Message> messages;
    
    private Double temperature;
    
    private Integer maxTokens;
    
    private Double topP;
    
    private List<String> stop;
    
    private Map<String, Object> extraParams;
    
    private boolean stream;
    
    @Data
    public static class Message {
        private String role;
        private String content;
        private String name;
        private List<Map<String, Object>> toolCalls;
    }
}
