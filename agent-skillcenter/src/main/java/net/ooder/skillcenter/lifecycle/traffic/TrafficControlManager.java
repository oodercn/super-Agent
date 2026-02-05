package net.ooder.skillcenter.lifecycle.traffic;

import java.util.Map;

public interface TrafficControlManager {
    
    void setRateLimit(String skillId, RateLimitConfig config);
    
    RateLimitConfig getRateLimit(String skillId);
    
    void setCircuitBreaker(String skillId, CircuitBreakerConfig config);
    
    CircuitBreakerConfig getCircuitBreaker(String skillId);
    
    void setLoadBalancing(String skillId, LoadBalancingConfig config);
    
    LoadBalancingConfig getLoadBalancing(String skillId);
    
    TrafficStats getTrafficStats(String skillId);
    
    Map<String, TrafficStats> getAllTrafficStats();
    
    boolean checkRateLimit(String skillId);
    
    boolean checkCircuitBreaker(String skillId);
    
    String selectExecutionNode(String skillId);
    
    class RateLimitConfig {
        private int maxRequestsPerSecond;
        private int maxRequestsPerMinute;
        private int maxConcurrentRequests;
        private long timeWindowMs;
        
        public int getMaxRequestsPerSecond() {
            return maxRequestsPerSecond;
        }
        
        public void setMaxRequestsPerSecond(int maxRequestsPerSecond) {
            this.maxRequestsPerSecond = maxRequestsPerSecond;
        }
        
        public int getMaxRequestsPerMinute() {
            return maxRequestsPerMinute;
        }
        
        public void setMaxRequestsPerMinute(int maxRequestsPerMinute) {
            this.maxRequestsPerMinute = maxRequestsPerMinute;
        }
        
        public int getMaxConcurrentRequests() {
            return maxConcurrentRequests;
        }
        
        public void setMaxConcurrentRequests(int maxConcurrentRequests) {
            this.maxConcurrentRequests = maxConcurrentRequests;
        }
        
        public long getTimeWindowMs() {
            return timeWindowMs;
        }
        
        public void setTimeWindowMs(long timeWindowMs) {
            this.timeWindowMs = timeWindowMs;
        }
    }
    
    class CircuitBreakerConfig {
        private int failureThreshold;
        private int successThreshold;
        private long timeoutMs;
        private long halfOpenTimeoutMs;
        private boolean enabled;
        
        public int getFailureThreshold() {
            return failureThreshold;
        }
        
        public void setFailureThreshold(int failureThreshold) {
            this.failureThreshold = failureThreshold;
        }
        
        public int getSuccessThreshold() {
            return successThreshold;
        }
        
        public void setSuccessThreshold(int successThreshold) {
            this.successThreshold = successThreshold;
        }
        
        public long getTimeoutMs() {
            return timeoutMs;
        }
        
        public void setTimeoutMs(long timeoutMs) {
            this.timeoutMs = timeoutMs;
        }
        
        public long getHalfOpenTimeoutMs() {
            return halfOpenTimeoutMs;
        }
        
        public void setHalfOpenTimeoutMs(long halfOpenTimeoutMs) {
            this.halfOpenTimeoutMs = halfOpenTimeoutMs;
        }
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
    
    class LoadBalancingConfig {
        private String strategy;
        private Map<String, Integer> nodeWeights;
        private boolean healthCheckEnabled;
        private long healthCheckIntervalMs;
        
        public String getStrategy() {
            return strategy;
        }
        
        public void setStrategy(String strategy) {
            this.strategy = strategy;
        }
        
        public Map<String, Integer> getNodeWeights() {
            return nodeWeights;
        }
        
        public void setNodeWeights(Map<String, Integer> nodeWeights) {
            this.nodeWeights = nodeWeights;
        }
        
        public boolean isHealthCheckEnabled() {
            return healthCheckEnabled;
        }
        
        public void setHealthCheckEnabled(boolean healthCheckEnabled) {
            this.healthCheckEnabled = healthCheckEnabled;
        }
        
        public long getHealthCheckIntervalMs() {
            return healthCheckIntervalMs;
        }
        
        public void setHealthCheckIntervalMs(long healthCheckIntervalMs) {
            this.healthCheckIntervalMs = healthCheckIntervalMs;
        }
    }
    
    class TrafficStats {
        private String skillId;
        private long totalRequests;
        private long successfulRequests;
        private long failedRequests;
        private long currentRequests;
        private double averageResponseTime;
        private long lastUpdated;
        
        public String getSkillId() {
            return skillId;
        }
        
        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }
        
        public long getTotalRequests() {
            return totalRequests;
        }
        
        public void setTotalRequests(long totalRequests) {
            this.totalRequests = totalRequests;
        }
        
        public long getSuccessfulRequests() {
            return successfulRequests;
        }
        
        public void setSuccessfulRequests(long successfulRequests) {
            this.successfulRequests = successfulRequests;
        }
        
        public long getFailedRequests() {
            return failedRequests;
        }
        
        public void setFailedRequests(long failedRequests) {
            this.failedRequests = failedRequests;
        }
        
        public long getCurrentRequests() {
            return currentRequests;
        }
        
        public void setCurrentRequests(long currentRequests) {
            this.currentRequests = currentRequests;
        }
        
        public double getAverageResponseTime() {
            return averageResponseTime;
        }
        
        public void setAverageResponseTime(double averageResponseTime) {
            this.averageResponseTime = averageResponseTime;
        }
        
        public long getLastUpdated() {
            return lastUpdated;
        }
        
        public void setLastUpdated(long lastUpdated) {
            this.lastUpdated = lastUpdated;
        }
    }
}
