package net.ooder.sdk.capability.model;

import java.util.Map;

public class ExecutionLog {
    
    private String logId;
    private String capabilityId;
    private String executionId;
    private int level;
    private String message;
    private Map<String, Object> context;
    private long timestamp;
    
    public String getLogId() { return logId; }
    public void setLogId(String logId) { this.logId = logId; }
    
    public String getCapabilityId() { return capabilityId; }
    public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
    
    public String getExecutionId() { return executionId; }
    public void setExecutionId(String executionId) { this.executionId = executionId; }
    
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Map<String, Object> getContext() { return context; }
    public void setContext(Map<String, Object> context) { this.context = context; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
