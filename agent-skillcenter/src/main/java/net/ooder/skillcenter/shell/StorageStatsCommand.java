package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.market.SkillListing;
import net.ooder.skillcenter.market.SkillReview;

import java.util.List;

/**
 * 存储统计命令，显示存储系统的统计信息
 */
public class StorageStatsCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "storage stats";
    }
    
    @Override
    public String getDescription() {
        return "显示存储系统的统计信息";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        // 获取所有技能列表
        List<SkillListing> listings = marketManager.getAllSkills();
        
        // 统计评论数
        int totalReviews = 0;
        for (SkillListing listing : listings) {
            List<SkillReview> reviews = marketManager.getSkillReviews(listing.getSkillId());
            totalReviews += reviews.size();
        }
        
        // 统计下载量
        int totalDownloads = 0;
        double totalRatings = 0;
        int ratedSkills = 0;
        
        for (SkillListing listing : listings) {
            totalDownloads += listing.getDownloadCount();
            if (listing.getRating() > 0) {
                totalRatings += listing.getRating();
                ratedSkills++;
            }
        }
        
        double avgRating = ratedSkills > 0 ? totalRatings / ratedSkills : 0;
        
        output.println("==============================================");
        output.println("存储统计信息");
        output.println("==============================================");
        output.println("技能总数: " + listings.size());
        output.println("评论总数: " + totalReviews);
        output.println("总下载量: " + totalDownloads);
        output.println("已评分技能: " + ratedSkills);
        output.println("平均评分: " + String.format("%.2f", avgRating));
        output.println("==============================================");
        
        if (!listings.isEmpty()) {
            // 显示排名前三的技能
            output.println("下载量排名前三的技能:");
            output.println("----------------------------------------------");
            
            listings.sort((a, b) -> Integer.compare(b.getDownloadCount(), a.getDownloadCount()));
            int limit = Math.min(3, listings.size());
            
            for (int i = 0; i < limit; i++) {
                SkillListing listing = listings.get(i);
                output.println((i + 1) + ". " + listing.getName() + " - " + listing.getDownloadCount() + " 次下载");
            }
            
            output.println("==============================================");
        }
    }
}
