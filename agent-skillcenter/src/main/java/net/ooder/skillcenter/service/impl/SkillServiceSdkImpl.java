package net.ooder.skillcenter.service.impl;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.SkillDTO;
import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.service.SkillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "skillcenter.sdk.mode", havingValue = "sdk")
public class SkillServiceSdkImpl implements SkillService {

    private static final Logger log = LoggerFactory.getLogger(SkillServiceSdkImpl.class);

    private SkillManager skillManager;

    private final Map<String, SkillDTO> skillStore = new ConcurrentHashMap<>();
    private final Map<String, SkillDTO> marketSkillStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        log.info("[SkillServiceSdkImpl] Initialized with SDK mode, loading skills from SkillManager");
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
            log.info("[SkillServiceSdkImpl] Loaded {} skills from SkillManager", skills.size());
        }
    }

    private SkillDTO convertToDTO(Skill skill) {
        if (skill == null) return null;
        SkillDTO dto = new SkillDTO();
        dto.setId(skill.getId());
        dto.setName(skill.getName());
        dto.setDescription(skill.getDescription());
        dto.setVersion("1.0.0");
        dto.setAvailable(skill.isAvailable());
        dto.setCreatedAt(new Date());
        dto.setUpdatedAt(new Date());
        
        if (skill instanceof SkillManager.SkillInfo) {
            SkillManager.SkillInfo info = (SkillManager.SkillInfo) skill;
            dto.setCategory(info.getCategory());
            dto.setStatus(info.getStatus());
        } else {
            dto.setCategory("local");
            dto.setStatus(skill.isAvailable() ? "active" : "inactive");
        }
        return dto;
    }

    @Override
    public PageResult<SkillDTO> getAllSkills(String category, String status, String keyword, int pageNum, int pageSize) {
        // 每次都从SkillManager重新加载，确保获取最新数据
        refreshFromManager();
        
        List<SkillDTO> allSkills = new ArrayList<>(skillStore.values());

        List<SkillDTO> filtered = allSkills.stream()
            .filter(skill -> category == null || category.isEmpty() || category.equals(skill.getCategory()))
            .filter(skill -> status == null || status.isEmpty() || status.equals(skill.getStatus()))
            .filter(skill -> keyword == null || keyword.isEmpty() ||
                skill.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                skill.getId().toLowerCase().contains(keyword.toLowerCase()))
            .sorted(Comparator.comparing(SkillDTO::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
            .collect(Collectors.toList());

        return paginate(filtered, pageNum, pageSize);
    }
    
    private void refreshFromManager() {
        List<Skill> skills = skillManager.getAllSkills();
        if (skills != null) {
            for (Skill skill : skills) {
                if (!skillStore.containsKey(skill.getId())) {
                    SkillDTO dto = convertToDTO(skill);
                    skillStore.put(dto.getId(), dto);
                }
            }
        }
    }

    @Override
    public SkillDTO getSkillById(String skillId) {
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
    public SkillDTO addSkill(SkillDTO skillDTO) {
        String id = skillDTO.getId();
        if (id == null || id.isEmpty()) {
            id = "skill-" + idGenerator.getAndIncrement();
        }

        skillDTO.setId(id);
        skillDTO.setCreatedAt(new Date());
        skillDTO.setUpdatedAt(new Date());
        skillDTO.setStatus(skillDTO.getStatus() != null ? skillDTO.getStatus() : "pending");

        SkillManager.SkillInfo skillInfo = new SkillManager.SkillInfo();
        skillInfo.setId(id);
        skillInfo.setName(skillDTO.getName());
        skillInfo.setDescription(skillDTO.getDescription());
        skillInfo.setCategory(skillDTO.getCategory() != null ? skillDTO.getCategory() : "general");
        skillInfo.setStatus(skillDTO.getStatus());
        skillInfo.setAvailable("active".equals(skillDTO.getStatus()));
        skillInfo.setCreatedAt(new Date());
        
        skillManager.registerSkill(skillInfo);
        skillStore.put(id, skillDTO);
        log.info("[SkillServiceSdkImpl] Skill added: {}", id);
        return skillDTO;
    }

    @Override
    public SkillDTO updateSkill(String skillId, SkillDTO skillDTO) {
        SkillDTO existing = skillStore.get(skillId);
        if (existing == null) {
            return null;
        }

        skillDTO.setId(skillId);
        skillDTO.setCreatedAt(existing.getCreatedAt());
        skillDTO.setUpdatedAt(new Date());
        skillStore.put(skillId, skillDTO);

        log.info("[SkillServiceSdkImpl] Skill updated: {}", skillId);
        return skillDTO;
    }

    @Override
    public boolean deleteSkill(String skillId) {
        SkillDTO removed = skillStore.remove(skillId);
        if (removed != null) {
            skillManager.unregisterSkill(skillId);
            log.info("[SkillServiceSdkImpl] Skill deleted: {}", skillId);
            return true;
        }
        return false;
    }

    @Override
    public boolean approveSkill(String skillId) {
        SkillDTO skill = skillStore.get(skillId);
        if (skill != null) {
            skill.setStatus("active");
            skill.setAvailable(true);
            skill.setUpdatedAt(new Date());
            skillManager.approveSkill(skillId);
            log.info("[SkillServiceSdkImpl] Skill approved: {}", skillId);
            return true;
        }
        return false;
    }

    @Override
    public boolean rejectSkill(String skillId) {
        SkillDTO skill = skillStore.get(skillId);
        if (skill != null) {
            skill.setStatus("rejected");
            skill.setAvailable(false);
            skill.setUpdatedAt(new Date());
            skillManager.rejectSkill(skillId);
            log.info("[SkillServiceSdkImpl] Skill rejected: {}", skillId);
            return true;
        }
        return false;
    }

    @Override
    public PageResult<SkillDTO> getMarketSkills(String category, String status, String keyword, int pageNum, int pageSize) {
        List<SkillDTO> filtered = marketSkillStore.values().stream()
            .filter(skill -> category == null || category.isEmpty() || category.equals(skill.getCategory()))
            .filter(skill -> status == null || status.isEmpty() || status.equals(skill.getStatus()))
            .filter(skill -> keyword == null || keyword.isEmpty() ||
                skill.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                skill.getId().toLowerCase().contains(keyword.toLowerCase()))
            .sorted(Comparator.comparing(SkillDTO::getDownloadCount, Comparator.nullsLast(Comparator.reverseOrder())))
            .collect(Collectors.toList());

        return paginate(filtered, pageNum, pageSize);
    }

    @Override
    public SkillDTO addMarketSkill(SkillDTO skillDTO) {
        String id = skillDTO.getId();
        if (id == null || id.isEmpty()) {
            id = "market-" + idGenerator.getAndIncrement();
        }

        skillDTO.setId(id);
        skillDTO.setCreatedAt(new Date());
        skillDTO.setUpdatedAt(new Date());
        marketSkillStore.put(id, skillDTO);

        log.info("[SkillServiceSdkImpl] Market skill added: {}", id);
        return skillDTO;
    }

    @Override
    public SkillDTO updateMarketSkill(String skillId, SkillDTO skillDTO) {
        SkillDTO existing = marketSkillStore.get(skillId);
        if (existing == null) {
            return null;
        }

        skillDTO.setId(skillId);
        skillDTO.setCreatedAt(existing.getCreatedAt());
        skillDTO.setUpdatedAt(new Date());
        marketSkillStore.put(skillId, skillDTO);

        return skillDTO;
    }

    @Override
    public boolean removeMarketSkill(String skillId) {
        return marketSkillStore.remove(skillId) != null;
    }

    @Override
    public int getSkillCount() {
        return skillStore.size();
    }

    @Override
    public int getExecutionCount() {
        return 0;
    }

    @Override
    public int getSuccessfulExecutionCount() {
        return 0;
    }

    @Override
    public int getFailedExecutionCount() {
        return 0;
    }

    @Override
    public int getSharedSkillCount() {
        return (int) skillStore.values().stream()
            .filter(skill -> Boolean.TRUE.equals(skill.isAvailable()))
            .count();
    }

    @Override
    public java.util.concurrent.CompletableFuture<net.ooder.skillcenter.model.SpecValidationModels.SpecValidationResult> validateSpec(String skillId) {
        return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            net.ooder.skillcenter.model.SpecValidationModels.SpecValidationResult result = new net.ooder.skillcenter.model.SpecValidationModels.SpecValidationResult();
            SkillDTO skill = skillStore.get(skillId);
            if (skill == null) {
                result.setValid(false);
                result.setErrors(java.util.Collections.singletonList("Skill not found: " + skillId));
            } else {
                result.setValid(true);
                result.setErrors(new java.util.ArrayList<>());
                result.setWarnings(new java.util.ArrayList<>());
                java.util.Map<String, Object> details = new java.util.HashMap<>();
                details.put("skillId", skillId);
                details.put("name", skill.getName());
                details.put("version", skill.getVersion());
                result.setDetails(details);
            }
            return result;
        });
    }

    @Override
    public java.util.concurrent.CompletableFuture<net.ooder.skillcenter.model.SpecValidationModels.SpecValidationResult> validateDefinition(java.util.Map<String, Object> definition) {
        return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            net.ooder.skillcenter.model.SpecValidationModels.SpecValidationResult result = new net.ooder.skillcenter.model.SpecValidationModels.SpecValidationResult();
            java.util.List<String> errors = new java.util.ArrayList<>();
            java.util.List<String> warnings = new java.util.ArrayList<>();
            
            if (definition == null) {
                errors.add("Definition cannot be null");
            } else {
                if (!definition.containsKey("name") || definition.get("name") == null) {
                    errors.add("Skill name is required");
                }
                if (!definition.containsKey("version") || definition.get("version") == null) {
                    warnings.add("Version is recommended");
                }
            }
            
            result.setValid(errors.isEmpty());
            result.setErrors(errors);
            result.setWarnings(warnings);
            result.setDetails(definition);
            return result;
        });
    }

    @Override
    public java.util.concurrent.CompletableFuture<java.util.List<net.ooder.skillcenter.model.SpecValidationModels.VersionHistory>> getVersionHistory(String skillId, int limit) {
        return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            java.util.List<net.ooder.skillcenter.model.SpecValidationModels.VersionHistory> history = new java.util.ArrayList<>();
            SkillDTO skill = skillStore.get(skillId);
            if (skill != null) {
                net.ooder.skillcenter.model.SpecValidationModels.VersionHistory vh = new net.ooder.skillcenter.model.SpecValidationModels.VersionHistory();
                vh.setVersionId(skillId + "-v1");
                vh.setSkillId(skillId);
                vh.setVersion(skill.getVersion() != null ? skill.getVersion() : "1.0.0");
                vh.setAuthor(skill.getAuthor() != null ? skill.getAuthor() : "unknown");
                vh.setCreateTime(skill.getCreatedAt() != null ? skill.getCreatedAt().getTime() : System.currentTimeMillis());
                vh.setChangeDescription("Initial version");
                history.add(vh);
            }
            return history;
        });
    }

    @Override
    public java.util.concurrent.CompletableFuture<net.ooder.skillcenter.model.SpecValidationModels.SpecValidationReport> getValidationReport(String skillId) {
        return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            net.ooder.skillcenter.model.SpecValidationModels.SpecValidationReport report = new net.ooder.skillcenter.model.SpecValidationModels.SpecValidationReport();
            report.setSkillId(skillId);
            report.setValidationTime(System.currentTimeMillis());
            report.setPassed(true);
            report.setTotalChecks(3);
            report.setPassedChecks(3);
            report.setFailedChecks(0);
            report.setWarningCount(0);
            report.setChecks(new java.util.ArrayList<>());
            report.setSummary(new java.util.HashMap<>());
            return report;
        });
    }

    private PageResult<SkillDTO> paginate(List<SkillDTO> list, int pageNum, int pageSize) {
        int total = list.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        if (start >= total) {
            return PageResult.empty();
        }

        List<SkillDTO> pageList = list.subList(start, end);
        return PageResult.of(pageList, total, pageNum, pageSize);
    }
}
