package net.ooder.skillcenter.dto.scene;

public class SkillInfoDTO {
    private String skillId;
    private String name;
    private String version;
    private String description;
    private String author;
    private String category;
    private String status;
    private Long installedAt;
    private Long updatedAt;

    public SkillInfoDTO() {}

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getInstalledAt() { return installedAt; }
    public void setInstalledAt(Long installedAt) { this.installedAt = installedAt; }
    public Long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Long updatedAt) { this.updatedAt = updatedAt; }
}
