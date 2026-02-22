package net.ooder.nexus.dto.config;

import java.io.Serializable;

/**
 * End config update request DTO
 */
public class EndConfigUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer endAgentDiscoveryInterval;
    private Integer endAgentHeartbeatInterval;
    private Integer endAgentTimeout;
    private Integer endAgentMaxRetries;
    private Boolean enableEndAgentCaching;
    private Integer endAgentCacheExpiry;
    private Boolean enableEndAgentHealthCheck;
    private Integer endAgentHealthCheckInterval;
    private Boolean enableEndAgentMetrics;
    private Integer endAgentMetricsInterval;
    private Boolean enableEndAgentAutoDiscovery;
    private Boolean enableEndAgentAutoRegistration;
    private Integer endAgentMaxConcurrentTasks;
    private Integer endAgentQueueSize;
    private Integer endAgentCommandTimeout;
    private Integer endAgentMaxConnections;
    private Boolean enableEndAgentCompression;
    private Boolean enableEndAgentEncryption;

    public Integer getEndAgentDiscoveryInterval() { return endAgentDiscoveryInterval; }
    public void setEndAgentDiscoveryInterval(Integer endAgentDiscoveryInterval) { this.endAgentDiscoveryInterval = endAgentDiscoveryInterval; }
    public Integer getEndAgentHeartbeatInterval() { return endAgentHeartbeatInterval; }
    public void setEndAgentHeartbeatInterval(Integer endAgentHeartbeatInterval) { this.endAgentHeartbeatInterval = endAgentHeartbeatInterval; }
    public Integer getEndAgentTimeout() { return endAgentTimeout; }
    public void setEndAgentTimeout(Integer endAgentTimeout) { this.endAgentTimeout = endAgentTimeout; }
    public Integer getEndAgentMaxRetries() { return endAgentMaxRetries; }
    public void setEndAgentMaxRetries(Integer endAgentMaxRetries) { this.endAgentMaxRetries = endAgentMaxRetries; }
    public Boolean getEnableEndAgentCaching() { return enableEndAgentCaching; }
    public void setEnableEndAgentCaching(Boolean enableEndAgentCaching) { this.enableEndAgentCaching = enableEndAgentCaching; }
    public Integer getEndAgentCacheExpiry() { return endAgentCacheExpiry; }
    public void setEndAgentCacheExpiry(Integer endAgentCacheExpiry) { this.endAgentCacheExpiry = endAgentCacheExpiry; }
    public Boolean getEnableEndAgentHealthCheck() { return enableEndAgentHealthCheck; }
    public void setEnableEndAgentHealthCheck(Boolean enableEndAgentHealthCheck) { this.enableEndAgentHealthCheck = enableEndAgentHealthCheck; }
    public Integer getEndAgentHealthCheckInterval() { return endAgentHealthCheckInterval; }
    public void setEndAgentHealthCheckInterval(Integer endAgentHealthCheckInterval) { this.endAgentHealthCheckInterval = endAgentHealthCheckInterval; }
    public Boolean getEnableEndAgentMetrics() { return enableEndAgentMetrics; }
    public void setEnableEndAgentMetrics(Boolean enableEndAgentMetrics) { this.enableEndAgentMetrics = enableEndAgentMetrics; }
    public Integer getEndAgentMetricsInterval() { return endAgentMetricsInterval; }
    public void setEndAgentMetricsInterval(Integer endAgentMetricsInterval) { this.endAgentMetricsInterval = endAgentMetricsInterval; }
    public Boolean getEnableEndAgentAutoDiscovery() { return enableEndAgentAutoDiscovery; }
    public void setEnableEndAgentAutoDiscovery(Boolean enableEndAgentAutoDiscovery) { this.enableEndAgentAutoDiscovery = enableEndAgentAutoDiscovery; }
    public Boolean getEnableEndAgentAutoRegistration() { return enableEndAgentAutoRegistration; }
    public void setEnableEndAgentAutoRegistration(Boolean enableEndAgentAutoRegistration) { this.enableEndAgentAutoRegistration = enableEndAgentAutoRegistration; }
    public Integer getEndAgentMaxConcurrentTasks() { return endAgentMaxConcurrentTasks; }
    public void setEndAgentMaxConcurrentTasks(Integer endAgentMaxConcurrentTasks) { this.endAgentMaxConcurrentTasks = endAgentMaxConcurrentTasks; }
    public Integer getEndAgentQueueSize() { return endAgentQueueSize; }
    public void setEndAgentQueueSize(Integer endAgentQueueSize) { this.endAgentQueueSize = endAgentQueueSize; }
    public Integer getEndAgentCommandTimeout() { return endAgentCommandTimeout; }
    public void setEndAgentCommandTimeout(Integer endAgentCommandTimeout) { this.endAgentCommandTimeout = endAgentCommandTimeout; }
    public Integer getEndAgentMaxConnections() { return endAgentMaxConnections; }
    public void setEndAgentMaxConnections(Integer endAgentMaxConnections) { this.endAgentMaxConnections = endAgentMaxConnections; }
    public Boolean getEnableEndAgentCompression() { return enableEndAgentCompression; }
    public void setEnableEndAgentCompression(Boolean enableEndAgentCompression) { this.enableEndAgentCompression = enableEndAgentCompression; }
    public Boolean getEnableEndAgentEncryption() { return enableEndAgentEncryption; }
    public void setEnableEndAgentEncryption(Boolean enableEndAgentEncryption) { this.enableEndAgentEncryption = enableEndAgentEncryption; }
}
