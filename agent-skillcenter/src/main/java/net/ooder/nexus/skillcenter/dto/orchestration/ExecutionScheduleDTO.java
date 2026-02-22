package net.ooder.nexus.skillcenter.dto.orchestration;

import java.util.List;

public class ExecutionScheduleDTO {
    private String scheduleId;
    private String templateId;
    private String name;
    private String cronExpression;
    private String timezone;
    private boolean enabled;
    private long createdAt;
    private long updatedAt;
    private long lastExecutionTime;
    private long nextExecutionTime;
    private int executionCount;
    private int failureCount;
    private String status;
    private List<ExecutionHistoryItem> recentHistory;

    public String getScheduleId() { return scheduleId; }
    public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }
    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCronExpression() { return cronExpression; }
    public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
    public long getLastExecutionTime() { return lastExecutionTime; }
    public void setLastExecutionTime(long lastExecutionTime) { this.lastExecutionTime = lastExecutionTime; }
    public long getNextExecutionTime() { return nextExecutionTime; }
    public void setNextExecutionTime(long nextExecutionTime) { this.nextExecutionTime = nextExecutionTime; }
    public int getExecutionCount() { return executionCount; }
    public void setExecutionCount(int executionCount) { this.executionCount = executionCount; }
    public int getFailureCount() { return failureCount; }
    public void setFailureCount(int failureCount) { this.failureCount = failureCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<ExecutionHistoryItem> getRecentHistory() { return recentHistory; }
    public void setRecentHistory(List<ExecutionHistoryItem> recentHistory) { this.recentHistory = recentHistory; }

    public static class ExecutionHistoryItem {
        private String executionId;
        private long executionTime;
        private String status;
        private long duration;

        public String getExecutionId() { return executionId; }
        public void setExecutionId(String executionId) { this.executionId = executionId; }
        public long getExecutionTime() { return executionTime; }
        public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
    }
}
