package net.ooder.skillcenter.shell;

import java.util.Scanner;

/**
 * 系统关闭命令，关闭系统
 */
public class SystemShutdownCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "system shutdown";
    }
    
    @Override
    public String getDescription() {
        return "关闭系统";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        
        output.println("==============================================");
        output.println("关闭系统");
        output.println("==============================================");
        output.warn("警告: 此操作将关闭整个SkillCenter系统!");
        output.println("==============================================");
        output.print("确认关闭? (y/n): ");
        
        String confirmation = scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("y")) {
            output.info("操作已取消");
            return;
        }
        
        output.println("==============================================");
        output.println("正在关闭系统...");
        output.println("==============================================");
        
        // 执行关闭操作
        // 1. 关闭存储
        skillStorage.close();
        
        // 2. 关闭执行引擎
        executorEngine.shutdown();
        
        // 3. 显示关闭信息
        output.println("存储系统已关闭");
        output.println("执行引擎已关闭");
        output.println("==============================================");
        output.success("系统已成功关闭");
        output.println("==============================================");
        
        // 退出JVM
        System.exit(0);
    }
}
