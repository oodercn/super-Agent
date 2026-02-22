package net.ooder.nexus.service.skill;

import net.ooder.nexus.domain.skill.model.SkillAuthorization;
import net.ooder.nexus.domain.skill.model.SkillResourceLog;

import java.util.List;
import java.util.Map;

/**
 * 技能授权服务接口
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public interface SkillAuthService {
    
    List<SkillAuthorization> getPendingAuthorizations(String userId);
    
    List<SkillAuthorization> getSkillAuthorizations(String userId, String skillId);
    
    List<SkillAuthorization> getAllAuthorizations(String userId);
    
    SkillAuthorization createAuthorizationRequest(String userId, String skillId, String skillName, 
            String authType, List<Map<String, Object>> resourcePermissions, 
            List<Map<String, Object>> scenePermissions, String authReason);
    
    boolean approveAuthorization(String authId, String userId);
    
    boolean rejectAuthorization(String authId, String userId, String reason);
    
    boolean revokeAuthorization(String authId, String userId);
    
    boolean checkPermission(String skillId, String userId, String resourceType, String resourceId, String action);
    
    void logResourceAccess(String skillId, String skillName, String userId, String action,
            String resourceType, String resourceId, String resourceName,
            String sceneId, String sceneName, String groupId, String detail, int status);
    
    List<SkillResourceLog> getResourceLogs(String userId, Map<String, Object> filters);
    
    List<SkillResourceLog> getSkillResourceLogs(String skillId, String userId);
    
    Map<String, Object> getResourceUsageStats(String userId, String skillId);
    
    List<Map<String, Object>> getInstallRequests(String userId);
    
    Map<String, Object> processInstallRequest(String requestId, String userId, boolean approved, String reason);
}
