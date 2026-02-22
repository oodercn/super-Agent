package net.ooder.skillcenter.sdk;

import net.ooder.skillcenter.config.SdkConfig;
import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.SkillDTO;
import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.model.Skill;
import net.ooder.nexus.skillcenter.dto.skill.SkillManifestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Primary
public class SkillSdkAdapterImpl implements SkillSdkAdapter {

    private static final Logger log = LoggerFactory.getLogger(SkillSdkAdapterImpl.class);

    @Autowired
    private SdkConfig sdkConfig;

    private SkillManager skillManager;

    private final Map<String, SkillDTO> skillStore = new ConcurrentHashMap<>();
    private final Map<String, SkillDTO> receivedSkills = new ConcurrentHashMap<>();
    private final Map<String, SkillManifestDTO> manifestStore = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("[SkillSdkAdapter] Initializing with real data from SkillManager");
        skillManager = SkillManager.getInstance();
        loadSkillsFromManager();
    }

    private void loadSkillsFromManager() {
        List<Skill> skills = skillManager.getAllSkills();
        if (skills != null) {
            for (Skill skill : skills) {
                SkillDTO dto = convertToDTO(skill);
                skillStore.put(dto.getId(), dto);
            }
            log.info("[SkillSdkAdapter] Loaded {} skills from SkillManager", skills.size());
        }
    }

    private SkillDTO convertToDTO(Skill skill) {
        SkillDTO dto = new SkillDTO();
        dto.setId(skill.getId());
        dto.setName(skill.getName());
        dto.setDescription(skill.getDescription());
        dto.setVersion("1.0.0");
        dto.setStatus(skill.isAvailable() ? "active" : "inactive");
        dto.setAvailable(skill.isAvailable());
        dto.setCreatedAt(new Date());
        dto.setUpdatedAt(new Date());
        if (skill instanceof SkillManager.SkillInfo) {
            SkillManager.SkillInfo info = (SkillManager.SkillInfo) skill;
            dto.setCategory(info.getCategory());
        }
        return dto;
    }

    @Override
    public PageResult<SkillDTO> getSkills(String category, String status, String keyword, int pageNum, int pageSize) {
        log.debug("[SkillSdkAdapter] Getting skills: category={}, status={}, keyword={}", category, status, keyword);
        
        loadSkillsFromManager();
        
        List<SkillDTO> filtered = skillStore.values().stream()
            .filter(s -> category == null || category.isEmpty() || category.equals(s.getCategory()))
            .filter(s -> status == null || status.isEmpty() || status.equals(s.getStatus()))
            .filter(s -> keyword == null || keyword.isEmpty() || 
                s.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                s.getDescription().toLowerCase().contains(keyword.toLowerCase()))
            .sorted(Comparator.comparing(SkillDTO::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
            .collect(Collectors.toList());
        
        return paginate(filtered, pageNum, pageSize);
    }

    @Override
    public SkillDTO getSkillById(String skillId) {
        log.debug("[SkillSdkAdapter] Getting skill: {}", skillId);
        SkillDTO dto = skillStore.get(skillId);
        if (dto == null) {
            Skill skill = skillManager.getSkill(skillId);
            if (skill != null) {
                dto = convertToDTO(skill);
                skillStore.put(skillId, dto);
            }
        }
        return dto;
    }

    @Override
    public SkillDTO createSkill(SkillDTO skill) {
        log.debug("[SkillSdkAdapter] Creating skill: {}", skill.getName());
        String id = skill.getId() != null ? skill.getId() : "skill-" + UUID.randomUUID().toString().substring(0, 8);
        skill.setId(id);
        skill.setCreatedAt(new Date());
        skill.setUpdatedAt(new Date());
        skill.setStatus(skill.getStatus() != null ? skill.getStatus() : "pending");
        
        SkillManager.SkillInfo skillInfo = new SkillManager.SkillInfo();
        skillInfo.setId(id);
        skillInfo.setName(skill.getName());
        skillInfo.setDescription(skill.getDescription());
        skillInfo.setCategory(skill.getCategory() != null ? skill.getCategory() : "general");
        skillInfo.setStatus(skill.getStatus());
        skillInfo.setAvailable("active".equals(skill.getStatus()));
        skillInfo.setCreatedAt(new Date());
        
        skillManager.registerSkill(skillInfo);
        skillStore.put(id, skill);
        log.info("[SkillSdkAdapter] Skill created: {}", id);
        return skill;
    }

    @Override
    public SkillDTO updateSkill(String skillId, SkillDTO skill) {
        log.debug("[SkillSdkAdapter] Updating skill: {}", skillId);
        SkillDTO existing = skillStore.get(skillId);
        if (existing == null) {
            return null;
        }
        skill.setId(skillId);
        skill.setCreatedAt(existing.getCreatedAt());
        skill.setUpdatedAt(new Date());
        skillStore.put(skillId, skill);
        
        SkillManager.SkillInfo skillInfo = new SkillManager.SkillInfo();
        skillInfo.setId(skillId);
        skillInfo.setName(skill.getName());
        skillInfo.setDescription(skill.getDescription());
        skillInfo.setCategory(skill.getCategory() != null ? skill.getCategory() : "general");
        skillInfo.setStatus(skill.getStatus());
        skillInfo.setAvailable("active".equals(skill.getStatus()));
        
        try {
            skillManager.updateSkill(skillInfo);
        } catch (Exception e) {
            log.warn("[SkillSdkAdapter] Failed to update skill in manager: {}", e.getMessage());
        }
        
        return skill;
    }

    @Override
    public boolean deleteSkill(String skillId) {
        log.debug("[SkillSdkAdapter] Deleting skill: {}", skillId);
        SkillDTO removed = skillStore.remove(skillId);
        if (removed != null) {
            skillManager.unregisterSkill(skillId);
            log.info("[SkillSdkAdapter] Skill deleted: {}", skillId);
            return true;
        }
        return false;
    }

    @Override
    public boolean installSkill(String skillId, String source) {
        log.debug("[SkillSdkAdapter] Installing skill: {} from {}", skillId, source);
        SkillDTO skill = skillStore.get(skillId);
        if (skill != null) {
            skill.setStatus("installed");
            skill.setUpdatedAt(new Date());
            log.info("[SkillSdkAdapter] Skill installed: {}", skillId);
            return true;
        }
        return false;
    }

    @Override
    public boolean uninstallSkill(String skillId) {
        log.debug("[SkillSdkAdapter] Uninstalling skill: {}", skillId);
        SkillDTO skill = skillStore.get(skillId);
        if (skill != null && "installed".equals(skill.getStatus())) {
            skill.setStatus("available");
            skill.setUpdatedAt(new Date());
            log.info("[SkillSdkAdapter] Skill uninstalled: {}", skillId);
            return true;
        }
        return false;
    }

    @Override
    public PageResult<SkillDTO> searchSkills(String keyword, String category, int pageNum, int pageSize) {
        return getSkills(category, null, keyword, pageNum, pageSize);
    }

    @Override
    public List<SkillDTO> getInstalledSkills() {
        return skillStore.values().stream()
            .filter(s -> "installed".equals(s.getStatus()) || "active".equals(s.getStatus()))
            .collect(Collectors.toList());
    }

    @Override
    public List<SkillDTO> getAvailableSkills() {
        return skillStore.values().stream()
            .filter(s -> "available".equals(s.getStatus()) || "active".equals(s.getStatus()))
            .collect(Collectors.toList());
    }

    @Override
    public SkillManifestDTO getSkillManifest(String skillId) {
        log.debug("[SkillSdkAdapter] Getting manifest: {}", skillId);
        return manifestStore.get(skillId);
    }

    @Override
    public Map<String, Object> getSkillStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", skillStore.size());
        stats.put("installed", getInstalledSkills().size());
        stats.put("available", getAvailableSkills().size());
        stats.put("shared", receivedSkills.size());
        return stats;
    }

    @Override
    public boolean shareSkill(String skillId, List<String> targetAgents) {
        log.debug("[SkillSdkAdapter] Sharing skill: {} to {}", skillId, targetAgents);
        SkillDTO skill = skillStore.get(skillId);
        if (skill != null) {
            log.info("[SkillSdkAdapter] Skill shared: {} to {} agents", skillId, targetAgents.size());
            return true;
        }
        return false;
    }

    @Override
    public boolean cancelShare(String skillId) {
        log.debug("[SkillSdkAdapter] Canceling share: {}", skillId);
        return true;
    }

    @Override
    public PageResult<SkillDTO> getReceivedSkills(int pageNum, int pageSize) {
        List<SkillDTO> all = new ArrayList<>(receivedSkills.values());
        return paginate(all, pageNum, pageSize);
    }

    @Override
    public boolean acceptReceivedSkill(String skillId) {
        log.debug("[SkillSdkAdapter] Accepting received skill: {}", skillId);
        SkillDTO skill = receivedSkills.remove(skillId);
        if (skill != null) {
            skill.setStatus("installed");
            skillStore.put(skillId, skill);
            log.info("[SkillSdkAdapter] Received skill accepted: {}", skillId);
            return true;
        }
        return false;
    }

    @Override
    public boolean rejectReceivedSkill(String skillId) {
        log.debug("[SkillSdkAdapter] Rejecting received skill: {}", skillId);
        SkillDTO removed = receivedSkills.remove(skillId);
        return removed != null;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    private <T> PageResult<T> paginate(List<T> list, int pageNum, int pageSize) {
        int total = list.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        if (start >= total) {
            return PageResult.empty();
        }

        List<T> pageList = list.subList(start, end);
        return PageResult.of(pageList, total, pageNum, pageSize);
    }
}
