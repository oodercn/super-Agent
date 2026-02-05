package net.ooder.sdk.command.persistence;

import net.ooder.sdk.system.enums.CommandStatus;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CommandBaseService {
    
    /**
     * 创建命令
     */
    CompletableFuture<String> createCommand(String command, Map<String, Object> params);
    
    /**
     * 获取命令状态
     */
    CompletableFuture<CommandStatus> getCommandStatus(String commandId);
    
    /**
     * 取消命令
     */
    CompletableFuture<Boolean> cancelCommand(String commandId);
    
    /**
     * 更新命令状态
     */
    CompletableFuture<Boolean> updateCommandStatus(String commandId, CommandStatus status);
}
