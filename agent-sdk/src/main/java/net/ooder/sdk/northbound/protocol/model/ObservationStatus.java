package net.ooder.sdk.northbound.protocol.model;

public class ObservationStatus {
    
    private String targetId;
    private boolean observing;
    private long startTime;
    private long lastUpdate;
    private int metricsCount;
    private int logsCount;
    private int tracesCount;
    private int alertsCount;
    
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    
    public boolean isObserving() { return observing; }
    public void setObserving(boolean observing) { this.observing = observing; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(long lastUpdate) { this.lastUpdate = lastUpdate; }
    
    public int getMetricsCount() { return metricsCount; }
    public void setMetricsCount(int metricsCount) { this.metricsCount = metricsCount; }
    
    public int getLogsCount() { return logsCount; }
    public void setLogsCount(int logsCount) { this.logsCount = logsCount; }
    
    public int getTracesCount() { return tracesCount; }
    public void setTracesCount(int tracesCount) { this.tracesCount = tracesCount; }
    
    public int getAlertsCount() { return alertsCount; }
    public void setAlertsCount(int alertsCount) { this.alertsCount = alertsCount; }
}
