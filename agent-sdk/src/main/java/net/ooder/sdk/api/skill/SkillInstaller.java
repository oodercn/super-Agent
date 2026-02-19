
package net.ooder.sdk.api.skill;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.common.enums.SceneType;

public interface SkillInstaller {
    
    CompletableFuture<InstallResult> install(SkillPackage skillPackage, InstallMode mode);
    
    CompletableFuture<InstallResult> installWithDependencies(SkillPackage skillPackage, InstallMode mode);
    
    CompletableFuture<UninstallResult> uninstall(String skillId, boolean removeData);
    
    CompletableFuture<UpdateResult> update(String skillId, String targetVersion);
    
    CompletableFuture<RollbackResult> rollback(String skillId, String targetVersion);
    
    CompletableFuture<ValidateResult> validate(SkillPackage skillPackage);
    
    CompletableFuture<List<String>> checkDependencies(SkillPackage skillPackage);
    
    CompletableFuture<DependencyInfo> checkDependencyInfo(String skillId);
    
    CompletableFuture<Void> installDependencies(String skillId);
    
    CompletableFuture<Void> prepareInstallEnvironment(SkillPackage skillPackage);
    
    CompletableFuture<Void> cleanupInstallEnvironment(String skillId);
    
    CompletableFuture<InstallProgress> getProgress(String skillId);
    
    enum InstallMode {
        NORMAL,
        FORCE,
        UPGRADE
    }
    
    class InstallProgress {
        private String installId;
        private String skillId;
        private String stage;
        private String status;
        private int progress;
        private String message;
        private long startTime;
        private long timestamp;
        
        public String getInstallId() { return installId; }
        public void setInstallId(String installId) { this.installId = installId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getStage() { return stage; }
        public void setStage(String stage) { this.stage = stage; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        
        @Deprecated
        public String getPhase() { return stage; }
        @Deprecated
        public void setPhase(String phase) { this.stage = phase; }
        @Deprecated
        public int getPercentage() { return progress; }
        @Deprecated
        public void setPercentage(int percentage) { this.progress = percentage; }
    }
    
    class ValidateResult {
        private boolean valid;
        private List<String> errors;
        private List<String> warnings;
        
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    }
    
    class RollbackResult {
        private String skillId;
        private boolean success;
        private String previousVersion;
        private String currentVersion;
        private String error;
        
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getPreviousVersion() { return previousVersion; }
        public void setPreviousVersion(String previousVersion) { this.previousVersion = previousVersion; }
        public String getCurrentVersion() { return currentVersion; }
        public void setCurrentVersion(String currentVersion) { this.currentVersion = currentVersion; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}
