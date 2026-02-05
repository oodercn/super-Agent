package net.ooder.skillcenter.ide;

import java.util.Map;

/**
 * 技能开发上下文，用于跟踪技能开发过程中的状态
 */
public class SkillDevelopmentContext {
    private String contextId;
    private String skillId;
    private long startTime;
    private long lastModified;
    private String status;
    private Map<String, Object> developmentData;
    
    /**
     * 获取上下文ID
     * @return 上下文ID
     */
    public String getContextId() {
        return contextId;
    }
    
    /**
     * 设置上下文ID
     * @param contextId 上下文ID
     */
    public void setContextId(String contextId) {
        this.contextId = contextId;
    }
    
    /**
     * 获取技能ID
     * @return 技能ID
     */
    public String getSkillId() {
        return skillId;
    }
    
    /**
     * 设置技能ID
     * @param skillId 技能ID
     */
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    /**
     * 获取开始时间
     * @return 开始时间
     */
    public long getStartTime() {
        return startTime;
    }
    
    /**
     * 设置开始时间
     * @param startTime 开始时间
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    /**
     * 获取最后修改时间
     * @return 最后修改时间
     */
    public long getLastModified() {
        return lastModified;
    }
    
    /**
     * 设置最后修改时间
     * @param lastModified 最后修改时间
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
    
    /**
     * 获取状态
     * @return 状态
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * 设置状态
     * @param status 状态
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * 获取开发数据
     * @return 开发数据
     */
    public Map<String, Object> getDevelopmentData() {
        return developmentData;
    }
    
    /**
     * 设置开发数据
     * @param developmentData 开发数据
     */
    public void setDevelopmentData(Map<String, Object> developmentData) {
        this.developmentData = developmentData;
    }
}