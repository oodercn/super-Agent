package net.ooder.skill.config.dto;

import java.util.List;

public class DbPoolConfigDTO {
    
    private int maxConnections;
    private int minIdle;
    private int connectionTimeout;
    private int idleTimeout;
    private List<String> connectionUrls;

    public int getMaxConnections() { return maxConnections; }
    public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }
    public int getMinIdle() { return minIdle; }
    public void setMinIdle(int minIdle) { this.minIdle = minIdle; }
    public int getConnectionTimeout() { return connectionTimeout; }
    public void setConnectionTimeout(int connectionTimeout) { this.connectionTimeout = connectionTimeout; }
    public int getIdleTimeout() { return idleTimeout; }
    public void setIdleTimeout(int idleTimeout) { this.idleTimeout = idleTimeout; }
    public List<String> getConnectionUrls() { return connectionUrls; }
    public void setConnectionUrls(List<String> connectionUrls) { this.connectionUrls = connectionUrls; }
}
