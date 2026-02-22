package net.ooder.nexus.service;

import java.util.Map;

/**
 * 技能包信息
 */
public class SkillPackage {
    
    private String packageId;
    private String name;
    private String version;
    private String description;
    private String downloadUrl;
    private byte[] packageData;
    private Map<String, Object> metadata;

    public SkillPackage() {}

    public String getPackageId() { return packageId; }
    public void setPackageId(String packageId) { this.packageId = packageId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
    
    public byte[] getPackageData() { return packageData; }
    public void setPackageData(byte[] packageData) { this.packageData = packageData; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
