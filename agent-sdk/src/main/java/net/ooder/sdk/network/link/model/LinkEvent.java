package net.ooder.sdk.network.link.model;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.network.link.LinkStatus;

import java.util.Map;
import java.util.UUID;

public class LinkEvent {
    @JSONField(name = "eventId")
    private String eventId;
    
    @JSONField(name = "eventType")
    private LinkEventType eventType;
    
    @JSONField(name = "linkId")
    private String linkId;
    
    @JSONField(name = "timestamp")
    private long timestamp;
    
    @JSONField(name = "previousStatus")
    private LinkStatus previousStatus;
    
    @JSONField(name = "currentStatus")
    private LinkStatus currentStatus;
    
    @JSONField(name = "details")
    private Map<String, Object> details;
    
    @JSONField(name = "processed")
    private boolean processed;
    
    public LinkEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.processed = false;
    }
    
    public LinkEvent(LinkEventType eventType, String linkId) {
        this();
        this.eventType = eventType;
        this.linkId = linkId;
    }
    
    public LinkEvent(LinkEventType eventType, String linkId, LinkStatus previousStatus, LinkStatus currentStatus) {
        this(eventType, linkId);
        this.previousStatus = previousStatus;
        this.currentStatus = currentStatus;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public LinkEventType getEventType() {
        return eventType;
    }

    public void setEventType(LinkEventType eventType) {
        this.eventType = eventType;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public LinkStatus getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(LinkStatus previousStatus) {
        this.previousStatus = previousStatus;
    }

    public LinkStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(LinkStatus currentStatus) {
        this.currentStatus = currentStatus;
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
        return "LinkEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventType=" + eventType +
                ", linkId='" + linkId + '\'' +
                ", timestamp=" + timestamp +
                ", previousStatus=" + previousStatus +
                ", currentStatus=" + currentStatus +
                ", processed=" + processed +
                '}';
    }
}
