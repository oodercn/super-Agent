package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.market.SDKSkillStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 存储备份命令，备份存储系统中的数据
 */
public class StorageBackupCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "storage backup";
    }
    
    @Override
    public String getDescription() {
        return "备份存储系统中的数据";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        // 检查存储目录是否存在
        File storageDir = new File(SDKSkillStorage.STORAGE_DIR);
        if (!storageDir.exists() || !storageDir.isDirectory()) {
            output.error("存储目录不存在");
            return;
        }
        
        // 创建备份文件名
        String timestamp = String.valueOf(System.currentTimeMillis());
        String backupFile = "skillcenter-backup-" + timestamp + ".zip";
        File backup = new File(backupFile);
        
        output.println("==============================================");
        output.println("备份存储数据");
        output.println("==============================================");
        output.println("正在创建备份文件: " + backupFile);
        output.println("==============================================");
        
        // 创建ZIP文件
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(backup))) {
            // 添加存储文件到ZIP
            addFileToZip(zos, new File(SDKSkillStorage.STORAGE_DIR, SDKSkillStorage.SKILL_LISTINGS_FILE), "");
            addFileToZip(zos, new File(SDKSkillStorage.STORAGE_DIR, SDKSkillStorage.SKILL_RATINGS_FILE), "");
            addFileToZip(zos, new File(SDKSkillStorage.STORAGE_DIR, SDKSkillStorage.SKILL_REVIEWS_FILE), "");
        }
        
        output.println("==============================================");
        output.success("备份成功!");
        output.println("备份文件: " + backupFile);
        output.println("文件大小: " + backup.length() + " bytes");
        output.println("==============================================");
    }
    
    /**
     * 将文件添加到ZIP中
     */
    private void addFileToZip(ZipOutputStream zos, File file, String path) throws IOException {
        if (!file.exists()) {
            return;
        }
        
        String entryName = path + file.getName();
        ZipEntry entry = new ZipEntry(entryName);
        zos.putNextEntry(entry);
        
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
        }
        
        zos.closeEntry();
    }
}
