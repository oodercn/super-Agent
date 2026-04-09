package net.ooder.nexus.llm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LLMRequest {
    
    private String agentId;
    private String conversationId;
    private String message;
    private String systemPrompt;
    private List<Map<String, String>> history;
    private Map<String, Object> config;
    private List<Map<String, Object>> functions;
    private boolean streaming;
    
    public LLMRequest() {
        this.history = new ArrayList<>();
        this.config = new HashMap<>();
        this.functions = new ArrayList<>();
    }
    
    public String getAgentId() {
        return agentId;
    }
    
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
    
    public String getConversationId() {
        return conversationId;
    }
    
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getSystemPrompt() {
        return systemPrompt;
    }
    
    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }
    
    public List<Map<String, String>> getHistory() {
        return history;
    }
    
    public void setHistory(List<Map<String, String>> history) {
        this.history = history;
    }
    
    public Map<String, Object> getConfig() {
        return config;
    }
    
    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }
    
    public List<Map<String, Object>> getFunctions() {
        return functions;
    }
    
    public void setFunctions(List<Map<String, Object>> functions) {
        this.functions = functions;
    }
    
    public boolean isStreaming() {
        return streaming;
    }
    
    public void setStreaming(boolean streaming) {
        this.streaming = streaming;
    }
    
    public void addMessages(List<Map<String, Object>> messages) {
        // Add messages to history
        for (Map<String, Object> msg : messages) {
            Map<String, String> historyMsg = new HashMap<>();
            historyMsg.put("role", (String) msg.get("role"));
            historyMsg.put("content", (String) msg.get("content"));
            this.history.add(historyMsg);
        }
    }
}
