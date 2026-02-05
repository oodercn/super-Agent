package net.ooder.sdk.skill;

import java.util.Map;

/**
 * 技能接口，定义技能的基本方法
 */
public interface Skill {
    
    /**
     * 获取技能ID
     * @return 技能唯一标识符
     */
    String getSkillId();
    
    /**
     * 获取技能名称
     * @return 技能名称
     */
    String getName();
    
    /**
     * 获取技能描述
     * @return 技能描述信息
     */
    String getDescription();
    
    /**
     * 获取技能参数定义
     * @return 参数定义映射，key为参数名，value为参数描述
     */
    Map<String, String> getParameters();
    
    /**
     * 执行技能
     * @param params 执行参数
     * @return 执行结果
     */
    SkillResult execute(Map<String, Object> params);
    
    /**
     * 初始化技能
     */
    void initialize();
    
    /**
     * 销毁技能
     */
    void destroy();
    
    /**
     * 获取技能状态
     * @return 技能状态
     */
    SkillStatus getStatus();
}