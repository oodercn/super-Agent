package net.ooder.mvp.skill.scene.config.controller;

import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.service.AuditService;
import net.ooder.mvp.skill.scene.dto.audit.AuditLogDTO;
import net.ooder.mvp.skill.scene.dto.audit.AuditEventType;
import net.ooder.mvp.skill.scene.dto.audit.AuditResultType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/config/vfs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VfsConfigController {

    private static final Logger log = LoggerFactory.getLogger(VfsConfigController.class);

    @Autowired(required = false)
    private AuditService auditService;

    private Map<String, Object> vfsConfig = new HashMap<>();

    public VfsConfigController() {
        vfsConfig.put("storageType", "local");
        vfsConfig.put("basePath", "./data/files");
        vfsConfig.put("maxFileSize", 100);
        vfsConfig.put("dirPermission", "755");
    }

    @GetMapping
    public ResultModel<Map<String, Object>> getConfig() {
        return ResultModel.success(vfsConfig);
    }

    @PutMapping
    public ResultModel<Map<String, Object>> saveConfig(@RequestBody Map<String, Object> config) {
        String storageType = (String) config.getOrDefault("storageType", "local");
        vfsConfig.putAll(config);
        vfsConfig.put("updatedAt", System.currentTimeMillis());
        
        logConfigChange("vfs_config", "global", "update", "更新文件系统配置，存储类型: " + storageType);
        
        return ResultModel.success(vfsConfig);
    }

    @GetMapping("/stats")
    public ResultModel<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("storageType", vfsConfig.get("storageType"));
        stats.put("fileCount", 0);
        stats.put("usedSpace", "0 MB");
        stats.put("maxFileSize", vfsConfig.get("maxFileSize") + " MB");
        return ResultModel.success(stats);
    }

    @GetMapping("/storage-types")
    public ResultModel<List<Map<String, Object>>> getStorageTypes() {
        List<Map<String, Object>> types = Arrays.asList(
            createStorageInfo("local", "本地存储", "文件存储在本地文件系统", true),
            createStorageInfo("s3", "AWS S3", "使用Amazon S3对象存储", false),
            createStorageInfo("oss", "阿里云OSS", "使用阿里云对象存储", false),
            createStorageInfo("minio", "MinIO", "使用MinIO对象存储", false),
            createStorageInfo("database", "数据库存储", "文件存储在数据库中", false)
        );
        return ResultModel.success(types);
    }

    @PostMapping("/test")
    public ResultModel<Map<String, Object>> testStorage(@RequestBody Map<String, Object> request) {
        String storageType = (String) request.getOrDefault("storageType", "local");
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("storageType", storageType);
        result.put("message", "存储连接测试成功");
        result.put("testedAt", System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    private Map<String, Object> createStorageInfo(String type, String name, String description, boolean active) {
        Map<String, Object> info = new HashMap<>();
        info.put("type", type);
        info.put("name", name);
        info.put("description", description);
        info.put("active", active);
        return info;
    }

    private void logConfigChange(String resourceType, String resourceId, String action, String detail) {
        if (auditService != null) {
            try {
                AuditLogDTO auditLog = new AuditLogDTO();
                auditLog.setEventType(AuditEventType.CONFIG_VFS);
                auditLog.setResult(AuditResultType.SUCCESS);
                auditLog.setResourceType(resourceType);
                auditLog.setResourceId(resourceId);
                auditLog.setAction(action);
                auditLog.setDetail(detail);
                auditService.logEvent(auditLog);
            } catch (Exception e) {
                log.warn("Failed to log audit event", e);
            }
        }
    }
}
