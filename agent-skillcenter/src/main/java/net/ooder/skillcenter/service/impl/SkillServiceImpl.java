package net.ooder.skillcenter.service.impl;

import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillResult;
import net.ooder.skillcenter.model.PageResponse;
import net.ooder.skillcenter.service.SkillService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 技能服务实现类 - 符合 ooderNexus 规范
 */
@Service
public class SkillServiceImpl implements SkillService {

    private final SkillManager skillManager;

    /**
     * 构造方法，初始化技能管理器
     */
    public SkillServiceImpl() {
        this.skillManager = SkillManager.getInstance();
    }

    /**
     * 构造方法，用于依赖注入
     * @param skillManager 技能管理器
     */
    public SkillServiceImpl(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @Override
    public PageResponse<Skill> getAllSkills(int page, int size, String sortBy, String sortDirection) {
        List<Skill> allSkills = skillManager.getAllSkills();
        long totalElements = allSkills.size();

        // 排序
        if (sortBy != null && !sortBy.isEmpty()) {
            allSkills.sort((s1, s2) -> {
                int compareResult = 0;
                switch (sortBy) {
                    case "id":
                        compareResult = s1.getId().compareTo(s2.getId());
                        break;
                    case "name":
                        compareResult = s1.getName().compareTo(s2.getName());
                        break;
                    case "category":
                        String cat1 = s1.getClass().getPackage().getName();
                        String cat2 = s2.getClass().getPackage().getName();
                        compareResult = cat1.compareTo(cat2);
                        break;
                    default:
                        compareResult = s1.getId().compareTo(s2.getId());
                }
                return "desc".equalsIgnoreCase(sortDirection) ? -compareResult : compareResult;
            });
        }

        // 分页
        int start = (page - 1) * size;
        int end = Math.min(start + size, allSkills.size());
        List<Skill> pagedSkills = start < allSkills.size() ? allSkills.subList(start, end) : new java.util.ArrayList<>();

        return PageResponse.of(pagedSkills, page, size, totalElements);
    }

    @Override
    public Skill getSkill(String id) {
        return skillManager.getSkill(id);
    }

    @Override
    public SkillResult executeSkill(String id, Map<String, Object> parameters, Map<String, Object> attributes) {
        try {
            SkillContext context = new SkillContext();
            // 设置参数
            if (parameters != null) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    context.addParameter(entry.getKey(), entry.getValue());
                }
            }
            // 设置属性
            if (attributes != null) {
                for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                    context.addAttribute(entry.getKey(), entry.getValue());
                }
            }
            // 执行技能
            return skillManager.executeSkill(id, context);
        } catch (SkillException e) {
            SkillResult result = new SkillResult(SkillResult.Status.FAILED, e.getMessage());
            result.setException(e);
            return result;
        }
    }

    @Override
    public List<String> getCategories() {
        List<Skill> skills = skillManager.getAllSkills();
        return skills.stream()
                .map(skill -> skill.getClass().getPackage().getName())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<Skill> getSkillsByCategory(String category, int page, int size, String sortBy, String sortDirection) {
        List<Skill> categorySkills = skillManager.getSkillsByCategory(category);
        long totalElements = categorySkills.size();

        // 排序
        if (sortBy != null && !sortBy.isEmpty()) {
            categorySkills.sort((s1, s2) -> {
                int compareResult = 0;
                switch (sortBy) {
                    case "id":
                        compareResult = s1.getId().compareTo(s2.getId());
                        break;
                    case "name":
                        compareResult = s1.getName().compareTo(s2.getName());
                        break;
                    default:
                        compareResult = s1.getId().compareTo(s2.getId());
                }
                return "desc".equalsIgnoreCase(sortDirection) ? -compareResult : compareResult;
            });
        }

        // 分页
        int start = (page - 1) * size;
        int end = Math.min(start + size, categorySkills.size());
        List<Skill> pagedSkills = start < categorySkills.size() ? categorySkills.subList(start, end) : new java.util.ArrayList<>();

        return PageResponse.of(pagedSkills, page, size, totalElements);
    }

    @Override
    public boolean deleteSkill(String id) {
        try {
            skillManager.unregisterSkill(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean publishSkill(Skill skill) {
        try {
            skillManager.registerSkill(skill);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateSkill(String id, Skill skill) {
        try {
            // 确保技能ID一致
            if (skill instanceof SkillManager.SkillInfo) {
                ((SkillManager.SkillInfo) skill).setId(id);
            }
            skillManager.updateSkill(skill);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Skill> getSkillsByIds(List<String> ids) {
        return ids.stream()
                .map(skillManager::getSkill)
                .filter(skill -> skill != null)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<Skill> searchSkills(String keyword, int page, int size) {
        List<Skill> allSkills = skillManager.getAllSkills();
        List<Skill> matchedSkills = allSkills.stream()
                .filter(skill -> 
                        skill.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                        skill.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                        skill.getId().toLowerCase().contains(keyword.toLowerCase())
                )
                .collect(Collectors.toList());

        long totalElements = matchedSkills.size();

        // 分页
        int start = (page - 1) * size;
        int end = Math.min(start + size, matchedSkills.size());
        List<Skill> pagedSkills = start < matchedSkills.size() ? matchedSkills.subList(start, end) : new java.util.ArrayList<>();

        return PageResponse.of(pagedSkills, page, size, totalElements);
    }
}
