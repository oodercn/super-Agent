package net.ooder.sdk.northbound.protocol.model;

import java.util.List;

public class ObservationTrace {
    
    private String traceId;
    private String targetId;
    private String operationType;
    private String operationName;
    private long duration;
    private boolean success;
    private String errorMessage;
    private List<TraceSpan> spans;
    private long timestamp;
    
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
    
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    
    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    
    public String getOperationName() { return operationName; }
    public void setOperationName(String operationName) { this.operationName = operationName; }
    
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
