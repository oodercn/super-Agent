package net.ooder.skill.config.dto;

public class DbMonitorDTO {
    
    private int activeConnections;
    private int idleConnections;
    private int totalConnections;
    private long totalQueries;
    private long avgQueryTime;

    public int getActiveConnections() { return activeConnections; }
    public void setActiveConnections(int activeConnections) { this.activeConnections = activeConnections; }
    public int getIdleConnections() { return idleConnections; }
    public void setIdleConnections(int idleConnections) { this.idleConnections = idleConnections; }
    public int getTotalConnections() { return totalConnections; }
    public void setTotalConnections(int totalConnections) { this.totalConnections = totalConnections; }
    public long getTotalQueries() { return totalQueries; }
    public void setTotalQueries(long totalQueries) { this.totalQueries = totalQueries; }
    public long getAvgQueryTime() { return avgQueryTime; }
    public void setAvgQueryTime(long avgQueryTime) { this.avgQueryTime = avgQueryTime; }
}
