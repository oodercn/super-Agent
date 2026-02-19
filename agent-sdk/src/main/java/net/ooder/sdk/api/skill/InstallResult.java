
package net.ooder.sdk.api.skill;

import java.util.List;

public class InstallResult {
    
    private String skillId;
    private boolean success;
    private String version;
    private String installPath;
    private List<String> installedDependencies;
    private List<String> joinedScenes;
    private String error;
    private long duration;
    
    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getInstallPath() {
        return installPath;
    }
    
    public void setInstallPath(String installPath) {
        this.installPath = installPath;
    }
    
    public List<String> getInstalledDependencies() {
        return installedDependencies;
    }
    
    public void setInstalledDependencies(List<String> installedDependencies) {
        this.installedDependencies = installedDependencies;
    }
    
    public List<String> getJoinedScenes() {
        return joinedScenes;
    }
    
    public void setJoinedScenes(List<String> joinedScenes) {
        this.joinedScenes = joinedScenes;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public long getDuration() {
        return duration;
    }
    
    public void setDuration(long duration) {
        this.duration = duration;
    }
}
