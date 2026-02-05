package net.ooder.sdk.persistence;

import java.util.List;

/**
 * MCP持久化客户端接口，用于MCP代理查询跨路由代理的资源
 */
public interface McpPersistenceClient {
    // ==================== 路由代理和技能查询 ====================
    
    /**
     * 获取所有路由代理及其技能
     * @return 路由代理树列表
     */
    List<RouteAgentTree> getRouteAgentsAndSkills();
    
    /**
     * 获取特定路由代理的技能树
     * @param routeAgentId 路由代理ID
     * @return 技能树节点
     */
    SkillTreeNode getSkillTree(String routeAgentId);
    
    /**
     * 通过路由路径模式查找技能
     * @param routePath 路由路径模式，支持通配符
     * @return 技能树节点列表
     */
    List<SkillTreeNode> findSkillsByRoutePath(String routePath);
    
    /**
     * 获取技能依赖关系树
     * @param skillId 技能ID
     * @return 技能依赖关系树
     */
    SkillTreeNode getSkillDependencies(String skillId);
    
    /**
     * 获取路由代理层次结构
     * @return 路由代理层次结构
     */
    RouteAgentTree getRouteAgentHierarchy();
    
    /**
     * 查找特定类型的路由代理
     * @param agentType 代理类型
     * @return 路由代理树列表
     */
    List<RouteAgentTree> findRouteAgentsByType(String agentType);
    
    /**
     * 获取路由代理统计信息
     * @return 路由代理统计信息
     */
    RouteAgentStats getRouteAgentStats();
}
