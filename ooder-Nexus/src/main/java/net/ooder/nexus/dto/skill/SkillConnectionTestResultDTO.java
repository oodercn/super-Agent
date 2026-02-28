package net.ooder.nexus.dto.skill;

import java.io.Serializable;

public class SkillConnectionTestResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean success;
    private String message;
    private Long responseTime;
    private String error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
