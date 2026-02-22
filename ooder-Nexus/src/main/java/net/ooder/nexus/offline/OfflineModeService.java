package net.ooder.nexus.offline;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Offline Mode Service Interface
 *
 * @author ooder Team
 * @since 2.0
 */
public interface OfflineModeService {

    CompletableFuture<OfflineStatus> getOfflineStatus();

    CompletableFuture<Void> enableOfflineMode();

    CompletableFuture<Void> disableOfflineMode();

    CompletableFuture<ExecutionResult> executeLocally(Map<String, Object> task);

    CompletableFuture<SyncResult> syncData();

    CompletableFuture<SyncResult> syncData(String dataType);

    CompletableFuture<List<PendingSync>> getPendingSyncs();

    CompletableFuture<Void> clearPendingSyncs();

    CompletableFuture<Map<String, Object>> getOfflineConfig();

    CompletableFuture<Void> updateOfflineConfig(Map<String, Object> config);
}

class OfflineStatus {
    private boolean offlineMode;
    private long lastSyncTime;
    private int pendingSyncCount;
    private long offlineDuration;
    private String status;

    public boolean isOfflineMode() { return offlineMode; }
    public void setOfflineMode(boolean offlineMode) { this.offlineMode = offlineMode; }
    public long getLastSyncTime() { return lastSyncTime; }
    public void setLastSyncTime(long lastSyncTime) { this.lastSyncTime = lastSyncTime; }
    public int getPendingSyncCount() { return pendingSyncCount; }
    public void setPendingSyncCount(int pendingSyncCount) { this.pendingSyncCount = pendingSyncCount; }
    public long getOfflineDuration() { return offlineDuration; }
    public void setOfflineDuration(long offlineDuration) { this.offlineDuration = offlineDuration; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

class ExecutionResult {
    private String taskId;
    private boolean success;
    private Object result;
    private String error;
    private long executionTime;

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public Object getResult() { return result; }
    public void setResult(Object result) { this.result = result; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public long getExecutionTime() { return executionTime; }
    public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
}

class SyncResult {
    private boolean success;
    private int syncedItems;
    private int failedItems;
    private long syncTime;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public int getSyncedItems() { return syncedItems; }
    public void setSyncedItems(int syncedItems) { this.syncedItems = syncedItems; }
    public int getFailedItems() { return failedItems; }
    public void setFailedItems(int failedItems) { this.failedItems = failedItems; }
    public long getSyncTime() { return syncTime; }
    public void setSyncTime(long syncTime) { this.syncTime = syncTime; }
}

class PendingSync {
    private String syncId;
    private String dataType;
    private String operation;
    private long createTime;

    public String getSyncId() { return syncId; }
    public void setSyncId(String syncId) { this.syncId = syncId; }
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
}
