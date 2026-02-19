package net.ooder.sdk.capability.model;

public interface MonitorListener {
    
    void onLogRecorded(ExecutionLog log);
    
    void onMetricRecorded(MetricRecord metric);
    
    void onTraceRecorded(ExecutionTrace trace);
    
    void onAlertTriggered(AlertInfo alert);
}
