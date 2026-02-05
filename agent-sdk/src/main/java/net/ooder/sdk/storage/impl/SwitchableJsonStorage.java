package net.ooder.sdk.storage.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.ooder.sdk.storage.JsonStorage;
import net.ooder.sdk.storage.StorageConfig;
import net.ooder.sdk.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 可切换的JSON存储实现类，提供VFS优先、本地存储作为fallback的存储服务
 */
public class SwitchableJsonStorage implements JsonStorage {
    private static final Logger log = LoggerFactory.getLogger(SwitchableJsonStorage.class);
    private static final long VFS_CHECK_INTERVAL = 30; // 30秒检查一次VFS可用性

    private final VfsJsonStorage vfsJsonStorage;
    private final LocalJsonStorage localJsonStorage;
    private final StorageConfig config;
    private final VfsClient vfsClient;
    private volatile boolean isVfsAvailable;
    private final ScheduledExecutorService vfsCheckExecutor;

    /**
     * 构造函数
     * @param config 存储配置
     */
    public SwitchableJsonStorage(StorageConfig config) {
        this.config = config;
        this.vfsClient = new VfsClient(config.getVfsConfig());
        this.vfsJsonStorage = new VfsJsonStorage(config);
        this.localJsonStorage = new LocalJsonStorage(config);
        this.isVfsAvailable = checkVfsAvailability();
        this.vfsCheckExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "vfs-availability-checker");
            t.setDaemon(true);
            return t;
        });

        // 定期检查VFS可用性
        startVfsAvailabilityCheck();
    }

    /**
     * 检查VFS可用性
     * @return VFS是否可用
     */
    private boolean checkVfsAvailability() {
        if (!config.getVfsConfig().isEnableVfs()) {
            return false;
        }
        
        boolean available = vfsClient.isVfsAvailable();
        if (available != isVfsAvailable) {
            if (available) {
                log.info("VFS server is now available, switching to VFS storage");
                // 可以在这里添加数据同步逻辑，将本地数据同步到VFS
            } else {
                log.warn("VFS server is no longer available, switching to local storage");
            }
        }
        return available;
    }

    /**
     * 启动VFS可用性检查任务
     */
    private void startVfsAvailabilityCheck() {
        if (config.getVfsConfig().isEnableVfs()) {
            vfsCheckExecutor.scheduleAtFixedRate(() -> {
                try {
                    isVfsAvailable = checkVfsAvailability();
                } catch (Exception e) {
                    log.error("Error checking VFS availability: {}", e.getMessage());
                }
            }, VFS_CHECK_INTERVAL, VFS_CHECK_INTERVAL, TimeUnit.SECONDS);
        }
    }

    /**
     * 获取当前使用的存储服务
     * @return 当前使用的存储服务
     */
    private JsonStorage getCurrentStorage() {
        if (isVfsAvailable) {
            return vfsJsonStorage;
        } else {
            return localJsonStorage;
        }
    }

    @Override
    public <T> boolean save(String key, T data) {
        JsonStorage storage = getCurrentStorage();
        log.debug("Saving data with key '{}' to {} storage", key, storage instanceof VfsJsonStorage ? "VFS" : "local");
        
        boolean success = storage.save(key, data);
        
        // 如果使用本地存储且VFS不可用但已启用，记录日志
        if (storage instanceof LocalJsonStorage && config.getVfsConfig().isEnableVfs()) {
            log.info("Using local storage for save operation (key: {}), VFS is not available", key);
        }
        
        return success;
    }

    @Override
    public <T> T load(String key, Class<T> clazz) {
        JsonStorage storage = getCurrentStorage();
        log.debug("Loading data with key '{}' from {} storage", key, storage instanceof VfsJsonStorage ? "VFS" : "local");
        
        T data = storage.load(key, clazz);
        
        // 如果从VFS加载失败且启用了fallback，尝试从本地加载
        if (data == null && storage instanceof VfsJsonStorage && config.getVfsConfig().isEnableVfsFallback()) {
            log.info("Failed to load data from VFS (key: {}), trying local storage", key);
            data = localJsonStorage.load(key, clazz);
        }
        
        return data;
    }

    @Override
    public boolean delete(String key) {
        JsonStorage storage = getCurrentStorage();
        log.debug("Deleting data with key '{}' from {} storage", key, storage instanceof VfsJsonStorage ? "VFS" : "local");
        
        boolean success = storage.delete(key);
        
        // 如果使用VFS删除，同时也从本地删除以保持一致性
        if (storage instanceof VfsJsonStorage) {
            localJsonStorage.delete(key);
        }
        
        return success;
    }

    @Override
    public boolean exists(String key) {
        JsonStorage storage = getCurrentStorage();
        boolean exists = storage.exists(key);
        
        // 如果从VFS检查不存在且启用了fallback，尝试从本地检查
        if (!exists && storage instanceof VfsJsonStorage && config.getVfsConfig().isEnableVfsFallback()) {
            exists = localJsonStorage.exists(key);
        }
        
        return exists;
    }

    @Override
    public <T> boolean saveBatch(Map<String, T> dataMap) {
        JsonStorage storage = getCurrentStorage();
        log.debug("Saving batch data to {} storage", storage instanceof VfsJsonStorage ? "VFS" : "local");
        return storage.saveBatch(dataMap);
    }

    @Override
    public <T> Map<String, T> loadBatch(List<String> keys, Class<T> clazz) {
        JsonStorage storage = getCurrentStorage();
        log.debug("Loading batch data from {} storage", storage instanceof VfsJsonStorage ? "VFS" : "local");
        return storage.loadBatch(keys, clazz);
    }

    @Override
    public boolean deleteBatch(List<String> keys) {
        JsonStorage storage = getCurrentStorage();
        log.debug("Deleting batch data from {} storage", storage instanceof VfsJsonStorage ? "VFS" : "local");
        boolean success = storage.deleteBatch(keys);
        
        // 如果使用VFS删除，同时也从本地删除以保持一致性
        if (storage instanceof VfsJsonStorage) {
            localJsonStorage.deleteBatch(keys);
        }
        
        return success;
    }

    @Override
    public <T> CompletableFuture<Boolean> saveAsync(String key, T data) {
        return CompletableFuture.supplyAsync(() -> save(key, data));
    }

    @Override
    public <T> CompletableFuture<T> loadAsync(String key, Class<T> clazz) {
        return CompletableFuture.supplyAsync(() -> load(key, clazz));
    }

    @Override
    public Transaction beginTransaction() {
        // 使用本地存储的事务功能，因为VFS不支持事务
        return localJsonStorage.beginTransaction();
    }

    @Override
    public boolean saveJson(String key, JSONObject json) {
        return save(key, json);
    }

    @Override
    public JSONObject loadJson(String key) {
        return load(key, JSONObject.class);
    }

    @Override
    public <T> T loadJson(String key, TypeReference<T> typeRef) {
        JsonStorage storage = getCurrentStorage();
        T data = null;
        
        try {
            data = storage.loadJson(key, typeRef);
        } catch (Exception e) {
            log.error("Failed to load JSON from {} storage for key: {}", 
                    storage instanceof VfsJsonStorage ? "VFS" : "local", key, e);
        }
        
        // 如果从VFS加载失败且启用了fallback，尝试从本地加载
        if (data == null && storage instanceof VfsJsonStorage && config.getVfsConfig().isEnableVfsFallback()) {
            log.info("Failed to load JSON from VFS (key: {}), trying local storage", key);
            data = localJsonStorage.loadJson(key, typeRef);
        }
        
        return data;
    }

    @Override
    public boolean updateJson(String key, Map<String, Object> updates) {
        JsonStorage storage = getCurrentStorage();
        boolean success = storage.updateJson(key, updates);
        
        // 如果使用VFS更新，同时也从本地更新以保持一致性
        if (storage instanceof VfsJsonStorage) {
            localJsonStorage.updateJson(key, updates);
        }
        
        return success;
    }

    @Override
    public <T> T getJsonField(String key, String fieldName, Class<T> fieldType) {
        JsonStorage storage = getCurrentStorage();
        T fieldValue = storage.getJsonField(key, fieldName, fieldType);
        
        // 如果从VFS获取失败且启用了fallback，尝试从本地获取
        if (fieldValue == null && storage instanceof VfsJsonStorage && config.getVfsConfig().isEnableVfsFallback()) {
            log.info("Failed to get JSON field from VFS (key: {}, field: {}), trying local storage", key, fieldName);
            fieldValue = localJsonStorage.getJsonField(key, fieldName, fieldType);
        }
        
        return fieldValue;
    }

    @Override
    public <T> List<T> findByField(String directory, String fieldName, Object fieldValue, Class<T> clazz) {
        JsonStorage storage = getCurrentStorage();
        List<T> results = storage.findByField(directory, fieldName, fieldValue, clazz);
        
        // 如果从VFS查询为空且启用了fallback，尝试从本地查询
        if (results.isEmpty() && storage instanceof VfsJsonStorage && config.getVfsConfig().isEnableVfsFallback()) {
            log.info("No results from VFS query (directory: {}, field: {}, value: {}), trying local storage", 
                    directory, fieldName, fieldValue);
            results = localJsonStorage.findByField(directory, fieldName, fieldValue, clazz);
        }
        
        return results;
    }

    /**
     * 获取当前VFS是否可用
     * @return VFS是否可用
     */
    public boolean isVfsAvailable() {
        return isVfsAvailable;
    }

    /**
     * 手动检查VFS可用性
     * @return VFS是否可用
     */
    public boolean checkVfsNow() {
        return this.isVfsAvailable = checkVfsAvailability();
    }

    /**
     * 关闭存储服务，释放资源
     */
    public void close() {
        vfsCheckExecutor.shutdownNow();
        if (localJsonStorage instanceof AutoCloseable) {
            try {
                ((AutoCloseable) localJsonStorage).close();
            } catch (Exception e) {
                log.error("Failed to close local storage: {}", e.getMessage());
            }
        }
    }
}