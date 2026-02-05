package net.ooder.skillcenter.shell;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Scanner;

/**
 * 系统监控命令，监控系统的实时状态
 */
public class SystemMonitorCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "system monitor";
    }
    
    @Override
    public String getDescription() {
        return "监控系统的实时状态";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        output.println("==============================================");
        output.println("系统实时监控");
        output.println("==============================================");
        output.println("按 Ctrl+C 退出监控");
        output.println("==============================================");
        
        Scanner scanner = new Scanner(System.in);
        
        // 启动监控线程
        Thread monitorThread = new Thread(() -> {
            try {
                while (!Thread.interrupted()) {
                    // 清除屏幕
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    
                    // 显示监控信息
                    displayMonitorInfo();
                    
                    // 等待1秒
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                // 线程被中断，退出监控
            }
        });
        
        monitorThread.start();
        
        // 等待用户输入
        scanner.nextLine();
        
        // 停止监控线程
        monitorThread.interrupt();
        monitorThread.join();
        
        output.println("==============================================");
        output.println("监控已停止");
        output.println("==============================================");
    }
    
    /**
     * 显示监控信息
     */
    private void displayMonitorInfo() {
        Runtime runtime = Runtime.getRuntime();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemory = memoryBean.getHeapMemoryUsage();
        
        // 计算内存使用情况
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        
        // 计算内存使用率
        double memoryUsagePercent = (double) usedMemory / totalMemory * 100;
        
        // 显示监控信息
        output.println("==============================================");
        output.println("系统实时监控");
        output.println("==============================================");
        output.println("内存使用: " + usedMemory + " MB / " + totalMemory + " MB (" + String.format("%.1f", memoryUsagePercent) + "%)");
        output.println("堆内存: " + heapMemory.getUsed() / 1024 / 1024 + " MB / " + heapMemory.getMax() / 1024 / 1024 + " MB");
        output.println("处理器数量: " + runtime.availableProcessors());
        output.println("技能总数: " + marketManager.getAllSkills().size());
        output.println("==============================================");
        output.println("按 Enter 键退出监控");
        output.println("==============================================");
    }
}
