package net.ooder.nexus.domain.config.model;

import java.io.Serializable;

/**
 * End configuration result
 * Used for return type in ConfigController getEndConfig and updateEndConfig methods
 */
public class EndConfigResult implements Serializable {
    private int endAgentDiscoveryInterval;
    private int endAgentHeartbeatInterval;
    private int endAgentTimeout;
    private int endAgentMaxRetries;
    private boolean enableEndAgentCaching;
    private int endAgentCacheExpiry;
    private boolean enableEndAgentHealthCheck;
    private int endAgentHealthCheckInterval;
    private boolean enableEndAgentMetrics;
    private int endAgentMetricsInterval;
    private boolean enableEndAgentAutoDiscovery;
    private boolean enableEndAgentAutoRegistration;
    private int endAgentMaxConcurrentTasks;
    private int endAgentQueueSize;
    private int endAgentCommandTimeout;
    private int endAgentMaxConnections;
    private boolean enableEndAgentCompression;
    private boolean enableEndAgentEncryption;

    public int getEndAgentDiscoveryInterval() {
        return endAgentDiscoveryInterval;
    }

    public void setEndAgentDiscoveryInterval(int endAgentDiscoveryInterval) {
        this.endAgentDiscoveryInterval = endAgentDiscoveryInterval;
    }

    public int getEndAgentHeartbeatInterval() {
        return endAgentHeartbeatInterval;
    }

    public void setEndAgentHeartbeatInterval(int endAgentHeartbeatInterval) {
        this.endAgentHeartbeatInterval = endAgentHeartbeatInterval;
    }

    public int getEndAgentTimeout() {
        return endAgentTimeout;
    }

    public void setEndAgentTimeout(int endAgentTimeout) {
        this.endAgentTimeout = endAgentTimeout;
    }

    public int getEndAgentMaxRetries() {
        return endAgentMaxRetries;
    }

    public void setEndAgentMaxRetries(int endAgentMaxRetries) {
        this.endAgentMaxRetries = endAgentMaxRetries;
    }

    public boolean isEnableEndAgentCaching() {
        return enableEndAgentCaching;
    }

    public void setEnableEndAgentCaching(boolean enableEndAgentCaching) {
        this.enableEndAgentCaching = enableEndAgentCaching;
    }

    public int getEndAgentCacheExpiry() {
        return endAgentCacheExpiry;
    }

    public void setEndAgentCacheExpiry(int endAgentCacheExpiry) {
        this.endAgentCacheExpiry = endAgentCacheExpiry;
    }

    public boolean isEnableEndAgentHealthCheck() {
        return enableEndAgentHealthCheck;
    }

    public void setEnableEndAgentHealthCheck(boolean enableEndAgentHealthCheck) {
        this.enableEndAgentHealthCheck = enableEndAgentHealthCheck;
    }

    public int getEndAgentHealthCheckInterval() {
        return endAgentHealthCheckInterval;
    }

    public void setEndAgentHealthCheckInterval(int endAgentHealthCheckInterval) {
        this.endAgentHealthCheckInterval = endAgentHealthCheckInterval;
    }

    public boolean isEnableEndAgentMetrics() {
        return enableEndAgentMetrics;
    }

    public void setEnableEndAgentMetrics(boolean enableEndAgentMetrics) {
        this.enableEndAgentMetrics = enableEndAgentMetrics;
    }

    public int getEndAgentMetricsInterval() {
        return endAgentMetricsInterval;
    }

    public void setEndAgentMetricsInterval(int endAgentMetricsInterval) {
        this.endAgentMetricsInterval = endAgentMetricsInterval;
    }

    public boolean isEnableEndAgentAutoDiscovery() {
        return enableEndAgentAutoDiscovery;
    }

    public void setEnableEndAgentAutoDiscovery(boolean enableEndAgentAutoDiscovery) {
        this.enableEndAgentAutoDiscovery = enableEndAgentAutoDiscovery;
    }

    public boolean isEnableEndAgentAutoRegistration() {
        return enableEndAgentAutoRegistration;
    }

    public void setEnableEndAgentAutoRegistration(boolean enableEndAgentAutoRegistration) {
        this.enableEndAgentAutoRegistration = enableEndAgentAutoRegistration;
    }

    public int getEndAgentMaxConcurrentTasks() {
        return endAgentMaxConcurrentTasks;
    }

    public void setEndAgentMaxConcurrentTasks(int endAgentMaxConcurrentTasks) {
        this.endAgentMaxConcurrentTasks = endAgentMaxConcurrentTasks;
    }

    public int getEndAgentQueueSize() {
        return endAgentQueueSize;
    }

    public void setEndAgentQueueSize(int endAgentQueueSize) {
        this.endAgentQueueSize = endAgentQueueSize;
    }

    public int getEndAgentCommandTimeout() {
        return endAgentCommandTimeout;
    }

    public void setEndAgentCommandTimeout(int endAgentCommandTimeout) {
        this.endAgentCommandTimeout = endAgentCommandTimeout;
    }

    public int getEndAgentMaxConnections() {
        return endAgentMaxConnections;
    }

    public void setEndAgentMaxConnections(int endAgentMaxConnections) {
        this.endAgentMaxConnections = endAgentMaxConnections;
    }

    public boolean isEnableEndAgentCompression() {
        return enableEndAgentCompression;
    }

    public void setEnableEndAgentCompression(boolean enableEndAgentCompression) {
        this.enableEndAgentCompression = enableEndAgentCompression;
    }

    public boolean isEnableEndAgentEncryption() {
        return enableEndAgentEncryption;
    }

    public void setEnableEndAgentEncryption(boolean enableEndAgentEncryption) {
        this.enableEndAgentEncryption = enableEndAgentEncryption;
    }
}
