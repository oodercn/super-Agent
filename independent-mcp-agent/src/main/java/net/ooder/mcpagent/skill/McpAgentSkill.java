package net.ooder.mcpagent.skill;

import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.network.packet.CommandPacket;

/**
 * MCP Agent技能接口
 */
public interface McpAgentSkill {
    
    /**
     * 初始化技能
     * @param sdk AgentSDK实例
     */
    void initialize(AgentSDK sdk);
    
    /**
     * 处理MCP注册命令
     * @param packet 命令包
     */
    void handleMcpRegisterCommand(CommandPacket packet);
    
    /**
     * 处理MCP注销命令
     * @param packet 命令包
     */
    void handleMcpDeregisterCommand(CommandPacket packet);
    
    /**
     * 处理MCP心跳命令
     * @param packet 命令包
     */
    void handleMcpHeartbeatCommand(CommandPacket packet);
    
    /**
     * 处理MCP状态查询命令
     * @param packet 命令包
     */
    void handleMcpStatusCommand(CommandPacket packet);
    
    /**
     * 处理MCP发现命令
     * @param packet 命令包
     */
    void handleMcpDiscoverCommand(CommandPacket packet);
    
    /**
     * 处理路由查询命令
     * @param packet 命令包
     */
    void handleRouteQueryCommand(CommandPacket packet);
    
    /**
     * 处理路由更新命令
     * @param packet 命令包
     */
    void handleRouteUpdateCommand(CommandPacket packet);
    
    /**
     * 处理终端发现命令
     * @param packet 命令包
     */
    void handleEndagentDiscoverCommand(CommandPacket packet);
    
    /**
     * 处理终端状态查询命令
     * @param packet 命令包
     */
    void handleEndagentStatusCommand(CommandPacket packet);
    
    /**
     * 处理终端添加命令
     * @param packet 命令包
     */
    void handleEndagentAddCommand(CommandPacket packet);
    
    /**
     * 处理终端移除命令
     * @param packet 命令包
     */
    void handleEndagentRemoveCommand(CommandPacket packet);
    
    /**
     * 处理任务请求命令
     * @param packet 命令包
     */
    void handleTaskRequestCommand(CommandPacket packet);
    
    /**
     * 处理任务响应命令
     * @param packet 命令包
     */
    void handleTaskResponseCommand(CommandPacket packet);
    
    /**
     * 处理认证命令
     * @param packet 命令包
     */
    void handleAuthenticateCommand(CommandPacket packet);
    
    /**
     * 处理认证响应命令
     * @param packet 命令包
     */
    void handleAuthResponseCommand(CommandPacket packet);
    
    /**
     * 启动技能
     */
    void start();
    
    /**
     * 停止技能
     */
    void stop();
}