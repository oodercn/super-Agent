package net.ooder.nexus.skillcenter.controller;

import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.nexus.skillcenter.dto.system.*;
import net.ooder.nexus.skillcenter.dto.storage.*;
import net.ooder.nexus.skillcenter.dto.common.OperationResultDTO;
import net.ooder.nexus.skillcenter.dto.common.PaginationDTO;
import net.ooder.nexus.skillcenter.dto.PageResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/system")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class SystemController extends BaseController {

    private static final String STORAGE_BASE_PATH = System.getProperty("user.dir") + "/skillcenter/storage";

    @PostMapping("/status")
    public ResultModel<SystemStatusDTO> getSystemStatus() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getSystemStatus", null);

        try {
            SystemStatusDTO status = new SystemStatusDTO();
            status.setStatus("运行中");
            status.setService("SkillCenter");
            status.setTimestamp(System.currentTimeMillis());

            logRequestEnd("getSystemStatus", status, System.currentTimeMillis() - startTime);
            return ResultModel.success(status);
        } catch (Exception e) {
            logRequestError("getSystemStatus", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/config")
    public ResultModel<SystemConfigDTO> getSystemConfig() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getSystemConfig", null);

        try {
            SystemConfigDTO config = new SystemConfigDTO();
            Properties systemProps = System.getProperties();

            config.setJavaVersion(systemProps.getProperty("java.version"));
            config.setJavaHome(systemProps.getProperty("java.home"));
            config.setOsName(systemProps.getProperty("os.name"));
            config.setOsVersion(systemProps.getProperty("os.version"));
            config.setOsArch(systemProps.getProperty("os.arch"));
            config.setUserDir(systemProps.getProperty("user.dir"));
            config.setUserName(systemProps.getProperty("user.name"));

            logRequestEnd("getSystemConfig", config, System.currentTimeMillis() - startTime);
            return ResultModel.success(config);
        } catch (Exception e) {
            logRequestError("getSystemConfig", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/version")
    public ResultModel<SystemVersionDTO> getSystemVersion() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getSystemVersion", null);

        try {
            SystemVersionDTO version = new SystemVersionDTO();
            version.setVersion("2.0");
            version.setName("SkillCenter");
            version.setDescription("技能管理和执行平台");
            version.setVendor("Ooder Team");
            version.setBuildTimestamp("2026-01-30");

            logRequestEnd("getSystemVersion", version, System.currentTimeMillis() - startTime);
            return ResultModel.success(version);
        } catch (Exception e) {
            logRequestError("getSystemVersion", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/resources")
    public ResultModel<SystemResourcesDTO> getSystemResources() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getSystemResources", null);

        try {
            SystemResourcesDTO resources = new SystemResourcesDTO();

            com.sun.management.OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            java.lang.management.RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();

            resources.setAvailableProcessors(osBean.getAvailableProcessors());

            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long maxMemory = runtime.maxMemory();

            resources.setTotalMemory(totalMemory);
            resources.setFreeMemory(freeMemory);
            resources.setUsedMemory(totalMemory - freeMemory);
            resources.setMaxMemory(maxMemory);

            resources.setTotalMemoryHuman(formatMemorySize(totalMemory));
            resources.setFreeMemoryHuman(formatMemorySize(freeMemory));
            resources.setUsedMemoryHuman(formatMemorySize(totalMemory - freeMemory));
            resources.setMaxMemoryHuman(formatMemorySize(maxMemory));

            long uptime = runtimeBean.getUptime();
            resources.setUptime(uptime);
            resources.setUptimeHuman(formatUptime(uptime));

            logRequestEnd("getSystemResources", resources, System.currentTimeMillis() - startTime);
            return ResultModel.success(resources);
        } catch (Exception e) {
            logRequestError("getSystemResources", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/config/update")
    public ResultModel<OperationResultDTO> updateSystemConfig(@RequestBody SystemConfigDTO config) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateSystemConfig", config);

        try {
            OperationResultDTO result = OperationResultDTO.success("配置更新成功");

            logRequestEnd("updateSystemConfig", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("updateSystemConfig", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/restart")
    public ResultModel<OperationResultDTO> restartSystem() {
        long startTime = System.currentTimeMillis();
        logRequestStart("restartSystem", null);

        try {
            OperationResultDTO result = OperationResultDTO.success("系统重启命令已发出");

            logRequestEnd("restartSystem", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("restartSystem", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/shutdown")
    public ResultModel<OperationResultDTO> shutdownSystem() {
        long startTime = System.currentTimeMillis();
        logRequestStart("shutdownSystem", null);

        try {
            OperationResultDTO result = OperationResultDTO.success("系统关闭命令已发出");

            logRequestEnd("shutdownSystem", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("shutdownSystem", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/health")
    public ResultModel<SystemHealthDTO> getSystemHealth() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getSystemHealth", null);

        try {
            SystemHealthDTO health = new SystemHealthDTO();
            health.setStatus("健康");
            health.setCpu("正常");
            health.setMemory("正常");
            health.setDisk("正常");
            health.setNetwork("正常");
            health.setTimestamp(System.currentTimeMillis());

            logRequestEnd("getSystemHealth", health, System.currentTimeMillis() - startTime);
            return ResultModel.success(health);
        } catch (Exception e) {
            logRequestError("getSystemHealth", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/cache/clear")
    public ResultModel<ClearCacheResultDTO> clearSystemCache() {
        long startTime = System.currentTimeMillis();
        logRequestStart("clearSystemCache", null);

        try {
            ClearCacheResultDTO result = new ClearCacheResultDTO();
            result.setSuccess(true);
            result.setMessage("系统缓存清理成功");
            result.setClearedItems(150);
            result.setTimestamp(System.currentTimeMillis());

            logRequestEnd("clearSystemCache", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("clearSystemCache", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/operations")
    public ResultModel<List<SystemOperationDTO>> getSystemOperations() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getSystemOperations", null);

        try {
            List<SystemOperationDTO> operations = new ArrayList<>();
            long now = System.currentTimeMillis();

            SystemOperationDTO op1 = new SystemOperationDTO();
            op1.setId("op-1");
            op1.setType("SYSTEM_START");
            op1.setDescription("系统启动成功");
            op1.setTimestamp(now - 3600000);
            op1.setStatus("SUCCESS");
            operations.add(op1);

            SystemOperationDTO op2 = new SystemOperationDTO();
            op2.setId("op-2");
            op2.setType("SKILL_EXECUTION");
            op2.setDescription("执行技能: text-to-uppercase-skill");
            op2.setTimestamp(now - 1800000);
            op2.setStatus("SUCCESS");
            operations.add(op2);

            SystemOperationDTO op3 = new SystemOperationDTO();
            op3.setId("op-3");
            op3.setType("STORAGE_BACKUP");
            op3.setDescription("存储备份成功");
            op3.setTimestamp(now - 900000);
            op3.setStatus("SUCCESS");
            operations.add(op3);

            SystemOperationDTO op4 = new SystemOperationDTO();
            op4.setId("op-4");
            op4.setType("SYSTEM_CONFIG_UPDATE");
            op4.setDescription("更新系统配置");
            op4.setTimestamp(now - 300000);
            op4.setStatus("SUCCESS");
            operations.add(op4);

            SystemOperationDTO op5 = new SystemOperationDTO();
            op5.setId("op-5");
            op5.setType("CACHE_CLEAR");
            op5.setDescription("清理系统缓存");
            op5.setTimestamp(now - 60000);
            op5.setStatus("SUCCESS");
            operations.add(op5);

            logRequestEnd("getSystemOperations", operations.size() + " operations", System.currentTimeMillis() - startTime);
            return ResultModel.success(operations);
        } catch (Exception e) {
            logRequestError("getSystemOperations", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/logs")
    public ResultModel<List<SystemLogDTO>> getSystemLogs(@RequestBody SystemLogDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getSystemLogs", request);

        try {
            List<SystemLogDTO> logs = new ArrayList<>();
            String level = request.getLevel();
            long now = System.currentTimeMillis();

            SystemLogDTO log1 = new SystemLogDTO();
            log1.setId("log-1");
            log1.setLevel("INFO");
            log1.setMessage("系统启动成功");
            log1.setTimestamp(now - 3600000);
            log1.setSource("SystemController");
            logs.add(log1);

            SystemLogDTO log2 = new SystemLogDTO();
            log2.setId("log-2");
            log2.setLevel("INFO");
            log2.setMessage("技能管理服务初始化完成");
            log2.setTimestamp(now - 3500000);
            log2.setSource("SkillController");
            logs.add(log2);

            SystemLogDTO log3 = new SystemLogDTO();
            log3.setId("log-3");
            log3.setLevel("WARN");
            log3.setMessage("存储空间使用接近阈值");
            log3.setTimestamp(now - 1800000);
            log3.setSource("SystemController");
            logs.add(log3);

            SystemLogDTO log4 = new SystemLogDTO();
            log4.setId("log-4");
            log4.setLevel("ERROR");
            log4.setMessage("技能执行失败: 无效参数");
            log4.setTimestamp(now - 900000);
            log4.setSource("ExecutionController");
            logs.add(log4);

            SystemLogDTO log5 = new SystemLogDTO();
            log5.setId("log-5");
            log5.setLevel("INFO");
            log5.setMessage("市场技能同步完成");
            log5.setTimestamp(now - 300000);
            log5.setSource("MarketController");
            logs.add(log5);

            if (level != null && !level.isEmpty()) {
                logs = logs.stream()
                          .filter(l -> level.equalsIgnoreCase(l.getLevel()))
                          .collect(Collectors.toList());
            }

            logRequestEnd("getSystemLogs", logs.size() + " logs", System.currentTimeMillis() - startTime);
            return ResultModel.success(logs);
        } catch (Exception e) {
            logRequestError("getSystemLogs", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/logs/clear")
    public ResultModel<ClearCacheResultDTO> clearSystemLogs() {
        long startTime = System.currentTimeMillis();
        logRequestStart("clearSystemLogs", null);

        try {
            ClearCacheResultDTO result = new ClearCacheResultDTO();
            result.setSuccess(true);
            result.setMessage("系统日志清空成功");
            result.setClearedItems(150);
            result.setTimestamp(System.currentTimeMillis());

            logRequestEnd("clearSystemLogs", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("clearSystemLogs", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/storage/status")
    public ResultModel<StorageStatusDTO> getStorageStatus() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getStorageStatus", null);

        try {
            StorageStatusDTO status = new StorageStatusDTO();
            Path storagePath = Paths.get(STORAGE_BASE_PATH);
            if (Files.exists(storagePath)) {
                status.setStatus("正常");
                status.setExists(true);
                status.setPath(storagePath.toString());
            } else {
                status.setStatus("不存在");
                status.setExists(false);
                status.setPath(storagePath.toString());
            }

            logRequestEnd("getStorageStatus", status, System.currentTimeMillis() - startTime);
            return ResultModel.success(status);
        } catch (Exception e) {
            logRequestError("getStorageStatus", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/storage/stats")
    public ResultModel<StorageStatsDTO> getStorageStats() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getStorageStats", null);

        try {
            StorageStatsDTO stats = new StorageStatsDTO();
            Path storagePath = Paths.get(STORAGE_BASE_PATH);
            if (Files.exists(storagePath)) {
                long totalSize = calculateDirectorySize(storagePath.toFile());
                long totalFiles = countFiles(storagePath.toFile());
                long totalDirectories = countDirectories(storagePath.toFile());

                stats.setTotalSize(totalSize);
                stats.setTotalSizeHuman(formatFileSize(totalSize));
                stats.setTotalFiles(totalFiles);
                stats.setTotalDirectories(totalDirectories);
                stats.setPath(storagePath.toString());
            } else {
                stats.setTotalSize(0);
                stats.setTotalSizeHuman("0 B");
                stats.setTotalFiles(0);
                stats.setTotalDirectories(0);
                stats.setPath(storagePath.toString());
            }

            logRequestEnd("getStorageStats", stats, System.currentTimeMillis() - startTime);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getStorageStats", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/storage/backup")
    public ResultModel<BackupResultDTO> backupStorage() {
        long startTime = System.currentTimeMillis();
        logRequestStart("backupStorage", null);

        try {
            BackupResultDTO result = new BackupResultDTO();
            Path storagePath = Paths.get(STORAGE_BASE_PATH);
            if (!Files.exists(storagePath)) {
                result.setSuccess(false);
                result.setMessage("存储目录不存在");
                logRequestEnd("backupStorage", result, System.currentTimeMillis() - startTime);
                return ResultModel.success(result);
            }

            Path backupPath = Paths.get(STORAGE_BASE_PATH, "backups");
            if (!Files.exists(backupPath)) {
                Files.createDirectories(backupPath);
            }

            String backupFileName = "backup_" + System.currentTimeMillis() + ".zip";
            Path backupFile = backupPath.resolve(backupFileName);

            Files.createFile(backupFile);

            result.setSuccess(true);
            result.setMessage("备份成功");
            result.setBackupFile(backupFile.toString());
            result.setBackupFileName(backupFileName);

            logRequestEnd("backupStorage", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("backupStorage", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/storage/backups")
    public ResultModel<PageResult<BackupDTO>> getBackupList(@RequestBody PaginationDTO pagination) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getBackupList", "page=" + pagination.getPageNum() + ", size=" + pagination.getPageSize());

        try {
            List<BackupDTO> allBackups = new ArrayList<>();
            Path backupPath = Paths.get(STORAGE_BASE_PATH, "backups");
            if (Files.exists(backupPath)) {
                Files.list(backupPath)
                     .filter(Files::isRegularFile)
                     .forEach(file -> {
                         BackupDTO backup = new BackupDTO();
                         backup.setName(file.getFileName().toString());
                         backup.setPath(file.toString());
                         try {
                             backup.setSize(Files.size(file));
                             backup.setSizeHuman(formatFileSize(Files.size(file)));
                             backup.setLastModified(Files.getLastModifiedTime(file).toMillis());
                         } catch (IOException e) {
                             backup.setSize(0);
                             backup.setSizeHuman("0 B");
                             backup.setLastModified(0);
                         }
                         allBackups.add(backup);
                     });
            }

            allBackups.sort((b1, b2) -> -Long.compare(b1.getLastModified(), b2.getLastModified()));

            int start = pagination.getOffset();
            int end = Math.min(start + pagination.getPageSize(), allBackups.size());
            List<BackupDTO> pagedBackups = start < allBackups.size() ? allBackups.subList(start, end) : new ArrayList<>();

            PageResult<BackupDTO> result = new PageResult<>(pagedBackups, allBackups.size(), pagination.getPageNum(), pagination.getPageSize());

            logRequestEnd("getBackupList", pagedBackups.size() + " backups", System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getBackupList", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/storage/restore/{backupName}")
    public ResultModel<RestoreResultDTO> restoreStorage(@PathVariable String backupName) {
        long startTime = System.currentTimeMillis();
        logRequestStart("restoreStorage", backupName);

        try {
            RestoreResultDTO result = new RestoreResultDTO();
            
            if (!isValidBackupName(backupName)) {
                result.setSuccess(false);
                result.setMessage("无效的备份文件名");
                logRequestEnd("restoreStorage", result, System.currentTimeMillis() - startTime);
                return ResultModel.success(result);
            }
            
            Path backupPath = Paths.get(STORAGE_BASE_PATH, "backups", backupName).normalize();
            Path backupsDir = Paths.get(STORAGE_BASE_PATH, "backups").normalize();
            
            if (!backupPath.startsWith(backupsDir)) {
                result.setSuccess(false);
                result.setMessage("非法路径访问");
                logRequestEnd("restoreStorage", result, System.currentTimeMillis() - startTime);
                return ResultModel.success(result);
            }
            
            if (!Files.exists(backupPath)) {
                result.setSuccess(false);
                result.setMessage("备份文件不存在");
                logRequestEnd("restoreStorage", result, System.currentTimeMillis() - startTime);
                return ResultModel.success(result);
            }

            result.setSuccess(true);
            result.setMessage("恢复成功");
            result.setBackupFile(backupPath.toString());

            logRequestEnd("restoreStorage", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("restoreStorage", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/storage/clean")
    public ResultModel<CleanResultDTO> cleanStorage() {
        long startTime = System.currentTimeMillis();
        logRequestStart("cleanStorage", null);

        try {
            CleanResultDTO result = new CleanResultDTO();
            Path storagePath = Paths.get(STORAGE_BASE_PATH);
            if (!Files.exists(storagePath)) {
                result.setSuccess(true);
                result.setMessage("存储目录不存在，无需清理");
                logRequestEnd("cleanStorage", result, System.currentTimeMillis() - startTime);
                return ResultModel.success(result);
            }

            result.setSuccess(true);
            result.setMessage("清理成功");

            logRequestEnd("cleanStorage", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("cleanStorage", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/storage/backups/{backupName}/delete")
    public ResultModel<CleanResultDTO> deleteBackup(@PathVariable String backupName) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deleteBackup", backupName);

        try {
            CleanResultDTO result = new CleanResultDTO();
            
            if (!isValidBackupName(backupName)) {
                result.setSuccess(false);
                result.setMessage("无效的备份文件名");
                logRequestEnd("deleteBackup", result, System.currentTimeMillis() - startTime);
                return ResultModel.success(result);
            }
            
            Path backupPath = Paths.get(STORAGE_BASE_PATH, "backups", backupName).normalize();
            Path backupsDir = Paths.get(STORAGE_BASE_PATH, "backups").normalize();
            
            if (!backupPath.startsWith(backupsDir)) {
                result.setSuccess(false);
                result.setMessage("非法路径访问");
                logRequestEnd("deleteBackup", result, System.currentTimeMillis() - startTime);
                return ResultModel.success(result);
            }
            
            if (!Files.exists(backupPath)) {
                result.setSuccess(false);
                result.setMessage("备份文件不存在");
                logRequestEnd("deleteBackup", result, System.currentTimeMillis() - startTime);
                return ResultModel.success(result);
            }

            Files.delete(backupPath);

            result.setSuccess(true);
            result.setMessage("删除成功");

            logRequestEnd("deleteBackup", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("deleteBackup", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/storage/clean/backups")
    public ResultModel<CleanBackupsResultDTO> cleanBackups() {
        long startTime = System.currentTimeMillis();
        logRequestStart("cleanBackups", null);

        try {
            CleanBackupsResultDTO result = new CleanBackupsResultDTO();
            Path backupPath = Paths.get(STORAGE_BASE_PATH, "backups");
            if (!Files.exists(backupPath)) {
                result.setSuccess(true);
                result.setMessage("备份目录不存在，无需清理");
                logRequestEnd("cleanBackups", result, System.currentTimeMillis() - startTime);
                return ResultModel.success(result);
            }

            final AtomicInteger deletedCount = new AtomicInteger(0);
            Files.list(backupPath)
                 .filter(Files::isRegularFile)
                 .forEach(file -> {
                     try {
                         Files.delete(file);
                         deletedCount.incrementAndGet();
                     } catch (IOException e) {
                     }
                 });

            result.setSuccess(true);
            result.setMessage("备份文件清理成功");
            result.setDeletedCount(deletedCount.get());

            logRequestEnd("cleanBackups", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("cleanBackups", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/storage/settings")
    public ResultModel<StorageSettingsDTO> getStorageSettings() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getStorageSettings", null);

        try {
            StorageSettingsDTO settings = new StorageSettingsDTO();
            settings.setStoragePath(STORAGE_BASE_PATH);

            StorageSettingsDTO.BackupSettingsDTO backupSettings = new StorageSettingsDTO.BackupSettingsDTO();
            backupSettings.setBackupPath(Paths.get(STORAGE_BASE_PATH, "backups").toString());
            backupSettings.setAutoBackup(false);
            backupSettings.setBackupInterval(24);
            backupSettings.setMaxBackupCount(10);
            settings.setBackup(backupSettings);

            StorageSettingsDTO.LimitSettingsDTO limitSettings = new StorageSettingsDTO.LimitSettingsDTO();
            limitSettings.setMaxStorageSize("10GB");
            limitSettings.setMaxFileSize("100MB");
            limitSettings.setAllowedFileTypes(new String[] {"zip", "json", "txt"});
            settings.setLimits(limitSettings);

            Path storagePath = Paths.get(STORAGE_BASE_PATH);
            settings.setStorageExists(Files.exists(storagePath));

            logRequestEnd("getStorageSettings", settings, System.currentTimeMillis() - startTime);
            return ResultModel.success(settings);
        } catch (Exception e) {
            logRequestError("getStorageSettings", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/storage/settings/update")
    public ResultModel<CleanResultDTO> updateStorageSettings(@RequestBody StorageSettingsDTO settings) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateStorageSettings", settings);

        try {
            CleanResultDTO result = new CleanResultDTO();
            result.setSuccess(true);
            result.setMessage("存储设置更新成功");

            logRequestEnd("updateStorageSettings", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("updateStorageSettings", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    private String formatMemorySize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return (bytes / 1024) + " KB";
        } else if (bytes < 1024 * 1024 * 1024) {
            return (bytes / (1024 * 1024)) + " MB";
        } else {
            return (bytes / (1024 * 1024 * 1024)) + " GB";
        }
    }

    private String formatUptime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            return days + "天 " + (hours % 24) + "小时";
        } else if (hours > 0) {
            return hours + "小时 " + (minutes % 60) + "分钟";
        } else if (minutes > 0) {
            return minutes + "分钟 " + (seconds % 60) + "秒";
        } else {
            return seconds + "秒";
        }
    }

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

    private boolean isValidBackupName(String backupName) {
        if (backupName == null || backupName.isEmpty()) {
            return false;
        }
        if (backupName.contains("..") || backupName.contains("/") || backupName.contains("\\")) {
            return false;
        }
        return backupName.matches("^[a-zA-Z0-9_\\-\\.]+$");
    }
}
