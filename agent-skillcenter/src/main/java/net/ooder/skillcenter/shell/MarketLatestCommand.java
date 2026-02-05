package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.market.SkillListing;

import java.util.List;

/**
 * 市场最新技能命令，显示最新发布的技能
 */
public class MarketLatestCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "market latest";
    }
    
    @Override
    public String getDescription() {
        return "显示最新发布的技能";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        List<SkillListing> listings = marketManager.getAllSkills();
        
        if (listings.isEmpty()) {
            output.warn("暂无已发布的技能");
            return;
        }
        
        // 按发布时间排序
        listings.sort((a, b) -> Long.compare(b.getLastUpdated(), a.getLastUpdated()));
        
        // 限制显示数量
        int limit = 10;
        if (listings.size() > limit) {
            listings = listings.subList(0, limit);
        }
        
        output.println("==============================================");
        output.println("最新发布技能");
        output.println("==============================================");
        
        // 准备表格数据
        String[] headers = {"发布时间", "名称", "版本", "分类", "评分", "发布者"};
        String[][] rows = new String[listings.size()][headers.length];
        
        for (int i = 0; i < listings.size(); i++) {
            SkillListing listing = listings.get(i);
            rows[i][0] = new java.util.Date(listing.getLastUpdated()).toString();
            rows[i][1] = listing.getName();
            rows[i][2] = listing.getVersion();
            rows[i][3] = listing.getCategory();
            rows[i][4] = String.format("%.1f", listing.getRating());
            rows[i][5] = listing.getAuthor();
        }
        
        output.table(headers, rows);
        output.println("==============================================");
        output.println("注: 按发布时间排序");
        output.println("==============================================");
    }
}
