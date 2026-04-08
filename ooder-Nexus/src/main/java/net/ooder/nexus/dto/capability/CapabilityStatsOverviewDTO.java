package net.ooder.nexus.dto.capability;

public class CapabilityStatsOverviewDTO {
    private Long totalInvocations;
    private Long successInvocations;
    private Long failedInvocations;
    private Double avgResponseTime;
    private Integer activeCapabilities;
    private Integer installedCapabilities;
    private Integer totalCapabilities;
    private Integer systemCapabilities;

    public CapabilityStatsOverviewDTO() {}

    public Long getTotalInvocations() {
        return totalInvocations;
    }

    public void setTotalInvocations(Long totalInvocations) {
        this.totalInvocations = totalInvocations;
    }

    public Long getSuccessInvocations() {
        return successInvocations;
    }

    public void setSuccessInvocations(Long successInvocations) {
        this.successInvocations = successInvocations;
    }

    public Long getFailedInvocations() {
        return failedInvocations;
    }

    public void setFailedInvocations(Long failedInvocations) {
        this.failedInvocations = failedInvocations;
    }

    public Double getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(Double avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }

    public Integer getActiveCapabilities() {
        return activeCapabilities;
    }

    public void setActiveCapabilities(Integer activeCapabilities) {
        this.activeCapabilities = activeCapabilities;
    }

    public Integer getInstalledCapabilities() {
        return installedCapabilities;
    }

    public void setInstalledCapabilities(Integer installedCapabilities) {
        this.installedCapabilities = installedCapabilities;
    }

    public Integer getTotalCapabilities() {
        return totalCapabilities;
    }

    public void setTotalCapabilities(Integer totalCapabilities) {
        this.totalCapabilities = totalCapabilities;
    }

    public Integer getSystemCapabilities() {
        return systemCapabilities;
    }

    public void setSystemCapabilities(Integer systemCapabilities) {
        this.systemCapabilities = systemCapabilities;
    }
}
