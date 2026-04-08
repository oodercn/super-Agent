package net.ooder.mvp.skill.scene.dto.llm;

import java.util.Map;

public class OverallStatsDTO {
    
    private Long totalCalls;
    private Long successCalls;
    private Long failedCalls;
    private Long totalTokens;
    private Double totalCost;
    private Long avgLatency;
    private Double successRate;
    private Map<String, Long> providerDistribution;
    
    public OverallStatsDTO() {
    }
    
    public Long getTotalCalls() { return totalCalls; }
    public void setTotalCalls(Long totalCalls) { this.totalCalls = totalCalls; }
    
    public Long getSuccessCalls() { return successCalls; }
    public void setSuccessCalls(Long successCalls) { this.successCalls = successCalls; }
    
    public Long getFailedCalls() { return failedCalls; }
    public void setFailedCalls(Long failedCalls) { this.failedCalls = failedCalls; }
    
    public Long getTotalTokens() { return totalTokens; }
    public void setTotalTokens(Long totalTokens) { this.totalTokens = totalTokens; }
    
    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }
    
    public Long getAvgLatency() { return avgLatency; }
    public void setAvgLatency(Long avgLatency) { this.avgLatency = avgLatency; }
    
    public Double getSuccessRate() { return successRate; }
    public void setSuccessRate(Double successRate) { this.successRate = successRate; }
    
    public Map<String, Long> getProviderDistribution() { return providerDistribution; }
    public void setProviderDistribution(Map<String, Long> providerDistribution) { this.providerDistribution = providerDistribution; }
}
