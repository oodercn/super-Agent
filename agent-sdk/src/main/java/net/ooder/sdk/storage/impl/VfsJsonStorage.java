package net.ooder.sdk.storage.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.ooder.sdk.storage.JsonStorage;
import net.ooder.sdk.storage.StorageConfig;
import net.ooder.sdk.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * VFS JSON存储实现类，提供基于VFS服务器的JSON数据持久化功能
 */
public class VfsJsonStorage implements JsonStorage {
    private static final Logger log = LoggerFactory.getLogger(VfsJsonStorage.class);
    private static final String JSON_EXTENSION = ".json";
    private static final String DATA_DIR = "data";
    private static final String CONFIG_DIR = "config";

    private final VfsClient vfsClient;
    private final StorageConfig config;
    private final boolean enableFallback;

    /**
     * 构造函数
     * @param config 存储配置
     */
    public VfsJsonStorage(StorageConfig config) {
        this.config = config;
        this.vfsClient = new VfsClient(config.getVfsConfig());
        this.enableFallback = config.getVfsConfig().isEnableVfsFallback();
        
        // 初始化VFS连接
        if (!vfsClient.isVfsAvailable() && !enableFallback) {
            log.warn("VFS server is not available and fallback is disabled, operations will fail");
        }
    }

    /**
     * 获取文件在VFS中的完整路径
     * @param key 存储键
     * @return VFS完整路径
     */
    private String getVfsFilePath(String key) {
        // 根据key确定文件路径：config目录或data目录
        String directory = key.startsWith("config/") ? CONFIG_DIR : DATA_DIR;
        return directory + "/" + key.replace("config/", "") + JSON_EXTENSION;
    }

    @Override
    public <T> boolean save(String key, T data) {
        try {
            if (!vfsClient.isVfsAvailable()) {
                log.warn("VFS not available, save operation skipped for key: {}", key);
                return enableFallback;
            }

            String vfsPath = getVfsFilePath(key);
            String endpoint = vfsClient.getGroupEndpoint(vfsPath);
            
            // 准备存储数据
            Map<String, Object> storageData = new HashMap<>();
            storageData.put("data", data);
            storageData.put("timestamp", System.currentTimeMillis());
            storageData.put("version", 1);
            
            String response = vfsClient.put(endpoint, storageData);
            JSONObject responseJson = JSON.parseObject(response);
            return responseJson.getBooleanValue("success");
        } catch (Exception e) {
            log.error("Failed to save data to VFS for key: {}", key, e);
            return enableFallback;
        }
    }

    @Override
    public <T> T load(String key, Class<T> clazz) {
        try {
            if (!vfsClient.isVfsAvailable()) {
                log.warn("VFS not available, load operation skipped for key: {}", key);
                return null;
            }

            String vfsPath = getVfsFilePath(key);
            String endpoint = vfsClient.getGroupEndpoint(vfsPath);
            
            String response = vfsClient.get(endpoint);
            JSONObject responseJson = JSON.parseObject(response);
            
            if (responseJson.getBooleanValue("success")) {
                JSONObject dataJson = responseJson.getJSONObject("data");
                return dataJson.getObject("data", clazz);
            }
            return null;
        } catch (Exception e) {
            log.error("Failed to load data from VFS for key: {}", key, e);
            return null;
        }
    }

    @Override
    public boolean delete(String key) {
        try {
            if (!vfsClient.isVfsAvailable()) {
                log.warn("VFS not available, delete operation skipped for key: {}", key);
                return enableFallback;
            }

            String vfsPath = getVfsFilePath(key);
            String endpoint = vfsClient.getGroupEndpoint(vfsPath);
            
            String response = vfsClient.delete(endpoint);
            JSONObject responseJson = JSON.parseObject(response);
            return responseJson.getBooleanValue("success");
        } catch (Exception e) {
            log.error("Failed to delete data from VFS for key: {}", key, e);
            return enableFallback;
        }
    }

    @Override
    public boolean exists(String key) {
        try {
            if (!vfsClient.isVfsAvailable()) {
                log.warn("VFS not available, exists operation skipped for key: {}", key);
                return false;
            }

            String vfsPath = getVfsFilePath(key);
            String endpoint = vfsClient.getGroupEndpoint(vfsPath) + "/exists";
            
            String response = vfsClient.get(endpoint);
            JSONObject responseJson = JSON.parseObject(response);
            return responseJson.getBooleanValue("success") && responseJson.getBooleanValue("exists");
        } catch (Exception e) {
            log.error("Failed to check existence in VFS for key: {}", key, e);
            return false;
        }
    }

    @Override
    public <T> boolean saveBatch(Map<String, T> dataMap) {
        if (dataMap == null || dataMap.isEmpty()) {
            return true;
        }

        boolean allSuccess = true;
        for (Map.Entry<String, T> entry : dataMap.entrySet()) {
            if (!save(entry.getKey(), entry.getValue())) {
                allSuccess = false;
            }
        }
        return allSuccess;
    }

    @Override
    public <T> Map<String, T> loadBatch(List<String> keys, Class<T> clazz) {
        Map<String, T> resultMap = new HashMap<>();
        if (keys == null || keys.isEmpty()) {
            return resultMap;
        }

        for (String key : keys) {
            T data = load(key, clazz);
            if (data != null) {
                resultMap.put(key, data);
            }
        }
        return resultMap;
    }

    @Override
    public boolean deleteBatch(List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return true;
        }

        boolean allSuccess = true;
        for (String key : keys) {
            if (!delete(key)) {
                allSuccess = false;
            }
        }
        return allSuccess;
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
        // VFS不支持事务，返回一个空实现
        return new Transaction() {
            @Override
            public <T> boolean save(String key, T data) {
                return VfsJsonStorage.this.save(key, data);
            }

            @Override
            public <T> T load(String key, Class<T> clazz) {
                return VfsJsonStorage.this.load(key, clazz);
            }

            @Override
            public boolean delete(String key) {
                return VfsJsonStorage.this.delete(key);
            }

            @Override
            public boolean commit() {
                return true; // 无事务可提交
            }

            @Override
            public boolean rollback() {
                return false; // 无事务可回滚
            }
        };
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
        try {
            if (!vfsClient.isVfsAvailable()) {
                log.warn("VFS not available, loadJson operation skipped for key: {}", key);
                return null;
            }

            String vfsPath = getVfsFilePath(key);
            String endpoint = vfsClient.getGroupEndpoint(vfsPath);
            
            String response = vfsClient.get(endpoint);
            JSONObject responseJson = JSON.parseObject(response);
            
            if (responseJson.getBooleanValue("success")) {
                JSONObject dataJson = responseJson.getJSONObject("data");
                String jsonString = JSON.toJSONString(dataJson.get("data"));
                return JSON.parseObject(jsonString, typeRef);
            }
            return null;
        } catch (Exception e) {
            log.error("Failed to load JSON from VFS for key: {}", key, e);
            return null;
        }
    }

    @Override
    public boolean updateJson(String key, Map<String, Object> updates) {
        try {
            if (!vfsClient.isVfsAvailable()) {
                log.warn("VFS not available, updateJson operation skipped for key: {}", key);
                return enableFallback;
            }

            String vfsPath = getVfsFilePath(key);
            String endpoint = vfsClient.getGroupEndpoint(vfsPath) + "/update";
            
            String response = vfsClient.post(endpoint, updates);
            JSONObject responseJson = JSON.parseObject(response);
            return responseJson.getBooleanValue("success");
        } catch (Exception e) {
            log.error("Failed to update JSON in VFS for key: {}", key, e);
            return enableFallback;
        }
    }

    @Override
    public <T> T getJsonField(String key, String fieldName, Class<T> fieldType) {
        try {
            if (!vfsClient.isVfsAvailable()) {
                log.warn("VFS not available, getJsonField operation skipped for key: {}", key);
                return null;
            }

            String vfsPath = getVfsFilePath(key);
            String endpoint = vfsClient.getGroupEndpoint(vfsPath) + "/field/" + fieldName;
            
            String response = vfsClient.get(endpoint);
            JSONObject responseJson = JSON.parseObject(response);
            
            if (responseJson.getBooleanValue("success")) {
                return responseJson.getObject("data", fieldType);
            }
            return null;
        } catch (Exception e) {
            log.error("Failed to get JSON field from VFS for key: {}, field: {}", key, fieldName, e);
            return null;
        }
    }

    @Override
    public <T> List<T> findByField(String directory, String fieldName, Object fieldValue, Class<T> clazz) {
        try {
            if (!vfsClient.isVfsAvailable()) {
                log.warn("VFS not available, findByField operation skipped for directory: {}", directory);
                return Collections.emptyList();
            }

            String vfsPath = DATA_DIR + "/" + directory;
            String endpoint = vfsClient.getGroupEndpoint(vfsPath) + "/find?field=" + fieldName + "&value=" + fieldValue;
            
            String response = vfsClient.get(endpoint);
            JSONObject responseJson = JSON.parseObject(response);
            
            if (responseJson.getBooleanValue("success")) {
                return responseJson.getJSONArray("data").toJavaList(clazz);
            }
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to find by field in VFS for directory: {}, field: {}", directory, fieldName, e);
            return Collections.emptyList();
        }
    }
}