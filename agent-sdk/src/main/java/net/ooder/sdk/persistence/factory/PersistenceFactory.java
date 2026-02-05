package net.ooder.sdk.persistence.factory;

import net.ooder.sdk.persistence.PersistenceClient;
import net.ooder.sdk.persistence.EndPersistenceClient;
import net.ooder.sdk.persistence.McpPersistenceClient;
import net.ooder.sdk.persistence.RoutePersistenceClient;
import net.ooder.sdk.system.factory.Factory;

/**
 * 持久化工厂，用于创建不同类型的持久化客户端
 */
public interface PersistenceFactory extends Factory {
    
    /**
     * 获取统一持久化客户端
     * @return 持久化客户端实例
     */
    PersistenceClient getPersistenceClient();
    
    /**
     * 获取MCP持久化客户端
     * @return MCP持久化客户端实例
     */
    McpPersistenceClient getMcpPersistenceClient();
    
    /**
     * 获取路由持久化客户端
     * @param routeAgentId 路由代理ID
     * @return 路由持久化客户端实例
     */
    RoutePersistenceClient getRoutePersistenceClient(String routeAgentId);
    
    /**
     * 获取终端持久化客户端
     * @param endAgentId 终端代理ID
     * @return 终端持久化客户端实例
     */
    EndPersistenceClient getEndPersistenceClient(String endAgentId);
}
