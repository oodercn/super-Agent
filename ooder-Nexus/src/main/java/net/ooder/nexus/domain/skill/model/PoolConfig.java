package net.ooder.nexus.domain.skill.model;

public class PoolConfig {
    private int maxPoolSize;
    private int minIdle;
    private long connectionTimeout;
    private long idleTimeout;
    private long maxLifetime;

    public PoolConfig() {
        this.maxPoolSize = 10;
        this.minIdle = 2;
        this.connectionTimeout = 30000;
        this.idleTimeout = 600000;
        this.maxLifetime = 1800000;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public long getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(long idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public long getMaxLifetime() {
        return maxLifetime;
    }

    public void setMaxLifetime(long maxLifetime) {
        this.maxLifetime = maxLifetime;
    }
}
