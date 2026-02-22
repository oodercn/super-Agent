package net.ooder.skillcenter.dto.scene;

public class SkillInstallResultDTO {
    private boolean success;
    private String error;
    private String skillId;
    private Integer progress;

    public SkillInstallResultDTO() {}

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }

    public static SkillInstallResultDTO success(String skillId) {
        SkillInstallResultDTO result = new SkillInstallResultDTO();
        result.setSuccess(true);
        result.setSkillId(skillId);
        return result;
    }

    public static SkillInstallResultDTO failure(String error) {
        SkillInstallResultDTO result = new SkillInstallResultDTO();
        result.setSuccess(false);
        result.setError(error);
        return result;
    }
}
