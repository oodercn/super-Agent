package net.ooder.sdk.capability.model;

import java.util.Map;

public class ErrorHandling {
    
    private ErrorStrategy strategy;
    private String fallbackStep;
    private int maxErrors;
    private Map<String, String> errorMappings;
    
    public ErrorStrategy getStrategy() { return strategy; }
    public void setStrategy(ErrorStrategy strategy) { this.strategy = strategy; }
    
    public String getFallbackStep() { return fallbackStep; }
    public void setFallbackStep(String fallbackStep) { this.fallbackStep = fallbackStep; }
    
    public int getMaxErrors() { return maxErrors; }
    public void setMaxErrors(int maxErrors) { this.maxErrors = maxErrors; }
    
    public Map<String, String> getErrorMappings() { return errorMappings; }
    public void setErrorMappings(Map<String, String> errorMappings) { this.errorMappings = errorMappings; }
}
