package net.ooder.nexus.management;

import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.network.packet.CommandPacket;

import java.util.List;
import java.util.Map;

/**
 * Nexus管理接口
 * 用于内部管理Nexus的核心功能
 */
public interface NexusManager {

    /**
     * 初始化管理器
     * @param sdk AgentSDK实例
     */
    void initialize(AgentSDK sdk);

    // ==================== LLM 交互管理 ====================

    /**
     * 注册LLM提供者
     * @param providerId LLM提供者ID
     * @param providerInfo LLM提供者信息
     */
    void registerLlmProvider(String providerId, Map<String, Object> providerInfo);

    /**
     * 移除LLM提供者
     * @param providerId LLM提供者ID
     */
    void removeLlmProvider(String providerId);

    /**
     * 获取所有LLM提供者
     * @return LLM提供者列表
     */
    Map<String, Map<String, Object>> getLlmProviders();

    /**
     * 发送LLM请求
     * @param providerId LLM提供者ID
     * @param requestData 请求数据
     * @return 响应数据
     */
    Map<String, Object> sendLlmRequest(String providerId, Map<String, Object> requestData);

    // ==================== 北上南下协议中枢 ====================

    /**
     * 注册协议处理器
     * @param commandType 命令类型
     * @param handler 命令处理器
     */
    void registerProtocolHandler(String commandType, ProtocolHandler handler);

    /**
     * 移除协议处理器
     * @param commandType 命令类型
     */
    void removeProtocolHandler(String commandType);

    /**
     * 获取所有协议处理器
     * @return 协议处理器映射
     */
    Map<String, ProtocolHandler> getProtocolHandlers();

    /**
     * 处理协议命令
     * @param commandType 命令类型
     * @param packet 命令包
     * @return 处理结果
     */
    boolean handleProtocolCommand(String commandType, CommandPacket packet);

    // ==================== Agent 内部网络能力管理 ====================

    /**
     * 注册网络节点
     * @param nodeId 节点ID
     * @param nodeInfo 节点信息
     */
    void registerNetworkNode(String nodeId, Map<String, Object> nodeInfo);

    /**
     * 移除网络节点
     * @param nodeId 节点ID
     */
    void removeNetworkNode(String nodeId);

    /**
     * 获取所有网络节点
     * @return 网络节点列表
     */
    Map<String, Map<String, Object>> getNetworkNodes();

    /**
     * 创建网络连接
     * @param sourceNodeId 源节点ID
     * @param targetNodeId 目标节点ID
     * @param connectionInfo 连接信息
     * @return 连接ID
     */
    String createNetworkConnection(String sourceNodeId, String targetNodeId, Map<String, Object> connectionInfo);

    /**
     * 断开网络连接
     * @param connectionId 连接ID
     */
    void disconnectNetworkConnection(String connectionId);

    /**
     * 获取网络拓扑
     * @return 网络拓扑信息
     */
    Map<String, Object> getNetworkTopology();

    /**
     * 注册能力
     * @param capabilityId 能力ID
     * @param capabilityInfo 能力信息
     */
    void registerCapability(String capabilityId, Map<String, Object> capabilityInfo);

    /**
     * 移除能力
     * @param capabilityId 能力ID
     */
    void removeCapability(String capabilityId);

    /**
     * 获取所有能力
     * @return 能力列表
     */
    Map<String, Map<String, Object>> getCapabilities();

    /**
     * 调用能力
     * @param capabilityId 能力ID
     * @param params 调用参数
     * @return 调用结果
     */
    Map<String, Object> invokeCapability(String capabilityId, Map<String, Object> params);

    // ==================== 通用管理方法 ====================

    /**
     * 获取系统状态
     * @return 系统状态信息
     */
    Map<String, Object> getSystemStatus();

    /**
     * 重启系统
     * @param reason 重启原因
     */
    void restartSystem(String reason);

    /**
     * 关闭系统
     * @param reason 关闭原因
     */
    void shutdownSystem(String reason);

    /**
     * 协议处理器接口
     */
    interface ProtocolHandler {
        /**
         * 处理协议命令
         * @param packet 命令包
         * @return 处理结果
         */
        boolean handle(CommandPacket packet);
    }
}
