package net.ooder.sdk.capability.model;

import java.util.List;

public class RetryPolicy {
    
    private int maxRetries;
    private int retryInterval;
    private double backoffMultiplier;
    private List<String> retryableErrors;
    
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    
    public int getRetryInterval() { return retryInterval; }
    public void setRetryInterval(int retryInterval) { this.retryInterval = retryInterval; }
    
    public double getBackoffMultiplier() { return backoffMultiplier; }
    public void setBackoffMultiplier(double backoffMultiplier) { this.backoffMultiplier = backoffMultiplier; }
    
    public List<String> getRetryableErrors() { return retryableErrors; }
    public void setRetryableErrors(List<String> retryableErrors) { this.retryableErrors = retryableErrors; }
}
