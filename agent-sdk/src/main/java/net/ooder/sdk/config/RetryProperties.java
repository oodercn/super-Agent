package net.ooder.sdk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ooder.sdk.retry")
public class RetryProperties {
    
    private int maxRetries = 3;
    
    private int delayBase = 1000;
    
    private RetryStrategy strategy = RetryStrategy.EXPONENTIAL;
    
    private boolean jitterEnabled = true;
    
    public enum RetryStrategy {
        FIXED,
        LINEAR,
        EXPONENTIAL,
        EXPONENTIAL_WITH_JITTER
    }
    
    public int getMaxRetries() {
        return maxRetries;
    }
    
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
    
    public int getDelayBase() {
        return delayBase;
    }
    
    public void setDelayBase(int delayBase) {
        this.delayBase = delayBase;
    }
    
    public RetryStrategy getStrategy() {
        return strategy;
    }
    
    public void setStrategy(RetryStrategy strategy) {
        this.strategy = strategy;
    }
    
    public boolean isJitterEnabled() {
        return jitterEnabled;
    }
    
    public void setJitterEnabled(boolean jitterEnabled) {
        this.jitterEnabled = jitterEnabled;
    }
}
