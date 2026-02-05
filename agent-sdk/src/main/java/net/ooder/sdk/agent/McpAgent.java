package net.ooder.sdk.agent;

import net.ooder.sdk.network.packet.AuthPacket;
import net.ooder.sdk.network.RouteAgentManager;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface McpAgent extends RouteAgent {
    
    /**
     * 注册 RouteAgent
     */
    CompletableFuture<Boolean> registerRouteAgent(String routeAgentId, String routeAgentName, Map<String, Object> capabilities);
    
    /**
     * 注销 RouteAgent
     */
    CompletableFuture<Boolean> deregisterRouteAgent(String routeAgentId);
    
    /**
     * 处理 RouteAgent 认证请求
     */
    CompletableFuture<AuthPacket> handleRouteAgentAuthRequest(AuthPacket authPacket);
    
    /**
     * 获取所有注册的 RouteAgent
     */
    Map<String, RouteAgentManager> getRegisteredRouteAgents();
    
    /**
     * 向所有 RouteAgent 广播消息
     */
    CompletableFuture<Boolean> broadcastToRouteAgents(Object message);
    
    /**
     * 协调 RouteAgent 之间的任务分配
     */
    CompletableFuture<Boolean> coordinateTaskAssignment(String taskId, List<String> routeAgentIds);
    
    /**
     * 监控 RouteAgent 状态
     */
    CompletableFuture<Map<String, String>> monitorRouteAgentStatus();
    
    /**
     * 处理 RouteAgent 状态更新
     */
    void handleRouteAgentStatusUpdate(String routeAgentId, String status);
    
    /**
     * 负载均衡 - 选择合适的 RouteAgent
     */
    CompletableFuture<String> selectRouteAgentForTask(Map<String, Object> taskRequirements);
    
    /**
     * 全局配置管理
     */
    CompletableFuture<Boolean> updateGlobalConfig(Map<String, Object> config);
    
    /**
     * 获取全局配置
     */
    Map<String, Object> getGlobalConfig();
    
    /**
     * 开启监听频道
     */
    CompletableFuture<Boolean> startListeningChannels();
    
    /**
     * 关闭监听频道
     */
    CompletableFuture<Boolean> stopListeningChannels();
    
    /**
     * 处理 Agent 入网请求
     */
    CompletableFuture<Boolean> handleAgentJoinRequest(String agentId, String agentName, String agentType, Map<String, Object> capabilities);
    
    /**
     * 签发令牌和密钥
     */
    CompletableFuture<Map<String, String>> issueTokens(String agentId);
}
