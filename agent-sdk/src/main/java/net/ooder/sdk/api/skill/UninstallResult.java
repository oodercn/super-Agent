
package net.ooder.sdk.api.skill;

public class UninstallResult {
    
    private String skillId;
    private boolean success;
    private boolean dataRemoved;
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
    
    public boolean isDataRemoved() {
        return dataRemoved;
    }
    
    public void setDataRemoved(boolean dataRemoved) {
        this.dataRemoved = dataRemoved;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
}
