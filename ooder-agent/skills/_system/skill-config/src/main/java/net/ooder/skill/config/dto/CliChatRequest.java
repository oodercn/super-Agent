package net.ooder.skill.config.dto;

import java.util.List;
import java.util.Map;

public class CliChatRequest {
    
    private String message;
    private List<Map<String, Object>> context;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<Map<String, Object>> getContext() { return context; }
    public void setContext(List<Map<String, Object>> context) { this.context = context; }
}
