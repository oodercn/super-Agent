package net.ooder.nexus.core.skill;

import net.ooder.sdk.api.OoderSDK;
import net.ooder.sdk.api.protocol.CommandPacket;

/**
 * Nexus 技能接口
 * 
 * <p>定义了 Nexus 系统支持的所有命令处理方法，对应 SDK 0.7.2 协议。</p>
 * 
 * <h3>命令分类：</h3>
 * <ul>
 *   <li><strong>MCP 命令</strong> - 主控代理协议命令</li>
 *   <li><strong>ROUTE 命令</strong> - 路由代理协议命令</li>
 *   <li><strong>END 命令</strong> - 终端代理协议命令</li>
 *   <li><strong>GROUP 命令</strong> - 组管理协议命令</li>
 * </ul>
 * 
 * @author ooder Team
 * @version 2.0.0-sdk072
 * @since 1.0.0
 */
public interface NexusSkill {
    
    /**
     * 初始化技能
     * @param sdk OoderSDK实例
     */
    void initialize(OoderSDK sdk);
    
    // ==================== MCP 命令处理 ====================
    
    /**
     * 处理 MCP 注册命令
     * @param packet 命令包
     */
    void handleMcpRegisterCommand(CommandPacket packet);
    
    /**
     * 处理 MCP 注销命令
     * @param packet 命令包
     */
    void handleMcpDeregisterCommand(CommandPacket packet);
    
    /**
     * 处理 MCP 心跳命令
     * @param packet 命令包
     */
    void handleMcpHeartbeatCommand(CommandPacket packet);
    
    /**
     * 处理 MCP 状态查询命令
     * @param packet 命令包
     */
    void handleMcpStatusCommand(CommandPacket packet);
    
    /**
     * 处理 MCP 发现命令
     * @param packet 命令包
     */
    void handleMcpDiscoverCommand(CommandPacket packet);
    
    /**
     * 处理 MCP 认证命令
     * @param packet 命令包
     */
    void handleMcpAuthenticateCommand(CommandPacket packet);
    
    /**
     * 处理 MCP 认证响应命令
     * @param packet 命令包
     */
    void handleMcpAuthResponseCommand(CommandPacket packet);
    
    /**
     * 处理 MCP 终端发现命令
     * @param packet 命令包
     */
    void handleMcpEndagentDiscoverCommand(CommandPacket packet);
    
    /**
     * 处理 MCP 终端状态命令
     * @param packet 命令包
     */
    void handleMcpEndagentStatusCommand(CommandPacket packet);
    
    /**
     * 处理 MCP 路由查询命令
     * @param packet 命令包
     */
    void handleMcpRouteQueryCommand(CommandPacket packet);
    
    /**
     * 处理 MCP 路由更新命令
     * @param packet 命令包
     */
    void handleMcpRouteUpdateCommand(CommandPacket packet);
    
    /**
     * 处理 MCP 任务请求命令
     * @param packet 命令包
     */
    void handleMcpTaskRequestCommand(CommandPacket packet);
    
    /**
     * 处理 MCP 任务响应命令
     * @param packet 命令包
     */
    void handleMcpTaskResponseCommand(CommandPacket packet);
    
    // ==================== ROUTE 命令处理 ====================
    
    /**
     * 处理路由添加命令
     * @param packet 命令包
     */
    void handleRouteAddCommand(CommandPacket packet);
    
    /**
     * 处理路由配置命令
     * @param packet 命令包
     */
    void handleRouteConfigureCommand(CommandPacket packet);
    
    /**
     * 处理路由认证请求命令
     * @param packet 命令包
     */
    void handleRouteAuthRequestCommand(CommandPacket packet);
    
    /**
     * 处理路由认证响应命令
     * @param packet 命令包
     */
    void handleRouteAuthResponseCommand(CommandPacket packet);
    
    /**
     * 处理路由终端注册命令
     * @param packet 命令包
     */
    void handleRouteEndagentRegisterCommand(CommandPacket packet);
    
    /**
     * 处理路由终端注销命令
     * @param packet 命令包
     */
    void handleRouteEndagentDeregisterCommand(CommandPacket packet);
    
    /**
     * 处理路由终端列表命令
     * @param packet 命令包
     */
    void handleRouteEndagentListCommand(CommandPacket packet);
    
    // ==================== END 命令处理 ====================
    
    /**
     * 处理终端执行命令
     * @param packet 命令包
     */
    void handleEndExecuteCommand(CommandPacket packet);
    
    /**
     * 处理终端重置命令
     * @param packet 命令包
     */
    void handleEndResetCommand(CommandPacket packet);
    
    /**
     * 处理终端配置命令
     * @param packet 命令包
     */
    void handleEndSetConfigCommand(CommandPacket packet);
    
    /**
     * 处理终端状态命令
     * @param packet 命令包
     */
    void handleEndStatusCommand(CommandPacket packet);
    
    /**
     * 处理终端升级命令
     * @param packet 命令包
     */
    void handleEndUpgradeCommand(CommandPacket packet);
    
    // ==================== GROUP 命令处理 ====================
    
    /**
     * 处理组创建命令
     * @param packet 命令包
     */
    void handleGroupCreateCommand(CommandPacket packet);
    
    /**
     * 处理组删除命令
     * @param packet 命令包
     */
    void handleGroupDeleteCommand(CommandPacket packet);
    
    /**
     * 处理组成员添加命令
     * @param packet 命令包
     */
    void handleGroupAddMemberCommand(CommandPacket packet);
    
    /**
     * 处理组成员移除命令
     * @param packet 命令包
     */
    void handleGroupRemoveMemberCommand(CommandPacket packet);
    
    /**
     * 处理组成员列表命令
     * @param packet 命令包
     */
    void handleGroupListMembersCommand(CommandPacket packet);
    
    /**
     * 处理组状态命令
     * @param packet 命令包
     */
    void handleGroupStatusCommand(CommandPacket packet);
    
    // ==================== 生命周期管理 ====================
    
    /**
     * 启动技能
     */
    void start();
    
    /**
     * 停止技能
     */
    void stop();
}
