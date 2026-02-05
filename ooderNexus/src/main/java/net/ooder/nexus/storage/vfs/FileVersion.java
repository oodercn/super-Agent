package net.ooder.nexus.storage.vfs;

public interface FileVersion {
    String getVersionID();
    String getFileId();
    String getFileObjectId();
    String getSourceId();
    Long getCreateTime();
    Long getUpdateTime();
    String getPersonId();
    String getHash();
    FileObject getFileObject();
    void setFileId(String fileId);
    void setCreateTime(Long createTime);
    void setPersonId(String personId);
    void setFileObjectId(String fileObjectId);
    void setSourceId(String sourceId);
}
