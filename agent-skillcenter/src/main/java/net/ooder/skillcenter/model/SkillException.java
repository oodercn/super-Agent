package net.ooder.skillcenter.model;

/**
 * 技能执行异常，包含技能ID、错误码和详细消息
 */
public class SkillException extends Exception {
    /**
     * 错误码枚举
     */
    public enum ErrorCode {
        PARAMETER_ERROR(400),       // 参数错误
        SKILL_NOT_FOUND(404),       // 技能不存在
        EXECUTION_EXCEPTION(500),   // 执行异常
        SKILL_NOT_AVAILABLE(503),   // 技能不可用
        DISTRIBUTION_ERROR(504);    // 技能分发错误

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

    /**
     * 创建技能异常
     * @param skillId 技能ID
     * @param message 异常消息
     * @param errorCode 错误码
     */
    public SkillException(String skillId, String message, int errorCode) {
        super(message);
        this.skillId = skillId;
        this.errorCode = errorCode;
    }

    /**
     * 创建技能异常
     * @param skillId 技能ID
     * @param message 异常消息
     * @param errorCode 错误码
     * @param cause 根因异常
     */
    public SkillException(String skillId, String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.skillId = skillId;
        this.errorCode = errorCode;
    }

    /**
     * 创建技能异常
     * @param skillId 技能ID
     * @param message 异常消息
     * @param errorCode 错误码枚举
     */
    public SkillException(String skillId, String message, ErrorCode errorCode) {
        this(skillId, message, errorCode.getCode());
    }

    /**
     * 创建技能异常
     * @param skillId 技能ID
     * @param message 异常消息
     * @param errorCode 错误码枚举
     * @param cause 根因异常
     */
    public SkillException(String skillId, String message, ErrorCode errorCode, Throwable cause) {
        this(skillId, message, errorCode.getCode(), cause);
    }

    // Getters and setters
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
