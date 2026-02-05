package net.ooder.skillcenter.shell;

import java.util.Properties;

/**
 * 系统配置命令，查看和修改系统配置
 */
public class SystemConfigCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "system config";
    }
    
    @Override
    public String getDescription() {
        return "查看和修改系统配置";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        // 显示系统配置
        output.println("==============================================");
        output.println("系统配置信息");
        output.println("==============================================");
        
        // 显示Java系统属性
        Properties properties = System.getProperties();
        
        // 显示关键配置
        output.println("Java版本: " + properties.getProperty("java.version"));
        output.println("Java虚拟机: " + properties.getProperty("java.vm.name"));
        output.println("操作系统: " + properties.getProperty("os.name") + " " + properties.getProperty("os.version"));
        output.println("系统架构: " + properties.getProperty("os.arch"));
        output.println("用户目录: " + properties.getProperty("user.home"));
        output.println("工作目录: " + properties.getProperty("user.dir"));
        output.println("==============================================");
        output.println("SkillCenter 配置:");
        output.println("存储目录: " + "skillcenter-storage");
        output.println("执行引擎线程数: " + Runtime.getRuntime().availableProcessors());
        output.println("==============================================");
        output.println("提示: 此命令当前仅显示系统配置，不支持修改配置");
        output.println("==============================================");
    }
}
