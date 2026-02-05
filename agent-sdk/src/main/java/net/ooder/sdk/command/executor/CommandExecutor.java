package net.ooder.sdk.command.executor;

import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.api.CommandTask;
import net.ooder.sdk.command.model.CommandResult;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.network.packet.CommandPacket;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CommandExecutor {
    private final ExecutorService executorService;
    private final Map<CommandType, CommandTask> commandTasks;
    private final AtomicInteger activeCommands = new AtomicInteger(0);
    private final int maxConcurrentCommands;
    
    public CommandExecutor() {
        this.maxConcurrentCommands = Runtime.getRuntime().availableProcessors() * 2;
        this.executorService = new ThreadPoolExecutor(
            this.maxConcurrentCommands,
            this.maxConcurrentCommands * 2,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadFactory() {
                private final AtomicInteger threadNumber = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "command-executor-" + threadNumber.getAndIncrement());
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        this.commandTasks = new ConcurrentHashMap<>();
    }
    
    public CommandExecutor(int maxConcurrentCommands) {
        this.maxConcurrentCommands = maxConcurrentCommands;
        this.executorService = new ThreadPoolExecutor(
            this.maxConcurrentCommands,
            this.maxConcurrentCommands * 2,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadFactory() {
                private final AtomicInteger threadNumber = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "command-executor-" + threadNumber.getAndIncrement());
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        this.commandTasks = new ConcurrentHashMap<>();
    }
    
    /**
     * 注册命令任务
     * @param commandType 命令类型
     * @param commandTask 命令任务
     */
    public void registerCommandTask(CommandType commandType, CommandTask commandTask) {
        commandTasks.put(commandType, commandTask);
    }
    
    /**
     * 执行命令数据包
     * @param packet 命令数据包
     * @return 命令执行结果
     */
    public CompletableFuture<CommandResult> executeCommand(CommandPacket packet) {
        if (activeCommands.get() >= maxConcurrentCommands) {
            CommandResult errorResult = CommandResult.executionError("Too many concurrent commands");
            errorResult.setErrorCode("TOO_MANY_COMMANDS");
            errorResult.setErrorMessage("Too many concurrent commands");
            return CompletableFuture.completedFuture(errorResult);
        }
        
        activeCommands.incrementAndGet();
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                CommandType commandType = CommandType.fromValue(packet.getOperation()).orElse(null);
                if (commandType == null) {
                    CommandResult errorResult = CommandResult.executionError("Unknown command type: " + packet.getOperation());
                    errorResult.setErrorCode("UNKNOWN_COMMAND_TYPE");
                    errorResult.setErrorMessage("Unknown command type: " + packet.getOperation());
                    return errorResult;
                }
                
                CommandTask commandTask = commandTasks.get(commandType);
                if (commandTask == null) {
                    CommandResult errorResult = CommandResult.executionError("No command task registered for: " + commandType);
                    errorResult.setErrorCode("NO_COMMAND_TASK");
                    errorResult.setErrorMessage("No command task registered for: " + commandType);
                    return errorResult;
                }
                
                return commandTask.execute(packet).join();
            } catch (Exception e) {
                CommandResult errorResult = CommandResult.executionError(e.getMessage());
                errorResult.setErrorCode("EXECUTION_ERROR");
                errorResult.setErrorMessage(e.getMessage());
                return errorResult;
            } finally {
                activeCommands.decrementAndGet();
            }
        }, executorService);
    }
    
    /**
     * 执行命令对象
     * @param command 命令对象
     * @return 命令执行结果
     */
    public CompletableFuture<CommandResult> executeCommand(Command command) {
        // 这里需要将Command对象转换为CommandPacket
        // 暂时返回一个错误结果
        CommandResult errorResult = CommandResult.executionError("Direct command execution not supported yet");
        errorResult.setErrorCode("NOT_SUPPORTED");
        errorResult.setErrorMessage("Direct command execution not supported yet");
        return CompletableFuture.completedFuture(errorResult);
    }
    
    /**
     * 关闭命令执行器
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
    
    /**
     * 获取当前活跃的命令数量
     * @return 活跃命令数量
     */
    public int getActiveCommands() {
        return activeCommands.get();
    }
    
    /**
     * 获取最大并发命令数量
     * @return 最大并发命令数量
     */
    public int getMaxConcurrentCommands() {
        return maxConcurrentCommands;
    }
    
    /**
     * 检查命令执行器是否关闭
     * @return 是否关闭
     */
    public boolean isShutdown() {
        return executorService.isShutdown();
    }
}
