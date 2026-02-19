package net.ooder.sdk.nexus.offline.model;

import java.util.List;

public class SyncResult {
    
    private String syncId;
    private boolean success;
    private int syncedCount;
    private int failedCount;
    private List<String> syncedItems;
    private List<String> failedItems;
    private String errorMessage;
    private long startTime;
    private long endTime;
    
    public String getSyncId() { return syncId; }
    public void setSyncId(String syncId) { this.syncId = syncId; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public int getSyncedCount() { return syncedCount; }
    public void setSyncedCount(int syncedCount) { this.syncedCount = syncedCount; }
    
    public int getFailedCount() { return failedCount; }
    public void setFailedCount(int failedCount) { this.failedCount = failedCount; }
    
    public List<String> getSyncedItems() { return syncedItems; }
    public void setSyncedItems(List<String> syncedItems) { this.syncedItems = syncedItems; }
    
    public List<String> getFailedItems() { return failedItems; }
    public void setFailedItems(List<String> failedItems) { this.failedItems = failedItems; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
}
