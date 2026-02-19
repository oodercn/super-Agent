
package net.ooder.sdk.infra.exception;

public class SkillException extends SDKException {
    
    private static final long serialVersionUID = 1L;
    
    private final String skillId;
    
    public SkillException(String skillId, String message) {
        super("SKILL_ERROR", message);
        this.skillId = skillId;
    }
    
    public SkillException(String skillId, String errorCode, String message) {
        super(errorCode, message);
        this.skillId = skillId;
    }
    
    public SkillException(String skillId, String message, Throwable cause) {
        super("SKILL_ERROR", message, cause);
        this.skillId = skillId;
    }
    
    public SkillException(String skillId, String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
        this.skillId = skillId;
    }
    
    public String getSkillId() {
        return skillId;
    }
}
