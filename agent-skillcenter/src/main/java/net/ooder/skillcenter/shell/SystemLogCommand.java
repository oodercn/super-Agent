package net.ooder.skillcenter.shell;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * 系统日志命令，查看系统日志
 */
public class SystemLogCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "system log";
    }
    
    @Override
    public String getDescription() {
        return "查看系统日志";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        // 检查日志文件是否存在
        File logFile = new File("skillcenter.log");
        
        if (!logFile.exists() || !logFile.isFile()) {
            output.warn("日志文件不存在: " + logFile.getPath());
            output.println("系统尚未生成日志文件");
            return;
        }
        
        output.println("==============================================");
        output.println("系统日志");
        output.println("==============================================");
        output.println("日志文件: " + logFile.getPath());
        output.println("文件大小: " + logFile.length() + " bytes");
        output.println("==============================================");
        
        // 读取并显示日志文件内容
        try (Scanner scanner = new Scanner(new FileInputStream(logFile))) {
            int lineCount = 0;
            int maxLines = 100; // 限制显示行数
            
            while (scanner.hasNextLine() && lineCount < maxLines) {
                String line = scanner.nextLine();
                output.println(line);
                lineCount++;
            }
            
            if (scanner.hasNextLine()) {
                output.println("... (日志内容过长，已截断)");
                output.println("注: 仅显示最后 " + maxLines + " 行日志");
            }
        }
        
        output.println("==============================================");
    }
}
