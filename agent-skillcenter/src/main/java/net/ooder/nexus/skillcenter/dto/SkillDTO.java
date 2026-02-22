package net.ooder.nexus.skillcenter.dto;

import java.util.List;

/**
 * 技能数据传输对象 - 符合v0.7.0协议规范
 */
public class SkillDTO extends BaseDTO {
    
    private String name;
    private String description;
    private String category;
    private String status;
    private String version;
    private String author;
    private boolean available;
    private int downloadCount;
    private double rating;
    
    private String type;
    private List<String> capabilities;
    private List<String> scenes;
    private String endpoint;
    private String downloadUrl;
    private String homepage;
    private String repository;
    private String license;

    public SkillDTO() {}
    
    public SkillDTO(String id, String name, String description, String category, String status) {
        super(id);
        this.name = name;
        this.description = description;
        this.category = category;
        this.status = status;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public int getDownloadCount() { return downloadCount; }
    public void setDownloadCount(int downloadCount) { this.downloadCount = downloadCount; }
    
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

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

    public String getHomepage() { return homepage; }
    public void setHomepage(String homepage) { this.homepage = homepage; }

    public String getRepository() { return repository; }
    public void setRepository(String repository) { this.repository = repository; }

    public String getLicense() { return license; }
    public void setLicense(String license) { this.license = license; }
}
