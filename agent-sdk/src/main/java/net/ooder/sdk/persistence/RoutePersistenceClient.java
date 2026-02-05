package net.ooder.sdk.persistence;

import net.ooder.sdk.skill.SkillStatus;

import java.util.List;

/**
 * 路由持久化客户端接口，用于路由代理管理自己的资源
 */
public interface RoutePersistenceClient {
    // ==================== 技能管理 ====================
    
    /**
     * 获取当前路由代理的技能树
     * @return 技能树节点
     */
    SkillTreeNode getSkillTree();
    
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
    
    /**
     * 获取所有技能状态
     * @return 技能状态映射
     */
    List<SkillStatusInfo> getAllSkillStatuses();
    
    // ==================== 路由管理 ====================
    
    /**
     * 获取路由代理信息
     * @return 路由代理树
     */
    RouteAgentTree getRouteAgentInfo();
    
    /**
     * 更新路由代理信息
     * @param routeAgentTree 路由代理树
     * @return 是否更新成功
     */
    boolean updateRouteAgentInfo(RouteAgentTree routeAgentTree);
    
    /**
     * 获取路由代理的子代理
     * @return 子代理列表
     */
    List<RouteAgentTree> getChildAgents();
    
    // ==================== 资源管理 ====================
    
    /**
     * 获取路由代理的资源使用情况
     * @return 资源使用情况
     */
    ResourceUsage getResourceUsage();
    
    /**
     * 获取路由代理的技能统计信息
     * @return 技能统计信息
     */
    SkillStats getSkillStats();
}
