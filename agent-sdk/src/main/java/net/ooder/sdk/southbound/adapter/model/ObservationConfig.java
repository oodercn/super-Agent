
package net.ooder.sdk.southbound.adapter.model;

import java.util.List;

public class ObservationConfig {
    
    private String observationId;
    private String targetType;
    private int interval;
    private int timeout;
    private List<String> metrics;
    private boolean continuous;
    private int maxSamples;
    private boolean includeMetadata;
    
    public String getObservationId() { return observationId; }
    public void setObservationId(String observationId) { this.observationId = observationId; }
    
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    
    public int getInterval() { return interval; }
    public void setInterval(int interval) { this.interval = interval; }
    
    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
    
    public List<String> getMetrics() { return metrics; }
    public void setMetrics(List<String> metrics) { this.metrics = metrics; }
    
    public boolean isContinuous() { return continuous; }
    public void setContinuous(boolean continuous) { this.continuous = continuous; }
    
    public int getMaxSamples() { return maxSamples; }
    public void setMaxSamples(int maxSamples) { this.maxSamples = maxSamples; }
    
    public boolean isIncludeMetadata() { return includeMetadata; }
    public void setIncludeMetadata(boolean includeMetadata) { this.includeMetadata = includeMetadata; }
}
