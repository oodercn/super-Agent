package net.ooder.skillcenter.service.impl;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.manager.StorageManager;
import net.ooder.skillcenter.service.StorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@ConditionalOnProperty(name = "skillcenter.sdk.mode", havingValue = "sdk")
public class StorageServiceSdkImpl implements StorageService {

    private StorageManager storageManager;

    @PostConstruct
    public void init() {
        storageManager = StorageManager.getInstance();
    }

    @Override
    public Map<String, Object> getStorageStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "正常");
        status.put("exists", true);
        status.put("itemCount", storageManager.getAllStorageItems().size());
        return status;
    }

    @Override
    public Map<String, Object> getStorageStats() {
        return storageManager.getStorageStats();
    }

    @Override
    public Map<String, Object> backupStorage() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "备份成功");
        result.put("backupFile", "backup_" + System.currentTimeMillis() + ".zip");
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> getBackupList(int page, int size, String sortBy, String sortDirection) {
        List<Map<String, Object>> backups = new ArrayList<>();
        Map<String, Object> backup = new HashMap<>();
        backup.put("name", "backup_1.zip");
        backup.put("size", 1024 * 1024 * 10);
        backups.add(backup);
        return PageResult.of(backups, backups.size(), page, size);
    }

    @Override
    public Map<String, Object> restoreStorage(String backupName) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "恢复成功");
        return result;
    }

    @Override
    public Map<String, Object> cleanStorage() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "清理成功");
        return result;
    }

    @Override
    public Map<String, Object> deleteBackup(String backupName) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "删除成功");
        return result;
    }

    @Override
    public Map<String, Object> cleanBackups() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "备份清理成功");
        return result;
    }

    @Override
    public Map<String, Object> getStorageSettings() {
        Map<String, Object> settings = new HashMap<>();
        settings.put("storagePath", "./skillcenter");
        settings.put("autoBackup", false);
        return settings;
    }

    @Override
    public Map<String, Object> updateStorageSettings(Map<String, Object> settings) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "设置更新成功");
        return result;
    }
}
