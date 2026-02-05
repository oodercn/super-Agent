package net.ooder.skillcenter.ide;

import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillResult;

/**
 * 调试插件，用于技能的调试
 */
public class DebuggingPlugin implements IDEPlugin {
    private boolean available;
    
    @Override
    public String getPluginId() {
        return "debugging-plugin";
    }
    
    @Override
    public String getPluginName() {
        return "Debugging Plugin";
    }
    
    @Override
    public String getDescription() {
        return "Provides debugging tools for skills";
    }
    
    @Override
    public void start() {
        available = true;
        System.out.println("Debugging Plugin started");
    }
    
    @Override
    public void stop() {
        available = false;
        System.out.println("Debugging Plugin stopped");
    }
    
    @Override
    public boolean isAvailable() {
        return available;
    }
    
    /**
     * 调试技能
     * @param skillId 技能ID
     * @param context 执行上下文
     * @return 调试结果
     * @throws SkillException 调试异常
     */
    public SkillResult debugSkill(String skillId, SkillContext context) throws SkillException {
        if (!available) {
            throw new SkillException(skillId, "Debugging plugin is not available", 
                                     SkillException.ErrorCode.EXECUTION_EXCEPTION);
        }
        
        System.out.println("Debugging skill: " + skillId);
        
        // 模拟调试
        SkillResult result = new SkillResult();
        result.setMessage("Skill debugged successfully");
        result.addData("skillId", skillId);
        result.addData("debugMode", "enabled");
        result.addData("debugInfo", "Debug information for skill");
        
        return result;
    }
}