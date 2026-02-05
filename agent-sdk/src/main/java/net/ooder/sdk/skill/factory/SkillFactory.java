package net.ooder.sdk.skill.factory;

import net.ooder.sdk.skill.Skill;
import net.ooder.sdk.skill.SkillExecutor;
import net.ooder.sdk.system.factory.Factory;

import java.util.Map;

/**
 * 技能工厂，用于创建技能实例和执行器
 */
public interface SkillFactory extends Factory {
    
    /**
     * 创建技能实例
     * @param skillId 技能ID
     * @param skillName 技能名称
     * @param description 技能描述
     * @param parameters 技能参数
     * @param skillCode 技能代码
     * @return 技能实例
     */
    Skill createSkill(String skillId, String skillName, String description, Map<String, String> parameters, String skillCode);
    
    /**
     * 创建技能执行器
     * @return 技能执行器
     */
    SkillExecutor createSkillExecutor();
    
    /**
     * 注册技能类型
     * @param skillType 技能类型
     * @param skillClass 技能类
     */
    void registerSkillType(String skillType, Class<? extends Skill> skillClass);
}
