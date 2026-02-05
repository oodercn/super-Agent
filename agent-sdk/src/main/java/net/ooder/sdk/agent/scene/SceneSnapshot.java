package net.ooder.sdk.agent.scene;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

/**
 * 场景快照类，用于保存场景的历史状态
 */
public class SceneSnapshot implements Serializable {
    private static final long serialVersionUID = 1L;

    private String snapshotId;
    private String sceneId;
    private Date snapshotTimestamp;
    private String createdBy;
    private String description;
    private SceneState sceneState;
    private SceneDefinition sceneDefinition;
    private Map<String, SceneMember> sceneMembers;
    private Map<String, Object> metadata;

    public SceneSnapshot() {
        this.snapshotTimestamp = new Date();
        this.metadata = new HashMap<>();
        this.sceneMembers = new HashMap<>();
    }

    public SceneSnapshot(String sceneId, SceneState sceneState, SceneDefinition sceneDefinition, 
                       Map<String, SceneMember> members) {
        this();
        this.sceneId = sceneId;
        this.sceneState = sceneState;
        this.sceneDefinition = sceneDefinition;
        this.sceneMembers = new HashMap<>(members);
        this.snapshotId = generateSnapshotId();
    }

    /**
     * 生成快照ID
     */
    private String generateSnapshotId() {
        return "snapshot_" + sceneId + "_" + System.currentTimeMillis();
    }

    // Getters and setters
    public String getSnapshotId() { return snapshotId; }
    public void setSnapshotId(String snapshotId) { this.snapshotId = snapshotId; }

    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }

    public Date getSnapshotTimestamp() { return snapshotTimestamp; }
    public void setSnapshotTimestamp(Date snapshotTimestamp) { this.snapshotTimestamp = snapshotTimestamp; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public SceneState getSceneState() { return sceneState; }
    public void setSceneState(SceneState sceneState) { this.sceneState = sceneState; }

    public SceneDefinition getSceneDefinition() { return sceneDefinition; }
    public void setSceneDefinition(SceneDefinition sceneDefinition) { this.sceneDefinition = sceneDefinition; }

    public Map<String, SceneMember> getSceneMembers() { return sceneMembers; }
    public void setSceneMembers(Map<String, SceneMember> sceneMembers) {
        this.sceneMembers = sceneMembers != null ? new HashMap<>(sceneMembers) : new HashMap<>();
    }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata != null ? metadata : new HashMap<>();
    }

    /**
     * 设置快照元数据
     */
    public void setMetadata(String key, Object value) {
        if (key != null) {
            this.metadata.put(key, value);
        }
    }

    /**
     * 获取快照元数据
     */
    @SuppressWarnings("unchecked")
    public <T> T getMetadata(String key, T defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        Object value = this.metadata.get(key);
        return value != null ? (T) value : defaultValue;
    }

    /**
     * 检查快照是否包含场景定义
     */
    public boolean hasSceneDefinition() {
        return sceneDefinition != null;
    }

    /**
     * 检查快照是否包含场景状态
     */
    public boolean hasSceneState() {
        return sceneState != null;
    }

    /**
     * 检查快照是否包含场景成员
     */
    public boolean hasSceneMembers() {
        return sceneMembers != null && !sceneMembers.isEmpty();
    }

    @Override
    public String toString() {
        return "SceneSnapshot{" +
                "snapshotId='" + snapshotId + '\'' +
                ", sceneId='" + sceneId + '\'' +
                ", snapshotTimestamp=" + snapshotTimestamp +
                ", description='" + description + '\'' +
                ", sceneState=" + sceneState +
                ", membersCount=" + (sceneMembers != null ? sceneMembers.size() : 0) +
                '}';
    }
}
