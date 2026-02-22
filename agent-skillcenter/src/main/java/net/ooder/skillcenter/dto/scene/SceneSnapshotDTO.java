package net.ooder.skillcenter.dto.scene;

import java.util.Map;

public class SceneSnapshotDTO {
    private String snapshotId;
    private String sceneId;
    private String version;
    private Map<String, Object> data;
    private long createTime;
    private String description;

    public String getSnapshotId() { return snapshotId; }
    public void setSnapshotId(String snapshotId) { this.snapshotId = snapshotId; }
    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
