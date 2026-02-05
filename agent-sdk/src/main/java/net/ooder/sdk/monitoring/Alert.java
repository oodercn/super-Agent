package net.ooder.sdk.monitoring;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;
import java.util.UUID;

public class Alert {
    @JSONField(name = "alertId")
    private String alertId;
    
    @JSONField(name = "level")
    private AlertLevel level;
    
    @JSONField(name = "type")
    private String type;
    
    @JSONField(name = "message")
    private String message;
    
    @JSONField(name = "timestamp")
    private long timestamp;
    
    @JSONField(name = "source")
    private String source;
    
    @JSONField(name = "details")
    private Map<String, Object> details;
    
    @JSONField(name = "resolved")
    private boolean resolved;
    
    @JSONField(name = "resolveTime")
    private Long resolveTime;
    
    @JSONField(name = "ruleId")
    private String ruleId;
    
    public Alert() {
        this.alertId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.resolved = false;
    }
    
    public Alert(AlertLevel level, String type, String message) {
        this();
        this.level = level;
        this.type = type;
        this.message = message;
    }
    
    public Alert(AlertLevel level, String type, String message, String source) {
        this(level, type, message);
        this.source = source;
    }
    
    public Alert(AlertLevel level, String type, String message, String source, Map<String, Object> details) {
        this(level, type, message, source);
        this.details = details;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public AlertLevel getLevel() {
        return level;
    }

    public void setLevel(AlertLevel level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
        if (resolved) {
            this.resolveTime = System.currentTimeMillis();
        }
    }

    public Long getResolveTime() {
        return resolveTime;
    }

    public void setResolveTime(Long resolveTime) {
        this.resolveTime = resolveTime;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "alertId='" + alertId + '\'' +
                ", level=" + level +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", source='" + source + '\'' +
                ", resolved=" + resolved +
                '}';
    }
}
