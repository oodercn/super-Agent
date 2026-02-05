package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.market.SkillListing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 技能分类命令，显示所有技能分类及其包含的技能数量
 */
public class SkillCategoriesCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "skill categories";
    }
    
    @Override
    public String getDescription() {
        return "显示所有技能分类及其包含的技能数量";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        List<SkillListing> listings = marketManager.getAllSkills();
        
        if (listings.isEmpty()) {
            output.warn("暂无已发布的技能");
            return;
        }
        
        // 统计每个分类的技能数量
        Map<String, Integer> categoryCounts = new HashMap<>();
        for (SkillListing listing : listings) {
            String category = listing.getCategory();
            categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + 1);
        }
        
        output.println("==============================================");
        output.println("技能分类统计");
        output.println("==============================================");
        
        // 准备表格数据
        String[] headers = {"分类", "技能数量"};
        String[][] rows = new String[categoryCounts.size()][headers.length];
        
        int index = 0;
        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            rows[index][0] = entry.getKey();
            rows[index][1] = String.valueOf(entry.getValue());
            index++;
        }
        
        output.table(headers, rows);
        output.println("==============================================");
        output.println("总计: " + categoryCounts.size() + " 个分类");
        output.println("==============================================");
    }
}
