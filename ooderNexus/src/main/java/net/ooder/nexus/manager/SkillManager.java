package net.ooder.nexus.manager;

import net.ooder.nexus.model.Skill;
import net.ooder.nexus.model.SkillContext;
import net.ooder.nexus.model.SkillException;
import net.ooder.nexus.model.SkillParam;
import net.ooder.nexus.model.SkillResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SkillManager {
    private static Map<String, SkillManager> instances = new HashMap<>();
    
    private Map<String, Skill> skillMap;
    private Map<String, List<Skill>> categoryMap;
    private ExecutorService executorService;

    private SkillManager() {
        this.skillMap = new HashMap<>();
        this.categoryMap = new HashMap<>();
        this.executorService = Executors.newFixedThreadPool(10);
        this.loadBuiltInSkills();
    }

    public static synchronized SkillManager getInstance() {
        return getInstance("default");
    }

    public static synchronized SkillManager getInstance(String name) {
        if (!instances.containsKey(name)) {
            instances.put(name, new SkillManager());
        }
        return instances.get(name);
    }

    public synchronized void registerSkill(Skill skill) {
        if (skill == null) {
            throw new IllegalArgumentException("Skill cannot be null");
        }

        String skillId = skill.getId();
        if (skillId == null || skillId.isEmpty()) {
            throw new IllegalArgumentException("Skill ID cannot be null or empty");
        }

        String category = skill.getClass().getPackage().getName();
        
        skillMap.put(skillId, skill);
        categoryMap.computeIfAbsent(category, k -> new ArrayList<>()).add(skill);
    }

    public synchronized void unregisterSkill(String skillId) {
        if (skillId == null || skillId.isEmpty()) {
            throw new IllegalArgumentException("Skill ID cannot be null or empty");
        }

        Skill skill = skillMap.remove(skillId);
        if (skill != null) {
            String category = skill.getClass().getPackage().getName();
            List<Skill> skills = categoryMap.get(category);
            if (skills != null) {
                skills.remove(skill);
                if (skills.isEmpty()) {
                    categoryMap.remove(category);
                }
            }
        }
    }

    public Skill getSkill(String skillId) {
        if (skillId == null || skillId.isEmpty()) {
            throw new IllegalArgumentException("Skill ID cannot be null or empty");
        }
        return skillMap.get(skillId);
    }

    public List<Skill> getAllSkills() {
        return new ArrayList<>(skillMap.values());
    }

    public List<Skill> getSkillsByCategory(String category) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        return new ArrayList<>(categoryMap.getOrDefault(category, new ArrayList<>()));
    }

    public List<Skill> findSkills(SkillCondition condition) {
        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }

        List<Skill> result = new ArrayList<>();
        for (Skill skill : skillMap.values()) {
            if (condition.match(skill)) {
                result.add(skill);
            }
        }
        return result;
    }

    public SkillResult executeSkill(String skillId, SkillContext context) throws SkillException {
        Skill skill = getSkill(skillId);
        if (skill != null && skill.isAvailable()) {
            try {
                return skill.execute(context);
            } catch (SkillException e) {
                throw e;
            } catch (Exception e) {
                throw new SkillException(skillId, "Local skill execution failed: " + e.getMessage(), 
                                         SkillException.ErrorCode.EXECUTION_EXCEPTION, e);
            }
        }
        
        throw new SkillException(skillId, "Skill not found or unavailable", 
                                     SkillException.ErrorCode.SKILL_NOT_FOUND);
    }

    public void executeSkillAsync(String skillId, SkillContext context, SkillCallback callback) {
        executorService.submit(() -> {
            try {
                SkillResult result = executeSkill(skillId, context);
                callback.onSuccess(result);
            } catch (SkillException e) {
                callback.onFailure(e);
            }
        });
    }

    private void loadBuiltInSkills() {
        addDefaultSkills();
        loadSkillsFromJson();
        System.out.println("Loaded " + skillMap.size() + " built-in skills");
    }

    private void addDefaultSkills() {
        SkillInfo[] defaultSkills = {
            new SkillInfo("skill-weather", "天气查询技能", "查询实时天气信息", "utilities"),
            new SkillInfo("skill-stock", "股票查询技能", "查询股票实时行情", "finance"),
            new SkillInfo("skill-translate", "翻译技能", "多语言翻译服务", "utilities"),
            new SkillInfo("skill-calculator", "计算器技能", "数学计算工具", "utilities"),
            new SkillInfo("skill-notes", "笔记技能", "创建和管理笔记", "productivity"),
            new SkillInfo("skill-reminder", "提醒技能", "设置和管理提醒", "productivity"),
            new SkillInfo("skill-calendar", "日历技能", "管理日程安排", "productivity"),
            new SkillInfo("skill-email", "邮件技能", "发送和管理邮件", "communication"),
            new SkillInfo("skill-chat", "聊天技能", "实时聊天功能", "communication"),
            new SkillInfo("skill-image", "图像处理技能", "图像处理和编辑", "media")
        };
        
        for (SkillInfo skill : defaultSkills) {
            skill.setStatus("active");
            registerSkill(skill);
        }
        
        System.out.println("Added " + defaultSkills.length + " default skills");
    }

    private void loadSkillsFromJson() {
        try {
            // 这里可以实现从 skills-data.json 文件加载技能数据的逻辑
            // 由于涉及文件读取和JSON解析，需要添加相应的依赖
            // 暂时保留此方法，作为后续扩展
            System.out.println("Skills from JSON will be loaded here in future");
        } catch (Exception e) {
            System.err.println("Failed to load skills from JSON: " + e.getMessage());
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public interface SkillCondition {
        boolean match(Skill skill);
    }

    public interface SkillCallback {
        void onSuccess(SkillResult result);
        void onFailure(SkillException exception);
    }

    public static class SkillInfo implements Skill {
        private String id;
        private String name;
        private String description;
        private String category;
        private String status;
        private Date createdAt;
        private boolean available;
        private Map<String, SkillParam> params;

        public SkillInfo() {
            this.params = new HashMap<>();
            this.available = true;
            this.status = "active";
            this.createdAt = new Date();
        }

        public SkillInfo(String id, String name, String description, String category) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.category = category;
            this.params = new HashMap<>();
            this.available = true;
            this.status = "active";
            this.createdAt = new Date();
        }

        @Override
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
            this.available = "active".equals(status);
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        @Override
        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        @Override
        public Map<String, SkillParam> getParams() {
            return params;
        }

        public void setParams(Map<String, SkillParam> params) {
            this.params = params;
        }

        @Override
        public SkillResult execute(SkillContext context) throws SkillException {
            throw new UnsupportedOperationException("SkillInfo is for management only");
        }
    }
}
