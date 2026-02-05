package net.ooder.skillcenter.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

/**
 * 控制台主类，负责初始化和启动控制台
 */
public class ShellConsole {
    
    // 命令注册中心
    private CommandRegistry commandRegistry;
    
    // 命令输出
    private CommandOutput output;
    
    // 历史命令
    private java.util.List<String> history;
    
    // 是否运行中
    private boolean running;
    
    /**
     * 构造方法
     */
    public ShellConsole() {
        this.commandRegistry = new CommandRegistry();
        this.output = new CommandOutput();
        this.history = new java.util.ArrayList<>();
        this.running = false;
        
        // 注册内置命令
        registerBuiltinCommands();
    }
    
    /**
     * 注册内置命令
     */
    private void registerBuiltinCommands() {
        // 帮助命令
        commandRegistry.registerCommand("help", new HelpCommand(this));
        
        // 退出命令
        commandRegistry.registerCommand("exit", new ExitCommand(this));
        
        // 安全管理命令
        commandRegistry.registerCommand("security key generate", new net.ooder.skillcenter.shell.security.SecurityKeyGenerateCommand());
        commandRegistry.registerCommand("security key list", new net.ooder.skillcenter.shell.security.SecurityKeyListCommand());
        commandRegistry.registerCommand("security key info", new net.ooder.skillcenter.shell.security.SecurityKeyInfoCommand());
        commandRegistry.registerCommand("security key distribute", new net.ooder.skillcenter.shell.security.SecurityKeyDistributeCommand());
        commandRegistry.registerCommand("security key revoke", new net.ooder.skillcenter.shell.security.SecurityKeyRevokeCommand());
        commandRegistry.registerCommand("security key rotate", new net.ooder.skillcenter.shell.security.SecurityKeyRotateCommand());
        commandRegistry.registerCommand("security key validate", new net.ooder.skillcenter.shell.security.SecurityKeyValidateCommand());
        commandRegistry.registerCommand("security key audit", new net.ooder.skillcenter.shell.security.SecurityKeyAuditCommand());
        
        // P2P 发现命令
        commandRegistry.registerCommand("p2p discover", new net.ooder.skillcenter.shell.p2p.P2PDiscoverCommand());
        commandRegistry.registerCommand("p2p list", new net.ooder.skillcenter.shell.p2p.P2PListCommand());
        commandRegistry.registerCommand("p2p refresh", new net.ooder.skillcenter.shell.p2p.P2PRefreshCommand());
        commandRegistry.registerCommand("p2p info", new net.ooder.skillcenter.shell.p2p.P2PInfoCommand());
        
        // 技能生命周期命令
        commandRegistry.registerCommand("skill register", new net.ooder.skillcenter.shell.lifecycle.SkillRegisterCommand());
        commandRegistry.registerCommand("skill update", new net.ooder.skillcenter.shell.lifecycle.SkillUpdateCommand());
        commandRegistry.registerCommand("skill delete", new net.ooder.skillcenter.shell.lifecycle.SkillDeleteCommand());
        commandRegistry.registerCommand("skill approve", new net.ooder.skillcenter.shell.lifecycle.SkillApproveCommand());
        commandRegistry.registerCommand("skill reject", new net.ooder.skillcenter.shell.lifecycle.SkillRejectCommand());
        commandRegistry.registerCommand("skill execute", new net.ooder.skillcenter.shell.lifecycle.SkillExecuteCommand());
        commandRegistry.registerCommand("skill history", new net.ooder.skillcenter.shell.lifecycle.SkillHistoryCommand());
        commandRegistry.registerCommand("skill traffic", new net.ooder.skillcenter.shell.lifecycle.SkillTrafficCommand());
        
        // 原有技能管理命令（保留兼容性）
        commandRegistry.registerCommand("skill list", new SkillListCommand());
        commandRegistry.registerCommand("skill info", new SkillInfoCommand());
        commandRegistry.registerCommand("skill publish", new SkillPublishCommand());
        commandRegistry.registerCommand("skill categories", new SkillCategoriesCommand());
        
        // 市场管理命令
        commandRegistry.registerCommand("market search", new MarketSearchCommand());
        commandRegistry.registerCommand("market rate", new MarketRateCommand());
        commandRegistry.registerCommand("market reviews", new MarketReviewsCommand());
        commandRegistry.registerCommand("market popular", new MarketPopularCommand());
        commandRegistry.registerCommand("market latest", new MarketLatestCommand());
        commandRegistry.registerCommand("market download", new MarketDownloadCommand());
        
        // 执行管理命令
        commandRegistry.registerCommand("execute run", new ExecuteRunCommand());
        commandRegistry.registerCommand("execute async", new ExecuteAsyncCommand());
        commandRegistry.registerCommand("execute history", new ExecuteHistoryCommand());
        commandRegistry.registerCommand("execute status", new ExecuteStatusCommand());
        commandRegistry.registerCommand("execute result", new ExecuteResultCommand());
        
        // 存储管理命令
        commandRegistry.registerCommand("storage status", new StorageStatusCommand());
        commandRegistry.registerCommand("storage clean", new StorageCleanCommand());
        commandRegistry.registerCommand("storage backup", new StorageBackupCommand());
        commandRegistry.registerCommand("storage restore", new StorageRestoreCommand());
        commandRegistry.registerCommand("storage stats", new StorageStatsCommand());
        
        // 系统管理命令
        commandRegistry.registerCommand("system status", new SystemStatusCommand());
        commandRegistry.registerCommand("system config", new SystemConfigCommand());
        commandRegistry.registerCommand("system log", new SystemLogCommand());
        commandRegistry.registerCommand("system monitor", new SystemMonitorCommand());
        commandRegistry.registerCommand("system shutdown", new SystemShutdownCommand());
        commandRegistry.registerCommand("system restart", new SystemRestartCommand());
    }
    
    /**
     * 启动控制台
     */
    public void start() {
        running = true;
        output.println("==============================================");
        output.println("SkillCenter Shell Console");
        output.println("==============================================");
        output.println("Type 'help' to see available commands");
        output.println("Type 'exit' to quit the console");
        output.println("==============================================");
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (running) {
                output.print("skillcenter> ");
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                
                // 记录历史命令
                history.add(line);
                
                // 解析和执行命令
                executeCommand(line);
            }
        } catch (IOException e) {
            output.error("Error reading input: " + e.getMessage());
        }
        
        output.println("==============================================");
        output.println("Shell Console exited");
        output.println("==============================================");
    }
    
    /**
     * 停止控制台
     */
    public void stop() {
        running = false;
    }
    
    /**
     * 执行命令
     * @param commandLine 命令行
     */
    public void executeCommand(String commandLine) {
        String[] parts = commandLine.split("\\s+");
        if (parts.length == 0) {
            return;
        }
        
        // 尝试匹配完整命令
        String fullCommand = commandLine;
        Command command = commandRegistry.getCommand(fullCommand);
        
        // 如果没有找到完整命令，尝试匹配前缀
        if (command == null) {
            // 尝试不同长度的前缀
            for (int i = parts.length; i > 0; i--) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < i; j++) {
                    if (j > 0) sb.append(" ");
                    sb.append(parts[j]);
                }
                String partialCommand = sb.toString();
                command = commandRegistry.getCommand(partialCommand);
                if (command != null) {
                    // 提取参数
                    String[] args = new String[parts.length - i];
                    System.arraycopy(parts, i, args, 0, args.length);
                    try {
                        command.execute(args);
                    } catch (Exception e) {
                        output.error("Command execution failed: " + e.getMessage());
                        e.printStackTrace();
                    }
                    return;
                }
            }
            
            // 没有找到命令
            output.error("Unknown command: " + commandLine);
            output.println("Type 'help' to see available commands");
        } else {
            // 执行完整命令
            try {
                command.execute(new String[0]);
            } catch (Exception e) {
                output.error("Command execution failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 获取命令注册中心
     * @return 命令注册中心
     */
    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }
    
    /**
     * 获取命令输出
     * @return 命令输出
     */
    public CommandOutput getOutput() {
        return output;
    }
    
    /**
     * 打印方法
     * @param message 消息
     */
    public void println(String message) {
        output.println(message);
    }
    
    /**
     * 打印方法
     * @param message 消息
     */
    public void print(String message) {
        System.out.print(message);
    }
}
