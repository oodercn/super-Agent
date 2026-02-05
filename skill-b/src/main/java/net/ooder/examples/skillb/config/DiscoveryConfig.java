package net.ooder.examples.skillb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "discovery")
public class DiscoveryConfig {
    private boolean enabled;
    private int broadcastPort;
    private int scanInterval;
    private boolean joinOnDiscovery;
    private String sceneName;

    // Getters and setters
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getBroadcastPort() {
        return broadcastPort;
    }

    public void setBroadcastPort(int broadcastPort) {
        this.broadcastPort = broadcastPort;
    }

    public int getScanInterval() {
        return scanInterval;
    }

    public void setScanInterval(int scanInterval) {
        this.scanInterval = scanInterval;
    }

    public boolean isJoinOnDiscovery() {
        return joinOnDiscovery;
    }

    public void setJoinOnDiscovery(boolean joinOnDiscovery) {
        this.joinOnDiscovery = joinOnDiscovery;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }
}
