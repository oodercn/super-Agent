package net.ooder.skillcenter.shell;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Date;

/**
 * 系统状态命令，显示系统的运行状态
 */
public class SystemStatusCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "system status";
    }
    
    @Override
    public String getDescription() {
        return "显示系统的运行状态";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        // 获取系统信息
        Runtime runtime = Runtime.getRuntime();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemory = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemory = memoryBean.getNonHeapMemoryUsage();
        
        // 计算内存使用情况
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        
        // 获取处理器数量
        int processors = runtime.availableProcessors();
        
        // 获取系统启动时间
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000;
        long uptimeHours = uptime / 3600;
        long uptimeMinutes = (uptime % 3600) / 60;
        long uptimeSeconds = uptime % 60;
        
        output.println("==============================================");
        output.println("系统运行状态");
        output.println("==============================================");
        output.println("当前时间: " + new Date());
        output.println("系统启动时间: " + uptimeHours + "h " + uptimeMinutes + "m " + uptimeSeconds + "s");
        output.println("处理器数量: " + processors);
        output.println("==============================================");
        output.println("内存使用情况:");
        output.println("总内存: " + totalMemory + " MB");
        output.println("已用内存: " + usedMemory + " MB");
        output.println("可用内存: " + freeMemory + " MB");
        output.println("最大内存: " + maxMemory + " MB");
        output.println("堆内存使用: " + heapMemory.getUsed() / 1024 / 1024 + " MB / " + heapMemory.getMax() / 1024 / 1024 + " MB");
        output.println("非堆内存使用: " + nonHeapMemory.getUsed() / 1024 / 1024 + " MB / " + nonHeapMemory.getMax() / 1024 / 1024 + " MB");
        output.println("==============================================");
        output.println("存储状态:");
        output.println("技能总数: " + marketManager.getAllSkills().size());
        output.println("执行引擎状态: 运行中");
        output.println("==============================================");
    }
}
