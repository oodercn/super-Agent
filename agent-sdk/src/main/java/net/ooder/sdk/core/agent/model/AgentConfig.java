
package net.ooder.sdk.core.agent.model;

import net.ooder.sdk.common.enums.AgentType;

public class AgentConfig {
    
    private String agentId;
    private String agentName;
    private AgentType agentType;
    private String endpoint;
    
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
    
    private String skillRootPath = "/skills/";
    private String vfsUrl;
    private String skillCenterUrl;
    
    private boolean strictMode = false;
    
    public AgentConfig() {
    }
    
    public static AgentConfigBuilder builder() {
        return new AgentConfigBuilder();
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
    
    public AgentType getAgentType() {
        return agentType;
    }
    
    public void setAgentType(AgentType agentType) {
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
    
    public String getRetryStrategy() {
        return "exponential";
    }
    
    public static class AgentConfigBuilder {
        private AgentConfig config = new AgentConfig();
        
        public AgentConfigBuilder agentId(String agentId) {
            config.setAgentId(agentId);
            return this;
        }
        
        public AgentConfigBuilder agentName(String agentName) {
            config.setAgentName(agentName);
            return this;
        }
        
        public AgentConfigBuilder agentType(AgentType agentType) {
            config.setAgentType(agentType);
            return this;
        }
        
        public AgentConfigBuilder endpoint(String endpoint) {
            config.setEndpoint(endpoint);
            return this;
        }
        
        public AgentConfigBuilder udpPort(int udpPort) {
            config.setUdpPort(udpPort);
            return this;
        }
        
        public AgentConfigBuilder heartbeatInterval(int interval) {
            config.setHeartbeatInterval(interval);
            return this;
        }
        
        public AgentConfigBuilder heartbeatTimeout(int timeout) {
            config.setHeartbeatTimeout(timeout);
            return this;
        }
        
        public AgentConfigBuilder strictMode(boolean strictMode) {
            config.setStrictMode(strictMode);
            return this;
        }
        
        public AgentConfig build() {
            if (config.getAgentId() == null || config.getAgentId().isEmpty()) {
                config.setAgentId(java.util.UUID.randomUUID().toString());
            }
            if (config.getAgentName() == null) {
                config.setAgentName("Agent-" + config.getAgentId().substring(0, 8));
            }
            if (config.getAgentType() == null) {
                config.setAgentType(AgentType.END);
            }
            return config;
        }
    }
}
