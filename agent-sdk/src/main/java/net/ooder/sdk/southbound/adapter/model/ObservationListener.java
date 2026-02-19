
package net.ooder.sdk.southbound.adapter.model;

public interface ObservationListener {
    
    void onDataCollected(String targetId, ObservationData data);
    
    void onObservationStarted(String targetId);
    
    void onObservationStopped(String targetId);
    
    void onObservationError(String targetId, String errorCode, String errorMessage);
    
    void onMetricChanged(String targetId, String metricName, Object oldValue, Object newValue);
}
