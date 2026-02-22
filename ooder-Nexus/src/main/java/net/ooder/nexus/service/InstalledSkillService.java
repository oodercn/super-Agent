package net.ooder.nexus.service;

import net.ooder.sdk.api.skill.InstalledSkill;
import net.ooder.sdk.common.enums.SkillStatus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 已安装技能服务接口
 * 基于 SDK 0.7.1 的 InstalledSkill 模型
 *
 * @author ooder Team
 * @version 0.7.1
 */
public interface InstalledSkillService {

    List<InstalledSkill> getAllInstalledSkills();

    List<InstalledSkill> getSkillsByStatus(SkillStatus status);

    InstalledSkill getSkillById(String skillId);

    CompletableFuture<InstalledSkill> installSkill(String skillId, String version, Map<String, String> config);

    CompletableFuture<InstalledSkill> installSkillFromPackage(String skillId, String name, String version, String downloadUrl, String source, Map<String, String> config);

    CompletableFuture<Boolean> uninstallSkill(String skillId);

    CompletableFuture<InstalledSkill> updateSkill(String skillId, String version);

    CompletableFuture<Void> startSkill(String skillId);

    CompletableFuture<Void> stopSkill(String skillId);

    SkillStatus getSkillStatus(String skillId);

    CompletableFuture<Boolean> testConnection(String skillId);

    void updateConfig(String skillId, Map<String, String> config);

    Map<String, String> getSkillConfig(String skillId);

    SkillStatistics getStatistics();

    CompletableFuture<Void> pauseSkill(String skillId);

    CompletableFuture<Void> resumeSkill(String skillId);

    CompletableFuture<Void> restartSkill(String skillId);

    InstallProgress getInstallProgress(String installId);

    List<InstallProgress> getActiveInstalls();

    DependencyInfo getDependencies(String skillId);

    CompletableFuture<Void> installDependencies(String skillId);

    CompletableFuture<Void> updateDependencies(String skillId);

    class SkillStatistics {
        private int totalSkills;
        private int runningSkills;
        private int stoppedSkills;
        private int errorSkills;

        public int getTotalSkills() { return totalSkills; }
        public void setTotalSkills(int totalSkills) { this.totalSkills = totalSkills; }
        public int getRunningSkills() { return runningSkills; }
        public void setRunningSkills(int runningSkills) { this.runningSkills = runningSkills; }
        public int getStoppedSkills() { return stoppedSkills; }
        public void setStoppedSkills(int stoppedSkills) { this.stoppedSkills = stoppedSkills; }
        public int getErrorSkills() { return errorSkills; }
        public void setErrorSkills(int errorSkills) { this.errorSkills = errorSkills; }
    }

    class InstallProgress {
        private String installId;
        private String skillId;
        private String skillName;
        private String stage;
        private int progress;
        private String status;
        private String message;
        private long startTime;

        public String getInstallId() { return installId; }
        public void setInstallId(String installId) { this.installId = installId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getSkillName() { return skillName; }
        public void setSkillName(String skillName) { this.skillName = skillName; }
        public String getStage() { return stage; }
        public void setStage(String stage) { this.stage = stage; }
        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
    }

    class DependencyInfo {
        private String skillId;
        private List<DependencyItem> dependencies;
        private List<DependencyItem> missing;
        private List<DependencyItem> outdated;
        private boolean satisfied;

        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public List<DependencyItem> getDependencies() { return dependencies; }
        public void setDependencies(List<DependencyItem> dependencies) { this.dependencies = dependencies; }
        public List<DependencyItem> getMissing() { return missing; }
        public void setMissing(List<DependencyItem> missing) { this.missing = missing; }
        public List<DependencyItem> getOutdated() { return outdated; }
        public void setOutdated(List<DependencyItem> outdated) { this.outdated = outdated; }
        public boolean isSatisfied() { return satisfied; }
        public void setSatisfied(boolean satisfied) { this.satisfied = satisfied; }
    }

    class DependencyItem {
        private String name;
        private String version;
        private String requiredVersion;
        private String installedVersion;
        private String status;
        private boolean required;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getRequiredVersion() { return requiredVersion; }
        public void setRequiredVersion(String requiredVersion) { this.requiredVersion = requiredVersion; }
        public String getInstalledVersion() { return installedVersion; }
        public void setInstalledVersion(String installedVersion) { this.installedVersion = installedVersion; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
    }
}
