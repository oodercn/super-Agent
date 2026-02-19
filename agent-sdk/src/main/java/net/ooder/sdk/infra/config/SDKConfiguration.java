
package net.ooder.sdk.infra.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SDKConfiguration {
    
    private String agentId;
    private String agentName;
    private String agentType;
    private String endpoint = "http://localhost:8080";
    
    private int udpPort = 8888;
    private int udpBufferSize = 65536;
    private int udpTimeout = 30000;
    private int udpMaxPacketSize = 65507;
    
    private int heartbeatInterval = 5000;
    private int heartbeatTimeout = 15000;
    private int heartbeatLossThreshold = 3;
    
    private int retryCount = 3;
    private long retryInterval = 1000;
    private double retryMultiplier = 2.0;
    private long retryMaxInterval = 30000;
    
    private int threadPoolSize = 10;
    private int queueCapacity = 10000;
    
    private String skillRootPath = "/skills/";
    private String vfsUrl;
    private String skillCenterUrl;
    
    private boolean strictMode = false;
    private boolean discoveryEnabled = true;
    
    private boolean scenePersistenceEnabled = true;
    private String sceneStoragePath = "./data/scenes";
    
    private boolean vfsSyncEnabled = false;
    private long vfsSyncInterval = 60000;
    
    private boolean dualStorageEnabled = false;
    private String dualStorageStrategy = "remote_wins";
    
    private boolean linkReplicationEnabled = false;
    private String haRole = "member";
    
    private boolean offlineSyncEnabled = true;
    private long offlineSyncRetryInterval = 30000;
    
    private int endpointStartPort = 8080;
    private int endpointEndPort = 9090;
    
    private final Map<String, Object> extraProperties = new HashMap<>();
    
    public SDKConfiguration() {
    }
    
    public static SDKConfiguration fromProperties(Properties props) {
        SDKConfiguration config = new SDKConfiguration();
        config.setAgentId(props.getProperty("agent.id"));
        config.setAgentName(props.getProperty("agent.name"));
        config.setAgentType(props.getProperty("agent.type"));
        config.setEndpoint(props.getProperty("agent.endpoint"));
        
        config.setUdpPort(getInt(props, "udp.port", config.getUdpPort()));
        config.setUdpBufferSize(getInt(props, "udp.bufferSize", config.getUdpBufferSize()));
        config.setUdpTimeout(getInt(props, "udp.timeout", config.getUdpTimeout()));
        config.setUdpMaxPacketSize(getInt(props, "udp.maxPacketSize", config.getUdpMaxPacketSize()));
        
        config.setHeartbeatInterval(getInt(props, "heartbeat.interval", config.getHeartbeatInterval()));
        config.setHeartbeatTimeout(getInt(props, "heartbeat.timeout", config.getHeartbeatTimeout()));
        config.setHeartbeatLossThreshold(getInt(props, "heartbeat.lossThreshold", config.getHeartbeatLossThreshold()));
        
        config.setRetryCount(getInt(props, "retry.count", config.getRetryCount()));
        config.setRetryInterval(getLong(props, "retry.interval", config.getRetryInterval()));
        config.setRetryMultiplier(getDouble(props, "retry.multiplier", config.getRetryMultiplier()));
        config.setRetryMaxInterval(getLong(props, "retry.maxInterval", config.getRetryMaxInterval()));
        
        config.setThreadPoolSize(getInt(props, "thread.poolSize", config.getThreadPoolSize()));
        config.setQueueCapacity(getInt(props, "queue.capacity", config.getQueueCapacity()));
        
        config.setSkillRootPath(props.getProperty("skill.rootPath", config.getSkillRootPath()));
        config.setVfsUrl(props.getProperty("vfs.url"));
        config.setSkillCenterUrl(props.getProperty("skillCenter.url"));
        
        config.setStrictMode(getBoolean(props, "strictMode", config.isStrictMode()));
        config.setDiscoveryEnabled(getBoolean(props, "discovery.enabled", config.isDiscoveryEnabled()));
        
        config.setScenePersistenceEnabled(getBoolean(props, "scene.persistence.enabled", config.isScenePersistenceEnabled()));
        config.setSceneStoragePath(props.getProperty("scene.storage.path", config.getSceneStoragePath()));
        
        config.setVfsSyncEnabled(getBoolean(props, "vfs.sync.enabled", config.isVfsSyncEnabled()));
        config.setVfsSyncInterval(getLong(props, "vfs.sync.interval", config.getVfsSyncInterval()));
        
        config.setDualStorageEnabled(getBoolean(props, "storage.dual.enabled", config.isDualStorageEnabled()));
        config.setDualStorageStrategy(props.getProperty("storage.dual.strategy", config.getDualStorageStrategy()));
        
        config.setLinkReplicationEnabled(getBoolean(props, "ha.replication.enabled", config.isLinkReplicationEnabled()));
        config.setHaRole(props.getProperty("ha.role", config.getHaRole()));
        
        config.setOfflineSyncEnabled(getBoolean(props, "offline.sync.enabled", config.isOfflineSyncEnabled()));
        config.setOfflineSyncRetryInterval(getLong(props, "offline.sync.retryInterval", config.getOfflineSyncRetryInterval()));
        
        config.setEndpointStartPort(getInt(props, "endpoint.startPort", config.getEndpointStartPort()));
        config.setEndpointEndPort(getInt(props, "endpoint.endPort", config.getEndpointEndPort()));
        
        return config;
    }
    
    private static int getInt(Properties props, String key, int defaultValue) {
        String value = props.getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    private static long getLong(Properties props, String key, long defaultValue) {
        String value = props.getProperty(key);
        if (value != null) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    private static double getDouble(Properties props, String key, double defaultValue) {
        String value = props.getProperty(key);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    private static boolean getBoolean(Properties props, String key, boolean defaultValue) {
        String value = props.getProperty(key);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }
    
    public String getAgentId() {
        return agentId;
    }
    
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
    
    public String getAgentName() {
        return agentName;
    }
    
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
    
    public String getAgentType() {
        return agentType;
    }
    
    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    public int getUdpPort() {
        return udpPort;
    }
    
    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }
    
    public int getUdpBufferSize() {
        return udpBufferSize;
    }
    
    public void setUdpBufferSize(int udpBufferSize) {
        this.udpBufferSize = udpBufferSize;
    }
    
    public int getUdpTimeout() {
        return udpTimeout;
    }
    
    public void setUdpTimeout(int udpTimeout) {
        this.udpTimeout = udpTimeout;
    }
    
    public int getUdpMaxPacketSize() {
        return udpMaxPacketSize;
    }
    
    public void setUdpMaxPacketSize(int udpMaxPacketSize) {
        this.udpMaxPacketSize = udpMaxPacketSize;
    }
    
    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }
    
    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }
    
    public int getHeartbeatTimeout() {
        return heartbeatTimeout;
    }
    
    public void setHeartbeatTimeout(int heartbeatTimeout) {
        this.heartbeatTimeout = heartbeatTimeout;
    }
    
    public int getHeartbeatLossThreshold() {
        return heartbeatLossThreshold;
    }
    
    public void setHeartbeatLossThreshold(int heartbeatLossThreshold) {
        this.heartbeatLossThreshold = heartbeatLossThreshold;
    }
    
    public int getRetryCount() {
        return retryCount;
    }
    
    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
    
    public long getRetryInterval() {
        return retryInterval;
    }
    
    public void setRetryInterval(long retryInterval) {
        this.retryInterval = retryInterval;
    }
    
    public double getRetryMultiplier() {
        return retryMultiplier;
    }
    
    public void setRetryMultiplier(double retryMultiplier) {
        this.retryMultiplier = retryMultiplier;
    }
    
    public long getRetryMaxInterval() {
        return retryMaxInterval;
    }
    
    public void setRetryMaxInterval(long retryMaxInterval) {
        this.retryMaxInterval = retryMaxInterval;
    }
    
    public int getThreadPoolSize() {
        return threadPoolSize;
    }
    
    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }
    
    public int getQueueCapacity() {
        return queueCapacity;
    }
    
    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }
    
    public String getSkillRootPath() {
        return skillRootPath;
    }
    
    public void setSkillRootPath(String skillRootPath) {
        this.skillRootPath = skillRootPath;
    }
    
    public String getVfsUrl() {
        return vfsUrl;
    }
    
    public void setVfsUrl(String vfsUrl) {
        this.vfsUrl = vfsUrl;
    }
    
    public String getSkillCenterUrl() {
        return skillCenterUrl;
    }
    
    public void setSkillCenterUrl(String skillCenterUrl) {
        this.skillCenterUrl = skillCenterUrl;
    }
    
    public boolean isStrictMode() {
        return strictMode;
    }
    
    public void setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
    }
    
    public boolean isDiscoveryEnabled() {
        return discoveryEnabled;
    }
    
    public void setDiscoveryEnabled(boolean discoveryEnabled) {
        this.discoveryEnabled = discoveryEnabled;
    }
    
    public Map<String, Object> getExtraProperties() {
        return extraProperties;
    }
    
    public void setExtraProperty(String key, Object value) {
        extraProperties.put(key, value);
    }
    
    public Object getExtraProperty(String key) {
        return extraProperties.get(key);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getExtraProperty(String key, Class<T> type) {
        Object value = extraProperties.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }
    
    public boolean isScenePersistenceEnabled() {
        return scenePersistenceEnabled;
    }
    
    public void setScenePersistenceEnabled(boolean scenePersistenceEnabled) {
        this.scenePersistenceEnabled = scenePersistenceEnabled;
    }
    
    public String getSceneStoragePath() {
        return sceneStoragePath;
    }
    
    public void setSceneStoragePath(String sceneStoragePath) {
        this.sceneStoragePath = sceneStoragePath;
    }
    
    public boolean isVfsSyncEnabled() {
        return vfsSyncEnabled;
    }
    
    public void setVfsSyncEnabled(boolean vfsSyncEnabled) {
        this.vfsSyncEnabled = vfsSyncEnabled;
    }
    
    public long getVfsSyncInterval() {
        return vfsSyncInterval;
    }
    
    public void setVfsSyncInterval(long vfsSyncInterval) {
        this.vfsSyncInterval = vfsSyncInterval;
    }
    
    public boolean isDualStorageEnabled() {
        return dualStorageEnabled;
    }
    
    public void setDualStorageEnabled(boolean dualStorageEnabled) {
        this.dualStorageEnabled = dualStorageEnabled;
    }
    
    public String getDualStorageStrategy() {
        return dualStorageStrategy;
    }
    
    public void setDualStorageStrategy(String dualStorageStrategy) {
        this.dualStorageStrategy = dualStorageStrategy;
    }
    
    public boolean isLinkReplicationEnabled() {
        return linkReplicationEnabled;
    }
    
    public void setLinkReplicationEnabled(boolean linkReplicationEnabled) {
        this.linkReplicationEnabled = linkReplicationEnabled;
    }
    
    public String getHaRole() {
        return haRole;
    }
    
    public void setHaRole(String haRole) {
        this.haRole = haRole;
    }
    
    public boolean isOfflineSyncEnabled() {
        return offlineSyncEnabled;
    }
    
    public void setOfflineSyncEnabled(boolean offlineSyncEnabled) {
        this.offlineSyncEnabled = offlineSyncEnabled;
    }
    
    public long getOfflineSyncRetryInterval() {
        return offlineSyncRetryInterval;
    }
    
    public void setOfflineSyncRetryInterval(long offlineSyncRetryInterval) {
        this.offlineSyncRetryInterval = offlineSyncRetryInterval;
    }
    
    public int getEndpointStartPort() {
        return endpointStartPort;
    }
    
    public void setEndpointStartPort(int endpointStartPort) {
        this.endpointStartPort = endpointStartPort;
    }
    
    public int getEndpointEndPort() {
        return endpointEndPort;
    }
    
    public void setEndpointEndPort(int endpointEndPort) {
        this.endpointEndPort = endpointEndPort;
    }
}
