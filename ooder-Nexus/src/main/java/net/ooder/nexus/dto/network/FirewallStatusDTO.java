package net.ooder.nexus.dto.network;

import java.io.Serializable;

public class FirewallStatusDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean running;
    private Integer totalRules;
    private Long blocked;
    private Long allowed;
    private Long lastUpdated;

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public Integer getTotalRules() {
        return totalRules;
    }

    public void setTotalRules(Integer totalRules) {
        this.totalRules = totalRules;
    }

    public Long getBlocked() {
        return blocked;
    }

    public void setBlocked(Long blocked) {
        this.blocked = blocked;
    }

    public Long getAllowed() {
        return allowed;
    }

    public void setAllowed(Long allowed) {
        this.allowed = allowed;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
