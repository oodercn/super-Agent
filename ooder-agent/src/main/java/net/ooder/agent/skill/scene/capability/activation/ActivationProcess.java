package net.ooder.mvp.skill.scene.capability.activation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActivationProcess {
    
    private String processId;
    private String installId;
    private String sceneId;
    private String sceneGroupId;
    private String templateId;
    private String roleName;
    private List<ActivationStep> steps;
    private int currentStep;
    private int totalSteps;
    private ActivationStatus status;
    private long createTime;
    private long updateTime;
    private String activator;
    
    private String keyId;
    private String keyStatus;
    private List<NetworkAction> networkActions;
    private Map<String, Object> config;
    private List<PrivateCapabilityConfig> privateCapabilities;
    private List<String> enabledPrivateCapabilities;
    private boolean menuRegistered;
    private boolean notificationSent;
    
    public enum ActivationStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        CANCELLED
    }
    
    public static class ActivationStep {
        private String stepId;
        private String name;
        private String description;
        private StepStatus status;
        private Map<String, Object> data;
        private String error;
        private long startTime;
        private long endTime;
        private boolean skippable;
        private boolean required;
        
        public enum StepStatus {
            PENDING,
            IN_PROGRESS,
            COMPLETED,
            FAILED,
            SKIPPED
        }
        
        public String getStepId() { return stepId; }
        public void setStepId(String stepId) { this.stepId = stepId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public StepStatus getStatus() { return status; }
        public void setStatus(StepStatus status) { this.status = status; }
        public Map<String, Object> getData() { return data; }
        public void setData(Map<String, Object> data) { this.data = data; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
        public boolean isSkippable() { return skippable; }
        public void setSkippable(boolean skippable) { this.skippable = skippable; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
    }
    
    public static class NetworkAction {
        private String actionId;
        private String actionName;
        private String actionType;
        private ActionStatus status;
        private String message;
        private long timestamp;
        private Map<String, Object> result;
        
        public enum ActionStatus {
            PENDING,
            RUNNING,
            COMPLETED,
            FAILED,
            SKIPPED
        }
        
        public String getActionId() { return actionId; }
        public void setActionId(String actionId) { this.actionId = actionId; }
        public String getActionName() { return actionName; }
        public void setActionName(String actionName) { this.actionName = actionName; }
        public String getActionType() { return actionType; }
        public void setActionType(String actionType) { this.actionType = actionType; }
        public ActionStatus getStatus() { return status; }
        public void setStatus(ActionStatus status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public Map<String, Object> getResult() { return result; }
        public void setResult(Map<String, Object> result) { this.result = result; }
    }
    
    public static class PrivateCapabilityConfig {
        private String capId;
        private String name;
        private String description;
        private boolean optional;
        private boolean enabled;
        private String skillId;
        private Map<String, Object> config;
        
        public String getCapId() { return capId; }
        public void setCapId(String capId) { this.capId = capId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public boolean isOptional() { return optional; }
        public void setOptional(boolean optional) { this.optional = optional; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
    }
    
    public String getProcessId() { return processId; }
    public void setProcessId(String processId) { this.processId = processId; }
    public String getInstallId() { return installId; }
    public void setInstallId(String installId) { this.installId = installId; }
    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public List<ActivationStep> getSteps() { return steps != null ? steps : new ArrayList<>(); }
    public void setSteps(List<ActivationStep> steps) { this.steps = steps; }
    public int getCurrentStep() { return currentStep; }
    public void setCurrentStep(int currentStep) { this.currentStep = currentStep; }
    public int getTotalSteps() { return totalSteps; }
    public void setTotalSteps(int totalSteps) { this.totalSteps = totalSteps; }
    public ActivationStatus getStatus() { return status; }
    public void setStatus(ActivationStatus status) { this.status = status; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public long getUpdateTime() { return updateTime; }
    public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
    public String getActivator() { return activator; }
    public void setActivator(String activator) { this.activator = activator; }
    public String getKeyId() { return keyId; }
    public void setKeyId(String keyId) { this.keyId = keyId; }
    public String getKeyStatus() { return keyStatus; }
    public void setKeyStatus(String keyStatus) { this.keyStatus = keyStatus; }
    public List<NetworkAction> getNetworkActions() { return networkActions != null ? networkActions : new ArrayList<>(); }
    public void setNetworkActions(List<NetworkAction> networkActions) { this.networkActions = networkActions; }
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
    public List<PrivateCapabilityConfig> getPrivateCapabilities() { return privateCapabilities != null ? privateCapabilities : new ArrayList<>(); }
    public void setPrivateCapabilities(List<PrivateCapabilityConfig> privateCapabilities) { this.privateCapabilities = privateCapabilities; }
    public List<String> getEnabledPrivateCapabilities() { return enabledPrivateCapabilities; }
    public void setEnabledPrivateCapabilities(List<String> enabledPrivateCapabilities) { this.enabledPrivateCapabilities = enabledPrivateCapabilities; }
    public boolean isMenuRegistered() { return menuRegistered; }
    public void setMenuRegistered(boolean menuRegistered) { this.menuRegistered = menuRegistered; }
    public boolean isNotificationSent() { return notificationSent; }
    public void setNotificationSent(boolean notificationSent) { this.notificationSent = notificationSent; }
    
    public int getProgress() {
        if (totalSteps == 0) return 0;
        return (currentStep * 100) / totalSteps;
    }
    
    public ActivationStep getCurrentStepObject() {
        if (steps == null || currentStep < 0 || currentStep >= steps.size()) {
            return null;
        }
        return steps.get(currentStep);
    }
    
    public boolean isCompleted() {
        return status == ActivationStatus.COMPLETED;
    }
    
    public boolean isInProgress() {
        return status == ActivationStatus.IN_PROGRESS;
    }
    
    public static ActivationProcess createDefault(String installId) {
        ActivationProcess process = new ActivationProcess();
        process.setProcessId("proc-" + System.currentTimeMillis());
        process.setInstallId(installId);
        process.setStatus(ActivationStatus.PENDING);
        process.setCreateTime(System.currentTimeMillis());
        process.setUpdateTime(System.currentTimeMillis());
        process.setCurrentStep(0);
        process.setTotalSteps(6);
        
        List<ActivationStep> steps = new ArrayList<>();
        
        ActivationStep step1 = new ActivationStep();
        step1.setStepId("confirm-participants");
        step1.setName("确认参与者");
        step1.setDescription("确认场景的主导者和协作者");
        step1.setStatus(ActivationStep.StepStatus.PENDING);
        step1.setRequired(true);
        step1.setSkippable(false);
        steps.add(step1);
        
        ActivationStep step2 = new ActivationStep();
        step2.setStepId("select-push-targets");
        step2.setName("选择推送目标");
        step2.setDescription("选择要推送的下属员工");
        step2.setStatus(ActivationStep.StepStatus.PENDING);
        step2.setRequired(true);
        step2.setSkippable(false);
        steps.add(step2);
        
        ActivationStep step3 = new ActivationStep();
        step3.setStepId("config-conditions");
        step3.setName("配置驱动条件");
        step3.setDescription("配置场景的驱动条件和触发规则");
        step3.setStatus(ActivationStep.StepStatus.PENDING);
        step3.setRequired(true);
        step3.setSkippable(false);
        steps.add(step3);
        
        ActivationStep step4 = new ActivationStep();
        step4.setStepId("get-key");
        step4.setName("获取KEY");
        step4.setDescription("获取访问安全数据的密钥");
        step4.setStatus(ActivationStep.StepStatus.PENDING);
        step4.setRequired(true);
        step4.setSkippable(false);
        steps.add(step4);
        
        ActivationStep step5 = new ActivationStep();
        step5.setStepId("confirm-activation");
        step5.setName("确认激活");
        step5.setDescription("确认激活场景");
        step5.setStatus(ActivationStep.StepStatus.PENDING);
        step5.setRequired(true);
        step5.setSkippable(false);
        steps.add(step5);
        
        ActivationStep step6 = new ActivationStep();
        step6.setStepId("network-actions");
        step6.setName("入网动作");
        step6.setDescription("执行入网相关动作，推送通知给员工");
        step6.setStatus(ActivationStep.StepStatus.PENDING);
        step6.setRequired(true);
        step6.setSkippable(false);
        steps.add(step6);
        
        process.setSteps(steps);
        
        List<NetworkAction> actions = new ArrayList<>();
        
        NetworkAction action1 = new NetworkAction();
        action1.setActionId("notify-other-scenes");
        action1.setActionName("通知其他场景");
        action1.setActionType("NOTIFICATION");
        action1.setStatus(NetworkAction.ActionStatus.PENDING);
        actions.add(action1);
        
        NetworkAction action2 = new NetworkAction();
        action2.setActionId("update-my-capabilities");
        action2.setActionName("更新我的能力");
        action2.setActionType("UPDATE");
        action2.setStatus(NetworkAction.ActionStatus.PENDING);
        actions.add(action2);
        
        NetworkAction action3 = new NetworkAction();
        action3.setActionId("update-my-todos");
        action3.setActionName("更新我的待办");
        action3.setActionType("UPDATE");
        action3.setStatus(NetworkAction.ActionStatus.PENDING);
        actions.add(action3);
        
        NetworkAction action4 = new NetworkAction();
        action4.setActionId("notify-collaborators");
        action4.setActionName("通知协作者");
        action4.setActionType("NOTIFICATION");
        action4.setStatus(NetworkAction.ActionStatus.PENDING);
        actions.add(action4);
        
        process.setNetworkActions(actions);
        
        return process;
    }
}
