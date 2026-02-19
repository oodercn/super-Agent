
package net.ooder.sdk.api.skill;

public class UpdateResult {
    
    private String skillId;
    private boolean success;
    private String previousVersion;
    private String newVersion;
    private String error;
    
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
    
    public String getPreviousVersion() {
        return previousVersion;
    }
    
    public void setPreviousVersion(String previousVersion) {
        this.previousVersion = previousVersion;
    }
    
    public String getNewVersion() {
        return newVersion;
    }
    
    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
}
