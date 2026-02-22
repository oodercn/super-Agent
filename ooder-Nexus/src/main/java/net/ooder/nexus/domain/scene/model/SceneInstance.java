package net.ooder.nexus.domain.scene.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SceneInstance {
    private String instanceId;
    private String sceneId;
    private String sceneName;
    private String instanceName;
    private String description;
    private String status;
    private String ownerId;
    private String ownerName;
    private List<SceneMemberInfo> members;
    private String inviteCode;
    private SceneConfig config;
    private Date createdAt;
    private Date updatedAt;

    public SceneInstance() {}

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<SceneMemberInfo> getMembers() {
        return members;
    }

    public void setMembers(List<SceneMemberInfo> members) {
        this.members = members;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public SceneConfig getConfig() {
        return config;
    }

    public void setConfig(SceneConfig config) {
        this.config = config;
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
}
