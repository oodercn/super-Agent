package net.ooder.nexus.skillcenter.dto.skill;

import java.util.List;

/**
 * 技能搜索结果DTO - 符合v0.7.0协议规范
 */
public class SkillSearchResultDTO {
    
    private int total;
    private List<SkillListItemDTO> skills;

    public SkillSearchResultDTO() {}

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<SkillListItemDTO> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillListItemDTO> skills) {
        this.skills = skills;
    }

    public static class SkillListItemDTO {
        private String id;
        private String name;
        private String version;
        private String type;
        private List<String> capabilities;
        private List<String> scenes;
        private String endpoint;
        private String downloadUrl;
        private String description;
        private String author;
        private double rating;
        private int downloadCount;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public List<String> getCapabilities() { return capabilities; }
        public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
        public List<String> getScenes() { return scenes; }
        public void setScenes(List<String> scenes) { this.scenes = scenes; }
        public String getEndpoint() { return endpoint; }
        public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
        public String getDownloadUrl() { return downloadUrl; }
        public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public double getRating() { return rating; }
        public void setRating(double rating) { this.rating = rating; }
        public int getDownloadCount() { return downloadCount; }
        public void setDownloadCount(int downloadCount) { this.downloadCount = downloadCount; }
    }
}
