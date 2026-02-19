package net.ooder.sdk.api.msg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MsgClientConfig {
    
    private String clientId;
    private String sceneId;
    private String groupId;
    private String personId;
    private String msgClass;
    
    private boolean cacheEnabled = true;
    private long cacheMaxSize = 10485760;
    private long cacheExpireTime = 86400000;
    
    private int connectionTimeout = 5000;
    private int retryCount = 3;
    private int retryInterval = 1000;
    
    private Map<String, Object> properties = new ConcurrentHashMap<>();
    
    public MsgClientConfig() {
    }
    
    public MsgClientConfig(String clientId, String sceneId) {
        this.clientId = clientId;
        this.sceneId = sceneId;
    }
    
    public static MsgClientConfig create(String clientId, String sceneId) {
        return new MsgClientConfig(clientId, sceneId);
    }
    
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    
    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    
    public String getPersonId() { return personId; }
    public void setPersonId(String personId) { this.personId = personId; }
    
    public String getMsgClass() { return msgClass; }
    public void setMsgClass(String msgClass) { this.msgClass = msgClass; }
    
    public boolean isCacheEnabled() { return cacheEnabled; }
    public void setCacheEnabled(boolean cacheEnabled) { this.cacheEnabled = cacheEnabled; }
    
    public long getCacheMaxSize() { return cacheMaxSize; }
    public void setCacheMaxSize(long cacheMaxSize) { this.cacheMaxSize = cacheMaxSize; }
    
    public long getCacheExpireTime() { return cacheExpireTime; }
    public void setCacheExpireTime(long cacheExpireTime) { this.cacheExpireTime = cacheExpireTime; }
    
    public int getConnectionTimeout() { return connectionTimeout; }
    public void setConnectionTimeout(int connectionTimeout) { this.connectionTimeout = connectionTimeout; }
    
    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    
    public int getRetryInterval() { return retryInterval; }
    public void setRetryInterval(int retryInterval) { this.retryInterval = retryInterval; }
    
    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { 
        this.properties = properties != null ? properties : new ConcurrentHashMap<>(); 
    }
    
    public Object getProperty(String key) { return properties.get(key); }
    public void setProperty(String key, Object value) { properties.put(key, value); }
    
    public MsgClientConfig groupId(String groupId) {
        this.groupId = groupId;
        return this;
    }
    
    public MsgClientConfig personId(String personId) {
        this.personId = personId;
        return this;
    }
    
    public MsgClientConfig cacheEnabled(boolean enabled) {
        this.cacheEnabled = enabled;
        return this;
    }
}
