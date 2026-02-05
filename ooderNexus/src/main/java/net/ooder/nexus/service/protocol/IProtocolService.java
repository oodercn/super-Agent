package net.ooder.nexus.service.protocol;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.model.mcp.ProtocolHandlerData;
import net.ooder.nexus.model.TestCommandResult;
import net.ooder.nexus.model.system.CommandStatsData;

import java.util.List;
import java.util.Map;

/**
 * 协议管理服务接口
 * 负责协议处理器管理、命令处理、命令统计等功能
 */
public interface IProtocolService {
    
    /**
     * 获取协议处理器列表
     * @param params 查询参数
     * @return 协议处理器列表
     */
    Result<List<ProtocolHandlerData>> getProtocolHandlers(Map<String, Object> params);

    /**
     * 注册协议处理器
     * @param handlerData 处理器数据
     * @return 注册结果
     */
    Result<ProtocolHandlerData> registerProtocolHandler(Map<String, Object> handlerData);

    /**
     * 移除协议处理器
     * @param commandType 命令类型
     * @return 移除结果
     */
    Result<ProtocolHandlerData> removeProtocolHandler(String commandType);

    /**
     * 处理协议命令
     * @param commandData 命令数据
     * @return 处理结果
     */
    Result<ProtocolHandlerData> handleProtocolCommand(Map<String, Object> commandData);

    /**
     * 刷新协议处理器列表
     * @return 刷新结果
     */
    Result<List<ProtocolHandlerData>> refreshProtocolHandlers();

    /**
     * 搜索协议处理器
     * @param params 搜索参数
     * @return 搜索结果
     */
    Result<List<ProtocolHandlerData>> searchProtocolHandlers(Map<String, Object> params);

    /**
     * 获取命令统计
     * @return 命令统计
     */
    Result<CommandStatsData> getCommandStats();

    /**
     * 测试命令
     * @param commandData 命令数据
     * @return 测试结果
     */
    Result<TestCommandResult> testCommand(Map<String, Object> commandData);
}
