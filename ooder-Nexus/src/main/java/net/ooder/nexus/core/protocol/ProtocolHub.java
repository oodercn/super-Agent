package net.ooder.nexus.core.protocol;

import net.ooder.nexus.core.protocol.model.CommandPacket;
import net.ooder.nexus.core.protocol.model.CommandResult;
import net.ooder.nexus.core.protocol.model.ProtocolStats;

import java.util.List;

/**
 * 协议中枢接口
 * 统一管理和分发南向协议的核心接口
 */
public interface ProtocolHub {

    /**
     * 注册协议处理器
     *
     * @param protocolType 协议类型（如：MCP, ROUTE, END等）
     * @param handler      协议处理器
     */
    void registerProtocolHandler(String protocolType, ProtocolHandler handler);

    /**
     * 注销协议处理器
     *
     * @param protocolType 协议类型
     */
    void unregisterProtocolHandler(String protocolType);

    /**
     * 处理南向命令
     *
     * @param packet 命令数据包
     * @return 命令执行结果
     */
    CommandResult handleCommand(CommandPacket packet);

    /**
     * 获取支持的协议列表
     *
     * @return 协议类型列表
     */
    List<String> getSupportedProtocols();

    /**
     * 获取协议统计信息
     *
     * @param protocolType 协议类型
     * @return 协议统计信息
     */
    ProtocolStats getProtocolStats(String protocolType);

    /**
     * 获取所有协议的统计信息
     *
     * @return 所有协议统计信息列表
     */
    List<ProtocolStats> getAllProtocolStats();

    /**
     * 检查协议是否已注册
     *
     * @param protocolType 协议类型
     * @return 是否已注册
     */
    boolean isProtocolRegistered(String protocolType);

    /**
     * 获取协议处理器
     *
     * @param protocolType 协议类型
     * @return 协议处理器，如果不存在返回null
     */
    ProtocolHandler getProtocolHandler(String protocolType);
}
