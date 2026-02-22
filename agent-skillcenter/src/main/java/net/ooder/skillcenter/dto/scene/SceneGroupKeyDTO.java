package net.ooder.skillcenter.dto.scene;

public class SceneGroupKeyDTO {
    private String keyId;
    private String sceneGroupId;
    private String keyData;
    private long createTime;

    public String getKeyId() { return keyId; }
    public void setKeyId(String keyId) { this.keyId = keyId; }
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getKeyData() { return keyData; }
    public void setKeyData(String keyData) { this.keyData = keyData; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
}
