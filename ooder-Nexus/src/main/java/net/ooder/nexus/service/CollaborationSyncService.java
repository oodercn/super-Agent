package net.ooder.nexus.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 协作状态同步服务接口
 *
 * <p>SDK 0.7.2 新增接口，提供协作状态同步能力。</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>状态同步管理</li>
 *   <li>冲突检测与解决</li>
 *   <li>同步历史记录</li>
 *   <li>离线状态队列</li>
 * </ul>
 *
 * @author ooder Team
 * @version 1.0
 * @since SDK 0.7.2
 */
public interface CollaborationSyncService {

    /**
     * 启动状态同步
     *
     * @param sceneGroupId 场景组ID
     * @return 启动结果
     */
    CompletableFuture<Void> startSync(String sceneGroupId);

    /**
     * 停止状态同步
     *
     * @param sceneGroupId 场景组ID
     * @return 停止结果
     */
    CompletableFuture<Void> stopSync(String sceneGroupId);

    /**
     * 检查是否正在同步
     *
     * @param sceneGroupId 场景组ID
     * @return 是否正在同步
     */
    boolean isSyncing(String sceneGroupId);

    /**
     * 同步本地状态
     *
     * @param sceneGroupId 场景组ID
     * @param localState 本地状态
     * @return 同步结果
     */
    CompletableFuture<SyncResult> syncLocalState(String sceneGroupId, Map<String, Object> localState);

    /**
     * 获取远程状态
     *
     * @param sceneGroupId 场景组ID
     * @return 远程状态
     */
    CompletableFuture<Map<String, Object>> getRemoteState(String sceneGroupId);

    /**
     * 合并状态
     *
     * @param localState 本地状态
     * @param remoteState 远程状态
     * @return 合并后的状态
     */
    CompletableFuture<MergeResult> mergeStates(Map<String, Object> localState, Map<String, Object> remoteState);

    /**
     * 解决冲突
     *
     * @param conflict 冲突信息
     * @param resolution 解决策略
     * @return 解决结果
     */
    CompletableFuture<ConflictResolution> resolveConflict(StateConflict conflict, String resolution);

    /**
     * 获取待同步队列
     *
     * @param sceneGroupId 场景组ID
     * @return 待同步项列表
     */
    CompletableFuture<List<SyncItem>> getPendingSyncItems(String sceneGroupId);

    /**
     * 立即同步所有待同步项
     *
     * @param sceneGroupId 场景组ID
     * @return 同步结果
     */
    CompletableFuture<BatchSyncResult> syncAllPending(String sceneGroupId);

    /**
     * 获取同步历史
     *
     * @param sceneGroupId 场景组ID
     * @param limit 数量限制
     * @return 同步历史列表
     */
    CompletableFuture<List<SyncHistory>> getSyncHistory(String sceneGroupId, int limit);

    /**
     * 提交任务执行结果
     *
     * @param taskId 任务ID
     * @param result 执行结果
     * @return 提交结果
     */
    CompletableFuture<TaskResultSubmission> submitTaskResult(String taskId, Map<String, Object> result);

    /**
     * 批量提交任务结果
     *
     * @param results 任务结果列表
     * @return 批量提交结果
     */
    CompletableFuture<BatchSubmissionResult> submitBatchResults(List<TaskResult> results);

    /**
     * 获取任务结果提交状态
     *
     * @param taskId 任务ID
     * @return 提交状态
     */
    CompletableFuture<SubmissionStatus> getSubmissionStatus(String taskId);

    /**
     * 获取待提交结果队列
     *
     * @param sceneGroupId 场景组ID
     * @return 待提交结果列表
     */
    CompletableFuture<List<PendingResult>> getPendingResults(String sceneGroupId);

    /**
     * 重试失败的结果提交
     *
     * @param taskId 任务ID
     * @return 重试结果
     */
    CompletableFuture<TaskResultSubmission> retrySubmission(String taskId);

    /**
     * 添加同步状态监听器
     *
     * @param listener 监听器
     */
    void addSyncStateListener(SyncStateListener listener);

    /**
     * 移除同步状态监听器
     *
     * @param listener 监听器
     */
    void removeSyncStateListener(SyncStateListener listener);

    /**
     * 同步状态监听器
     */
    interface SyncStateListener {
        void onSyncStarted(String sceneGroupId);
        void onSyncProgress(String sceneGroupId, int progress, String stage);
        void onSyncCompleted(String sceneGroupId, SyncResult result);
        void onSyncFailed(String sceneGroupId, String error);
        void onConflictDetected(String sceneGroupId, StateConflict conflict);
    }

    /**
     * 同步结果
     */
    class SyncResult {
        private boolean success;
        private String message;
        private long syncTime;
        private int itemsSynced;
        private int conflictsResolved;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getSyncTime() { return syncTime; }
        public void setSyncTime(long syncTime) { this.syncTime = syncTime; }
        public int getItemsSynced() { return itemsSynced; }
        public void setItemsSynced(int itemsSynced) { this.itemsSynced = itemsSynced; }
        public int getConflictsResolved() { return conflictsResolved; }
        public void setConflictsResolved(int conflictsResolved) { this.conflictsResolved = conflictsResolved; }
    }

    /**
     * 合并结果
     */
    class MergeResult {
        private boolean success;
        private Map<String, Object> mergedState;
        private List<StateConflict> conflicts;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public Map<String, Object> getMergedState() { return mergedState; }
        public void setMergedState(Map<String, Object> mergedState) { this.mergedState = mergedState; }
        public List<StateConflict> getConflicts() { return conflicts; }
        public void setConflicts(List<StateConflict> conflicts) { this.conflicts = conflicts; }
    }

    /**
     * 状态冲突
     */
    class StateConflict {
        private String conflictId;
        private String key;
        private Object localValue;
        private Object remoteValue;
        private long localTime;
        private long remoteTime;
        private String conflictType;

        public String getConflictId() { return conflictId; }
        public void setConflictId(String conflictId) { this.conflictId = conflictId; }
        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public Object getLocalValue() { return localValue; }
        public void setLocalValue(Object localValue) { this.localValue = localValue; }
        public Object getRemoteValue() { return remoteValue; }
        public void setRemoteValue(Object remoteValue) { this.remoteValue = remoteValue; }
        public long getLocalTime() { return localTime; }
        public void setLocalTime(long localTime) { this.localTime = localTime; }
        public long getRemoteTime() { return remoteTime; }
        public void setRemoteTime(long remoteTime) { this.remoteTime = remoteTime; }
        public String getConflictType() { return conflictType; }
        public void setConflictType(String conflictType) { this.conflictType = conflictType; }
    }

    /**
     * 冲突解决
     */
    class ConflictResolution {
        private String conflictId;
        private String resolution;
        private Object resolvedValue;
        private long resolveTime;

        public String getConflictId() { return conflictId; }
        public void setConflictId(String conflictId) { this.conflictId = conflictId; }
        public String getResolution() { return resolution; }
        public void setResolution(String resolution) { this.resolution = resolution; }
        public Object getResolvedValue() { return resolvedValue; }
        public void setResolvedValue(Object resolvedValue) { this.resolvedValue = resolvedValue; }
        public long getResolveTime() { return resolveTime; }
        public void setResolveTime(long resolveTime) { this.resolveTime = resolveTime; }
    }

    /**
     * 同步项
     */
    class SyncItem {
        private String itemId;
        private String sceneGroupId;
        private String itemType;
        private String key;
        private Object value;
        private long createTime;
        private int retryCount;
        private String status;

        public String getItemId() { return itemId; }
        public void setItemId(String itemId) { this.itemId = itemId; }
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public String getItemType() { return itemType; }
        public void setItemType(String itemType) { this.itemType = itemType; }
        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public Object getValue() { return value; }
        public void setValue(Object value) { this.value = value; }
        public long getCreateTime() { return createTime; }
        public void setCreateTime(long createTime) { this.createTime = createTime; }
        public int getRetryCount() { return retryCount; }
        public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    /**
     * 批量同步结果
     */
    class BatchSyncResult {
        private int totalItems;
        private int successCount;
        private int failureCount;
        private List<String> failedItems;
        private long totalTime;

        public int getTotalItems() { return totalItems; }
        public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
        public int getSuccessCount() { return successCount; }
        public void setSuccessCount(int successCount) { this.successCount = successCount; }
        public int getFailureCount() { return failureCount; }
        public void setFailureCount(int failureCount) { this.failureCount = failureCount; }
        public List<String> getFailedItems() { return failedItems; }
        public void setFailedItems(List<String> failedItems) { this.failedItems = failedItems; }
        public long getTotalTime() { return totalTime; }
        public void setTotalTime(long totalTime) { this.totalTime = totalTime; }
    }

    /**
     * 同步历史
     */
    class SyncHistory {
        private String historyId;
        private String sceneGroupId;
        private String syncType;
        private int itemCount;
        private long syncTime;
        private String status;

        public String getHistoryId() { return historyId; }
        public void setHistoryId(String historyId) { this.historyId = historyId; }
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public String getSyncType() { return syncType; }
        public void setSyncType(String syncType) { this.syncType = syncType; }
        public int getItemCount() { return itemCount; }
        public void setItemCount(int itemCount) { this.itemCount = itemCount; }
        public long getSyncTime() { return syncTime; }
        public void setSyncTime(long syncTime) { this.syncTime = syncTime; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    /**
     * 任务结果
     */
    class TaskResult {
        private String taskId;
        private String sceneGroupId;
        private boolean success;
        private Map<String, Object> result;
        private String errorMessage;
        private long executionTime;
        private long completionTime;

        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public Map<String, Object> getResult() { return result; }
        public void setResult(Map<String, Object> result) { this.result = result; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public long getExecutionTime() { return executionTime; }
        public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
        public long getCompletionTime() { return completionTime; }
        public void setCompletionTime(long completionTime) { this.completionTime = completionTime; }
    }

    /**
     * 任务结果提交
     */
    class TaskResultSubmission {
        private String submissionId;
        private String taskId;
        private boolean success;
        private String message;
        private long submitTime;
        private int retryCount;

        public String getSubmissionId() { return submissionId; }
        public void setSubmissionId(String submissionId) { this.submissionId = submissionId; }
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getSubmitTime() { return submitTime; }
        public void setSubmitTime(long submitTime) { this.submitTime = submitTime; }
        public int getRetryCount() { return retryCount; }
        public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    }

    /**
     * 批量提交结果
     */
    class BatchSubmissionResult {
        private int totalTasks;
        private int successCount;
        private int failureCount;
        private List<String> failedTaskIds;
        private long totalTime;

        public int getTotalTasks() { return totalTasks; }
        public void setTotalTasks(int totalTasks) { this.totalTasks = totalTasks; }
        public int getSuccessCount() { return successCount; }
        public void setSuccessCount(int successCount) { this.successCount = successCount; }
        public int getFailureCount() { return failureCount; }
        public void setFailureCount(int failureCount) { this.failureCount = failureCount; }
        public List<String> getFailedTaskIds() { return failedTaskIds; }
        public void setFailedTaskIds(List<String> failedTaskIds) { this.failedTaskIds = failedTaskIds; }
        public long getTotalTime() { return totalTime; }
        public void setTotalTime(long totalTime) { this.totalTime = totalTime; }
    }

    /**
     * 提交状态
     */
    class SubmissionStatus {
        private String taskId;
        private String status;
        private int retryCount;
        private long lastAttemptTime;
        private String lastError;

        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getRetryCount() { return retryCount; }
        public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
        public long getLastAttemptTime() { return lastAttemptTime; }
        public void setLastAttemptTime(long lastAttemptTime) { this.lastAttemptTime = lastAttemptTime; }
        public String getLastError() { return lastError; }
        public void setLastError(String lastError) { this.lastError = lastError; }
    }

    /**
     * 待提交结果
     */
    class PendingResult {
        private String resultId;
        private String taskId;
        private String sceneGroupId;
        private Map<String, Object> result;
        private long createTime;
        private int retryCount;
        private String status;

        public String getResultId() { return resultId; }
        public void setResultId(String resultId) { this.resultId = resultId; }
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public Map<String, Object> getResult() { return result; }
        public void setResult(Map<String, Object> result) { this.result = result; }
        public long getCreateTime() { return createTime; }
        public void setCreateTime(long createTime) { this.createTime = createTime; }
        public int getRetryCount() { return retryCount; }
        public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
