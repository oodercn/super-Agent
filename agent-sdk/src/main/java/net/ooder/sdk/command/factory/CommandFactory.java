package net.ooder.sdk.command.factory;

import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.api.CommandTask;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.system.factory.Factory;

/**
 * 命令工厂，用于创建命令和命令任务
 */
public interface CommandFactory extends Factory {
    
    /**
     * 创建命令实例
     * @param commandType 命令类型
     * @param <T> 命令类型
     * @return 命令实例
     */
    <T extends Command> T createCommand(CommandType commandType);
    
    /**
     * 创建命令任务
     * @param commandType 命令类型
     * @param <T> 命令任务类型
     * @return 命令任务实例
     */
    <T extends CommandTask> T createCommandTask(CommandType commandType);
    
    /**
     * 注册自定义命令
     * @param commandType 命令类型
     * @param commandClass 命令类
     */
    void registerCommand(CommandType commandType, Class<? extends Command> commandClass);
    
    /**
     * 注册自定义命令任务
     * @param commandType 命令类型
     * @param taskClass 命令任务类
     */
    void registerCommandTask(CommandType commandType, Class<? extends CommandTask> taskClass);
}
