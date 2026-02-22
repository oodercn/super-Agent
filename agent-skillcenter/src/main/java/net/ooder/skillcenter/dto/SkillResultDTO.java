package net.ooder.skillcenter.dto;

import java.util.Date;

/**
 * 技能执行结果数据传输对象
 */
public class SkillResultDTO {

    private String executionId;
    private String skillId;
    private String skillName;
    private String status;
    private Object output;
    private String error;
    private long executionTime;
    private Date executedAt;

    public SkillResultDTO() {}

    // Getters and Setters
    public String getExecutionId() { return executionId; }
    public void setExecutionId(String executionId) { this.executionId = executionId; }

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Object getOutput() { return output; }
    public void setOutput(Object output) { this.output = output; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public long getExecutionTime() { return executionTime; }
    public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }

    public Date getExecutedAt() { return executedAt; }
    public void setExecutedAt(Date executedAt) { this.executedAt = executedAt; }
}
