
package net.ooder.sdk.core.metadata.model;

import java.io.Serializable;

public class TimelinePoint implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final long timestamp;
    private final Context context;
    private final String event;
    private final String eventType;
    
    public TimelinePoint(long timestamp, Context context, String event, String eventType) {
        this.timestamp = timestamp;
        this.context = context;
        this.event = event;
        this.eventType = eventType;
    }
    
    public TimelinePoint(Context context, String event) {
        this.timestamp = System.currentTimeMillis();
        this.context = context;
        this.event = event;
        this.eventType = inferEventType(event);
    }
    
    public long getTimestamp() { return timestamp; }
    public Context getContext() { return context; }
    public String getEvent() { return event; }
    public String getEventType() { return eventType; }
    
    public boolean hasContext() {
        return context != null && context.isValid();
    }
    
    public String getAgentId() {
        return context != null ? context.getAgentId() : null;
    }
    
    public String getStatus() {
        return context != null ? context.getStatus() : null;
    }
    
    private String inferEventType(String event) {
        if (event == null) return "UNKNOWN";
        String lower = event.toLowerCase();
        if (lower.contains("start") || lower.contains("begin")) return "START";
        if (lower.contains("stop") || lower.contains("end")) return "STOP";
        if (lower.contains("error") || lower.contains("fail")) return "ERROR";
        if (lower.contains("join") || lower.contains("enter")) return "JOIN";
        if (lower.contains("leave") || lower.contains("exit")) return "LEAVE";
        if (lower.contains("change") || lower.contains("update")) return "CHANGE";
        if (lower.contains("heartbeat") || lower.contains("ping")) return "HEARTBEAT";
        return "OTHER";
    }
    
    @Override
    public String toString() {
        return "TimelinePoint[" + timestamp + ", event=" + event + ", type=" + eventType + "]";
    }
    
    public static TimelinePoint of(String event, Context context) {
        return new TimelinePoint(context, event);
    }
    
    public static TimelinePoint of(String event, IdentityInfo identity, LocationInfo location, ResourceInfo resource) {
        Context ctx = new Context(identity, location, resource);
        return new TimelinePoint(ctx, event);
    }
}
