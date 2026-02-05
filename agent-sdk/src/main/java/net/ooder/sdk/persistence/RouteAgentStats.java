package net.ooder.sdk.persistence;

/**
 * 路由代理统计信息
 */
public class RouteAgentStats {
    private int totalRouteAgents;
    private int activeRouteAgents;
    private int inactiveRouteAgents;
    private int totalSkills;
    private int activeSkills;
    private int inactiveSkills;
    private int totalEndAgents;
    private long totalRequests;
    private long successfulRequests;
    private long failedRequests;
    private long lastUpdated;
    
    // 构造函数
    public RouteAgentStats() {
        this.lastUpdated = System.currentTimeMillis();
    }
    
    // Getter和Setter方法
    public int getTotalRouteAgents() {
        return totalRouteAgents;
    }
    
    public void setTotalRouteAgents(int totalRouteAgents) {
        this.totalRouteAgents = totalRouteAgents;
    }
    
    public int getActiveRouteAgents() {
        return activeRouteAgents;
    }
    
    public void setActiveRouteAgents(int activeRouteAgents) {
        this.activeRouteAgents = activeRouteAgents;
    }
    
    public int getInactiveRouteAgents() {
        return inactiveRouteAgents;
    }
    
    public void setInactiveRouteAgents(int inactiveRouteAgents) {
        this.inactiveRouteAgents = inactiveRouteAgents;
    }
    
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
    
    public int getTotalEndAgents() {
        return totalEndAgents;
    }
    
    public void setTotalEndAgents(int totalEndAgents) {
        this.totalEndAgents = totalEndAgents;
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
