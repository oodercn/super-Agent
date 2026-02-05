package net.ooder.sdk.topology.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;
import java.util.UUID;

public class TopologyEvent {
    @JSONField(name = "eventId")
    private String eventId;
    
    @JSONField(name = "eventType")
    private TopologyEventType eventType;
    
    @JSONField(name = "timestamp")
    private long timestamp;
    
    @JSONField(name = "details")
    private Map<String, Object> details;
    
    @JSONField(name = "processed")
    private boolean processed;
    
    public TopologyEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.processed = false;
    }
    
    public TopologyEvent(TopologyEventType eventType, Map<String, Object> details) {
        this();
        this.eventType = eventType;
        this.details = details;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public TopologyEventType getEventType() {
        return eventType;
    }

    public void setEventType(TopologyEventType eventType) {
        this.eventType = eventType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    @Override
    public String toString() {
        return "TopologyEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventType=" + eventType +
                ", timestamp=" + timestamp +
                ", processed=" + processed +
                '}';
    }
}
