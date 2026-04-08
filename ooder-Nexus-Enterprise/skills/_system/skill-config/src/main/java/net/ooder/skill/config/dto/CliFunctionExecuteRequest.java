package net.ooder.skill.config.dto;

import java.util.Map;

public class CliFunctionExecuteRequest {
    
    private String functionName;
    private Map<String, Object> arguments;

    public String getFunctionName() { return functionName; }
    public void setFunctionName(String functionName) { this.functionName = functionName; }
    public Map<String, Object> getArguments() { return arguments; }
    public void setArguments(Map<String, Object> arguments) { this.arguments = arguments; }
}
