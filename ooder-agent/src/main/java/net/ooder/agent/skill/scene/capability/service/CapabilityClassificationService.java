package net.ooder.mvp.skill.scene.capability.service;

import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.model.SkillForm;
import net.ooder.mvp.skill.scene.capability.model.SceneType;
import net.ooder.mvp.skill.scene.capability.model.Visibility;
import net.ooder.mvp.skill.scene.capability.model.CapabilityCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class CapabilityClassificationService {
    
    private static final Logger log = LoggerFactory.getLogger(CapabilityClassificationService.class);
    
    private static final Set<String> AUTO_BUSINESS_CATEGORIES = new HashSet<>(Arrays.asList(
        "AI_ASSISTANT", "SYSTEM_TOOLS", "SYSTEM_MONITOR", "SECURITY_AUDIT", "INFRASTRUCTURE"
    ));
    
    private static final Set<String> INTERNAL_BUSINESS_CATEGORIES = new HashSet<>(Arrays.asList(
        "SYSTEM_MONITOR", "SECURITY_AUDIT", "INFRASTRUCTURE"
    ));
    
    public static class ClassificationResult {
        private final SkillForm skillForm;
        private final SceneType sceneType;
        private final Visibility visibility;
        private final CapabilityCategory capabilityCategory;
        private final boolean isInternal;
        private final String reason;
        
        public ClassificationResult(SkillForm skillForm, SceneType sceneType, 
                                     Visibility visibility, CapabilityCategory capabilityCategory,
                                     boolean isInternal, String reason) {
            this.skillForm = skillForm;
            this.sceneType = sceneType;
            this.visibility = visibility;
            this.capabilityCategory = capabilityCategory;
            this.isInternal = isInternal;
            this.reason = reason;
        }
        
        public SkillForm getSkillForm() { return skillForm; }
        public SceneType getSceneType() { return sceneType; }
        public Visibility getVisibility() { return visibility; }
        public CapabilityCategory getCapabilityCategory() { return capabilityCategory; }
        public boolean isInternal() { return isInternal; }
        public String getReason() { return reason; }
    }
    
    public ClassificationResult classify(Capability cap) {
        SkillForm skillForm = determineSkillForm(cap);
        SceneType sceneType = determineSceneType(cap, skillForm);
        Visibility visibility = determineVisibility(cap, skillForm, sceneType);
        CapabilityCategory capabilityCategory = determineCapabilityCategory(cap);
        boolean isInternal = visibility == Visibility.INTERNAL;
        
        String reason = String.format("form=%s, sceneType=%s, visibility=%s, category=%s, internal=%s",
            skillForm, sceneType, visibility, capabilityCategory, isInternal);
        
        log.debug("[classify] {} -> {}", cap.getCapabilityId(), reason);
        
        return new ClassificationResult(skillForm, sceneType, visibility, capabilityCategory, isInternal, reason);
    }
    
    private SkillForm determineSkillForm(Capability cap) {
        if (cap.getSkillForm() != null) {
            return cap.getSkillForm();
        }
        
        if (cap.isSceneCapability()) {
            return SkillForm.SCENE;
        }
        
        if (cap.getCapabilityType() != null) {
            String type = cap.getCapabilityType().name();
            if ("SCENE".equals(type) || "scene-skill".equalsIgnoreCase(type)) {
                return SkillForm.SCENE;
            }
            if ("DRIVER".equals(type)) {
                return SkillForm.DRIVER;
            }
        }
        
        if (cap.getDriverType() != null && cap.getDriverType().isTrigger()) {
            return SkillForm.DRIVER;
        }
        
        if (cap.getCapabilityCategory() != null) {
            CapabilityCategory cat = cap.getCapabilityCategory();
            if (cat == CapabilityCategory.LLM || cat == CapabilityCategory.KNOWLEDGE || 
                cat == CapabilityCategory.VFS) {
                return SkillForm.PROVIDER;
            }
        }
        
        if (cap.getMainFirstConfig() != null || cap.getParticipants() != null) {
            return SkillForm.SCENE;
        }
        
        return SkillForm.PROVIDER;
    }
    
    private SceneType determineSceneType(Capability cap, SkillForm skillForm) {
        if (cap.getSceneTypeEnum() != null) {
            return cap.getSceneTypeEnum();
        }
        
        if (skillForm != SkillForm.SCENE) {
            return null;
        }
        
        if (cap.isHasSelfDrive()) {
            return SceneType.AUTO;
        }
        
        if (cap.getMainFirstConfig() != null && cap.getMainFirstConfig().getSelfDrive() != null) {
            return SceneType.AUTO;
        }
        
        if (cap.getBusinessCategory() != null && AUTO_BUSINESS_CATEGORIES.contains(cap.getBusinessCategory())) {
            return SceneType.AUTO;
        }
        
        return SceneType.TRIGGER;
    }
    
    private Visibility determineVisibility(Capability cap, SkillForm skillForm, SceneType sceneType) {
        if (cap.getVisibilityEnum() != null) {
            return cap.getVisibilityEnum();
        }
        
        if (skillForm == SkillForm.INTERNAL) {
            return Visibility.INTERNAL;
        }
        
        if (cap.getBusinessCategory() != null && INTERNAL_BUSINESS_CATEGORIES.contains(cap.getBusinessCategory())) {
            return Visibility.INTERNAL;
        }
        
        if (sceneType == SceneType.AUTO) {
            Integer score = cap.getBusinessSemanticsScore();
            if (score != null && score < 8) {
                return Visibility.INTERNAL;
            }
        }
        
        if (cap.getParentSkill() != null && cap.getParentScene() != null) {
            return Visibility.INTERNAL;
        }
        
        return Visibility.PUBLIC;
    }
    
    private CapabilityCategory determineCapabilityCategory(Capability cap) {
        if (cap.getCapabilityCategory() != null) {
            return cap.getCapabilityCategory();
        }
        
        if (cap.getRequiredAddresses() != null && !cap.getRequiredAddresses().isEmpty()) {
            return cap.getRequiredAddresses().get(0).getCategory();
        }
        
        return CapabilityCategory.UTIL;
    }
    
    public boolean shouldDisplayInDiscovery(Capability cap) {
        ClassificationResult result = classify(cap);
        return result.getVisibility() != Visibility.INTERNAL;
    }
    
    public boolean matchesFilter(Capability cap, SkillForm formFilter, 
                                  SceneType sceneTypeFilter, CapabilityCategory categoryFilter) {
        ClassificationResult result = classify(cap);
        
        if (result.getVisibility() == Visibility.INTERNAL) {
            return false;
        }
        
        if (formFilter != null && result.getSkillForm() != formFilter) {
            return false;
        }
        
        if (sceneTypeFilter != null && result.getSceneType() != sceneTypeFilter) {
            return false;
        }
        
        if (categoryFilter != null && result.getCapabilityCategory() != categoryFilter) {
            return false;
        }
        
        return true;
    }
}
