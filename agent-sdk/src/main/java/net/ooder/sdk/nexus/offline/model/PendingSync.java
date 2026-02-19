package net.ooder.sdk.nexus.offline.model;

public class PendingSync {
    
    private String syncId;
    private SyncType type;
    private String resourceKey;
    private String resourceName;
    private long createdTime;
    private int retryCount;
    private SyncPriority priority;
    private String status;
    
    public String getSyncId() { return syncId; }
    public void setSyncId(String syncId) { this.syncId = syncId; }
    
    public SyncType getType() { return type; }
    public void setType(SyncType type) { this.type = type; }
    
    public String getResourceKey() { return resourceKey; }
    public void setResourceKey(String resourceKey) { this.resourceKey = resourceKey; }
    
    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }
    
    public long getCreatedTime() { return createdTime; }
    public void setCreatedTime(long createdTime) { this.createdTime = createdTime; }
    
    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    
    public SyncPriority getPriority() { return priority; }
    public void setPriority(SyncPriority priority) { this.priority = priority; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public enum SyncType {
        DATA_UPLOAD,
        DATA_DOWNLOAD,
        STATE_SYNC,
        CONFIG_SYNC,
        SKILL_SYNC
    }
    
    public enum SyncPriority {
        LOW,
        NORMAL,
        HIGH,
        CRITICAL
    }
}
