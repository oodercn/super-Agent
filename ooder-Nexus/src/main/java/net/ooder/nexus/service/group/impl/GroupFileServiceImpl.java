package net.ooder.nexus.service.group.impl;

import net.ooder.nexus.domain.group.model.GroupFile;
import net.ooder.nexus.dto.group.FileListDTO;
import net.ooder.nexus.dto.group.FileOperationDTO;
import net.ooder.nexus.dto.group.FileShareDTO;
import net.ooder.nexus.dto.group.FileUploadDTO;
import net.ooder.nexus.service.group.GroupFileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 群组文件服务实现
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@Service("groupFileServiceImpl")
public class GroupFileServiceImpl implements GroupFileService {

    private static final Logger log = LoggerFactory.getLogger(GroupFileServiceImpl.class);

    private final ConcurrentHashMap<String, List<GroupFile>> groupFiles = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, GroupFile> fileIndex = new ConcurrentHashMap<>();

    private static final long DEFAULT_MAX_QUOTA = 1024L * 1024 * 1024 * 10;

    @Override
    public GroupFile uploadFile(FileUploadDTO dto) {
        log.info("Uploading file to group: {}, file: {}", dto.getGroupId(), dto.getFileName());
        
        GroupFile file = new GroupFile();
        file.setFileId(UUID.randomUUID().toString());
        file.setGroupId(dto.getGroupId());
        file.setUploaderId(dto.getUploaderId());
        file.setUploaderName(dto.getUploaderName());
        file.setFileName(dto.getFileName());
        file.setFilePath(dto.getFilePath());
        file.setFileSize(dto.getFileSize());
        file.setFileType(dto.getFileType());
        file.setMimeType(dto.getMimeType());
        file.setDescription(dto.getDescription());
        file.setUploadTime(System.currentTimeMillis());
        file.setDownloadCount(0);
        file.setStatus("active");

        groupFiles.computeIfAbsent(dto.getGroupId(), k -> new ArrayList<>()).add(file);
        fileIndex.put(file.getFileId(), file);
        
        log.info("File uploaded: {}", file.getFileId());
        return file;
    }

    @Override
    public List<GroupFile> getFileList(FileListDTO dto) {
        log.info("Getting file list for group: {}", dto.getGroupId());
        
        List<GroupFile> files = groupFiles.getOrDefault(dto.getGroupId(), new ArrayList<>());
        
        if (dto.getFileType() != null) {
            files = files.stream()
                    .filter(f -> dto.getFileType().equals(f.getFileType()))
                    .collect(Collectors.toList());
        }
        
        if (dto.getUploaderId() != null) {
            files = files.stream()
                    .filter(f -> dto.getUploaderId().equals(f.getUploaderId()))
                    .collect(Collectors.toList());
        }
        
        files = files.stream()
                .filter(f -> "active".equals(f.getStatus()))
                .collect(Collectors.toList());
        
        if ("uploadTime".equals(dto.getSortBy())) {
            files.sort((a, b) -> {
                int cmp = Long.compare(b.getUploadTime(), a.getUploadTime());
                return "asc".equals(dto.getSortOrder()) ? -cmp : cmp;
            });
        } else if ("fileName".equals(dto.getSortBy())) {
            files.sort((a, b) -> {
                int cmp = a.getFileName().compareToIgnoreCase(b.getFileName());
                return "desc".equals(dto.getSortOrder()) ? -cmp : cmp;
            });
        } else if ("fileSize".equals(dto.getSortBy())) {
            files.sort((a, b) -> {
                int cmp = Long.compare(b.getFileSize(), a.getFileSize());
                return "asc".equals(dto.getSortOrder()) ? -cmp : cmp;
            });
        }
        
        int page = dto.getPage() != null ? dto.getPage() : 1;
        int pageSize = dto.getPageSize() != null ? dto.getPageSize() : 20;
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, files.size());
        
        if (start >= files.size()) {
            return new ArrayList<>();
        }
        
        return files.subList(start, end);
    }

    @Override
    public GroupFile getFile(String fileId) {
        return fileIndex.get(fileId);
    }

    @Override
    public String downloadFile(FileOperationDTO dto) {
        log.info("Downloading file: {} from group: {}", dto.getFileId(), dto.getGroupId());
        
        GroupFile file = fileIndex.get(dto.getFileId());
        if (file == null) {
            return null;
        }
        
        if (!file.getGroupId().equals(dto.getGroupId())) {
            return null;
        }
        
        file.setDownloadCount(file.getDownloadCount() + 1);
        
        return file.getFilePath();
    }

    @Override
    public boolean deleteFile(FileOperationDTO dto) {
        log.info("Deleting file: {} from group: {}", dto.getFileId(), dto.getGroupId());
        
        GroupFile file = fileIndex.get(dto.getFileId());
        if (file == null) {
            return false;
        }
        
        if (!file.getUploaderId().equals(dto.getOperatorId())) {
            return false;
        }
        
        file.setStatus("deleted");
        
        return true;
    }

    @Override
    public GroupFile shareFile(FileShareDTO dto) {
        log.info("Sharing file: {} from group: {} to group: {}", 
                dto.getFileId(), dto.getSourceGroupId(), dto.getTargetGroupId());
        
        GroupFile sourceFile = fileIndex.get(dto.getFileId());
        if (sourceFile == null) {
            return null;
        }
        
        if (!sourceFile.getGroupId().equals(dto.getSourceGroupId())) {
            return null;
        }
        
        GroupFile sharedFile = new GroupFile();
        sharedFile.setFileId(UUID.randomUUID().toString());
        sharedFile.setGroupId(dto.getTargetGroupId());
        sharedFile.setUploaderId(sourceFile.getUploaderId());
        sharedFile.setUploaderName(sourceFile.getUploaderName());
        sharedFile.setFileName(sourceFile.getFileName());
        sharedFile.setFilePath(sourceFile.getFilePath());
        sharedFile.setFileSize(sourceFile.getFileSize());
        sharedFile.setFileType(sourceFile.getFileType());
        sharedFile.setMimeType(sourceFile.getMimeType());
        sharedFile.setDescription(sourceFile.getDescription());
        sharedFile.setUploadTime(System.currentTimeMillis());
        sharedFile.setDownloadCount(0);
        sharedFile.setStatus("active");
        sharedFile.setSharedFrom(dto.getSourceGroupId());
        
        groupFiles.computeIfAbsent(dto.getTargetGroupId(), k -> new ArrayList<>()).add(sharedFile);
        fileIndex.put(sharedFile.getFileId(), sharedFile);
        
        sourceFile.setSharedTo(dto.getTargetGroupId());
        
        return sharedFile;
    }

    @Override
    public StorageStats getStorageStats(String groupId) {
        log.info("Getting storage stats for group: {}", groupId);
        
        List<GroupFile> files = groupFiles.getOrDefault(groupId, new ArrayList<>());
        
        StorageStats stats = new StorageStats();
        long totalSize = 0;
        int count = 0;
        
        for (GroupFile file : files) {
            if ("active".equals(file.getStatus())) {
                totalSize += file.getFileSize();
                count++;
            }
        }
        
        stats.setTotalSize(totalSize);
        stats.setFileCount(count);
        stats.setUsedQuota(totalSize);
        stats.setMaxQuota(DEFAULT_MAX_QUOTA);
        
        return stats;
    }

    @Override
    public List<GroupFile> searchFiles(String groupId, String keyword) {
        log.info("Searching files in group: {} with keyword: {}", groupId, keyword);
        
        List<GroupFile> files = groupFiles.getOrDefault(groupId, new ArrayList<>());
        String lowerKeyword = keyword.toLowerCase();
        
        return files.stream()
                .filter(f -> "active".equals(f.getStatus()))
                .filter(f -> f.getFileName().toLowerCase().contains(lowerKeyword) ||
                        (f.getDescription() != null && f.getDescription().toLowerCase().contains(lowerKeyword)))
                .collect(Collectors.toList());
    }
}
