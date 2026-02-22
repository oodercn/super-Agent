package net.ooder.nexus.skillcenter.dto.security;

public class SecurityStatsDTO {

    private int totalEvents;
    private int criticalEvents;
    private int warningEvents;
    private int infoEvents;
    private int blockedAttempts;
    private int allowedRequests;
    private double avgResponseTime;
    private long totalBytesScanned;
    private int activeThreats;
    private int resolvedThreats;
    private Long timestamp;

    public SecurityStatsDTO() {}

    public int getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(int totalEvents) {
        this.totalEvents = totalEvents;
    }

    public int getCriticalEvents() {
        return criticalEvents;
    }

    public void setCriticalEvents(int criticalEvents) {
        this.criticalEvents = criticalEvents;
    }

    public int getWarningEvents() {
        return warningEvents;
    }

    public void setWarningEvents(int warningEvents) {
        this.warningEvents = warningEvents;
    }

    public int getInfoEvents() {
        return infoEvents;
    }

    public void setInfoEvents(int infoEvents) {
        this.infoEvents = infoEvents;
    }

    public int getBlockedAttempts() {
        return blockedAttempts;
    }

    public void setBlockedAttempts(int blockedAttempts) {
        this.blockedAttempts = blockedAttempts;
    }

    public int getAllowedRequests() {
        return allowedRequests;
    }

    public void setAllowedRequests(int allowedRequests) {
        this.allowedRequests = allowedRequests;
    }

    public double getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(double avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }

    public long getTotalBytesScanned() {
        return totalBytesScanned;
    }

    public void setTotalBytesScanned(long totalBytesScanned) {
        this.totalBytesScanned = totalBytesScanned;
    }

    public int getActiveThreats() {
        return activeThreats;
    }

    public void setActiveThreats(int activeThreats) {
        this.activeThreats = activeThreats;
    }

    public int getResolvedThreats() {
        return resolvedThreats;
    }

    public void setResolvedThreats(int resolvedThreats) {
        this.resolvedThreats = resolvedThreats;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
