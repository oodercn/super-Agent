package net.ooder.sdk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ooder.sdk.monitoring")
public class MonitoringProperties {
    
    private boolean enabled = true;
    
    private boolean metricsCollectionEnabled = true;
    
    private int metricsCollectionIntervalMs = 5000;
    
    private boolean alertEnabled = true;
    
    private int errorThreshold = 10;
    
    private long latencyThresholdMs = 5000;
    
    private long throughputThresholdBytes = 1000000;
    
    private boolean reportingEnabled = true;
    
    private int reportingIntervalMs = 60000;
    
    private boolean intelligentMonitoringEnabled = false;
    
    private double anomalyThreshold = 3.0;
    
    private int predictionHorizon = 10;
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isMetricsCollectionEnabled() {
        return metricsCollectionEnabled;
    }
    
    public void setMetricsCollectionEnabled(boolean metricsCollectionEnabled) {
        this.metricsCollectionEnabled = metricsCollectionEnabled;
    }
    
    public int getMetricsCollectionIntervalMs() {
        return metricsCollectionIntervalMs;
    }
    
    public void setMetricsCollectionIntervalMs(int metricsCollectionIntervalMs) {
        this.metricsCollectionIntervalMs = metricsCollectionIntervalMs;
    }
    
    public boolean isAlertEnabled() {
        return alertEnabled;
    }
    
    public void setAlertEnabled(boolean alertEnabled) {
        this.alertEnabled = alertEnabled;
    }
    
    public int getErrorThreshold() {
        return errorThreshold;
    }
    
    public void setErrorThreshold(int errorThreshold) {
        this.errorThreshold = errorThreshold;
    }
    
    public long getLatencyThresholdMs() {
        return latencyThresholdMs;
    }
    
    public void setLatencyThresholdMs(long latencyThresholdMs) {
        this.latencyThresholdMs = latencyThresholdMs;
    }
    
    public long getThroughputThresholdBytes() {
        return throughputThresholdBytes;
    }
    
    public void setThroughputThresholdBytes(long throughputThresholdBytes) {
        this.throughputThresholdBytes = throughputThresholdBytes;
    }
    
    public boolean isReportingEnabled() {
        return reportingEnabled;
    }
    
    public void setReportingEnabled(boolean reportingEnabled) {
        this.reportingEnabled = reportingEnabled;
    }
    
    public int getReportingIntervalMs() {
        return reportingIntervalMs;
    }
    
    public void setReportingIntervalMs(int reportingIntervalMs) {
        this.reportingIntervalMs = reportingIntervalMs;
    }
    
    public boolean isIntelligentMonitoringEnabled() {
        return intelligentMonitoringEnabled;
    }
    
    public void setIntelligentMonitoringEnabled(boolean intelligentMonitoringEnabled) {
        this.intelligentMonitoringEnabled = intelligentMonitoringEnabled;
    }
    
    public double getAnomalyThreshold() {
        return anomalyThreshold;
    }
    
    public void setAnomalyThreshold(double anomalyThreshold) {
        this.anomalyThreshold = anomalyThreshold;
    }
    
    public int getPredictionHorizon() {
        return predictionHorizon;
    }
    
    public void setPredictionHorizon(int predictionHorizon) {
        this.predictionHorizon = predictionHorizon;
    }
}
