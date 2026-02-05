package net.ooder.sdk.agent;

import net.ooder.sdk.network.packet.AuthPacket;
import net.ooder.sdk.network.packet.TaskPacket;
import net.ooder.sdk.network.packet.RoutePacket;
import net.ooder.sdk.agent.scene.SceneDefinition;
import net.ooder.sdk.agent.scene.SceneMember;
import net.ooder.sdk.llm.LlmConfig;
import net.ooder.sdk.llm.LlmRequest;
import net.ooder.sdk.llm.LlmResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface RouteAgent extends EndAgent {
    
    /**
     * 向 MCP Agent 注册
     */
    CompletableFuture<Boolean> register(String targetMcpId);
    
    /**
     * 向 MCP Agent 注销
     */
    CompletableFuture<Boolean> deregister();
    
    /**
     * 认证 RouteAgent
     */
    CompletableFuture<Boolean> authenticate();
    
    /**
     * 处理认证响应
     */
    void handleAuthResponse(AuthPacket packet);
    
    /**
     * 查询 EndAgent 列表
     */
    CompletableFuture<List<RoutePacket.RouteEntry>> queryEndAgents();
    
    /**
     * 转发任务到 EndAgent
     */
    CompletableFuture<net.ooder.sdk.network.udp.SendResult> forwardTask(TaskPacket taskPacket, String endAgentId);
    
    /**
     * 发送路由状态更新
     */
    CompletableFuture<net.ooder.sdk.network.udp.SendResult> sendRouteUpdate(List<RoutePacket.RouteEntry> endAgents);
    
    /**
     * 创建新场景
     */
    CompletableFuture<Boolean> createScene(SceneDefinition sceneDefinition);
    
    /**
     * 获取场景定义
     */
    SceneDefinition getScene(String sceneId);
    
    /**
     * 删除场景
     */
    CompletableFuture<Boolean> deleteScene(String sceneId);
    
    /**
     * 向场景添加成员
     */
    CompletableFuture<Boolean> addSceneMember(String sceneId, SceneMember member);
    
    /**
     * 从场景移除成员
     */
    CompletableFuture<Boolean> removeSceneMember(String sceneId, String agentId);
    
    /**
     * 获取场景中的成员
     */
    Map<String, SceneMember> getSceneMembers(String sceneId);
    
    /**
     * 验证成员角色是否符合场景要求
     */
    boolean validateSceneMember(String sceneId, SceneMember member);
    
    /**
     * 检查场景是否已满足所有必填角色要求
     */
    boolean isSceneReady(String sceneId);
    
    /**
     * 初始化 LLM Service
     */
    void initLlmService(LlmConfig config);
    
    /**
     * 检查 LLM Service 连接状态
     */
    boolean isLlmServiceConnected();
    
    /**
     * 发送代码生成请求到 LLM Service
     */
    CompletableFuture<LlmResponse> generateCode(LlmRequest request);
    
    /**
     * 处理 EndAgent 加入请求
     */
    CompletableFuture<Boolean> handleEndAgentJoinRequest(String sceneId, SceneMember member);
    
    /**
     * 获取 MCP Agent ID
     */
    String getMcpAgentId();
    
    /**
     * 获取当前注册状态
     */
    boolean isRegistered();
    
    /**
     * 获取会话密钥
     */
    String getSessionKey();
    
    /**
     * 检查是否存在 MCP Agent
     */
    boolean hasMcpAgent();
    
    /**
     * 升级为 RouteMcp 服务
     */
    CompletableFuture<Boolean> upgradeToRouteMcp();
    
    /**
     * 检查是否为 RouteMcp 服务
     */
    boolean isRouteMcp();
    
    /**
     * 降级为 Route Agent
     */
    CompletableFuture<Boolean> downgradeToRoute();
    
    /**
     * 检测网络中是否存在确定性的指定的 MCP
     */
    boolean detectDeterministicMcp();
}
