package net.ooder.enexus.llm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LLMResponse {
    
    private String conversationId;
    private String message;
    private List<FunctionCall> functionCalls;
    private boolean success;
    private String errorMessage;
    private Map<String, Object> metadata;
    
    public LLMResponse() {
        this.functionCalls = new ArrayList<>();
        this.success = true;
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
    
    public List<FunctionCall> getFunctionCalls() {
        return functionCalls;
    }
    
    public void setFunctionCalls(List<FunctionCall> functionCalls) {
        this.functionCalls = functionCalls;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public boolean hasFunctionCall() {
        return functionCalls != null && !functionCalls.isEmpty();
    }
}
