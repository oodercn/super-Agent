package net.ooder.skillcenter.shell;

import java.util.Scanner;

/**
 * 技能删除命令，从市场中删除已发布的技能
 */
public class SkillDeleteCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "skill delete";
    }
    
    @Override
    public String getDescription() {
        return "从市场中删除已发布的技能";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length == 0) {
            output.error("请指定技能ID");
            output.println("使用示例: skill delete <skill-id>");
            return;
        }
        
        String skillId = args[0];
        
        // 确认技能存在
        if (marketManager.getSkillListing(skillId) == null) {
            output.error("技能不存在: " + skillId);
            return;
        }
        
        Scanner scanner = new Scanner(System.in);
        
        output.println("==============================================");
        output.println("删除技能");
        output.println("==============================================");
        output.warn("警告: 此操作不可恢复，将永久删除该技能及其所有相关数据!");
        output.println("==============================================");
        output.print("确认删除? (y/n): ");
        
        String confirmation = scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("y")) {
            output.info("操作已取消");
            return;
        }
        
        // 删除技能
        marketManager.removeSkill(skillId);
        
        output.println("==============================================");
        output.success("技能删除成功!");
        output.println("技能ID: " + skillId);
        output.println("==============================================");
    }
}
