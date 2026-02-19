package net.ooder.sdk.api.scheduler;

/**
 * Task Information
 *
 * @author ooder Team
 * @since 0.7.1
 */
public class TaskInfo {

    private String taskId;
    private String name;
    private TaskStatus status;
    private long createdAt;
    private long scheduledAt;
    private long executedAt;
    private long completedAt;
    private String cronExpression;
    private long periodMs;
    private long delayMs;
    private boolean recurring;
    private String error;
    private int executionCount;

    public TaskInfo() {
        this.status = TaskStatus.PENDING;
        this.createdAt = System.currentTimeMillis();
        this.executionCount = 0;
    }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(long scheduledAt) { this.scheduledAt = scheduledAt; }
    
    public long getScheduledTime() { return scheduledAt; }
    public void setScheduledTime(long scheduledTime) { this.scheduledAt = scheduledTime; }

    public long getExecutedAt() { return executedAt; }
    public void setExecutedAt(long executedAt) { this.executedAt = executedAt; }

    public long getCompletedAt() { return completedAt; }
    public void setCompletedAt(long completedAt) { this.completedAt = completedAt; }

    public String getCronExpression() { return cronExpression; }
    public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }

    public long getPeriodMs() { return periodMs; }
    public void setPeriodMs(long periodMs) { this.periodMs = periodMs; }

    public long getDelayMs() { return delayMs; }
    public void setDelayMs(long delayMs) { this.delayMs = delayMs; }

    public boolean isRecurring() { return recurring; }
    public void setRecurring(boolean recurring) { this.recurring = recurring; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public int getExecutionCount() { return executionCount; }
    public void setExecutionCount(int executionCount) { this.executionCount = executionCount; }

    public void incrementExecutionCount() {
        this.executionCount++;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "taskId='" + taskId + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", recurring=" + recurring +
                ", executionCount=" + executionCount +
                '}';
    }
}
