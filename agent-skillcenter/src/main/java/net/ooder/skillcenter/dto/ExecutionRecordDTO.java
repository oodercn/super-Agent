package net.ooder.skillcenter.dto;

import java.util.Date;

/**
 * 执行记录数据传输对象
 */
public class ExecutionRecordDTO {

    private String id;
    private String skillId;
    private String skillName;
    private String status;
    private Object input;
    private Object output;
    private String error;
    private long executionTime;
    private Date executedAt;
    private String executedBy;

    public ExecutionRecordDTO() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Object getInput() { return input; }
    public void setInput(Object input) { this.input = input; }

    public Object getOutput() { return output; }
    public void setOutput(Object output) { this.output = output; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public long getExecutionTime() { return executionTime; }
    public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }

    public Date getExecutedAt() { return executedAt; }
    public void setExecutedAt(Date executedAt) { this.executedAt = executedAt; }

    public String getExecutedBy() { return executedBy; }
    public void setExecutedBy(String executedBy) { this.executedBy = executedBy; }
}
