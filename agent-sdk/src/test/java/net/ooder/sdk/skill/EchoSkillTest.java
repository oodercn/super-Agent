package net.ooder.sdk.skill;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class EchoSkillTest {
    private EchoSkill echoSkill;
    
    @BeforeEach
    public void setUp() throws Exception {
        // 创建 EchoSkill 实例
        echoSkill = new EchoSkill();
        
        // 初始化技能
        Map<String, Object> capabilities = new java.util.HashMap<>();
        capabilities.put("echo", true);
        echoSkill.initialize("echo-skill", "Echo Skill", "EndAgent", capabilities);
    }
    
    @Test
    public void testStart() {
        boolean result = echoSkill.start();
        assertTrue(result);
    }
    
    @Test
    public void testStop() {
        boolean result = echoSkill.stop();
        assertTrue(result);
    }
    
    @Test
    public void testExecute_ValidMessage() {
        // 创建测试参数
        Map<String, Object> params = new java.util.HashMap<>();
        params.put("message", "Hello, World!");
        params.put("repeat", 2);
        
        // 执行技能
        Object result = echoSkill.execute(params);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof Map);
        
        Map<?, ?> resultMap = (Map<?, ?>) result;
        assertTrue((Boolean) resultMap.get("success"));
        assertEquals("Hello, World!", resultMap.get("originalMessage"));
        assertEquals(2, resultMap.get("repeatCount"));
        assertEquals("Hello, World!\nHello, World!", resultMap.get("echoMessage"));
        
        // 验证元数据
        assertNotNull(resultMap.get("metadata"));
        assertTrue(resultMap.get("metadata") instanceof Map);
        Map<?, ?> metadata = (Map<?, ?>) resultMap.get("metadata");
        assertEquals("echo-skill", metadata.get("skillId"));
        assertEquals("Echo Skill", metadata.get("skillName"));
        assertNotNull(metadata.get("executionTime"));
    }
    
    @Test
    public void testExecute_MessageOnly() {
        // 创建测试参数（只有 message）
        Map<String, Object> params = new java.util.HashMap<>();
        params.put("message", "Hello, World!");
        
        // 执行技能
        Object result = echoSkill.execute(params);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof Map);
        
        Map<?, ?> resultMap = (Map<?, ?>) result;
        assertTrue((Boolean) resultMap.get("success"));
        assertEquals("Hello, World!", resultMap.get("originalMessage"));
        assertEquals(1, resultMap.get("repeatCount")); // 默认值为 1
        assertEquals("Hello, World!", resultMap.get("echoMessage"));
    }
    
    @Test
    public void testExecute_NoMessage() {
        // 创建测试参数（无 message）
        Map<String, Object> params = new java.util.HashMap<>();
        params.put("repeat", 2);
        
        // 执行技能
        Object result = echoSkill.execute(params);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof Map);
        
        Map<?, ?> resultMap = (Map<?, ?>) result;
        assertFalse((Boolean) resultMap.get("success"));
        assertEquals("Message parameter is required", resultMap.get("message"));
    }
    
    @Test
    public void testExecute_EmptyMessage() {
        // 创建测试参数（空 message）
        Map<String, Object> params = new java.util.HashMap<>();
        params.put("message", "");
        params.put("repeat", 2);
        
        // 执行技能
        Object result = echoSkill.execute(params);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof Map);
        
        Map<?, ?> resultMap = (Map<?, ?>) result;
        assertFalse((Boolean) resultMap.get("success"));
        assertEquals("Message parameter is required", resultMap.get("message"));
    }
    
    @Test
    public void testExecute_InvalidTaskFormat() {
        // 执行技能（无效的任务格式）
        Object result = echoSkill.execute("not a map");
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof Map);
        
        Map<?, ?> resultMap = (Map<?, ?>) result;
        assertFalse((Boolean) resultMap.get("success"));
        assertEquals("Invalid task format", resultMap.get("message"));
    }
    
    @Test
    public void testExecute_NullTask() {
        // 执行技能（空任务）
        Object result = echoSkill.execute(null);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof Map);
        
        Map<?, ?> resultMap = (Map<?, ?>) result;
        assertFalse((Boolean) resultMap.get("success"));
        assertEquals("Invalid task format", resultMap.get("message"));
    }
    
    @Test
    public void testGetAgent() {
        Object agent = echoSkill.getAgent();
        assertNotNull(agent);
    }
    
    @Test
    public void testGetAgentType() {
        assertEquals("EndAgent", echoSkill.getAgentType());
    }
    
    @Test
    public void testGetSkillId() {
        assertEquals("echo-skill", echoSkill.getSkillId());
    }
    
    @Test
    public void testGetSkillName() {
        assertEquals("Echo Skill", echoSkill.getSkillName());
    }
}
