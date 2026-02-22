package net.ooder.nexus.service;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.domain.network.model.NetworkStatusData;
import net.ooder.nexus.domain.system.model.CommandStatsData;
import net.ooder.nexus.domain.network.model.EndAgent;
import net.ooder.nexus.model.TestCommandResult;
import net.ooder.nexus.domain.mcp.model.LogEntry;
import net.ooder.nexus.domain.config.model.BasicConfig;
import net.ooder.nexus.domain.config.model.AdvancedConfig;
import net.ooder.nexus.model.ConfigResult;
import net.ooder.nexus.domain.config.model.SecurityConfig;

import java.util.List;
import java.util.Map;

/**
 * MCP Agent 核心服务接口
 * 提供网络状态、命令统计、终端代理管理、配置管理等功能
 */
public interface McpCoreService {

    /**
     * 获取网络状态
     */
    Result<NetworkStatusData> getNetworkStatus();

    /**
     * 获取命令统计
     */
    Result<CommandStatsData> getCommandStats();

    /**
     * 获取终端代理列表
     */
    Result<List<EndAgent>> getEndAgents();

    /**
     * 添加终端代理
     */
    Result<EndAgent> addEndAgent(Map<String, Object> agentData);

    /**
     * 编辑终端代理
     */
    Result<EndAgent> editEndAgent(String agentId, Map<String, Object> agentData);

    /**
     * 删除终端代理
     */
    Result<EndAgent> deleteEndAgent(String agentId);

    /**
     * 获取终端代理详情
     */
    Result<EndAgent> getEndAgentDetails(String agentId);

    /**
     * 测试命令
     */
    Result<TestCommandResult> testCommand(Map<String, Object> commandData);

    /**
     * 获取日志列表
     */
    Result<List<LogEntry>> getLogList(int limit);

    /**
     * 清空日志
     */
    Result<Void> clearLog();

    /**
     * 获取基本配置
     */
    Result<BasicConfig> getBasicConfig();

    /**
     * 获取高级配置
     */
    Result<AdvancedConfig> getAdvancedConfig();

    /**
     * 保存配置
     */
    Result<ConfigResult> saveConfig(Map<String, Object> configData);

    /**
     * 重置配置
     */
    Result<ConfigResult> resetConfig();

    /**
     * 获取安全配置
     */
    Result<SecurityConfig> getSecurityConfig();
}
