package net.ooder.nexus.dto.config;

import java.io.Serializable;

/**
 * Nexus config update request DTO
 */
public class NexusConfigUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String version;
    private Integer servicePort;
    private Integer heartbeatInterval;
    private String logLevel;
    private Integer maxConnections;
    private Integer connectionTimeout;
    private Integer threadPoolSize;
    private Integer bufferSize;
    private Boolean enableTls;
    private Boolean enableCompression;
    private Boolean enableRateLimiting;
    private Integer rateLimitPerSecond;
    private Boolean enableCaching;
    private Integer cacheExpiryTime;
    private Boolean enableMetrics;
    private Integer metricsInterval;

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public Integer getServicePort() { return servicePort; }
    public void setServicePort(Integer servicePort) { this.servicePort = servicePort; }
    public Integer getHeartbeatInterval() { return heartbeatInterval; }
    public void setHeartbeatInterval(Integer heartbeatInterval) { this.heartbeatInterval = heartbeatInterval; }
    public String getLogLevel() { return logLevel; }
    public void setLogLevel(String logLevel) { this.logLevel = logLevel; }
    public Integer getMaxConnections() { return maxConnections; }
    public void setMaxConnections(Integer maxConnections) { this.maxConnections = maxConnections; }
    public Integer getConnectionTimeout() { return connectionTimeout; }
    public void setConnectionTimeout(Integer connectionTimeout) { this.connectionTimeout = connectionTimeout; }
    public Integer getThreadPoolSize() { return threadPoolSize; }
    public void setThreadPoolSize(Integer threadPoolSize) { this.threadPoolSize = threadPoolSize; }
    public Integer getBufferSize() { return bufferSize; }
    public void setBufferSize(Integer bufferSize) { this.bufferSize = bufferSize; }
    public Boolean getEnableTls() { return enableTls; }
    public void setEnableTls(Boolean enableTls) { this.enableTls = enableTls; }
    public Boolean getEnableCompression() { return enableCompression; }
    public void setEnableCompression(Boolean enableCompression) { this.enableCompression = enableCompression; }
    public Boolean getEnableRateLimiting() { return enableRateLimiting; }
    public void setEnableRateLimiting(Boolean enableRateLimiting) { this.enableRateLimiting = enableRateLimiting; }
    public Integer getRateLimitPerSecond() { return rateLimitPerSecond; }
    public void setRateLimitPerSecond(Integer rateLimitPerSecond) { this.rateLimitPerSecond = rateLimitPerSecond; }
    public Boolean getEnableCaching() { return enableCaching; }
    public void setEnableCaching(Boolean enableCaching) { this.enableCaching = enableCaching; }
    public Integer getCacheExpiryTime() { return cacheExpiryTime; }
    public void setCacheExpiryTime(Integer cacheExpiryTime) { this.cacheExpiryTime = cacheExpiryTime; }
    public Boolean getEnableMetrics() { return enableMetrics; }
    public void setEnableMetrics(Boolean enableMetrics) { this.enableMetrics = enableMetrics; }
    public Integer getMetricsInterval() { return metricsInterval; }
    public void setMetricsInterval(Integer metricsInterval) { this.metricsInterval = metricsInterval; }
}
