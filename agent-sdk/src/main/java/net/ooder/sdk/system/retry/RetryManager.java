package net.ooder.sdk.system.retry;

import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.persistence.CommandPersistenceService;
import net.ooder.sdk.command.persistence.CommandRecord;
import net.ooder.sdk.system.enums.CommandStatus;
import net.ooder.sdk.system.logging.LogPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 重试管理器，用于管理命令的重试过程
 */
public class RetryManager {

    private static final Logger log = LoggerFactory.getLogger(RetryManager.class);

    /**
     * 单例实例
     */
    private static final RetryManager instance = new RetryManager();

    /**
     * 调度器，用于安排重试任务
     */
    private final ScheduledExecutorService scheduler;

    /**
     * 重试策略映射，按命令类型或其他标识符映射到对应的重试策略
     */
    private final Map<String, RetryStrategy> retryStrategies;

    /**
     * 正在重试的命令映射，用于跟踪正在进行的重试
     */
    private final Map<String, ScheduledFuture<?>> retryTasks;

    /**
     * 构造方法
     */
    private RetryManager() {
        this.scheduler = Executors.newScheduledThreadPool(5);
        this.retryStrategies = new ConcurrentHashMap<>();
        this.retryTasks = new ConcurrentHashMap<>();

        // 注册默认重试策略
        registerDefaultStrategies();
    }

    /**
     * 获取单例实例
     * @return 重试管理器实例
     */
    public static RetryManager getInstance() {
        return instance;
    }

    /**
     * 注册默认重试策略
     */
    private void registerDefaultStrategies() {
        // 默认指数退避策略：初始间隔1秒，最大间隔30秒，退避因子2.0
        RetryStrategy defaultStrategy = new ExponentialBackoffRetryStrategy(1000, 30000, 2.0);
        retryStrategies.put("default", defaultStrategy);

        // 快速重试策略：固定间隔500毫秒
       RetryStrategy fastStrategy = new FixedIntervalRetryStrategy(500);
        retryStrategies.put("fast", fastStrategy);

        // 慢速重试策略：指数退避，初始间隔5秒，最大间隔60秒
       RetryStrategy slowStrategy = new ExponentialBackoffRetryStrategy(5000, 60000, 2.0);
        retryStrategies.put("slow", slowStrategy);
    }

    /**
     * 注册重试策略
     * @param key 策略标识符
     * @param strategy 重试策略
     */
    public void registerRetryStrategy(String key, RetryStrategy strategy) {
        retryStrategies.put(key, strategy);
    }

    /**
     * 为命令安排重试
     * @param command 命令对象
     * @param errorMessage 错误信息
     * @param strategyKey 重试策略标识符
     * @return 是否安排成功
     */
    public boolean scheduleRetry(Command command, String errorMessage, String strategyKey) {
        try {
            // 获取命令记录
            CommandRecord record = CommandPersistenceService.getInstance().loadCommandRecord(command.getCommandId());
            if (record == null) {
                log.warn("Command record not found for scheduling retry: {}", command.getCommandId());
                return false;
            }

            // 检查是否达到最大重试次数
            if (record.getRetryCount() >= record.getMaxRetryCount()) {
                log.warn("Maximum retry count reached for command: {}", command.getCommandId());
                // 更新命令状态为失败
                CommandPersistenceService.getInstance().updateCommandStatus(
                        command.getCommandId(),
                        CommandStatus.FAILED,
                        "Maximum retry count reached: " + errorMessage
                );
                return false;
            }

            // 获取重试策略
           RetryStrategy strategy = retryStrategies.getOrDefault(strategyKey, retryStrategies.get("default"));

            // 计算下一次重试时间
            long delay = strategy.calculateNextRetryDelay(record.getRetryCount());
            long nextRetryTime = java.lang.System.currentTimeMillis() + delay;

            // 更新命令记录
            record.setNextRetryTime(nextRetryTime);
            record.setErrorMessage(errorMessage);
            CommandPersistenceService.getInstance().saveCommandRecord(record);

            // 安排重试任务
            ScheduledFuture<?> future = scheduler.schedule(() -> {
                try {
                    // 执行重试
                    executeRetry(command, record);
                } catch (Exception e) {
                    log.error("Failed to execute retry task: {}", e.getMessage(), e);
                } finally {
                    // 移除重试任务
                    retryTasks.remove(command.getCommandId());
                }
            }, delay, TimeUnit.MILLISECONDS);

            // 存储重试任务
            retryTasks.put(command.getCommandId(), future);

            log.info("Scheduled retry for command {} in {}ms (attempt {}/{})",
                    command.getCommandId(), delay, record.getRetryCount() + 1, record.getMaxRetryCount());

            return true;
        } catch (Exception e) {
            log.error("Failed to schedule retry for command {}: {}", command.getCommandId(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * 执行重试
     * @param command 命令对象
     * @param record 命令记录
     */
    private void executeRetry(Command command, CommandRecord record) {
        try {
            log.info("Executing retry for command {} (attempt {}/{})",
                    command.getCommandId(), record.getRetryCount() + 1, record.getMaxRetryCount());

            // 增加重试次数
            CommandPersistenceService.getInstance().incrementRetryCount(command.getCommandId(), 0);

            // 这里应该调用命令执行逻辑
            // 暂时留空，后续在集成时实现

            // 记录重试日志
            LogPersistenceService.getInstance().logInfo(
                    "RetryManager",
                    "Executed retry for command: " + command.getCommandId(),
                    command.getCommandId()
            );

        } catch (Exception e) {
            log.error("Error executing retry for command {}: {}", command.getCommandId(), e.getMessage(), e);

            // 记录错误日志
            LogPersistenceService.getInstance().logError(
                    "RetryManager",
                    "Failed to execute retry for command",
                    e.getMessage(),
                    command.getCommandId()
            );
        }
    }

    /**
     * 取消重试
     * @param commandId 命令ID
     * @return 是否取消成功
     */
    public boolean cancelRetry(String commandId) {
        ScheduledFuture<?> future = retryTasks.remove(commandId);
        if (future != null && !future.isDone()) {
            future.cancel(false);
            log.info("Cancelled retry for command: {}", commandId);
            return true;
        }
        return false;
    }

    /**
     * 获取正在重试的命令数量
     * @return 正在重试的命令数量
     */
    public int getActiveRetryCount() {
        return retryTasks.size();
    }

    /**
     * 关闭重试管理器
     */
    public void shutdown() {
        // 取消所有正在进行的重试任务
        for (ScheduledFuture<?> future : retryTasks.values()) {
            future.cancel(false);
        }
        retryTasks.clear();

        // 关闭调度器
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        log.info("RetryManager shutdown completed");
    }
}
