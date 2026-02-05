package net.ooder.skillcenter.shell;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 命令注册中心，负责注册和管理所有命令
 */
public class CommandRegistry {
    
    // 命令映射，key为命令名称，value为命令实例
    private Map<String, Command> commands;
    
    // 命令分组映射，key为分组名称，value为命令集合
    private Map<String, Set<String>> commandGroups;
    
    /**
     * 构造方法
     */
    public CommandRegistry() {
        this.commands = new HashMap<>();
        this.commandGroups = new HashMap<>();
    }
    
    /**
     * 注册命令
     * @param name 命令名称
     * @param command 命令实例
     */
    public void registerCommand(String name, Command command) {
        commands.put(name, command);
        
        // 自动分组，根据命令名称的前缀
        String[] parts = name.split(" ");
        if (parts.length > 0) {
            String group = parts[0];
            commandGroups.computeIfAbsent(group, k -> new java.util.HashSet<>()).add(name);
        }
    }
    
    /**
     * 注册命令到指定分组
     * @param name 命令名称
     * @param command 命令实例
     * @param group 分组名称
     */
    public void registerCommand(String name, Command command, String group) {
        registerCommand(name, command);
        commandGroups.computeIfAbsent(group, k -> new java.util.HashSet<>()).add(name);
    }
    
    /**
     * 获取命令
     * @param name 命令名称
     * @return 命令实例
     */
    public Command getCommand(String name) {
        return commands.get(name);
    }
    
    /**
     * 检查命令是否存在
     * @param name 命令名称
     * @return true如果命令存在，false否则
     */
    public boolean hasCommand(String name) {
        return commands.containsKey(name);
    }
    
    /**
     * 获取所有命令
     * @return 命令映射
     */
    public Map<String, Command> getAllCommands() {
        return new HashMap<>(commands);
    }
    
    /**
     * 获取命令分组
     * @return 命令分组映射
     */
    public Map<String, Set<String>> getCommandGroups() {
        return new HashMap<>(commandGroups);
    }
    
    /**
     * 获取指定分组的命令
     * @param group 分组名称
     * @return 命令名称集合
     */
    public Set<String> getCommandsByGroup(String group) {
        return commandGroups.getOrDefault(group, new java.util.HashSet<>());
    }
    
    /**
     * 移除命令
     * @param name 命令名称
     * @return 被移除的命令实例
     */
    public Command removeCommand(String name) {
        Command command = commands.remove(name);
        
        // 从分组中移除
        for (Map.Entry<String, Set<String>> entry : commandGroups.entrySet()) {
            entry.getValue().remove(name);
            if (entry.getValue().isEmpty()) {
                commandGroups.remove(entry.getKey());
            }
        }
        
        return command;
    }
    
    /**
     * 清空所有命令
     */
    public void clear() {
        commands.clear();
        commandGroups.clear();
    }
    
    /**
     * 获取命令数量
     * @return 命令数量
     */
    public int size() {
        return commands.size();
    }
}
