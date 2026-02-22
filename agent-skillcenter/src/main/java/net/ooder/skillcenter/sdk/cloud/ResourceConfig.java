package net.ooder.skillcenter.sdk.cloud;

public class ResourceConfig {
    private double cpuLimit;
    private double cpuRequest;
    private long memoryLimit;
    private long memoryRequest;
    private long storageLimit;

    public double getCpuLimit() { return cpuLimit; }
    public void setCpuLimit(double cpuLimit) { this.cpuLimit = cpuLimit; }
    public double getCpuRequest() { return cpuRequest; }
    public void setCpuRequest(double cpuRequest) { this.cpuRequest = cpuRequest; }
    public long getMemoryLimit() { return memoryLimit; }
    public void setMemoryLimit(long memoryLimit) { this.memoryLimit = memoryLimit; }
    public long getMemoryRequest() { return memoryRequest; }
    public void setMemoryRequest(long memoryRequest) { this.memoryRequest = memoryRequest; }
    public long getStorageLimit() { return storageLimit; }
    public void setStorageLimit(long storageLimit) { this.storageLimit = storageLimit; }
}
