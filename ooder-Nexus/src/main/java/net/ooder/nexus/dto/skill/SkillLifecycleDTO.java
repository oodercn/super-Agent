package net.ooder.nexus.dto.skill;

import java.util.Map;

/**
 * 技能生命周期控制 DTO
 */
public class SkillLifecycleDTO {
    
    private String skillId;
    private String skillName;
    private String currentStatus;
    private String targetStatus;
    private String operation;
    private boolean success;
    private String message;
    private Map<String, Object> state;
    private long lastStateChange;

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    
    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }
    
    public String getTargetStatus() { return targetStatus; }
    public void setTargetStatus(String targetStatus) { this.targetStatus = targetStatus; }
    
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Map<String, Object> getState() { return state; }
    public void setState(Map<String, Object> state) { this.state = state; }
    
    public long getLastStateChange() { return lastStateChange; }
    public void setLastStateChange(long lastStateChange) { this.lastStateChange = lastStateChange; }
}
