package net.ooder.agent.llm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FunctionResult {
    
    private boolean success;
    private Object result;
    private String error;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public FunctionResult() {
    }
    
    public FunctionResult(boolean success, Object result, String error) {
        this.success = success;
        this.result = result;
        this.error = error;
    }
    
    public static FunctionResult success(Object result) {
        return new FunctionResult(true, result, null);
    }
    
    public static FunctionResult error(String error) {
        return new FunctionResult(false, null, error);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public Object getResult() {
        return result;
    }
    
    public void setResult(Object result) {
        this.result = result;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public String toJson() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"success\":false,\"error\":\"Failed to serialize result\"}";
        }
    }
}
