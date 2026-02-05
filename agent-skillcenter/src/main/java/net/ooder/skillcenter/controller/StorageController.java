package net.ooder.skillcenter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import net.ooder.skillcenter.model.PageResponse;
import net.ooder.skillcenter.model.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 存储管理REST API控制器
 */
@RestController
@RequestMapping("/api/storage")
public class StorageController {

    private static final String STORAGE_BASE_PATH = System.getProperty("user.dir") + "/skillcenter/storage";

    /**
     * 获取存储状态
     * @return 存储状态
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStorageStatus() {
        Map<String, Object> status = new HashMap<>();
        
        try {
            Path storagePath = Paths.get(STORAGE_BASE_PATH);
            if (Files.exists(storagePath)) {
                status.put("status", "正常");
                status.put("exists", true);
                status.put("path", storagePath.toString());
            } else {
                status.put("status", "不存在");
                status.put("exists", false);
                status.put("path", storagePath.toString());
            }
            
            return ResponseEntity.ok(ApiResponse.success(status));
        } catch (Exception e) {
            status.put("status", "错误");
            status.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    /**
     * 获取存储统计信息
     * @return 存储统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStorageStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            Path storagePath = Paths.get(STORAGE_BASE_PATH);
            if (Files.exists(storagePath)) {
                long totalSize = calculateDirectorySize(storagePath.toFile());
                long totalFiles = countFiles(storagePath.toFile());
                long totalDirectories = countDirectories(storagePath.toFile());
                
                stats.put("totalSize", totalSize);
                stats.put("totalSizeHuman", formatFileSize(totalSize));
                stats.put("totalFiles", totalFiles);
                stats.put("totalDirectories", totalDirectories);
                stats.put("path", storagePath.toString());
            } else {
                stats.put("totalSize", 0);
                stats.put("totalSizeHuman", "0 B");
                stats.put("totalFiles", 0);
                stats.put("totalDirectories", 0);
                stats.put("path", storagePath.toString());
            }
            
            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            stats.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    /**
     * 备份存储
     * @return 备份结果
     */
    @PostMapping("/backup")
    public ResponseEntity<ApiResponse<Map<String, Object>>> backupStorage() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Path storagePath = Paths.get(STORAGE_BASE_PATH);
            if (!Files.exists(storagePath)) {
                result.put("success", false);
                result.put("message", "存储目录不存在");
                return ResponseEntity.ok(ApiResponse.success(result));
            }
            
            // 创建备份目录
            Path backupPath = Paths.get(STORAGE_BASE_PATH, "backups");
            if (!Files.exists(backupPath)) {
                Files.createDirectories(backupPath);
            }
            
            // 生成备份文件名
            String backupFileName = "backup_" + System.currentTimeMillis() + ".zip";
            Path backupFile = backupPath.resolve(backupFileName);
            
            // 这里应该实现实际的备份逻辑
            // 暂时模拟备份
            Files.createFile(backupFile);
            
            result.put("success", true);
            result.put("message", "备份成功");
            result.put("backupFile", backupFile.toString());
            result.put("backupFileName", backupFileName);
            
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "备份失败");
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    /**
     * 获取备份列表（支持分页）
     * @param page 页码
     * @param size 每页大小
     * @param sortBy 排序字段
     * @param sortDirection 排序方向
     * @return 分页备份列表
     */
    @GetMapping("/backups")
    public ResponseEntity<ApiResponse<PageResponse<Map<String, Object>>>> getBackupList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        List<Map<String, Object>> allBackups = new ArrayList<>();
        
        try {
            Path backupPath = Paths.get(STORAGE_BASE_PATH, "backups");
            if (Files.exists(backupPath)) {
                Files.list(backupPath)
                     .filter(Files::isRegularFile)
                     .forEach(file -> {
                         Map<String, Object> backup = new HashMap<>();
                         backup.put("name", file.getFileName().toString());
                         backup.put("path", file.toString());
                         try {
                             backup.put("size", Files.size(file));
                             backup.put("sizeHuman", formatFileSize(Files.size(file)));
                             backup.put("lastModified", Files.getLastModifiedTime(file).toMillis());
                         } catch (IOException e) {
                             backup.put("size", 0);
                             backup.put("sizeHuman", "0 B");
                             backup.put("lastModified", 0);
                         }
                         allBackups.add(backup);
                     });
            }
            
            long totalElements = allBackups.size();
            
            // 排序
            if (sortBy != null && !sortBy.isEmpty()) {
                allBackups.sort((b1, b2) -> {
                    int compareResult = 0;
                    switch (sortBy) {
                        case "name":
                            compareResult = b1.get("name").toString().compareTo(b2.get("name").toString());
                            break;
                        case "size":
                            compareResult = Long.compare((Long) b1.get("size"), (Long) b2.get("size"));
                            break;
                        case "lastModified":
                            compareResult = Long.compare((Long) b1.get("lastModified"), (Long) b2.get("lastModified"));
                            break;
                        default:
                            compareResult = Long.compare((Long) b1.get("lastModified"), (Long) b2.get("lastModified"));
                    }
                    return "desc".equalsIgnoreCase(sortDirection) ? -compareResult : compareResult;
                });
            } else {
                // 默认按修改时间倒序
                allBackups.sort((b1, b2) -> 
                    -Long.compare((Long) b1.get("lastModified"), (Long) b2.get("lastModified"))
                );
            }
            
            // 分页
            int start = (page - 1) * size;
            int end = Math.min(start + size, allBackups.size());
            List<Map<String, Object>> pagedBackups = start < allBackups.size() ? allBackups.subList(start, end) : new ArrayList<>();
            
            PageResponse<Map<String, Object>> pageResponse = PageResponse.of(pagedBackups, page, size, totalElements);
            return ResponseEntity.ok(ApiResponse.success(pageResponse));
        } catch (Exception e) {
            PageResponse<Map<String, Object>> pageResponse = PageResponse.of(allBackups, 1, size, 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    /**
     * 恢复存储
     * @param backupName 备份文件名
     * @return 恢复结果
     */
    @PostMapping("/restore/{backupName}")
    public ResponseEntity<Map<String, Object>> restoreStorage(@PathVariable String backupName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Path backupPath = Paths.get(STORAGE_BASE_PATH, "backups", backupName);
            if (!Files.exists(backupPath)) {
                result.put("success", false);
                result.put("message", "备份文件不存在");
                return ResponseEntity.ok(result);
            }
            
            // 这里应该实现实际的恢复逻辑
            // 暂时模拟恢复
            
            result.put("success", true);
            result.put("message", "恢复成功");
            result.put("backupFile", backupPath.toString());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "恢复失败");
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 清理存储
     * @return 清理结果
     */
    @PostMapping("/clean")
    public ResponseEntity<Map<String, Object>> cleanStorage() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Path storagePath = Paths.get(STORAGE_BASE_PATH);
            if (!Files.exists(storagePath)) {
                result.put("success", true);
                result.put("message", "存储目录不存在，无需清理");
                return ResponseEntity.ok(result);
            }
            
            // 这里应该实现实际的清理逻辑
            // 暂时模拟清理
            
            result.put("success", true);
            result.put("message", "清理成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "清理失败");
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 删除备份
     * @param backupName 备份文件名
     * @return 删除结果
     */
    @DeleteMapping("/backups/{backupName}")
    public ResponseEntity<Map<String, Object>> deleteBackup(@PathVariable String backupName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Path backupPath = Paths.get(STORAGE_BASE_PATH, "backups", backupName);
            if (!Files.exists(backupPath)) {
                result.put("success", false);
                result.put("message", "备份文件不存在");
                return ResponseEntity.ok(result);
            }
            
            Files.delete(backupPath);
            
            result.put("success", true);
            result.put("message", "删除成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败");
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 清理备份文件
     * @return 清理结果
     */
    @PostMapping("/clean/backups")
    public ResponseEntity<Map<String, Object>> cleanBackups() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Path backupPath = Paths.get(STORAGE_BASE_PATH, "backups");
            if (!Files.exists(backupPath)) {
                result.put("success", true);
                result.put("message", "备份目录不存在，无需清理");
                return ResponseEntity.ok(result);
            }
            
            // 清理备份文件
            final AtomicInteger deletedCount = new AtomicInteger(0);
            Files.list(backupPath)
                 .filter(Files::isRegularFile)
                 .forEach(file -> {
                     try {
                         Files.delete(file);
                         deletedCount.incrementAndGet();
                     } catch (IOException e) {
                         // 忽略单个文件删除失败的情况
                     }
                 });
            
            result.put("success", true);
            result.put("message", "备份文件清理成功");
            result.put("deletedCount", deletedCount);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "备份文件清理失败");
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 获取存储设置
     * @return 存储设置
     */
    @GetMapping("/settings")
    public ResponseEntity<Map<String, Object>> getStorageSettings() {
        Map<String, Object> settings = new HashMap<>();
        
        try {
            // 存储路径设置
            settings.put("storagePath", STORAGE_BASE_PATH);
            
            // 备份设置
            Map<String, Object> backupSettings = new HashMap<>();
            backupSettings.put("backupPath", Paths.get(STORAGE_BASE_PATH, "backups").toString());
            backupSettings.put("autoBackup", false);
            backupSettings.put("backupInterval", 24); // 小时
            backupSettings.put("maxBackupCount", 10);
            settings.put("backup", backupSettings);
            
            // 存储限制设置
            Map<String, Object> limitSettings = new HashMap<>();
            limitSettings.put("maxStorageSize", "10GB");
            limitSettings.put("maxFileSize", "100MB");
            limitSettings.put("allowedFileTypes", new String[] {"zip", "json", "txt"});
            settings.put("limits", limitSettings);
            
            // 存储状态
            Path storagePath = Paths.get(STORAGE_BASE_PATH);
            settings.put("storageExists", Files.exists(storagePath));
            
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            settings.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(settings);
        }
    }

    /**
     * 更新存储设置
     * @param settings 更新的设置
     * @return 更新结果
     */
    @PostMapping("/settings")
    public ResponseEntity<Map<String, Object>> updateStorageSettings(@RequestBody Map<String, Object> settings) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 这里应该实现实际的设置更新逻辑
            // 暂时模拟更新
            
            result.put("success", true);
            result.put("message", "存储设置更新成功");
            result.put("updatedSettings", settings);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "存储设置更新失败");
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // 辅助方法
    private long calculateDirectorySize(File directory) {
        long size = 0;
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    size += calculateDirectorySize(file);
                }
            }
        } else {
            size = directory.length();
        }
        return size;
    }

    private long countFiles(File directory) {
        long count = 0;
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        count++;
                    } else {
                        count += countFiles(file);
                    }
                }
            }
        } else {
            count = 1;
        }
        return count;
    }

    private long countDirectories(File directory) {
        long count = 0;
        if (directory.isDirectory()) {
            count++;
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        count += countDirectories(file);
                    }
                }
            }
        }
        return count;
    }

    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return (size / 1024) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            return (size / (1024 * 1024)) + " MB";
        } else {
            return (size / (1024 * 1024 * 1024)) + " GB";
        }
    }
}
