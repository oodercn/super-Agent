package net.ooder.skillcenter.market;

import java.util.List;

/**
 * 技能列表项，用于在技能市场中展示技能信息 - 符合v0.7.0协议规范
 */
public class SkillListing {
    private String skillId;
    private String name;
    private String description;
    private String category;
    private String version;
    private String downloadUrl;
    private String author;
    private int downloadCount;
    private double rating;
    private int reviewCount;
    private long lastUpdated;
    
    private String type;
    private List<String> capabilities;
    private List<String> scenes;
    private String endpoint;
    private String homepage;
    private String repository;
    private String license;

    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getDownloadUrl() {
        return downloadUrl;
    }
    
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public int getDownloadCount() {
        return downloadCount;
    }
    
    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public int getReviewCount() {
        return reviewCount;
    }
    
    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
    
    public long getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }

    public List<String> getScenes() {
        return scenes;
    }

    public void setScenes(List<String> scenes) {
        this.scenes = scenes;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
