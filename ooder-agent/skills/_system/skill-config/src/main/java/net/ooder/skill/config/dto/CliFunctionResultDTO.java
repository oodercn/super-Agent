package net.ooder.skill.config.dto;

import java.util.Map;

public class CliFunctionResultDTO {
    
    private boolean success;
    private String functionName;
    private Map<String, Object> arguments;
    private Object result;
    private long timestamp;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getFunctionName() { return functionName; }
    public void setFunctionName(String functionName) { this.functionName = functionName; }
    public Map<String, Object> getArguments() { return arguments; }
    public void setArguments(Map<String, Object> arguments) { this.arguments = arguments; }
    public Object getResult() { return result; }
    public void setResult(Object result) { this.result = result; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
