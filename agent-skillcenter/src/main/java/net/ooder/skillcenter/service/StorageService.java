package net.ooder.skillcenter.service;

import net.ooder.skillcenter.dto.PageResult;

import java.util.Map;

public interface StorageService {
    Map<String, Object> getStorageStatus();
    Map<String, Object> getStorageStats();
    Map<String, Object> backupStorage();
    PageResult<Map<String, Object>> getBackupList(int page, int size, String sortBy, String sortDirection);
    Map<String, Object> restoreStorage(String backupName);
    Map<String, Object> cleanStorage();
    Map<String, Object> deleteBackup(String backupName);
    Map<String, Object> cleanBackups();
    Map<String, Object> getStorageSettings();
    Map<String, Object> updateStorageSettings(Map<String, Object> settings);
}
