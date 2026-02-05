package net.ooder.sdk.persistence;

/**
 * 技能统计信息
 */
public class SkillStats {
    private int totalSkills;
    private int activeSkills;
    private int inactiveSkills;
    private int errorSkills;
    private long totalExecutions;
    private long successfulExecutions;
    private long failedExecutions;
    private double averageExecutionTime;
    private long lastUpdated;
    
    // 构造函数
    public SkillStats() {
        this.lastUpdated = System.currentTimeMillis();
    }
    
    // Getter和Setter方法
    public int getTotalSkills() {
        return totalSkills;
    }
    
    public void setTotalSkills(int totalSkills) {
        this.totalSkills = totalSkills;
    }
    
    public int getActiveSkills() {
        return activeSkills;
    }
    
    public void setActiveSkills(int activeSkills) {
        this.activeSkills = activeSkills;
    }
    
    public int getInactiveSkills() {
        return inactiveSkills;
    }
    
    public void setInactiveSkills(int inactiveSkills) {
        this.inactiveSkills = inactiveSkills;
    }
    
    public int getErrorSkills() {
        return errorSkills;
    }
    
    public void setErrorSkills(int errorSkills) {
        this.errorSkills = errorSkills;
    }
    
    public long getTotalExecutions() {
        return totalExecutions;
    }
    
    public void setTotalExecutions(long totalExecutions) {
        this.totalExecutions = totalExecutions;
    }
    
    public long getSuccessfulExecutions() {
        return successfulExecutions;
    }
    
    public void setSuccessfulExecutions(long successfulExecutions) {
        this.successfulExecutions = successfulExecutions;
    }
    
    public long getFailedExecutions() {
        return failedExecutions;
    }
    
    public void setFailedExecutions(long failedExecutions) {
        this.failedExecutions = failedExecutions;
    }
    
    public double getAverageExecutionTime() {
        return averageExecutionTime;
    }
    
    public void setAverageExecutionTime(double averageExecutionTime) {
        this.averageExecutionTime = averageExecutionTime;
    }
    
    public long getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    // 辅助方法
    public void updateTimestamp() {
        this.lastUpdated = System.currentTimeMillis();
    }
}
