package net.ooder.sdk.command.persistence;

import net.ooder.sdk.system.enums.CommandStatus;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandBaseServiceImpl implements CommandBaseService {
    private static final Logger log = LoggerFactory.getLogger(CommandBaseServiceImpl.class);
    private final Map<String, CommandStatus> commandStatusMap = new ConcurrentHashMap<>();
    
    @Override
    public CompletableFuture<String> createCommand(String command, Map<String, Object> params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 生成命令ID
                String commandId = "cmd_" + System.currentTimeMillis() + "_" + Math.random();
                // 初始化命令状态
                commandStatusMap.put(commandId, CommandStatus.PENDING);
                log.info("Created command: {} with status {}", commandId, CommandStatus.PENDING);
                return commandId;
            } catch (Exception e) {
                log.error("Error creating command: {}", e.getMessage());
                throw new RuntimeException("Error creating command", e);
            }
        });
    }
    
    @Override
    public CompletableFuture<CommandStatus> getCommandStatus(String commandId) {
        return CompletableFuture.supplyAsync(() -> {
            CommandStatus status = commandStatusMap.get(commandId);
            if (status == null) {
                log.warn("Command not found: {}", commandId);
                return CommandStatus.FAILED;
            }
            return status;
        });
    }
    
    @Override
    public CompletableFuture<Boolean> cancelCommand(String commandId) {
        return CompletableFuture.supplyAsync(() -> {
            if (commandStatusMap.containsKey(commandId)) {
                commandStatusMap.put(commandId, CommandStatus.CANCELLED);
                log.info("Cancelled command: {}", commandId);
                return true;
            }
            log.warn("Command not found for cancellation: {}", commandId);
            return false;
        });
    }
    
    @Override
    public CompletableFuture<Boolean> updateCommandStatus(String commandId, CommandStatus status) {
        return CompletableFuture.supplyAsync(() -> {
            if (commandStatusMap.containsKey(commandId)) {
                commandStatusMap.put(commandId, status);
                log.info("Updated command {} status to {}", commandId, status);
                return true;
            }
            log.warn("Command not found for status update: {}", commandId);
            return false;
        });
    }
}
