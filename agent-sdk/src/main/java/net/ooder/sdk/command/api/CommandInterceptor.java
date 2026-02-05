package net.ooder.sdk.command.api;

import net.ooder.sdk.command.model.CommandResult;
import net.ooder.sdk.network.packet.CommandPacket;

public interface CommandInterceptor {
    /**
     * 命令执行前的处理
     * @param packet 命令数据包
     * @return 是否继续执行命令
     */
    boolean beforeExecute(CommandPacket packet);
    
    /**
     * 命令执行后的处理
     * @param packet 命令数据包
     * @param result 命令执行结果
     */
    void afterExecute(CommandPacket packet, CommandResult result);
    
    /**
     * 命令执行错误时的处理
     * @param packet 命令数据包
     * @param e 异常
     * @param result 错误结果
     */
    void onError(CommandPacket packet, Exception e, CommandResult result);
    
    /**
     * 获取拦截器的优先级
     * @return 优先级，数值越小优先级越高
     */
    int getPriority();
}
