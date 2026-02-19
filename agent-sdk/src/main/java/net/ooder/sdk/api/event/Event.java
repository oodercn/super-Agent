package net.ooder.sdk.api.event;

import java.util.Map;
import java.util.UUID;

public abstract class Event {
    
    private String eventId;
    private String eventType;
    private long timestamp;
    private String source;
    private Map<String, Object> metadata;
    
    public Event() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.eventType = this.getClass().getSimpleName();
    }
    
    public Event(String source) {
        this();
        this.source = source;
    }
    
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
