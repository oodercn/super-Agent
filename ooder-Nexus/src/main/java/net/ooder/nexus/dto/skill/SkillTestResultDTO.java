package net.ooder.nexus.dto.skill;

import java.io.Serializable;

public class SkillTestResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean success;
    private String skillId;
    private String message;
    private String error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
