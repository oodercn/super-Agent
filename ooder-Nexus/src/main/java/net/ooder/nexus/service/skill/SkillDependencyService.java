package net.ooder.nexus.service.skill;

import net.ooder.nexus.domain.skill.model.InstallPreview;
import net.ooder.nexus.domain.skill.model.SkillDependency;

import java.util.List;
import java.util.Map;

/**
 * Skill依赖分析服务接口
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public interface SkillDependencyService {
    
    SkillDependency analyzeDependencies(String skillId, String userId);
    
    InstallPreview previewInstall(String skillId, String userId, String downloadUrl);
    
    List<Map<String, Object>> analyzeRequiredPermissions(String skillId);
    
    List<Map<String, Object>> analyzeSceneDependencies(String skillId);
    
    List<Map<String, Object>> analyzeSkillDependencies(String skillId);
    
    boolean checkSkillInstalled(String skillId, String userId);
    
    Map<String, Object> getGroupAddress(String sceneId, String userId);
    
    Map<String, Object> writeCapInfo(String skillId, String userId, Map<String, Object> capConfig);
}
