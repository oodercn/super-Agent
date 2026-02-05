package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.market.SDKSkillStorage;

import java.io.File;

/**
 * 存储状态命令，查看存储系统的状态
 */
public class StorageStatusCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "storage status";
    }
    
    @Override
    public String getDescription() {
        return "查看存储系统的状态";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        // 检查存储文件是否存在
        File listingsFile = new File(SDKSkillStorage.STORAGE_DIR, SDKSkillStorage.SKILL_LISTINGS_FILE);
        File ratingsFile = new File(SDKSkillStorage.STORAGE_DIR, SDKSkillStorage.SKILL_RATINGS_FILE);
        File reviewsFile = new File(SDKSkillStorage.STORAGE_DIR, SDKSkillStorage.SKILL_REVIEWS_FILE);
        
        output.println("==============================================");
        output.println("存储系统状态");
        output.println("==============================================");
        output.println("存储目录: " + SDKSkillStorage.STORAGE_DIR);
        output.println("==============================================");
        output.println("技能列表文件: " + (listingsFile.exists() ? "存在" : "不存在"));
        if (listingsFile.exists()) {
            output.println("文件大小: " + listingsFile.length() + " bytes");
        }
        output.println("技能评分文件: " + (ratingsFile.exists() ? "存在" : "不存在"));
        if (ratingsFile.exists()) {
            output.println("文件大小: " + ratingsFile.length() + " bytes");
        }
        output.println("技能评论文件: " + (reviewsFile.exists() ? "存在" : "不存在"));
        if (reviewsFile.exists()) {
            output.println("文件大小: " + reviewsFile.length() + " bytes");
        }
        output.println("==============================================");
        output.println("存储状态: " + (listingsFile.exists() || ratingsFile.exists() || reviewsFile.exists() ? "正常" : "空"));
        output.println("==============================================");
    }
}
