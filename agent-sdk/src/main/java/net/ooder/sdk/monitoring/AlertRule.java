package net.ooder.sdk.monitoring;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;
import java.util.UUID;

public class AlertRule {
    @JSONField(name = "ruleId")
    private String ruleId;
    
    @JSONField(name = "name")
    private String name;
    
    @JSONField(name = "description")
    private String description;
    
    @JSONField(name = "level")
    private AlertLevel level;
    
    @JSONField(name = "type")
    private String type;
    
    @JSONField(name = "condition")
    private String condition;
    
    @JSONField(name = "threshold")
    private double threshold;
    
    @JSONField(name = "comparator")
    private String comparator;
    
    @JSONField(name = "metric")
    private String metric;
    
    @JSONField(name = "source")
    private String source;
    
    @JSONField(name = "enabled")
    private boolean enabled;
    
    @JSONField(name = "notificationEnabled")
    private boolean notificationEnabled;
    
    @JSONField(name = "notificationChannels")
    private Map<String, Object> notificationChannels;
    
    @JSONField(name = "createdAt")
    private long createdAt;
    
    @JSONField(name = "updatedAt")
    private long updatedAt;
    
    public AlertRule() {
        this.ruleId = UUID.randomUUID().toString();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.enabled = true;
        this.notificationEnabled = true;
    }
    
    public AlertRule(String name, AlertLevel level, String type, String metric, String condition, double threshold) {
        this();
        this.name = name;
        this.level = level;
        this.type = type;
        this.metric = metric;
        this.condition = condition;
        this.threshold = threshold;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = System.currentTimeMillis();
    }

    public AlertLevel getLevel() {
        return level;
    }

    public void setLevel(AlertLevel level) {
        this.level = level;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
        this.updatedAt = System.currentTimeMillis();
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getComparator() {
        return comparator;
    }

    public void setComparator(String comparator) {
        this.comparator = comparator;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
        this.updatedAt = System.currentTimeMillis();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.updatedAt = System.currentTimeMillis();
    }

    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
        this.updatedAt = System.currentTimeMillis();
    }

    public Map<String, Object> getNotificationChannels() {
        return notificationChannels;
    }

    public void setNotificationChannels(Map<String, Object> notificationChannels) {
        this.notificationChannels = notificationChannels;
        this.updatedAt = System.currentTimeMillis();
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "AlertRule{" +
                "ruleId='" + ruleId + '\'' +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", type='" + type + '\'' +
                ", metric='" + metric + '\'' +
                ", condition='" + condition + '\'' +
                ", threshold=" + threshold +
                ", enabled=" + enabled +
                '}';
    }
}
