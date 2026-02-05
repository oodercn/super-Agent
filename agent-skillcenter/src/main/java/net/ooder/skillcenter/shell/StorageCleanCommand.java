package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.market.SDKSkillStorage;

import java.io.File;
import java.util.Scanner;

/**
 * 存储清理命令，清理存储系统中的数据
 */
public class StorageCleanCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "storage clean";
    }
    
    @Override
    public String getDescription() {
        return "清理存储系统中的数据";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        
        output.println("==============================================");
        output.println("清理存储数据");
        output.println("==============================================");
        output.warn("警告: 此操作将删除所有存储的技能数据，不可恢复!");
        output.println("==============================================");
        output.print("确认清理? (y/n): ");
        
        String confirmation = scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("y")) {
            output.info("操作已取消");
            return;
        }
        
        // 清理存储文件
        File listingsFile = new File(SDKSkillStorage.STORAGE_DIR, SDKSkillStorage.SKILL_LISTINGS_FILE);
        File ratingsFile = new File(SDKSkillStorage.STORAGE_DIR, SDKSkillStorage.SKILL_RATINGS_FILE);
        File reviewsFile = new File(SDKSkillStorage.STORAGE_DIR, SDKSkillStorage.SKILL_REVIEWS_FILE);
        
        if (listingsFile.exists()) listingsFile.delete();
        if (ratingsFile.exists()) ratingsFile.delete();
        if (reviewsFile.exists()) reviewsFile.delete();
        
        // 重新初始化存储
        skillStorage.initialize();
        
        output.println("==============================================");
        output.success("存储数据清理成功!");
        output.println("所有技能数据已被删除");
        output.println("==============================================");
    }
}
