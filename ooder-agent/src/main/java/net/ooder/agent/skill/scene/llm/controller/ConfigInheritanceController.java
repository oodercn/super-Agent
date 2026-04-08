package net.ooder.mvp.skill.scene.llm.controller;

import net.ooder.mvp.skill.scene.config.sdk.ConfigNode;
import net.ooder.mvp.skill.scene.config.service.ConfigLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/config")
public class ConfigInheritanceController {

    private static final Logger log = LoggerFactory.getLogger(ConfigInheritanceController.class);

    @Autowired
    private ConfigLoaderService configLoader;

    @GetMapping("/inheritance-detail/{targetType}/{targetId}")
    public Map<String, Object> getConfigInheritance(
            @PathVariable String targetType,
            @PathVariable String targetId) {
        
        log.debug("[ConfigInheritance] Getting inheritance chain for {}/{}", targetType, targetId);
        
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("targetType", targetType);
        result.put("targetId", targetId);
        
        List<Map<String, Object>> chain = new ArrayList<>();
        Map<String, Object> resolved = new LinkedHashMap<>();
        
        Map<String, Object> systemConfig = getSystemConfig();
        if (systemConfig != null) {
            Map<String, Object> chainItem = new LinkedHashMap<>();
            chainItem.put("level", "system");
            chainItem.put("source", "system-config.json");
            chainItem.put("config", systemConfig);
            chain.add(chainItem);
            resolved.putAll(systemConfig);
        }
        
        if ("skill".equals(targetType) || "scene".equals(targetType) || "capability".equals(targetType)) {
            Map<String, Object> skillConfig = getSkillConfig(targetId);
            if (skillConfig != null) {
                Map<String, Object> chainItem = new LinkedHashMap<>();
                chainItem.put("level", "skill");
                chainItem.put("source", "skill.yaml");
                chainItem.put("config", skillConfig);
                chain.add(chainItem);
                mergeConfig(resolved, skillConfig);
            }
        }
        
        if ("scene".equals(targetType) || "capability".equals(targetType)) {
            Map<String, Object> sceneConfig = getSceneConfig(targetId);
            if (sceneConfig != null) {
                Map<String, Object> chainItem = new LinkedHashMap<>();
                chainItem.put("level", "scene");
                chainItem.put("source", "scene-config.yaml");
                chainItem.put("config", sceneConfig);
                chain.add(chainItem);
                mergeConfig(resolved, sceneConfig);
            }
        }
        
        if ("capability".equals(targetType)) {
            Map<String, Object> capabilityConfig = getCapabilityConfig(targetId);
            if (capabilityConfig != null) {
                Map<String, Object> chainItem = new LinkedHashMap<>();
                chainItem.put("level", "capability");
                chainItem.put("source", "capability.yaml");
                chainItem.put("config", capabilityConfig);
                chain.add(chainItem);
                mergeConfig(resolved, capabilityConfig);
            }
        }
        
        result.put("chain", chain);
        result.put("resolved", resolved);
        
        return result;
    }
    
    @GetMapping("/resolved/{targetType}/{targetId}")
    public Map<String, Object> getResolvedConfig(
            @PathVariable String targetType,
            @PathVariable String targetId) {
        
        Map<String, Object> result = getConfigInheritance(targetType, targetId);
        @SuppressWarnings("unchecked")
        Map<String, Object> resolved = (Map<String, Object>) result.get("resolved");
        return resolved;
    }

    private Map<String, Object> getSystemConfig() {
        try {
            ConfigNode node = configLoader.loadSystemConfig();
            if (node != null) {
                return node.getData();
            }
        } catch (Exception e) {
            log.debug("[ConfigInheritance] System config not found: {}", e.getMessage());
        }
        return null;
    }

    private Map<String, Object> getSkillConfig(String skillId) {
        try {
            ConfigNode node = configLoader.loadSkillConfig(skillId, true);
            if (node != null) {
                return node.getData();
            }
        } catch (Exception e) {
            log.debug("[ConfigInheritance] Skill config not found for {}: {}", skillId, e.getMessage());
        }
        return null;
    }

    private Map<String, Object> getSceneConfig(String sceneId) {
        try {
            ConfigNode node = configLoader.loadSceneConfig(sceneId, true);
            if (node != null) {
                return node.getData();
            }
        } catch (Exception e) {
            log.debug("[ConfigInheritance] Scene config not found for {}: {}", sceneId, e.getMessage());
        }
        return null;
    }

    private Map<String, Object> getCapabilityConfig(String capabilityId) {
        try {
            ConfigNode node = configLoader.loadInternalSkillConfig(null, capabilityId);
            if (node != null) {
                return node.getData();
            }
        } catch (Exception e) {
            log.debug("[ConfigInheritance] Capability config not found for {}: {}", capabilityId, e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void mergeConfig(Map<String, Object> target, Map<String, Object> source) {
        if (source == null) return;
        
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            if (value instanceof Map && target.containsKey(key) && target.get(key) instanceof Map) {
                Map<String, Object> targetMap = (Map<String, Object>) target.get(key);
                Map<String, Object> sourceMap = (Map<String, Object>) value;
                mergeConfig(targetMap, sourceMap);
            } else {
                target.put(key, value);
            }
        }
    }
}
