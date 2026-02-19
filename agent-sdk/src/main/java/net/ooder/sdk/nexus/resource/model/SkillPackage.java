package net.ooder.sdk.nexus.resource.model;

import java.util.Map;

public class SkillPackage {
    
    private String skillId;
    private String skillName;
    private String version;
    private String description;
    private String packagePath;
    private byte[] packageData;
    private Map<String, Object> metadata;
    
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getPackagePath() { return packagePath; }
    public void setPackagePath(String packagePath) { this.packagePath = packagePath; }
    
    public byte[] getPackageData() { return packageData; }
    public void setPackageData(byte[] packageData) { this.packageData = packageData; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
