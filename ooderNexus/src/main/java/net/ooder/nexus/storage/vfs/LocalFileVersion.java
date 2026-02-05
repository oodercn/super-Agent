package net.ooder.nexus.storage.vfs;

import java.io.File;

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

    public LocalFileVersion(File file, String hash) {
        this.file = file;
        this.hash = hash;
        this.versionID = file.getAbsolutePath();
        this.createTime = System.currentTimeMillis();
        this.updateTime = System.currentTimeMillis();
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
}
