package net.ooder.skillcenter.shell.lifecycle;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.lifecycle.registration.SkillRegistrationManager;
import net.ooder.skillcenter.lifecycle.registration.impl.SkillRegistrationManagerImpl;

public class SkillApproveCommand extends AbstractCommand {
    
    private SkillRegistrationManager registrationManager;
    
    public SkillApproveCommand() {
        super();
        this.registrationManager = new SkillRegistrationManagerImpl();
    }
    
    @Override
    public String getName() {
        return "skill approve";
    }
    
    @Override
    public String getDescription() {
        return "批准技能注册";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 1) {
            error("用法: skill approve <skillId> [options]");
            println("选项:");
            println("  --reviewer <reviewer>     - 审核人");
            println("  --comments <comments>     - 审核意见");
            return;
        }
        
        String skillId = args[0];
        String reviewer = System.getProperty("user.name");
        String comments = "";
        
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("--reviewer=")) {
                reviewer = args[i].substring(11);
            } else if (args[i].startsWith("--comments=")) {
                comments = args[i].substring(11);
            }
        }
        
        println("正在批准技能: " + skillId);
        println("审核人: " + reviewer);
        if (!comments.isEmpty()) {
            println("审核意见: " + comments);
        }
        
        boolean result = registrationManager.approveRegistration(skillId, reviewer, comments);
        
        if (result) {
            success("技能批准成功");
            println("技能 ID: " + skillId);
            println("审核人: " + reviewer);
        } else {
            error("技能批准失败: 技能不存在或已被审核");
        }
    }
}
