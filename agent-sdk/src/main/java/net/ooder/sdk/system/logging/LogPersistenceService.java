package net.ooder.sdk.system.logging;

import net.ooder.sdk.storage.StorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * 日志持久化服务类，用于管理日志记录的持久化存储
 */
public class LogPersistenceService {

    private static final Logger log = LoggerFactory.getLogger(LogPersistenceService.class);

    /**
     * 日志记录存储键前缀
     */
    private static final String LOG_RECORD_PREFIX = "log/record/";

    /**
     * 日志索引存储键前缀
     */
    private static final String LOG_INDEX_PREFIX = "log/index/";

    /**
     * 单例实例
     */
    private static final LogPersistenceService instance = new LogPersistenceService();

    /**
     * 私有构造函数
     */
    private LogPersistenceService() {
    }

    /**
     * 获取单例实例
     * @return 日志持久化服务实例
     */
    public static LogPersistenceService getInstance() {
        return instance;
    }

    /**
     * 保存日志记录
     * @param record 日志记录
     * @return 是否保存成功
     */
    public boolean saveLogRecord(LogRecord record) {
        try {
            // 生成日志ID
            if (record.getLogId() == null) {
                record.setLogId(UUID.randomUUID().toString());
            }

            // 生成存储键
            String timestamp = String.valueOf(record.getTimestamp());
            String key = LOG_RECORD_PREFIX + timestamp + "_" + record.getLogId();

            // 保存日志记录
            boolean success = StorageManager.getInstance().getJsonStorage().save(key, record);

            // 如果保存成功，更新索引
            if (success) {
                updateLogIndex(record);
            }

            return success;
        } catch (Exception e) {
            log.error("Failed to save log record: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 异步保存日志记录
     * @param record 日志记录
     * @return 保存结果的CompletableFuture
     */
    public CompletableFuture<Boolean> saveLogRecordAsync(LogRecord record) {
        return CompletableFuture.supplyAsync(() -> saveLogRecord(record));
    }

    /**
     * 批量保存日志记录
     * @param records 日志记录列表
     * @return 是否全部保存成功
     */
    public boolean saveLogRecords(List<LogRecord> records) {
        if (records == null || records.isEmpty()) {
            return true;
        }

        boolean allSuccess = true;
        for (LogRecord record : records) {
            if (!saveLogRecord(record)) {
                allSuccess = false;
            }
        }
        return allSuccess;
    }

    /**
     * 异步批量保存日志记录
     * @param records 日志记录列表
     * @return 保存结果的CompletableFuture
     */
    public CompletableFuture<Boolean> saveLogRecordsAsync(List<LogRecord> records) {
        return CompletableFuture.supplyAsync(() -> saveLogRecords(records));
    }

    /**
     * 加载日志记录
     * @param logId 日志ID
     * @return 日志记录
     */
    public LogRecord loadLogRecord(String logId) {
        try {
            // 这里简化实现，实际应该通过索引查找
            // 目前返回null，后续可以扩展
            return null;
        } catch (Exception e) {
            log.error("Failed to load log record: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据命令ID加载相关日志
     * @param commandId 命令ID
     * @return 日志记录列表
     */
    public List<LogRecord> loadLogsByCommandId(String commandId) {
        try {
            // 这里简化实现，实际应该通过索引查找
            // 目前返回空列表，后续可以扩展
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("Failed to load logs by command id: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 根据技能ID加载相关日志
     * @param skillId 技能ID
     * @return 日志记录列表
     */
    public List<LogRecord> loadLogsBySkillId(String skillId) {
        try {
            // 这里简化实现，实际应该通过索引查找
            // 目前返回空列表，后续可以扩展
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("Failed to load logs by skill id: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 清理过期的日志记录
     * @param maxAge 最大保留时间（毫秒）
     * @return 清理的记录数量
     */
    public int cleanExpiredLogs(long maxAge) {
        try {
            // 这里简化实现，实际应该通过查询接口获取并清理过期记录
            // 目前返回0，后续可以扩展
            return 0;
        } catch (Exception e) {
            log.error("Failed to clean expired logs: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 更新日志索引
     * @param record 日志记录
     */
    private void updateLogIndex(LogRecord record) {
        try {
            // 按命令ID索引
            if (record.getCommandId() != null) {
                String indexKey = LOG_INDEX_PREFIX + "command/" + record.getCommandId();
                // 这里简化实现，实际应该维护索引列表
            }

            // 按技能ID索引
            if (record.getSkillId() != null) {
                String indexKey = LOG_INDEX_PREFIX + "skill/" + record.getSkillId();
                // 这里简化实现，实际应该维护索引列表
            }

            // 按路由ID索引
            if (record.getRouteId() != null) {
                String indexKey = LOG_INDEX_PREFIX + "route/" + record.getRouteId();
                // 这里简化实现，实际应该维护索引列表
            }

            // 按时间索引
            String date = new Date(record.getTimestamp()).toString().substring(0, 10);
            String timeIndexKey = LOG_INDEX_PREFIX + "time/" + date;
            // 这里简化实现，实际应该维护索引列表
        } catch (Exception e) {
            log.error("Failed to update log index: {}", e.getMessage(), e);
        }
    }

    /**
     * 记录错误日志
     * @param source 来源
     * @param message 消息
     * @param error 错误信息
     * @param commandId 关联的命令ID（可选）
     * @return 是否记录成功
     */
    public boolean logError(String source, String message, String error, String commandId) {
        LogRecord record = new LogRecord();
        record.setLevel("ERROR");
        record.setSource(source);
        record.setMessage(message);
        record.setError(error);
        record.setCommandId(commandId);
        return saveLogRecord(record);
    }

    /**
     * 记录警告日志
     * @param source 来源
     * @param message 消息
     * @param commandId 关联的命令ID（可选）
     * @return 是否记录成功
     */
    public boolean logWarn(String source, String message, String commandId) {
        LogRecord record = new LogRecord();
        record.setLevel("WARN");
        record.setSource(source);
        record.setMessage(message);
        record.setCommandId(commandId);
        return saveLogRecord(record);
    }

    /**
     * 记录信息日志
     * @param source 来源
     * @param message 消息
     * @param commandId 关联的命令ID（可选）
     * @return 是否记录成功
     */
    public boolean logInfo(String source, String message, String commandId) {
        LogRecord record = new LogRecord();
        record.setLevel("INFO");
        record.setSource(source);
        record.setMessage(message);
        record.setCommandId(commandId);
        return saveLogRecord(record);
    }

    /**
     * 记录调试日志
     * @param source 来源
     * @param message 消息
     * @param commandId 关联的命令ID（可选）
     * @return 是否记录成功
     */
    public boolean logDebug(String source, String message, String commandId) {
        LogRecord record = new LogRecord();
        record.setLevel("DEBUG");
        record.setSource(source);
        record.setMessage(message);
        record.setCommandId(commandId);
        return saveLogRecord(record);
    }
}
