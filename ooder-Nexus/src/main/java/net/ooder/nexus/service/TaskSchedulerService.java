package net.ooder.nexus.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 任务调度服务接口
 *
 * <p>SDK 0.7.2 新增接口，提供协作任务调度和执行能力。</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>任务接收与解析</li>
 *   <li>任务调度执行</li>
 *   <li>任务状态管理</li>
 *   <li>结果提交</li>
 * </ul>
 *
 * @author ooder Team
 * @version 1.0
 * @since SDK 0.7.2
 */
public interface TaskSchedulerService {

    /**
     * 接收任务
     *
     * @param taskData 任务数据
     * @return 任务信息
     */
    CompletableFuture<CollaborationTask> receiveTask(Map<String, Object> taskData);

    /**
     * 获取待执行任务列表
     *
     * @return 任务列表
     */
    CompletableFuture<List<CollaborationTask>> getPendingTasks();

    /**
     * 获取任务详情
     *
     * @param taskId 任务ID
     * @return 任务详情
     */
    CompletableFuture<CollaborationTask> getTaskDetail(String taskId);

    /**
     * 开始执行任务
     *
     * @param taskId 任务ID
     * @return 执行结果
     */
    CompletableFuture<TaskExecution> startTask(String taskId);

    /**
     * 暂停任务
     *
     * @param taskId 任务ID
     * @return 暂停结果
     */
    CompletableFuture<Void> pauseTask(String taskId);

    /**
     * 恢复任务
     *
     * @param taskId 任务ID
     * @return 恢复结果
     */
    CompletableFuture<Void> resumeTask(String taskId);

    /**
     * 取消任务
     *
     * @param taskId 任务ID
     * @param reason 取消原因
     * @return 取消结果
     */
    CompletableFuture<Void> cancelTask(String taskId, String reason);

    /**
     * 提交任务结果
     *
     * @param taskId 任务ID
     * @param result 执行结果
     * @return 提交确认
     */
    CompletableFuture<ResultSubmission> submitResult(String taskId, TaskResult result);

    /**
     * 获取任务执行状态
     *
     * @param taskId 任务ID
     * @return 执行状态
     */
    CompletableFuture<TaskExecution> getTaskExecution(String taskId);

    /**
     * 获取执行历史
     *
     * @param limit 数量限制
     * @return 执行历史列表
     */
    CompletableFuture<List<TaskExecution>> getExecutionHistory(int limit);

    /**
     * 添加任务状态监听器
     *
     * @param listener 监听器
     */
    void addTaskListener(TaskStateListener listener);

    /**
     * 移除任务状态监听器
     *
     * @param listener 监听器
     */
    void removeTaskListener(TaskStateListener listener);

    /**
     * 任务状态监听器
     */
    interface TaskStateListener {
        void onTaskReceived(CollaborationTask task);
        void onTaskStateChanged(String taskId, String oldState, String newState);
        void onTaskProgress(String taskId, int progress, String stage);
        void onTaskCompleted(String taskId, TaskResult result);
    }

    /**
     * 协作任务
     */
    class CollaborationTask {
        private String taskId;
        private String taskName;
        private String taskType;
        private String description;
        private String sourceId;
        private String sceneGroupId;
        private int priority;
        private long createTime;
        private long deadline;
        private Map<String, Object> parameters;
        private String status;

        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getTaskName() { return taskName; }
        public void setTaskName(String taskName) { this.taskName = taskName; }
        public String getTaskType() { return taskType; }
        public void setTaskType(String taskType) { this.taskType = taskType; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getSourceId() { return sourceId; }
        public void setSourceId(String sourceId) { this.sourceId = sourceId; }
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
        public long getCreateTime() { return createTime; }
        public void setCreateTime(long createTime) { this.createTime = createTime; }
        public long getDeadline() { return deadline; }
        public void setDeadline(long deadline) { this.deadline = deadline; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    /**
     * 任务执行
     */
    class TaskExecution {
        private String executionId;
        private String taskId;
        private String status;
        private int progress;
        private String currentStage;
        private long startTime;
        private long endTime;
        private String executorId;
        private String errorMessage;

        public String getExecutionId() { return executionId; }
        public void setExecutionId(String executionId) { this.executionId = executionId; }
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }
        public String getCurrentStage() { return currentStage; }
        public void setCurrentStage(String currentStage) { this.currentStage = currentStage; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
        public String getExecutorId() { return executorId; }
        public void setExecutorId(String executorId) { this.executorId = executorId; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

    /**
     * 任务结果
     */
    class TaskResult {
        private String resultId;
        private String taskId;
        private boolean success;
        private Object data;
        private String message;
        private long completionTime;
        private Map<String, Object> metrics;

        public String getResultId() { return resultId; }
        public void setResultId(String resultId) { this.resultId = resultId; }
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getCompletionTime() { return completionTime; }
        public void setCompletionTime(long completionTime) { this.completionTime = completionTime; }
        public Map<String, Object> getMetrics() { return metrics; }
        public void setMetrics(Map<String, Object> metrics) { this.metrics = metrics; }
    }

    /**
     * 结果提交
     */
    class ResultSubmission {
        private String submissionId;
        private String taskId;
        private String status;
        private long submitTime;
        private String confirmationCode;

        public String getSubmissionId() { return submissionId; }
        public void setSubmissionId(String submissionId) { this.submissionId = submissionId; }
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getSubmitTime() { return submitTime; }
        public void setSubmitTime(long submitTime) { this.submitTime = submitTime; }
        public String getConfirmationCode() { return confirmationCode; }
        public void setConfirmationCode(String confirmationCode) { this.confirmationCode = confirmationCode; }
    }
}
