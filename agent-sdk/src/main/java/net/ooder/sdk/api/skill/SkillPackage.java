
package net.ooder.sdk.api.skill;

import java.util.List;
import java.util.Map;

public class SkillPackage {
    
    private String skillId;
    private String name;
    private String description;
    private String version;
    private String sceneId;
    private String source;
    private String downloadUrl;
    private String checksum;
    private long size;
    private List<Capability> capabilities;
    private List<String> dependencies;
    private Map<String, Object> metadata;
    private SkillManifest manifest;
    
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
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getSceneId() {
        return sceneId;
    }
    
    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getDownloadUrl() {
        return downloadUrl;
    }
    
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    
    public String getChecksum() {
        return checksum;
    }
    
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
    
    public long getSize() {
        return size;
    }
    
    public void setSize(long size) {
        this.size = size;
    }
    
    public List<Capability> getCapabilities() {
        return capabilities;
    }
    
    public void setCapabilities(List<Capability> capabilities) {
        this.capabilities = capabilities;
    }
    
    public List<String> getDependencies() {
        return dependencies;
    }
    
    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public SkillManifest getManifest() {
        return manifest;
    }
    
    public void setManifest(SkillManifest manifest) {
        this.manifest = manifest;
    }
}
