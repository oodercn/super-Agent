package net.ooder.sdk.api.storage.impl;

import net.ooder.sdk.api.storage.StorageService;
import net.ooder.sdk.api.storage.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class StorageServiceImpl implements StorageService {
    
    private static final Logger log = LoggerFactory.getLogger(StorageServiceImpl.class);
    
    private final Path basePath;
    private final ExecutorService executor;
    private final Map<String, Object> memoryCache;
    private final boolean useDisk;
    
    public StorageServiceImpl() {
        this.basePath = Paths.get(System.getProperty("user.home"), ".ooder", "storage");
        this.executor = Executors.newCachedThreadPool();
        this.memoryCache = new ConcurrentHashMap<String, Object>();
        this.useDisk = true;
        
        try {
            Files.createDirectories(basePath);
            log.info("StorageServiceImpl initialized with path: {}", basePath);
        } catch (IOException e) {
            log.warn("Failed to create storage directory, using memory only", e);
        }
    }
    
    public StorageServiceImpl(String basePath) {
        this.basePath = Paths.get(basePath);
        this.executor = Executors.newCachedThreadPool();
        this.memoryCache = new ConcurrentHashMap<String, Object>();
        this.useDisk = true;
        
        try {
            Files.createDirectories(this.basePath);
            log.info("StorageServiceImpl initialized with path: {}", this.basePath);
        } catch (IOException e) {
            log.warn("Failed to create storage directory, using memory only", e);
        }
    }
    
    @Override
    public String save(String key, Object data) {
        memoryCache.put(key, data);
        
        if (useDisk) {
            try {
                Path filePath = getFilePath(key);
                Files.createDirectories(filePath.getParent());
                
                String json = toJson(data);
                Files.write(filePath, json.getBytes("UTF-8"));
                log.debug("Saved data to: {}", filePath);
            } catch (Exception e) {
                log.error("Failed to save data for key: {}", key, e);
            }
        }
        
        return key;
    }
    
    @Override
    public <T> Optional<T> load(String key, Class<T> type) {
        Object cached = memoryCache.get(key);
        if (cached != null && type.isInstance(cached)) {
            return Optional.of(type.cast(cached));
        }
        
        if (useDisk) {
            try {
                Path filePath = getFilePath(key);
                if (Files.exists(filePath)) {
                    String json = new String(Files.readAllBytes(filePath), "UTF-8");
                    T data = fromJson(json, type);
                    if (data != null) {
                        memoryCache.put(key, data);
                        return Optional.of(data);
                    }
                }
            } catch (Exception e) {
                log.error("Failed to load data for key: {}", key, e);
            }
        }
        
        return Optional.empty();
    }
    
    @Override
    public <T> Optional<T> load(String key, TypeReference<T> typeRef) {
        return load(key, (Class<T>) typeRef.getType().getClass());
    }
    
    @Override
    public boolean delete(String key) {
        memoryCache.remove(key);
        
        if (useDisk) {
            try {
                Path filePath = getFilePath(key);
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    log.debug("Deleted: {}", filePath);
                    return true;
                }
            } catch (Exception e) {
                log.error("Failed to delete data for key: {}", key, e);
            }
        }
        
        return false;
    }
    
    @Override
    public boolean exists(String key) {
        if (memoryCache.containsKey(key)) {
            return true;
        }
        
        if (useDisk) {
            Path filePath = getFilePath(key);
            return Files.exists(filePath);
        }
        
        return false;
    }
    
    @Override
    public List<String> listKeys(String prefix) {
        List<String> keys = new ArrayList<String>();
        
        if (prefix == null || prefix.isEmpty()) {
            keys.addAll(memoryCache.keySet());
        } else {
            for (String key : memoryCache.keySet()) {
                if (key.startsWith(prefix)) {
                    keys.add(key);
                }
            }
        }
        
        if (useDisk) {
            try {
                Files.walk(basePath)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        String key = pathToKey(path);
                        if (prefix == null || prefix.isEmpty() || key.startsWith(prefix)) {
                            if (!keys.contains(key)) {
                                keys.add(key);
                            }
                        }
                    });
            } catch (Exception e) {
                log.error("Failed to list keys", e);
            }
        }
        
        return keys;
    }
    
    @Override
    public CompletableFuture<String> saveAsync(String key, Object data) {
        return CompletableFuture.supplyAsync(() -> save(key, data), executor);
    }
    
    @Override
    public <T> CompletableFuture<Optional<T>> loadAsync(String key, Class<T> type) {
        return CompletableFuture.supplyAsync(() -> load(key, type), executor);
    }
    
    @Override
    public <T> CompletableFuture<Optional<T>> loadAsync(String key, TypeReference<T> typeRef) {
        return CompletableFuture.supplyAsync(() -> load(key, typeRef), executor);
    }
    
    @Override
    public CompletableFuture<Boolean> deleteAsync(String key) {
        return CompletableFuture.supplyAsync(() -> delete(key), executor);
    }
    
    @Override
    public void saveBatch(Map<String, Object> batch) {
        for (Map.Entry<String, Object> entry : batch.entrySet()) {
            save(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public CompletableFuture<Void> saveBatchAsync(Map<String, Object> batch) {
        return CompletableFuture.runAsync(() -> saveBatch(batch), executor);
    }
    
    @Override
    public Map<String, Object> loadBatch(List<String> keys) {
        Map<String, Object> result = new HashMap<String, Object>();
        for (String key : keys) {
            Optional<Object> value = load(key, Object.class);
            if (value.isPresent()) {
                result.put(key, value.get());
            }
        }
        return result;
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> loadBatchAsync(List<String> keys) {
        return CompletableFuture.supplyAsync(() -> loadBatch(keys), executor);
    }
    
    @Override
    public String getBasePath() {
        return basePath.toString();
    }
    
    @Override
    public void setBasePath(String basePath) {
        throw new UnsupportedOperationException("Cannot change base path after initialization");
    }
    
    @Override
    public void clear() {
        memoryCache.clear();
        
        if (useDisk) {
            try {
                Files.walk(basePath)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            if (!path.equals(basePath)) {
                                Files.delete(path);
                            }
                        } catch (IOException e) {
                            log.warn("Failed to delete: {}", path);
                        }
                    });
            } catch (Exception e) {
                log.error("Failed to clear storage", e);
            }
        }
        
        log.info("Storage cleared");
    }
    
    @Override
    public long size() {
        long count = memoryCache.size();
        
        if (useDisk) {
            try {
                count = Files.walk(basePath)
                    .filter(Files::isRegularFile)
                    .count();
            } catch (Exception e) {
                log.error("Failed to count files", e);
            }
        }
        
        return count;
    }
    
    @Override
    public void shutdown() {
        log.info("Shutting down StorageService");
        executor.shutdown();
        memoryCache.clear();
        log.info("StorageService shutdown complete");
    }
    
    private Path getFilePath(String key) {
        String safeKey = key.replaceAll("[^a-zA-Z0-9_\\-:.]", "_");
        return basePath.resolve(safeKey + ".json");
    }
    
    private String pathToKey(Path path) {
        String fileName = path.getFileName().toString();
        if (fileName.endsWith(".json")) {
            return fileName.substring(0, fileName.length() - 5);
        }
        return fileName;
    }
    
    private String toJson(Object data) {
        if (data == null) return "null";
        if (data instanceof String) return "\"" + escapeJson((String) data) + "\"";
        if (data instanceof Number || data instanceof Boolean) return data.toString();
        if (data instanceof Map) {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) data).entrySet()) {
                if (!first) sb.append(",");
                sb.append("\"").append(entry.getKey()).append("\":").append(toJson(entry.getValue()));
                first = false;
            }
            sb.append("}");
            return sb.toString();
        }
        if (data instanceof List) {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object item : (List<?>) data) {
                if (!first) sb.append(",");
                sb.append(toJson(item));
                first = false;
            }
            sb.append("]");
            return sb.toString();
        }
        return "{\"value\":" + toJson(data.toString()) + "}";
    }
    
    private String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    
    @SuppressWarnings("unchecked")
    private <T> T fromJson(String json, Class<T> type) {
        if (json == null || json.equals("null")) return null;
        
        try {
            if (type == String.class) {
                if (json.startsWith("\"") && json.endsWith("\"")) {
                    return (T) json.substring(1, json.length() - 1);
                }
                return (T) json;
            }
            if (type == Integer.class || type == int.class) {
                return (T) Integer.valueOf(json.trim());
            }
            if (type == Long.class || type == long.class) {
                return (T) Long.valueOf(json.trim());
            }
            if (type == Double.class || type == double.class) {
                return (T) Double.valueOf(json.trim());
            }
            if (type == Boolean.class || type == boolean.class) {
                return (T) Boolean.valueOf(json.trim());
            }
            
            return (T) json;
        } catch (Exception e) {
            log.warn("Failed to parse JSON for type: {}", type.getName());
            return null;
        }
    }
}
