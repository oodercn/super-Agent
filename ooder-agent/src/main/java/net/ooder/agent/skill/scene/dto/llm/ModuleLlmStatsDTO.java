package net.ooder.mvp.skill.scene.dto.llm;

import java.util.Map;

public class ModuleLlmStatsDTO {
    
    private String moduleId;
    private String moduleName;
    private String moduleType;
    private String userId;
    
    private long totalCalls;
    private long successCalls;
    private long failedCalls;
    private double successRate;
    
    private long totalTokens;
    private long totalInputTokens;
    private long totalOutputTokens;
    
    private double totalCost;
    
    private double avgLatency;
    
    private Map<String, Long> providerDistribution;
    private Map<String, Long> modelDistribution;
    
    private long statsTime;

    public String getModuleId() { return moduleId; }
    public void setModuleId(String moduleId) { this.moduleId = moduleId; }
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    public String getModuleType() { return moduleType; }
    public void setModuleType(String moduleType) { this.moduleType = moduleType; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
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
    public double getAvgLatency() { return avgLatency; }
    public void setAvgLatency(double avgLatency) { this.avgLatency = avgLatency; }
    public Map<String, Long> getProviderDistribution() { return providerDistribution; }
    public void setProviderDistribution(Map<String, Long> providerDistribution) { this.providerDistribution = providerDistribution; }
    public Map<String, Long> getModelDistribution() { return modelDistribution; }
    public void setModelDistribution(Map<String, Long> modelDistribution) { this.modelDistribution = modelDistribution; }
    public long getStatsTime() { return statsTime; }
    public void setStatsTime(long statsTime) { this.statsTime = statsTime; }
}
