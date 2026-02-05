package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.market.SkillListing;

import java.util.List;

/**
 * 技能列表命令，显示所有已发布的技能
 */
public class SkillListCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "skill list";
    }
    
    @Override
    public String getDescription() {
        return "显示所有已发布的技能";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        List<SkillListing> listings = marketManager.getAllSkills();
        
        if (listings.isEmpty()) {
            output.warn("暂无已发布的技能");
            return;
        }
        
        output.println("==============================================");
        output.println("已发布技能列表");
        output.println("==============================================");
        
        // 准备表格数据
        String[] headers = {"ID", "名称", "版本", "分类", "评分", "下载量", "发布者"};
        String[][] rows = new String[listings.size()][headers.length];
        
        for (int i = 0; i < listings.size(); i++) {
            SkillListing listing = listings.get(i);
            rows[i][0] = listing.getSkillId();
            rows[i][1] = listing.getName();
            rows[i][2] = listing.getVersion();
            rows[i][3] = listing.getCategory();
            rows[i][4] = String.format("%.1f", listing.getRating());
            rows[i][5] = String.valueOf(listing.getDownloadCount());
            rows[i][6] = listing.getAuthor();
        }
        
        output.table(headers, rows);
        output.println("==============================================");
        output.println("总计: " + listings.size() + " 个技能");
        output.println("==============================================");
    }
}
