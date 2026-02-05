package net.ooder.sdk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "agent.config")
public class AgentConfigProperties {
    
    private int udpPort = 9001;
    private int udpBufferSize = 65535;
    private int udpTimeout = 5000;
    private int udpMaxPacketSize = 65507;
    
    private long heartbeatInterval = 30000;
    private long heartbeatTimeout = 90000;
    private int heartbeatLossThreshold = 3;
    
    private int retryMaxRetries = 5;
    private int retryInitialInterval = 1000;
    private int retryMaxInterval = 30000;
    private double retryBackoffFactor = 2.0;
    
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
    
    public int getRetryMaxRetries() {
        return retryMaxRetries;
    }
    
    public void setRetryMaxRetries(int retryMaxRetries) {
        this.retryMaxRetries = retryMaxRetries;
    }
    
    public int getRetryInitialInterval() {
        return retryInitialInterval;
    }
    
    public void setRetryInitialInterval(int retryInitialInterval) {
        this.retryInitialInterval = retryInitialInterval;
    }
    
    public int getRetryMaxInterval() {
        return retryMaxInterval;
    }
    
    public void setRetryMaxInterval(int retryMaxInterval) {
        this.retryMaxInterval = retryMaxInterval;
    }
    
    public double getRetryBackoffFactor() {
        return retryBackoffFactor;
    }
    
    public void setRetryBackoffFactor(double retryBackoffFactor) {
        this.retryBackoffFactor = retryBackoffFactor;
    }
}
