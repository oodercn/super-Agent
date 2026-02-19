package net.ooder.sdk.capability.model;

public class MonitorStatus {
    
    private String capabilityId;
    private boolean monitoring;
    private long startTime;
    private int logsCount;
    private int metricsCount;
    private int tracesCount;
    private int alertsCount;
    private HealthStatus healthStatus;
    
    public String getCapabilityId() { return capabilityId; }
    public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
    
    public boolean isMonitoring() { return monitoring; }
    public void setMonitoring(boolean monitoring) { this.monitoring = monitoring; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public int getLogsCount() { return logsCount; }
    public void setLogsCount(int logsCount) { this.logsCount = logsCount; }
    
    public int getMetricsCount() { return metricsCount; }
    public void setMetricsCount(int metricsCount) { this.metricsCount = metricsCount; }
    
    public int getTracesCount() { return tracesCount; }
    public void setTracesCount(int tracesCount) { this.tracesCount = tracesCount; }
    
    public int getAlertsCount() { return alertsCount; }
    public void setAlertsCount(int alertsCount) { this.alertsCount = alertsCount; }
    
    public HealthStatus getHealthStatus() { return healthStatus; }
    public void setHealthStatus(HealthStatus healthStatus) { this.healthStatus = healthStatus; }
}
