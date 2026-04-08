package net.ooder.skill.discovery.dto.discovery;

public class LocalDiscoveryConfigDTO {
    private String skillsPath;
    private boolean includeSubdirectories;
    private boolean forceRefresh;
    private String category;
    private String skillForm;
    private String sceneType;
    private String skillCategory;

    public String getSkillsPath() {
        return skillsPath;
    }

    public void setSkillsPath(String skillsPath) {
        this.skillsPath = skillsPath;
    }

    public boolean isIncludeSubdirectories() {
        return includeSubdirectories;
    }

    public void setIncludeSubdirectories(boolean includeSubdirectories) {
        this.includeSubdirectories = includeSubdirectories;
    }

    public boolean isForceRefresh() {
        return forceRefresh;
    }

    public void setForceRefresh(boolean forceRefresh) {
        this.forceRefresh = forceRefresh;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSkillForm() {
        return skillForm;
    }

    public void setSkillForm(String skillForm) {
        this.skillForm = skillForm;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    public String getSkillCategory() {
        return skillCategory;
    }

    public void setSkillCategory(String skillCategory) {
        this.skillCategory = skillCategory;
    }
}
