package net.ooder.sdk.capability.model;

import java.util.Map;

public class ExecutionConfig {
    
    private int timeout;
    private int retryCount;
    private int retryInterval;
    private String executor;
    private Map<String, Object> properties;
    
    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
    
    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    
    public int getRetryInterval() { return retryInterval; }
    public void setRetryInterval(int retryInterval) { this.retryInterval = retryInterval; }
    
    public String getExecutor() { return executor; }
    public void setExecutor(String executor) { this.executor = executor; }
    
    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
}
