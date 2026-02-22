package net.ooder.sdk.storage.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.ooder.sdk.storage.JsonStorage;
import net.ooder.sdk.storage.StorageConfig;
import net.ooder.sdk.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 本地JSON文件存储实现类，提供基于文件系统的JSON数据持久化功能
 */
public class LocalJsonStorage implements JsonStorage, AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(LocalJsonStorage.class);
    private static final String DEFAULT_DATA_DIR = "data";
    private static final String DEFAULT_CONFIG_DIR = "config";
    private static final String DEFAULT_LOGS_DIR = "logs";
    private static final String DEFAULT_TEMP_DIR = "temp";
    private static final String DEFAULT_BACKUP_DIR = "backup";
    private static final String JSON_EXTENSION = ".json";
    
    private final Path rootPath;
    private final Path dataPath;
    private final Path configPath;
    private final Path logsPath;
    private final Path tempPath;
    private final Path backupPath;
    private final StorageConfig config;
    private final Map<String, ReentrantLock> fileLocks = new ConcurrentHashMap<>();
    private final ExecutorService asyncExecutor = Executors.newFixedThreadPool(4);
    
    public LocalJsonStorage() {
        this(new StorageConfig());
    }
    
    public LocalJsonStorage(String rootDir) {
        this(new StorageConfig(rootDir));
    }
    
    public LocalJsonStorage(StorageConfig config) {
        this.config = config;
        this.rootPath = Paths.get(config.getRootDirectory());
        this.dataPath = rootPath.resolve(DEFAULT_DATA_DIR);
        this.configPath = rootPath.resolve(DEFAULT_CONFIG_DIR);
        this.logsPath = rootPath.resolve(DEFAULT_LOGS_DIR);
        this.tempPath = rootPath.resolve(DEFAULT_TEMP_DIR);
        this.backupPath = rootPath.resolve(DEFAULT_BACKUP_DIR);
        
        // 初始化目录结构
        initializeDirectories();
    }
    
    /**
     * 初始化目录结构
     */
    private void initializeDirectories() {
        try {
            Files.createDirectories(rootPath);
            Files.createDirectories(dataPath);
            Files.createDirectories(configPath);
            Files.createDirectories(logsPath);
            Files.createDirectories(tempPath);
            Files.createDirectories(backupPath);
            log.info("Storage directories initialized at: {}", rootPath);
        } catch (IOException e) {
            log.error("Failed to initialize storage directories: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize storage directories", e);
        }
    }
    
    /**
     * 根据key生成数据文件路径
     */
    private Path getDataFilePath(String key) {
        // 支持key格式："agent/agent-123/status"
        return dataPath.resolve(key + JSON_EXTENSION);
    }
    
    /**
     * 根据key生成配置文件路径
     */
    private Path getConfigFilePath(String key) {
        return configPath.resolve(key + JSON_EXTENSION);
    }
    
    /**
     * 读取文件内容
     */
    private String readFile(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            return null;
        }
        
        try (FileInputStream fis = new FileInputStream(filePath.toFile());
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8))) {
            
            // 单独处理文件锁，避免关闭顺序问题
            FileChannel channel = fis.getChannel();
            FileLock lock = null;
            try {
                if (config.isEnableFileLock()) {
                    lock = channel.lock(0, Long.MAX_VALUE, true); // 共享锁
                }
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                return content.toString();
            } finally {
                if (lock != null && lock.isValid()) {
                    try {
                        lock.release();
                    } catch (Exception e) {
                        // 忽略锁释放错误
                        log.debug("Error releasing file lock: {}", e.getMessage());
                    }
                }
            }
        }
    }
    
    /**
     * 写入文件内容
     */
    private void writeFile(Path filePath, String content) throws IOException {
        // 确保父目录存在
        Files.createDirectories(filePath.getParent());
        
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8))) {
            
            // 单独处理文件锁，避免关闭顺序问题
            FileChannel channel = fos.getChannel();
            FileLock lock = null;
            try {
                if (config.isEnableFileLock()) {
                    lock = channel.lock(); // 排他锁
                }
                writer.write(content);
                writer.flush();
                log.debug("File written: {}", filePath);
            } finally {
                if (lock != null && lock.isValid()) {
                    try {
                        lock.release();
                    } catch (Exception e) {
                        // 忽略锁释放错误
                        log.debug("Error releasing file lock: {}", e.getMessage());
                    }
                }
            }
        }
    }
    
    /**
     * 获取文件锁
     */
    private ReentrantLock getFileLock(String key) {
        return fileLocks.computeIfAbsent(key, k -> new ReentrantLock());
    }
    
    // 单条数据操作实现
    @Override
    public <T> boolean save(String key, T data) {
        Objects.requireNonNull(key, "Key cannot be null");
        Objects.requireNonNull(data, "Data cannot be null");
        
        ReentrantLock lock = getFileLock(key);
        lock.lock();
        try {
            String json = JSON.toJSONString(data);
            Path filePath = getDataFilePath(key);
            writeFile(filePath, json);
            return true;
        } catch (Exception e) {
            log.error("Failed to save data for key " + key, e);
            return false;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public <T> T load(String key, Class<T> clazz) {
        Objects.requireNonNull(key, "Key cannot be null");
        Objects.requireNonNull(clazz, "Class cannot be null");
        
        ReentrantLock lock = getFileLock(key);
        lock.lock();
        try {
            Path filePath = getDataFilePath(key);
            String json = readFile(filePath);
            if (json == null) {
                return null;
            }
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            log.error("Failed to load data for key {}: {}", key, e.getMessage());
            return null;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public boolean delete(String key) {
        Objects.requireNonNull(key, "Key cannot be null");
        
        ReentrantLock lock = getFileLock(key);
        lock.lock();
        try {
            Path filePath = getDataFilePath(key);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.debug("File deleted: {}", filePath);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Failed to delete data for key {}: {}", key, e.getMessage());
            return false;
        } finally {
            lock.unlock();
            fileLocks.remove(key);
        }
    }
    
    @Override
    public boolean exists(String key) {
        Objects.requireNonNull(key, "Key cannot be null");
        
        try {
            Path filePath = getDataFilePath(key);
            return Files.exists(filePath);
        } catch (Exception e) {
            log.error("Failed to check existence for key {}: {}", key, e.getMessage());
            return false;
        }
    }
    
    // 批量操作实现
    @Override
    public <T> boolean saveBatch(Map<String, T> dataMap) {
        Objects.requireNonNull(dataMap, "Data map cannot be null");
        
        if (dataMap.isEmpty()) {
            return true;
        }
        
        // 获取所有文件锁
        List<ReentrantLock> locks = new ArrayList<>();
        for (String key : dataMap.keySet()) {
            ReentrantLock lock = getFileLock(key);
            lock.lock();
            locks.add(lock);
        }
        
        try {
            boolean allSuccess = true;
            for (Map.Entry<String, T> entry : dataMap.entrySet()) {
                String key = entry.getKey();
                T data = entry.getValue();
                try {
                    String json = JSON.toJSONString(data);
                    Path filePath = getDataFilePath(key);
                    writeFile(filePath, json);
                } catch (Exception e) {
                    log.error("Failed to save data for key {} in batch: {}", key, e.getMessage());
                    allSuccess = false;
                }
            }
            return allSuccess;
        } finally {
            // 释放所有锁
            for (ReentrantLock lock : locks) {
                lock.unlock();
            }
        }
    }
    
    @Override
    public <T> Map<String, T> loadBatch(List<String> keys, Class<T> clazz) {
        Objects.requireNonNull(keys, "Keys list cannot be null");
        Objects.requireNonNull(clazz, "Class cannot be null");
        
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
    public boolean deleteBatch(List<String> keys) {
        Objects.requireNonNull(keys, "Keys list cannot be null");
        
        boolean allSuccess = true;
        for (String key : keys) {
            if (!delete(key)) {
                allSuccess = false;
            }
        }
        
        return allSuccess;
    }
    
    // 异步操作实现
    @Override
    public <T> CompletableFuture<Boolean> saveAsync(String key, T data) {
        return CompletableFuture.supplyAsync(() -> save(key, data), asyncExecutor);
    }
    
    @Override
    public <T> CompletableFuture<T> loadAsync(String key, Class<T> clazz) {
        return CompletableFuture.supplyAsync(() -> load(key, clazz), asyncExecutor);
    }
    
    // 事务操作实现
    @Override
    public Transaction beginTransaction() {
        return new LocalTransaction();
    }
    
    // JSON特定操作实现
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
        Objects.requireNonNull(key, "Key cannot be null");
        Objects.requireNonNull(typeRef, "Type reference cannot be null");
        
        ReentrantLock lock = getFileLock(key);
        lock.lock();
        try {
            Path filePath = getDataFilePath(key);
            String json = readFile(filePath);
            if (json == null) {
                return null;
            }
            return JSON.parseObject(json, typeRef);
        } catch (Exception e) {
            log.error("Failed to load JSON data for key {}: {}", key, e.getMessage());
            return null;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public boolean updateJson(String key, Map<String, Object> updates) {
        Objects.requireNonNull(key, "Key cannot be null");
        Objects.requireNonNull(updates, "Updates cannot be null");
        
        ReentrantLock lock = getFileLock(key);
        lock.lock();
        try {
            Path filePath = getDataFilePath(key);
            String json = readFile(filePath);
            if (json == null) {
                return false;
            }
            
            JSONObject jsonObject = JSON.parseObject(json);
            jsonObject.putAll(updates);
            
            String updatedJson = JSON.toJSONString(jsonObject);
            writeFile(filePath, updatedJson);
            return true;
        } catch (Exception e) {
            log.error("Failed to update JSON data for key {}: {}", key, e.getMessage());
            return false;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public <T> T getJsonField(String key, String fieldName, Class<T> fieldType) {
        Objects.requireNonNull(key, "Key cannot be null");
        Objects.requireNonNull(fieldName, "Field name cannot be null");
        Objects.requireNonNull(fieldType, "Field type cannot be null");
        
        ReentrantLock lock = getFileLock(key);
        lock.lock();
        try {
            Path filePath = getDataFilePath(key);
            String json = readFile(filePath);
            if (json == null) {
                return null;
            }
            
            JSONObject jsonObject = JSON.parseObject(json);
            return jsonObject.getObject(fieldName, fieldType);
        } catch (Exception e) {
            log.error("Failed to get JSON field for key {} and field {}: {}", key, fieldName, e.getMessage());
            return null;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public <T> List<T> findByField(String directory, String fieldName, Object fieldValue, Class<T> clazz) {
        Objects.requireNonNull(directory, "Directory cannot be null");
        Objects.requireNonNull(fieldName, "Field name cannot be null");
        Objects.requireNonNull(fieldValue, "Field value cannot be null");
        Objects.requireNonNull(clazz, "Class cannot be null");
        
        List<T> result = new ArrayList<>();
        Path dirPath = dataPath.resolve(directory);
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*" + JSON_EXTENSION)) {
            for (Path file : stream) {
                try {
                    String json = readFile(file);
                    if (json != null) {
                        JSONObject jsonObject = JSON.parseObject(json);
                        Object value = jsonObject.get(fieldName);
                        if (fieldValue.equals(value)) {
                            T data = JSON.parseObject(json, clazz);
                            result.add(data);
                        }
                    }
                } catch (Exception e) {
                    log.error("Failed to process file {}: {}", file, e.getMessage());
                }
            }
        } catch (IOException e) {
            log.error("Failed to search directory {}: {}", directory, e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 本地事务实现类
     */
    private class LocalTransaction implements Transaction {
        private final Map<String, Object> changes = new HashMap<>();
        private final Set<String> deletions = new HashSet<>();
        private boolean committed = false;
        private boolean rolledBack = false;
        
        @Override
        public <T> boolean save(String key, T data) {
            checkTransactionState();
            changes.put(key, data);
            deletions.remove(key);
            return true;
        }
        
        @Override
        public <T> T load(String key, Class<T> clazz) {
            checkTransactionState();
            // 优先从事务中加载
            if (changes.containsKey(key)) {
                return clazz.cast(changes.get(key));
            }
            // 如果标记为删除，返回null
            if (deletions.contains(key)) {
                return null;
            }
            // 从文件系统加载
            return LocalJsonStorage.this.load(key, clazz);
        }
        
        @Override
        public boolean delete(String key) {
            checkTransactionState();
            changes.remove(key);
            deletions.add(key);
            return true;
        }
        
        @Override
        public boolean commit() {
            checkTransactionState();
            
            boolean success = true;
            
            // 执行删除操作
            for (String key : deletions) {
                if (!LocalJsonStorage.this.delete(key)) {
                    success = false;
                }
            }
            
            // 执行保存操作
            for (Map.Entry<String, Object> entry : changes.entrySet()) {
                String key = entry.getKey();
                Object data = entry.getValue();
                if (!LocalJsonStorage.this.save(key, data)) {
                    success = false;
                }
            }
            
            committed = true;
            return success;
        }
        
        @Override
        public boolean rollback() {
            checkTransactionState();
            
            changes.clear();
            deletions.clear();
            rolledBack = true;
            return true;
        }
        
        private void checkTransactionState() {
            if (committed) {
                throw new IllegalStateException("Transaction has already been committed");
            }
            if (rolledBack) {
                throw new IllegalStateException("Transaction has already been rolled back");
            }
        }
    }
    
    // 清理资源
    public void shutdown() {
        asyncExecutor.shutdown();
    }
    
    @Override
    public void close() throws Exception {
        shutdown();
    }
}