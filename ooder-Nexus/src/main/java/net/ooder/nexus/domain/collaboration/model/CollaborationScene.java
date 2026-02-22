package net.ooder.nexus.domain.collaboration.model;

import java.util.List;

/**
 * 协作场景模型
 *
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public class CollaborationScene {

    private String sceneId;
    private String name;
    private String description;
    private String ownerId;
    private String ownerName;
    private List<SceneMember> members;
    private List<String> skillIds;
    private SceneStatus status;
    private String sceneKey;
    private long createTime;
    private long updateTime;

    public enum SceneStatus {
        CREATED,
        RUNNING,
        PAUSED,
        STOPPED,
        ARCHIVED
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<SceneMember> getMembers() {
        return members;
    }

    public void setMembers(List<SceneMember> members) {
        this.members = members;
    }

    public List<String> getSkillIds() {
        return skillIds;
    }

    public void setSkillIds(List<String> skillIds) {
        this.skillIds = skillIds;
    }

    public SceneStatus getStatus() {
        return status;
    }

    public void setStatus(SceneStatus status) {
        this.status = status;
    }

    public String getSceneKey() {
        return sceneKey;
    }

    public void setSceneKey(String sceneKey) {
        this.sceneKey = sceneKey;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
