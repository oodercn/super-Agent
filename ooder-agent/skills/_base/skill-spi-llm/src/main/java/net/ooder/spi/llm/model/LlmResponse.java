package net.ooder.spi.llm.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class LlmResponse {
    
    private String id;
    
    private String providerId;
    
    private String modelId;
    
    private List<Choice> choices;
    
    private Usage usage;
    
    private Long created;
    
    private Map<String, Object> metadata;
    
    @Data
    public static class Choice {
        private Integer index;
        private Message message;
        private String finishReason;
    }
    
    @Data
    public static class Message {
        private String role;
        private String content;
        private List<Map<String, Object>> toolCalls;
    }
    
    @Data
    public static class Usage {
        private Integer promptTokens;
        private Integer completionTokens;
        private Integer totalTokens;
    }
}
