package net.ooder.nexus.skillcenter.dto.monitor;

import java.util.List;
import java.util.Map;

public class AlertDTO {
    private String alertId;
    private String skillId;
    private String instanceId;
    private String severity;
    private String type;
    private String title;
    private String message;
    private long timestamp;
    private String status;
    private Map<String, Object> details;
    private List<String> suggestedActions;

    public String getAlertId() { return alertId; }
    public void setAlertId(String alertId) { this.alertId = alertId; }
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public String getInstanceId() { return instanceId; }
    public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Map<String, Object> getDetails() { return details; }
    public void setDetails(Map<String, Object> details) { this.details = details; }
    public List<String> getSuggestedActions() { return suggestedActions; }
    public void setSuggestedActions(List<String> suggestedActions) { this.suggestedActions = suggestedActions; }
}
