/*
 * Copyright (c) 2024 Ooder Team
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */
package net.ooder.skillcenter.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Storage Manager - Manages storage service registration and switching
 * Default uses VFS storage, also supports JSON storage
 */
public class StorageManager {
    private static final Logger logger = LoggerFactory.getLogger(StorageManager.class);

    private static StorageManager instance;

    private Map<String, StorageService> storageServices;
    private StorageService defaultStorage;
    private StorageType defaultStorageType;

    private StorageManager() {
        storageServices = new ConcurrentHashMap<>();
        // 注册默认存储服务
        registerDefaultStorageServices();
        // 设置默认存储类型为VFS
        setDefaultStorage(StorageType.VFS);
    }

    /**
     * 获取存储管理器实例
     * @return 存储管理器实例
     */
    public static synchronized StorageManager getInstance() {
        if (instance == null) {
            instance = new StorageManager();
        }
        return instance;
    }

    /**
     * 注册默认存储服务
     */
    private void registerDefaultStorageServices() {
        // 注册JSON存储服务
        JsonStorageService jsonStorage = new JsonStorageService();
        storageServices.put(StorageType.JSON.name().toLowerCase(), jsonStorage);

        // 注册VFS存储服务
        VfsStorageService vfsStorage = new VfsStorageService();
        storageServices.put(StorageType.VFS.name().toLowerCase(), vfsStorage);
    }

    /**
     * 注册存储服务
     * @param name 存储服务名称
     * @param service 存储服务实例
     */
    public void registerStorageService(String name, StorageService service) {
        storageServices.put(name.toLowerCase(), service);
        logger.info("Storage service registered: {}", name);
    }

    /**
     * 获取存储服务
     * @param name 存储服务名称
     * @return 存储服务实例
     */
    public StorageService getStorageService(String name) {
        return storageServices.get(name.toLowerCase());
    }

    /**
     * 设置默认存储服务
     * @param type 存储类型
     */
    public void setDefaultStorage(StorageType type) {
        defaultStorageType = type;
        defaultStorage = storageServices.get(type.name().toLowerCase());
        if (defaultStorage != null) {
            // 确保默认存储服务已初始化
            if (defaultStorage.getStatus() == StorageStatus.UNINITIALIZED) {
                defaultStorage.initialize();
            }
            logger.info("Default storage set to: {}", type.name());
        }
    }

    /**
     * 获取默认存储服务
     * @return 默认存储服务实例
     */
    public StorageService getDefaultStorage() {
        return defaultStorage;
    }

    /**
     * 获取默认存储类型
     * @return 默认存储类型
     */
    public StorageType getDefaultStorageType() {
        return defaultStorageType;
    }

    /**
     * 初始化所有存储服务
     */
    public void initializeAll() {
        for (StorageService service : storageServices.values()) {
            if (service.getStatus() == StorageStatus.UNINITIALIZED) {
                service.initialize();
            }
        }
        logger.info("All storage services initialized");
    }

    /**
     * 关闭所有存储服务
     */
    public void closeAll() {
        for (StorageService service : storageServices.values()) {
            if (service.getStatus() == StorageStatus.RUNNING) {
                service.close();
            }
        }
        logger.info("All storage services closed");
    }

    /**
     * 存储类型枚举
     */
    public enum StorageType {
        JSON,  // JSON文件存储
        VFS    // VFS存储
    }
}
