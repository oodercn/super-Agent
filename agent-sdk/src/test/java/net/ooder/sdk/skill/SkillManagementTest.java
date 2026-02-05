package net.ooder.sdk.skill;

import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.agent.model.AgentConfig;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.network.packet.CommandMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SkillManagementTest {

    private AgentSDK agentSDK;
    private TestSkill testSkill;

    @BeforeEach
    void setUp() throws Exception {
        // 创建一个模拟的AgentConfig
        AgentConfig config = new AgentConfig();
        config.setAgentId("test-agent-1");
        config.setAgentName("Test Agent");
        config.setAgentType("MCP");
        config.setEndpoint("127.0.0.1:8080");
        config.setUdpPort(9001); // 使用不同的端口避免冲突
        config.setUdpBufferSize(8192);
        config.setUdpTimeout(5000);
        config.setUdpMaxPacketSize(65536);
        
        // 初始化AgentSDK
        agentSDK = new AgentSDK(config);
        
        // 创建一个测试技能
        Map<String, String> params = new HashMap<>();
        params.put("param1", "First parameter");
        params.put("param2", "Second parameter");
        testSkill = new TestSkill("test-skill-1", "Test Skill", params);
    }

    @Test
    void testSkillRegistration() {
        // 注册技能
        agentSDK.registerSkill(testSkill);
        
        // 验证技能是否注册成功
        assertNotNull(agentSDK.getSkill("test-skill-1"));
        assertEquals("Test Skill", agentSDK.getSkill("test-skill-1").getDescription());
        assertEquals(2, agentSDK.getSkill("test-skill-1").getParameters().size());
    }

    @Test
    void testSkillUnregistration() {
        // 注册技能
        agentSDK.registerSkill(testSkill);
        
        // 验证技能是否注册成功
        assertNotNull(agentSDK.getSkill("test-skill-1"));
        
        // 注销技能
        agentSDK.unregisterSkill("test-skill-1");
        
        // 验证技能是否注销成功
        assertNull(agentSDK.getSkill("test-skill-1"));
    }

    @Test
    void testSkillInvocation() {
        // 注册技能
        agentSDK.registerSkill(testSkill);
        
        // 创建一个技能参数映射
        Map<String, Object> skillParams = new HashMap<>();
        skillParams.put("param1", "value1");
        skillParams.put("param2", "value2");
        
        // 调用技能
        SkillResult result = testSkill.execute(skillParams);
        
        // 验证技能调用结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        Map<String, Object> resultData = (Map<String, Object>) result.getData();
        assertEquals("Test Skill invoked with param1: value1, param2: value2", resultData.get("output"));
    }

    @Test
    void testSkillInvokeCommand() {
        // 注册技能
        agentSDK.registerSkill(testSkill);
        
        // 创建一个SKILL_INVOKE命令包
        CommandPacket packet = new CommandPacket();
        packet.setOperation(CommandType.SKILL_INVOKE.getValue());
        
        Map<String, Object> params = new HashMap<>();
        params.put("skillId", "test-skill-1");
        Map<String, Object> skillParams = new HashMap<>();
        skillParams.put("param1", "value1");
        skillParams.put("param2", "value2");
        params.put("params", skillParams);
        
        packet.setParams(params);
        packet.setSenderId("sender-1");
        packet.setMetadata(new CommandMetadata());
        
        // 由于handleCommand方法现在是public的，我们可以直接调用它
        agentSDK.handleCommand(packet);
        
        // 验证技能是否仍然存在
        assertNotNull(agentSDK.getSkill("test-skill-1"));
    }

    // 测试技能实现
    private static class TestSkill implements Skill {
        private final String skillId;
        private final String description;
        private final Map<String, String> parameters;
        private SkillStatus status = SkillStatus.UNINITIALIZED;

        public TestSkill(String skillId, String description, Map<String, String> parameters) {
            this.skillId = skillId;
            this.description = description;
            this.parameters = new HashMap<>(parameters);
        }

        @Override
        public String getSkillId() {
            return skillId;
        }

        @Override
        public String getName() {
            return description; // 使用description作为名称
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public Map<String, String> getParameters() {
            return new HashMap<>(parameters);
        }

        @Override
        public SkillResult execute(Map<String, Object> params) {
            status = SkillStatus.EXECUTING;
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("output", String.format("%s invoked with param1: %s, param2: %s", 
                    description, params.get("param1"), params.get("param2")));
            resultData.put("parameters", params);
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("skillId", skillId);
            metadata.put("executionTime", System.currentTimeMillis());
            status = SkillStatus.READY;
            return SkillResult.success(resultData, metadata);
        }

        @Override
        public void initialize() {
            status = SkillStatus.READY;
        }

        @Override
        public void destroy() {
            status = SkillStatus.DESTROYED;
        }

        @Override
        public SkillStatus getStatus() {
            return status;
        }
    }
}
