package net.ooder.mvp.skill.scene.dto.llm;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

public class ChatRequestDTO {
    
    @NotBlank(message = "消息不能为空")
    private String message;
    
    private String conversationId;
    
    private String model;
    
    private String provider;
    
    private Double temperature;
    
    private Integer maxTokens;
    
    private List<Map<String, Object>> history;
    
    private Map<String, Object> context;

    public ChatRequestDTO() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public List<Map<String, Object>> getHistory() {
        return history;
    }

    public void setHistory(List<Map<String, Object>> history) {
        this.history = history;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }
}
