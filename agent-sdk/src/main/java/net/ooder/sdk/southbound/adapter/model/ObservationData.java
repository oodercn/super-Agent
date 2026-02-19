
package net.ooder.sdk.southbound.adapter.model;

import java.util.Map;

public class ObservationData {
    
    private String targetId;
    private String observationId;
    private long timestamp;
    private Map<String, Object> metrics;
    private Map<String, Object> metadata;
    private String status;
    private String errorMessage;
    
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    
    public String getObservationId() { return observationId; }
    public void setObservationId(String observationId) { this.observationId = observationId; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public Map<String, Object> getMetrics() { return metrics; }
    public void setMetrics(Map<String, Object> metrics) { this.metrics = metrics; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public boolean isSuccess() {
        return "success".equals(status);
    }
    
    public Object getMetric(String name) {
        return metrics != null ? metrics.get(name) : null;
    }
    
    public double getMetricAsDouble(String name, double defaultValue) {
        Object value = getMetric(name);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return defaultValue;
    }
    
    public long getMetricAsLong(String name, long defaultValue) {
        Object value = getMetric(name);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return defaultValue;
    }
}
