package net.ooder.sdk.terminal.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;
import java.util.UUID;

public class TerminalEvent {
    @JSONField(name = "eventId")
    private String eventId;
    
    @JSONField(name = "eventType")
    private TerminalEventType eventType;
    
    @JSONField(name = "deviceId")
    private String deviceId;
    
    @JSONField(name = "timestamp")
    private long timestamp;
    
    @JSONField(name = "details")
    private Map<String, Object> details;
    
    @JSONField(name = "processed")
    private boolean processed;
    
    public TerminalEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.processed = false;
    }
    
    public TerminalEvent(TerminalEventType eventType, String deviceId, Map<String, Object> details) {
        this();
        this.eventType = eventType;
        this.deviceId = deviceId;
        this.details = details;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public TerminalEventType getEventType() {
        return eventType;
    }

    public void setEventType(TerminalEventType eventType) {
        this.eventType = eventType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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
        return "TerminalEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventType=" + eventType +
                ", deviceId='" + deviceId + '\'' +
                ", timestamp=" + timestamp +
                ", processed=" + processed +
                '}';
    }
}
