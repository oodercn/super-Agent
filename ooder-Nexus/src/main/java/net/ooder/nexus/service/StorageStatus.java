package net.ooder.nexus.service;

public class StorageStatus {
    
    private long totalSpace;
    private long usedSpace;
    private long availableSpace;
    private int fileCount;
    private String status;

    public StorageStatus() {}

    public long getTotalSpace() { return totalSpace; }
    public void setTotalSpace(long totalSpace) { this.totalSpace = totalSpace; }
    
    public long getUsedSpace() { return usedSpace; }
    public void setUsedSpace(long usedSpace) { this.usedSpace = usedSpace; }
    
    public long getAvailableSpace() { return availableSpace; }
    public void setAvailableSpace(long availableSpace) { this.availableSpace = availableSpace; }
    
    public int getFileCount() { return fileCount; }
    public void setFileCount(int fileCount) { this.fileCount = fileCount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public double getUsagePercent() {
        if (totalSpace == 0) return 0;
        return (double) usedSpace / totalSpace * 100;
    }
}
