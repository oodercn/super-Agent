package net.ooder.nexus.model.config;

import java.io.Serializable;

public class AdvancedConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private int udpPort;
    private int heartbeatInterval;
    private int maxConnections;
    private int connectionTimeout;
    private int commandTimeout;
    private int maxCommandRetries;
    private boolean enableLogging;
    private String logLevel;
    private long lastUpdated;

    public AdvancedConfig() {
    }

    public AdvancedConfig(int udpPort, int heartbeatInterval, int maxConnections, int connectionTimeout, int commandTimeout, int maxCommandRetries, boolean enableLogging, String logLevel) {
        this.udpPort = udpPort;
        this.heartbeatInterval = heartbeatInterval;
        this.maxConnections = maxConnections;
        this.connectionTimeout = connectionTimeout;
        this.commandTimeout = commandTimeout;
        this.maxCommandRetries = maxCommandRetries;
        this.enableLogging = enableLogging;
        this.logLevel = logLevel;
        this.lastUpdated = System.currentTimeMillis();
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
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

    public int getCommandTimeout() {
        return commandTimeout;
    }

    public void setCommandTimeout(int commandTimeout) {
        this.commandTimeout = commandTimeout;
    }

    public int getMaxCommandRetries() {
        return maxCommandRetries;
    }

    public void setMaxCommandRetries(int maxCommandRetries) {
        this.maxCommandRetries = maxCommandRetries;
    }

    public boolean isEnableLogging() {
        return enableLogging;
    }

    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
