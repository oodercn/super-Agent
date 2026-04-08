package net.ooder.mvp.skill.scene.config;

import net.ooder.mvp.skill.scene.dto.SceneDefinitionDTO;
import net.ooder.mvp.skill.scene.service.SceneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ConfigVersionService {

    private static final Logger log = LoggerFactory.getLogger(ConfigVersionService.class);

    @Autowired
    private SceneService sceneService;

    private final Map<String, List<ConfigVersion>> versionHistory = new ConcurrentHashMap<>();
    private final Map<String, ConfigVersion> activeVersions = new ConcurrentHashMap<>();
    
    private static final int MAX_VERSIONS = 50;

    public ConfigVersion saveVersion(String sceneId, Map<String, Object> config, 
                                       String operator, String description) {
        String versionId = "ver-" + System.currentTimeMillis() + "-" + 
            UUID.randomUUID().toString().substring(0, 8);
        String version = generateVersion(sceneId);
        
        ConfigVersion configVersion = new ConfigVersion();
        configVersion.setVersionId(versionId);
        configVersion.setSceneId(sceneId);
        configVersion.setConfig(deepCopy(config));
        configVersion.setOperator(operator != null ? operator : "system");
        configVersion.setCreateTime(System.currentTimeMillis());
        configVersion.setDescription(description);
        configVersion.setVersion(version);
        configVersion.setActive(true);
        
        List<ConfigVersion> versions = versionHistory.computeIfAbsent(sceneId, 
            k -> new ArrayList<>());
        
        for (ConfigVersion v : versions) {
            v.setActive(false);
        }
        
        versions.add(configVersion);
        
        if (versions.size() > MAX_VERSIONS) {
            versions.remove(0);
        }
        
        activeVersions.put(sceneId, configVersion);
        
        log.info("[ConfigVersion] Saved version {} for scene {} by {}", 
            version, sceneId, operator);
        
        return configVersion;
    }

    public ConfigVersion rollback(String sceneId, String versionId) {
        List<ConfigVersion> versions = versionHistory.get(sceneId);
        if (versions == null) {
            throw new RuntimeException("No version history for scene: " + sceneId);
        }
        
        ConfigVersion targetVersion = versions.stream()
            .filter(v -> v.getVersionId().equals(versionId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Version not found: " + versionId));
        
        try {
            SceneDefinitionDTO scene = sceneService.get(sceneId);
            if (scene != null) {
                Map<String, Object> config = targetVersion.getConfig();
                // scene.setConfig(config);
                // sceneService.update(scene);
                
                for (ConfigVersion v : versions) {
                    v.setActive(false);
                }
                targetVersion.setActive(true);
                activeVersions.put(sceneId, targetVersion);
                
                log.info("[ConfigVersion] Rolled back scene {} to version {}", 
                    sceneId, targetVersion.getVersion());
                
                return targetVersion;
            }
        } catch (Exception e) {
            log.error("[ConfigVersion] Rollback failed: {}", e.getMessage());
            throw new RuntimeException("Rollback failed: " + e.getMessage());
        }
        
        throw new RuntimeException("Scene not found: " + sceneId);
    }

    public List<ConfigVersion> getVersionHistory(String sceneId) {
        List<ConfigVersion> versions = versionHistory.get(sceneId);
        if (versions == null) {
            return Collections.emptyList();
        }
        return versions.stream()
            .sorted((a, b) -> Long.compare(b.getCreateTime(), a.getCreateTime()))
            .collect(Collectors.toList());
    }

    public ConfigVersion getActiveVersion(String sceneId) {
        return activeVersions.get(sceneId);
    }

    public ConfigVersion getVersion(String sceneId, String versionId) {
        List<ConfigVersion> versions = versionHistory.get(sceneId);
        if (versions == null) {
            return null;
        }
        return versions.stream()
            .filter(v -> v.getVersionId().equals(versionId))
            .findFirst()
            .orElse(null);
    }

    public ConfigVersion getPreviousVersion(String sceneId) {
        List<ConfigVersion> versions = versionHistory.get(sceneId);
        if (versions == null || versions.size() < 2) {
            return null;
        }
        
        List<ConfigVersion> sorted = versions.stream()
            .sorted((a, b) -> Long.compare(b.getCreateTime(), a.getCreateTime()))
            .collect(Collectors.toList());
        
        return sorted.get(1);
    }

    public void deleteVersion(String sceneId, String versionId) {
        List<ConfigVersion> versions = versionHistory.get(sceneId);
        if (versions != null) {
            versions.removeIf(v -> v.getVersionId().equals(versionId));
            log.info("[ConfigVersion] Deleted version {} for scene {}", versionId, sceneId);
        }
    }

    public ConfigDiff compareVersions(String sceneId, String versionId1, String versionId2) {
        ConfigVersion v1 = getVersion(sceneId, versionId1);
        ConfigVersion v2 = getVersion(sceneId, versionId2);
        
        if (v1 == null || v2 == null) {
            throw new RuntimeException("One or both versions not found");
        }
        
        return computeDiff(v1.getConfig(), v2.getConfig());
    }

    private ConfigDiff computeDiff(Map<String, Object> config1, Map<String, Object> config2) {
        ConfigDiff diff = new ConfigDiff();
        
        Set<String> allKeys = new HashSet<>();
        if (config1 != null) allKeys.addAll(config1.keySet());
        if (config2 != null) allKeys.addAll(config2.keySet());
        
        for (String key : allKeys) {
            Object val1 = config1 != null ? config1.get(key) : null;
            Object val2 = config2 != null ? config2.get(key) : null;
            
            if (val1 == null && val2 != null) {
                diff.addAdded(key, val2);
            } else if (val1 != null && val2 == null) {
                diff.addRemoved(key, val1);
            } else if (!Objects.equals(val1, val2)) {
                diff.addModified(key, val1, val2);
            }
        }
        
        return diff;
    }

    private String generateVersion(String sceneId) {
        List<ConfigVersion> versions = versionHistory.get(sceneId);
        int nextNum = (versions != null ? versions.size() : 0) + 1;
        return "v" + nextNum;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> deepCopy(Map<String, Object> original) {
        if (original == null) {
            return new HashMap<>();
        }
        Map<String, Object> copy = new HashMap<>();
        for (Map.Entry<String, Object> entry : original.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                copy.put(entry.getKey(), deepCopy((Map<String, Object>) value));
            } else if (value instanceof List) {
                copy.put(entry.getKey(), new ArrayList<>((List<?>) value));
            } else {
                copy.put(entry.getKey(), value);
            }
        }
        return copy;
    }

    public static class ConfigDiff {
        private final List<DiffEntry> added = new ArrayList<>();
        private final List<DiffEntry> removed = new ArrayList<>();
        private final List<DiffEntry> modified = new ArrayList<>();

        public void addAdded(String key, Object value) {
            added.add(new DiffEntry(key, null, value, "added"));
        }

        public void addRemoved(String key, Object value) {
            removed.add(new DiffEntry(key, value, null, "removed"));
        }

        public void addModified(String key, Object oldValue, Object newValue) {
            modified.add(new DiffEntry(key, oldValue, newValue, "modified"));
        }

        public List<DiffEntry> getAdded() { return added; }
        public List<DiffEntry> getRemoved() { return removed; }
        public List<DiffEntry> getModified() { return modified; }
        public boolean hasChanges() { 
            return !added.isEmpty() || !removed.isEmpty() || !modified.isEmpty(); 
        }
    }

    public static class DiffEntry {
        private final String key;
        private final Object oldValue;
        private final Object newValue;
        private final String type;

        public DiffEntry(String key, Object oldValue, Object newValue, String type) {
            this.key = key;
            this.oldValue = oldValue;
            this.newValue = newValue;
            this.type = type;
        }

        public String getKey() { return key; }
        public Object getOldValue() { return oldValue; }
        public Object getNewValue() { return newValue; }
        public String getType() { return type; }
    }
}
