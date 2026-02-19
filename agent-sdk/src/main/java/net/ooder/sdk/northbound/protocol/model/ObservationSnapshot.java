package net.ooder.sdk.northbound.protocol.model;

import java.util.List;
import java.util.Map;

public class ObservationSnapshot {
    
    private String targetId;
    private long timestamp;
    private Map<String, ObservationMetric> latestMetrics;
    private List<ObservationLog> recentLogs;
    private List<AlertInfo> activeAlerts;
    private HealthStatus healthStatus;
    
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public Map<String, ObservationMetric> getLatestMetrics() { return latestMetrics; }
    public void setLatestMetrics(Map<String, ObservationMetric> latestMetrics) { this.latestMetrics = latestMetrics; }
    
    public List<ObservationLog> getRecentLogs() { return recentLogs; }
    public void setRecentLogs(List<ObservationLog> recentLogs) { this.recentLogs = recentLogs; }
    
    public List<AlertInfo> getActiveAlerts() { return activeAlerts; }
    public void setActiveAlerts(List<AlertInfo> activeAlerts) { this.activeAlerts = activeAlerts; }
    
    public HealthStatus getHealthStatus() { return healthStatus; }
    public void setHealthStatus(HealthStatus healthStatus) { this.healthStatus = healthStatus; }
}
