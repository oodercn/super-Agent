package net.ooder.nexus.core.storage.vfs;

import java.util.List;
import java.util.Map;

/**
 * 文件版本接口（扩充版）
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
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
    
    String getVersionNumber();
    
    void setVersionNumber(String versionNumber);
    
    String getDescription();
    
    void setDescription(String description);
    
    String getChangeNote();
    
    void setChangeNote(String changeNote);
    
    Long getSize();
    
    void setSize(Long size);
    
    String getStatus();
    
    void setStatus(String status);
    
    String getParentVersionId();
    
    void setParentVersionId(String parentVersionId);
    
    String getMimeType();
    
    void setMimeType(String mimeType);
    
    List<String> getTags();
    
    void setTags(List<String> tags);
    
    Map<String, Object> getMetadata();
    
    void setMetadata(Map<String, Object> metadata);
    
    boolean isActive();
    
    boolean isArchived();
    
    void archive();
    
    void restore();
    
    Map<String, Object> toMap();
}
