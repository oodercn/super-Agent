package net.ooder.skill.discovery.dto.discovery;

public class PluginDetailDTO {
    private String id;
    private String name;
    private String description;
    private String version;
    private String category;
    private String author;
    private Boolean installed;
    private Boolean running;
    private String status;
    private String icon;
    private Long installTime;
    private String location;
    private String license;
    private String homepage;
    private String repository;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public Boolean getInstalled() { return installed; }
    public void setInstalled(Boolean installed) { this.installed = installed; }
    public Boolean getRunning() { return running; }
    public void setRunning(Boolean running) { this.running = running; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Long getInstallTime() { return installTime; }
    public void setInstallTime(Long installTime) { this.installTime = installTime; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getLicense() { return license; }
    public void setLicense(String license) { this.license = license; }
    public String getHomepage() { return homepage; }
    public void setHomepage(String homepage) { this.homepage = homepage; }
    public String getRepository() { return repository; }
    public void setRepository(String repository) { this.repository = repository; }
}
