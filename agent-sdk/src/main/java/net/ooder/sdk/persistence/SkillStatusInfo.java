package net.ooder.sdk.persistence;

import net.ooder.sdk.skill.SkillStatus;

/**
 * 技能状态信息，包含技能ID和状态
 */
public class SkillStatusInfo {
    private String skillId;
    private String skillName;
    private SkillStatus status;
    private long lastUpdated;
    private String errorMessage;
    
    // 构造函数
    public SkillStatusInfo() {
    }
    
    public SkillStatusInfo(String skillId, SkillStatus status) {
        this.skillId = skillId;
        this.status = status;
        this.lastUpdated = System.currentTimeMillis();
    }
    
    // Getter和Setter方法
    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    public String getSkillName() {
        return skillName;
    }
    
    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
    
    public SkillStatus getStatus() {
        return status;
    }
    
    public void setStatus(SkillStatus status) {
        this.status = status;
        this.lastUpdated = System.currentTimeMillis();
    }
    
    public long getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
