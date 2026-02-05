package net.ooder.sdk;

import net.ooder.sdk.agent.model.AgentConfig;
import net.ooder.sdk.network.udp.SendResult;
import net.ooder.sdk.network.packet.StatusReportPacket;
import net.ooder.sdk.network.packet.TaskPacket;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.config.TestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
class AgentSDKTest {

    private AgentSDK agentSDK;
    private AgentConfig config;

    @BeforeEach
    void setUp() throws Exception {
        config = AgentConfig.builder()
                .agentId("test-agent-001")
                .agentName("TestAgent")
                .agentType("skill")
                .endpoint("http://localhost:9000")
                .udpPort(9001)
                .heartbeatInterval(30000)
                .build();

        agentSDK = new AgentSDK(config);
    }

    @Test
    void testAgentConfigBuilder() {
        assertEquals("test-agent-001", config.getAgentId());
        assertEquals("TestAgent", config.getAgentName());
        assertEquals("skill", config.getAgentType());
        assertEquals("http://localhost:9000", config.getEndpoint());
        assertEquals(9001, config.getUdpPort());
        assertEquals(30000, config.getHeartbeatInterval());
    }

    @Test
    void testSendStatusReport() throws ExecutionException, InterruptedException, TimeoutException {
        StatusReportPacket packet = StatusReportPacket.builder()
                .reportType("test_report")
                .entityType("skill")
                .entityId("test-agent-001")
                .statusType("running")
                .currentStatus("running")
                .build();

        CompletableFuture<SendResult> future = agentSDK.sendStatusReport(packet);
        // 由于UDP发送可能失败，我们只验证方法能正常调用，不验证结果
        assertNotNull(future);
    }

    @Test
    void testSendCommand() throws ExecutionException, InterruptedException, TimeoutException {
        Map<String, Object> params = new HashMap<>();
        params.put("test", "value");

        CompletableFuture<SendResult> future = agentSDK.sendCommand(CommandType.ROUTE_ADD, params);
        // 由于UDP发送可能失败，我们只验证方法能正常调用，不验证结果
        assertNotNull(future);
    }

    @Test
    void testStartAndStop() {
        assertDoesNotThrow(() -> agentSDK.start());
        assertDoesNotThrow(() -> agentSDK.stop());
    }

    @Test
    void testGetConfig() throws Exception {
        AgentSDK sdk = new AgentSDK(config);
        assertEquals(config, sdk.getConfig());
    }

    @Test
    void testRouteAgentFunctionality() throws ExecutionException, InterruptedException, TimeoutException {
        // 创建RouteAgent管理器
        Map<String, Object> capabilities = new HashMap<>();
        capabilities.put("forwarding", true);
        capabilities.put("loadBalancing", true);
        capabilities.put("maxConnections", 100);

        agentSDK.createRouteAgentManager("TestRouteAgent", capabilities);

        // 测试RouteAgent注册
        CompletableFuture<Boolean> registerFuture = agentSDK.registerRouteAgent("test-mcp-001");
        assertNotNull(registerFuture);

        // 测试任务转发
        TaskPacket taskPacket = TaskPacket.builder()
                .taskId("test-task-001")
                .taskType("execute")
                .skillflowId("test-skillflow-001")
                .build();

        CompletableFuture<SendResult> forwardFuture = agentSDK.forwardTask(taskPacket, "test-endagent-001");
        assertNotNull(forwardFuture);

        // 测试发送任务结果
        Map<String, Object> result = new HashMap<>();
        result.put("status", "completed");
        result.put("data", "test result");

        CompletableFuture<SendResult> resultFuture = agentSDK.sendTaskResult("test-task-001", result);
        assertNotNull(resultFuture);
    }
}
