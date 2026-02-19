
package net.ooder.sdk.api.metadata;

import java.util.Map;

import net.ooder.sdk.common.enums.ChangeType;

public class ChangeRecord {
    
    private String recordId;
    private String entityId;
    private String entityType;
    private ChangeType changeType;
    private long timestamp;
    private String operatorId;
    private Map<String, Object> before;
    private Map<String, Object> after;
    private String description;
    private String sceneGroupId;
    private String correlationId;
    
    public String getRecordId() {
        return recordId;
    }
    
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
    
    public String getEntityId() {
        return entityId;
    }
    
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    public ChangeType getChangeType() {
        return changeType;
    }
    
    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
    
    public Map<String, Object> getBefore() {
        return before;
    }
    
    public void setBefore(Map<String, Object> before) {
        this.before = before;
    }
    
    public Map<String, Object> getAfter() {
        return after;
    }
    
    public void setAfter(Map<String, Object> after) {
        this.after = after;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSceneGroupId() {
        return sceneGroupId;
    }
    
    public void setSceneGroupId(String sceneGroupId) {
        this.sceneGroupId = sceneGroupId;
    }
    
    public String getCorrelationId() {
        return correlationId;
    }
    
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}
