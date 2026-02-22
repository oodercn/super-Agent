package net.ooder.nexus.domain.im.model;

import java.util.List;
import java.util.Map;

/**
 * 共享文件模型
 *
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public class SharedFile {
    
    private String fileId;
    private String fileName;
    private long fileSize;
    private String fileType;
    private String filePath;
    private String source;
    private String sourceType;
    private String sourceId;
    private String uploaderId;
    private String uploaderName;
    private long uploadTime;
    private List<String> sharedWith;
    private Map<String, Object> metadata;
    
    public SharedFile() {}
    
    public SharedFile(String fileId, String fileName, long fileSize, String fileType) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.uploadTime = System.currentTimeMillis();
    }
    
    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    
    public String getSourceId() { return sourceId; }
    public void setSourceId(String sourceId) { this.sourceId = sourceId; }
    
    public String getUploaderId() { return uploaderId; }
    public void setUploaderId(String uploaderId) { this.uploaderId = uploaderId; }
    
    public String getUploaderName() { return uploaderName; }
    public void setUploaderName(String uploaderName) { this.uploaderName = uploaderName; }
    
    public long getUploadTime() { return uploadTime; }
    public void setUploadTime(long uploadTime) { this.uploadTime = uploadTime; }
    
    public List<String> getSharedWith() { return sharedWith; }
    public void setSharedWith(List<String> sharedWith) { this.sharedWith = sharedWith; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
