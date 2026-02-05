package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.market.SkillListing;

import java.util.List;

/**
 * 市场热门技能命令，显示最受欢迎的技能
 */
public class MarketPopularCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "market popular";
    }
    
    @Override
    public String getDescription() {
        return "显示最受欢迎的技能";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        List<SkillListing> listings = marketManager.getAllSkills();
        
        if (listings.isEmpty()) {
            output.warn("暂无已发布的技能");
            return;
        }
        
        // 按下载量排序
        listings.sort((a, b) -> Integer.compare(b.getDownloadCount(), a.getDownloadCount()));
        
        // 限制显示数量
        int limit = 10;
        if (listings.size() > limit) {
            listings = listings.subList(0, limit);
        }
        
        output.println("==============================================");
        output.println("热门技能排行榜");
        output.println("==============================================");
        
        // 准备表格数据
        String[] headers = {"排名", "名称", "版本", "分类", "评分", "下载量"};
        String[][] rows = new String[listings.size()][headers.length];
        
        for (int i = 0; i < listings.size(); i++) {
            SkillListing listing = listings.get(i);
            rows[i][0] = String.valueOf(i + 1);
            rows[i][1] = listing.getName();
            rows[i][2] = listing.getVersion();
            rows[i][3] = listing.getCategory();
            rows[i][4] = String.format("%.1f", listing.getRating());
            rows[i][5] = String.valueOf(listing.getDownloadCount());
        }
        
        output.table(headers, rows);
        output.println("==============================================");
        output.println("注: 按下载量排序");
        output.println("==============================================");
    }
}
