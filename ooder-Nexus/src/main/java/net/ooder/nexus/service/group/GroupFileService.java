package net.ooder.nexus.service.group;

import net.ooder.nexus.domain.group.model.GroupFile;
import net.ooder.nexus.dto.group.FileListDTO;
import net.ooder.nexus.dto.group.FileOperationDTO;
import net.ooder.nexus.dto.group.FileShareDTO;
import net.ooder.nexus.dto.group.FileUploadDTO;

import java.util.List;

/**
 * 群组文件服务接口
 * 
 * <p>提供群组文件的上传、下载、共享等功能。</p>
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public interface GroupFileService {

    /**
     * 上传文件
     *
     * @param dto 文件上传 DTO
     * @return 上传后的文件信息
     */
    GroupFile uploadFile(FileUploadDTO dto);

    /**
     * 获取文件列表
     *
     * @param dto 查询参数
     * @return 文件列表
     */
    List<GroupFile> getFileList(FileListDTO dto);

    /**
     * 获取文件详情
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    GroupFile getFile(String fileId);

    /**
     * 下载文件
     *
     * @param dto 文件操作 DTO
     * @return 文件下载路径
     */
    String downloadFile(FileOperationDTO dto);

    /**
     * 删除文件
     *
     * @param dto 文件操作 DTO
     * @return 操作结果
     */
    boolean deleteFile(FileOperationDTO dto);

    /**
     * 共享文件到其他群组
     *
     * @param dto 文件共享 DTO
     * @return 共享后的文件信息
     */
    GroupFile shareFile(FileShareDTO dto);

    /**
     * 获取群组存储统计
     *
     * @param groupId 群组ID
     * @return 存储统计信息
     */
    StorageStats getStorageStats(String groupId);

    /**
     * 搜索文件
     *
     * @param groupId 群组ID
     * @param keyword 关键词
     * @return 文件列表
     */
    List<GroupFile> searchFiles(String groupId, String keyword);

    /**
     * 存储统计信息
     */
    class StorageStats {
        private long totalSize;
        private int fileCount;
        private long usedQuota;
        private long maxQuota;

        public long getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(long totalSize) {
            this.totalSize = totalSize;
        }

        public int getFileCount() {
            return fileCount;
        }

        public void setFileCount(int fileCount) {
            this.fileCount = fileCount;
        }

        public long getUsedQuota() {
            return usedQuota;
        }

        public void setUsedQuota(long usedQuota) {
            this.usedQuota = usedQuota;
        }

        public long getMaxQuota() {
            return maxQuota;
        }

        public void setMaxQuota(long maxQuota) {
            this.maxQuota = maxQuota;
        }
    }
}
