package net.ooder.enexus.skill.registry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.Data;

@Service
public class SkillRegistry {
    
    private static final Logger log = LoggerFactory.getLogger(SkillRegistry.class);
    
    private final Map<String, SkillInfo> skills = new ConcurrentHashMap<>();
    
    private final Map<String, SkillInfo> systemSkills = new ConcurrentHashMap<>();
    
    public void registerSystemSkill(SkillInfo skill) {
        if (skill == null || skill.getSkillId() == null) {
            return;
        }
        skill.setInstalled(true);
        skill.setSource("local");
        skill.setSystem(true);
        systemSkills.put(skill.getSkillId(), skill);
        skills.put(skill.getSkillId(), skill);
        log.info("[SkillRegistry] Registered system skill: {}", skill.getSkillId());
    }
    
    public void registerSkill(SkillInfo skill) {
        if (skill == null || skill.getSkillId() == null) {
            return;
        }
        skill.setInstalled(true);
        skills.put(skill.getSkillId(), skill);
        log.info("[SkillRegistry] Registered skill: {}", skill.getSkillId());
    }
    
    public void unregisterSkill(String skillId) {
        if (systemSkills.containsKey(skillId)) {
            log.warn("[SkillRegistry] Cannot unregister system skill: {}", skillId);
            return;
        }
        SkillInfo removed = skills.remove(skillId);
        if (removed != null) {
            log.info("[SkillRegistry] Unregistered skill: {}", skillId);
        }
    }
    
    public SkillInfo getSkill(String skillId) {
        return skills.get(skillId);
    }
    
    public List<SkillInfo> getAllSkills() {
        return new ArrayList<>(skills.values());
    }
    
    public List<SkillInfo> getSystemSkills() {
        return new ArrayList<>(systemSkills.values());
    }
    
    public List<SkillInfo> getInstalledSkills() {
        List<SkillInfo> result = new ArrayList<>();
        for (SkillInfo skill : skills.values()) {
            if (skill.isInstalled()) {
                result.add(skill);
            }
        }
        return result;
    }
    
    public boolean isInstalled(String skillId) {
        SkillInfo skill = skills.get(skillId);
        return skill != null && skill.isInstalled();
    }
    
    public boolean isSystemSkill(String skillId) {
        return systemSkills.containsKey(skillId);
    }
    
    public int getSkillCount() {
        return skills.size();
    }
    
    public int getSystemSkillCount() {
        return systemSkills.size();
    }
    
    @Data
    public static class SkillInfo {
        private String skillId;
        private String name;
        private String version;
        private String description;
        private String category;
        private String skillForm;
        private boolean installed;
        private String source;
        private boolean system;
        private Map<String, Object> metadata;
        private List<String> capabilities;
        private List<String> dependencies;
        private String yamlPath;
        
        public static SkillInfo fromYaml(Map<String, Object> yaml, String yamlPath) {
            SkillInfo info = new SkillInfo();
            info.setYamlPath(yamlPath);
            
            Map<String, Object> metadata = (Map<String, Object>) yaml.get("metadata");
            if (metadata != null) {
                info.setSkillId((String) metadata.get("id"));
                info.setName((String) metadata.get("name"));
                info.setVersion((String) metadata.get("version"));
                info.setDescription((String) metadata.get("description"));
                info.setCategory((String) metadata.get("category"));
            }
            
            Map<String, Object> spec = (Map<String, Object>) yaml.get("spec");
            if (spec != null) {
                info.setSkillForm((String) spec.get("skillForm"));
                
                List<Map<String, Object>> caps = (List<Map<String, Object>>) spec.get("capabilities");
                if (caps != null) {
                    List<String> capIds = new ArrayList<>();
                    for (Map<String, Object> cap : caps) {
                        String capId = (String) cap.get("id");
                        if (capId != null) {
                            capIds.add(capId);
                        }
                    }
                    info.setCapabilities(capIds);
                }
                
                Object depsObj = spec.get("dependencies");
                if (depsObj != null) {
                    List<String> depIds = new ArrayList<>();
                    if (depsObj instanceof List) {
                        List<?> depsList = (List<?>) depsObj;
                        for (Object dep : depsList) {
                            if (dep instanceof String) {
                                // 简单字符串格式: - skill-common
                                depIds.add((String) dep);
                            } else if (dep instanceof Map) {
                                // Map 格式: - skillId: skill-common
                                Map<?, ?> depMap = (Map<?, ?>) dep;
                                String depId = (String) depMap.get("skillId");
                                if (depId != null) {
                                    depIds.add(depId);
                                }
                            }
                        }
                    }
                    info.setDependencies(depIds);
                }
            }
            
            info.setMetadata(yaml);
            return info;
        }
    }
}
