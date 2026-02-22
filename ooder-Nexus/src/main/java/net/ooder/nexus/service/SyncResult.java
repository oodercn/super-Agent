package net.ooder.nexus.service;

/**
 * 同步结果
 */
public class SyncResult {
    
    private int totalItems;
    private int successCount;
    private int failureCount;
    private long syncTime;
    private String message;

    public SyncResult() {}

    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
    
    public int getSuccessCount() { return successCount; }
    public void setSuccessCount(int successCount) { this.successCount = successCount; }
    
    public int getFailureCount() { return failureCount; }
    public void setFailureCount(int failureCount) { this.failureCount = failureCount; }
    
    public long getSyncTime() { return syncTime; }
    public void setSyncTime(long syncTime) { this.syncTime = syncTime; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public boolean isComplete() {
        return failureCount == 0;
    }
}
