package net.ooder.sdk.route.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;
import java.util.UUID;

public class RouteEvent {
    @JSONField(name = "eventId")
    private String eventId;
    
    @JSONField(name = "eventType")
    private RouteEventType eventType;
    
    @JSONField(name = "routeId")
    private String routeId;
    
    @JSONField(name = "timestamp")
    private long timestamp;
    
    @JSONField(name = "details")
    private Map<String, Object> details;
    
    @JSONField(name = "processed")
    private boolean processed;
    
    public RouteEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.processed = false;
    }
    
    public RouteEvent(RouteEventType eventType, String routeId, Map<String, Object> details) {
        this();
        this.eventType = eventType;
        this.routeId = routeId;
        this.details = details;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public RouteEventType getEventType() {
        return eventType;
    }

    public void setEventType(RouteEventType eventType) {
        this.eventType = eventType;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
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
        return "RouteEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventType=" + eventType +
                ", routeId='" + routeId + '\'' +
                ", timestamp=" + timestamp +
                ", processed=" + processed +
                '}';
    }
}
