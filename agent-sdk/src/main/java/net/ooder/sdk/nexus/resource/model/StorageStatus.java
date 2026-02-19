package net.ooder.sdk.nexus.resource.model;

public class StorageStatus {
    
    private String storageType;
    private boolean available;
    private long totalSize;
    private long usedSize;
    private long freeSize;
    private int fileCount;
    private long lastCheckTime;
    
    public String getStorageType() { return storageType; }
    public void setStorageType(String storageType) { this.storageType = storageType; }
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public long getTotalSize() { return totalSize; }
    public void setTotalSize(long totalSize) { this.totalSize = totalSize; }
    
    public long getUsedSize() { return usedSize; }
    public void setUsedSize(long usedSize) { this.usedSize = usedSize; }
    
    public long getFreeSize() { return freeSize; }
    public void setFreeSize(long freeSize) { this.freeSize = freeSize; }
    
    public int getFileCount() { return fileCount; }
    public void setFileCount(int fileCount) { this.fileCount = fileCount; }
    
    public long getLastCheckTime() { return lastCheckTime; }
    public void setLastCheckTime(long lastCheckTime) { this.lastCheckTime = lastCheckTime; }
}
