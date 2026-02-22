package net.ooder.nexus.domain.config.model;

import java.io.Serializable;

public class ServiceConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private int serviceTimeout;
    private int maxConnections;
    private int threadPoolSize;
    private int queueSize;
    private boolean enableCompression;
    private boolean enableCaching;
    private int cacheSize;
    private long lastUpdated;

    public ServiceConfig() {
    }

    public ServiceConfig(int serviceTimeout, int maxConnections, int threadPoolSize, int queueSize, boolean enableCompression, boolean enableCaching, int cacheSize) {
        this.serviceTimeout = serviceTimeout;
        this.maxConnections = maxConnections;
        this.threadPoolSize = threadPoolSize;
        this.queueSize = queueSize;
        this.enableCompression = enableCompression;
        this.enableCaching = enableCaching;
        this.cacheSize = cacheSize;
        this.lastUpdated = System.currentTimeMillis();
    }

    public ServiceConfig(int serviceTimeout, int maxConnections, int threadPoolSize, int queueSize, boolean enableCompression, boolean enableCaching, int cacheSize, long lastUpdated) {
        this.serviceTimeout = serviceTimeout;
        this.maxConnections = maxConnections;
        this.threadPoolSize = threadPoolSize;
        this.queueSize = queueSize;
        this.enableCompression = enableCompression;
        this.enableCaching = enableCaching;
        this.cacheSize = cacheSize;
        this.lastUpdated = lastUpdated;
    }

    public int getServiceTimeout() {
        return serviceTimeout;
    }

    public void setServiceTimeout(int serviceTimeout) {
        this.serviceTimeout = serviceTimeout;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public boolean isEnableCompression() {
        return enableCompression;
    }

    public void setEnableCompression(boolean enableCompression) {
        this.enableCompression = enableCompression;
    }

    public boolean isEnableCaching() {
        return enableCaching;
    }

    public void setEnableCaching(boolean enableCaching) {
        this.enableCaching = enableCaching;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
