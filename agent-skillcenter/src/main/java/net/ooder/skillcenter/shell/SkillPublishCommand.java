package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.market.SkillListing;

import java.util.Scanner;

/**
 * 技能发布命令，发布新技能到市场
 */
public class SkillPublishCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "skill publish";
    }
    
    @Override
    public String getDescription() {
        return "发布新技能到市场";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        
        output.println("==============================================");
        output.println("发布新技能");
        output.println("==============================================");
        
        // 收集技能信息
        output.print("技能名称: ");
        String name = scanner.nextLine();
        
        output.print("技能版本: ");
        String version = scanner.nextLine();
        
        output.print("技能分类: ");
        String category = scanner.nextLine();
        
        output.print("发布者: ");
        String publisher = scanner.nextLine();
        
        output.print("技能描述: ");
        String description = scanner.nextLine();
        
        // 创建技能发布信息
        SkillListing listing = new SkillListing();
        listing.setName(name);
        listing.setVersion(version);
        listing.setCategory(category);
        listing.setAuthor(publisher);
        listing.setDescription(description);
        
        // 生成技能ID
        String skillId = "skill-" + System.currentTimeMillis();
        listing.setSkillId(skillId);
        
        // 模拟创建Skill对象
        net.ooder.skillcenter.model.Skill skill = new net.ooder.skillcenter.model.Skill() {
            @Override
            public String getId() {
                return skillId;
            }
            
            @Override
            public String getName() {
                return name;
            }
            
            @Override
            public String getDescription() {
                return description;
            }
            
            @Override
            public boolean isAvailable() {
                return true;
            }
            
            @Override
            public java.util.Map<String, net.ooder.skillcenter.model.SkillParam> getParams() {
                return new java.util.HashMap<>();
            }
            
            @Override
            public net.ooder.skillcenter.model.SkillResult execute(net.ooder.skillcenter.model.SkillContext context) throws net.ooder.skillcenter.model.SkillException {
                net.ooder.skillcenter.model.SkillResult result = new net.ooder.skillcenter.model.SkillResult();
                result.setMessage("Skill executed successfully");
                return result;
            }
        };
        
        // 发布技能
        marketManager.publishSkill(skill, listing);
        
        output.println("==============================================");
        output.success("技能发布成功!");
        output.println("技能ID: " + skillId);
        output.println("技能名称: " + name);
        output.println("技能版本: " + version);
        output.println("==============================================");
    }
}
