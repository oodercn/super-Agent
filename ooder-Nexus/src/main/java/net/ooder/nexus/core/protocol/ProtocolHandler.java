package net.ooder.nexus.core.protocol;

import net.ooder.nexus.core.protocol.model.CommandPacket;
import net.ooder.nexus.core.protocol.model.CommandResult;
import net.ooder.nexus.core.protocol.model.ProtocolStatus;

/**
 * 协议处理器接口
 * 处理特定南向协议的核心接口
 */
public interface ProtocolHandler {

    /**
     * 获取协议类型
     *
     * @return 协议类型标识（如：MCP, ROUTE, END等）
     */
    String getProtocolType();

    /**
     * 处理命令
     *
     * @param packet 命令数据包
     * @return 命令执行结果
     */
    CommandResult handleCommand(CommandPacket packet);

    /**
     * 验证命令合法性
     *
     * @param packet 命令数据包
     * @return 是否合法
     */
    boolean validateCommand(CommandPacket packet);

    /**
     * 获取协议状态
     *
     * @return 协议状态
     */
    ProtocolStatus getStatus();

    /**
     * 初始化协议处理器
     */
    void initialize();

    /**
     * 销毁协议处理器
     */
    void destroy();

    /**
     * 是否支持该命令类型
     *
     * @param commandType 命令类型
     * @return 是否支持
     */
    boolean supportsCommand(String commandType);

    /**
     * 获取处理器描述
     *
     * @return 描述信息
     */
    String getDescription();
}
