package net.ooder.sdk.api.scene.store;

import java.util.List;
import java.util.Map;

public interface GroupStore {
    
    void saveGroup(String sceneId, String groupId, Map<String, Object> config);
    
    Map<String, Object> loadGroup(String sceneId, String groupId);
    
    void deleteGroup(String sceneId, String groupId);
    
    List<String> listGroups(String sceneId);
    
    boolean groupExists(String sceneId, String groupId);
    
    void updateGroupConfig(String sceneId, String groupId, String key, Object value);
    
    Object getGroupConfigValue(String sceneId, String groupId, String key);
    
    void addSkillToGroup(String sceneId, String groupId, String skillId);
    
    void removeSkillFromGroup(String sceneId, String groupId, String skillId);
    
    List<String> getGroupSkills(String sceneId, String groupId);
}
