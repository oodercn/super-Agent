package net.ooder.nexus.service.im.impl;

import net.ooder.nexus.domain.im.model.SharedFile;
import net.ooder.nexus.service.im.FileService;
import net.ooder.nexus.provider.NexusStorageProvider;
import net.ooder.scene.skill.StorageProvider;
import net.ooder.scene.skill.StorageProvider.FileInfo;
import net.ooder.scene.skill.StorageProvider.StorageQuota;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 文件共享服务实现
 *
 * <p>委托给 StorageProvider 实现文件存储能力</p>
 *
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@Service
public class FileServiceImpl implements FileService {

    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    private final ConcurrentHashMap<String, SharedFile> files = new ConcurrentHashMap<String, SharedFile>();
    private final ConcurrentHashMap<String, List<String>> userFiles = new ConcurrentHashMap<String, List<String>>();
    private final ConcurrentHashMap<String, List<String>> groupFiles = new ConcurrentHashMap<String, List<String>>();

    private static final long TOTAL_SPACE = 10L * 1024 * 1024 * 1024;

    @Autowired(required = false)
    private NexusStorageProvider storageProvider;

    public FileServiceImpl() {
        initDefaultFiles();
    }

    private void initDefaultFiles() {
        addFile(createFile("f1", "项目计划书.pdf", 204800, "pdf", "张三", "user", "user-002"));
        addFile(createFile("f2", "需求文档.docx", 102400, "doc", "李四", "user", "user-003"));
        addFile(createFile("f3", "数据分析表.xlsx", 51200, "xls", "技术群组", "group", "group-001"));
        addFile(createFile("f4", "产品截图.png", 307200, "img", "王五", "user", "user-004"));
        addFile(createFile("f5", "会议纪要.pdf", 81920, "pdf", "部门群组", "department", "dept-001"));

        log.info("Initialized {} default files", files.size());
    }

    private SharedFile createFile(String id, String name, long size, String type,
                                  String source, String sourceType, String sourceId) {
        SharedFile file = new SharedFile(id, name, size, type);
        file.setSource(source);
        file.setSourceType(sourceType);
        file.setSourceId(sourceId);
        file.setUploaderId(sourceId);
        file.setUploaderName(source);
        file.setUploadTime(System.currentTimeMillis() - (long)(Math.random() * 259200000));
        file.setSharedWith(new ArrayList<String>());
        return file;
    }

    private void addFile(SharedFile file) {
        files.put(file.getFileId(), file);

        if ("user".equals(file.getSourceType())) {
            userFiles.computeIfAbsent(file.getSourceId(), k -> new ArrayList<String>()).add(file.getFileId());
        } else {
            groupFiles.computeIfAbsent(file.getSourceId(), k -> new ArrayList<String>()).add(file.getFileId());
        }
    }

    @Override
    public List<SharedFile> getFiles(String userId, String folder, String fileType) {
        log.info("Getting files for user: {}, folder: {}, type: {}", userId, folder, fileType);

        List<SharedFile> result = new ArrayList<SharedFile>();

        if ("mine".equals(folder)) {
            List<String> fileIds = userFiles.getOrDefault(userId, new ArrayList<String>());
            for (String fid : fileIds) {
                SharedFile f = files.get(fid);
                if (f != null) result.add(f);
            }
        } else if ("shared".equals(folder)) {
            for (SharedFile f : files.values()) {
                if (f.getSharedWith() != null && f.getSharedWith().contains(userId)) {
                    result.add(f);
                }
            }
        } else if ("recent".equals(folder)) {
            long oneDayAgo = System.currentTimeMillis() - 86400000;
            for (SharedFile f : files.values()) {
                if (f.getUploadTime() > oneDayAgo) {
                    result.add(f);
                }
            }
        } else {
            result.addAll(files.values());
        }

        if (fileType != null && !fileType.isEmpty() && !"all".equals(fileType)) {
            result = result.stream()
                .filter(f -> fileType.equals(f.getFileType()))
                .collect(Collectors.toList());
        }

        result.sort((a, b) -> Long.compare(b.getUploadTime(), a.getUploadTime()));

        return result;
    }

    @Override
    public SharedFile getFile(String fileId) {
        SharedFile file = files.get(fileId);
        if (file != null) {
            return file;
        }

        if (storageProvider != null) {
            FileInfo info = storageProvider.getFileInfo(fileId);
            if (info != null) {
                return convertToFile(info);
            }
        }

        return null;
    }

    @Override
    public SharedFile uploadFile(String fileName, long fileSize, String fileType,
                                  String uploaderId, String uploaderName,
                                  String sourceId, String sourceType) {
        log.info("Uploading file: {}, uploader: {}", fileName, uploaderId);

        String fileId = "f" + System.currentTimeMillis();

        SharedFile file = new SharedFile();
        file.setFileId(fileId);
        file.setFileName(fileName);
        file.setFileSize(fileSize);
        file.setFileType(fileType);
        file.setFilePath("/files/" + fileId + "/" + fileName);
        file.setUploaderId(uploaderId);
        file.setUploaderName(uploaderName);
        file.setSource(uploaderName);
        file.setSourceType(sourceType != null ? sourceType : "user");
        file.setSourceId(sourceId != null ? sourceId : uploaderId);
        file.setUploadTime(System.currentTimeMillis());
        file.setSharedWith(new ArrayList<String>());

        files.put(fileId, file);

        if ("user".equals(file.getSourceType())) {
            userFiles.computeIfAbsent(file.getSourceId(), k -> new ArrayList<String>()).add(fileId);
        } else {
            groupFiles.computeIfAbsent(file.getSourceId(), k -> new ArrayList<String>()).add(fileId);
        }

        return file;
    }

    @Override
    public boolean deleteFile(String fileId, String operatorId) {
        log.info("Deleting file: {}, operator: {}", fileId, operatorId);

        SharedFile file = files.get(fileId);
        if (file == null) {
            return false;
        }

        if (!operatorId.equals(file.getUploaderId())) {
            return false;
        }

        files.remove(fileId);

        if ("user".equals(file.getSourceType())) {
            List<String> userFileList = userFiles.get(file.getSourceId());
            if (userFileList != null) {
                userFileList.remove(fileId);
            }
        } else {
            List<String> groupFileList = groupFiles.get(file.getSourceId());
            if (groupFileList != null) {
                groupFileList.remove(fileId);
            }
        }

        if (storageProvider != null) {
            storageProvider.deleteFile(file.getFilePath(), false);
        }

        return true;
    }

    @Override
    public boolean shareFile(String fileId, List<String> targetIds, String operatorId) {
        log.info("Sharing file: {} to: {}", fileId, targetIds);

        SharedFile file = files.get(fileId);
        if (file == null) {
            return false;
        }

        if (!operatorId.equals(file.getUploaderId())) {
            return false;
        }

        List<String> sharedWith = file.getSharedWith();
        if (sharedWith == null) {
            sharedWith = new ArrayList<String>();
            file.setSharedWith(sharedWith);
        }

        for (String targetId : targetIds) {
            if (!sharedWith.contains(targetId)) {
                sharedWith.add(targetId);
            }
        }

        return true;
    }

    @Override
    public StorageStats getStorageStats(String userId) {
        StorageStats stats = new StorageStats();

        long usedSpace = 0;
        int fileCount = 0;

        List<String> userFileIds = userFiles.getOrDefault(userId, new ArrayList<String>());
        for (String fid : userFileIds) {
            SharedFile f = files.get(fid);
            if (f != null) {
                usedSpace += f.getFileSize();
                fileCount++;
            }
        }

        if (storageProvider != null) {
            StorageQuota quota = storageProvider.getQuota();
            stats.setUsedSpace(quota.getUsedSpace());
            stats.setTotalSpace(quota.getTotalSpace());
            stats.setFileCount(quota.getFileCount());
        } else {
            stats.setUsedSpace(usedSpace);
            stats.setTotalSpace(TOTAL_SPACE);
            stats.setFileCount(fileCount);
        }

        return stats;
    }

    private SharedFile convertToFile(FileInfo info) {
        SharedFile file = new SharedFile();
        file.setFileId(info.getPath());
        file.setFileName(info.getName());
        file.setFileSize(info.getSize());
        file.setFilePath(info.getPath());
        file.setUploadTime(info.getLastModified());

        String name = info.getName().toLowerCase();
        if (name.endsWith(".pdf")) {
            file.setFileType("pdf");
        } else if (name.endsWith(".doc") || name.endsWith(".docx")) {
            file.setFileType("doc");
        } else if (name.endsWith(".xls") || name.endsWith(".xlsx")) {
            file.setFileType("xls");
        } else if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg")) {
            file.setFileType("img");
        } else {
            file.setFileType("other");
        }

        return file;
    }
}
