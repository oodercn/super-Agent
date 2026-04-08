package net.ooder.skill.discovery.service;

import net.ooder.skill.discovery.dto.discovery.CapabilityDTO;
import net.ooder.skill.discovery.dto.discovery.RepositoryDTO;
import net.ooder.skill.discovery.model.CapabilityCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import org.yaml.snakeyaml.Yaml;

@Service
public class MvpSkillIndexLoader {

    private static final Logger log = LoggerFactory.getLogger(MvpSkillIndexLoader.class);

    @Value("${ooder.skill.index-dir:skill-index}")
    private String skillIndexDir;
    
    @Value("${ooder.mock.enabled:false}")
    private boolean mockEnabled;

    private List<Map<String, Object>> skills = new ArrayList<>();
    private List<Map<String, Object>> scenes = new ArrayList<>();
    private List<Map<String, Object>> categories = new ArrayList<>();
    
    private Set<String> mockInstalledSkills = new HashSet<>();

    @PostConstruct
    public void init() {
        loadSkillIndex();
    }

    public void reload() {
        log.info("[reload] Clearing cache and reloading skill index");
        clearCache();
        loadSkillIndex();
    }
    
    public void clearCache() {
        log.info("[clearCache] Clearing skill index cache");
        skills.clear();
        scenes.clear();
        categories.clear();
        mockInstalledSkills.clear();
    }

    private void loadSkillIndex() {
        log.info("[loadSkillIndex] Loading skill index from directory: {}", skillIndexDir);
        
        Yaml yaml = new Yaml();
        
        try {
            File indexDir = findSkillIndexDir();
            if (indexDir != null) {
                loadFromDirectory(indexDir, yaml);
            } else {
                log.warn("[loadSkillIndex] Skill index directory not found");
            }

            log.info("[loadSkillIndex] Total: {} skills, {} scenes, {} categories",
                skills.size(), scenes.size(), categories.size());
        } catch (Exception e) {
            log.error("[loadSkillIndex] Failed to load skill index: {}", e.getMessage(), e);
        }
    }

    private File findSkillIndexDir() {
        File dir = new File(skillIndexDir);
        if (dir.exists() && dir.isDirectory()) {
            log.info("[findSkillIndexDir] Found index directory: {}", dir.getAbsolutePath());
            return dir;
        }
        log.warn("[findSkillIndexDir] Skill index directory not found: {}", skillIndexDir);
        return null;
    }

    @SuppressWarnings("unchecked")
    private void loadFromDirectory(File indexDir, Yaml yaml) {
        log.info("[loadSkillIndex] Loading from directory: {}", indexDir.getAbsolutePath());
        
        File categoriesFile = new File(indexDir, "categories.yaml");
        if (categoriesFile.exists()) {
            try (InputStream is = new FileInputStream(categoriesFile)) {
                Map<String, Object> data = yaml.load(is);
                List<Map<String, Object>> cats = (List<Map<String, Object>>) data.get("categories");
                if (cats != null && !cats.isEmpty()) {
                    categories = cats;
                    log.info("[loadSkillIndex] Loaded {} categories from categories.yaml", cats.size());
                }
            } catch (Exception e) {
                log.warn("[loadSkillIndex] Failed to load categories.yaml: {}", e.getMessage());
            }
        }

        File skillsDir = new File(indexDir, "skills");
        if (skillsDir.exists() && skillsDir.isDirectory()) {
            File[] skillFiles = skillsDir.listFiles((dir, name) -> name.endsWith(".yaml"));
            if (skillFiles != null) {
                for (File skillFile : skillFiles) {
                    try (InputStream is = new FileInputStream(skillFile)) {
                        Map<String, Object> data = yaml.load(is);
                        List<Map<String, Object>> fileSkills = (List<Map<String, Object>>) data.get("skills");
                        if (fileSkills != null) {
                            skills.addAll(fileSkills);
                            log.info("[loadSkillIndex] Loaded {} skills from {}", fileSkills.size(), skillFile.getName());
                        }
                    } catch (Exception e) {
                        log.warn("[loadSkillIndex] Failed to load {}: {}", skillFile.getName(), e.getMessage());
                    }
                }
            }
        }

        File scenesDir = new File(indexDir, "scenes");
        if (scenesDir.exists() && scenesDir.isDirectory()) {
            File[] sceneFiles = scenesDir.listFiles((dir, name) -> name.endsWith(".yaml"));
            if (sceneFiles != null) {
                for (File sceneFile : sceneFiles) {
                    try (InputStream is = new FileInputStream(sceneFile)) {
                        Map<String, Object> data = yaml.load(is);
                        List<Map<String, Object>> fileScenes = (List<Map<String, Object>>) data.get("scenes");
                        if (fileScenes != null) {
                            scenes.addAll(fileScenes);
                            log.info("[loadSkillIndex] Loaded {} scenes from {}", fileScenes.size(), sceneFile.getName());
                        }
                    } catch (Exception e) {
                        log.warn("[loadSkillIndex] Failed to load {}: {}", sceneFile.getName(), e.getMessage());
                    }
                }
            }
        }
    }

    public List<CapabilityDTO> getSkillsFromIndex(String source) {
        List<CapabilityDTO> capabilities = new ArrayList<>();
        
        for (Map<String, Object> skill : skills) {
            CapabilityDTO cap = new CapabilityDTO();
            String skillId = (String) skill.get("skillId");
            if (skillId == null) {
                skillId = (String) skill.get("id");
            }
            cap.setId(skillId);
            cap.setSkillId(skillId);
            cap.setName((String) skill.get("name"));
            cap.setDescription((String) skill.get("description"));
            cap.setVersion((String) skill.get("version"));
            cap.setSource(source);
            
            boolean isInstalled = checkIfInstalled(skillId);
            cap.setStatus(isInstalled ? "installed" : "available");
            cap.setInstalled(isInstalled);
            
            capabilities.add(cap);
        }
        
        return capabilities;
    }

    public List<CapabilityDTO> getScenesFromIndex(String source) {
        List<CapabilityDTO> capabilities = new ArrayList<>();
        
        for (Map<String, Object> scene : scenes) {
            CapabilityDTO cap = new CapabilityDTO();
            String sceneId = (String) scene.get("sceneId");
            if (sceneId == null) {
                sceneId = (String) scene.get("id");
            }
            cap.setId(sceneId);
            cap.setSkillId(sceneId);
            cap.setName((String) scene.get("name"));
            cap.setDescription((String) scene.get("description"));
            cap.setVersion((String) scene.get("version"));
            cap.setSource(source);
            
            boolean isInstalled = checkIfInstalled(sceneId);
            cap.setStatus(isInstalled ? "installed" : "available");
            cap.setInstalled(isInstalled);
            
            cap.setType("SCENE");
            cap.setSceneCapability(true);
            
            capabilities.add(cap);
        }
        
        return capabilities;
    }

    public List<RepositoryDTO> getRepositories(String source) {
        List<RepositoryDTO> repos = new ArrayList<>();
        
        for (Map<String, Object> skill : skills) {
            RepositoryDTO repo = new RepositoryDTO();
            repo.setFullName((String) skill.get("skillId"));
            repo.setName((String) skill.get("name"));
            repo.setDescription((String) skill.get("description"));
            repo.setHtmlUrl((String) skill.get("giteeDownloadUrl"));
            repo.setLatestVersion((String) skill.get("version"));
            repos.add(repo);
        }
        
        return repos;
    }

    public Map<String, Object> getSkillIndex() {
        Map<String, Object> result = new HashMap<>();
        result.put("skills", skills);
        result.put("scenes", scenes);
        result.put("categories", categories);
        return result;
    }

    public List<Map<String, Object>> getSkills() {
        return skills;
    }

    public List<Map<String, Object>> getScenes() {
        return scenes;
    }

    public List<Map<String, Object>> getCategories() {
        return categories;
    }

    public List<CapabilityDTO> getAllCapabilities(String source) {
        List<CapabilityDTO> all = new ArrayList<>();
        all.addAll(getSkillsFromIndex(source));
        all.addAll(getScenesFromIndex(source));
        return all;
    }
    
    private boolean checkIfInstalled(String skillId) {
        if (skillId == null) {
            return false;
        }
        
        if (mockEnabled && mockInstalledSkills.contains(skillId)) {
            return true;
        }
        
        return false;
    }
    
    public void markAsInstalled(String skillId) {
        if (skillId != null) {
            mockInstalledSkills.add(skillId);
            log.info("[markAsInstalled] Marked {} as installed (mock mode)", skillId);
        }
    }
    
    public void markAsUninstalled(String skillId) {
        if (skillId != null) {
            mockInstalledSkills.remove(skillId);
            log.info("[markAsUninstalled] Marked {} as uninstalled (mock mode)", skillId);
        }
    }

    public Map<String, Object> getSkillInfo(String skillId) {
        if (skillId == null || skills == null) {
            return null;
        }
        
        for (Map<String, Object> skill : skills) {
            String id = (String) skill.get("skillId");
            if (skillId.equals(id)) {
                return skill;
            }
        }
        
        return null;
    }
    
    public String getDownloadUrl(String skillId) {
        Map<String, Object> skill = getSkillInfo(skillId);
        if (skill != null) {
            String giteeUrl = (String) skill.get("giteeDownloadUrl");
            String githubUrl = (String) skill.get("downloadUrl");
            return giteeUrl != null ? giteeUrl : githubUrl;
        }
        return null;
    }
}
