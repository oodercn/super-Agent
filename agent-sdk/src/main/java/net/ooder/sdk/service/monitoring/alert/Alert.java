
package net.ooder.sdk.service.monitoring.alert;

import java.util.Map;

public class Alert {
    
    private String id;
    private String ruleId;
    private AlertLevel level;
    private String title;
    private String message;
    private long timestamp;
    private Map<String, Object> metadata;
    private boolean acknowledged;
    private long acknowledgedTime;
    private String acknowledgedBy;
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getRuleId() { return ruleId; }
    public void setRuleId(String ruleId) { this.ruleId = ruleId; }
    
    public AlertLevel getLevel() { return level; }
    public void setLevel(AlertLevel level) { this.level = level; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    public boolean isAcknowledged() { return acknowledged; }
    public void setAcknowledged(boolean acknowledged) { this.acknowledged = acknowledged; }
    
    public long getAcknowledgedTime() { return acknowledgedTime; }
    public void setAcknowledgedTime(long acknowledgedTime) { this.acknowledgedTime = acknowledgedTime; }
    
    public String getAcknowledgedBy() { return acknowledgedBy; }
    public void setAcknowledgedBy(String acknowledgedBy) { this.acknowledgedBy = acknowledgedBy; }
    
    public void acknowledge(String by) {
        this.acknowledged = true;
        this.acknowledgedTime = System.currentTimeMillis();
        this.acknowledgedBy = by;
    }
}
