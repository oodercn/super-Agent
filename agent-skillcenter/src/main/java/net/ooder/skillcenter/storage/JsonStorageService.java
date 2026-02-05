package net.ooder.skillcenter.storage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JSON文件存储服务实现
 * 基于文件系统的JSON格式存储
 */
public class JsonStorageService implements StorageService {
    
    private static final String STORAGE_DIR = "skillcenter/storage/json";
    private static final String DATA_FILE = "data.json";
    
    private Path storagePath;
    private Map<String, Object> dataCache;
    private StorageStatus status;
    
    public JsonStorageService() {
        this.storagePath = Paths.get(System.getProperty("user.dir"), STORAGE_DIR);
        this.dataCache = new ConcurrentHashMap<>();
        this.status = StorageStatus.UNINITIALIZED;
    }
    
    @Override
    public void initialize() {
        try {
            status = StorageStatus.INITIALIZING;
            // 创建存储目录
            Files.createDirectories(storagePath);
            // 加载数据
            loadData();
            status = StorageStatus.RUNNING;
            System.out.println("JSON storage service initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize JSON storage service: " + e.getMessage());
            e.printStackTrace();
            status = StorageStatus.ERROR;
        }
    }
    
    @Override
    public void close() {
        try {
            saveData();
            status = StorageStatus.CLOSED;
            System.out.println("JSON storage service closed successfully");
        } catch (Exception e) {
            System.err.println("Failed to close JSON storage service: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void save(String key, Object data) {
        dataCache.put(key, data);
        try {
            saveData();
        } catch (Exception e) {
            System.err.println("Failed to save data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void saveAll(Map<String, Object> dataMap) {
        dataCache.putAll(dataMap);
        try {
            saveData();
        } catch (Exception e) {
            System.err.println("Failed to save all data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public <T> T load(String key, Class<T> clazz) {
        Object data = dataCache.get(key);
        if (data == null) {
            return null;
        }
        return convertToType(data, clazz);
    }
    
    @Override
    public <T> Map<String, T> loadAll(List<String> keys, Class<T> clazz) {
        Map<String, T> result = new HashMap<>();
        for (String key : keys) {
            T data = load(key, clazz);
            if (data != null) {
                result.put(key, data);
            }
        }
        return result;
    }
    
    @Override
    public void delete(String key) {
        dataCache.remove(key);
        try {
            saveData();
        } catch (Exception e) {
            System.err.println("Failed to delete data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void deleteAll(List<String> keys) {
        for (String key : keys) {
            dataCache.remove(key);
        }
        try {
            saveData();
        } catch (Exception e) {
            System.err.println("Failed to delete all data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean exists(String key) {
        return dataCache.containsKey(key);
    }
    
    @Override
    public List<String> getAllKeys() {
        return new ArrayList<>(dataCache.keySet());
    }
    
    @Override
    public String getName() {
        return "json-storage";
    }
    
    @Override
    public StorageStatus getStatus() {
        return status;
    }
    
    /**
     * 加载数据从文件
     */
    private void loadData() throws IOException {
        Path dataFile = storagePath.resolve(DATA_FILE);
        if (Files.exists(dataFile)) {
            try (Reader reader = Files.newBufferedReader(dataFile)) {
                String content = readAll(reader);
                JSONObject jsonObject = JSON.parseObject(content);
                for (String key : jsonObject.keySet()) {
                    dataCache.put(key, jsonObject.get(key));
                }
            }
        }
    }
    
    /**
     * 保存数据到文件
     */
    private void saveData() throws IOException {
        Path dataFile = storagePath.resolve(DATA_FILE);
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, Object> entry : dataCache.entrySet()) {
            jsonObject.put(entry.getKey(), entry.getValue());
        }
        try (Writer writer = Files.newBufferedWriter(dataFile)) {
            jsonObject.writeJSONString(writer);
        }
    }
    
    /**
     * 将对象转换为指定类型
     */
    private <T> T convertToType(Object data, Class<T> clazz) {
        if (data == null) {
            return null;
        }
        if (data instanceof JSONObject) {
            return JSON.toJavaObject((JSONObject) data, clazz);
        }
        String jsonStr = JSON.toJSONString(data);
        return JSON.parseObject(jsonStr, clazz);
    }
    
    /**
     * 读取Reader的所有内容
     */
    private String readAll(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[1024];
        int n;
        while ((n = reader.read(buffer)) != -1) {
            sb.append(buffer, 0, n);
        }
        return sb.toString();
    }
}
