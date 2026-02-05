package net.ooder.skillcenter.lifecycle.traffic.impl;

import net.ooder.skillcenter.lifecycle.traffic.TrafficControlManager;
import java.util.*;

public class TrafficControlManagerImpl implements TrafficControlManager {
    
    private static TrafficControlManagerImpl instance;
    private Map<String, RateLimitConfig> rateLimits;
    private Map<String, CircuitBreakerConfig> circuitBreakers;
    private Map<String, TrafficStats> trafficStats;
    
    public TrafficControlManagerImpl() {
        this.rateLimits = new HashMap<>();
        this.circuitBreakers = new HashMap<>();
        this.trafficStats = new HashMap<>();
        loadSampleData();
    }
    
    public static TrafficControlManager getInstance() {
        if (instance == null) {
            instance = new TrafficControlManagerImpl();
        }
        return instance;
    }
    
    @Override
    public void setRateLimit(String skillId, RateLimitConfig config) {
        rateLimits.put(skillId, config);
    }
    
    @Override
    public RateLimitConfig getRateLimit(String skillId) {
        return rateLimits.get(skillId);
    }
    
    @Override
    public void setCircuitBreaker(String skillId, CircuitBreakerConfig config) {
        circuitBreakers.put(skillId, config);
    }
    
    @Override
    public CircuitBreakerConfig getCircuitBreaker(String skillId) {
        return circuitBreakers.get(skillId);
    }
    
    @Override
    public void setLoadBalancing(String skillId, LoadBalancingConfig config) {
    }
    
    @Override
    public LoadBalancingConfig getLoadBalancing(String skillId) {
        return null;
    }
    
    @Override
    public TrafficStats getTrafficStats(String skillId) {
        return trafficStats.get(skillId);
    }
    
    @Override
    public Map<String, TrafficStats> getAllTrafficStats() {
        return new HashMap<>(trafficStats);
    }
    
    @Override
    public boolean checkRateLimit(String skillId) {
        RateLimitConfig config = rateLimits.get(skillId);
        if (config == null) {
            return true;
        }
        
        TrafficStats stats = trafficStats.get(skillId);
        if (stats == null) {
            return true;
        }
        
        return stats.getCurrentRequests() < config.getMaxConcurrentRequests();
    }
    
    @Override
    public boolean checkCircuitBreaker(String skillId) {
        CircuitBreakerConfig config = circuitBreakers.get(skillId);
        if (config == null || !config.isEnabled()) {
            return true;
        }
        
        TrafficStats stats = trafficStats.get(skillId);
        if (stats == null) {
            return true;
        }
        
        long totalRequests = stats.getTotalRequests();
        long failedRequests = stats.getFailedRequests();
        
        if (totalRequests == 0) {
            return true;
        }
        
        double failureRate = (double) failedRequests / totalRequests;
        return failureRate < ((double) config.getFailureThreshold() / 100);
    }
    
    @Override
    public String selectExecutionNode(String skillId) {
        return "default-node";
    }
    
    private void loadSampleData() {
        RateLimitConfig rateLimit = new RateLimitConfig();
        rateLimit.setMaxRequestsPerSecond(10);
        rateLimit.setMaxRequestsPerMinute(100);
        rateLimit.setMaxConcurrentRequests(5);
        rateLimit.setTimeWindowMs(60000);
        rateLimits.put("skill-sample-001", rateLimit);
        
        CircuitBreakerConfig circuitBreaker = new CircuitBreakerConfig();
        circuitBreaker.setFailureThreshold(5);
        circuitBreaker.setSuccessThreshold(3);
        circuitBreaker.setTimeoutMs(30000);
        circuitBreaker.setHalfOpenTimeoutMs(10000);
        circuitBreaker.setEnabled(true);
        circuitBreakers.put("skill-sample-001", circuitBreaker);
        
        TrafficStats stats = new TrafficStats();
        stats.setSkillId("skill-sample-001");
        stats.setTotalRequests(100);
        stats.setSuccessfulRequests(95);
        stats.setFailedRequests(5);
        stats.setCurrentRequests(2);
        stats.setAverageResponseTime(150.5);
        stats.setLastUpdated(System.currentTimeMillis());
        trafficStats.put(stats.getSkillId(), stats);
    }
}
