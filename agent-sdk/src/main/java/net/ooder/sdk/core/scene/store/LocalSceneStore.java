package net.ooder.sdk.core.scene.store;

import net.ooder.sdk.api.scene.store.SceneStore;
import net.ooder.sdk.api.scene.store.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LocalSceneStore implements SceneStore {
    
    private static final Logger log = LoggerFactory.getLogger(LocalSceneStore.class);
    
    private final Path storagePath;
    private final String format;
    private final Map<String, Map<String, Object>> memoryCache;
    
    public LocalSceneStore(String storagePath) {
        this(storagePath, "yaml");
    }
    
    public LocalSceneStore(String storagePath, String format) {
        this.storagePath = Paths.get(storagePath);
        this.format = format;
        this.memoryCache = new ConcurrentHashMap<>();
        
        try {
            Files.createDirectories(this.storagePath);
            log.info("LocalSceneStore initialized at: {}", this.storagePath);
        } catch (IOException e) {
            log.error("Failed to create storage directory: {}", this.storagePath, e);
        }
    }
    
    @Override
    public void saveScene(String sceneId, Map<String, Object> config) {
        if (sceneId == null || sceneId.isEmpty()) {
            throw new IllegalArgumentException("sceneId cannot be null or empty");
        }
        
        memoryCache.put(sceneId, config);
        
        try {
            Path filePath = getSceneFilePath(sceneId);
            Files.createDirectories(filePath.getParent());
            
            String content = mapToYaml(config);
            Files.write(filePath, content.getBytes(StandardCharsets.UTF_8), 
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
            log.debug("Scene saved: {} -> {}", sceneId, filePath);
        } catch (IOException e) {
            log.error("Failed to save scene: {}", sceneId, e);
            throw new RuntimeException("Failed to save scene: " + sceneId, e);
        }
    }
    
    @Override
    public Map<String, Object> loadScene(String sceneId) {
        if (sceneId == null || sceneId.isEmpty()) {
            return null;
        }
        
        Map<String, Object> cached = memoryCache.get(sceneId);
        if (cached != null) {
            return new LinkedHashMap<>(cached);
        }
        
        try {
            Path filePath = getSceneFilePath(sceneId);
            if (Files.exists(filePath)) {
                String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
                Map<String, Object> config = yamlToMap(content);
                memoryCache.put(sceneId, config);
                log.debug("Scene loaded: {} <- {}", sceneId, filePath);
                return config;
            }
        } catch (IOException e) {
            log.error("Failed to load scene: {}", sceneId, e);
        }
        
        return null;
    }
    
    @Override
    public void deleteScene(String sceneId) {
        if (sceneId == null || sceneId.isEmpty()) {
            return;
        }
        
        memoryCache.remove(sceneId);
        
        try {
            Path filePath = getSceneFilePath(sceneId);
            Files.deleteIfExists(filePath);
            log.debug("Scene deleted: {}", sceneId);
        } catch (IOException e) {
            log.error("Failed to delete scene: {}", sceneId, e);
        }
    }
    
    @Override
    public List<String> listScenes() {
        List<String> scenes = new ArrayList<>();
        
        try {
            if (Files.exists(storagePath)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(storagePath, "*." + format)) {
                    for (Path path : stream) {
                        String fileName = path.getFileName().toString();
                        scenes.add(fileName.replace("." + format, ""));
                    }
                }
            }
        } catch (IOException e) {
            log.error("Failed to list scenes", e);
        }
        
        return scenes;
    }
    
    @Override
    public boolean sceneExists(String sceneId) {
        if (memoryCache.containsKey(sceneId)) {
            return true;
        }
        
        Path filePath = getSceneFilePath(sceneId);
        return Files.exists(filePath);
    }
    
    @Override
    public void updateSceneConfig(String sceneId, String key, Object value) {
        Map<String, Object> config = loadScene(sceneId);
        if (config == null) {
            config = new LinkedHashMap<>();
        }
        config.put(key, value);
        saveScene(sceneId, config);
    }
    
    @Override
    public Object getSceneConfigValue(String sceneId, String key) {
        Map<String, Object> config = loadScene(sceneId);
        return config != null ? config.get(key) : null;
    }
    
    private Path getSceneFilePath(String sceneId) {
        return storagePath.resolve(sceneId + "." + format);
    }
    
    private String mapToYaml(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        mapToYaml(map, sb, 0);
        return sb.toString();
    }
    
    @SuppressWarnings("unchecked")
    private void mapToYaml(Map<String, Object> map, StringBuilder sb, int indent) {
        String indentStr = repeat("  ", indent);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                sb.append(indentStr).append(entry.getKey()).append(":\n");
                mapToYaml((Map<String, Object>) value, sb, indent + 1);
            } else if (value instanceof List) {
                sb.append(indentStr).append(entry.getKey()).append(":\n");
                for (Object item : (List<?>) value) {
                    if (item instanceof Map) {
                        sb.append(indentStr).append("  -\n");
                        mapToYaml((Map<String, Object>) item, sb, indent + 2);
                    } else {
                        sb.append(indentStr).append("  - ").append(item).append("\n");
                    }
                }
            } else if (value != null) {
                sb.append(indentStr).append(entry.getKey()).append(": ").append(value).append("\n");
            }
        }
    }
    
    private Map<String, Object> yamlToMap(String yaml) {
        Map<String, Object> result = new LinkedHashMap<>();
        String[] lines = yaml.split("\n");
        for (String line : lines) {
            if (line.contains(":") && !line.trim().startsWith("-")) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    if (!value.isEmpty()) {
                        result.put(key, parseValue(value));
                    }
                }
            }
        }
        return result;
    }
    
    private Object parseValue(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        
        if ("true".equalsIgnoreCase(value)) return true;
        if ("false".equalsIgnoreCase(value)) return false;
        
        try {
            if (value.contains(".")) {
                return Double.parseDouble(value);
            } else {
                return Long.parseLong(value);
            }
        } catch (NumberFormatException e) {
            return value;
        }
    }
    
    private String repeat(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    public void clearCache() {
        memoryCache.clear();
        log.info("Scene cache cleared");
    }
    
    public int getCacheSize() {
        return memoryCache.size();
    }
}
