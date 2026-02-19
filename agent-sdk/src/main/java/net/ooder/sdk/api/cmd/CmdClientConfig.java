package net.ooder.sdk.api.cmd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CmdClientConfig {
    
    private String clientId;
    private String sceneId;
    private String groupId;
    private String domainId;
    
    private long defaultTimeout = 30000;
    private int maxRetryCount = 3;
    private int retryInterval = 1000;
    private boolean asyncEnabled = true;
    private boolean rollbackEnabled = false;
    
    private Map<String, Object> properties = new ConcurrentHashMap<>();
    
    public CmdClientConfig() {
    }
    
    public CmdClientConfig(String clientId, String sceneId) {
        this.clientId = clientId;
        this.sceneId = sceneId;
    }
    
    public static CmdClientConfig create(String clientId, String sceneId) {
        return new CmdClientConfig(clientId, sceneId);
    }
    
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    
    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    
    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
    
    public long getDefaultTimeout() { return defaultTimeout; }
    public void setDefaultTimeout(long defaultTimeout) { this.defaultTimeout = defaultTimeout; }
    
    public int getMaxRetryCount() { return maxRetryCount; }
    public void setMaxRetryCount(int maxRetryCount) { this.maxRetryCount = maxRetryCount; }
    
    public int getRetryInterval() { return retryInterval; }
    public void setRetryInterval(int retryInterval) { this.retryInterval = retryInterval; }
    
    public boolean isAsyncEnabled() { return asyncEnabled; }
    public void setAsyncEnabled(boolean asyncEnabled) { this.asyncEnabled = asyncEnabled; }
    
    public boolean isRollbackEnabled() { return rollbackEnabled; }
    public void setRollbackEnabled(boolean rollbackEnabled) { this.rollbackEnabled = rollbackEnabled; }
    
    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { 
        this.properties = properties != null ? properties : new ConcurrentHashMap<>(); 
    }
    
    public Object getProperty(String key) { return properties.get(key); }
    public void setProperty(String key, Object value) { properties.put(key, value); }
    
    public CmdClientConfig groupId(String groupId) {
        this.groupId = groupId;
        return this;
    }
    
    public CmdClientConfig domainId(String domainId) {
        this.domainId = domainId;
        return this;
    }
    
    public CmdClientConfig defaultTimeout(long timeout) {
        this.defaultTimeout = timeout;
        return this;
    }
}
