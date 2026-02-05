package net.ooder.skillcenter.manager;

import net.ooder.skillcenter.execution.SkillExecutorEngine;
import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillParam;
import net.ooder.skillcenter.model.SkillResult;
import net.ooder.skillcenter.p2p.P2PSkillExecutor;
import net.ooder.skillcenter.p2p.P2PNodeManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 技能管理器，负责技能的注册、发现和执行
 */
public class SkillManager {
    // 单例实例映射
    private static Map<String, SkillManager> instances = new HashMap<>();
    
    // 技能映射，key为技能ID，value为技能实例
    private Map<String, Skill> skillMap;
    
    // 技能分类映射，key为分类名称，value为技能列表
    private Map<String, List<Skill>> categoryMap;
    
    // 线程池，用于异步执行技能
    private ExecutorService executorService;
    
    // 技能执行引擎
    private SkillExecutorEngine executorEngine;

    /**
     * 私有构造方法，初始化技能管理器
     */
    private SkillManager() {
        this.skillMap = new HashMap<>();
        this.categoryMap = new HashMap<>();
        this.executorService = Executors.newFixedThreadPool(10);
        this.executorEngine = SkillExecutorEngine.getInstance();
        this.loadBuiltInSkills();
    }

    /**
     * 获取默认名称的技能管理器实例
     * @return 技能管理器实例
     */
    public static synchronized SkillManager getInstance() {
        return getInstance("default");
    }

    /**
     * 获取指定名称的技能管理器实例
     * @param name 实例名称
     * @return 技能管理器实例
     */
    public static synchronized SkillManager getInstance(String name) {
        if (!instances.containsKey(name)) {
            instances.put(name, new SkillManager());
        }
        return instances.get(name);
    }

    /**
     * 注册技能到管理器中
     * @param skill 技能实例
     */
    public synchronized void registerSkill(Skill skill) {
        if (skill == null) {
            throw new IllegalArgumentException("Skill cannot be null");
        }

        String skillId = skill.getId();
        if (skillId == null || skillId.isEmpty()) {
            throw new IllegalArgumentException("Skill ID cannot be null or empty");
        }

        // 获取技能分类（使用包名作为分类）
        String category = skill.getClass().getPackage().getName();
        
        // 添加到技能映射
        skillMap.put(skillId, skill);
        
        // 添加到分类映射
        categoryMap.computeIfAbsent(category, k -> new ArrayList<>()).add(skill);
    }

    /**
     * 卸载指定ID的技能
     * @param skillId 技能ID
     */
    public synchronized void unregisterSkill(String skillId) {
        if (skillId == null || skillId.isEmpty()) {
            throw new IllegalArgumentException("Skill ID cannot be null or empty");
        }

        Skill skill = skillMap.remove(skillId);
        if (skill != null) {
            // 从分类映射中移除
            String category = skill.getClass().getPackage().getName();
            List<Skill> skills = categoryMap.get(category);
            if (skills != null) {
                skills.remove(skill);
                // 如果分类为空，移除分类
                if (skills.isEmpty()) {
                    categoryMap.remove(category);
                }
            }
        }
    }

    /**
     * 根据ID获取技能实例
     * @param skillId 技能ID
     * @return 技能实例，不存在则返回null
     */
    public Skill getSkill(String skillId) {
        if (skillId == null || skillId.isEmpty()) {
            throw new IllegalArgumentException("Skill ID cannot be null or empty");
        }
        return skillMap.get(skillId);
    }

    /**
     * 获取所有已注册的技能
     * @return 技能列表
     */
    public List<Skill> getAllSkills() {
        return new ArrayList<>(skillMap.values());
    }

    /**
     * 根据分类获取技能列表
     * @param category 分类名称
     * @return 技能列表
     */
    public List<Skill> getSkillsByCategory(String category) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        return new ArrayList<>(categoryMap.getOrDefault(category, new ArrayList<>()));
    }

    /**
     * 根据条件查询技能
     * @param condition 过滤条件
     * @return 匹配的技能列表
     */
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

    /**
     * 同步执行指定ID的技能
     * @param skillId 技能ID
     * @param context 执行上下文
     * @return 执行结果
     * @throws SkillException 技能执行异常
     */
    public SkillResult executeSkill(String skillId, SkillContext context) throws SkillException {
        // 首先尝试在本地执行技能
        Skill skill = getSkill(skillId);
        if (skill != null && skill.isAvailable()) {
            try {
                return executorEngine.executeSkill(skill, context);
            } catch (SkillException e) {
                throw e;
            } catch (Exception e) {
                throw new SkillException(skillId, "Local skill execution failed: " + e.getMessage(), 
                                         SkillException.ErrorCode.EXECUTION_EXCEPTION, e);
            }
        }
        
        // 本地没有找到技能或技能不可用，尝试在P2P网络中执行
        try {
            System.out.println("Local skill not found or unavailable, trying P2P network...");
            P2PSkillExecutor p2pExecutor = P2PSkillExecutor.getInstance();
            return p2pExecutor.executeSkill(skillId, context);
        } catch (SkillException e) {
            throw e;
        } catch (Exception e) {
            throw new SkillException(skillId, "P2P skill execution failed: " + e.getMessage(), 
                                     SkillException.ErrorCode.EXECUTION_EXCEPTION, e);
        }
    }

    /**
     * 异步执行指定ID的技能
     * @param skillId 技能ID
     * @param context 执行上下文
     * @param callback 回调函数
     */
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

    /**
     * 加载内置技能
     */
    private void loadBuiltInSkills() {
        // 注册内置技能
        registerSkill(new net.ooder.skillcenter.model.impl.CodeGenerationSkill());
        registerSkill(new net.ooder.skillcenter.model.impl.LocalDeploymentSkill());
        registerSkill(new net.ooder.skillcenter.model.impl.TextToUppercaseSkill());
        
        // 添加默认技能数据
        addDefaultSkills();
        
        System.out.println("Loaded " + skillMap.size() + " built-in skills");
        
        // 启动P2P服务
        startP2PService();
    }
    
    /**
     * 添加默认技能数据
     */
    private void addDefaultSkills() {
        // 创建默认技能数据
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
        
        // 注册默认技能
        for (SkillInfo skill : defaultSkills) {
            skill.setStatus("active");
            registerSkill(skill);
        }
        
        System.out.println("Added " + defaultSkills.length + " default skills");
    }
    
    /**
     * 启动P2P服务
     */
    private void startP2PService() {
        try {
            P2PNodeManager p2pManager = P2PNodeManager.getInstance();
            p2pManager.start();
            System.out.println("P2P service started successfully");
        } catch (Exception e) {
            System.err.println("Failed to start P2P service: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 关闭技能管理器，释放资源
     */
    public void shutdown() {
        executorService.shutdown();
        
        // 关闭执行引擎
        executorEngine.shutdown();
        
        // 关闭P2P服务
        try {
            P2PNodeManager p2pManager = P2PNodeManager.getInstance();
            p2pManager.stop();
            
            P2PSkillExecutor p2pExecutor = P2PSkillExecutor.getInstance();
            p2pExecutor.shutdown();
            
            System.out.println("P2P services shutdown successfully");
        } catch (Exception e) {
            System.err.println("Failed to shutdown P2P services: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 添加技能
     * @param skill 技能信息
     * @return 添加的技能
     */
    public synchronized Skill addSkill(Skill skill) {
        if (skill == null) {
            throw new IllegalArgumentException("Skill cannot be null");
        }
        
        // 使用SkillInfo来处理添加操作
        SkillManager.SkillInfo skillInfo = null;
        if (skill instanceof SkillManager.SkillInfo) {
            skillInfo = (SkillManager.SkillInfo) skill;
        } else {
            // 将普通Skill转换为SkillInfo
            skillInfo = new SkillManager.SkillInfo();
            skillInfo.setId(skill.getId() != null ? skill.getId() : "skill-" + System.currentTimeMillis());
            skillInfo.setName(skill.getName());
            skillInfo.setDescription(skill.getDescription());
            skillInfo.setParams(skill.getParams());
            skillInfo.setAvailable(skill.isAvailable());
            skillInfo.setCategory("general");
            skillInfo.setStatus("pending");
            skillInfo.setCreatedAt(new java.util.Date());
        }
        
        // 确保ID和创建时间
        if (skillInfo.getId() == null || skillInfo.getId().isEmpty()) {
            skillInfo.setId("skill-" + System.currentTimeMillis());
        }
        
        if (skillInfo.getCreatedAt() == null) {
            skillInfo.setCreatedAt(new java.util.Date());
        }
        
        registerSkill(skillInfo);
        return skillInfo;
    }

    /**
     * 更新技能
     * @param skill 技能信息
     * @return 更新后的技能
     */
    public synchronized Skill updateSkill(Skill skill) {
        if (skill == null || skill.getId() == null) {
            throw new IllegalArgumentException("Skill or skill ID cannot be null");
        }
        
        if (!skillMap.containsKey(skill.getId())) {
            throw new IllegalArgumentException("Skill not found: " + skill.getId());
        }
        
        // 移除旧技能
        unregisterSkill(skill.getId());
        
        // 注册新技能
        registerSkill(skill);
        return skill;
    }

    /**
     * 删除技能
     * @param skillId 技能ID
     * @return 是否删除成功
     */
    public synchronized boolean deleteSkill(String skillId) {
        if (skillId == null || skillId.isEmpty()) {
            throw new IllegalArgumentException("Skill ID cannot be null or empty");
        }
        
        if (!skillMap.containsKey(skillId)) {
            return false;
        }
        
        unregisterSkill(skillId);
        return true;
    }

    /**
     * 审核技能
     * @param skillId 技能ID
     * @return 是否审核成功
     */
    public synchronized boolean approveSkill(String skillId) {
        if (skillId == null || skillId.isEmpty()) {
            throw new IllegalArgumentException("Skill ID cannot be null or empty");
        }
        
        Skill skill = skillMap.get(skillId);
        if (skill == null) {
            return false;
        }
        
        // 确保使用SkillInfo类来设置状态
        if (skill instanceof SkillManager.SkillInfo) {
            ((SkillManager.SkillInfo) skill).setStatus("active");
            return true;
        }
        
        return false;
    }

    /**
     * 拒绝技能
     * @param skillId 技能ID
     * @return 是否拒绝成功
     */
    public synchronized boolean rejectSkill(String skillId) {
        if (skillId == null || skillId.isEmpty()) {
            throw new IllegalArgumentException("Skill ID cannot be null or empty");
        }
        
        Skill skill = skillMap.get(skillId);
        if (skill == null) {
            return false;
        }
        
        // 确保使用SkillInfo类来设置状态
        if (skill instanceof SkillManager.SkillInfo) {
            ((SkillManager.SkillInfo) skill).setStatus("inactive");
            return true;
        }
        
        return false;
    }

    /**
     * 技能条件接口，用于过滤技能
     */
    public interface SkillCondition {
        /**
         * 判断技能是否匹配条件
         * @param skill 技能实例
         * @return true如果匹配，false否则
         */
        boolean match(Skill skill);
    }

    /**
     * 技能回调接口，用于异步执行技能
     */
    public interface SkillCallback {
        /**
         * 技能执行成功回调
         * @param result 执行结果
         */
        void onSuccess(SkillResult result);

        /**
         * 技能执行失败回调
         * @param exception 异常信息
         */
        void onFailure(SkillException exception);
    }

    /**
     * 技能信息类，用于管理技能的详细信息
     */
    public static class SkillInfo implements Skill {
        private String id;
        private String name;
        private String description;
        private String category;
        private String status;
        private Date createdAt;
        private boolean available;
        private Map<String, SkillParam> params;

        /**
         * 默认构造方法
         */
        public SkillInfo() {
            this.params = new HashMap<>();
            this.available = true;
            this.status = "active";
            this.createdAt = new Date();
        }

        /**
         * 构造方法
         * @param id 技能ID
         * @param name 技能名称
         * @param description 技能描述
         * @param category 技能分类
         */
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
