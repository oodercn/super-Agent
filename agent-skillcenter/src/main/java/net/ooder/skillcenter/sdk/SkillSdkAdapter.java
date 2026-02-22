package net.ooder.skillcenter.sdk;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.SkillDTO;
import net.ooder.nexus.skillcenter.dto.skill.SkillManifestDTO;

import java.util.List;
import java.util.Map;

public interface SkillSdkAdapter {
    
    PageResult<SkillDTO> getSkills(String category, String status, String keyword, int pageNum, int pageSize);
    
    SkillDTO getSkillById(String skillId);
    
    SkillDTO createSkill(SkillDTO skill);
    
    SkillDTO updateSkill(String skillId, SkillDTO skill);
    
    boolean deleteSkill(String skillId);
    
    boolean installSkill(String skillId, String source);
    
    boolean uninstallSkill(String skillId);
    
    PageResult<SkillDTO> searchSkills(String keyword, String category, int pageNum, int pageSize);
    
    List<SkillDTO> getInstalledSkills();
    
    List<SkillDTO> getAvailableSkills();
    
    SkillManifestDTO getSkillManifest(String skillId);
    
    Map<String, Object> getSkillStats();
    
    boolean shareSkill(String skillId, List<String> targetAgents);
    
    boolean cancelShare(String skillId);
    
    PageResult<SkillDTO> getReceivedSkills(int pageNum, int pageSize);
    
    boolean acceptReceivedSkill(String skillId);
    
    boolean rejectReceivedSkill(String skillId);
    
    boolean isAvailable();
}
