package net.ooder.skillcenter.resources.model;

/**
 * 资源限制 - 符合v0.7.0协议规范
 */
public class ResourceLimit {
    
    private String cpuLimit;
    private String memoryLimit;
    private String storageLimit;
    private int maxReplicas;
    private long timeoutSeconds;
    
    public ResourceLimit() {
        this.maxReplicas = 1;
        this.timeoutSeconds = 300;
    }
    
    public static ResourceLimit of(String cpu, String memory, String storage) {
        ResourceLimit limit = new ResourceLimit();
        limit.setCpuLimit(cpu);
        limit.setMemoryLimit(memory);
        limit.setStorageLimit(storage);
        return limit;
    }
    
    public static ResourceLimit defaultLimit() {
        return of("2000m", "2Gi", "10Gi");
    }
    
    public boolean isWithinLimits(ResourceRequest request) {
        if (request == null) return false;
        
        if (request.getCpuMillis() > parseCpu(cpuLimit)) {
            return false;
        }
        if (request.getMemoryBytes() > parseSize(memoryLimit)) {
            return false;
        }
        if (request.getStorageBytes() > parseSize(storageLimit)) {
            return false;
        }
        return true;
    }
    
    private long parseCpu(String cpu) {
        if (cpu == null) return Long.MAX_VALUE;
        if (cpu.endsWith("m")) {
            return Long.parseLong(cpu.substring(0, cpu.length() - 1));
        }
        return Long.parseLong(cpu) * 1000;
    }
    
    private long parseSize(String size) {
        if (size == null) return Long.MAX_VALUE;
        size = size.toUpperCase();
        try {
            if (size.endsWith("GI")) {
                return Long.parseLong(size.substring(0, size.length() - 2)) * 1024 * 1024 * 1024;
            } else if (size.endsWith("MI")) {
                return Long.parseLong(size.substring(0, size.length() - 2)) * 1024 * 1024;
            } else if (size.endsWith("KI")) {
                return Long.parseLong(size.substring(0, size.length() - 2)) * 1024;
            }
            return Long.parseLong(size);
        } catch (NumberFormatException e) {
            return Long.MAX_VALUE;
        }
    }
    
    public String getCpuLimit() { return cpuLimit; }
    public void setCpuLimit(String cpuLimit) { this.cpuLimit = cpuLimit; }
    
    public String getMemoryLimit() { return memoryLimit; }
    public void setMemoryLimit(String memoryLimit) { this.memoryLimit = memoryLimit; }
    
    public String getStorageLimit() { return storageLimit; }
    public void setStorageLimit(String storageLimit) { this.storageLimit = storageLimit; }
    
    public int getMaxReplicas() { return maxReplicas; }
    public void setMaxReplicas(int maxReplicas) { this.maxReplicas = maxReplicas; }
    
    public long getTimeoutSeconds() { return timeoutSeconds; }
    public void setTimeoutSeconds(long timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
}
