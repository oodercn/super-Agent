package net.ooder.skillcenter.shell.lifecycle;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.lifecycle.registration.SkillRegistrationManager;
import net.ooder.skillcenter.lifecycle.registration.SkillRegistrationManager.SkillUpdateRequest;
import net.ooder.skillcenter.lifecycle.registration.SkillRegistrationManager.RegistrationResult;
import net.ooder.skillcenter.lifecycle.registration.impl.SkillRegistrationManagerImpl;

public class SkillUpdateCommand extends AbstractCommand {
    
    private SkillRegistrationManager registrationManager;
    
    public SkillUpdateCommand() {
        super();
        this.registrationManager = new SkillRegistrationManagerImpl();
    }
    
    @Override
    public String getName() {
        return "skill update";
    }
    
    @Override
    public String getDescription() {
        return "更新技能信息";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 1) {
            error("用法: skill update <skillId> [options]");
            println("选项:");
            println("  --name <name>             - 技能名称");
            println("  --description <desc>       - 技能描述");
            println("  --category <category>       - 技能分类");
            println("  --version <version>        - 技能版本");
            return;
        }
        
        String skillId = args[0];
        SkillUpdateRequest request = new SkillUpdateRequest();
        
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("--name=")) {
                request.setSkillName(args[i].substring(7));
            } else if (args[i].startsWith("--description=")) {
                request.setDescription(args[i].substring(14));
            } else if (args[i].startsWith("--category=")) {
                request.setCategory(args[i].substring(11));
            } else if (args[i].startsWith("--version=")) {
                request.setVersion(args[i].substring(10));
            }
        }
        
        println("正在更新技能: " + skillId);
        
        RegistrationResult result = registrationManager.updateSkill(skillId, request);
        
        if (result.isSuccess()) {
            success("技能更新成功");
            println("技能 ID: " + result.getSkillId());
            println("状态: " + result.getStatus());
            println("时间: " + formatTimestamp(result.getTimestamp()));
        } else {
            error("技能更新失败: " + result.getMessage());
        }
    }
    
    private String formatTimestamp(long timestamp) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(new java.util.Date(timestamp));
    }
}
