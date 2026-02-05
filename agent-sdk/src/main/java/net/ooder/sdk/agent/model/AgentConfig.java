package net.ooder.sdk.agent.model;

import net.ooder.sdk.config.AgentConfigProperties;
import net.ooder.sdk.system.retry.RetryStrategy;
import net.ooder.sdk.system.sleep.DefaultSleepStrategy;
import net.ooder.sdk.system.sleep.SleepStrategy;

public class AgentConfig {
    private String agentId;
    private String agentName;
    private String agentType;
    private String endpoint;

    private int udpPort;
    private int udpBufferSize;
    private int udpTimeout;
    private int udpMaxPacketSize;

    private long heartbeatInterval;
    private long heartbeatTimeout;
    private int heartbeatLossThreshold;

    private RetryStrategy retryStrategy;
    private SleepStrategy sleepStrategy;

    public AgentConfig() {
        this.udpPort = 9001;
        this.udpBufferSize = 65535;
        this.udpTimeout = 5000;
        this.udpMaxPacketSize = 65507;

        this.heartbeatInterval = 30000;
        this.heartbeatTimeout = 90000;
        this.heartbeatLossThreshold = 3;

        this.retryStrategy = RetryStrategy.builder()
                .type(RetryStrategy.RetryType.EXPONENTIAL_BACKOFF)
                .maxRetries(5)
                .initialInterval(1000)
                .maxInterval(30000)
                .backoffFactor(2.0)
                .build();

        this.sleepStrategy = new DefaultSleepStrategy();
    }

    public AgentConfig(AgentConfigProperties properties) {
        if (properties != null) {
            this.udpPort = properties.getUdpPort();
            this.udpBufferSize = properties.getUdpBufferSize();
            this.udpTimeout = properties.getUdpTimeout();
            this.udpMaxPacketSize = properties.getUdpMaxPacketSize();

            this.heartbeatInterval = properties.getHeartbeatInterval();
            this.heartbeatTimeout = properties.getHeartbeatTimeout();
            this.heartbeatLossThreshold = properties.getHeartbeatLossThreshold();

            this.retryStrategy = RetryStrategy.builder()
                    .type(RetryStrategy.RetryType.EXPONENTIAL_BACKOFF)
                    .maxRetries(properties.getRetryMaxRetries())
                    .initialInterval(properties.getRetryInitialInterval())
                    .maxInterval(properties.getRetryMaxInterval())
                    .backoffFactor(properties.getRetryBackoffFactor())
                    .build();

            this.sleepStrategy = new DefaultSleepStrategy();
        } else {
            this.udpPort = 9001;
            this.udpBufferSize = 65535;
            this.udpTimeout = 5000;
            this.udpMaxPacketSize = 65507;

            this.heartbeatInterval = 30000;
            this.heartbeatTimeout = 90000;
            this.heartbeatLossThreshold = 3;

            this.retryStrategy = RetryStrategy.builder()
                    .type(RetryStrategy.RetryType.EXPONENTIAL_BACKOFF)
                    .maxRetries(5)
                    .initialInterval(1000)
                    .maxInterval(30000)
                    .backoffFactor(2.0)
                    .build();

            this.sleepStrategy = new DefaultSleepStrategy();
        }
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

    public long getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(long heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public long getHeartbeatTimeout() {
        return heartbeatTimeout;
    }

    public void setHeartbeatTimeout(long heartbeatTimeout) {
        this.heartbeatTimeout = heartbeatTimeout;
    }

    public int getHeartbeatLossThreshold() {
        return heartbeatLossThreshold;
    }

    public void setHeartbeatLossThreshold(int heartbeatLossThreshold) {
        this.heartbeatLossThreshold = heartbeatLossThreshold;
    }

    public RetryStrategy getRetryStrategy() {
        return retryStrategy;
    }

    public void setRetryStrategy(RetryStrategy retryStrategy) {
        this.retryStrategy = retryStrategy;
    }

    public SleepStrategy getSleepStrategy() {
        return sleepStrategy;
    }

    public void setSleepStrategy(SleepStrategy sleepStrategy) {
        this.sleepStrategy = sleepStrategy;
    }

    public static AgentConfigBuilder builder() {
        return new AgentConfigBuilder();
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

        public AgentConfigBuilder agentType(String agentType) {
            config.setAgentType(agentType);
            return this;
        }

        public AgentConfigBuilder endpoint(String endpoint) {
            config.setEndpoint(endpoint);
            return this;
        }

        public AgentConfigBuilder udpPort(int port) {
            config.setUdpPort(port);
            return this;
        }

        public AgentConfigBuilder heartbeatInterval(long interval) {
            config.setHeartbeatInterval(interval);
            return this;
        }

        public AgentConfigBuilder retryStrategy(RetryStrategy strategy) {
            config.setRetryStrategy(strategy);
            return this;
        }

        public AgentConfigBuilder sleepStrategy(SleepStrategy strategy) {
            config.setSleepStrategy(strategy);
            return this;
        }

        public AgentConfig build() {
            return config;
        }
    }
}
