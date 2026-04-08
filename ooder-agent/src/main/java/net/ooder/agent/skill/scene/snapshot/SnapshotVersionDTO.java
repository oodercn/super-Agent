package net.ooder.mvp.skill.scene.snapshot;

import java.util.Map;

public class SnapshotVersionDTO {
    private String versionId;
    private String snapshotId;
    private String sceneGroupId;
    private String versionName;
    private String versionNumber;
    private String description;
    private String createdBy;
    private long createTime;
    private long dataSize;
    private String checksum;
    private Map<String, Object> metadata;
    private String parentVersionId;
    private boolean isLatest;
    
    public String getVersionId() { return versionId; }
    public void setVersionId(String versionId) { this.versionId = versionId; }
    public String getSnapshotId() { return snapshotId; }
    public void setSnapshotId(String snapshotId) { this.snapshotId = snapshotId; }
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getVersionName() { return versionName; }
    public void setVersionName(String versionName) { this.versionName = versionName; }
    public String getVersionNumber() { return versionNumber; }
    public void setVersionNumber(String versionNumber) { this.versionNumber = versionNumber; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public long getDataSize() { return dataSize; }
    public void setDataSize(long dataSize) { this.dataSize = dataSize; }
    public String getChecksum() { return checksum; }
    public void setChecksum(String checksum) { this.checksum = checksum; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    public String getParentVersionId() { return parentVersionId; }
    public void setParentVersionId(String parentVersionId) { this.parentVersionId = parentVersionId; }
    public boolean isLatest() { return isLatest; }
    public void setLatest(boolean latest) { isLatest = latest; }
}
