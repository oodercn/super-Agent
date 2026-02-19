package net.ooder.sdk.northbound.protocol.model;

public class AlertInfo {
    
    private String alertId;
    private String ruleId;
    private String targetId;
    private String metricType;
    private double value;
    private double threshold;
    private AlertSeverity severity;
    private String message;
    private long triggeredAt;
    private boolean acknowledged;
    private String acknowledgedBy;
    private long acknowledgedAt;
    
    public String getAlertId() { return alertId; }
    public void setAlertId(String alertId) { this.alertId = alertId; }
    
    public String getRuleId() { return ruleId; }
    public void setRuleId(String ruleId) { this.ruleId = ruleId; }
    
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    
    public String getMetricType() { return metricType; }
    public void setMetricType(String metricType) { this.metricType = metricType; }
    
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    
    public double getThreshold() { return threshold; }
    public void setThreshold(double threshold) { this.threshold = threshold; }
    
    public AlertSeverity getSeverity() { return severity; }
    public void setSeverity(AlertSeverity severity) { this.severity = severity; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public long getTriggeredAt() { return triggeredAt; }
    public void setTriggeredAt(long triggeredAt) { this.triggeredAt = triggeredAt; }
    
    public boolean isAcknowledged() { return acknowledged; }
    public void setAcknowledged(boolean acknowledged) { this.acknowledged = acknowledged; }
    
    public String getAcknowledgedBy() { return acknowledgedBy; }
    public void setAcknowledgedBy(String acknowledgedBy) { this.acknowledgedBy = acknowledgedBy; }
    
    public long getAcknowledgedAt() { return acknowledgedAt; }
    public void setAcknowledgedAt(long acknowledgedAt) { this.acknowledgedAt = acknowledgedAt; }
}
