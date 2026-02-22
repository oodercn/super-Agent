package net.ooder.nexus.service.enterprise;

import net.ooder.nexus.model.Result;

import java.io.InputStream;
import java.util.List;

/**
 * VFS (Virtual File System) service interface
 * Provides file storage and management capabilities
 */
public interface VfsService {

    /**
     * Upload file
     */
    Result<FileInfo> uploadFile(String path, byte[] data);

    /**
     * Upload file from input stream
     */
    Result<FileInfo> uploadFile(String path, InputStream inputStream, long size);

    /**
     * Download file
     */
    Result<byte[]> downloadFile(String path);

    /**
     * Download file to input stream
     */
    Result<InputStream> downloadFileStream(String path);

    /**
     * Delete file
     */
    Result<Boolean> deleteFile(String path);

    /**
     * Check if file exists
     */
    Result<Boolean> exists(String path);

    /**
     * Get file info
     */
    Result<FileInfo> getFileInfo(String path);

    /**
     * List files in directory
     */
    Result<List<FileInfo>> listFiles(String directory);

    /**
     * Create directory
     */
    Result<Boolean> createDirectory(String path);

    /**
     * Delete directory
     */
    Result<Boolean> deleteDirectory(String path);

    /**
     * Copy file
     */
    Result<Boolean> copyFile(String sourcePath, String targetPath);

    /**
     * Move file
     */
    Result<Boolean> moveFile(String sourcePath, String targetPath);

    /**
     * Get file size
     */
    Result<Long> getFileSize(String path);

    /**
     * File info model
     */
    class FileInfo {
        private String path;
        private String name;
        private long size;
        private long createTime;
        private long modifyTime;
        private boolean directory;
        private String mimeType;
        private String checksum;

        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public long getSize() { return size; }
        public void setSize(long size) { this.size = size; }
        public long getCreateTime() { return createTime; }
        public void setCreateTime(long createTime) { this.createTime = createTime; }
        public long getModifyTime() { return modifyTime; }
        public void setModifyTime(long modifyTime) { this.modifyTime = modifyTime; }
        public boolean isDirectory() { return directory; }
        public void setDirectory(boolean directory) { this.directory = directory; }
        public String getMimeType() { return mimeType; }
        public void setMimeType(String mimeType) { this.mimeType = mimeType; }
        public String getChecksum() { return checksum; }
        public void setChecksum(String checksum) { this.checksum = checksum; }
    }
}
