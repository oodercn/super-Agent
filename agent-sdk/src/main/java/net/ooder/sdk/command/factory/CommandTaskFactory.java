package net.ooder.sdk.command.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.impl.AbstractCommandTask;
import net.ooder.sdk.command.model.CommandResult;
import net.ooder.sdk.command.api.CommandTask;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.system.container.SimpleIOCContainer;
import net.ooder.sdk.network.packet.CommandPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 命令任务工厂，用于创建和管理命令任务实例
 */
public class CommandTaskFactory {
    private static final CommandTaskFactory INSTANCE = new CommandTaskFactory();
    private final Map<CommandType, Class<? extends CommandTask>> commandTaskMap = new HashMap<>();
    private final Map<String, CommandType> commandKeyMap = new HashMap<>();
    private AgentSDK agentSDK;

    private CommandTaskFactory() {
        // 私有构造方法，实现单例模式
    }

    public static CommandTaskFactory getInstance() {
        return INSTANCE;
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
     * 注册命令任务类
     *
     * @param commandType 命令类型
     * @param taskClass   命令任务类
     */
    public void registerCommandTask(CommandType commandType, Class<? extends CommandTask> taskClass) {
        commandTaskMap.put(commandType, taskClass);
        // 使用commandType的值作为默认key
        commandKeyMap.put(commandType.getValue(), commandType);
    }

    /**
     * 注册命令任务类，使用指定的key
     *
     * @param commandKey  命令key
     * @param commandType 命令类型
     * @param taskClass   命令任务类
     */
    public void registerCommandTask(String commandKey, CommandType commandType, Class<? extends CommandTask> taskClass) {
        commandTaskMap.put(commandType, taskClass);
        commandKeyMap.put(commandKey, commandType);
    }

    /**
     * 通过key获取命令类型
     *
     * @param commandKey 命令key
     * @return 命令类型
     */
    public CommandType getCommandTypeByKey(String commandKey) {
        return commandKeyMap.get(commandKey);
    }

    /**
     * 通过key创建命令任务实例
     *
     * @param commandKey 命令key
     * @return 命令任务实例
     * @throws Exception 创建命令任务失败时抛出异常
     */
    public CommandTask createCommandTaskByKey(String commandKey) throws Exception {
        CommandType commandType = commandKeyMap.get(commandKey);
        if (commandType == null) {
            throw new IllegalArgumentException("No command task registered for key: " + commandKey);
        }
        return createCommandTask(commandType);
    }

    /**
     * 通过key执行命令任务
     *
     * @param commandKey 命令key
     * @param packet     命令数据包
     * @return 命令执行结果的CompletableFuture
     */
    public CompletableFuture<CommandResult> executeCommandByKey(String commandKey, CommandPacket packet) {
        try {
            CommandType commandType = commandKeyMap.get(commandKey);
            if (commandType == null) {
                throw new IllegalArgumentException("No command task registered for key: " + commandKey);
            }
            return executeCommand(commandType, packet);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    CommandResult.failed("Failed to execute command by key: " + e.getMessage())
            );
        }
    }

    /**
     * 创建命令任务实例
     *
     * @param commandType 命令类型
     * @return 命令任务实例
     * @throws Exception 创建命令任务失败时抛出异常
     */
    public CommandTask createCommandTask(CommandType commandType) throws Exception {
        Class<? extends CommandTask> taskClass = commandTaskMap.get(commandType);
        if (taskClass == null) {
            throw new IllegalArgumentException("No command task registered for type: " + commandType);
        }

        // 使用IOC容器获取或创建实例
        SimpleIOCContainer container = SimpleIOCContainer.getInstance();

        // 检查容器中是否已有该类型的实例
        CommandTask task;
        if (container.hasInstance(taskClass)) {
            task = container.getInstance(taskClass);
        } else {
            // 创建新实例
            task = taskClass.newInstance();
            if (task instanceof AbstractCommandTask) {
                ((AbstractCommandTask) task).setAgentSDK(agentSDK);
            }
            // 注册到容器中
            container.registerInstance((Class<CommandTask>) taskClass, task);
        }

        return task;
    }

    /**
     * 执行命令任务
     *
     * @param packet 命令数据包
     * @return 命令执行结果的CompletableFuture
     */
    public CompletableFuture<CommandResult> executeCommand(CommandPacket packet) {
        try {
            CommandType commandType = CommandType.fromValue(packet.getOperation()).orElse(null);
            if (commandType == null) {
                throw new IllegalArgumentException("Unknown command type: " + packet.getOperation());
            }
            CommandTask task = createCommandTask(commandType);
            return task.execute(packet);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    CommandResult.failed("Failed to execute command: " + e.getMessage())
            );
        }
    }

    /**
     * 执行命令任务
     *
     * @param commandType 命令类型
     * @param packet      命令数据包
     * @return 命令执行结果的CompletableFuture
     */
    public CompletableFuture<CommandResult> executeCommand(CommandType commandType, CommandPacket packet) {
        try {
            CommandTask task = createCommandTask(commandType);
            return task.execute(packet);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    CommandResult.failed("Failed to execute command: " + e.getMessage())
            );
        }
    }

    /**
     * 检查是否有注册的命令任务
     *
     * @param commandType 命令类型
     * @return 如果有注册的命令任务则返回true，否则返回false
     */
    public boolean hasCommandTask(CommandType commandType) {
        return commandTaskMap.containsKey(commandType);
    }

    /**
     * 从JSON字符串创建命令对象
     *
     * @param json JSON字符串
     * @return 命令对象
     * @throws Exception 创建命令失败时抛出异常
     */
    public <T extends Command> T createCommandFromJson(String json) throws Exception {
        JSONObject jsonObject = JSON.parseObject(json);
        String commandTypeValue = jsonObject.getString("commandType");

        if (commandTypeValue == null) {
            throw new IllegalArgumentException("Command type is missing in JSON");
        }

        CommandType commandType = CommandType.fromValue(commandTypeValue).orElseThrow(() -> 
            new IllegalArgumentException("Unknown command type: " + commandTypeValue)
        );

        Class<?> commandClass = commandType.getCommandClass();
        if (commandClass == null) {
            throw new IllegalArgumentException("No command class registered for type: " + commandTypeValue);
        }
        return (T) JSON.parseObject(json, commandClass);
    }

    /**
     * 从JSON字符串创建CommandPacket对象
     *
     * @param json JSON字符串
     * @return CommandPacket对象
     * @throws Exception 创建CommandPacket失败时抛出异常
     */
    public <T> CommandPacket<T> createCommandPacketFromJson(String json) throws Exception {
        JSONObject jsonObject = JSON.parseObject(json);
        String commandTypeValue = jsonObject.getString("command");

        if (commandTypeValue == null) {
            throw new IllegalArgumentException("Command type is missing in JSON");
        }

        CommandType commandType = CommandType.fromValue(commandTypeValue).orElseThrow(() -> 
            new IllegalArgumentException("Unknown command type: " + commandTypeValue)
        );

        Class<?> commandClass = commandType.getCommandClass();
        if (commandClass == null) {
            throw new IllegalArgumentException("No command class registered for type: " + commandTypeValue);
        }
        return JSON.parseObject(json, new com.alibaba.fastjson.TypeReference<CommandPacket<T>>(commandClass) {
        });
    }
}