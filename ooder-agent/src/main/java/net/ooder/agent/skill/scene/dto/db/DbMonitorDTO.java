package net.ooder.mvp.skill.scene.dto.db;

public class DbMonitorDTO {
    
    private Integer activeConnections;
    private Integer idleConnections;
    private Integer pendingConnections;
    private Integer totalConnections;
    private Long avgResponseTime;
    private Double poolUsagePercent;

    public DbMonitorDTO() {}

    public Integer getActiveConnections() {
        return activeConnections;
    }

    public void setActiveConnections(Integer activeConnections) {
        this.activeConnections = activeConnections;
    }

    public Integer getIdleConnections() {
        return idleConnections;
    }

    public void setIdleConnections(Integer idleConnections) {
        this.idleConnections = idleConnections;
    }

    public Integer getPendingConnections() {
        return pendingConnections;
    }

    public void setPendingConnections(Integer pendingConnections) {
        this.pendingConnections = pendingConnections;
    }

    public Integer getTotalConnections() {
        return totalConnections;
    }

    public void setTotalConnections(Integer totalConnections) {
        this.totalConnections = totalConnections;
    }

    public Long getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(Long avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }

    public Double getPoolUsagePercent() {
        return poolUsagePercent;
    }

    public void setPoolUsagePercent(Double poolUsagePercent) {
        this.poolUsagePercent = poolUsagePercent;
    }
}
