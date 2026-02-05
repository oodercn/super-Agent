package net.ooder.sdk.command.api;

import net.ooder.sdk.command.model.CommandResult;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.system.enums.CommandStatus;

import java.util.concurrent.CompletableFuture;

/**
 * 命令任务接口，定义命令任务的基本方法
 */
public interface CommandTask {
    /**
     * 执行命令任务
     * @param packet 命令数据包
     * @return 命令执行结果的CompletableFuture
     */
    CompletableFuture<CommandResult> execute(CommandPacket packet);
    
    /**
     * 获取命令类型
     * @return 命令类型
     */
    CommandType getCommandType();
    
    /**
     * 发送命令执行报告
     * @param packet 原始命令数据包
     * @param status 执行状态
     * @param message 状态消息
     */
    void sendCommandReport(CommandPacket packet, CommandStatus status, String message);
    
    /**
     * 发送命令执行结果
     * @param packet 原始命令数据包
     * @param result 执行结果
     */
    void sendCommandResult(CommandPacket packet, CommandResult result);
    
    /**
     * 发送命令（统一的命令发送方法）
     * @param command 要发送的命令对象
     * @param <T> 命令类型，必须继承自Command抽象类
     */
    <T extends Command> void send(T command);
}