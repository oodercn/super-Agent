package net.ooder.skillcenter.storage;

import com.alibaba.fastjson.JSON;
import net.ooder.nexus.skillcenter.dto.skill.SkillManifestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 技能清单存储服务 - 符合v0.7.0协议规范
 */
public class SkillManifestStorage {
    private static final Logger logger = LoggerFactory.getLogger(SkillManifestStorage.class);
    
    private static final String MANIFESTS_DIR = "manifests";
    private static final String MANIFEST_FILE = "manifests.json";
    
    private final Path storagePath;
    private final Map<String, SkillManifestDTO> manifestCache;
    
    public SkillManifestStorage(String basePath) {
        this.storagePath = Paths.get(basePath, MANIFESTS_DIR);
        this.manifestCache = new ConcurrentHashMap<>();
        initialize();
    }
    
    private void initialize() {
        try {
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }
            loadAllManifests();
        } catch (IOException e) {
            logger.error("Failed to initialize manifest storage: {}", e.getMessage(), e);
        }
    }
    
    private void loadAllManifests() {
        try {
            Path manifestFile = storagePath.resolve(MANIFEST_FILE);
            if (Files.exists(manifestFile)) {
                String content = new String(Files.readAllBytes(manifestFile), StandardCharsets.UTF_8);
                List<SkillManifestDTO> manifests = JSON.parseArray(content, SkillManifestDTO.class);
                if (manifests != null) {
                    for (SkillManifestDTO manifest : manifests) {
                        if (manifest.getMetadata() != null && manifest.getMetadata().getId() != null) {
                            manifestCache.put(manifest.getMetadata().getId(), manifest);
                        }
                    }
                }
                logger.info("Loaded {} skill manifests", manifestCache.size());
            }
        } catch (Exception e) {
            logger.error("Failed to load manifests: {}", e.getMessage(), e);
        }
    }
    
    public boolean saveManifest(SkillManifestDTO manifest) {
        if (manifest == null || manifest.getMetadata() == null || manifest.getMetadata().getId() == null) {
            return false;
        }
        
        String skillId = manifest.getMetadata().getId();
        manifestCache.put(skillId, manifest);
        
        try {
            persistAllManifests();
            logger.info("Saved manifest for skill: {}", skillId);
            return true;
        } catch (Exception e) {
            logger.error("Failed to save manifest for {}: {}", skillId, e.getMessage(), e);
            return false;
        }
    }
    
    public SkillManifestDTO getManifest(String skillId) {
        return manifestCache.get(skillId);
    }
    
    public boolean deleteManifest(String skillId) {
        if (skillId == null) {
            return false;
        }
        
        SkillManifestDTO removed = manifestCache.remove(skillId);
        if (removed != null) {
            try {
                persistAllManifests();
                logger.info("Deleted manifest for skill: {}", skillId);
                return true;
            } catch (Exception e) {
                logger.error("Failed to persist after deleting manifest: {}", e.getMessage(), e);
                manifestCache.put(skillId, removed);
                return false;
            }
        }
        return false;
    }
    
    public List<SkillManifestDTO> getAllManifests() {
        return new ArrayList<>(manifestCache.values());
    }
    
    public List<SkillManifestDTO> searchByCapabilities(List<String> capabilities) {
        if (capabilities == null || capabilities.isEmpty()) {
            return getAllManifests();
        }
        
        return manifestCache.values().stream()
                .filter(m -> m.getSpec() != null && m.getSpec().getCapabilities() != null)
                .filter(m -> m.getSpec().getCapabilities().stream()
                        .anyMatch(c -> capabilities.contains(c.getId())))
                .collect(Collectors.toList());
    }
    
    public List<SkillManifestDTO> searchByScenes(List<String> scenes) {
        if (scenes == null || scenes.isEmpty()) {
            return getAllManifests();
        }
        
        return manifestCache.values().stream()
                .filter(m -> m.getSpec() != null && m.getSpec().getScenes() != null)
                .filter(m -> m.getSpec().getScenes().stream()
                        .anyMatch(s -> scenes.contains(s.getName())))
                .collect(Collectors.toList());
    }
    
    public List<SkillManifestDTO> searchByType(String type) {
        if (type == null || type.isEmpty()) {
            return getAllManifests();
        }
        
        return manifestCache.values().stream()
                .filter(m -> m.getSpec() != null && type.equals(m.getSpec().getType()))
                .collect(Collectors.toList());
    }
    
    public List<SkillManifestDTO> searchByKeywords(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return getAllManifests();
        }
        
        return manifestCache.values().stream()
                .filter(m -> {
                    if (m.getMetadata() == null) return false;
                    String name = m.getMetadata().getName() != null ? 
                            m.getMetadata().getName().toLowerCase() : "";
                    String desc = m.getMetadata().getDescription() != null ? 
                            m.getMetadata().getDescription().toLowerCase() : "";
                    return keywords.stream()
                            .anyMatch(k -> name.contains(k.toLowerCase()) || desc.contains(k.toLowerCase()));
                })
                .collect(Collectors.toList());
    }
    
    private void persistAllManifests() throws IOException {
        Path manifestFile = storagePath.resolve(MANIFEST_FILE);
        String json = JSON.toJSONString(new ArrayList<>(manifestCache.values()), true);
        Files.write(manifestFile, json.getBytes(StandardCharsets.UTF_8));
    }
    
    public int getManifestCount() {
        return manifestCache.size();
    }
    
    public void clear() {
        manifestCache.clear();
        try {
            Path manifestFile = storagePath.resolve(MANIFEST_FILE);
            if (Files.exists(manifestFile)) {
                Files.delete(manifestFile);
            }
            logger.info("Cleared all manifests");
        } catch (IOException e) {
            logger.error("Failed to clear manifests: {}", e.getMessage(), e);
        }
    }
}
