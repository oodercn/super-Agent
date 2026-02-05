package net.ooder.sdk.persistence;

import net.ooder.sdk.skill.SkillStatus;

/**
 * 终端持久化客户端接口，用于终端代理访问有限的资源
 */
public interface EndPersistenceClient {
    // ==================== 技能管理 ====================
    
    /**
     * 保存技能状态
     * @param skillId 技能ID
     * @param status 技能状态
     * @return 是否保存成功
     */
    boolean saveSkillStatus(String skillId, SkillStatus status);
    
    /**
     * 加载技能状态
     * @param skillId 技能ID
     * @return 技能状态
     */
    SkillStatus loadSkillStatus(String skillId);
    
    // ==================== 配置管理 ====================
    
    /**
     * 保存终端代理配置
     * @param config 代理配置
     * @return 是否保存成功
     */
    boolean saveAgentConfig(Object config);
    
    /**
     * 加载终端代理配置
     * @param configClass 配置类
     * @param <T> 配置类型
     * @return 代理配置
     */
    <T> T loadAgentConfig(Class<T> configClass);
    
    // ==================== 资源管理 ====================
    
    /**
     * 保存终端代理的资源使用情况
     * @param resourceUsage 资源使用情况
     * @return 是否保存成功
     */
    boolean saveResourceUsage(ResourceUsage resourceUsage);
    
    /**
     * 加载终端代理的资源使用情况
     * @return 资源使用情况
     */
    ResourceUsage loadResourceUsage();
}
