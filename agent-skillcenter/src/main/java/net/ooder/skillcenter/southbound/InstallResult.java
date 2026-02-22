package net.ooder.skillcenter.southbound;

public class InstallResult {
    private boolean success;
    private String skillId;
    private String errorMessage;
    private long duration;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
}
