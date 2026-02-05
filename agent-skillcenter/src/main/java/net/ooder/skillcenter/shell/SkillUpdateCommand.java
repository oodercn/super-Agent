package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.market.SkillListing;

import java.util.Scanner;

/**
 * 技能更新命令，更新已发布的技能信息
 */
public class SkillUpdateCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "skill update";
    }
    
    @Override
    public String getDescription() {
        return "更新已发布的技能信息";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length == 0) {
            output.error("请指定技能ID");
            output.println("使用示例: skill update <skill-id>");
            return;
        }
        
        String skillId = args[0];
        SkillListing existingListing = marketManager.getSkillListing(skillId);
        
        if (existingListing == null) {
            output.error("技能不存在: " + skillId);
            return;
        }
        
        Scanner scanner = new Scanner(System.in);
        
        output.println("==============================================");
        output.println("更新技能信息");
        output.println("==============================================");
        output.println("当前技能: " + existingListing.getName() + " (v" + existingListing.getVersion() + ")");
        output.println("==============================================");
        
        // 收集更新信息
        output.print("新名称 (回车保持不变): ");
        String name = scanner.nextLine();
        if (name.isEmpty()) name = existingListing.getName();
        
        output.print("新版本 (回车保持不变): ");
        String version = scanner.nextLine();
        if (version.isEmpty()) version = existingListing.getVersion();
        
        output.print("新分类 (回车保持不变): ");
        String category = scanner.nextLine();
        if (category.isEmpty()) category = existingListing.getCategory();
        
        output.print("新描述 (回车保持不变): ");
        String description = scanner.nextLine();
        if (description.isEmpty()) description = existingListing.getDescription();
        
        // 更新技能信息
        existingListing.setName(name);
        existingListing.setVersion(version);
        existingListing.setCategory(category);
        existingListing.setDescription(description);
        
        marketManager.updateSkill(existingListing);
        
        output.println("==============================================");
        output.success("技能更新成功!");
        output.println("技能ID: " + skillId);
        output.println("技能名称: " + name);
        output.println("技能版本: " + version);
        output.println("==============================================");
    }
}
