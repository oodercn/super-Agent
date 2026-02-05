package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.market.SkillListing;

import java.util.List;

/**
 * 市场搜索命令，根据关键词搜索技能
 */
public class MarketSearchCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "market search";
    }
    
    @Override
    public String getDescription() {
        return "根据关键词搜索技能";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length == 0) {
            output.error("请指定搜索关键词");
            output.println("使用示例: market search <keyword>");
            return;
        }
        
        String keyword = String.join(" ", args);
        List<SkillListing> listings = marketManager.searchSkills(keyword);
        
        if (listings.isEmpty()) {
            output.warn("未找到匹配的技能: " + keyword);
            return;
        }
        
        output.println("==============================================");
        output.println("搜索结果: " + keyword);
        output.println("==============================================");
        
        // 准备表格数据
        String[] headers = {"ID", "名称", "版本", "分类", "评分", "发布者"};
        String[][] rows = new String[listings.size()][headers.length];
        
        for (int i = 0; i < listings.size(); i++) {
            SkillListing listing = listings.get(i);
            rows[i][0] = listing.getSkillId();
            rows[i][1] = listing.getName();
            rows[i][2] = listing.getVersion();
            rows[i][3] = listing.getCategory();
            rows[i][4] = String.format("%.1f", listing.getRating());
            rows[i][5] = listing.getAuthor();
        }
        
        output.table(headers, rows);
        output.println("==============================================");
        output.println("总计: " + listings.size() + " 个匹配技能");
        output.println("==============================================");
    }
}
