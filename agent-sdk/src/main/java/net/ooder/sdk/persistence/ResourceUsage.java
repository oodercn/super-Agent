package net.ooder.sdk.persistence;

/**
 * 资源使用情况
 */
public class ResourceUsage {
    private double cpuUsage;
    private long memoryUsage;
    private long diskUsage;
    private int networkUsage;
    private int skillCount;
    private int routeCount;
    private long lastUpdated;
    
    // 构造函数
    public ResourceUsage() {
        this.lastUpdated = System.currentTimeMillis();
    }
    
    // Getter和Setter方法
    public double getCpuUsage() {
        return cpuUsage;
    }
    
    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }
    
    public long getMemoryUsage() {
        return memoryUsage;
    }
    
    public void setMemoryUsage(long memoryUsage) {
        this.memoryUsage = memoryUsage;
    }
    
    public long getDiskUsage() {
        return diskUsage;
    }
    
    public void setDiskUsage(long diskUsage) {
        this.diskUsage = diskUsage;
    }
    
    public int getNetworkUsage() {
        return networkUsage;
    }
    
    public void setNetworkUsage(int networkUsage) {
        this.networkUsage = networkUsage;
    }
    
    public int getSkillCount() {
        return skillCount;
    }
    
    public void setSkillCount(int skillCount) {
        this.skillCount = skillCount;
    }
    
    public int getRouteCount() {
        return routeCount;
    }
    
    public void setRouteCount(int routeCount) {
        this.routeCount = routeCount;
    }
    
    public long getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    // 辅助方法
    public void updateTimestamp() {
        this.lastUpdated = System.currentTimeMillis();
    }
}
