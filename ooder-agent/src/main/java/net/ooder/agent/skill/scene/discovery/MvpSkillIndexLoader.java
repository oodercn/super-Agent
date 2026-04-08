package net.ooder.mvp.skill.scene.discovery;

import net.ooder.scene.skill.model.SceneType;
import net.ooder.scene.skill.model.SkillForm;
import net.ooder.mvp.skill.scene.capability.model.CapabilityCategory;
import net.ooder.mvp.skill.scene.capability.service.MetadataCompat;
import net.ooder.mvp.skill.scene.dto.discovery.CapabilityDTO;
import net.ooder.mvp.skill.scene.dto.discovery.RepositoryDTO;
import net.ooder.skills.api.SkillPackageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

@Service("mvpSkillIndexLoader")
public class MvpSkillIndexLoader {

    private static final Logger log = LoggerFactory.getLogger(MvpSkillIndexLoader.class);

    @Value("${ooder.skill.index-dir:skill-index}")
    private String skillIndexDir;
    
    @Value("${ooder.mock.enabled:false}")
    private boolean mockEnabled;
    
    @Value("${ooder.skills.directories.downloads:./.ooder/downloads}")
    private String downloadsDir;
    
    @Value("${ooder.skills.directories.installed:./.ooder/installed}")
    private String installedDir;
    
    @Value("${ooder.skills.directories.activated:./.ooder/activated}")
    private String activatedDir;
    
    @Value("${ooder.skills.directories.dev:./.ooder/dev}")
    private String devDir;

    @Autowired(required = false)
    private SkillPackageManager skillPackageManager;
    
    @Autowired(required = false)
    private net.ooder.mvp.skill.scene.capability.service.CapabilityService capabilityService;
    
    @Autowired(required = false)
    private net.ooder.mvp.skill.scene.capability.service.CapabilityStateService capabilityStateService;

    private List<Map<String, Object>> skills = new ArrayList<>();
    private List<Map<String, Object>> scenes = new ArrayList<>();
    private List<Map<String, Object>> categories = new ArrayList<>();
    private List<Map<String, Object>> sceneDrivers = new ArrayList<>();
    
    private Set<String> mockInstalledSkills = new HashSet<>();
    private Set<String> locallyInstalledSkills = new HashSet<>();
    private long lastRegistryCheck = 0;
    private static final long REGISTRY_CHECK_INTERVAL = 30000;

    @PostConstruct
    public void init() {
        loadSkillIndex();
        loadLocalRegistry();
    }
    
    private void loadLocalRegistry() {
        locallyInstalledSkills.clear();
        
        String[] registryPaths = {
            "./data/installed-skills/registry.properties",
            "./.ooder/installed/registry.properties",
            "./.ooder/registry.properties"
        };
        
        for (String registryPath : registryPaths) {
            File registryFile = new File(registryPath);
            if (registryFile.exists()) {
                try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(registryFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("#") || line.trim().isEmpty()) continue;
                        int eqIdx = line.indexOf('=');
                        if (eqIdx > 0) {
                            String key = line.substring(0, eqIdx);
                            if (key.endsWith(".id")) {
                                String skillId = line.substring(eqIdx + 1);
                                locallyInstalledSkills.add(skillId);
                                log.debug("[loadLocalRegistry] Found installed skill: {}", skillId);
                            }
                        }
                    }
                    log.info("[loadLocalRegistry] Loaded {} installed skills from {}", locallyInstalledSkills.size(), registryPath);
                    return;
                } catch (Exception e) {
                    log.warn("[loadLocalRegistry] Failed to load registry from {}: {}", registryPath, e.getMessage());
                }
            }
        }
        log.info("[loadLocalRegistry] No local registry found, {} skills in memory", locallyInstalledSkills.size());
    }

    public void reload() {
        log.info("[reload] Clearing cache and reloading skill index");
        clearCache();
        loadSkillIndex();
        loadLocalRegistry();
    }
    
    public void clearCache() {
        log.info("[clearCache] Clearing skill index cache");
        skills.clear();
        scenes.clear();
        categories.clear();
        sceneDrivers.clear();
        mockInstalledSkills.clear();
        locallyInstalledSkills.clear();
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

        File sceneDriversFile = new File(indexDir, "scene-drivers.yaml");
        if (sceneDriversFile.exists()) {
            try (InputStream is = new FileInputStream(sceneDriversFile)) {
                Map<String, Object> data = yaml.load(is);
                List<Map<String, Object>> drivers = (List<Map<String, Object>>) data.get("sceneDrivers");
                if (drivers != null && !drivers.isEmpty()) {
                    sceneDrivers = drivers;
                    log.info("[loadSkillIndex] Loaded {} sceneDrivers from scene-drivers.yaml", drivers.size());
                }
            } catch (Exception e) {
                log.warn("[loadSkillIndex] Failed to load scene-drivers.yaml: {}", e.getMessage());
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
        
        Set<String> sceneIds = new HashSet<>();
        for (Map<String, Object> scene : scenes) {
            String sceneId = (String) scene.get("sceneId");
            if (sceneId != null) {
                sceneIds.add(sceneId);
            }
        }
        
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
            
            Object caps = skill.get("capabilities");
            if (caps instanceof List) {
                cap.setCapabilities((List<String>) caps);
            }
            
            Object typeObj = skill.get("type");
            String type = typeObj != null ? String.valueOf(typeObj) : null;
            
            Object sceneIdObj = skill.get("sceneId");
            String sceneId = sceneIdObj != null ? String.valueOf(sceneIdObj) : null;
            
            boolean isScene = "SCENE".equals(type) || "scene".equals(type) || "scene-skill".equals(type) || sceneId != null;
            
            cap.setType(isScene ? "SCENE" : "SKILL");
            cap.setSceneCapability(isScene);
            
            Object skillFormObj = skill.get("skillForm");
            String skillFormCode = skillFormObj != null ? String.valueOf(skillFormObj) : null;
            if (skillFormCode == null) {
                skillFormCode = isScene ? "SCENE" : "PROVIDER";
            }
            cap.setSkillForm(skillFormCode);
            
            Object businessCategoryObj = skill.get("businessCategory");
            if (businessCategoryObj == null) {
                businessCategoryObj = skill.get("capabilityCategory");
            }
            if (businessCategoryObj == null) {
                businessCategoryObj = skill.get("category");
            }
            if (businessCategoryObj != null) {
                cap.setBusinessCategory(String.valueOf(businessCategoryObj));
            }
            
            Object categoryObj = skill.get("category");
            if (categoryObj == null) {
                categoryObj = skill.get("capabilityCategory");
            }
            if (categoryObj != null) {
                String categoryStr = String.valueOf(categoryObj);
                CapabilityCategory category = CapabilityCategory.fromCode(categoryStr);
                cap.setCategory(category.getCode());
                cap.setCapabilityCategory(category.getCode());
            }
            
            Object visibilityObj = skill.get("visibility");
            if (visibilityObj == null) {
                Object labelsObj = skill.get("labels");
                if (labelsObj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> labels = (Map<String, Object>) labelsObj;
                    visibilityObj = labels.get("scene.visibility");
                }
            }
            cap.setVisibility(visibilityObj != null ? String.valueOf(visibilityObj) : 
                MetadataCompat.getVisibility(skill));
            
            if (isScene) {
                Object sceneTypeObj = skill.get("sceneType");
                String sceneTypeCode = sceneTypeObj != null ? String.valueOf(sceneTypeObj) : "MANUAL";
                
                boolean hasSelfDrive = "AUTO".equals(sceneTypeCode);
                
                cap.setSceneType(sceneTypeCode);
                cap.setMainFirst(hasSelfDrive);
                
                Object driverConditionsObj = skill.get("driverConditions");
                if (driverConditionsObj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> driverConditions = (List<Map<String, Object>>) driverConditionsObj;
                    cap.setDriverConditions(convertToMapList(driverConditions));
                }
                
                Object participantsObj = skill.get("participants");
                if (participantsObj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> participants = (List<Map<String, Object>>) participantsObj;
                    cap.setParticipants(convertToMapList(participants));
                }
            }
            
            capabilities.add(cap);
        }
        
        return capabilities;
    }
    
    private List<Map<String, Object>> convertToMapList(List<Map<String, Object>> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> item : list) {
            result.add(new HashMap<>(item));
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public List<CapabilityDTO> getSkillsFromEntryFiles(String source) {
        List<CapabilityDTO> capabilities = new ArrayList<>();
        
        List<File> entryFiles = findSkillIndexEntryFiles();
        log.info("[getSkillsFromEntryFiles] Found {} skill-index-entry.yaml files", entryFiles.size());
        
        Yaml yaml = new Yaml();
        
        for (File entryFile : entryFiles) {
            try (InputStream is = new FileInputStream(entryFile)) {
                Map<String, Object> entry = yaml.load(is);
                if (entry == null) continue;
                
                Map<String, Object> metadata = (Map<String, Object>) entry.get("metadata");
                Map<String, Object> spec = (Map<String, Object>) entry.get("spec");
                
                if (metadata == null || spec == null) continue;
                
                CapabilityDTO cap = new CapabilityDTO();
                String skillId = (String) metadata.get("id");
                cap.setId(skillId);
                cap.setName((String) metadata.get("name"));
                cap.setDescription((String) metadata.get("description"));
                cap.setVersion((String) metadata.get("version"));
                cap.setSource(source);
                
                boolean isInstalled = checkIfInstalled(skillId);
                cap.setStatus(isInstalled ? "installed" : "available");
                
                String skillForm = (String) spec.get("skillForm");
                cap.setSkillForm(skillForm != null ? skillForm : "PROVIDER");
                
                String sceneType = (String) spec.get("sceneType");
                cap.setSceneType(sceneType);
                
                String visibility = (String) spec.get("visibility");
                cap.setVisibility(visibility != null ? visibility : "public");
                
                String businessCategory = (String) spec.get("businessCategory");
                cap.setBusinessCategory(businessCategory);
                
                String capabilityCategory = (String) spec.get("capabilityCategory");
                cap.setCapabilityCategory(capabilityCategory);
                
                String category = (String) spec.get("category");
                
                // 【修复】从实际 skill.yaml 文件读取 spec.capability.category
                if (category == null || category.isEmpty()) {
                    String categoryFromSkillYaml = readCategoryFromSkillYaml(entryFile, skillId, yaml);
                    if (categoryFromSkillYaml != null && !categoryFromSkillYaml.isEmpty()) {
                        category = categoryFromSkillYaml;
                        log.info("[getSkillsFromEntryFiles] Read category '{}' from skill.yaml for skill {}", category, skillId);
                    }
                }
                
                cap.setCategory(category);
                
                String subCategory = (String) spec.get("subCategory");
                cap.setSubCategory(subCategory);
                
                Object tagsObj = spec.get("tags");
                if (tagsObj instanceof List) {
                    cap.setTags((List<String>) tagsObj);
                }
                
                Object dependenciesObj = spec.get("dependencies");
                if (dependenciesObj instanceof List) {
                    cap.setDependencies((List<String>) dependenciesObj);
                }
                
                boolean isScene = "SCENE".equals(skillForm);
                cap.setType(isScene ? "SCENE" : "SKILL");
                cap.setSceneCapability(isScene);
                
                capabilities.add(cap);
                
            } catch (Exception e) {
                log.warn("[getSkillsFromEntryFiles] Failed to load {}: {}", entryFile.getAbsolutePath(), e.getMessage());
            }
        }
        
        return capabilities;
    }
    
    private List<File> findSkillIndexEntryFiles() {
        List<File> files = new ArrayList<>();
        
        File indexDir = findSkillIndexDir();
        if (indexDir == null) {
            log.warn("[findSkillIndexEntryFiles] Skill index directory not configured or not found");
            return files;
        }
        
        File skillsDir = new File(indexDir, "skills");
        if (skillsDir.exists() && skillsDir.isDirectory()) {
            findFilesInAllowedDir(skillsDir, "skill-index-entry.yaml", files);
            log.info("[findSkillIndexEntryFiles] Found {} files in {}", files.size(), skillsDir.getAbsolutePath());
        }
        
        return files;
    }
    
    private void findFilesInAllowedDir(File dir, String fileName, List<File> result) {
        if (!dir.exists() || !dir.isDirectory()) return;
        
        File[] children = dir.listFiles();
        if (children == null) return;
        
        for (File child : children) {
            if (child.isDirectory()) {
                findFilesInAllowedDir(child, fileName, result);
            } else if (fileName.equals(child.getName())) {
                result.add(child);
            }
        }
    }
    
    private void findFilesRecursive(File dir, String fileName, List<File> result) {
        File[] children = dir.listFiles();
        if (children == null) return;
        
        for (File child : children) {
            if (child.isDirectory()) {
                findFilesRecursive(child, fileName, result);
            } else if (fileName.equals(child.getName())) {
                result.add(child);
            }
        }
    }
    
    private boolean checkIfInstalled(String skillId) {
        if (skillId == null) {
            return false;
        }
        
        if (mockEnabled && mockInstalledSkills.contains(skillId)) {
            return true;
        }
        
        if (locallyInstalledSkills.contains(skillId)) {
            return true;
        }
        
        if (System.currentTimeMillis() - lastRegistryCheck > REGISTRY_CHECK_INTERVAL) {
            loadLocalRegistry();
            lastRegistryCheck = System.currentTimeMillis();
            if (locallyInstalledSkills.contains(skillId)) {
                return true;
            }
        }
        
        if (skillPackageManager == null) {
            return false;
        }
        try {
            return skillPackageManager.isInstalled(skillId).get();
        } catch (Exception e) {
            log.debug("[checkIfInstalled] Failed to check installation status for {}: {}", skillId, e.getMessage());
            return false;
        }
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
        result.put("sceneDrivers", sceneDrivers);
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

    public List<Map<String, Object>> getSceneDrivers() {
        return sceneDrivers;
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
            
            Object caps = scene.get("capabilities");
            if (caps instanceof List) {
                cap.setCapabilities((List<String>) caps);
            }
            
            Object sceneTypeObj = scene.get("sceneType");
            String sceneTypeCode = sceneTypeObj != null ? String.valueOf(sceneTypeObj) : "MANUAL";
            
            Object skillFormObj = scene.get("skillForm");
            String skillFormCode = skillFormObj != null ? String.valueOf(skillFormObj) : "STANDALONE";
            
            cap.setSceneType(sceneTypeCode);
            cap.setSkillForm(skillFormCode);
            cap.setMainFirst("AUTO".equals(sceneTypeCode));
            
            Object visibilityObj = scene.get("visibility");
            cap.setVisibility(visibilityObj != null ? String.valueOf(visibilityObj) : "public");
            
            Object businessCategoryObj = scene.get("businessCategory");
            if (businessCategoryObj == null) {
                businessCategoryObj = scene.get("capabilityCategory");
            }
            if (businessCategoryObj == null) {
                businessCategoryObj = scene.get("category");
            }
            if (businessCategoryObj != null) {
                cap.setBusinessCategory(String.valueOf(businessCategoryObj));
            }
            
            Object driverConditionsObj = scene.get("driverConditions");
            if (driverConditionsObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> driverConditions = (List<Map<String, Object>>) driverConditionsObj;
                cap.setDriverConditions(convertToMapList(driverConditions));
            }
            
            Object participantsObj = scene.get("participants");
            if (participantsObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> participants = (List<Map<String, Object>>) participantsObj;
                cap.setParticipants(convertToMapList(participants));
            }
            
            capabilities.add(cap);
        }
        
        return capabilities;
    }

    public List<CapabilityDTO> getAllCapabilities(String source) {
        List<CapabilityDTO> all = new ArrayList<>();
        all.addAll(getSkillsFromIndex(source));
        all.addAll(getScenesFromIndex(source));
        return all;
    }
    
    public List<CapabilityDTO> getWorkspaceCapabilities(String source) {
        List<CapabilityDTO> capabilities = new ArrayList<>();
        Map<String, CapabilityDTO> skillMap = new LinkedHashMap<>();
        
        List<CapabilityDTO> registrySkills = scanRegistryInstalledSkills(source);
        for (CapabilityDTO cap : registrySkills) {
            if (cap.getSkillId() != null) {
                skillMap.put(cap.getSkillId(), cap);
            }
        }
        
        List<CapabilityDTO> downloaded = scanDirectoryForSkills(downloadsDir, "downloaded", source);
        for (CapabilityDTO cap : downloaded) {
            if (cap.getSkillId() != null && !skillMap.containsKey(cap.getSkillId())) {
                skillMap.put(cap.getSkillId(), cap);
            }
        }
        
        List<CapabilityDTO> installed = scanDirectoryForSkills(installedDir, "installed", source);
        for (CapabilityDTO cap : installed) {
            if (cap.getSkillId() != null && !skillMap.containsKey(cap.getSkillId())) {
                skillMap.put(cap.getSkillId(), cap);
            }
        }
        
        List<CapabilityDTO> activated = scanDirectoryForSkills(activatedDir, "activated", source);
        for (CapabilityDTO cap : activated) {
            if (cap.getSkillId() != null && !skillMap.containsKey(cap.getSkillId())) {
                skillMap.put(cap.getSkillId(), cap);
            }
        }
        
        List<CapabilityDTO> dev = scanDirectoryForSkills(devDir, "developing", source);
        for (CapabilityDTO cap : dev) {
            if (cap.getSkillId() != null && !skillMap.containsKey(cap.getSkillId())) {
                skillMap.put(cap.getSkillId(), cap);
            }
        }
        
        capabilities.addAll(skillMap.values());
        
        log.info("[getWorkspaceCapabilities] Total {} unique skills found", capabilities.size());
        
        syncToCapabilityRegistry(capabilities);
        
        updateCapabilityStates(capabilities);
        
        return capabilities;
    }
    
    private void updateCapabilityStates(List<CapabilityDTO> capabilities) {
        if (capabilityStateService == null) {
            log.debug("[updateCapabilityStates] CapabilityStateService not available, skipping state update");
            return;
        }
        
        for (CapabilityDTO cap : capabilities) {
            try {
                net.ooder.mvp.skill.scene.capability.model.CapabilityStatus status = 
                    capabilityStateService.getStatus(cap.getSkillId());
                if (status != null) {
                    cap.setStatus(status.getCode().toLowerCase());
                    cap.setEnabled(status == net.ooder.mvp.skill.scene.capability.model.CapabilityStatus.RUNNING 
                        || status == net.ooder.mvp.skill.scene.capability.model.CapabilityStatus.SCHEDULED
                        || status == net.ooder.mvp.skill.scene.capability.model.CapabilityStatus.PENDING
                        || status == net.ooder.mvp.skill.scene.capability.model.CapabilityStatus.WAITING);
                    log.debug("[updateCapabilityStates] Updated {} status={}, enabled={}", 
                        cap.getSkillId(), status.getCode(), cap.getEnabled());
                }
            } catch (Exception e) {
                log.debug("[updateCapabilityStates] Failed to get status for {}: {}", cap.getSkillId(), e.getMessage());
            }
        }
    }
    
    private void syncToCapabilityRegistry(List<CapabilityDTO> capabilities) {
        if (capabilityService == null) {
            log.debug("[syncToCapabilityRegistry] CapabilityService not available, skipping sync");
            return;
        }
        
        for (CapabilityDTO dto : capabilities) {
            try {
                net.ooder.mvp.skill.scene.capability.model.Capability existing = capabilityService.findById(dto.getSkillId());
                if (existing == null) {
                    net.ooder.mvp.skill.scene.capability.model.Capability cap = new net.ooder.mvp.skill.scene.capability.model.Capability();
                    cap.setCapabilityId(dto.getSkillId());
                    cap.setName(dto.getName());
                    cap.setDescription(dto.getDescription());
                    cap.setVersion(dto.getVersion());
                    cap.setSkillId(dto.getSkillId());
                    cap.setInstalled(dto.isInstalled());
                    cap.setCreateTime(System.currentTimeMillis());
                    cap.setUpdateTime(System.currentTimeMillis());
                    
                    if (dto.getCategory() != null) {
                        cap.setSceneType(dto.getCategory());
                    }
                    
                    if (dto.getSkillForm() != null) {
                        cap.setSkillForm(dto.getSkillForm());
                        if ("SCENE".equals(dto.getSkillForm())) {
                            cap.setCapabilityType(net.ooder.mvp.skill.scene.capability.model.CapabilityType.SCENE);
                        }
                    }
                    
                    capabilityService.register(cap);
                    log.info("[syncToCapabilityRegistry] Registered capability: {} with skillForm={}", dto.getSkillId(), dto.getSkillForm());
                } else {
                    if (dto.getSkillForm() != null && existing.getSkillForm() == null) {
                        existing.setSkillForm(dto.getSkillForm());
                        if ("SCENE".equals(dto.getSkillForm())) {
                            existing.setCapabilityType(net.ooder.mvp.skill.scene.capability.model.CapabilityType.SCENE);
                        }
                    }
                    existing.setUpdateTime(System.currentTimeMillis());
                    capabilityService.register(existing);
                    log.debug("[syncToCapabilityRegistry] Updated capability: {} with skillForm={}", dto.getSkillId(), dto.getSkillForm());
                }
            } catch (Exception e) {
                log.warn("[syncToCapabilityRegistry] Failed to sync capability {}: {}", dto.getSkillId(), e.getMessage());
            }
        }
    }
    
    private List<CapabilityDTO> scanRegistryInstalledSkills(String source) {
        List<CapabilityDTO> capabilities = new ArrayList<>();
        
        String[] registryPaths = {
            "./data/installed-skills/registry.properties",
            "./.ooder/installed/registry.properties",
            "./.ooder/registry.properties"
        };
        
        for (String registryPath : registryPaths) {
            File registryFile = new File(registryPath);
            log.info("[scanRegistryInstalledSkills] Checking registry file: {} exists={}", registryPath, registryFile.exists());
            if (registryFile.exists()) {
                try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(registryFile))) {
                    Map<String, String> properties = new HashMap<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("#") || line.trim().isEmpty()) continue;
                        int eqIdx = line.indexOf('=');
                        if (eqIdx > 0) {
                            String key = line.substring(0, eqIdx);
                            String value = line.substring(eqIdx + 1);
                            properties.put(key, value);
                            log.debug("[scanRegistryInstalledSkills] Loaded property: {} = {}", key, value);
                        }
                    }
                    
                    log.info("[scanRegistryInstalledSkills] Loaded {} properties from {}", properties.size(), registryPath);
                    
                    for (String key : properties.keySet()) {
                        if (key.endsWith(".id")) {
                            String skillId = properties.get(key);
                            String pathKey = skillId + ".path";
                            String skillPath = properties.get(pathKey);
                            
                            log.info("[scanRegistryInstalledSkills] Found skillId={}, pathKey={}, skillPath={}", skillId, pathKey, skillPath);
                            
                            if (skillPath != null && !skillPath.isEmpty()) {
                                String normalizedPath = skillPath.replace("\\\\", "\\").replace("\\:", ":");
                                File skillDir = new File(normalizedPath);
                                log.info("[scanRegistryInstalledSkills] Checking skill directory: {} (normalized: {}) exists={}", skillPath, normalizedPath, skillDir.exists());
                                if (skillDir.exists() && skillDir.isDirectory()) {
                                    CapabilityDTO cap = scanSingleSkillDirectory(skillDir, "installed", source);
                                    if (cap != null) {
                                        capabilities.add(cap);
                                        log.info("[scanRegistryInstalledSkills] Found installed skill: {} at {}", skillId, normalizedPath);
                                    }
                                }
                            }
                        }
                    }
                    
                    if (!capabilities.isEmpty()) {
                        log.info("[scanRegistryInstalledSkills] Found {} installed skills from {}", capabilities.size(), registryPath);
                    }
                    break;
                } catch (Exception e) {
                    log.warn("[scanRegistryInstalledSkills] Failed to read registry from {}: {}", registryPath, e.getMessage());
                }
            }
        }
        
        return capabilities;
    }
    
    private CapabilityDTO scanSingleSkillDirectory(File skillDir, String status, String source) {
        Yaml yaml = new Yaml();
        
        try {
            File skillYaml = new File(skillDir, "src/main/resources/skill.yaml");
            if (!skillYaml.exists()) {
                skillYaml = new File(skillDir, "skill.yaml");
            }
            if (!skillYaml.exists()) {
                skillYaml = new File(skillDir, "skill.yml");
            }
            
            if (skillYaml.exists()) {
                try (InputStream is = new FileInputStream(skillYaml)) {
                    Map<String, Object> skillData = yaml.load(is);
                    if (skillData != null) {
                        return convertSkillYamlToDTO(skillData, skillDir.getName(), status, source);
                    }
                }
            } else {
                CapabilityDTO cap = new CapabilityDTO();
                cap.setId(skillDir.getName());
                cap.setSkillId(skillDir.getName());
                cap.setName(skillDir.getName());
                cap.setStatus(status);
                cap.setInstalled("installed".equals(status) || "activated".equals(status));
                cap.setSource(source);
                return cap;
            }
        } catch (Exception e) {
            log.warn("[scanSingleSkillDirectory] Failed to read skill from {}: {}", skillDir.getName(), e.getMessage());
        }
        
        return null;
    }
    
    private List<CapabilityDTO> scanDirectoryForSkills(String dirPath, String status, String source) {
        List<CapabilityDTO> capabilities = new ArrayList<>();
        
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            log.debug("[scanDirectoryForSkills] Directory not found: {}", dirPath);
            return capabilities;
        }
        
        File[] skillDirs = dir.listFiles(File::isDirectory);
        if (skillDirs == null) return capabilities;
        
        Yaml yaml = new Yaml();
        
        for (File skillDir : skillDirs) {
            try {
                File skillYaml = new File(skillDir, "src/main/resources/skill.yaml");
                if (!skillYaml.exists()) {
                    skillYaml = new File(skillDir, "skill.yaml");
                }
                if (!skillYaml.exists()) {
                    skillYaml = new File(skillDir, "skill.yml");
                }
                
                if (skillYaml.exists()) {
                    try (InputStream is = new FileInputStream(skillYaml)) {
                        Map<String, Object> skillData = yaml.load(is);
                        if (skillData != null) {
                            CapabilityDTO cap = convertSkillYamlToDTO(skillData, skillDir.getName(), status, source);
                            if (cap != null) {
                                capabilities.add(cap);
                            }
                        }
                    }
                } else {
                    CapabilityDTO cap = new CapabilityDTO();
                    cap.setId(skillDir.getName());
                    cap.setSkillId(skillDir.getName());
                    cap.setName(skillDir.getName());
                    cap.setStatus(status);
                    cap.setInstalled("installed".equals(status) || "activated".equals(status));
                    cap.setSource(source);
                    capabilities.add(cap);
                }
            } catch (Exception e) {
                log.warn("[scanDirectoryForSkills] Failed to read skill from {}: {}", skillDir.getName(), e.getMessage());
            }
        }
        
        log.info("[scanDirectoryForSkills] Found {} skills in {} with status {}", capabilities.size(), dirPath, status);
        return capabilities;
    }
    
    @SuppressWarnings("unchecked")
    private CapabilityDTO convertSkillYamlToDTO(Map<String, Object> skillData, String dirName, String status, String source) {
        if (skillData == null) return null;
        
        CapabilityDTO cap = new CapabilityDTO();
        
        Map<String, Object> metadata = (Map<String, Object>) skillData.get("metadata");
        Map<String, Object> spec = (Map<String, Object>) skillData.get("spec");
        
        String skillId = null;
        String name = null;
        String description = null;
        String version = null;
        String category = null;
        
        if (metadata != null) {
            skillId = (String) metadata.get("id");
            name = (String) metadata.get("name");
            version = (String) metadata.get("version");
        }
        
        String topLevelCategory = (String) skillData.get("category");
        if (topLevelCategory != null) {
            category = topLevelCategory;
        }
        
        String topLevelName = (String) skillData.get("name");
        if (topLevelName != null && name == null) {
            name = topLevelName;
        }
        
        String topLevelDesc = (String) skillData.get("description");
        if (topLevelDesc != null && description == null) {
            description = topLevelDesc;
        }
        
        String topLevelVersion = (String) skillData.get("version");
        if (topLevelVersion != null && version == null) {
            version = topLevelVersion;
        }
        
        if (spec != null) {
            if (name == null) name = (String) spec.get("name");
            description = (String) spec.get("description");
            if (version == null) version = (String) spec.get("version");
            
            String type = (String) spec.get("type");
            if (type != null) {
                category = type.replace("-skill", "").replace("scene-", "business");
                
                if ("scene".equals(type) || "scene-skill".equals(type)) {
                    cap.setSkillForm("SCENE");
                    cap.setCapabilityType("SCENE");
                } else {
                    cap.setSkillForm("PROVIDER");
                    cap.setCapabilityType("PROVIDER");
                }
            }
            
            List<Map<String, Object>> capabilities = (List<Map<String, Object>>) spec.get("capabilities");
            if (capabilities != null && !capabilities.isEmpty()) {
                String capCategory = (String) capabilities.get(0).get("category");
                if (capCategory != null) {
                    category = capCategory;
                }
            }
            
            List<Map<String, Object>> scenes = (List<Map<String, Object>>) spec.get("scenes");
            if (scenes != null && !scenes.isEmpty()) {
                cap.setCapabilityType("SCENE");
                cap.setSkillForm("SCENE");
            }
        }
        
        if (cap.getSkillForm() == null || cap.getSkillForm().isEmpty()) {
            List<Map<String, Object>> menu = (List<Map<String, Object>>) skillData.get("menu");
            if (menu != null && !menu.isEmpty()) {
                cap.setSkillForm("SCENE");
                cap.setCapabilityType("SCENE");
            } else {
                cap.setSkillForm("PROVIDER");
                cap.setCapabilityType("PROVIDER");
            }
        }
        
        if (skillId == null) skillId = dirName;
        if (name == null) name = dirName;
        if (category == null) category = "util";
        
        cap.setId(skillId);
        cap.setSkillId(skillId);
        cap.setName(name);
        cap.setDescription(description);
        cap.setVersion(version);
        cap.setCategory(category);
        cap.setStatus(status);
        cap.setInstalled("installed".equals(status) || "activated".equals(status));
        cap.setEnabled("activated".equals(status));
        cap.setSource(source);
        
        return cap;
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
    
    public String getRepoUrl(String skillId) {
        Map<String, Object> skill = getSkillInfo(skillId);
        if (skill != null) {
            String giteeRepo = (String) skill.get("giteeRepo");
            String githubRepo = (String) skill.get("githubRepo");
            if (giteeRepo != null && !giteeRepo.isEmpty()) {
                return "https://gitee.com/" + giteeRepo + ".git";
            }
            if (githubRepo != null && !githubRepo.isEmpty()) {
                return "https://github.com/" + githubRepo + ".git";
            }
        }
        return "https://gitee.com/ooderCN/skills.git";
    }
    
    /**
     * 从实际 skill.yaml 文件读取 spec.capability.category
     * 
     * @param entryFile entry 文件
     * @param skillId skill ID
     * @param yaml Yaml 解析器
     * @return category 值，如果未找到则返回 null
     */
    private String readCategoryFromSkillYaml(File entryFile, String skillId, Yaml yaml) {
        try {
            // 从 entry 文件路径推断 skill.yaml 路径
            // entry 文件路径示例: .../skills/capabilities/auth/skill-user-auth/skill-index-entry.yaml
            // skill.yaml 路径示例: .../skills/capabilities/auth/skill-user-auth/src/main/resources/skill.yaml
            String entryPath = entryFile.getAbsolutePath();
            File skillDir = entryFile.getParentFile();
            
            // 尝试多个可能的 skill.yaml 路径
            String[] possiblePaths = {
                skillDir.getAbsolutePath() + "/src/main/resources/skill.yaml",
                skillDir.getAbsolutePath() + "/skill.yaml",
                skillDir.getParentFile().getAbsolutePath() + "/src/main/resources/skill.yaml",
                skillDir.getParentFile().getAbsolutePath() + "/skill.yaml"
            };
            
            for (String skillYamlPath : possiblePaths) {
                File skillYamlFile = new File(skillYamlPath);
                if (skillYamlFile.exists()) {
                    try (InputStream skillIs = new FileInputStream(skillYamlFile)) {
                        Map<String, Object> skillData = yaml.load(skillIs);
                        if (skillData != null) {
                            Map<String, Object> spec = (Map<String, Object>) skillData.get("spec");
                            if (spec != null) {
                                // 首先尝试从 spec.capability.category 读取
                                Map<String, Object> capability = (Map<String, Object>) spec.get("capability");
                                if (capability != null) {
                                    String category = (String) capability.get("category");
                                    if (category != null && !category.isEmpty()) {
                                        log.debug("[readCategoryFromSkillYaml] Found category '{}' in spec.capability.category for {}", category, skillId);
                                        return category;
                                    }
                                }
                                
                                // 然后尝试从 spec.category 读取（废弃字段，但可能还存在）
                                String category = (String) spec.get("category");
                                if (category != null && !category.isEmpty()) {
                                    log.debug("[readCategoryFromSkillYaml] Found category '{}' in spec.category (deprecated) for {}", category, skillId);
                                    return category;
                                }
                            }
                        }
                    }
                }
            }
            
            log.debug("[readCategoryFromSkillYaml] Category not found in skill.yaml for {}", skillId);
            return null;
            
        } catch (Exception e) {
            log.debug("[readCategoryFromSkillYaml] Error reading category from skill.yaml for {}: {}", skillId, e.getMessage());
            return null;
        }
    }
    
    public Map<String, Long> getSkillCountByCategory() {
        Map<String, Long> counts = new HashMap<>();
        
        for (CapabilityCategory cat : CapabilityCategory.values()) {
            counts.put(cat.getCode(), 0L);
        }
        
        for (Map<String, Object> skill : skills) {
            String category = (String) skill.get("category");
            if (category == null) {
                category = (String) skill.get("capabilityCategory");
            }
            if (category == null) {
                category = "sys";
            }
            CapabilityCategory cat = CapabilityCategory.fromCode(category);
            counts.put(cat.getCode(), counts.getOrDefault(cat.getCode(), 0L) + 1);
        }
        
        for (Map<String, Object> scene : scenes) {
            String category = (String) scene.get("category");
            if (category == null) {
                category = (String) scene.get("capabilityCategory");
            }
            if (category == null) {
                category = "biz";
            }
            CapabilityCategory cat = CapabilityCategory.fromCode(category);
            counts.put(cat.getCode(), counts.getOrDefault(cat.getCode(), 0L) + 1);
        }
        
        return counts;
    }
    
    public List<Map<String, Object>> getUserFacingCategories() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (CapabilityCategory cat : CapabilityCategory.values()) {
            if (cat.isUserFacing()) {
                Map<String, Object> catMap = new LinkedHashMap<>();
                catMap.put("id", cat.getCode());
                catMap.put("name", cat.getName());
                catMap.put("icon", cat.getIcon());
                catMap.put("description", cat.getDescription());
                catMap.put("userFacing", true);
                result.add(catMap);
            }
        }
        
        return result;
    }
    
    public List<Map<String, Object>> getAllCategoriesWithStats() {
        Map<String, Long> counts = getSkillCountByCategory();
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (CapabilityCategory cat : CapabilityCategory.values()) {
            Map<String, Object> catMap = new LinkedHashMap<>();
            catMap.put("id", cat.getCode());
            catMap.put("name", cat.getName());
            catMap.put("icon", cat.getIcon());
            catMap.put("description", cat.getDescription());
            catMap.put("userFacing", cat.isUserFacing());
            catMap.put("count", counts.getOrDefault(cat.getCode(), 0L));
            result.add(catMap);
        }
        
        return result;
    }
    
    public List<Map<String, Object>> getSubCategories(String categoryId) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        if ("biz".equals(categoryId)) {
            for (Map.Entry<String, String> entry : CapabilityCategory.BIZ_SUBCATEGORY_MAPPING.entrySet()) {
                Map<String, Object> subMap = new LinkedHashMap<>();
                subMap.put("id", entry.getKey());
                subMap.put("name", entry.getValue());
                subMap.put("count", countBySubCategory(entry.getKey()));
                result.add(subMap);
            }
        }
        
        return result;
    }
    
    private long countBySubCategory(String subCategory) {
        long count = 0;
        
        for (Map<String, Object> skill : skills) {
            String sub = (String) skill.get("subCategory");
            if (subCategory.equals(sub)) {
                count++;
            }
        }
        
        for (Map<String, Object> scene : scenes) {
            String sub = (String) scene.get("subCategory");
            if (subCategory.equals(sub)) {
                count++;
            }
        }
        
        return count;
    }
    
    public Map<String, Object> getCategoryStats() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", skills.size() + scenes.size());
        result.put("categories", getSkillCountByCategory());
        result.put("userFacingCategories", getUserFacingCategories());
        result.put("allCategories", getAllCategoriesWithStats());
        result.put("installStats", getInstallStats());
        result.put("skillFormStats", getSkillFormStats());
        result.put("ownershipStats", getOwnershipStats());
        return result;
    }
    
    private Map<String, Object> getInstallStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        
        long installedCount = 0;
        long notInstalledCount = 0;
        long enabledCount = 0;
        long disabledCount = 0;
        
        for (Map<String, Object> skill : skills) {
            Boolean installed = (Boolean) skill.get("installed");
            Boolean enabled = (Boolean) skill.get("enabled");
            
            if (Boolean.TRUE.equals(installed)) {
                installedCount++;
                if (Boolean.TRUE.equals(enabled)) {
                    enabledCount++;
                } else {
                    disabledCount++;
                }
            } else {
                notInstalledCount++;
            }
        }
        
        for (Map<String, Object> scene : scenes) {
            Boolean installed = (Boolean) scene.get("installed");
            Boolean enabled = (Boolean) scene.get("enabled");
            
            if (Boolean.TRUE.equals(installed)) {
                installedCount++;
                if (Boolean.TRUE.equals(enabled)) {
                    enabledCount++;
                } else {
                    disabledCount++;
                }
            } else {
                notInstalledCount++;
            }
        }
        
        stats.put("installed", installedCount);
        stats.put("notInstalled", notInstalledCount);
        stats.put("enabled", enabledCount);
        stats.put("disabled", disabledCount);
        
        return stats;
    }
    
    private Map<String, Long> getSkillFormStats() {
        Map<String, Long> stats = new LinkedHashMap<>();
        
        for (Map<String, Object> skill : skills) {
            String skillForm = (String) skill.get("skillForm");
            if (skillForm == null) {
                skillForm = (String) skill.get("config.skillForm");
            }
            if (skillForm == null) {
                skillForm = "PROVIDER";
            }
            stats.merge(skillForm, 1L, Long::sum);
        }
        
        for (Map<String, Object> scene : scenes) {
            String skillForm = (String) scene.get("skillForm");
            if (skillForm == null) {
                skillForm = (String) scene.get("config.skillForm");
            }
            if (skillForm == null) {
                skillForm = "SCENE";
            }
            stats.merge(skillForm, 1L, Long::sum);
        }
        
        return stats;
    }
    
    private Map<String, Long> getOwnershipStats() {
        Map<String, Long> stats = new LinkedHashMap<>();
        
        for (Map<String, Object> skill : skills) {
            String ownership = (String) skill.get("ownership");
            if (ownership == null) {
                ownership = "PLATFORM";
            }
            stats.merge(ownership, 1L, Long::sum);
        }
        
        for (Map<String, Object> scene : scenes) {
            String ownership = (String) scene.get("ownership");
            if (ownership == null) {
                ownership = "PLATFORM";
            }
            stats.merge(ownership, 1L, Long::sum);
        }
        
        return stats;
    }
}
