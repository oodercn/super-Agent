package net.ooder.sdk.northbound.protocol.model;

import java.util.List;

public class ObservationConfig {
    
    private boolean enableMetrics;
    private boolean enableLogs;
    private boolean enableTraces;
    private int metricsInterval;
    private int logLevel;
    private int retentionDays;
    private List<String> metricTypes;
    
    public boolean isEnableMetrics() { return enableMetrics; }
    public void setEnableMetrics(boolean enableMetrics) { this.enableMetrics = enableMetrics; }
    
    public boolean isEnableLogs() { return enableLogs; }
    public void setEnableLogs(boolean enableLogs) { this.enableLogs = enableLogs; }
    
    public boolean isEnableTraces() { return enableTraces; }
    public void setEnableTraces(boolean enableTraces) { this.enableTraces = enableTraces; }
    
    public int getMetricsInterval() { return metricsInterval; }
    public void setMetricsInterval(int metricsInterval) { this.metricsInterval = metricsInterval; }
    
    public int getLogLevel() { return logLevel; }
    public void setLogLevel(int logLevel) { this.logLevel = logLevel; }
    
    public int getRetentionDays() { return retentionDays; }
    public void setRetentionDays(int retentionDays) { this.retentionDays = retentionDays; }
    
    public List<String> getMetricTypes() { return metricTypes; }
    public void setMetricTypes(List<String> metricTypes) { this.metricTypes = metricTypes; }
}
