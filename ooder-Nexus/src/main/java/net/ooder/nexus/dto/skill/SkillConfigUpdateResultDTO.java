package net.ooder.nexus.dto.skill;

import java.io.Serializable;

public class SkillConfigUpdateResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String skillId;
    private String status;
    private ConnectionTestResultDTO connectionTest;

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ConnectionTestResultDTO getConnectionTest() {
        return connectionTest;
    }

    public void setConnectionTest(ConnectionTestResultDTO connectionTest) {
        this.connectionTest = connectionTest;
    }

    public static class ConnectionTestResultDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private Boolean success;
        private String message;
        private Long responseTime;

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
    }
}
