package net.ooder.skillcenter.shell.lifecycle;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.lifecycle.registration.SkillRegistrationManager;
import net.ooder.skillcenter.lifecycle.registration.impl.SkillRegistrationManagerImpl;

public class SkillRejectCommand extends AbstractCommand {
    
    private SkillRegistrationManager registrationManager;
    
    public SkillRejectCommand() {
        super();
        this.registrationManager = new SkillRegistrationManagerImpl();
    }
    
    @Override
    public String getName() {
        return "skill reject";
    }
    
    @Override
    public String getDescription() {
        return "拒绝技能注册";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 1) {
            error("用法: skill reject <skillId> [options]");
            println("选项:");
            println("  --reviewer <reviewer>     - 审核人");
            println("  --comments <comments>     - 拒绝原因");
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
        
        println("正在拒绝技能: " + skillId);
        println("审核人: " + reviewer);
        if (!comments.isEmpty()) {
            println("拒绝原因: " + comments);
        }
        
        boolean result = registrationManager.rejectRegistration(skillId, reviewer, comments);
        
        if (result) {
            success("技能拒绝成功");
            println("技能 ID: " + skillId);
            println("审核人: " + reviewer);
        } else {
            error("技能拒绝失败: 技能不存在或已被审核");
        }
    }
}
