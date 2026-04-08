package net.ooder.mvp.skill.scene.dto.llm;

import java.util.List;
import java.util.Map;

public class LlmUsageStatsDTO {
    
    private String configId;
    private String configName;
    private String providerType;
    private String model;
    private long totalRequests;
    private long successRequests;
    private long failedRequests;
    private double successRate;
    private long totalTokens;
    private long inputTokens;
    private long outputTokens;
    private double totalCost;
    private double avgLatency;
    private long periodStart;
    private long periodEnd;
    private List<DailyStats> dailyStats;
    private Map<String, Object> breakdown;
    
    public static class DailyStats {
        private String date;
        private long requests;
        private long tokens;
        private double cost;
        private double avgLatency;
        
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public long getRequests() { return requests; }
        public void setRequests(long requests) { this.requests = requests; }
        public long getTokens() { return tokens; }
        public void setTokens(long tokens) { this.tokens = tokens; }
        public double getCost() { return cost; }
        public void setCost(double cost) { this.cost = cost; }
        public double getAvgLatency() { return avgLatency; }
        public void setAvgLatency(double avgLatency) { this.avgLatency = avgLatency; }
    }
    
    public String getConfigId() { return configId; }
    public void setConfigId(String configId) { this.configId = configId; }
    public String getConfigName() { return configName; }
    public void setConfigName(String configName) { this.configName = configName; }
    public String getProviderType() { return providerType; }
    public void setProviderType(String providerType) { this.providerType = providerType; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public long getTotalRequests() { return totalRequests; }
    public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
    public long getSuccessRequests() { return successRequests; }
    public void setSuccessRequests(long successRequests) { this.successRequests = successRequests; }
    public long getFailedRequests() { return failedRequests; }
    public void setFailedRequests(long failedRequests) { this.failedRequests = failedRequests; }
    public double getSuccessRate() { return successRate; }
    public void setSuccessRate(double successRate) { this.successRate = successRate; }
    public long getTotalTokens() { return totalTokens; }
    public void setTotalTokens(long totalTokens) { this.totalTokens = totalTokens; }
    public long getInputTokens() { return inputTokens; }
    public void setInputTokens(long inputTokens) { this.inputTokens = inputTokens; }
    public long getOutputTokens() { return outputTokens; }
    public void setOutputTokens(long outputTokens) { this.outputTokens = outputTokens; }
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    public double getAvgLatency() { return avgLatency; }
    public void setAvgLatency(double avgLatency) { this.avgLatency = avgLatency; }
    public long getPeriodStart() { return periodStart; }
    public void setPeriodStart(long periodStart) { this.periodStart = periodStart; }
    public long getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(long periodEnd) { this.periodEnd = periodEnd; }
    public List<DailyStats> getDailyStats() { return dailyStats; }
    public void setDailyStats(List<DailyStats> dailyStats) { this.dailyStats = dailyStats; }
    public Map<String, Object> getBreakdown() { return breakdown; }
    public void setBreakdown(Map<String, Object> breakdown) { this.breakdown = breakdown; }
}
