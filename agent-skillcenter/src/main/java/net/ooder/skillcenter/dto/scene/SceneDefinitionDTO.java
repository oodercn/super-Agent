package net.ooder.skillcenter.dto.scene;

import java.util.List;
import java.util.Map;

public class SceneDefinitionDTO {
    private String sceneId;
    private String name;
    private String description;
    private String version;
    private String type;
    private String scenePrefix;
    private List<CapabilityDTO> capabilities;
    private List<String> collaborativeScenes;
    private Map<String, Object> config;
    private Map<String, Object> vfsConfig;
    private Map<String, Object> authConfig;
    private long createTime;
    private long updateTime;

    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getScenePrefix() { return scenePrefix; }
    public void setScenePrefix(String scenePrefix) { this.scenePrefix = scenePrefix; }
    public List<CapabilityDTO> getCapabilities() { return capabilities; }
    public void setCapabilities(List<CapabilityDTO> capabilities) { this.capabilities = capabilities; }
    public List<String> getCollaborativeScenes() { return collaborativeScenes; }
    public void setCollaborativeScenes(List<String> collaborativeScenes) { this.collaborativeScenes = collaborativeScenes; }
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
    public Map<String, Object> getVfsConfig() { return vfsConfig; }
    public void setVfsConfig(Map<String, Object> vfsConfig) { this.vfsConfig = vfsConfig; }
    public Map<String, Object> getAuthConfig() { return authConfig; }
    public void setAuthConfig(Map<String, Object> authConfig) { this.authConfig = authConfig; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public long getUpdateTime() { return updateTime; }
    public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
}
