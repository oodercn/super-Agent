package net.ooder.sdk.command.persistence;

import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.storage.StorageManager;
import net.ooder.sdk.system.enums.CommandStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * 命令持久化服务类，用于管理命令执行记录的持久化存储
 */
public class CommandPersistenceService {

    private static final Logger log = LoggerFactory.getLogger(CommandPersistenceService.class);

    /**
     * 命令记录存储键前缀
     */
    private static final String COMMAND_RECORD_PREFIX = "command/record/";

    /**
     * 命令历史存储键前缀
     */
    private static final String COMMAND_HISTORY_PREFIX = "command/history/";

    /**
     * 命令队列存储键
     */
    private static final String COMMAND_QUEUE_KEY = "command/queue";

    /**
     * 单例实例
     */
    private static final CommandPersistenceService instance = new CommandPersistenceService();

    /**
     * 私有构造函数
     */
    private CommandPersistenceService() {
    }

    /**
     * 获取单例实例
     * @return 命令持久化服务实例
     */
    public static CommandPersistenceService getInstance() {
        return instance;
    }

    /**
     * 保存命令记录
     * @param record 命令记录
     * @return 是否保存成功
     */
    public boolean saveCommandRecord(CommandRecord record) {
        try {
            String key = COMMAND_RECORD_PREFIX + record.getCommandId();
            return StorageManager.getInstance().getJsonStorage().save(key, record);
        } catch (Exception e) {
            log.error("Failed to save command record: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 异步保存命令记录
     * @param record 命令记录
     * @return 保存结果的CompletableFuture
     */
    public CompletableFuture<Boolean> saveCommandRecordAsync(CommandRecord record) {
        return CompletableFuture.supplyAsync(() -> saveCommandRecord(record));
    }

    /**
     * 加载命令记录
     * @param commandId 命令ID
     * @return 命令记录
     */
    public CommandRecord loadCommandRecord(String commandId) {
        try {
            String key = COMMAND_RECORD_PREFIX + commandId;
            return StorageManager.getInstance().getJsonStorage().load(key, CommandRecord.class);
        } catch (Exception e) {
            log.error("Failed to load command record: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 异步加载命令记录
     * @param commandId 命令ID
     * @return 命令记录的CompletableFuture
     */
    public CompletableFuture<CommandRecord> loadCommandRecordAsync(String commandId) {
        return CompletableFuture.supplyAsync(() -> loadCommandRecord(commandId));
    }

    /**
     * 更新命令记录状态
     * @param commandId 命令ID
     * @param status 新状态
     * @param errorMessage 错误信息（可选）
     * @return 是否更新成功
     */
    public boolean updateCommandStatus(String commandId, CommandStatus status, String errorMessage) {
        try {
            CommandRecord record = loadCommandRecord(commandId);
            if (record == null) {
                log.warn("Command record not found for updating status: {}", commandId);
                return false;
            }

            record.setStatus(status);
            record.setUpdatedTime(System.currentTimeMillis());
            if (errorMessage != null) {
                record.setErrorMessage(errorMessage);
            }

            return saveCommandRecord(record);
        } catch (Exception e) {
            log.error("Failed to update command status: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 记录命令执行结果
     * @param commandId 命令ID
     * @param result 执行结果
     * @return 是否记录成功
     */
    public boolean recordCommandResult(String commandId, Map<String, Object> result) {
        try {
            CommandRecord record = loadCommandRecord(commandId);
            if (record == null) {
                log.warn("Command record not found for recording result: {}", commandId);
                return false;
            }

            record.setResult(result);
            record.setStatus(CommandStatus.COMPLETED);
            record.setUpdatedTime(System.currentTimeMillis());

            return saveCommandRecord(record);
        } catch (Exception e) {
            log.error("Failed to record command result: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 增加重试次数
     * @param commandId 命令ID
     * @param nextRetryTime 下一次重试时间
     * @return 是否更新成功
     */
    public boolean incrementRetryCount(String commandId, long nextRetryTime) {
        try {
            CommandRecord record = loadCommandRecord(commandId);
            if (record == null) {
                log.warn("Command record not found for incrementing retry count: {}", commandId);
                return false;
            }

            record.setRetryCount(record.getRetryCount() + 1);
            record.setNextRetryTime(nextRetryTime);
            record.setUpdatedTime(System.currentTimeMillis());

            return saveCommandRecord(record);
        } catch (Exception e) {
            log.error("Failed to increment retry count: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 保存命令到历史记录
     * @param command 命令对象
     * @param result 执行结果
     * @return 是否保存成功
     */
    public boolean saveToHistory(Command command, Map<String, Object> result) {
        try {
            // 生成历史记录键
            String timestamp = String.valueOf(System.currentTimeMillis());
            String key = COMMAND_HISTORY_PREFIX + command.getCommandId() + "_" + timestamp;

            // 构建历史记录数据
            Map<String, Object> historyData = new HashMap<>();
            historyData.put("commandId", command.getCommandId());
            historyData.put("commandType", command.getCommandType().toString());
            historyData.put("timestamp", command.getTimestamp());
            historyData.put("senderId", command.getSenderId());
            historyData.put("receiverId", command.getReceiverId());
            historyData.put("priority", command.getPriority());
            historyData.put("timeout", command.getTimeout());
            historyData.put("executionTime", System.currentTimeMillis());
            historyData.put("result", result);

            return StorageManager.getInstance().getJsonStorage().save(key, historyData);
        } catch (Exception e) {
            log.error("Failed to save command to history: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 加载待重试的命令列表
     * @return 待重试的命令记录列表
     */
    public List<CommandRecord> loadPendingRetryCommands() {
        try {
            // 这里简化实现，实际应该通过查询接口获取
            // 目前返回空列表，后续可以扩展
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("Failed to load pending retry commands: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 清理过期的命令记录
     * @param maxAge 最大保留时间（毫秒）
     * @return 清理的记录数量
     */
    public int cleanExpiredRecords(long maxAge) {
        try {
            // 这里简化实现，实际应该通过查询接口获取并清理过期记录
            // 目前返回0，后续可以扩展
            return 0;
        } catch (Exception e) {
            log.error("Failed to clean expired records: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 从命令创建命令记录
     * @param command 命令对象
     * @return 命令记录
     */
    public CommandRecord createFromCommand(Command command) {
        CommandRecord record = new CommandRecord();
        record.setRecordId(UUID.randomUUID().toString());
        record.setCommandId(command.getCommandId());
        record.setCommandType(command.getCommandType());
        record.setSenderId(command.getSenderId());
        record.setReceiverId(command.getReceiverId());
        record.setCreatedTime(String.valueOf(command.getTimestamp()));
        record.setUpdatedTime(System.currentTimeMillis());
        record.setPriority(command.getPriority());
        record.setTimeout(command.getTimeout());
        record.setStatus(CommandStatus.PENDING);

        return record;
    }
}
