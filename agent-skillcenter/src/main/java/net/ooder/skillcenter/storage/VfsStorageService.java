package net.ooder.skillcenter.storage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * VFS（虚拟文件系统）存储服务实现
 * 基于虚拟文件系统的存储方案
 */
public class VfsStorageService implements StorageService {
    
    private static final String STORAGE_DIR = "skillcenter/storage/vfs";
    private static final String METADATA_FILE = "metadata.json";
    
    private Path storagePath;
    private Map<String, Object> dataCache;
    private Map<String, String> fileMapping;
    private StorageStatus status;
    
    public VfsStorageService() {
        this.storagePath = Paths.get(System.getProperty("user.dir"), STORAGE_DIR);
        this.dataCache = new ConcurrentHashMap<>();
        this.fileMapping = new ConcurrentHashMap<>();
        this.status = StorageStatus.UNINITIALIZED;
    }
    
    @Override
    public void initialize() {
        try {
            status = StorageStatus.INITIALIZING;
            // 创建存储目录
            Files.createDirectories(storagePath);
            // 加载元数据
            loadMetadata();
            // 加载数据
            loadData();
            status = StorageStatus.RUNNING;
            System.out.println("VFS storage service initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize VFS storage service: " + e.getMessage());
            e.printStackTrace();
            status = StorageStatus.ERROR;
        }
    }
    
    @Override
    public void close() {
        try {
            saveData();
            saveMetadata();
            status = StorageStatus.CLOSED;
            System.out.println("VFS storage service closed successfully");
        } catch (Exception e) {
            System.err.println("Failed to close VFS storage service: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void save(String key, Object data) {
        dataCache.put(key, data);
        try {
            saveData();
            saveMetadata();
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
            saveMetadata();
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
        if (data instanceof JSONObject) {
            return JSON.toJavaObject((JSONObject) data, clazz);
        }
        String jsonStr = JSON.toJSONString(data);
        return JSON.parseObject(jsonStr, clazz);
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
        String filePath = fileMapping.remove(key);
        if (filePath != null) {
            try {
                Path path = Paths.get(filePath);
                Files.deleteIfExists(path);
            } catch (Exception e) {
                System.err.println("Failed to delete file: " + e.getMessage());
            }
        }
        try {
            saveMetadata();
        } catch (Exception e) {
            System.err.println("Failed to save metadata: " + e.getMessage());
        }
    }
    
    @Override
    public void deleteAll(List<String> keys) {
        for (String key : keys) {
            delete(key);
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
        return "vfs-storage";
    }
    
    @Override
    public StorageStatus getStatus() {
        return status;
    }
    
    /**
     * 加载元数据
     */
    private void loadMetadata() throws IOException {
        Path metadataFile = storagePath.resolve(METADATA_FILE);
        if (Files.exists(metadataFile)) {
            try (Reader reader = Files.newBufferedReader(metadataFile)) {
                String content = readAll(reader);
                JSONObject jsonObject = JSON.parseObject(content);
                JSONObject mapping = jsonObject.getJSONObject("fileMapping");
                if (mapping != null) {
                    for (String key : mapping.keySet()) {
                        fileMapping.put(key, mapping.getString(key));
                    }
                }
            }
        }
    }
    
    /**
     * 保存元数据
     */
    private void saveMetadata() throws IOException {
        Path metadataFile = storagePath.resolve(METADATA_FILE);
        JSONObject jsonObject = new JSONObject();
        JSONObject mapping = new JSONObject();
        for (Map.Entry<String, String> entry : fileMapping.entrySet()) {
            mapping.put(entry.getKey(), entry.getValue());
        }
        jsonObject.put("fileMapping", mapping);
        try (Writer writer = Files.newBufferedWriter(metadataFile)) {
            jsonObject.writeJSONString(writer);
        }
    }
    
    /**
     * 加载数据
     */
    private void loadData() throws IOException {
        for (Map.Entry<String, String> entry : fileMapping.entrySet()) {
            String key = entry.getKey();
            String filePath = entry.getValue();
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                try (Reader reader = Files.newBufferedReader(path)) {
                    String content = readAll(reader);
                    Object data = JSON.parse(content);
                    dataCache.put(key, data);
                }
            }
        }
    }
    
    /**
     * 保存数据
     */
    private void saveData() throws IOException {
        for (Map.Entry<String, Object> entry : dataCache.entrySet()) {
            String key = entry.getKey();
            Object data = entry.getValue();
            String fileName = generateFileName(key);
            Path filePath = storagePath.resolve(fileName);
            fileMapping.put(key, filePath.toString());
            try (Writer writer = Files.newBufferedWriter(filePath)) {
                String jsonStr = JSON.toJSONString(data);
                writer.write(jsonStr);
            }
        }
    }
    
    /**
     * 生成文件名
     */
    private String generateFileName(String key) {
        return key.replaceAll("[^a-zA-Z0-9]", "_") + ".json";
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
