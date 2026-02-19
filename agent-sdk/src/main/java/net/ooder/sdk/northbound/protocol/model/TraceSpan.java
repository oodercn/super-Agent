package net.ooder.sdk.northbound.protocol.model;

import java.util.Map;

public class TraceSpan {
    
    private String spanId;
    private String parentSpanId;
    private String name;
    private long startTime;
    private long endTime;
    private Map<String, String> attributes;
    
    public String getSpanId() { return spanId; }
    public void setSpanId(String spanId) { this.spanId = spanId; }
    
    public String getParentSpanId() { return parentSpanId; }
    public void setParentSpanId(String parentSpanId) { this.parentSpanId = parentSpanId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    
    public Map<String, String> getAttributes() { return attributes; }
    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }
}
