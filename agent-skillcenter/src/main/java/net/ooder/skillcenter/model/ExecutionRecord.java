package net.ooder.skillcenter.model;

/**
 * 执行记录模型类
 */
public class ExecutionRecord {
    private String executionId;
    private String skillId;
    private String skillName;
    private String timestamp;
    private String status; // SUCCESS, FAILED, RUNNING
    private String executionTime;
    private String output;
    private String errorMessage;
    private String userId;
    
    public ExecutionRecord() {
    }
    
    public ExecutionRecord(String executionId, String skillId, String skillName, 
                          String timestamp, String status, String executionTime) {
        this.executionId = executionId;
        this.skillId = skillId;
        this.skillName = skillName;
        this.timestamp = timestamp;
        this.status = status;
        this.executionTime = executionTime;
    }
    
    // Getters and Setters
    public String getExecutionId() {
        return executionId;
    }
    
    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }
    
    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    public String getSkillName() {
        return skillName;
    }
    
    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getExecutionTime() {
        return executionTime;
    }
    
    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }
    
    public String getOutput() {
        return output;
    }
    
    public void setOutput(String output) {
        this.output = output;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    @Override
    public String toString() {
        return "ExecutionRecord{" +
                "executionId='" + executionId + '\'' +
                ", skillId='" + skillId + '\'' +
                ", skillName='" + skillName + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", status='" + status + '\'' +
                ", executionTime='" + executionTime + '\'' +
                '}';
    }
}
