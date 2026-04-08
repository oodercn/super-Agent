package net.ooder.mvp.skill.scene.config.sdk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonConfigStorageImpl implements SdkConfigStorage {

    private static final Logger log = LoggerFactory.getLogger(JsonConfigStorageImpl.class);

    private final Path configRoot;
    private final ObjectMapper objectMapper;
    private final Map<String, ConfigNode> cache;
    private final long cacheTtlMillis;
    private final Map<String, Long> cacheTimestamps;

    public JsonConfigStorageImpl(String configRootPath) {
        this(configRootPath, 300000);
    }

    public JsonConfigStorageImpl(String configRootPath, long cacheTtlMillis) {
        this.configRoot = Paths.get(configRootPath);
        this.objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .registerModule(new JavaTimeModule());
        this.cache = new ConcurrentHashMap<>();
        this.cacheTimestamps = new ConcurrentHashMap<>();
        this.cacheTtlMillis = cacheTtlMillis;
        
        ensureDirectory(configRoot);
        ensureDirectory(configRoot.resolve("profiles"));
        ensureDirectory(configRoot.resolve("runtime"));
        ensureDirectory(configRoot.resolve("capabilities"));
        ensureDirectory(configRoot.resolve("secrets"));
        
        log.info("[JsonConfigStorage] Initialized with config root: {}", configRootPath);
    }

    @Override
    public ConfigNode loadSystemConfig() {
        String cacheKey = "system";
        if (isCacheValid(cacheKey)) {
            return cache.get(cacheKey);
        }
        
        Path configFile = configRoot.resolve("system-config.json");
        if (!Files.exists(configFile)) {
            log.info("[JsonConfigStorage] System config not found, loading default profile");
            return loadDefaultProfile();
        }
        
        ConfigNode config = readJson(configFile);
        updateCache(cacheKey, config);
        return config;
    }

    @Override
    public ConfigNode loadProfile(String profileName) {
        String cacheKey = "profile:" + profileName;
        if (isCacheValid(cacheKey)) {
            return cache.get(cacheKey);
        }
        
        Path profileFile = configRoot.resolve("profiles/" + profileName + ".json");
        if (!Files.exists(profileFile)) {
            throw new ConfigNotFoundException("Profile not found: " + profileName);
        }
        
        ConfigNode config = readJson(profileFile);
        updateCache(cacheKey, config);
        return config;
    }

    @Override
    public ConfigNode loadSkillConfig(String skillId) {
        String cacheKey = "skill:" + skillId;
        if (isCacheValid(cacheKey)) {
            return cache.get(cacheKey);
        }
        
        Path configFile = configRoot.resolve("runtime/skill-" + sanitizeId(skillId) + ".json");
        if (!Files.exists(configFile)) {
            return null;
        }
        
        ConfigNode config = readJson(configFile);
        updateCache(cacheKey, config);
        return config;
    }

    @Override
    public ConfigNode loadSceneConfig(String sceneId) {
        String cacheKey = "scene:" + sceneId;
        if (isCacheValid(cacheKey)) {
            return cache.get(cacheKey);
        }
        
        Path configFile = configRoot.resolve("runtime/scene-" + sanitizeId(sceneId) + ".json");
        if (!Files.exists(configFile)) {
            return null;
        }
        
        ConfigNode config = readJson(configFile);
        updateCache(cacheKey, config);
        return config;
    }

    @Override
    public ConfigNode loadInternalSkillConfig(String sceneId, String skillId) {
        String cacheKey = "internal:" + sceneId + ":" + skillId;
        if (isCacheValid(cacheKey)) {
            return cache.get(cacheKey);
        }
        
        Path configFile = configRoot.resolve(
            "runtime/scene-" + sanitizeId(sceneId) + "-skill-" + sanitizeId(skillId) + ".json");
        if (!Files.exists(configFile)) {
            return null;
        }
        
        ConfigNode config = readJson(configFile);
        updateCache(cacheKey, config);
        return config;
    }

    @Override
    public void saveSystemConfig(ConfigNode config) {
        Path configFile = configRoot.resolve("system-config.json");
        writeJson(configFile, config);
        invalidateCache("system");
        log.info("[JsonConfigStorage] System config saved");
    }

    @Override
    public void saveSkillConfig(String skillId, ConfigNode config) {
        Path runtimeDir = configRoot.resolve("runtime");
        ensureDirectory(runtimeDir);
        Path configFile = runtimeDir.resolve("skill-" + sanitizeId(skillId) + ".json");
        writeJson(configFile, config);
        invalidateCache("skill:" + skillId);
        log.info("[JsonConfigStorage] Skill config saved: {}", skillId);
    }

    @Override
    public void saveSceneConfig(String sceneId, ConfigNode config) {
        Path runtimeDir = configRoot.resolve("runtime");
        ensureDirectory(runtimeDir);
        Path configFile = runtimeDir.resolve("scene-" + sanitizeId(sceneId) + ".json");
        writeJson(configFile, config);
        invalidateCache("scene:" + sceneId);
        log.info("[JsonConfigStorage] Scene config saved: {}", sceneId);
    }

    @Override
    public void saveInternalSkillConfig(String sceneId, String skillId, ConfigNode config) {
        Path runtimeDir = configRoot.resolve("runtime");
        ensureDirectory(runtimeDir);
        Path configFile = runtimeDir.resolve(
            "scene-" + sanitizeId(sceneId) + "-skill-" + sanitizeId(skillId) + ".json");
        writeJson(configFile, config);
        invalidateCache("internal:" + sceneId + ":" + skillId);
        log.info("[JsonConfigStorage] Internal skill config saved: {} / {}", sceneId, skillId);
    }

    @Override
    public void deleteConfig(String targetType, String targetId) {
        Path configFile = getConfigPath(targetType, targetId);
        
        try {
            Files.deleteIfExists(configFile);
            invalidateCache(targetType + ":" + targetId);
            log.info("[JsonConfigStorage] Config deleted: {} / {}", targetType, targetId);
        } catch (IOException e) {
            throw new ConfigException("Failed to delete config: " + configFile, e);
        }
    }

    @Override
    public boolean exists(String targetType, String targetId) {
        Path configFile = getConfigPath(targetType, targetId);
        return Files.exists(configFile);
    }

    @Override
    public void invalidateCache(String key) {
        cache.remove(key);
        cacheTimestamps.remove(key);
    }

    @Override
    public void invalidateAllCache() {
        cache.clear();
        cacheTimestamps.clear();
    }

    private Path getConfigPath(String targetType, String targetId) {
        switch (targetType) {
            case "system":
                return configRoot.resolve("system-config.json");
            case "skill":
                return configRoot.resolve("runtime/skill-" + sanitizeId(targetId) + ".json");
            case "scene":
                return configRoot.resolve("runtime/scene-" + sanitizeId(targetId) + ".json");
            case "internal_skill":
                String[] parts = targetId.split(":");
                if (parts.length == 2) {
                    return configRoot.resolve(
                        "runtime/scene-" + sanitizeId(parts[0]) + "-skill-" + sanitizeId(parts[1]) + ".json");
                }
                throw new IllegalArgumentException("Invalid internal_skill targetId: " + targetId);
            default:
                throw new IllegalArgumentException("Unknown target type: " + targetType);
        }
    }

    private ConfigNode loadDefaultProfile() {
        try {
            return loadProfile("micro");
        } catch (ConfigNotFoundException e) {
            log.warn("[JsonConfigStorage] Default profile not found, creating default config");
            return createDefaultSystemConfig();
        }
    }

    private ConfigNode createDefaultSystemConfig() {
        Map<String, Object> config = new LinkedHashMap<>();
        config.put("apiVersion", "skills.ooder.io/v1");
        config.put("kind", "SystemConfig");
        
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("name", "ooder-skills-system");
        metadata.put("version", "1.0.0");
        metadata.put("profile", "micro");
        metadata.put("createdAt", Instant.now().toString());
        config.put("metadata", metadata);
        
        Map<String, Object> spec = new LinkedHashMap<>();
        spec.put("capabilities", new LinkedHashMap<>());
        config.put("spec", spec);
        
        return new ConfigNode(config);
    }

    private ConfigNode readJson(Path path) {
        try {
            Map<String, Object> data = objectMapper.readValue(
                path.toFile(), 
                new TypeReference<Map<String, Object>>() {}
            );
            return new ConfigNode(data);
        } catch (IOException e) {
            throw new ConfigLoadException("Failed to load config: " + path, e);
        }
    }

    private void writeJson(Path path, ConfigNode config) {
        try {
            File parentDir = path.getParent().toFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            objectMapper.writeValue(path.toFile(), config.getData());
        } catch (IOException e) {
            throw new ConfigSaveException("Failed to save config: " + path, e);
        }
    }

    private void ensureDirectory(Path dir) {
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            log.warn("[JsonConfigStorage] Failed to create directory: {}", dir);
        }
    }

    private String sanitizeId(String id) {
        return id.replaceAll("[^a-zA-Z0-9_-]", "_");
    }

    private boolean isCacheValid(String key) {
        ConfigNode cached = cache.get(key);
        if (cached == null) {
            return false;
        }
        Long timestamp = cacheTimestamps.get(key);
        if (timestamp == null) {
            return false;
        }
        return System.currentTimeMillis() - timestamp < cacheTtlMillis;
    }

    private void updateCache(String key, ConfigNode config) {
        cache.put(key, config);
        cacheTimestamps.put(key, System.currentTimeMillis());
    }
}
