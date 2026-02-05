package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.market.SDKSkillStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 存储恢复命令，从备份文件中恢复存储数据
 */
public class StorageRestoreCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "storage restore";
    }
    
    @Override
    public String getDescription() {
        return "从备份文件中恢复存储数据";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length == 0) {
            output.error("请指定备份文件路径");
            output.println("使用示例: storage restore <backup-file>");
            return;
        }
        
        String backupFile = args[0];
        File backup = new File(backupFile);
        
        if (!backup.exists() || !backup.isFile()) {
            output.error("备份文件不存在: " + backupFile);
            return;
        }
        
        Scanner scanner = new Scanner(System.in);
        
        output.println("==============================================");
        output.println("恢复存储数据");
        output.println("==============================================");
        output.warn("警告: 此操作将覆盖当前所有存储的技能数据!");
        output.println("==============================================");
        output.print("确认恢复? (y/n): ");
        
        String confirmation = scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("y")) {
            output.info("操作已取消");
            return;
        }
        
        // 确保存储目录存在
        File storageDir = new File(SDKSkillStorage.STORAGE_DIR);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        
        output.println("==============================================");
        output.println("正在从备份文件恢复: " + backupFile);
        output.println("==============================================");
        
        // 从ZIP文件中提取数据
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(backup))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();
                File outputFile = new File(SDKSkillStorage.STORAGE_DIR, entryName);
                
                // 写入文件
                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
                
                zis.closeEntry();
                output.println("已恢复: " + entryName);
            }
        }
        
        // 重新初始化存储
        skillStorage.initialize();
        
        output.println("==============================================");
        output.success("恢复成功!");
        output.println("从备份文件恢复完成: " + backupFile);
        output.println("==============================================");
    }
}
