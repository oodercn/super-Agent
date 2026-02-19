package net.ooder.sdk.capability.model;

public class ChainErrorHandling {
    
    private ErrorStrategy strategy;
    private String fallbackLink;
    private String errorMessage;
    
    public ErrorStrategy getStrategy() { return strategy; }
    public void setStrategy(ErrorStrategy strategy) { this.strategy = strategy; }
    
    public String getFallbackLink() { return fallbackLink; }
    public void setFallbackLink(String fallbackLink) { this.fallbackLink = fallbackLink; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
