package net.ooder.skillcenter.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "skill_packages")
public class SkillPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skill_id", unique = true, nullable = false)
    private String skillId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "author")
    private String author;

    @Column(name = "license")
    private String license;

    @Column(name = "homepage")
    private String homepage;

    @Column(name = "repository")
    private String repository;

    @Column(name = "documentation")
    private String documentation;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "source_type")
    private String sourceType; // github, local, etc.

    @Column(name = "source_url")
    private String sourceUrl; // Full GitHub URL

    @Column(name = "branch")
    private String branch; // GitHub branch

    @Column(name = "build_status")
    private String buildStatus; // pending, success, failed

    @Column(name = "last_sync_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSyncAt;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "checksum")
    private String checksum;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToMany(mappedBy = "skillPackage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Capability> capabilities;

    @OneToMany(mappedBy = "skillPackage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SceneInfo> scenes;

    @OneToMany(mappedBy = "skillPackage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SkillConfig> configs;

    @OneToMany(mappedBy = "skillPackage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SkillEndpoint> endpoints;

    @ElementCollection
    @CollectionTable(name = "skill_keywords", joinColumns = @JoinColumn(name = "skill_id"))
    @Column(name = "keyword")
    private List<String> keywords;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
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

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Capability> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<Capability> capabilities) {
        this.capabilities = capabilities;
    }

    public List<SceneInfo> getScenes() {
        return scenes;
    }

    public void setScenes(List<SceneInfo> scenes) {
        this.scenes = scenes;
    }

    public List<SkillConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<SkillConfig> configs) {
        this.configs = configs;
    }

    public List<SkillEndpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<SkillEndpoint> endpoints) {
        this.endpoints = endpoints;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(String buildStatus) {
        this.buildStatus = buildStatus;
    }

    public Date getLastSyncAt() {
        return lastSyncAt;
    }

    public void setLastSyncAt(Date lastSyncAt) {
        this.lastSyncAt = lastSyncAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

}
