package net.ooder.sdk.skill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 技能管理器，负责技能的注册、执行和生命周期管理
 */
public class SkillManager {
    
    private static final Logger log = LoggerFactory.getLogger(SkillManager.class);
    private static SkillManager instance;
    
    // 技能注册表
    private final Map<String, Skill> skills = new ConcurrentHashMap<>();
    
    // 技能执行器
    private final SkillExecutor skillExecutor;
    
    private SkillManager() {
        this.skillExecutor = new SkillExecutor();
    }
    
    /**
     * 获取技能管理器实例
     * @return 技能管理器实例
     */
    public static synchronized SkillManager getInstance() {
        if (instance == null) {
            instance = new SkillManager();
        }
        return instance;
    }
    
    /**
     * 注册技能
     * @param skill 技能实例
     */
    public void registerSkill(Skill skill) {
        if (skill == null) {
            log.error("Cannot register null skill");
            return;
        }
        
        String skillId = skill.getSkillId();
        if (skillId == null || skillId.isEmpty()) {
            log.error("Skill must have a valid ID");
            return;
        }
        
        if (skills.containsKey(skillId)) {
            log.warn("Skill already registered: {}", skillId);
            return;
        }
        
        try {
            skill.initialize();
            skills.put(skillId, skill);
            log.info("Skill registered successfully: {}", skillId);
        } catch (Exception e) {
            log.error("Failed to register skill: {}", skillId, e);
        }
    }
    
    /**
     * 注销技能
     * @param skillId 技能ID
     */
    public void unregisterSkill(String skillId) {
        if (skillId == null || skillId.isEmpty()) {
            log.error("Skill ID cannot be empty");
            return;
        }
        
        Skill skill = skills.remove(skillId);
        if (skill != null) {
            try {
                skill.destroy();
                log.info("Skill unregistered successfully: {}", skillId);
            } catch (Exception e) {
                log.error("Failed to unregister skill: {}", skillId, e);
            }
        } else {
            log.warn("Skill not found: {}", skillId);
        }
    }
    
    /**
     * 获取技能
     * @param skillId 技能ID
     * @return 技能实例
     */
    public Skill getSkill(String skillId) {
        return skills.get(skillId);
    }
    
    /**
     * 获取所有技能
     * @return 技能映射
     */
    public Map<String, Skill> getAllSkills() {
        return new HashMap<>(skills);
    }
    
    /**
     * 执行技能
     * @param skillId 技能ID
     * @param params 执行参数
     * @return 执行结果
     */
    public SkillResult executeSkill(String skillId, Map<String, Object> params) {
        Skill skill = getSkill(skillId);
        if (skill == null) {
            log.error("Skill not found: {}", skillId);
            return SkillResult.failure("Skill not found: " + skillId, null);
        }
        
        return skillExecutor.execute(skill, params);
    }
    
    /**
     * 获取技能状态
     * @param skillId 技能ID
     * @return 技能状态
     */
    public SkillStatus getSkillStatus(String skillId) {
        Skill skill = getSkill(skillId);
        if (skill == null) {
            return null;
        }
        return skill.getStatus();
    }
    
    /**
     * 关闭技能管理器
     */
    public void shutdown() {
        log.info("Shutting down SkillManager...");
        
        // 注销所有技能
        for (String skillId : skills.keySet()) {
            unregisterSkill(skillId);
        }
        
        // 关闭执行器
        skillExecutor.shutdown();
        
        log.info("SkillManager shut down successfully");
    }
}