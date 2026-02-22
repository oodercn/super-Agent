package net.ooder.nexus.domain.config.model;

/**
 * Nexus配置结果实体Bean
 * 用于ConfigController中getNexusConfig和updateNexusConfig方法的返回类型
 */
public class NexusConfigResult {
    
    private String version;
    private int servicePort;
    private int heartbeatInterval;
    private String logLevel;
    private int maxConnections;
    private int connectionTimeout;
    private int threadPoolSize;
    private int bufferSize;
    private boolean enableTls;
    private boolean enableCompression;
    private boolean enableRateLimiting;
    private int rateLimitPerSecond;
    private boolean enableCaching;
    private int cacheExpiryTime;
    private boolean enableMetrics;
    private int metricsInterval;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public boolean isEnableTls() {
        return enableTls;
    }

    public void setEnableTls(boolean enableTls) {
        this.enableTls = enableTls;
    }

    public boolean isEnableCompression() {
        return enableCompression;
    }

    public void setEnableCompression(boolean enableCompression) {
        this.enableCompression = enableCompression;
    }

    public boolean isEnableRateLimiting() {
        return enableRateLimiting;
    }

    public void setEnableRateLimiting(boolean enableRateLimiting) {
        this.enableRateLimiting = enableRateLimiting;
    }

    public int getRateLimitPerSecond() {
        return rateLimitPerSecond;
    }

    public void setRateLimitPerSecond(int rateLimitPerSecond) {
        this.rateLimitPerSecond = rateLimitPerSecond;
    }

    public boolean isEnableCaching() {
        return enableCaching;
    }

    public void setEnableCaching(boolean enableCaching) {
        this.enableCaching = enableCaching;
    }

    public int getCacheExpiryTime() {
        return cacheExpiryTime;
    }

    public void setCacheExpiryTime(int cacheExpiryTime) {
        this.cacheExpiryTime = cacheExpiryTime;
    }

    public boolean isEnableMetrics() {
        return enableMetrics;
    }

    public void setEnableMetrics(boolean enableMetrics) {
        this.enableMetrics = enableMetrics;
    }

    public int getMetricsInterval() {
        return metricsInterval;
    }

    public void setMetricsInterval(int metricsInterval) {
        this.metricsInterval = metricsInterval;
    }
}
