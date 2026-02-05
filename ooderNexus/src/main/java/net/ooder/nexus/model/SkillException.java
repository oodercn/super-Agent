package net.ooder.nexus.model;

public class SkillException extends Exception {
    public enum ErrorCode {
        PARAMETER_ERROR(400),
        SKILL_NOT_FOUND(404),
        EXECUTION_EXCEPTION(500),
        SKILL_NOT_AVAILABLE(503),
        DISTRIBUTION_ERROR(504);

        private final int code;

        ErrorCode(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    private String skillId;
    private int errorCode;

    public SkillException(String skillId, String message, int errorCode) {
        super(message);
        this.skillId = skillId;
        this.errorCode = errorCode;
    }

    public SkillException(String skillId, String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.skillId = skillId;
        this.errorCode = errorCode;
    }

    public SkillException(String skillId, String message, ErrorCode errorCode) {
        this(skillId, message, errorCode.getCode());
    }

    public SkillException(String skillId, String message, ErrorCode errorCode, Throwable cause) {
        this(skillId, message, errorCode.getCode(), cause);
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
