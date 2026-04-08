package net.ooder.mvp.skill.scene.dto.llm;

import java.util.List;
import java.util.Map;

public class CompanyLlmStatsDTO {
    
    private String companyId;
    private String companyName;
    
    private long totalCalls;
    private long successCalls;
    private long failedCalls;
    private double successRate;
    
    private long totalInputTokens;
    private long totalOutputTokens;
    private long totalTokens;
    
    private double totalCost;
    private double monthToDateCost;
    private double budgetLimit;
    private double budgetUsedPercent;
    
    private double avgLatency;
    private long maxLatency;
    private long minLatency;
    
    private long todayCalls;
    private long weekCalls;
    private long monthCalls;
    
    private List<DepartmentLlmStatsDTO> topDepartments;
    
    private long statsTime;
    private long startTime;
    private long endTime;

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public long getTotalCalls() { return totalCalls; }
    public void setTotalCalls(long totalCalls) { this.totalCalls = totalCalls; }
    public long getSuccessCalls() { return successCalls; }
    public void setSuccessCalls(long successCalls) { this.successCalls = successCalls; }
    public long getFailedCalls() { return failedCalls; }
    public void setFailedCalls(long failedCalls) { this.failedCalls = failedCalls; }
    public double getSuccessRate() { return successRate; }
    public void setSuccessRate(double successRate) { this.successRate = successRate; }
    public long getTotalInputTokens() { return totalInputTokens; }
    public void setTotalInputTokens(long totalInputTokens) { this.totalInputTokens = totalInputTokens; }
    public long getTotalOutputTokens() { return totalOutputTokens; }
    public void setTotalOutputTokens(long totalOutputTokens) { this.totalOutputTokens = totalOutputTokens; }
    public long getTotalTokens() { return totalTokens; }
    public void setTotalTokens(long totalTokens) { this.totalTokens = totalTokens; }
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    public double getMonthToDateCost() { return monthToDateCost; }
    public void setMonthToDateCost(double monthToDateCost) { this.monthToDateCost = monthToDateCost; }
    public double getBudgetLimit() { return budgetLimit; }
    public void setBudgetLimit(double budgetLimit) { this.budgetLimit = budgetLimit; }
    public double getBudgetUsedPercent() { return budgetUsedPercent; }
    public void setBudgetUsedPercent(double budgetUsedPercent) { this.budgetUsedPercent = budgetUsedPercent; }
    public double getAvgLatency() { return avgLatency; }
    public void setAvgLatency(double avgLatency) { this.avgLatency = avgLatency; }
    public long getMaxLatency() { return maxLatency; }
    public void setMaxLatency(long maxLatency) { this.maxLatency = maxLatency; }
    public long getMinLatency() { return minLatency; }
    public void setMinLatency(long minLatency) { this.minLatency = minLatency; }
    public long getTodayCalls() { return todayCalls; }
    public void setTodayCalls(long todayCalls) { this.todayCalls = todayCalls; }
    public long getWeekCalls() { return weekCalls; }
    public void setWeekCalls(long weekCalls) { this.weekCalls = weekCalls; }
    public long getMonthCalls() { return monthCalls; }
    public void setMonthCalls(long monthCalls) { this.monthCalls = monthCalls; }
    public List<DepartmentLlmStatsDTO> getTopDepartments() { return topDepartments; }
    public void setTopDepartments(List<DepartmentLlmStatsDTO> topDepartments) { this.topDepartments = topDepartments; }
    public long getStatsTime() { return statsTime; }
    public void setStatsTime(long statsTime) { this.statsTime = statsTime; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
}
