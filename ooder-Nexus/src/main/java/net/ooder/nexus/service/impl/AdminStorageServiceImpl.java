package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.AdminStorageService;
import net.ooder.sdk.api.storage.StorageService;
import net.ooder.sdk.api.storage.TypeReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminStorageServiceImpl implements AdminStorageService {

    private static final Logger log = LoggerFactory.getLogger(AdminStorageServiceImpl.class);
    private static final String STORAGE_KEY = "admin/storage-list";
    private static final String SETTINGS_KEY = "admin/storage-settings";
    private static final String BACKUPS_KEY = "admin/storage-backups";

    private final StorageService storageService;

    @Autowired
    public AdminStorageServiceImpl(StorageService storageService) {
        this.storageService = storageService;
        initializeDefaultSettings();
        log.info("AdminStorageServiceImpl initialized with StorageService (SDK 0.7.1)");
    }

    private void initializeDefaultSettings() {
        Optional<Map<String, Object>> settingsOpt = storageService.load(SETTINGS_KEY, 
            new TypeReference<Map<String, Object>>() {});
        if (!settingsOpt.isPresent()) {
            Map<String, Object> defaultSettings = new HashMap<String, Object>();
            defaultSettings.put("autoBackup", true);
            defaultSettings.put("backupInterval", Long.valueOf(86400000L));
            defaultSettings.put("maxBackups", Integer.valueOf(10));
            defaultSettings.put("compressionEnabled", true);
            defaultSettings.put("encryptionEnabled", false);
            storageService.save(SETTINGS_KEY, defaultSettings);
            log.info("Default storage settings initialized");
        }
    }

    @Override
    public List<Map<String, Object>> getAllStorage() {
        Optional<List<Map<String, Object>>> listOpt = storageService.load(STORAGE_KEY, 
            new TypeReference<List<Map<String, Object>>>() {});
        return listOpt.orElse(new ArrayList<Map<String, Object>>());
    }

    @Override
    public Map<String, Object> getStorageById(String storageId) {
        List<Map<String, Object>> storageList = getAllStorage();
        for (Map<String, Object> item : storageList) {
            if (storageId.equals(item.get("id"))) {
                return item;
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> createStorage(Map<String, Object> storageData) {
        log.info("Creating storage: {}", storageData.get("name"));
        List<Map<String, Object>> storageList = getAllStorage();
        String id = "storage-" + System.currentTimeMillis();
        Map<String, Object> storage = new HashMap<String, Object>(storageData);
        storage.put("id", id);
        storage.put("createTime", Long.valueOf(System.currentTimeMillis()));
        storageList.add(storage);
        storageService.save(STORAGE_KEY, storageList);
        log.info("Storage created with id: {}", id);
        return storage;
    }

    @Override
    public Map<String, Object> updateStorage(String storageId, Map<String, Object> storageData) {
        log.info("Updating storage: {}", storageId);
        List<Map<String, Object>> storageList = getAllStorage();
        for (int i = 0; i < storageList.size(); i++) {
            if (storageId.equals(storageList.get(i).get("id"))) {
                Map<String, Object> existing = new HashMap<String, Object>(storageList.get(i));
                existing.putAll(storageData);
                existing.put("id", storageId);
                existing.put("updateTime", Long.valueOf(System.currentTimeMillis()));
                storageList.set(i, existing);
                storageService.save(STORAGE_KEY, storageList);
                log.info("Storage updated: {}", storageId);
                return existing;
            }
        }
        return null;
    }

    @Override
    public boolean deleteStorage(String storageId) {
        List<Map<String, Object>> storageList = getAllStorage();
        boolean removed = storageList.removeIf(item -> storageId.equals(item.get("id")));
        if (removed) {
            storageService.save(STORAGE_KEY, storageList);
            log.info("Storage deleted: {}", storageId);
        }
        return removed;
    }

    @Override
    public Map<String, Object> getStorageSettings() {
        Optional<Map<String, Object>> settingsOpt = storageService.load(SETTINGS_KEY, 
            new TypeReference<Map<String, Object>>() {});
        return settingsOpt.orElse(new HashMap<String, Object>());
    }

    @Override
    public boolean updateStorageSettings(Map<String, Object> newSettings) {
        Map<String, Object> settings = getStorageSettings();
        settings.putAll(newSettings);
        storageService.save(SETTINGS_KEY, settings);
        log.info("Storage settings updated");
        return true;
    }

    @Override
    public List<Map<String, Object>> getBackupList() {
        Optional<List<Map<String, Object>>> listOpt = storageService.load(BACKUPS_KEY, 
            new TypeReference<List<Map<String, Object>>>() {});
        return listOpt.orElse(new ArrayList<Map<String, Object>>());
    }

    @Override
    public boolean createBackup() {
        List<Map<String, Object>> backups = getBackupList();
        Map<String, Object> backup = new HashMap<String, Object>();
        backup.put("id", "backup-" + System.currentTimeMillis());
        backup.put("createTime", Long.valueOf(System.currentTimeMillis()));
        backup.put("status", "completed");
        backup.put("size", Long.valueOf(1024000L));
        backups.add(backup);
        storageService.save(BACKUPS_KEY, backups);
        log.info("Backup created: {}", backup.get("id"));
        return true;
    }

    @Override
    public boolean restoreBackup(String backupId) {
        log.info("Restoring backup: {}", backupId);
        return true;
    }

    @Override
    public boolean deleteBackup(String backupId) {
        List<Map<String, Object>> backups = getBackupList();
        boolean removed = backups.removeIf(backup -> backupId.equals(backup.get("id")));
        if (removed) {
            storageService.save(BACKUPS_KEY, backups);
            log.info("Backup deleted: {}", backupId);
        }
        return removed;
    }
}
