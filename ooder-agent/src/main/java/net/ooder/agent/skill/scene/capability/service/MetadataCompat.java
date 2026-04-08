package net.ooder.mvp.skill.scene.capability.service;

import net.ooder.scene.skill.model.SceneType;
import net.ooder.scene.skill.model.SkillForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetadataCompat {

    private MetadataCompat() {
    }

    public static boolean isSceneSkill(Map<String, Object> metadata) {
        if (metadata == null) {
            return false;
        }
        
        Object sceneSkill = metadata.get("sceneSkill");
        if (sceneSkill instanceof Boolean) {
            return (Boolean) sceneSkill;
        }
        
        Object type = metadata.get("type");
        if ("scene-skill".equals(type) || "SCENE".equals(type) || "scene".equals(type)) {
            return true;
        }
        
        return false;
    }

    public static SceneType getSceneType(Map<String, Object> metadata) {
        if (metadata == null) {
            return null;
        }
        
        Object sceneType = metadata.get("sceneType");
        if (sceneType != null) {
            try {
                return SceneType.valueOf(String.valueOf(sceneType));
            } catch (Exception e) {
                return null;
            }
        }
        
        return null;
    }

    public static SkillForm getSkillForm(Map<String, Object> metadata) {
        if (metadata == null) {
            return SkillForm.STANDALONE;
        }
        
        Object skillForm = metadata.get("skillForm");
        if (skillForm != null) {
            try {
                return SkillForm.valueOf(String.valueOf(skillForm));
            } catch (Exception e) {
                return SkillForm.STANDALONE;
            }
        }
        
        if (isSceneSkill(metadata)) {
            return SkillForm.SCENE;
        }
        
        return SkillForm.STANDALONE;
    }

    public static String getVisibility(Map<String, Object> metadata) {
        if (metadata == null) {
            return "private";
        }
        
        Object visibility = metadata.get("visibility");
        if (visibility != null) {
            return String.valueOf(visibility);
        }
        
        return "public";
    }

    public static boolean isInternal(Map<String, Object> metadata) {
        return "internal".equals(getVisibility(metadata));
    }

    @SuppressWarnings("unchecked")
    public static List<String> getBusinessTags(Map<String, Object> metadata) {
        if (metadata == null) {
            return new ArrayList<String>();
        }
        
        Object businessTags = metadata.get("businessTags");
        if (businessTags instanceof List) {
            List<String> result = new ArrayList<String>();
            for (Object tag : (List<?>) businessTags) {
                if (tag != null) {
                    result.add(String.valueOf(tag));
                }
            }
            return result;
        }
        
        return new ArrayList<String>();
    }
}
