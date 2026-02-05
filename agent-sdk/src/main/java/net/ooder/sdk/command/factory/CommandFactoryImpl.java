package net.ooder.sdk.command.factory;

import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.api.CommandTask;
import net.ooder.sdk.command.model.CommandType;

import java.util.HashMap;
import java.util.Map;

/**
 * 命令工厂实现类
 */
public class CommandFactoryImpl implements CommandFactory {
    
    private final Map<CommandType, Class<? extends Command>> commandMap = new HashMap<>();
    private final Map<CommandType, Class<? extends CommandTask>> taskMap = new HashMap<>();
    
    @Override
    public <T extends Command> T createCommand(CommandType commandType) {
        Class<? extends Command> commandClass = commandMap.get(commandType);
        if (commandClass == null) {
            throw new IllegalArgumentException("Command type not registered: " + commandType);
        }
        
        try {
            return (T) commandClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create command: " + commandType, e);
        }
    }
    
    @Override
    public <T extends CommandTask> T createCommandTask(CommandType commandType) {
        Class<? extends CommandTask> taskClass = taskMap.get(commandType);
        if (taskClass == null) {
            throw new IllegalArgumentException("Command task not registered: " + commandType);
        }
        
        try {
            return (T) taskClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create command task: " + commandType, e);
        }
    }
    
    @Override
    public void registerCommand(CommandType commandType, Class<? extends Command> commandClass) {
        commandMap.put(commandType, commandClass);
    }
    
    @Override
    public void registerCommandTask(CommandType commandType, Class<? extends CommandTask> taskClass) {
        taskMap.put(commandType, taskClass);
    }
}
