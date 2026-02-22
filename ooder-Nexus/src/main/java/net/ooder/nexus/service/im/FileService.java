package net.ooder.nexus.service.im;

import net.ooder.nexus.domain.im.model.SharedFile;

import java.util.List;

/**
 * 文件共享服务接口
 *
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public interface FileService {
    
    /**
     * 获取文件列表
     */
    List<SharedFile> getFiles(String userId, String folder, String fileType);
    
    /**
     * 获取文件详情
     */
    SharedFile getFile(String fileId);
    
    /**
     * 上传文件
     */
    SharedFile uploadFile(String fileName, long fileSize, String fileType, 
                          String uploaderId, String uploaderName, String sourceId, String sourceType);
    
    /**
     * 删除文件
     */
    boolean deleteFile(String fileId, String operatorId);
    
    /**
     * 分享文件
     */
    boolean shareFile(String fileId, List<String> targetIds, String operatorId);
    
    /**
     * 获取存储统计
     */
    StorageStats getStorageStats(String userId);
    
    /**
     * 存储统计
     */
    class StorageStats {
        private long usedSpace;
        private long totalSpace;
        private int fileCount;
        
        public long getUsedSpace() { return usedSpace; }
        public void setUsedSpace(long usedSpace) { this.usedSpace = usedSpace; }
        public long getTotalSpace() { return totalSpace; }
        public void setTotalSpace(long totalSpace) { this.totalSpace = totalSpace; }
        public int getFileCount() { return fileCount; }
        public void setFileCount(int fileCount) { this.fileCount = fileCount; }
    }
}
