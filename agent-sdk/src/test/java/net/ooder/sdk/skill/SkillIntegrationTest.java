package net.ooder.sdk.skill;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

import net.ooder.sdk.skill.ExampleEndSkill;

public class SkillIntegrationTest {
    
    private final String skillId = "test-skill-123";
    private final String skillName = "Test Skill";
    private final String agentType = "endagent";
    private final Map<String, Object> capabilities = new java.util.HashMap<>();
    
    public SkillIntegrationTest() {
        capabilities.put("skill", "test");
        capabilities.put("version", "1.0.0");
    }
    
    @Test
    public void testSkillInitialization() {
        ExampleEndSkill skill = new ExampleEndSkill();
        skill.initialize(skillId, skillName, agentType, capabilities);
        
        assertNotNull(skill);
        assertEquals(skillId, skill.getSkillId());
        assertEquals(skillName, skill.getSkillName());
        assertEquals(agentType, skill.getAgentType());
        assertNotNull(skill.getAgent());
    }
    
    @Test
    public void testSkillStartAndStop() {
        ExampleEndSkill skill = new ExampleEndSkill();
        skill.initialize(skillId, skillName, agentType, capabilities);
        
        // 测试启动
        boolean startResult = skill.start();
        assertTrue(startResult);
        
        // 测试停止
        boolean stopResult = skill.stop();
        assertTrue(stopResult);
    }
    
    @Test
    public void testSkillExecute() {
        ExampleEndSkill skill = new ExampleEndSkill();
        skill.initialize(skillId, skillName, agentType, capabilities);
        
        // 启动 Skill
        skill.start();
        
        // 测试执行任务
        Object task = "test task";
        Object result = skill.execute(task);
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertTrue(((String) result).contains(skillName));
        
        // 停止 Skill
        skill.stop();
    }
}
