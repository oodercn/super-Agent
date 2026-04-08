package net.ooder.skill.discovery.dto.discovery;

public class DirectoryTreeChildDTO {
    
    private String skillId;
    private String name;
    private String absolutePath;
    private String category;
    private String skillForm;

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAbsolutePath() { return absolutePath; }
    public void setAbsolutePath(String absolutePath) { this.absolutePath = absolutePath; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSkillForm() { return skillForm; }
    public void setSkillForm(String skillForm) { this.skillForm = skillForm; }
}
