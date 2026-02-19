package net.ooder.sdk.core.skill.impl;

import net.ooder.sdk.api.skill.SkillPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LocalSkillRegistry {
    
    private static final Logger log = LoggerFactory.getLogger(LocalSkillRegistry.class);
    
    private final Map<String, SkillPackage> packages = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> configs = new ConcurrentHashMap<>();
    private final Map<String, String> states = new ConcurrentHashMap<>();
    
    public void register(SkillPackage skillPackage) {
        if (skillPackage == null || skillPackage.getSkillId() == null) {
            throw new IllegalArgumentException("SkillPackage and skillId cannot be null");
        }
        String skillId = skillPackage.getSkillId();
        packages.put(skillId, skillPackage);
        states.put(skillId, "registered");
        log.debug("Skill package registered: {}", skillId);
    }
    
    public void unregister(String skillId) {
        SkillPackage removed = packages.remove(skillId);
        configs.remove(skillId);
        states.remove(skillId);
        if (removed != null) {
            log.debug("Skill package unregistered: {}", skillId);
        }
    }
    
    public SkillPackage get(String skillId) {
        return packages.get(skillId);
    }
    
    public Collection<SkillPackage> getAll() {
        return new ArrayList<>(packages.values());
    }
    
    public Set<String> getAllSkillIds() {
        return packages.keySet();
    }
    
    public boolean has(String skillId) {
        return packages.containsKey(skillId);
    }
    
    public int size() {
        return packages.size();
    }
    
    public void clear() {
        packages.clear();
        configs.clear();
        states.clear();
        log.debug("All skill packages cleared");
    }
    
    public void configure(String skillId, Map<String, Object> config) {
        if (!packages.containsKey(skillId)) {
            log.warn("Cannot configure non-existent skill: {}", skillId);
            return;
        }
        Map<String, Object> existingConfig = configs.computeIfAbsent(skillId, k -> new ConcurrentHashMap<>());
        if (config != null) {
            existingConfig.putAll(config);
        }
        log.debug("Skill {} configured", skillId);
    }
    
    public Map<String, Object> getConfiguration(String skillId) {
        return configs.get(skillId);
    }
    
    public void activate(String skillId) {
        if (!packages.containsKey(skillId)) {
            log.warn("Cannot activate non-existent skill: {}", skillId);
            return;
        }
        states.put(skillId, "active");
        log.debug("Skill {} activated", skillId);
    }
    
    public void deactivate(String skillId) {
        if (!packages.containsKey(skillId)) {
            log.warn("Cannot deactivate non-existent skill: {}", skillId);
            return;
        }
        states.put(skillId, "inactive");
        log.debug("Skill {} deactivated", skillId);
    }
    
    public String getState(String skillId) {
        return states.get(skillId);
    }
    
    public boolean isActive(String skillId) {
        return "active".equals(states.get(skillId));
    }
}
