package net.ooder.nexus.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import net.ooder.nexus.service.SkillDiscoveryService;
import net.ooder.sdk.api.skill.SkillPackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SkillDiscoveryServiceImpl implements SkillDiscoveryService {

    private static final Logger log = LoggerFactory.getLogger(SkillDiscoveryServiceImpl.class);

    @Value("${skill.discovery.path:classpath:skills}")
    private String skillsPath;

    @Value("${skill.discovery.external-path:./data/skills}")
    private String externalSkillsPath;

    private final Map<String, SkillPackage> skillsCache = new ConcurrentHashMap<String, SkillPackage>();
    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    @PostConstruct
    public void init() {
        loadBuiltinSkills();
        loadExternalSkills();
    }

    private void loadBuiltinSkills() {
        try {
            Path skillsDir = Paths.get("src/main/resources/skills");
            if (Files.exists(skillsDir)) {
                loadSkillsFromDirectory(skillsDir.toFile());
            }
        } catch (Exception e) {
            log.warn("Failed to load builtin skills: {}", e.getMessage());
        }
    }

    private void loadExternalSkills() {
        try {
            File externalDir = new File(externalSkillsPath);
            if (externalDir.exists()) {
                loadSkillsFromDirectory(externalDir);
            }
        } catch (Exception e) {
            log.warn("Failed to load external skills: {}", e.getMessage());
        }
    }

    private void loadSkillsFromDirectory(File directory) {
        File[] skillDirs = directory.listFiles(File::isDirectory);
        if (skillDirs == null) {
            return;
        }

        for (File skillDir : skillDirs) {
            File manifestFile = new File(skillDir, "skill.yaml");
            if (manifestFile.exists()) {
                try {
                    SkillPackage skill = loadSkillManifest(manifestFile);
                    if (skill != null) {
                        skillsCache.put(skill.getSkillId(), skill);
                        log.info("Loaded skill: {} ({})", skill.getName(), skill.getSkillId());
                    }
                } catch (Exception e) {
                    log.warn("Failed to load skill manifest: {} - {}", manifestFile.getPath(), e.getMessage());
                }
            }
        }
    }

    private SkillPackage loadSkillManifest(File file) throws IOException {
        String content = new String(Files.readAllBytes(file.toPath()));
        return parseSkillManifest(content);
    }

    private SkillPackage parseSkillManifest(String yaml) {
        try {
            Map<String, Object> manifest = yamlMapper.readValue(yaml, Map.class);

            @SuppressWarnings("unchecked")
            Map<String, Object> metadata = (Map<String, Object>) manifest.get("metadata");
            @SuppressWarnings("unchecked")
            Map<String, Object> spec = (Map<String, Object>) manifest.get("spec");

            if (metadata == null || spec == null) {
                return null;
            }

            SkillPackage skill = new SkillPackage();
            skill.setSkillId((String) metadata.get("id"));
            skill.setName((String) metadata.get("name"));
            skill.setVersion((String) metadata.get("version"));
            skill.setDescription((String) metadata.get("description"));
            skill.setSceneId((String) spec.get("sceneId"));

            return skill;
        } catch (Exception e) {
            log.warn("Failed to parse skill manifest: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<SkillPackage> getAllSkills() {
        return new ArrayList<SkillPackage>(skillsCache.values());
    }

    @Override
    public SkillPackage getSkillById(String skillId) {
        return skillsCache.get(skillId);
    }

    @Override
    public List<SkillPackage> searchSkills(String keyword, String scene, String capability, String type) {
        return skillsCache.values().stream()
                .filter(skill -> matchesKeyword(skill, keyword))
                .filter(skill -> matchesScene(skill, scene))
                .filter(skill -> matchesCapability(skill, capability))
                .filter(skill -> matchesType(skill, type))
                .collect(Collectors.toList());
    }

    private boolean matchesKeyword(SkillPackage skill, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return true;
        }
        String lowerKeyword = keyword.toLowerCase();
        if (skill.getName() != null && skill.getName().toLowerCase().contains(lowerKeyword)) {
            return true;
        }
        if (skill.getDescription() != null && skill.getDescription().toLowerCase().contains(lowerKeyword)) {
            return true;
        }
        return false;
    }

    private boolean matchesScene(SkillPackage skill, String scene) {
        if (scene == null || scene.isEmpty()) {
            return true;
        }
        if (skill.getSceneId() == null) {
            return false;
        }
        return scene.equals(skill.getSceneId());
    }

    private boolean matchesCapability(SkillPackage skill, String capability) {
        if (capability == null || capability.isEmpty()) {
            return true;
        }
        return true;
    }

    private boolean matchesType(SkillPackage skill, String type) {
        if (type == null || type.isEmpty()) {
            return true;
        }
        return true;
    }

    @Override
    public Map<String, Object> getSkillDetail(String skillId) {
        SkillPackage skill = skillsCache.get(skillId);
        if (skill == null) {
            return null;
        }

        Map<String, Object> detail = new HashMap<String, Object>();
        detail.put("skillId", skill.getSkillId());
        detail.put("name", skill.getName());
        detail.put("version", skill.getVersion());
        detail.put("description", skill.getDescription());
        detail.put("sceneId", skill.getSceneId());
        detail.put("source", skill.getSource());

        List<Map<String, Object>> capabilities = new ArrayList<Map<String, Object>>();
        detail.put("capabilities", capabilities);

        List<Map<String, Object>> scenes = new ArrayList<Map<String, Object>>();
        detail.put("scenes", scenes);

        return detail;
    }

    @Override
    public void refreshSkills() {
        skillsCache.clear();
        loadBuiltinSkills();
        loadExternalSkills();
        log.info("Skills cache refreshed, total skills: {}", skillsCache.size());
    }
}
