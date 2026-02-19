package net.ooder.sdk.capability.model;

import java.util.List;
import java.util.Map;

public class ExecutionTrace {
    
    private String traceId;
    private String capabilityId;
    private String executionId;
    private long duration;
    private boolean success;
    private String errorMessage;
    private List<TraceSpan> spans;
    private long timestamp;
    
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
    
    public String getCapabilityId() { return capabilityId; }
    public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
    
    public String getExecutionId() { return executionId; }
    public void setExecutionId(String executionId) { this.executionId = executionId; }
    
    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public List<TraceSpan> getSpans() { return spans; }
    public void setSpans(List<TraceSpan> spans) { this.spans = spans; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
