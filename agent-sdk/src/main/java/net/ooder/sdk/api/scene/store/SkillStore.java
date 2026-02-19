package net.ooder.sdk.api.scene.store;

import java.util.List;

public interface SkillStore {
    
    void saveSkill(SkillRegistration registration);
    
    SkillRegistration loadSkill(String skillId);
    
    void deleteSkill(String skillId);
    
    List<SkillRegistration> listSkills(String sceneId, String groupId);
    
    List<SkillRegistration> listSkillsByScene(String sceneId);
    
    List<SkillRegistration> listSkillsByType(String sceneId, String skillType);
    
    boolean skillExists(String skillId);
    
    void updateSkillHeartbeat(String skillId, long timestamp);
    
    Long getSkillLastHeartbeat(String skillId);
    
    void updateSkillStatus(String skillId, String status);
    
    void updateSkillEndpoints(String skillId, java.util.Map<String, Object> endpoints);
}
