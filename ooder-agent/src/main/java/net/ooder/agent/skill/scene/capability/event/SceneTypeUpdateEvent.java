package net.ooder.mvp.skill.scene.capability.event;

import java.util.List;

public class SceneTypeUpdateEvent {
    
    private final String capabilityId;
    private final String capabilityName;
    private final UpdateAction action;
    private final String sceneType;
    private final List<String> updatedSceneTypes;
    private final String approvedBy;
    private final long timestamp;
    
    public enum UpdateAction {
        ADD,
        REMOVE
    }
    
    public SceneTypeUpdateEvent(String capabilityId, String capabilityName, 
                                UpdateAction action, String sceneType,
                                List<String> updatedSceneTypes, String approvedBy) {
        this.capabilityId = capabilityId;
        this.capabilityName = capabilityName;
        this.action = action;
        this.sceneType = sceneType;
        this.updatedSceneTypes = updatedSceneTypes;
        this.approvedBy = approvedBy;
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getCapabilityId() {
        return capabilityId;
    }
    
    public String getCapabilityName() {
        return capabilityName;
    }
    
    public UpdateAction getAction() {
        return action;
    }
    
    public String getSceneType() {
        return sceneType;
    }
    
    public List<String> getUpdatedSceneTypes() {
        return updatedSceneTypes;
    }
    
    public String getApprovedBy() {
        return approvedBy;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return String.format("SceneTypeUpdateEvent{capabilityId='%s', action=%s, sceneType='%s', approvedBy='%s'}",
            capabilityId, action, sceneType, approvedBy);
    }
}
