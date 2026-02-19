
package net.ooder.sdk.api.skill;

import java.util.List;
import java.util.Map;

public interface CollaborativeGroupManager {
    
    String createCollaborativeGroup(String sceneId, String groupId, Map<String, Object> config);
    
    boolean removeCollaborativeGroup(String groupId);
    
    boolean addMember(String groupId, String skillId, String role);
    
    boolean removeMember(String groupId, String skillId);
    
    String createLink(String fromSkillId, String toSkillId, String linkType);
    
    boolean removeLink(String linkId);
    
    Map<String, Object> getCollaborativeGroupInfo(String groupId);
    
    Map<String, Object> getLinkInfo(String linkId);
    
    void syncToMain(String mainServiceId);
    
    void receiveFromMain(Map<String, Object> collaborativeInfo);
    
    List<Map<String, Object>> getAllCollaborativeGroups();
    
    List<Map<String, Object>> getAllLinks();
}
