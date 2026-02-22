package net.ooder.nexus.core.storage.vfs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalFileVersion implements FileVersion {
    private String versionID;
    private String fileId;
    private String fileObjectId;
    private String sourceId;
    private Long createTime;
    private Long updateTime;
    private String personId;
    private String hash;
    private File file;
    
    private String versionNumber;
    private String description;
    private String changeNote;
    private Long size;
    private String status;
    private String parentVersionId;
    private String mimeType;
    private List<String> tags;
    private Map<String, Object> metadata;

    public LocalFileVersion(File file, String hash) {
        this.file = file;
        this.hash = hash;
        this.versionID = file.getAbsolutePath();
        this.createTime = System.currentTimeMillis();
        this.updateTime = System.currentTimeMillis();
        this.size = file.length();
        this.status = "active";
        this.tags = new ArrayList<>();
        this.metadata = new HashMap<>();
    }

    @Override
    public String getVersionID() {
        return versionID;
    }

    @Override
    public String getFileId() {
        return fileId;
    }

    @Override
    public String getFileObjectId() {
        return fileObjectId;
    }

    @Override
    public String getSourceId() {
        return sourceId;
    }

    @Override
    public Long getCreateTime() {
        return createTime;
    }

    @Override
    public Long getUpdateTime() {
        return updateTime;
    }

    @Override
    public String getPersonId() {
        return personId;
    }

    @Override
    public String getHash() {
        return hash;
    }

    @Override
    public FileObject getFileObject() {
        return new LocalFileObject(file, hash);
    }

    @Override
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    @Override
    public void setFileObjectId(String fileObjectId) {
        this.fileObjectId = fileObjectId;
    }

    @Override
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String getVersionNumber() {
        return versionNumber;
    }

    @Override
    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getChangeNote() {
        return changeNote;
    }

    @Override
    public void setChangeNote(String changeNote) {
        this.changeNote = changeNote;
    }

    @Override
    public Long getSize() {
        return size;
    }

    @Override
    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getParentVersionId() {
        return parentVersionId;
    }

    @Override
    public void setParentVersionId(String parentVersionId) {
        this.parentVersionId = parentVersionId;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @Override
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean isActive() {
        return "active".equals(status);
    }

    @Override
    public boolean isArchived() {
        return "archived".equals(status);
    }

    @Override
    public void archive() {
        this.status = "archived";
        this.updateTime = System.currentTimeMillis();
    }

    @Override
    public void restore() {
        this.status = "active";
        this.updateTime = System.currentTimeMillis();
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("versionID", versionID);
        map.put("fileId", fileId);
        map.put("versionNumber", versionNumber);
        map.put("description", description);
        map.put("changeNote", changeNote);
        map.put("size", size);
        map.put("status", status);
        map.put("createTime", createTime);
        map.put("updateTime", updateTime);
        map.put("personId", personId);
        map.put("hash", hash);
        map.put("mimeType", mimeType);
        map.put("tags", tags);
        map.put("metadata", metadata);
        return map;
    }
}
