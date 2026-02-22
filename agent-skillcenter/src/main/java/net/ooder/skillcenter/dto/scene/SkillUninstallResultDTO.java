package net.ooder.skillcenter.dto.scene;

public class SkillUninstallResultDTO {
    private boolean success;
    private String error;
    private String skillId;

    public SkillUninstallResultDTO() {}

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }

    public static SkillUninstallResultDTO success(String skillId) {
        SkillUninstallResultDTO result = new SkillUninstallResultDTO();
        result.setSuccess(true);
        result.setSkillId(skillId);
        return result;
    }

    public static SkillUninstallResultDTO failure(String error) {
        SkillUninstallResultDTO result = new SkillUninstallResultDTO();
        result.setSuccess(false);
        result.setError(error);
        return result;
    }
}
