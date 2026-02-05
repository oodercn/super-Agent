package net.ooder.skillcenter.shell;

import java.util.Map;
import java.util.Set;

/**
 * 帮助命令，显示所有可用命令
 */
public class HelpCommand extends AbstractCommand {
    
    private ShellConsole console;
    
    /**
     * 构造方法
     * @param console 控制台实例
     */
    public HelpCommand(ShellConsole console) {
        this.console = console;
    }
    
    @Override
    public String getName() {
        return "help";
    }
    
    @Override
    public String getDescription() {
        return "显示所有可用命令";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        CommandRegistry registry = console.getCommandRegistry();
        Map<String, Command> commands = registry.getAllCommands();
        Map<String, Set<String>> groups = registry.getCommandGroups();
        
        output.println("==============================================");
        output.println("SkillCenter 控制台命令帮助");
        output.println("==============================================");
        
        // 按分组显示命令
        for (Map.Entry<String, Set<String>> groupEntry : groups.entrySet()) {
            String group = groupEntry.getKey();
            Set<String> commandNames = groupEntry.getValue();
            
            output.println("");
            output.info(group.toUpperCase() + " 命令:");
            output.println("----------------------------------------------");
            
            for (String commandName : commandNames) {
                Command command = commands.get(commandName);
                if (command != null) {
                    output.println(String.format("  %-20s %s", commandName, command.getDescription()));
                }
            }
        }
        
        output.println("");
        output.println("==============================================");
        output.println("使用示例: skill list");
        output.println("==============================================");
    }
}
