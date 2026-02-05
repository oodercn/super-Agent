package net.ooder.skillcenter.shell;

import java.util.Scanner;

/**
 * 系统重启命令，重启系统
 */
public class SystemRestartCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "system restart";
    }
    
    @Override
    public String getDescription() {
        return "重启系统";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        
        output.println("==============================================");
        output.println("重启系统");
        output.println("==============================================");
        output.warn("警告: 此操作将重启整个SkillCenter系统!");
        output.println("==============================================");
        output.print("确认重启? (y/n): ");
        
        String confirmation = scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("y")) {
            output.info("操作已取消");
            return;
        }
        
        output.println("==============================================");
        output.println("正在重启系统...");
        output.println("==============================================");
        
        // 执行重启操作
        // 1. 关闭存储
        skillStorage.close();
        
        // 2. 关闭执行引擎
        executorEngine.shutdown();
        
        output.println("存储系统已关闭");
        output.println("执行引擎已关闭");
        output.println("==============================================");
        output.println("正在重新启动...");
        output.println("==============================================");
        
        // 3. 重新初始化存储
        skillStorage.initialize();
        
        // 4. 执行引擎会在下次使用时自动初始化
        
        output.println("存储系统已重新初始化");
        output.println("执行引擎已重新启动");
        output.println("==============================================");
        output.success("系统已成功重启");
        output.println("==============================================");
    }
}
