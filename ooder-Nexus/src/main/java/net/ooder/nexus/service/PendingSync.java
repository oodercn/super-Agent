package net.ooder.nexus.service;

/**
 * 待同步项
 */
public class PendingSync {
    
    private String syncId;
    private String resourceType;
    private String resourceId;
    private String operation;
    private long createdTime;
    private int retryCount;
    private String status;

    public PendingSync() {}

    public String getSyncId() { return syncId; }
    public void setSyncId(String syncId) { this.syncId = syncId; }
    
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    
    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
    
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
    
    public long getCreatedTime() { return createdTime; }
    public void setCreatedTime(long createdTime) { this.createdTime = createdTime; }
    
    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
