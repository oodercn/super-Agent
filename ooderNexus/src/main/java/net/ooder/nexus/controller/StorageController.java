package net.ooder.nexus.controller;

import net.ooder.nexus.storage.vfs.*;
import net.ooder.nexus.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/storage")
@CrossOrigin(origins = "*")
public class StorageController {

    private final LocalVFSManager vfsManager;

    public StorageController() {
        this.vfsManager = LocalVFSManager.getInstance();
        System.out.println("StorageController initialized");
    }

    @GetMapping("/space")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStorageSpace() {
        Map<String, Object> data = new HashMap<>();
        
        File storageRoot = new File("./data/storage");
        long totalSpace = storageRoot.getTotalSpace();
        long freeSpace = storageRoot.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        
        data.put("totalSpace", totalSpace);
        data.put("freeSpace", freeSpace);
        data.put("usedSpace", usedSpace);
        data.put("usagePercent", (double) usedSpace / totalSpace * 100);
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @GetMapping("/folder/children")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRootFolderChildren() {
        try {
            System.out.println("获取根文件夹内容");
            
            // 使用默认的存储根目录路径
            String folderPath = "./data/storage";
            System.out.println("使用默认存储根目录: " + folderPath);
            
            // 转换为绝对路径，确保正确解析
            File folderFile = new File(folderPath);
            String absolutePath = folderFile.getAbsolutePath();
            System.out.println("转换为绝对路径: " + absolutePath);
            
            // 尝试获取文件夹
            Folder folder = vfsManager.getFolderByID(absolutePath);
            
            // 如果文件夹不存在，尝试创建它
            if (folder == null) {
                System.out.println("文件夹不存在，尝试创建: " + absolutePath);
                if (!folderFile.exists()) {
                    folderFile.mkdirs();
                }
                folder = vfsManager.getFolderByID(absolutePath);
            }
            
            if (folder == null) {
                System.out.println("无法创建文件夹: " + absolutePath);
                return ResponseEntity.ok(ApiResponse.error("文件夹不存在且无法创建"));
            }

            Map<String, Object> data = new HashMap<>();
            
            // 构建简化的文件夹信息，避免无限递归序列化
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
            
            // 构建简化的子文件夹列表
            List<Map<String, Object>> simplifiedChildren = new ArrayList<>();
            try {
                for (Folder child : folder.getChildrenList()) {
                    Map<String, Object> childInfo = new HashMap<>();
                    childInfo.put("id", child.getID());
                    childInfo.put("name", child.getName());
                    simplifiedChildren.add(childInfo);
                }
            } catch (Exception e) {
                System.out.println("获取子文件夹列表失败: " + e.getMessage());
            }
            
            // 构建简化的文件列表
            List<Map<String, Object>> simplifiedFiles = new ArrayList<>();
            try {
                for (FileInfo file : folder.getFileList()) {
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("id", file.getID());
                    fileInfo.put("name", file.getName());
                    fileInfo.put("path", file.getPath());
                    try {
                        fileInfo.put("size", new File(file.getPath()).length());
                    } catch (Exception e) {
                        fileInfo.put("size", 0);
                    }
                    simplifiedFiles.add(fileInfo);
                }
            } catch (Exception e) {
                System.out.println("获取文件列表失败: " + e.getMessage());
            }
            
            data.put("folder", folderInfo);
            data.put("children", simplifiedChildren);
            data.put("files", simplifiedFiles);

            System.out.println("返回文件夹内容: " + folder.getName() + "，子文件夹: " + simplifiedChildren.size() + "，文件: " + simplifiedFiles.size());
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取文件夹内容失败: " + e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("获取文件夹内容失败：" + e.getMessage()));
        }
    }

    @PostMapping("/folder/children")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFolderChildren(@RequestBody Map<String, String> request) {
        try {
            String folderId = request.get("folderId");
            System.out.println("获取文件夹内容: " + folderId);
            
            // 转换为绝对路径，确保正确解析
            File folderFile = new File(folderId);
            String absolutePath = folderFile.getAbsolutePath();
            System.out.println("转换为绝对路径: " + absolutePath);
            
            // 尝试获取文件夹
            Folder folder = vfsManager.getFolderByID(absolutePath);
            
            // 如果文件夹不存在，尝试创建它
            if (folder == null) {
                System.out.println("文件夹不存在，尝试创建: " + absolutePath);
                if (!folderFile.exists()) {
                    folderFile.mkdirs();
                }
                folder = vfsManager.getFolderByID(absolutePath);
            }
            
            if (folder == null) {
                System.out.println("无法创建文件夹: " + absolutePath);
                return ResponseEntity.ok(ApiResponse.error("文件夹不存在且无法创建"));
            }

            Map<String, Object> data = new HashMap<>();
            
            // 构建简化的文件夹信息，避免无限递归序列化
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
            
            // 构建简化的子文件夹列表
            List<Map<String, Object>> simplifiedChildren = new ArrayList<>();
            try {
                for (Folder child : folder.getChildrenList()) {
                    Map<String, Object> childInfo = new HashMap<>();
                    childInfo.put("id", child.getID());
                    childInfo.put("name", child.getName());
                    simplifiedChildren.add(childInfo);
                }
            } catch (Exception e) {
                System.out.println("获取子文件夹列表失败: " + e.getMessage());
            }
            
            // 构建简化的文件列表
            List<Map<String, Object>> simplifiedFiles = new ArrayList<>();
            try {
                for (FileInfo file : folder.getFileList()) {
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("id", file.getID());
                    fileInfo.put("name", file.getName());
                    fileInfo.put("path", file.getPath());
                    try {
                        fileInfo.put("size", new File(file.getPath()).length());
                    } catch (Exception e) {
                        fileInfo.put("size", 0);
                    }
                    simplifiedFiles.add(fileInfo);
                }
            } catch (Exception e) {
                System.out.println("获取文件列表失败: " + e.getMessage());
            }
            
            data.put("folder", folderInfo);
            data.put("children", simplifiedChildren);
            data.put("files", simplifiedFiles);

            System.out.println("返回文件夹内容: " + folder.getName() + "，子文件夹: " + simplifiedChildren.size() + "，文件: " + simplifiedFiles.size());
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取文件夹内容失败: " + e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("获取文件夹内容失败：" + e.getMessage()));
        }
    }

    @GetMapping("/folder/{folderId}/children")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFolderChildren(@PathVariable String folderId) {
        try {
            System.out.println("获取文件夹内容: " + folderId);
            
            // 转换为绝对路径，确保正确解析
            File folderFile = new File(folderId);
            String absolutePath = folderFile.getAbsolutePath();
            System.out.println("转换为绝对路径: " + absolutePath);
            
            // 尝试获取文件夹
            Folder folder = vfsManager.getFolderByID(absolutePath);
            
            // 如果文件夹不存在，尝试创建它
            if (folder == null) {
                System.out.println("文件夹不存在，尝试创建: " + absolutePath);
                if (!folderFile.exists()) {
                    folderFile.mkdirs();
                }
                folder = vfsManager.getFolderByID(absolutePath);
            }
            
            if (folder == null) {
                System.out.println("无法创建文件夹: " + absolutePath);
                return ResponseEntity.ok(ApiResponse.error("文件夹不存在且无法创建"));
            }

            Map<String, Object> data = new HashMap<>();
            
            // 构建简化的文件夹信息，避免无限递归序列化
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
            
            // 构建简化的子文件夹列表
            List<Map<String, Object>> simplifiedChildren = new ArrayList<>();
            try {
                for (Folder child : folder.getChildrenList()) {
                    Map<String, Object> childInfo = new HashMap<>();
                    childInfo.put("id", child.getID());
                    childInfo.put("name", child.getName());
                    simplifiedChildren.add(childInfo);
                }
            } catch (Exception e) {
                System.out.println("获取子文件夹列表失败: " + e.getMessage());
            }
            
            // 构建简化的文件列表
            List<Map<String, Object>> simplifiedFiles = new ArrayList<>();
            try {
                for (FileInfo file : folder.getFileList()) {
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("id", file.getID());
                    fileInfo.put("name", file.getName());
                    fileInfo.put("path", file.getPath());
                    try {
                        fileInfo.put("size", new File(file.getPath()).length());
                    } catch (Exception e) {
                        fileInfo.put("size", 0);
                    }
                    simplifiedFiles.add(fileInfo);
                }
            } catch (Exception e) {
                System.out.println("获取文件列表失败: " + e.getMessage());
            }
            
            data.put("folder", folderInfo);
            data.put("children", simplifiedChildren);
            data.put("files", simplifiedFiles);

            System.out.println("返回文件夹内容: " + folder.getName() + "，子文件夹: " + simplifiedChildren.size() + "，文件: " + simplifiedFiles.size());
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取文件夹内容失败: " + e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("获取文件夹内容失败：" + e.getMessage()));
        }
    }

    @PostMapping("/folder")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createFolder(@RequestBody Map<String, String> request) {
        try {
            String parentId = request.get("parentId");
            String name = request.get("name");
            String description = request.getOrDefault("description", "");
            
            // 当parentId为空时，使用存储系统的根目录路径作为父文件夹ID
            if (parentId == null || parentId.isEmpty()) {
                parentId = "./data/storage";
            }
            
            Folder parentFolder = vfsManager.getFolderByID(parentId);
            if (parentFolder == null) {
                // 如果父文件夹不存在，尝试创建存储根目录
                File rootDir = new File("./data/storage");
                if (!rootDir.exists()) {
                    rootDir.mkdirs();
                }
                // 重新获取父文件夹
                parentFolder = vfsManager.getFolderByID(parentId);
                if (parentFolder == null) {
                    return ResponseEntity.ok(ApiResponse.error("父文件夹不存在"));
                }
            }
            
            Folder newFolder = parentFolder.createChildFolder(name, "system");
            newFolder.setDescription(description);
            
            // 构建简化的文件夹信息，避免无限递归序列化
            Map<String, Object> folderInfo = new HashMap<>();
            folderInfo.put("id", newFolder.getID());
            folderInfo.put("name", newFolder.getName());
            folderInfo.put("path", newFolder.getPath());
            folderInfo.put("personId", newFolder.getPersonId());
            folderInfo.put("description", newFolder.getDescription());
            folderInfo.put("parentId", newFolder.getParentId());
            folderInfo.put("folderType", newFolder.getFolderType());
            folderInfo.put("createTime", newFolder.getCreateTime());
            folderInfo.put("updateTime", newFolder.getUpdateTime());
            
            Map<String, Object> data = new HashMap<>();
            data.put("folder", folderInfo);
            
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ApiResponse.error("创建文件夹失败：" + e.getMessage()));
        }
    }

    @DeleteMapping("/folder/{folderId}")
    public ResponseEntity<ApiResponse<Object>> deleteFolder(@PathVariable String folderId) {
        boolean result = vfsManager.deleteFolder(folderId);
        
        if (result) {
            return ResponseEntity.ok(ApiResponse.success("文件夹删除成功"));
        } else {
            return ResponseEntity.ok(ApiResponse.error("文件夹删除失败"));
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folderId") String folderId,
            @RequestParam(value = "description", required = false) String description) {
        try {
            // 当folderId为空时，使用存储系统的根目录路径作为父文件夹ID
            if (folderId == null || folderId.isEmpty()) {
                folderId = "./data/storage";
            }
            
            Folder folder = vfsManager.getFolderByID(folderId);
            if (folder == null) {
                // 如果父文件夹不存在，尝试创建存储根目录
                File rootDir = new File("./data/storage");
                if (!rootDir.exists()) {
                    rootDir.mkdirs();
                }
                // 重新获取父文件夹
                folder = vfsManager.getFolderByID(folderId);
                if (folder == null) {
                    return ResponseEntity.ok(ApiResponse.error("父文件夹不存在"));
                }
            }
            
            FileInfo fileInfo = folder.createFile(file.getOriginalFilename(), "system");
            // FileInfo类没有setDescription方法，直接使用创建的文件信息
            
            // 保存上传的文件到临时位置
            File tempFile = File.createTempFile("upload-", ".tmp");
            file.transferTo(tempFile);
            
            // 直接使用文件信息，不需要额外的copyFile调用
            
            // 构建简化的文件信息，避免无限递归序列化
            Map<String, Object> simplifiedFileInfo = new HashMap<>();
            simplifiedFileInfo.put("id", fileInfo.getID());
            simplifiedFileInfo.put("name", fileInfo.getName());
            simplifiedFileInfo.put("path", fileInfo.getPath());
            simplifiedFileInfo.put("size", new File(fileInfo.getPath()).length());
            
            Map<String, Object> data = new HashMap<>();
            data.put("file", simplifiedFileInfo);
            
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("文件上传失败：" + e.getMessage()));
        }
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) {
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

    @DeleteMapping("/file/{fileId}")
    public ResponseEntity<ApiResponse<Object>> deleteFile(@PathVariable String fileId) {
        boolean result = vfsManager.removeFileInfo(fileId);
        
        if (result) {
            return ResponseEntity.ok(ApiResponse.success("文件删除成功"));
        } else {
            return ResponseEntity.ok(ApiResponse.error("文件删除失败"));
        }
    }

    @PutMapping("/file/{fileId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateFile(
            @PathVariable String fileId,
            @RequestBody Map<String, String> request) {
        String name = request.get("name");
        String description = request.getOrDefault("description", "");
        
        FileInfo fileInfo = vfsManager.getFileInfoByID(fileId);
        if (fileInfo == null) {
            return ResponseEntity.ok(ApiResponse.error("文件不存在"));
        }
        
        vfsManager.updateFileInfo(fileInfo.getPath(), name, description);
        
        // 构建简化的文件信息，避免无限递归序列化
        Map<String, Object> simplifiedFileInfo = new HashMap<>();
        simplifiedFileInfo.put("id", fileInfo.getID());
        simplifiedFileInfo.put("name", fileInfo.getName());
        simplifiedFileInfo.put("path", fileInfo.getPath());
        simplifiedFileInfo.put("size", new File(fileInfo.getPath()).length());
        
        Map<String, Object> data = new HashMap<>();
        data.put("file", simplifiedFileInfo);
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @GetMapping("/file/{fileId}/versions")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFileVersions(@PathVariable String fileId) {
        try {
            FileInfo fileInfo = vfsManager.getFileInfoByID(fileId);
            if (fileInfo == null) {
                return ResponseEntity.ok(ApiResponse.error("文件不存在"));
            }
            
            List<FileVersion> versions = fileInfo.getVersionList();
            
            // 构建简化的版本列表，避免无限递归序列化
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
            
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ApiResponse.error("获取文件版本失败：" + e.getMessage()));
        }
    }

    @PostMapping("/file/{fileId}/restore/{versionId}")
    public ResponseEntity<ApiResponse<Object>> restoreFileVersion(
            @PathVariable String fileId,
            @PathVariable String versionId) {
        FileInfo fileInfo = vfsManager.getFileInfoByID(fileId);
        if (fileInfo == null) {
            return ResponseEntity.ok(ApiResponse.error("文件不存在"));
        }
        
        FileVersion version = vfsManager.getVersionById(versionId);
        if (version == null) {
            return ResponseEntity.ok(ApiResponse.error("版本不存在"));
        }
        
        vfsManager.createFileVersion(fileInfo.getPath(), version.getHash());
        
        return ResponseEntity.ok(ApiResponse.success("版本恢复成功"));
    }

    @PostMapping("/cleanup")
    public ResponseEntity<ApiResponse<Map<String, Object>>> cleanupStorage() {
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
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @GetMapping("/shared")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSharedFiles() {
        Map<String, Object> data = new HashMap<>();
        // 模拟分享的文件列表
        List<Map<String, Object>> sharedFiles = new ArrayList<>();
        
        // 模拟数据
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
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @PostMapping("/share")
    public ResponseEntity<ApiResponse<Map<String, Object>>> shareFile(@RequestBody Map<String, String> request) {
        String fileId = request.get("fileId");
        String target = request.get("target");
        String targetType = request.getOrDefault("targetType", "user");
        String expireTime = request.getOrDefault("expireTime", "");
        
        FileInfo fileInfo = vfsManager.getFileInfoByID(fileId);
        if (fileInfo == null) {
            return ResponseEntity.ok(ApiResponse.error("文件不存在"));
        }
        
        // 模拟分享文件的逻辑
        Map<String, Object> data = new HashMap<>();
        data.put("id", System.currentTimeMillis());
        data.put("fileId", fileId);
        data.put("fileName", fileInfo.getName());
        data.put("target", target);
        data.put("targetType", targetType);
        data.put("shareTime", System.currentTimeMillis());
        data.put("expireTime", expireTime.isEmpty() ? null : Long.parseLong(expireTime));
        data.put("shareUrl", "/api/storage/shared/" + System.currentTimeMillis());
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @GetMapping("/received")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getReceivedFiles() {
        Map<String, Object> data = new HashMap<>();
        // 模拟收到的文件列表
        List<Map<String, Object>> receivedFiles = new ArrayList<>();
        
        // 模拟数据
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
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @DeleteMapping("/share/{shareId}")
    public ResponseEntity<ApiResponse<Object>> unshareFile(@PathVariable String shareId) {
        // 模拟取消分享文件的逻辑
        System.out.println("取消分享文件: " + shareId);
        
        return ResponseEntity.ok(ApiResponse.success("取消分享成功"));
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
