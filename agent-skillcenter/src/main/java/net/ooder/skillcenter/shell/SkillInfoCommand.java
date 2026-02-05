package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.market.SkillListing;
import net.ooder.skillcenter.market.SkillReview;

import java.util.List;

/**
 * 技能信息命令，显示指定技能的详细信息
 */
public class SkillInfoCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "skill info";
    }
    
    @Override
    public String getDescription() {
        return "显示指定技能的详细信息";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length == 0) {
            output.error("请指定技能ID");
            output.println("使用示例: skill info <skill-id>");
            return;
        }
        
        String skillId = args[0];
        SkillListing listing = marketManager.getSkillListing(skillId);
        
        if (listing == null) {
            output.error("技能不存在: " + skillId);
            return;
        }
        
        output.println("==============================================");
        output.println("技能详细信息");
        output.println("==============================================");
        output.println("ID: " + listing.getSkillId());
        output.println("名称: " + listing.getName());
        output.println("版本: " + listing.getVersion());
        output.println("分类: " + listing.getCategory());
        output.println("发布者: " + listing.getAuthor());
        output.println("最后更新: " + new java.util.Date(listing.getLastUpdated()));
        output.println("评分: " + String.format("%.1f", listing.getRating()));
        output.println("下载量: " + listing.getDownloadCount());
        output.println("描述: " + listing.getDescription());
        output.println("==============================================");
        
        // 显示评论
        List<SkillReview> reviews = marketManager.getSkillReviews(skillId);
        if (!reviews.isEmpty()) {
            output.println("评论:");
            output.println("----------------------------------------------");
            for (SkillReview review : reviews) {
                output.println("用户: " + review.getUserId() + " (" + review.getRating() + "星)");
                output.println("时间: " + new java.util.Date(review.getTimestamp()));
                output.println("内容: " + review.getComment());
                output.println("----------------------------------------------");
            }
        } else {
            output.warn("暂无评论");
        }
        
        output.println("==============================================");
    }
}
