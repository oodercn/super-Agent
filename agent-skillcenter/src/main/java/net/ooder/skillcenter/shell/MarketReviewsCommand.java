package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.market.SkillReview;

import java.util.List;

/**
 * 市场评论命令，查看技能的评论
 */
public class MarketReviewsCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "market reviews";
    }
    
    @Override
    public String getDescription() {
        return "查看技能的评论";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length == 0) {
            output.error("请指定技能ID");
            output.println("使用示例: market reviews <skill-id>");
            return;
        }
        
        String skillId = args[0];
        
        // 确认技能存在
        if (marketManager.getSkillListing(skillId) == null) {
            output.error("技能不存在: " + skillId);
            return;
        }
        
        List<SkillReview> reviews = marketManager.getSkillReviews(skillId);
        
        if (reviews.isEmpty()) {
            output.warn("该技能暂无评论");
            return;
        }
        
        output.println("==============================================");
        output.println("技能评论");
        output.println("==============================================");
        
        for (SkillReview review : reviews) {
            output.println("用户: " + review.getUserId());
            output.println("评分: " + review.getRating() + "星");
            output.println("时间: " + new java.util.Date(review.getTimestamp()));
            output.println("内容: " + review.getComment());
            output.println("----------------------------------------------");
        }
        
        output.println("==============================================");
        output.println("总计: " + reviews.size() + " 条评论");
        output.println("==============================================");
    }
}
