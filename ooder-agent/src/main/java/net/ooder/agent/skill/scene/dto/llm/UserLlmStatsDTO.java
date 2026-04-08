package net.ooder.mvp.skill.scene.dto.llm;

import java.util.List;

public class UserLlmStatsDTO {
    
    private String userId;
    private String userName;
    private String departmentId;
    private String departmentName;
    
    private long totalCalls;
    private long successCalls;
    private long failedCalls;
    private double successRate;
    
    private long totalTokens;
    private long totalInputTokens;
    private long totalOutputTokens;
    
    private double totalCost;
    private double quotaLimit;
    private double quotaUsed;
    
    private List<ModuleLlmStatsDTO> moduleStats;
    
    private long statsTime;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public long getTotalCalls() { return totalCalls; }
    public void setTotalCalls(long totalCalls) { this.totalCalls = totalCalls; }
    public long getSuccessCalls() { return successCalls; }
    public void setSuccessCalls(long successCalls) { this.successCalls = successCalls; }
    public long getFailedCalls() { return failedCalls; }
    public void setFailedCalls(long failedCalls) { this.failedCalls = failedCalls; }
    public double getSuccessRate() { return successRate; }
    public void setSuccessRate(double successRate) { this.successRate = successRate; }
    public long getTotalTokens() { return totalTokens; }
    public void setTotalTokens(long totalTokens) { this.totalTokens = totalTokens; }
    public long getTotalInputTokens() { return totalInputTokens; }
    public void setTotalInputTokens(long totalInputTokens) { this.totalInputTokens = totalInputTokens; }
    public long getTotalOutputTokens() { return totalOutputTokens; }
    public void setTotalOutputTokens(long totalOutputTokens) { this.totalOutputTokens = totalOutputTokens; }
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    public double getQuotaLimit() { return quotaLimit; }
    public void setQuotaLimit(double quotaLimit) { this.quotaLimit = quotaLimit; }
    public double getQuotaUsed() { return quotaUsed; }
    public void setQuotaUsed(double quotaUsed) { this.quotaUsed = quotaUsed; }
    public List<ModuleLlmStatsDTO> getModuleStats() { return moduleStats; }
    public void setModuleStats(List<ModuleLlmStatsDTO> moduleStats) { this.moduleStats = moduleStats; }
    public long getStatsTime() { return statsTime; }
    public void setStatsTime(long statsTime) { this.statsTime = statsTime; }
}
