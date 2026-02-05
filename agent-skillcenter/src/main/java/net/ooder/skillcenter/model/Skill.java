package net.ooder.skillcenter.model;

import java.util.Map;

/**
 * 技能接口，定义技能的基本行为
 */
public interface Skill {
    /**
     * 获取技能ID
     * @return 技能唯一标识符
     */
    String getId();
    
    /**
     * 获取技能名称
     * @return 技能名称
     */
    String getName();
    
    /**
     * 获取技能描述
     * @return 技能描述
     */
    String getDescription();
    
    /**
     * 执行技能
     * @param context 执行上下文
     * @return 执行结果
     * @throws SkillException 技能执行异常
     */
    SkillResult execute(SkillContext context) throws SkillException;
    
    /**
     * 检查技能是否可用
     * @return true如果技能可用，false否则
     */
    boolean isAvailable();
    
    /**
     * 获取技能所需的参数
     * @return 参数描述映射
     */
    Map<String, SkillParam> getParams();
}
