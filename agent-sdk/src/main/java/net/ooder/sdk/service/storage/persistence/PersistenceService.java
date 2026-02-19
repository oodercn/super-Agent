
package net.ooder.sdk.service.storage.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PersistenceService {
    
    private static final Logger log = LoggerFactory.getLogger(PersistenceService.class);
    
    private final ObjectMapper objectMapper;
    private final Map<String, Object> memoryCache;
    private final String storagePath;
    
    public PersistenceService(String storagePath) {
        this.storagePath = storagePath;
        this.objectMapper = new ObjectMapper();
        this.memoryCache = new ConcurrentHashMap<>();
    }
    
    public <T> CompletableFuture<Void> save(String key, T data) {
        return CompletableFuture.runAsync(() -> {
            memoryCache.put(key, data);
            
            try {
                Path filePath = getFilePath(key);
                Files.createDirectories(filePath.getParent());
                
                String json = objectMapper.writeValueAsString(data);
                writeStringToFile(filePath, json);
                
                log.debug("Saved data for key: {}", key);
            } catch (IOException e) {
                log.error("Failed to save data for key: {}", key, e);
                throw new RuntimeException("Persistence save failed", e);
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    public <T> CompletableFuture<T> load(String key, Class<T> type) {
        return CompletableFuture.supplyAsync(() -> {
            Object cached = memoryCache.get(key);
            if (cached != null && type.isInstance(cached)) {
                return (T) cached;
            }
            
            try {
                Path filePath = getFilePath(key);
                if (Files.exists(filePath)) {
                    String json = readStringFromFile(filePath);
                    T data = objectMapper.readValue(json, type);
                    memoryCache.put(key, data);
                    return data;
                }
            } catch (IOException e) {
                log.error("Failed to load data for key: {}", key, e);
            }
            
            return null;
        });
    }
    
    public CompletableFuture<Void> delete(String key) {
        return CompletableFuture.runAsync(() -> {
            memoryCache.remove(key);
            
            try {
                Path filePath = getFilePath(key);
                Files.deleteIfExists(filePath);
                log.debug("Deleted data for key: {}", key);
            } catch (IOException e) {
                log.error("Failed to delete data for key: {}", key, e);
            }
        });
    }
    
    public CompletableFuture<Boolean> exists(String key) {
        return CompletableFuture.supplyAsync(() -> {
            if (memoryCache.containsKey(key)) {
                return true;
            }
            
            Path filePath = getFilePath(key);
            return Files.exists(filePath);
        });
    }
    
    public CompletableFuture<Void> clear() {
        return CompletableFuture.runAsync(() -> {
            memoryCache.clear();
            log.info("Cleared all cached data");
        });
    }
    
    private Path getFilePath(String key) {
        String safeKey = key.replaceAll("[^a-zA-Z0-9_-]", "_");
        return Paths.get(storagePath, safeKey + ".json");
    }
    
    private void writeStringToFile(Path path, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(Files.newOutputStream(path), StandardCharsets.UTF_8))) {
            writer.write(content);
        }
    }
    
    private String readStringFromFile(Path path) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }
}
