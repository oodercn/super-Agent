
package net.ooder.sdk.core.skill.loader;

import net.ooder.sdk.api.skill.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SkillManifestLoaderImpl implements SkillManifestLoader {
    
    private static final Logger log = LoggerFactory.getLogger(SkillManifestLoaderImpl.class);
    
    private final List<SkillManifest> loadedManifests = new ArrayList<>();
    
    @Override
    public SkillManifest load(String manifestPath) {
        try {
            Path path = Paths.get(manifestPath);
            if (!Files.exists(path)) {
                log.warn("Manifest file not found: {}", manifestPath);
                return null;
            }
            
            String content = new String(Files.readAllBytes(path), "UTF-8");
            return parseYaml(content);
            
        } catch (Exception e) {
            log.error("Failed to load manifest from: {}", manifestPath, e);
            return null;
        }
    }
    
    @Override
    public SkillManifest load(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return parseYaml(sb.toString());
            
        } catch (Exception e) {
            log.error("Failed to load manifest from stream", e);
            return null;
        }
    }
    
    @Override
    public SkillManifest loadFromClasspath(String resourcePath) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (is == null) {
                log.warn("Resource not found on classpath: {}", resourcePath);
                return null;
            }
            return load(is);
            
        } catch (Exception e) {
            log.error("Failed to load manifest from classpath: {}", resourcePath, e);
            return null;
        }
    }
    
    @Override
    public List<SkillManifest> loadAll() {
        return new ArrayList<>(loadedManifests);
    }
    
    @Override
    public boolean validate(SkillManifest manifest) {
        if (manifest == null) {
            return false;
        }
        
        if (manifest.getSkillId() == null || manifest.getSkillId().isEmpty()) {
            log.warn("Manifest validation failed: missing skillId");
            return false;
        }
        
        if (manifest.getVersion() == null || manifest.getVersion().isEmpty()) {
            log.warn("Manifest validation failed: missing version");
            return false;
        }
        
        return true;
    }
    
    private SkillManifest parseYaml(String content) {
        SkillManifest manifest = new SkillManifest();
        Map<String, Object> data = parseSimpleYaml(content);
        
        if (data.containsKey("skillId")) {
            manifest.setSkillId((String) data.get("skillId"));
        }
        if (data.containsKey("name")) {
            manifest.setName((String) data.get("name"));
        }
        if (data.containsKey("description")) {
            manifest.setDescription((String) data.get("description"));
        }
        if (data.containsKey("version")) {
            manifest.setVersion((String) data.get("version"));
        }
        if (data.containsKey("sceneId")) {
            manifest.setSceneId((String) data.get("sceneId"));
        }
        if (data.containsKey("mainClass")) {
            manifest.setMainClass((String) data.get("mainClass"));
        }
        if (data.containsKey("author")) {
            manifest.setAuthor((String) data.get("author"));
        }
        if (data.containsKey("license")) {
            manifest.setLicense((String) data.get("license"));
        }
        
        if (data.containsKey("collaborativeScenes")) {
            @SuppressWarnings("unchecked")
            List<String> scenes = (List<String>) data.get("collaborativeScenes");
            manifest.setCollaborativeScenes(scenes);
        }
        
        if (data.containsKey("config")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> config = (Map<String, Object>) data.get("config");
            manifest.setConfig(config);
        }
        
        loadedManifests.add(manifest);
        log.info("Loaded manifest: {}", manifest.getSkillId());
        
        return manifest;
    }
    
    private Map<String, Object> parseSimpleYaml(String content) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<String> currentPath = new ArrayList<>();
        
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                continue;
            }
            
            int indent = 0;
            while (indent < line.length() && line.charAt(indent) == ' ') {
                indent++;
            }
            
            String trimmed = line.trim();
            int colonIndex = trimmed.indexOf(':');
            if (colonIndex > 0) {
                String key = trimmed.substring(0, colonIndex).trim();
                String value = colonIndex < trimmed.length() - 1 ? 
                    trimmed.substring(colonIndex + 1).trim() : "";
                
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                } else if (value.startsWith("'") && value.endsWith("'")) {
                    value = value.substring(1, value.length() - 1);
                }
                
                int depth = indent / 2;
                while (currentPath.size() > depth) {
                    currentPath.remove(currentPath.size() - 1);
                }
                
                if (value.isEmpty()) {
                    currentPath.add(key);
                } else {
                    setNestedValue(result, currentPath, key, value);
                }
            }
        }
        
        return result;
    }
    
    @SuppressWarnings("unchecked")
    private void setNestedValue(Map<String, Object> map, List<String> path, String key, Object value) {
        Map<String, Object> current = map;
        for (String p : path) {
            Object next = current.get(p);
            if (!(next instanceof Map)) {
                next = new LinkedHashMap<String, Object>();
                current.put(p, next);
            }
            current = (Map<String, Object>) next;
        }
        current.put(key, value);
    }
}
