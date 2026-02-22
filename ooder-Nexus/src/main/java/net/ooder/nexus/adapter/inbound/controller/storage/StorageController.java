package net.ooder.nexus.adapter.inbound.controller.storage;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.core.storage.vfs.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * 存储管理控制器
 * 处理文件夹管理、文件上传下载、版本控制、文件分享等操作
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
@RestController
@RequestMapping("/api/storage")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class StorageController {

    private final LocalVFSManager vfsManager;

    public StorageController() {
        this.vfsManager = LocalVFSManager.getInstance();
    }

    @PostMapping("/space")
    @ResponseBody
    public ResultModel<Map<String, Object>> getStorageSpace() {
        Map<String, Object> data = new HashMap<>();

        File storageRoot = new File("./data/storage");
        long totalSpace = storageRoot.getTotalSpace();
        long freeSpace = storageRoot.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;

        data.put("totalSpace", totalSpace);
        data.put("freeSpace", freeSpace);
        data.put("usedSpace", usedSpace);
        data.put("usagePercent", (double) usedSpace / totalSpace * 100);

        return ResultModel.success(data);
    }

    @PostMapping("/folder/children")
    @ResponseBody
    public ResultModel<Map<String, Object>> getFolderChildren(@RequestBody Map<String, String> request) {
        try {
            String folderId = request.get("folderId");
            if (folderId == null || folderId.isEmpty()) {
                folderId = "./data/storage";
            }

            File folderFile = new File(folderId);
            String absolutePath = folderFile.getAbsolutePath();

            Folder folder = vfsManager.getFolderByID(absolutePath);

            if (folder == null) {
                if (!folderFile.exists()) {
                    folderFile.mkdirs();
                }
                folder = vfsManager.getFolderByID(absolutePath);
            }

            if (folder == null) {
                return ResultModel.error("文件夹不存在且无法创建", 404);
            }

            Map<String, Object> data = buildFolderData(folder);
            return ResultModel.success(data);
        } catch (Exception e) {
            return ResultModel.error("获取文件夹内容失败：" + e.getMessage(), 500);
        }
    }

    @PostMapping("/folder/create")
    @ResponseBody
    public ResultModel<Map<String, Object>> createFolder(@RequestBody Map<String, String> request) {
        try {
            String parentId = request.get("parentId");
            String name = request.get("name");
            String description = request.getOrDefault("description", "");

            if (parentId == null || parentId.isEmpty()) {
                parentId = "./data/storage";
            }

            Folder parentFolder = vfsManager.getFolderByID(parentId);
            if (parentFolder == null) {
                File rootDir = new File("./data/storage");
                if (!rootDir.exists()) {
                    rootDir.mkdirs();
                }
                parentFolder = vfsManager.getFolderByID(parentId);
                if (parentFolder == null) {
                    return ResultModel.error("父文件夹不存在", 404);
                }
            }

            Folder newFolder = parentFolder.createChildFolder(name, "system");
            newFolder.setDescription(description);

            Map<String, Object> data = new HashMap<>();
            data.put("folder", buildFolderInfo(newFolder));

            return ResultModel.success(data);
        } catch (Exception e) {
            return ResultModel.error("创建文件夹失败：" + e.getMessage(), 500);
        }
    }

    @PostMapping("/folder/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteFolder(@RequestBody Map<String, String> request) {
        String folderId = request.get("folderId");
        boolean result = vfsManager.deleteFolder(folderId);

        if (result) {
            return ResultModel.success(true);
        } else {
            return ResultModel.error("文件夹删除失败", 500);
        }
    }

    @PostMapping("/file/upload")
    @ResponseBody
    public ResultModel<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folderId") String folderId,
            @RequestParam(value = "description", required = false) String description) {
        try {
            if (folderId == null || folderId.isEmpty()) {
                folderId = "./data/storage";
            }

            Folder folder = vfsManager.getFolderByID(folderId);
            if (folder == null) {
                File rootDir = new File("./data/storage");
                if (!rootDir.exists()) {
                    rootDir.mkdirs();
                }
                folder = vfsManager.getFolderByID(folderId);
                if (folder == null) {
                    return ResultModel.error("父文件夹不存在", 404);
                }
            }

            FileInfo fileInfo = folder.createFile(file.getOriginalFilename(), "system");

            File tempFile = File.createTempFile("upload-", ".tmp");
            file.transferTo(tempFile);

            Map<String, Object> data = new HashMap<>();
            data.put("file", buildFileInfo(fileInfo));

            return ResultModel.success(data);
        } catch (Exception e) {
            return ResultModel.error("文件上传失败：" + e.getMessage(), 500);
        }
    }

    @PostMapping("/file/download")
    @ResponseBody
    public ResponseEntity<byte[]> downloadFile(@RequestBody Map<String, String> request) {
        String fileId = request.get("fileId");
        FileInfo fileInfo = vfsManager.getFileInfoByID(fileId);
        if (fileInfo == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            File file = new File(fileInfo.getPath());
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            FileInputStream inputStream = new FileInputStream(file);
            byte[] fileContent = new byte[(int) file.length()];
            inputStream.read(fileContent);
            inputStream.close();

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileInfo.getName() + "\"")
                    .body(fileContent);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/file/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteFile(@RequestBody Map<String, String> request) {
        String fileId = request.get("fileId");
        boolean result = vfsManager.removeFileInfo(fileId);

        if (result) {
            return ResultModel.success(true);
        } else {
            return ResultModel.error("文件删除失败", 500);
        }
    }

    @PostMapping("/file/update")
    @ResponseBody
    public ResultModel<Map<String, Object>> updateFile(@RequestBody Map<String, Object> request) {
        String fileId = (String) request.get("fileId");
        String name = (String) request.get("name");
        String description = request.get("description") != null ? (String) request.get("description") : "";

        FileInfo fileInfo = vfsManager.getFileInfoByID(fileId);
        if (fileInfo == null) {
            return ResultModel.error("文件不存在", 404);
        }

        vfsManager.updateFileInfo(fileInfo.getPath(), name, description);

        Map<String, Object> data = new HashMap<>();
        data.put("file", buildFileInfo(fileInfo));

        return ResultModel.success(data);
    }

    @PostMapping("/file/versions")
    @ResponseBody
    public ResultModel<Map<String, Object>> getFileVersions(@RequestBody Map<String, String> request) {
        try {
            String fileId = request.get("fileId");
            FileInfo fileInfo = vfsManager.getFileInfoByID(fileId);
            if (fileInfo == null) {
                return ResultModel.error("文件不存在", 404);
            }

            List<FileVersion> versions = fileInfo.getVersionList();

            List<Map<String, Object>> simplifiedVersions = new ArrayList<>();
            for (FileVersion version : versions) {
                Map<String, Object> versionInfo = new HashMap<>();
                versionInfo.put("id", version.getVersionID());
                versionInfo.put("fileId", version.getFileId());
                versionInfo.put("createTime", version.getCreateTime());
                versionInfo.put("hash", version.getHash());
                simplifiedVersions.add(versionInfo);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("versions", simplifiedVersions);

            return ResultModel.success(data);
        } catch (Exception e) {
            return ResultModel.error("获取文件版本失败：" + e.getMessage(), 500);
        }
    }

    @PostMapping("/file/restore")
    @ResponseBody
    public ResultModel<Boolean> restoreFileVersion(@RequestBody Map<String, String> request) {
        String fileId = request.get("fileId");
        String versionId = request.get("versionId");

        FileInfo fileInfo = vfsManager.getFileInfoByID(fileId);
        if (fileInfo == null) {
            return ResultModel.error("文件不存在", 404);
        }

        FileVersion version = vfsManager.getVersionById(versionId);
        if (version == null) {
            return ResultModel.error("版本不存在", 404);
        }

        vfsManager.createFileVersion(fileInfo.getPath(), version.getHash());

        return ResultModel.success(true);
    }

    @PostMapping("/cleanup")
    @ResponseBody
    public ResultModel<Map<String, Object>> cleanupStorage() {
        File cacheDir = new File("./data/cache");
        File tempDir = new File("./data/temp");

        long freedSpace = 0;

        if (cacheDir.exists()) {
            long cacheSize = getDirectorySize(cacheDir);
            deleteDirectory(cacheDir);
            freedSpace += cacheSize;
        }

        if (tempDir.exists()) {
            long tempSize = getDirectorySize(tempDir);
            deleteDirectory(tempDir);
            freedSpace += tempSize;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("freedSpace", freedSpace);

        return ResultModel.success(data);
    }

    @PostMapping("/shared/list")
    @ResponseBody
    public ResultModel<Map<String, Object>> getSharedFiles() {
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> sharedFiles = new ArrayList<>();

        Map<String, Object> sharedFile1 = new HashMap<>();
        sharedFile1.put("id", "1");
        sharedFile1.put("fileId", "file1");
        sharedFile1.put("fileName", "文档1.txt");
        sharedFile1.put("target", "user1");
        sharedFile1.put("targetType", "user");
        sharedFile1.put("shareTime", System.currentTimeMillis() - 3600000);
        sharedFile1.put("expireTime", System.currentTimeMillis() + 86400000);
        sharedFiles.add(sharedFile1);

        Map<String, Object> sharedFile2 = new HashMap<>();
        sharedFile2.put("id", "2");
        sharedFile2.put("fileId", "file2");
        sharedFile2.put("fileName", "文档2.pdf");
        sharedFile2.put("target", "group1");
        sharedFile2.put("targetType", "group");
        sharedFile2.put("shareTime", System.currentTimeMillis() - 7200000);
        sharedFile2.put("expireTime", null);
        sharedFiles.add(sharedFile2);

        data.put("sharedFiles", sharedFiles);

        return ResultModel.success(data);
    }

    @PostMapping("/share")
    @ResponseBody
    public ResultModel<Map<String, Object>> shareFile(@RequestBody Map<String, String> request) {
        String fileId = request.get("fileId");
        String target = request.get("target");
        String targetType = request.getOrDefault("targetType", "user");
        String expireTime = request.getOrDefault("expireTime", "");

        FileInfo fileInfo = vfsManager.getFileInfoByID(fileId);
        if (fileInfo == null) {
            return ResultModel.error("文件不存在", 404);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id", System.currentTimeMillis());
        data.put("fileId", fileId);
        data.put("fileName", fileInfo.getName());
        data.put("target", target);
        data.put("targetType", targetType);
        data.put("shareTime", System.currentTimeMillis());
        data.put("expireTime", expireTime.isEmpty() ? null : Long.parseLong(expireTime));
        data.put("shareUrl", "/api/storage/shared/" + System.currentTimeMillis());

        return ResultModel.success(data);
    }

    @PostMapping("/received/list")
    @ResponseBody
    public ResultModel<Map<String, Object>> getReceivedFiles() {
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> receivedFiles = new ArrayList<>();

        Map<String, Object> receivedFile1 = new HashMap<>();
        receivedFile1.put("id", "1");
        receivedFile1.put("fileId", "file3");
        receivedFile1.put("fileName", "共享文档1.docx");
        receivedFile1.put("sharedBy", "user2");
        receivedFile1.put("shareTime", System.currentTimeMillis() - 10800000);
        receivedFile1.put("expireTime", System.currentTimeMillis() + 43200000);
        receivedFile1.put("shareUrl", "/api/storage/shared/123456");
        receivedFiles.add(receivedFile1);

        Map<String, Object> receivedFile2 = new HashMap<>();
        receivedFile2.put("id", "2");
        receivedFile2.put("fileId", "file4");
        receivedFile2.put("fileName", "共享文档2.xlsx");
        receivedFile2.put("sharedBy", "user3");
        receivedFile2.put("shareTime", System.currentTimeMillis() - 14400000);
        receivedFile2.put("expireTime", null);
        receivedFile2.put("shareUrl", "/api/storage/shared/789012");
        receivedFiles.add(receivedFile2);

        data.put("receivedFiles", receivedFiles);

        return ResultModel.success(data);
    }

    @PostMapping("/share/cancel")
    @ResponseBody
    public ResultModel<Boolean> unshareFile(@RequestBody Map<String, String> request) {
        String shareId = request.get("shareId");
        return ResultModel.success(true);
    }

    private Map<String, Object> buildFolderData(Folder folder) {
        Map<String, Object> data = new HashMap<>();

        data.put("folder", buildFolderInfo(folder));

        List<Map<String, Object>> simplifiedChildren = new ArrayList<>();
        try {
            for (Folder child : folder.getChildrenList()) {
                Map<String, Object> childInfo = new HashMap<>();
                childInfo.put("id", child.getID());
                childInfo.put("name", child.getName());
                simplifiedChildren.add(childInfo);
            }
        } catch (Exception e) {
        }
        data.put("children", simplifiedChildren);

        List<Map<String, Object>> simplifiedFiles = new ArrayList<>();
        try {
            for (FileInfo file : folder.getFileList()) {
                simplifiedFiles.add(buildFileInfo(file));
            }
        } catch (Exception e) {
        }
        data.put("files", simplifiedFiles);

        return data;
    }

    private Map<String, Object> buildFolderInfo(Folder folder) {
        Map<String, Object> folderInfo = new HashMap<>();
        folderInfo.put("id", folder.getID());
        folderInfo.put("name", folder.getName());
        folderInfo.put("path", folder.getPath());
        folderInfo.put("personId", folder.getPersonId());
        folderInfo.put("description", folder.getDescription());
        folderInfo.put("parentId", folder.getParentId());
        folderInfo.put("folderType", folder.getFolderType());
        folderInfo.put("createTime", folder.getCreateTime());
        folderInfo.put("updateTime", folder.getUpdateTime());
        return folderInfo;
    }

    private Map<String, Object> buildFileInfo(FileInfo fileInfo) {
        Map<String, Object> fileData = new HashMap<>();
        fileData.put("id", fileInfo.getID());
        fileData.put("name", fileInfo.getName());
        fileData.put("path", fileInfo.getPath());
        try {
            fileData.put("size", new File(fileInfo.getPath()).length());
        } catch (Exception e) {
            fileData.put("size", 0);
        }
        return fileData;
    }

    private long getDirectorySize(File directory) {
        long size = 0;
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        size += getDirectorySize(file);
                    } else {
                        size += file.length();
                    }
                }
            }
        }
        return size;
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}
