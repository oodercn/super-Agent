package net.ooder.sdk.command.impl;

import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.api.CommandTask;
import net.ooder.sdk.command.api.CommandInterceptor;
import net.ooder.sdk.command.api.CommandInterceptorChain;
import net.ooder.sdk.command.model.CommandResult;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.command.persistence.CommandPersistenceService;
import net.ooder.sdk.command.persistence.CommandRecord;
import net.ooder.sdk.system.enums.CommandStatus;
import net.ooder.sdk.system.logging.LogPersistenceService;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.network.packet.StatusReportPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 抽象命令任务类，实现CommandTask接口并提供通用功能
 *
 * @param <T> 命令参数类型
 */
public abstract class AbstractCommandTask<T> implements CommandTask {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected AgentSDK agentSDK;
    protected CommandType commandType;
    private final CommandInterceptorChain interceptorChain = new CommandInterceptorChain();

    public AbstractCommandTask(CommandType commandType) {
        this.commandType = commandType;
    }

    /**
     * 设置AgentSDK实例
     *
     * @param agentSDK AgentSDK实例
     */
    public void setAgentSDK(AgentSDK agentSDK) {
        this.agentSDK = agentSDK;
    }

    /**
     * 添加命令拦截器
     * @param interceptor 命令拦截器
     */
    public void addInterceptor(CommandInterceptor interceptor) {
        interceptorChain.addInterceptor(interceptor);
    }

    /**
     * 移除命令拦截器
     * @param interceptor 命令拦截器
     */
    public void removeInterceptor(CommandInterceptor interceptor) {
        interceptorChain.removeInterceptor(interceptor);
    }

    @Override
    public CommandType getCommandType() {
        return commandType;
    }

    @Override
    public CompletableFuture<CommandResult> execute(CommandPacket packet) {
        return CompletableFuture.supplyAsync(() -> {
            CommandRecord record = null;
            try {
                // 执行前置拦截器
                if (!interceptorChain.executeBefore(packet)) {
                    CommandResult errorResult = CommandResult.executionError("Command execution blocked by interceptor");
                    errorResult.setErrorCode("INTERCEPTOR_BLOCKED");
                    errorResult.setErrorMessage("Command execution blocked by interceptor");
                    sendCommandReport(packet, CommandStatus.FAILED, "Command execution blocked by interceptor");
                    sendCommandResult(packet, errorResult);
                    return errorResult;
                }

                // 创建命令记录
                record = createCommandRecord(packet);
                CommandPersistenceService.getInstance().saveCommandRecord(record);

                // 发送命令开始执行报告
                sendCommandReport(packet, CommandStatus.EXECUTING, "Command execution started");

                // 执行命令逻辑
                CommandResult result = doExecute((CommandPacket<T>) packet);

                // 记录命令执行结果
                recordCommandResult(packet, result, record);

                // 执行后置拦截器
                interceptorChain.executeAfter(packet, result);

                // 发送命令执行结果
                sendCommandResult(packet, result);

                return result;
            } catch (Exception e) {
                log.error("Failed to execute command: {}", e.getMessage(), e);
                CommandResult errorResult = CommandResult.executionError(e.getMessage());
                errorResult.setErrorCode("EXECUTION_FAILED");
                errorResult.setErrorMessage(e.getMessage());

                // 执行错误拦截器
                interceptorChain.executeOnError(packet, e, errorResult);

                // 记录错误并安排重试
                handleCommandError(packet, e, errorResult, record);

                // 发送命令执行失败报告
                sendCommandReport(packet, CommandStatus.FAILED, e.getMessage());
                sendCommandResult(packet, errorResult);

                return errorResult;
            }
        });
    }

    /**
     * 创建命令记录
     *
     * @param packet 命令数据包
     * @return 命令记录
     */
    private CommandRecord createCommandRecord(CommandPacket packet) {
        CommandRecord record = new CommandRecord();
        record.setRecordId(java.util.UUID.randomUUID().toString());
        record.setCommandId(packet.getMessageId()); // 使用messageId作为命令ID
        CommandType commandType = CommandType.fromValue(packet.getOperation()).orElse(null);
        record.setCommandType(commandType);
        record.setSenderId(packet.getSenderId());
        record.setReceiverId(packet.getReceiverId());
        record.setCreatedTime(packet.getTimestamp());

        return record;
    }

    /**
     * 记录命令执行结果
     *
     * @param packet 命令数据包
     * @param result 执行结果
     * @param record 命令记录
     */
    private void recordCommandResult(CommandPacket packet, CommandResult result, CommandRecord record) {
        try {
            // 更新命令记录
            record.setResult(result.toMap());
            record.setStatus(CommandStatus.COMPLETED);
            record.setUpdatedTime(System.currentTimeMillis());
            CommandPersistenceService.getInstance().saveCommandRecord(record);

            // 记录成功日志
            LogPersistenceService.getInstance().logInfo(
                    getClass().getName(),
                    "Command executed successfully: " + packet.getMessageId(),
                    packet.getMessageId()
            );
        } catch (Exception e) {
            log.error("Failed to record command result: {}", e.getMessage(), e);
        }
    }

    /**
     * 处理命令执行错误
     *
     * @param packet      命令数据包
     * @param e           异常
     * @param errorResult 错误结果
     * @param record      命令记录
     */
    private void handleCommandError(CommandPacket packet, Exception e, CommandResult errorResult, CommandRecord record) {
        try {
            // 更新命令记录
            if (record != null) {
                record.setStatus(CommandStatus.FAILED);
                record.setErrorMessage(e.getMessage());
                record.setUpdatedTime(System.currentTimeMillis());
                CommandPersistenceService.getInstance().saveCommandRecord(record);
            }

            // 记录错误日志
            LogPersistenceService.getInstance().logError(
                    getClass().getName(),
                    "Failed to execute command",
                    e.getMessage(),
                    packet.getMessageId()
            );

            // 这里暂时注释掉重试逻辑，因为CommandPacket中没有Command对象
            // 后续需要根据实际情况修改
            // RetryManager.getInstance().scheduleRetry(
            //         packet.getCommand(),
            //         e.getMessage(),
            //         "default"
            // );
        } catch (Exception ex) {
            log.error("Failed to handle command error: {}", ex.getMessage(), ex);
        }
    }

    /**
     * 执行具体的命令逻辑，由子类实现
     *
     * @param packet 命令数据包
     * @return 命令执行结果
     * @throws Exception 执行命令失败时抛出异常
     */
    protected abstract CommandResult doExecute(CommandPacket<T> packet) throws Exception;

    @Override
    public void sendCommandReport(CommandPacket packet, CommandStatus status, String message) {
        if (agentSDK == null) {
            log.error("AgentSDK is not set, cannot send command report");
            return;
        }

        // 创建状态报告数据包
        StatusReportPacket reportPacket = new StatusReportPacket();
        reportPacket.setReportType("command_execution");
        reportPacket.setCurrentStatus(status.name());
        reportPacket.setSenderId(agentSDK.getAgentId());
        reportPacket.setReceiverId(packet.getSenderId());

        try {
            // 发送状态报告
            agentSDK.sendStatusReport(reportPacket);
        } catch (Exception e) {
            log.error("Failed to send command report: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendCommandResult(CommandPacket packet, CommandResult result) {
        if (agentSDK == null) {
            log.error("AgentSDK is not set, cannot send command result");
            return;
        }

        // 构建结果参数
        Map<String, Object> resultParams = new HashMap<>();
        resultParams.put("status", result.getStatus());
        resultParams.put("message", result.getMessage());
        resultParams.put("data", result.getData());
        resultParams.put("timestamp", result.getTimestamp());
        resultParams.put("errorCode", result.getErrorCode());
        resultParams.put("errorMessage", result.getErrorMessage());

        // 发送结果命令
        try {
            agentSDK.sendCommand(CommandType.COMMAND_RESPONSE, resultParams);
        } catch (Exception e) {
            log.error("Failed to send command result: {}", e.getMessage(), e);
        }
    }

    @Override
    public <T extends Command> void send(T command) {
        if (agentSDK == null) {
            log.error("AgentSDK is not set, cannot send command");
            return;
        }

        try {
            // 获取命令类型
            CommandType commandType = command.getCommandType();

            // 使用FastJSON将Command对象转换为Map
            Map<String, Object> params = com.alibaba.fastjson.JSON.parseObject(
                    com.alibaba.fastjson.JSON.toJSONString(command), Map.class);

            // 移除命令对象中的通用字段，保留自定义字段
            params.remove("commandId");
            params.remove("commandType");
            params.remove("timestamp");
            params.remove("senderId");
            params.remove("receiverId");
            params.remove("priority");
            params.remove("timeout");

            // 调用AgentSDK的sendCommand方法
            agentSDK.sendCommand(commandType, params);
        } catch (Exception e) {
            log.error("Failed to send command: {}", e.getMessage(), e);
        }
    }
}