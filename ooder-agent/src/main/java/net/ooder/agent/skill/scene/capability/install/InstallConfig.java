package net.ooder.mvp.skill.scene.capability.install;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InstallConfig {
    
    private String installId;
    private String capabilityId;
    private String capabilityName;
    private String driverCondition;
    private String driverConditionName;
    private Participants participants;
    private List<String> optionalCapabilities;
    private InstallStatus status;
    private long createTime;
    private long updateTime;
    private PushType pushType;
    private String creator;
    private Map<String, Object> config;
    
    private String sceneId;
    private String sceneGroupId;
    private List<DependencyInfo> dependencies;
    private List<String> installedCapabilities;
    private int installProgress;
    private String installMessage;
    private String driverConditionConfig;
    private boolean pushed;
    private long pushTime;
    
    private String skillForm;
    private String sceneType;
    private String visibility;
    private List<String> nextSteps;
    
    public enum InstallStatus {
        DRAFT,
        PENDING,
        DOWNLOADING,
        DOWNLOADED,
        INSTALLING,
        INSTALLED,
        PENDING_ACTIVATION,
        ACTIVATED,
        FAILED,
        CANCELLED,
        SCHEDULED,
        RUNNING,
        PAUSED,
        WAITING,
        COMPLETED,
        ARCHIVED,
        DEV,
        PUBLISHING,
        PUBLISHED,
        UNINSTALLING,
        DEACTIVATING
    }
    
    public enum PushType {
        DELEGATE,
        INVITE,
        SHARE
    }
    
    public static class Participants {
        private Participant leader;
        private List<Participant> collaborators;
        
        public Participant getLeader() { return leader; }
        public void setLeader(Participant leader) { this.leader = leader; }
        public List<Participant> getCollaborators() { 
            return collaborators != null ? collaborators : new ArrayList<>(); 
        }
        public void setCollaborators(List<Participant> collaborators) { 
            this.collaborators = collaborators; 
        }
    }
    
    public static class Participant {
        private String userId;
        private String name;
        private String avatar;
        private ParticipantRole role;
        private boolean notified;
        private boolean accepted;
        
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
        public ParticipantRole getRole() { return role; }
        public void setRole(ParticipantRole role) { this.role = role; }
        public boolean isNotified() { return notified; }
        public void setNotified(boolean notified) { this.notified = notified; }
        public boolean isAccepted() { return accepted; }
        public void setAccepted(boolean accepted) { this.accepted = accepted; }
    }
    
    public enum ParticipantRole {
        LEADER,
        COLLABORATOR,
        APPROVER,
        VIEWER
    }
    
    public static class DependencyInfo {
        private String capabilityId;
        private String name;
        private String version;
        private DependencyStatus status;
        private String message;
        
        public enum DependencyStatus {
            PENDING,
            INSTALLING,
            INSTALLED,
            FAILED,
            SKIPPED
        }
        
        public String getCapabilityId() { return capabilityId; }
        public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public DependencyStatus getStatus() { return status; }
        public void setStatus(DependencyStatus status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public String getInstallId() { return installId; }
    public void setInstallId(String installId) { this.installId = installId; }
    public String getCapabilityId() { return capabilityId; }
    public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
    public String getCapabilityName() { return capabilityName; }
    public void setCapabilityName(String capabilityName) { this.capabilityName = capabilityName; }
    public String getDriverCondition() { return driverCondition; }
    public void setDriverCondition(String driverCondition) { this.driverCondition = driverCondition; }
    public String getDriverConditionName() { return driverConditionName; }
    public void setDriverConditionName(String driverConditionName) { this.driverConditionName = driverConditionName; }
    public Participants getParticipants() { return participants; }
    public void setParticipants(Participants participants) { this.participants = participants; }
    public List<String> getOptionalCapabilities() { return optionalCapabilities; }
    public void setOptionalCapabilities(List<String> optionalCapabilities) { this.optionalCapabilities = optionalCapabilities; }
    public InstallStatus getStatus() { return status; }
    public void setStatus(InstallStatus status) { this.status = status; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public long getUpdateTime() { return updateTime; }
    public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
    public PushType getPushType() { return pushType; }
    public void setPushType(PushType pushType) { this.pushType = pushType; }
    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public List<DependencyInfo> getDependencies() { return dependencies != null ? dependencies : new ArrayList<>(); }
    public void setDependencies(List<DependencyInfo> dependencies) { this.dependencies = dependencies; }
    public List<String> getInstalledCapabilities() { return installedCapabilities != null ? installedCapabilities : new ArrayList<>(); }
    public void setInstalledCapabilities(List<String> installedCapabilities) { this.installedCapabilities = installedCapabilities; }
    public int getInstallProgress() { return installProgress; }
    public void setInstallProgress(int installProgress) { this.installProgress = installProgress; }
    public String getInstallMessage() { return installMessage; }
    public void setInstallMessage(String installMessage) { this.installMessage = installMessage; }
    public boolean isPushed() { return pushed; }
    public void setPushed(boolean pushed) { this.pushed = pushed; }
    public long getPushTime() { return pushTime; }
    public void setPushTime(long pushTime) { this.pushTime = pushTime; }
    public String getDriverConditionConfig() { return driverConditionConfig; }
    public void setDriverConditionConfig(String driverConditionConfig) { this.driverConditionConfig = driverConditionConfig; }
    public String getSkillForm() { return skillForm; }
    public void setSkillForm(String skillForm) { this.skillForm = skillForm; }
    public String getSceneType() { return sceneType; }
    public void setSceneType(String sceneType) { this.sceneType = sceneType; }
    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }
    public List<String> getNextSteps() { return nextSteps != null ? nextSteps : new ArrayList<String>(); }
    public void setNextSteps(List<String> nextSteps) { this.nextSteps = nextSteps; }
}
