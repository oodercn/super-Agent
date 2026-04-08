package net.ooder.mvp.skill.scene.dto.stats;

public class CapabilityStatsDTO {
    
    private int totalCapabilities;
    private int activeCapabilities;
    private int totalInvocations;
    private int successInvocations;
    private int failedInvocations;
    private double avgResponseTime;
    private long lastInvokeTime;
    private int installedCapabilities;

    public CapabilityStatsDTO() {
    }

    public int getTotalCapabilities() {
        return totalCapabilities;
    }

    public void setTotalCapabilities(int totalCapabilities) {
        this.totalCapabilities = totalCapabilities;
    }

    public int getActiveCapabilities() {
        return activeCapabilities;
    }

    public void setActiveCapabilities(int activeCapabilities) {
        this.activeCapabilities = activeCapabilities;
    }

    public int getTotalInvocations() {
        return totalInvocations;
    }

    public void setTotalInvocations(int totalInvocations) {
        this.totalInvocations = totalInvocations;
    }

    public int getSuccessInvocations() {
        return successInvocations;
    }

    public void setSuccessInvocations(int successInvocations) {
        this.successInvocations = successInvocations;
    }

    public int getFailedInvocations() {
        return failedInvocations;
    }

    public void setFailedInvocations(int failedInvocations) {
        this.failedInvocations = failedInvocations;
    }

    public double getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(double avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }

    public long getLastInvokeTime() {
        return lastInvokeTime;
    }

    public void setLastInvokeTime(long lastInvokeTime) {
        this.lastInvokeTime = lastInvokeTime;
    }

    public int getInstalledCapabilities() {
        return installedCapabilities;
    }

    public void setInstalledCapabilities(int installedCapabilities) {
        this.installedCapabilities = installedCapabilities;
    }
}
