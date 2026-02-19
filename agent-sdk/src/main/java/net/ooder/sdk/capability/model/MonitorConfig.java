package net.ooder.sdk.capability.model;

public class MonitorConfig {
    
    private boolean enableLogs;
    private boolean enableMetrics;
    private boolean enableTraces;
    private int logLevel;
    private int metricsInterval;
    private int retentionDays;
    
    public boolean isEnableLogs() { return enableLogs; }
    public void setEnableLogs(boolean enableLogs) { this.enableLogs = enableLogs; }
    
    public boolean isEnableMetrics() { return enableMetrics; }
    public void setEnableMetrics(boolean enableMetrics) { this.enableMetrics = enableMetrics; }
    
    public boolean isEnableTraces() { return enableTraces; }
    public void setEnableTraces(boolean enableTraces) { this.enableTraces = enableTraces; }
    
    public int getLogLevel() { return logLevel; }
    public void setLogLevel(int logLevel) { this.logLevel = logLevel; }
    
    public int getMetricsInterval() { return metricsInterval; }
    public void setMetricsInterval(int metricsInterval) { this.metricsInterval = metricsInterval; }
    
    public int getRetentionDays() { return retentionDays; }
    public void setRetentionDays(int retentionDays) { this.retentionDays = retentionDays; }
}
