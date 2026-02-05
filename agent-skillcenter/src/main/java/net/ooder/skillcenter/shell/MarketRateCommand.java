package net.ooder.skillcenter.shell;

import java.util.Scanner;

/**
 * 市场评分命令，为技能打分
 */
public class MarketRateCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "market rate";
    }
    
    @Override
    public String getDescription() {
        return "为技能打分";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            output.error("请指定技能ID和评分");
            output.println("使用示例: market rate <skill-id> <rating>");
            output.println("评分范围: 1-5星");
            return;
        }
        
        String skillId = args[0];
        int rating;
        
        try {
            rating = Integer.parseInt(args[1]);
            if (rating < 1 || rating > 5) {
                output.error("评分必须在1-5星之间");
                return;
            }
        } catch (NumberFormatException e) {
            output.error("评分必须是数字");
            return;
        }
        
        // 确认技能存在
        if (marketManager.getSkillListing(skillId) == null) {
            output.error("技能不存在: " + skillId);
            return;
        }
        
        Scanner scanner = new Scanner(System.in);
        
        output.print("请输入评论内容 (可选): ");
        String comment = scanner.nextLine();
        
        // 为技能评分
        marketManager.rateSkill(skillId, (double) rating, comment, "user123");
        
        output.println("==============================================");
        output.success("评分成功!");
        output.println("技能ID: " + skillId);
        output.println("评分: " + rating + "星");
        if (!comment.isEmpty()) {
            output.println("评论: " + comment);
        }
        output.println("==============================================");
    }
}
