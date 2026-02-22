package net.ooder.nexus.skillcenter.dto.cloud;

public class EstimateCloudCostRequestDTO {
    private String provider;
    private double cpuLimit;
    private long memoryLimit;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public double getCpuLimit() {
        return cpuLimit;
    }

    public void setCpuLimit(double cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    public long getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(long memoryLimit) {
        this.memoryLimit = memoryLimit;
    }
}
