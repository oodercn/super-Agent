package net.ooder.nexus.skillcenter.dto.skill;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class ExecutionResultDTO extends BaseDTO {

    private String executionId;
    private String status;
    private String output;
    private long executionTime;

    public ExecutionResultDTO() {}

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
}
