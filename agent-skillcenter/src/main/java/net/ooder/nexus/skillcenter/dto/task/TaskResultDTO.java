package net.ooder.nexus.skillcenter.dto.task;

import java.util.Map;

public class TaskResultDTO {
    
    private String resultId;
    private String taskId;
    private String groupId;
    private String executorId;
    private boolean success;
    private Object output;
    private String errorMessage;
    private long startTime;
    private long endTime;
    private long duration;
    private Map<String, Object> metrics;

    public String getResultId() { return resultId; }
    public void setResultId(String resultId) { this.resultId = resultId; }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getExecutorId() { return executorId; }
    public void setExecutorId(String executorId) { this.executorId = executorId; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public Object getOutput() { return output; }
    public void setOutput(Object output) { this.output = output; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }

    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }

    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }

    public Map<String, Object> getMetrics() { return metrics; }
    public void setMetrics(Map<String, Object> metrics) { this.metrics = metrics; }
}
