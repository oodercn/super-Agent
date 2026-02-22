package net.ooder.nexus.service;

import java.util.List;
import java.util.Map;

public interface AdminStorageService {

    List<Map<String, Object>> getAllStorage();

    Map<String, Object> getStorageById(String storageId);

    Map<String, Object> createStorage(Map<String, Object> storageData);

    Map<String, Object> updateStorage(String storageId, Map<String, Object> storageData);

    boolean deleteStorage(String storageId);

    Map<String, Object> getStorageSettings();

    boolean updateStorageSettings(Map<String, Object> settings);

    List<Map<String, Object>> getBackupList();

    boolean createBackup();

    boolean restoreBackup(String backupId);

    boolean deleteBackup(String backupId);
}
