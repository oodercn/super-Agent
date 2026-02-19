
package net.ooder.sdk.core.skill.discovery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.skill.Capability;
import net.ooder.sdk.api.skill.SkillDiscoverer;
import net.ooder.sdk.api.skill.SkillManifest;
import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.common.enums.DiscoveryMethod;

public class LocalDiscoverer implements SkillDiscoverer {
    
    private static final Logger log = LoggerFactory.getLogger(LocalDiscoverer.class);
    
    private static final String MANIFEST_FILE = "skill.json";
    private static final String SKILLS_DIR = "skills";
    
    private long timeout = 30000;
    private DiscoveryFilter filter;
    private String skillsDirectory;
    
    public LocalDiscoverer() {
        this.skillsDirectory = System.getProperty("user.home") + File.separator + ".ooder" + File.separator + SKILLS_DIR;
    }
    
    public LocalDiscoverer(String skillsDirectory) {
        this.skillsDirectory = skillsDirectory;
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> discover() {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillPackage> packages = new ArrayList<>();
            Path skillsPath = Paths.get(skillsDirectory);
            
            if (!Files.exists(skillsPath)) {
                log.debug("Skills directory does not exist: {}", skillsDirectory);
                return packages;
            }
            
            try {
                List<Path> skillDirs = Files.walk(skillsPath, 1)
                    .filter(Files::isDirectory)
                    .filter(p -> !p.equals(skillsPath))
                    .collect(Collectors.toList());
                
                for (Path skillDir : skillDirs) {
                    SkillPackage pkg = loadSkillPackage(skillDir);
                    if (pkg != null && passesFilter(pkg)) {
                        packages.add(pkg);
                    }
                }
                
                log.info("Discovered {} skill packages from local directory", packages.size());
            } catch (IOException e) {
                log.error("Error scanning skills directory: {}", e.getMessage());
            }
            
            return packages;
        });
    }
    
    @Override
    public CompletableFuture<SkillPackage> discover(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            log.debug("Discovering skill locally: {}", skillId);
            
            Path skillPath = Paths.get(skillsDirectory, skillId);
            if (!Files.exists(skillPath)) {
                log.debug("Skill not found locally: {}", skillId);
                return null;
            }
            
            SkillPackage pkg = loadSkillPackage(skillPath);
            if (pkg != null && passesFilter(pkg)) {
                return pkg;
            }
            
            return null;
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> discoverByScene(String sceneId) {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillPackage> result = new ArrayList<>();
            Path skillsPath = Paths.get(skillsDirectory);
            
            if (!Files.exists(skillsPath)) {
                return result;
            }
            
            try {
                List<Path> skillDirs = Files.walk(skillsPath, 1)
                    .filter(Files::isDirectory)
                    .filter(p -> !p.equals(skillsPath))
                    .collect(Collectors.toList());
                
                for (Path skillDir : skillDirs) {
                    SkillPackage pkg = loadSkillPackage(skillDir);
                    if (pkg != null && sceneId.equals(pkg.getSceneId()) && passesFilter(pkg)) {
                        result.add(pkg);
                    }
                }
                
                log.debug("Found {} skills for scene: {}", result.size(), sceneId);
            } catch (IOException e) {
                log.error("Error scanning skills directory: {}", e.getMessage());
            }
            
            return result;
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> search(String query) {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillPackage> result = new ArrayList<>();
            Path skillsPath = Paths.get(skillsDirectory);
            
            if (!Files.exists(skillsPath) || query == null || query.isEmpty()) {
                return result;
            }
            
            String lowerQuery = query.toLowerCase();
            
            try {
                List<Path> skillDirs = Files.walk(skillsPath, 1)
                    .filter(Files::isDirectory)
                    .filter(p -> !p.equals(skillsPath))
                    .collect(Collectors.toList());
                
                for (Path skillDir : skillDirs) {
                    SkillPackage pkg = loadSkillPackage(skillDir);
                    if (pkg != null && matchesQuery(pkg, lowerQuery) && passesFilter(pkg)) {
                        result.add(pkg);
                    }
                }
                
                log.debug("Found {} skills matching query: {}", result.size(), query);
            } catch (IOException e) {
                log.error("Error searching skills: {}", e.getMessage());
            }
            
            return result;
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> searchByCapability(String capabilityId) {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillPackage> result = new ArrayList<>();
            Path skillsPath = Paths.get(skillsDirectory);
            
            if (!Files.exists(skillsPath) || capabilityId == null || capabilityId.isEmpty()) {
                return result;
            }
            
            try {
                List<Path> skillDirs = Files.walk(skillsPath, 1)
                    .filter(Files::isDirectory)
                    .filter(p -> !p.equals(skillsPath))
                    .collect(Collectors.toList());
                
                for (Path skillDir : skillDirs) {
                    SkillPackage pkg = loadSkillPackage(skillDir);
                    if (pkg != null && hasCapability(pkg, capabilityId) && passesFilter(pkg)) {
                        result.add(pkg);
                    }
                }
                
                log.debug("Found {} skills with capability: {}", result.size(), capabilityId);
            } catch (IOException e) {
                log.error("Error searching skills by capability: {}", e.getMessage());
            }
            
            return result;
        });
    }
    
    @Override
    public DiscoveryMethod getMethod() {
        return DiscoveryMethod.LOCAL_FS;
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
    
    @Override
    public void setTimeout(long timeoutMs) {
        this.timeout = timeoutMs;
    }
    
    @Override
    public long getTimeout() {
        return timeout;
    }
    
    @Override
    public void setFilter(DiscoveryFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public DiscoveryFilter getFilter() {
        return filter;
    }
    
    public void setSkillsDirectory(String skillsDirectory) {
        this.skillsDirectory = skillsDirectory;
    }
    
    public String getSkillsDirectory() {
        return skillsDirectory;
    }
    
    private SkillPackage loadSkillPackage(Path skillDir) {
        Path manifestPath = skillDir.resolve(MANIFEST_FILE);
        if (!Files.exists(manifestPath)) {
            return null;
        }
        
        try {
            String content = readFileContent(manifestPath);
            return parseSkillPackage(content, skillDir);
        } catch (IOException e) {
            log.warn("Failed to load skill manifest from {}: {}", manifestPath, e.getMessage());
            return null;
        }
    }
    
    private String readFileContent(Path path) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(path.toFile()), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
    
    private SkillPackage parseSkillPackage(String json, Path skillDir) {
        SkillPackage pkg = new SkillPackage();
        SkillManifest manifest = new SkillManifest();
        
        Map<String, Object> data = parseJson(json);
        
        String skillId = getString(data, "skillId");
        if (skillId == null) {
            skillId = skillDir.getFileName().toString();
        }
        
        pkg.setSkillId(skillId);
        pkg.setName(getString(data, "name"));
        pkg.setDescription(getString(data, "description"));
        pkg.setVersion(getString(data, "version"));
        pkg.setSceneId(getString(data, "sceneId"));
        pkg.setSource("local:" + skillDir.toString());
        
        manifest.setSkillId(skillId);
        manifest.setName(pkg.getName());
        manifest.setDescription(pkg.getDescription());
        manifest.setVersion(pkg.getVersion());
        manifest.setSceneId(pkg.getSceneId());
        manifest.setMainClass(getString(data, "mainClass"));
        manifest.setAuthor(getString(data, "author"));
        manifest.setLicense(getString(data, "license"));
        
        pkg.setManifest(manifest);
        
        try {
            pkg.setSize(getDirectorySize(skillDir));
        } catch (IOException e) {
            pkg.setSize(0);
        }
        
        return pkg;
    }
    
    private Map<String, Object> parseJson(String json) {
        Map<String, Object> result = new HashMap<>();
        if (json == null || json.isEmpty()) {
            return result;
        }
        
        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith("}")) {
            return result;
        }
        
        json = json.substring(1, json.length() - 1).trim();
        
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean inKey = true;
        boolean inString = false;
        boolean inValue = false;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
                continue;
            }
            
            if (!inString) {
                if (c == ':' && inKey) {
                    inKey = false;
                    inValue = true;
                    continue;
                }
                if (c == ',' && !inKey) {
                    String k = key.toString().trim();
                    String v = value.toString().trim();
                    if (!k.isEmpty()) {
                        result.put(k, parseValue(v));
                    }
                    key = new StringBuilder();
                    value = new StringBuilder();
                    inKey = true;
                    inValue = false;
                    continue;
                }
            }
            
            if (inKey) {
                key.append(c);
            } else if (inValue) {
                value.append(c);
            }
        }
        
        String k = key.toString().trim();
        String v = value.toString().trim();
        if (!k.isEmpty()) {
            result.put(k, parseValue(v));
        }
        
        return result;
    }
    
    private Object parseValue(String v) {
        if (v == null || v.isEmpty()) return null;
        v = v.trim();
        if ("null".equals(v)) return null;
        if ("true".equals(v)) return true;
        if ("false".equals(v)) return false;
        if (v.startsWith("\"") && v.endsWith("\"")) {
            return v.substring(1, v.length() - 1);
        }
        try {
            if (v.contains(".")) {
                return Double.parseDouble(v);
            }
            return Long.parseLong(v);
        } catch (NumberFormatException e) {
            return v;
        }
    }
    
    private String getString(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : null;
    }
    
    private long getDirectorySize(Path dir) throws IOException {
        return Files.walk(dir)
            .filter(Files::isRegularFile)
            .mapToLong(p -> {
                try {
                    return Files.size(p);
                } catch (IOException e) {
                    return 0;
                }
            })
            .sum();
    }
    
    private boolean matchesQuery(SkillPackage pkg, String query) {
        if (pkg.getName() != null && pkg.getName().toLowerCase().contains(query)) {
            return true;
        }
        if (pkg.getDescription() != null && pkg.getDescription().toLowerCase().contains(query)) {
            return true;
        }
        if (pkg.getSkillId() != null && pkg.getSkillId().toLowerCase().contains(query)) {
            return true;
        }
        return false;
    }
    
    private boolean hasCapability(SkillPackage pkg, String capabilityId) {
        List<Capability> capabilities = pkg.getCapabilities();
        if (capabilities == null) {
            return false;
        }
        for (Capability cap : capabilities) {
            if (capabilityId.equals(cap.getCapId())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean passesFilter(SkillPackage pkg) {
        if (filter == null) {
            return true;
        }
        if (filter.getSceneId() != null && !filter.getSceneId().equals(pkg.getSceneId())) {
            return false;
        }
        if (filter.getVersion() != null && !filter.getVersion().equals(pkg.getVersion())) {
            return false;
        }
        return true;
    }
}
