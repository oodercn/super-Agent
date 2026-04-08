package net.ooder.mvp.skill.scene.capability.install;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface InstallService {
    
    InstallConfig createInstall(CreateInstallRequest request);
    
    InstallConfig getInstall(String installId);
    
    InstallConfig updateDriverCondition(String installId, String driverCondition);
    
    InstallConfig addParticipant(String installId, AddParticipantRequest request);
    
    InstallConfig removeParticipant(String installId, String userId);
    
    InstallConfig updateOptionalCapabilities(String installId, List<String> capabilities);
    
    CompletableFuture<InstallConfig> executeInstall(String installId);
    
    InstallProgress getInstallProgress(String installId);
    
    InstallConfig pushToParticipants(String installId, PushRequest request);
    
    List<InstallConfig> listMyInstalls(String userId);
    
    List<InstallConfig> listPendingActivations(String userId);
    
    CompletableFuture<RollbackResult> rollbackInstall(String installId);
    
    RollbackResult getRollbackStatus(String installId);
    
    CompletableFuture<InstallConfig> downloadAndInstall(String skillId, CreateInstallRequest request);
    
    CompletableFuture<InstallConfig> activateSkill(String installId);
    
    CompletableFuture<InstallConfig> deactivateSkill(String installId);
    
    CompletableFuture<InstallConfig> uninstallSkill(String installId);
    
    List<String> listDownloadedSkills();
    
    List<String> listInstalledSkills();
    
    List<String> listActivatedSkills();
    
    public static class CreateInstallRequest {
        private String capabilityId;
        private String driverCondition;
        private Participants participants;
        private List<String> optionalCapabilities;
        private Map<String, Object> config;
        private InstallConfig.PushType pushType;
        
        public String getCapabilityId() { return capabilityId; }
        public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
        public String getDriverCondition() { return driverCondition; }
        public void setDriverCondition(String driverCondition) { this.driverCondition = driverCondition; }
        public Participants getParticipants() { return participants; }
        public void setParticipants(Participants participants) { this.participants = participants; }
        public List<String> getOptionalCapabilities() { return optionalCapabilities; }
        public void setOptionalCapabilities(List<String> optionalCapabilities) { this.optionalCapabilities = optionalCapabilities; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
        public InstallConfig.PushType getPushType() { return pushType; }
        public void setPushType(InstallConfig.PushType pushType) { this.pushType = pushType; }
    }
    
    public static class AddParticipantRequest {
        private InstallConfig.Participant participant;
        
        public InstallConfig.Participant getParticipant() { return participant; }
        public void setParticipant(InstallConfig.Participant participant) { this.participant = participant; }
    }
    
    public static class PushRequest {
        private String message;
        private boolean force;
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public boolean isForce() { return force; }
        public void setForce(boolean force) { this.force = force; }
    }
    
    public static class InstallProgress {
        private String installId;
        private int progress;
        private String currentAction;
        private List<InstallConfig.DependencyInfo> dependencies;
        private List<String> installedCapabilities;
        private InstallConfig.InstallStatus status;
        private String message;
        
        public String getInstallId() { return installId; }
        public void setInstallId(String installId) { this.installId = installId; }
        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }
        public String getCurrentAction() { return currentAction; }
        public void setCurrentAction(String currentAction) { this.currentAction = currentAction; }
        public List<InstallConfig.DependencyInfo> getDependencies() { return dependencies; }
        public void setDependencies(List<InstallConfig.DependencyInfo> dependencies) { this.dependencies = dependencies; }
        public List<String> getInstalledCapabilities() { return installedCapabilities; }
        public void setInstalledCapabilities(List<String> installedCapabilities) { this.installedCapabilities = installedCapabilities; }
        public InstallConfig.InstallStatus getStatus() { return status; }
        public void setStatus(InstallConfig.InstallStatus status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class Participants {
        private InstallConfig.Participant leader;
        private List<InstallConfig.Participant> collaborators;
        
        public InstallConfig.Participant getLeader() { return leader; }
        public void setLeader(InstallConfig.Participant leader) { this.leader = leader; }
        public List<InstallConfig.Participant> getCollaborators() { return collaborators; }
        public void setCollaborators(List<InstallConfig.Participant> collaborators) { this.collaborators = collaborators; }
    }
    
    public static class RollbackResult {
        private String installId;
        private boolean success;
        private List<String> rolledBackCapabilities;
        private List<String> failedRollbacks;
        private String message;
        private long rollbackTime;
        
        public String getInstallId() { return installId; }
        public void setInstallId(String installId) { this.installId = installId; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public List<String> getRolledBackCapabilities() { return rolledBackCapabilities; }
        public void setRolledBackCapabilities(List<String> rolledBackCapabilities) { this.rolledBackCapabilities = rolledBackCapabilities; }
        public List<String> getFailedRollbacks() { return failedRollbacks; }
        public void setFailedRollbacks(List<String> failedRollbacks) { this.failedRollbacks = failedRollbacks; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getRollbackTime() { return rollbackTime; }
        public void setRollbackTime(long rollbackTime) { this.rollbackTime = rollbackTime; }
    }
}
