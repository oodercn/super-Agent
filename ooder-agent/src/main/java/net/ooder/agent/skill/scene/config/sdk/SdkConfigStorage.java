package net.ooder.mvp.skill.scene.config.sdk;

public interface SdkConfigStorage {
    
    ConfigNode loadSystemConfig();
    
    ConfigNode loadProfile(String profileName);
    
    ConfigNode loadSkillConfig(String skillId);
    
    ConfigNode loadSceneConfig(String sceneId);
    
    ConfigNode loadInternalSkillConfig(String sceneId, String skillId);
    
    void saveSystemConfig(ConfigNode config);
    
    void saveSkillConfig(String skillId, ConfigNode config);
    
    void saveSceneConfig(String sceneId, ConfigNode config);
    
    void saveInternalSkillConfig(String sceneId, String skillId, ConfigNode config);
    
    void deleteConfig(String targetType, String targetId);
    
    boolean exists(String targetType, String targetId);
    
    void invalidateCache(String key);
    
    void invalidateAllCache();
}
