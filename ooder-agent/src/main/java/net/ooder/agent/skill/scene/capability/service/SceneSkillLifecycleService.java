package net.ooder.mvp.skill.scene.capability.service;

import net.ooder.mvp.skill.scene.capability.model.CapabilityStatus;
import net.ooder.mvp.skill.scene.capability.model.SceneType;
import net.ooder.mvp.skill.scene.capability.model.SkillForm;

import java.util.List;
import java.util.Map;

public interface SceneSkillLifecycleService {
    
    LifecycleResult activate(String capabilityId, String userId);
    
    LifecycleResult pause(String capabilityId, String userId);
    
    LifecycleResult deactivate(String capabilityId, String userId);
    
    LifecycleResult resume(String capabilityId, String userId);
    
    LifecycleResult trigger(String capabilityId, TriggerRequest request);
    
    LifecycleResult archive(String capabilityId, String userId);
    
    LifecycleState getState(String capabilityId);
    
    List<LifecycleTransition> getAvailableTransitions(String capabilityId);
    
    public static class TriggerRequest {
        private String action;
        private Map<String, Object> params;
        private String userId;
        
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public Map<String, Object> getParams() { return params; }
        public void setParams(Map<String, Object> params) { this.params = params; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }
    
    public static class LifecycleResult {
        private boolean success;
        private String capabilityId;
        private CapabilityStatus previousStatus;
        private CapabilityStatus currentStatus;
        private SceneType sceneType;
        private SkillForm skillForm;
        private String sceneGroupId;
        private String message;
        private List<String> nextSteps;
        private long timestamp;
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getCapabilityId() { return capabilityId; }
        public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
        public CapabilityStatus getPreviousStatus() { return previousStatus; }
        public void setPreviousStatus(CapabilityStatus previousStatus) { this.previousStatus = previousStatus; }
        public CapabilityStatus getCurrentStatus() { return currentStatus; }
        public void setCurrentStatus(CapabilityStatus currentStatus) { this.currentStatus = currentStatus; }
        public SceneType getSceneType() { return sceneType; }
        public void setSceneType(SceneType sceneType) { this.sceneType = sceneType; }
        public SkillForm getSkillForm() { return skillForm; }
        public void setSkillForm(SkillForm skillForm) { this.skillForm = skillForm; }
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public List<String> getNextSteps() { return nextSteps; }
        public void setNextSteps(List<String> nextSteps) { this.nextSteps = nextSteps; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        
        public static LifecycleResult success(String capabilityId, CapabilityStatus previous, CapabilityStatus current) {
            LifecycleResult result = new LifecycleResult();
            result.setSuccess(true);
            result.setCapabilityId(capabilityId);
            result.setPreviousStatus(previous);
            result.setCurrentStatus(current);
            result.setTimestamp(System.currentTimeMillis());
            return result;
        }
        
        public static LifecycleResult failure(String capabilityId, String message) {
            LifecycleResult result = new LifecycleResult();
            result.setSuccess(false);
            result.setCapabilityId(capabilityId);
            result.setMessage(message);
            result.setTimestamp(System.currentTimeMillis());
            return result;
        }
    }
    
    public static class LifecycleState {
        private String capabilityId;
        private CapabilityStatus status;
        private SceneType sceneType;
        private SkillForm skillForm;
        private boolean canPause;
        private boolean canResume;
        private boolean canTrigger;
        private boolean canArchive;
        private List<LifecycleTransition> availableTransitions;
        private Map<String, Object> context;
        
        public String getCapabilityId() { return capabilityId; }
        public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
        public CapabilityStatus getStatus() { return status; }
        public void setStatus(CapabilityStatus status) { this.status = status; }
        public SceneType getSceneType() { return sceneType; }
        public void setSceneType(SceneType sceneType) { this.sceneType = sceneType; }
        public SkillForm getSkillForm() { return skillForm; }
        public void setSkillForm(SkillForm skillForm) { this.skillForm = skillForm; }
        public boolean isCanPause() { return canPause; }
        public void setCanPause(boolean canPause) { this.canPause = canPause; }
        public boolean isCanResume() { return canResume; }
        public void setCanResume(boolean canResume) { this.canResume = canResume; }
        public boolean isCanTrigger() { return canTrigger; }
        public void setCanTrigger(boolean canTrigger) { this.canTrigger = canTrigger; }
        public boolean isCanArchive() { return canArchive; }
        public void setCanArchive(boolean canArchive) { this.canArchive = canArchive; }
        public List<LifecycleTransition> getAvailableTransitions() { return availableTransitions; }
        public void setAvailableTransitions(List<LifecycleTransition> availableTransitions) { this.availableTransitions = availableTransitions; }
        public Map<String, Object> getContext() { return context; }
        public void setContext(Map<String, Object> context) { this.context = context; }
    }
    
    public static class LifecycleTransition {
        private String action;
        private String name;
        private String description;
        private CapabilityStatus targetStatus;
        private boolean requiresPermission;
        private List<String> requiredPermissions;
        
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public CapabilityStatus getTargetStatus() { return targetStatus; }
        public void setTargetStatus(CapabilityStatus targetStatus) { this.targetStatus = targetStatus; }
        public boolean isRequiresPermission() { return requiresPermission; }
        public void setRequiresPermission(boolean requiresPermission) { this.requiresPermission = requiresPermission; }
        public List<String> getRequiredPermissions() { return requiredPermissions; }
        public void setRequiredPermissions(List<String> requiredPermissions) { this.requiredPermissions = requiredPermissions; }
    }
}
