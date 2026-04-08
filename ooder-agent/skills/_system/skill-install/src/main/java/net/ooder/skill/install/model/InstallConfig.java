package net.ooder.skill.install.model;

public class InstallConfig {
    
    private String installId;
    private String capabilityId;
    private String skillId;
    private InstallStatus status;
    private String driverCondition;
    private Participant leader;
    private java.util.List<Participant> collaborators;
    private java.util.List<String> optionalCapabilities;
    private java.util.List<DependencyInfo> dependencies;
    private java.util.Map<String, Object> config;
    private PushType pushType;
    private long createTime;
    private long updateTime;
    private String installPath;
    private String installSource;

    public enum InstallStatus {
        PENDING, DOWNLOADING, INSTALLING, INSTALLED, ACTIVATED, FAILED, ROLLBACK
    }

    public enum PushType {
        MANUAL, AUTO, SCHEDULED
    }

    public static class Participant {
        private String userId;
        private String name;
        private String role;
        private java.util.List<String> capabilities;

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public java.util.List<String> getCapabilities() { return capabilities; }
        public void setCapabilities(java.util.List<String> capabilities) { this.capabilities = capabilities; }
    }

    public static class DependencyInfo {
        private String capabilityId;
        private String name;
        private String version;
        private DependencyStatus status;

        public enum DependencyStatus {
            PENDING, RESOLVING, RESOLVED, FAILED, SKIPPED
        }

        public String getCapabilityId() { return capabilityId; }
        public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public DependencyStatus getStatus() { return status; }
        public void setStatus(DependencyStatus status) { this.status = status; }
    }

    public String getInstallId() { return installId; }
    public void setInstallId(String installId) { this.installId = installId; }
    public String getCapabilityId() { return capabilityId; }
    public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public InstallStatus getStatus() { return status; }
    public void setStatus(InstallStatus status) { this.status = status; }
    public String getDriverCondition() { return driverCondition; }
    public void setDriverCondition(String driverCondition) { this.driverCondition = driverCondition; }
    public Participant getLeader() { return leader; }
    public void setLeader(Participant leader) { this.leader = leader; }
    public java.util.List<Participant> getCollaborators() { return collaborators; }
    public void setCollaborators(java.util.List<Participant> collaborators) { this.collaborators = collaborators; }
    public java.util.List<String> getOptionalCapabilities() { return optionalCapabilities; }
    public void setOptionalCapabilities(java.util.List<String> optionalCapabilities) { this.optionalCapabilities = optionalCapabilities; }
    public java.util.List<DependencyInfo> getDependencies() { return dependencies; }
    public void setDependencies(java.util.List<DependencyInfo> dependencies) { this.dependencies = dependencies; }
    public java.util.Map<String, Object> getConfig() { return config; }
    public void setConfig(java.util.Map<String, Object> config) { this.config = config; }
    public PushType getPushType() { return pushType; }
    public void setPushType(PushType pushType) { this.pushType = pushType; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public long getUpdateTime() { return updateTime; }
    public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
    public String getInstallPath() { return installPath; }
    public void setInstallPath(String installPath) { this.installPath = installPath; }
    public String getInstallSource() { return installSource; }
    public void setInstallSource(String installSource) { this.installSource = installSource; }
}
