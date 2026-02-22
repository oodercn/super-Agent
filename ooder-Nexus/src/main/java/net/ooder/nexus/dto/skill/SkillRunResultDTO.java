package net.ooder.nexus.dto.skill;

import java.io.Serializable;
import java.util.List;

/**
 * 技能运行结果DTO
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
public class SkillRunResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private boolean success;
    private String output;
    private String error;
    private List<String> logs;
    private long executionTime;
    private String status;

    public SkillRunResultDTO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
