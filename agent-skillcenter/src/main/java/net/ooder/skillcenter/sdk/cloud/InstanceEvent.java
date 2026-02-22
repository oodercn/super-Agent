package net.ooder.skillcenter.sdk.cloud;

public class InstanceEvent {
    private String eventId;
    private long timestamp;
    private String type;
    private String message;
    private String reason;

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
