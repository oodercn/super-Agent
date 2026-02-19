package net.ooder.sdk.northbound.protocol.model;

public interface ObservationListener {
    
    void onMetricCollected(String targetId, ObservationMetric metric);
    
    void onLogCollected(String targetId, ObservationLog log);
    
    void onTraceCollected(String targetId, ObservationTrace trace);
    
    void onAlertTriggered(String targetId, AlertInfo alert);
    
    void onAlertAcknowledged(String alertId);
    
    void onHealthChanged(String targetId, HealthStatus oldStatus, HealthStatus newStatus);
}
