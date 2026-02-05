package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.market.SkillListing;

/**
 * 市场下载命令，下载技能
 */
public class MarketDownloadCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "market download";
    }
    
    @Override
    public String getDescription() {
        return "下载技能";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length == 0) {
            output.error("请指定技能ID");
            output.println("使用示例: market download <skill-id>");
            return;
        }
        
        String skillId = args[0];
        SkillListing listing = marketManager.getSkillListing(skillId);
        
        if (listing == null) {
            output.error("技能不存在: " + skillId);
            return;
        }
        
        // 模拟下载过程
        output.println("==============================================");
        output.println("正在下载技能");
        output.println("==============================================");
        output.println("技能: " + listing.getName() + " (v" + listing.getVersion() + ")");
        output.println("分类: " + listing.getCategory());
        output.println("发布者: " + listing.getAuthor());
        output.println("==============================================");
        
        // 模拟下载进度
        for (int i = 0; i <= 100; i += 10) {
            Thread.sleep(200); // 模拟下载延迟
            output.print("下载进度: " + i + "%\r");
        }
        output.println("下载进度: 100%");
        
        // 更新下载计数
        listing.setDownloadCount(listing.getDownloadCount() + 1);
        marketManager.updateSkill(listing);
        
        output.println("==============================================");
        output.success("技能下载成功!");
        output.println("技能: " + listing.getName());
        output.println("版本: " + listing.getVersion());
        output.println("下载次数: " + listing.getDownloadCount());
        output.println("==============================================");
    }
}
