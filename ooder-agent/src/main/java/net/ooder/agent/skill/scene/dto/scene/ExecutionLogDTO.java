package net.ooder.mvp.skill.scene.dto.scene;

public class ExecutionLogDTO {
    private String logId;
    private String workflowId;
    private String stepId;
    private String action;
    private String status;
    private String message;
    private long timestamp;

    public String getLogId() { return logId; }
    public void setLogId(String logId) { this.logId = logId; }
    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public String getStepId() { return stepId; }
    public void setStepId(String stepId) { this.stepId = stepId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
