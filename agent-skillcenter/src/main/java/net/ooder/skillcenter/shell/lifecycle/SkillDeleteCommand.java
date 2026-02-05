package net.ooder.skillcenter.shell.lifecycle;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.lifecycle.registration.SkillRegistrationManager;
import net.ooder.skillcenter.lifecycle.registration.impl.SkillRegistrationManagerImpl;

public class SkillDeleteCommand extends AbstractCommand {
    
    private SkillRegistrationManager registrationManager;
    
    public SkillDeleteCommand() {
        super();
        this.registrationManager = new SkillRegistrationManagerImpl();
    }
    
    @Override
    public String getName() {
        return "skill delete";
    }
    
    @Override
    public String getDescription() {
        return "删除技能";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 1) {
            error("用法: skill delete <skillId> [options]");
            println("选项:");
            println("  --force                   - 强制删除");
            return;
        }
        
        String skillId = args[0];
        boolean force = false;
        
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--force")) {
                force = true;
            }
        }
        
        println("正在删除技能: " + skillId);
        if (force) {
            println("强制删除模式");
        }
        
        boolean result = registrationManager.unregisterSkill(skillId);
        
        if (result) {
            success("技能删除成功");
            println("技能 ID: " + skillId);
        } else {
            error("技能删除失败: 技能不存在");
        }
    }
}
