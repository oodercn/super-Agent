package net.ooder.skillcenter.ide;

import java.util.Map;

/**
 * 测试插件，用于技能的测试
 */
public class TestingPlugin implements IDEPlugin {
    private boolean available;
    
    @Override
    public String getPluginId() {
        return "testing-plugin";
    }
    
    @Override
    public String getPluginName() {
        return "Testing Plugin";
    }
    
    @Override
    public String getDescription() {
        return "Provides testing tools for skills";
    }
    
    @Override
    public void start() {
        available = true;
        System.out.println("Testing Plugin started");
    }
    
    @Override
    public void stop() {
        available = false;
        System.out.println("Testing Plugin stopped");
    }
    
    @Override
    public boolean isAvailable() {
        return available;
    }
    
    /**
     * 测试技能
     * @param skillId 技能ID
     * @param testCases 测试用例
     * @return 测试结果
     */
    public SkillTestResult testSkill(String skillId, Map<String, Object> testCases) {
        if (!available) {
            SkillTestResult result = new SkillTestResult();
            result.setSkillId(skillId);
            result.setSuccess(false);
            result.setMessage("Testing plugin is not available");
            return result;
        }
        
        System.out.println("Testing skill: " + skillId);
        
        // 模拟测试
        SkillTestResult result = new SkillTestResult();
        result.setSkillId(skillId);
        result.setSuccess(true);
        result.setMessage("Skill tested successfully");
        result.setTestCount(testCases != null ? testCases.size() : 0);
        result.setPassCount(testCases != null ? testCases.size() : 0);
        result.setFailCount(0);
        
        return result;
    }
}