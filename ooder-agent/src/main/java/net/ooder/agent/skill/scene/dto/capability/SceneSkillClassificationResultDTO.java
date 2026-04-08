package net.ooder.mvp.skill.scene.dto.capability;

public class SceneSkillClassificationResultDTO {
    
    private String capabilityId;
    private String skillForm;
    private String sceneType;
    private int businessSemanticsScore;
    private boolean hasSelfDrive;
    private boolean hasBusinessSemantics;
    private boolean isValidSceneSkill;
    
    public String getCapabilityId() { return capabilityId; }
    public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
    public String getSkillForm() { return skillForm; }
    public void setSkillForm(String skillForm) { this.skillForm = skillForm; }
    public String getSceneType() { return sceneType; }
    public void setSceneType(String sceneType) { this.sceneType = sceneType; }
    public int getBusinessSemanticsScore() { return businessSemanticsScore; }
    public void setBusinessSemanticsScore(int businessSemanticsScore) { this.businessSemanticsScore = businessSemanticsScore; }
    public boolean isHasSelfDrive() { return hasSelfDrive; }
    public void setHasSelfDrive(boolean hasSelfDrive) { this.hasSelfDrive = hasSelfDrive; }
    public boolean isHasBusinessSemantics() { return hasBusinessSemantics; }
    public void setHasBusinessSemantics(boolean hasBusinessSemantics) { this.hasBusinessSemantics = hasBusinessSemantics; }
    public boolean isValidSceneSkill() { return isValidSceneSkill; }
    public void setValidSceneSkill(boolean validSceneSkill) { isValidSceneSkill = validSceneSkill; }
    
    public static SceneSkillClassificationResultDTO from(String capabilityId, String skillForm, String sceneType, Integer score) {
        SceneSkillClassificationResultDTO dto = new SceneSkillClassificationResultDTO();
        dto.setCapabilityId(capabilityId);
        dto.setSkillForm(skillForm);
        dto.setSceneType(sceneType);
        int scoreValue = score != null ? score : 0;
        dto.setBusinessSemanticsScore(scoreValue);
        dto.setHasSelfDrive("AUTO".equals(sceneType));
        dto.setHasBusinessSemantics(scoreValue > 0);
        dto.setValidSceneSkill(true);
        return dto;
    }
}
