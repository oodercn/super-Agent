package net.ooder.skill.config.dto;

import java.util.List;
import java.util.Map;

public class CliChatResultDTO {
    
    private boolean success;
    private String message;
    private List<String> suggestedFunctions;
    private Map<String, Object> parsedIntent;
    private List<Map<String, Object>> availableFunctions;
    private Map<String, Object> executionResult;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<String> getSuggestedFunctions() { return suggestedFunctions; }
    public void setSuggestedFunctions(List<String> suggestedFunctions) { this.suggestedFunctions = suggestedFunctions; }
    public Map<String, Object> getParsedIntent() { return parsedIntent; }
    public void setParsedIntent(Map<String, Object> parsedIntent) { this.parsedIntent = parsedIntent; }
    public List<Map<String, Object>> getAvailableFunctions() { return availableFunctions; }
    public void setAvailableFunctions(List<Map<String, Object>> availableFunctions) { this.availableFunctions = availableFunctions; }
    public Map<String, Object> getExecutionResult() { return executionResult; }
    public void setExecutionResult(Map<String, Object> executionResult) { this.executionResult = executionResult; }
}
