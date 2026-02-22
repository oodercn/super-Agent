package net.ooder.nexus.infrastructure.management;

import net.ooder.sdk.api.OoderSDK;
import net.ooder.sdk.api.protocol.CommandPacket;

import java.util.List;
import java.util.Map;

/**
 * Nexus 管理器接口
 *
 * <p>用于内部管理 Nexus 的核心功能，提供以下管理能力：</p>
 * <ul>
 *   <li><strong>LLM 交互管理</strong> - 注册和管理 LLM 提供商，发送 LLM 请求</li>
 *   <li><strong>协议中枢管理</strong> - 注册和处理协议命令，实现北上南下协议交互</li>
 *   <li><strong>网络能力管理</strong> - 管理 P2P 网络节点、连接和能力</li>
 *   <li><strong>系统管理</strong> - 系统状态监控、重启和关闭</li>
 * </ul>
 *
 * <p><strong>设计模式：</strong></p>
 * <p>采用外观模式（Facade Pattern），为 Nexus 的复杂子系统提供统一的简化接口。</p>
 *
 * @author ooder Team
 * @version 2.0.0-sdk072
 * @since 1.0.0
 * @see NexusManagerImpl
 * @see OoderSDK
 */
public interface NexusManager {

    /**
     * 初始化管理器
     *
     * <p>使用 OoderSDK 实例初始化 NexusManager，建立与 P2P 网络的连接。</p>
     *
     * @param sdk OoderSDK 实例，用于 P2P 网络通信
     */
    void initialize(OoderSDK sdk);

    // ==================== LLM 交互管理 ====================

    /**
     * 注册 LLM 提供商
     *
     * <p>注册一个 LLM（大语言模型）提供商，使其可以被系统中的其他组件调用。</p>
     *
     * @param providerId   LLM 提供商唯一标识
     * @param providerInfo LLM 提供商信息，包含名称、类型、配置参数等
     */
    void registerLlmProvider(String providerId, Map<String, Object> providerInfo);

    /**
     * 移除 LLM 提供商
     *
     * <p>从系统中移除指定的 LLM 提供商。</p>
     *
     * @param providerId LLM 提供商唯一标识
     */
    void removeLlmProvider(String providerId);

    /**
     * 获取所有 LLM 提供商
     *
     * <p>获取系统中注册的所有 LLM 提供商信息。</p>
     *
     * @return LLM 提供商映射，key 为 providerId，value 为提供商信息
     */
    Map<String, Map<String, Object>> getLlmProviders();

    /**
     * 发送 LLM 请求
     *
     * <p>向指定的 LLM 提供商发送请求，并获取响应。</p>
     *
     * @param providerId  LLM 提供商唯一标识
     * @param requestData 请求数据，包含 prompt、参数等
     * @return 响应数据，包含生成的文本、token 使用量等
     */
    Map<String, Object> sendLlmRequest(String providerId, Map<String, Object> requestData);

    // ==================== 北上南下协议中枢 ====================

    /**
     * 注册协议处理器
     *
     * <p>注册一个协议命令处理器，用于处理特定类型的协议命令。</p>
     *
     * @param commandType 命令类型，如 "MCP", "ROUTE", "CONFIG"
     * @param handler     命令处理器实例
     */
    void registerProtocolHandler(String commandType, ProtocolHandler handler);

    /**
     * 移除协议处理器
     *
     * <p>从系统中移除指定类型的协议命令处理器。</p>
     *
     * @param commandType 命令类型
     */
    void removeProtocolHandler(String commandType);

    /**
     * 获取所有协议处理器
     *
     * <p>获取系统中注册的所有协议命令处理器。</p>
     *
     * @return 协议处理器映射，key 为命令类型，value 为处理器实例
     */
    Map<String, ProtocolHandler> getProtocolHandlers();

    /**
     * 处理协议命令
     *
     * <p>使用注册的处理器处理协议命令包。</p>
     *
     * @param commandType 命令类型
     * @param packet      命令数据包
     * @return true 如果处理成功，false 如果处理失败或没有对应的处理器
     */
    boolean handleProtocolCommand(String commandType, CommandPacket packet);

    // ==================== Agent 内部网络能力管理 ====================

    /**
     * 注册网络节点
     *
     * <p>在 P2P 网络中注册一个新的节点。</p>
     *
     * @param nodeId   节点唯一标识
     * @param nodeInfo 节点信息，包含地址、类型、能力等
     */
    void registerNetworkNode(String nodeId, Map<String, Object> nodeInfo);

    /**
     * 移除网络节点
     *
     * <p>从 P2P 网络中移除指定的节点。</p>
     *
     * @param nodeId 节点唯一标识
     */
    void removeNetworkNode(String nodeId);

    /**
     * 获取所有网络节点
     *
     * <p>获取 P2P 网络中的所有节点信息。</p>
     *
     * @return 网络节点映射，key 为 nodeId，value 为节点信息
     */
    Map<String, Map<String, Object>> getNetworkNodes();

    /**
     * 创建网络连接
     *
     * <p>在两个节点之间建立网络连接。</p>
     *
     * @param sourceNodeId   源节点 ID
     * @param targetNodeId   目标节点 ID
     * @param connectionInfo 连接信息，包含连接类型、参数等
     * @return 连接唯一标识
     */
    String createNetworkConnection(String sourceNodeId, String targetNodeId, Map<String, Object> connectionInfo);

    /**
     * 断开网络连接
     *
     * <p>断开指定的网络连接。</p>
     *
     * @param connectionId 连接唯一标识
     */
    void disconnectNetworkConnection(String connectionId);

    /**
     * 获取网络拓扑
     *
     * <p>获取 P2P 网络的拓扑结构信息。</p>
     *
     * @return 网络拓扑信息，包含节点和连接关系
     */
    Map<String, Object> getNetworkTopology();

    /**
     * 注册能力
     *
     * <p>注册一个能力（Capability），使其可以被网络中的其他节点调用。</p>
     *
     * @param capabilityId   能力唯一标识
     * @param capabilityInfo 能力信息，包含名称、描述、参数等
     */
    void registerCapability(String capabilityId, Map<String, Object> capabilityInfo);

    /**
     * 移除能力
     *
     * <p>从系统中移除指定的能力。</p>
     *
     * @param capabilityId 能力唯一标识
     */
    void removeCapability(String capabilityId);

    /**
     * 获取所有能力
     *
     * <p>获取系统中注册的所有能力信息。</p>
     *
     * @return 能力映射，key 为 capabilityId，value 为能力信息
     */
    Map<String, Map<String, Object>> getCapabilities();

    /**
     * 调用能力
     *
     * <p>调用指定的能力，并获取执行结果。</p>
     *
     * @param capabilityId 能力唯一标识
     * @param params       调用参数
     * @return 能力执行结果
     */
    Map<String, Object> invokeCapability(String capabilityId, Map<String, Object> params);

    // ==================== 通用管理方法 ====================

    /**
     * 获取系统状态
     *
     * <p>获取 Nexus 系统的当前状态信息。</p>
     *
     * @return 系统状态信息，包含运行时间、资源使用、节点数量等
     */
    Map<String, Object> getSystemStatus();

    /**
     * 重启系统
     *
     * <p>重启 Nexus 系统。</p>
     *
     * @param reason 重启原因，用于日志记录
     */
    void restartSystem(String reason);

    /**
     * 关闭系统
     *
     * <p>关闭 Nexus 系统。</p>
     *
     * @param reason 关闭原因，用于日志记录
     */
    void shutdownSystem(String reason);

    /**
     * 协议处理器接口
     *
     * <p>用于处理特定类型的协议命令。</p>
     */
    interface ProtocolHandler {
        /**
         * 处理协议命令
         *
         * @param packet 命令数据包
         * @return true 如果处理成功，false 如果处理失败
         */
        boolean handle(CommandPacket packet);
    }
}
