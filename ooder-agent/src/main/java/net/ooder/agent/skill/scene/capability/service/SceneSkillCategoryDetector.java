package net.ooder.mvp.skill.scene.capability.service;

import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.model.CapabilityType;
import net.ooder.mvp.skill.scene.capability.model.SceneType;
import net.ooder.mvp.skill.scene.capability.model.SkillForm;
import net.ooder.mvp.skill.scene.capability.model.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SceneSkillCategoryDetector {

    private static final Logger log = LoggerFactory.getLogger(SceneSkillCategoryDetector.class);

    public SceneType detectSceneType(Capability capability) {
        if (capability == null) {
            return null;
        }
        
        return capability.getSceneTypeEnum();
    }

    public SceneType detectSceneType(Map<String, Object> skillData) {
        if (skillData == null) {
            return null;
        }
        
        Object sceneType = skillData.get("sceneType");
        if (sceneType != null) {
            try {
                return SceneType.fromCode(String.valueOf(sceneType));
            } catch (Exception e) {
                log.warn("[detectSceneType] Invalid sceneType value: {}", sceneType);
            }
        }
        
        return null;
    }

    public SkillForm detectSkillForm(Capability capability) {
        if (capability == null) {
            return SkillForm.PROVIDER;
        }
        
        if (capability.getSkillForm() != null) {
            return capability.getSkillForm();
        }
        
        return SkillForm.PROVIDER;
    }

    public SkillForm detectSkillForm(Map<String, Object> skillData) {
        if (skillData == null) {
            return SkillForm.PROVIDER;
        }
        
        Object skillForm = skillData.get("skillForm");
        if (skillForm != null) {
            try {
                return SkillForm.fromCode(String.valueOf(skillForm));
            } catch (Exception e) {
                log.warn("[detectSkillForm] Invalid skillForm value: {}", skillForm);
            }
        }
        
        return SkillForm.PROVIDER;
    }
    
    public boolean isSceneSkill(Capability capability) {
        return detectSkillForm(capability) == SkillForm.SCENE;
    }
    
    public boolean isInternal(Capability capability) {
        if (capability == null) {
            return false;
        }
        
        Visibility visibility = capability.getVisibilityEnum();
        return visibility == Visibility.INTERNAL;
    }
    
    public boolean isInternal(Map<String, Object> metadata) {
        if (metadata == null) {
            return false;
        }
        
        Object visibility = metadata.get("visibility");
        if (visibility != null) {
            return Visibility.INTERNAL.getCode().equalsIgnoreCase(String.valueOf(visibility));
        }
        return false;
    }
}
