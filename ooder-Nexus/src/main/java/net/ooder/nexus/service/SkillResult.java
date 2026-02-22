package net.ooder.nexus.service;

import java.util.Map;

public class SkillResult {
    
    private boolean success;
    private Object data;
    private String message;
    private long executionTime;
    private Map<String, Object> metadata;

    public SkillResult() {}

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public long getExecutionTime() { return executionTime; }
    public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
