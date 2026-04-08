package net.ooder.mvp.skill.scene.config;

import java.util.Map;
import java.util.List;

public class ConfigVersion {
    private String versionId;
    private String sceneId;
    private Map<String, Object> config;
    private String operator;
    private long createTime;
    private String description;
    private String version;
    private boolean active;

    public String getVersionId() { return versionId; }
    public void setVersionId(String versionId) { this.versionId = versionId; }
    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
