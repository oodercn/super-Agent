package net.ooder.nexus.service.skill.impl;

import net.ooder.nexus.domain.skill.model.SkillPackage;
import net.ooder.nexus.domain.skill.model.SkillPackage.AuthStatus;
import net.ooder.nexus.service.skill.SkillMarketService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 技能市场服务实现
 *
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@Service
public class SkillMarketServiceImpl implements SkillMarketService {

    private static final Logger log = LoggerFactory.getLogger(SkillMarketServiceImpl.class);

    private final Map<String, SkillPackage> skillStore = new ConcurrentHashMap<String, SkillPackage>();
    private final Map<String, Set<String>> userInstalledSkills = new ConcurrentHashMap<String, Set<String>>();

    public SkillMarketServiceImpl() {
        initDefaultSkills();
    }

    private void initDefaultSkills() {
        addSkill(createSkill("skill-001", "智能对话", "1.0.0", "AI助手", "提供智能对话能力", "ooder Team", AuthStatus.VERIFIED));
        addSkill(createSkill("skill-002", "文档解析", "1.2.0", "文档处理", "解析PDF、Word等文档", "ooder Team", AuthStatus.VERIFIED));
        addSkill(createSkill("skill-003", "图像识别", "2.0.0", "AI视觉", "图像分类、目标检测", "ooder Team", AuthStatus.VERIFIED));
        addSkill(createSkill("skill-004", "语音转写", "1.5.0", "语音处理", "语音转文字", "ooder Team", AuthStatus.PENDING));
        addSkill(createSkill("skill-005", "数据分析", "1.0.0", "数据科学", "数据可视化、统计分析", "ooder Team", AuthStatus.VERIFIED));
        addSkill(createSkill("skill-006", "代码生成", "1.3.0", "开发工具", "AI辅助编程", "ooder Team", AuthStatus.VERIFIED));
        addSkill(createSkill("skill-007", "翻译助手", "2.1.0", "语言服务", "多语言翻译", "ooder Team", AuthStatus.VERIFIED));
        addSkill(createSkill("skill-008", "知识库", "1.0.0", "知识管理", "知识检索、问答", "ooder Team", AuthStatus.PENDING));

        log.info("Initialized {} default skills", skillStore.size());
    }

    private SkillPackage createSkill(String id, String name, String version, String category, String description, String author, AuthStatus authStatus) {
        SkillPackage skill = new SkillPackage();
        skill.setSkillId(id);
        skill.setName(name);
        skill.setVersion(version);
        skill.setCategory(category);
        skill.setDescription(description);
        skill.setAuthor(author);
        skill.setAuthStatus(authStatus);
        skill.setIcon("ri-apps-line");
        skill.setTags(Arrays.asList(category, "AI"));
        skill.setDownloadUrl("/skills/" + id + "/download");
        skill.setChecksum(UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        skill.setDownloadCount((long) (Math.random() * 10000));
        skill.setInstallCount((long) (Math.random() * 5000));
        skill.setCreateTime(System.currentTimeMillis() - (long) (Math.random() * 31536000000L));
        skill.setUpdateTime(System.currentTimeMillis() - (long) (Math.random() * 2592000000L));
        skill.setStatus("published");
        return skill;
    }

    private void addSkill(SkillPackage skill) {
        skillStore.put(skill.getSkillId(), skill);
    }

    @Override
    public List<SkillPackage> getSkillList(String category, int page, int pageSize) {
        log.info("Getting skill list: category={}, page={}, size={}", category, page, pageSize);

        List<SkillPackage> filtered = new ArrayList<SkillPackage>();
        for (SkillPackage skill : skillStore.values()) {
            if (category == null || category.isEmpty() || category.equals(skill.getCategory())) {
                filtered.add(skill);
            }
        }

        filtered.sort((a, b) -> Long.compare(b.getDownloadCount(), a.getDownloadCount()));

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, filtered.size());

        if (start >= filtered.size()) {
            return new ArrayList<SkillPackage>();
        }

        return filtered.subList(start, end);
    }

    @Override
    public List<SkillPackage> searchSkills(String keyword, String category, int page, int pageSize) {
        log.info("Searching skills: keyword={}, category={}", keyword, category);

        String lowerKeyword = keyword != null ? keyword.toLowerCase() : "";

        List<SkillPackage> filtered = new ArrayList<SkillPackage>();
        for (SkillPackage skill : skillStore.values()) {
            boolean matches = true;

            if (keyword != null && !keyword.isEmpty()) {
                matches = (skill.getName().toLowerCase().contains(lowerKeyword) ||
                        skill.getDescription().toLowerCase().contains(lowerKeyword) ||
                        (skill.getTags() != null && skill.getTags().stream().anyMatch(t -> t.toLowerCase().contains(lowerKeyword))));
            }

            if (category != null && !category.isEmpty()) {
                matches = matches && category.equals(skill.getCategory());
            }

            if (matches) {
                filtered.add(skill);
            }
        }

        filtered.sort((a, b) -> Long.compare(b.getDownloadCount(), a.getDownloadCount()));

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, filtered.size());

        if (start >= filtered.size()) {
            return new ArrayList<SkillPackage>();
        }

        return filtered.subList(start, end);
    }

    @Override
    public SkillPackage getSkillDetail(String skillId) {
        log.info("Getting skill detail: {}", skillId);
        return skillStore.get(skillId);
    }

    @Override
    public Map<String, Object> installSkill(String skillId, String userId, Map<String, Object> options) {
        log.info("Installing skill: {} for user: {}", skillId, userId);

        Map<String, Object> result = new HashMap<String, Object>();

        SkillPackage skill = skillStore.get(skillId);
        if (skill == null) {
            result.put("success", false);
            result.put("message", "技能不存在");
            return result;
        }

        Set<String> installed = userInstalledSkills.computeIfAbsent(userId, k -> new HashSet<String>());
        if (installed.contains(skillId)) {
            result.put("success", false);
            result.put("message", "技能已安装");
            return result;
        }

        installed.add(skillId);
        skill.setInstallCount(skill.getInstallCount() + 1);

        result.put("success", true);
        result.put("message", "安装成功");
        result.put("skillId", skillId);
        result.put("installTime", System.currentTimeMillis());

        return result;
    }

    @Override
    public boolean uninstallSkill(String skillId, String userId) {
        log.info("Uninstalling skill: {} for user: {}", skillId, userId);

        Set<String> installed = userInstalledSkills.get(userId);
        if (installed == null || !installed.contains(skillId)) {
            return false;
        }

        installed.remove(skillId);

        SkillPackage skill = skillStore.get(skillId);
        if (skill != null) {
            skill.setInstallCount(Math.max(0, skill.getInstallCount() - 1));
        }

        return true;
    }

    @Override
    public Map<String, Object> updateSkill(String skillId, String userId) {
        log.info("Updating skill: {} for user: {}", skillId, userId);

        Map<String, Object> result = new HashMap<String, Object>();

        SkillPackage skill = skillStore.get(skillId);
        if (skill == null) {
            result.put("success", false);
            result.put("message", "技能不存在");
            return result;
        }

        Set<String> installed = userInstalledSkills.get(userId);
        if (installed == null || !installed.contains(skillId)) {
            result.put("success", false);
            result.put("message", "技能未安装");
            return result;
        }

        result.put("success", true);
        result.put("message", "更新成功");
        result.put("skillId", skillId);
        result.put("version", skill.getVersion());
        result.put("updateTime", System.currentTimeMillis());

        return result;
    }

    @Override
    public AuthStatus getAuthStatus(String skillId) {
        log.info("Getting auth status for skill: {}", skillId);

        SkillPackage skill = skillStore.get(skillId);
        if (skill == null) {
            return AuthStatus.UNVERIFIED;
        }

        return skill.getAuthStatus();
    }

    @Override
    public int getSkillCount(String category) {
        if (category == null || category.isEmpty()) {
            return skillStore.size();
        }

        int count = 0;
        for (SkillPackage skill : skillStore.values()) {
            if (category.equals(skill.getCategory())) {
                count++;
            }
        }
        return count;
    }

    @Override
    public List<Map<String, Object>> getCategories() {
        log.info("Getting skill categories");

        Map<String, Integer> categoryCount = new HashMap<String, Integer>();
        for (SkillPackage skill : skillStore.values()) {
            String cat = skill.getCategory();
            categoryCount.put(cat, categoryCount.getOrDefault(cat, 0) + 1);
        }

        List<Map<String, Object>> categories = new ArrayList<Map<String, Object>>();
        for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
            Map<String, Object> cat = new HashMap<String, Object>();
            cat.put("name", entry.getKey());
            cat.put("count", entry.getValue());
            categories.add(cat);
        }

        return categories;
    }
}
