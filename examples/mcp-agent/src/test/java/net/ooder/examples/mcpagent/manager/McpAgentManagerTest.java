package net.ooder.examples.mcpagent.manager;

import net.ooder.sdk.enums.CommandType;
import net.ooder.sdk.llm.LlmResponse;
import net.ooder.sdk.packet.*;
import net.ooder.sdk.scene.*;
import net.ooder.sdk.udp.SendResult;
import net.ooder.sdk.udp.UDPSDK;
import net.ooder.sdk.udp.UDPConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class McpAgentManagerTest {
    private McpAgentManager mcpAgentManager;
    private UDPSDK mockUdpSdk;
    private Map<String, Object> testCapabilities;

    @BeforeEach
    public void setUp() {
        // 准备测试数据
        testCapabilities = new HashMap<>();
        testCapabilities.put("llm", true);
        testCapabilities.put("vfs", true);
        testCapabilities.put("a2ui", true);

        // 创建UDPSDK模拟对象
        mockUdpSdk = Mockito.mock(UDPSDK.class);
        doNothing().when(mockUdpSdk).startListening();

        // 使用带依赖注入的构造函数创建McpAgentManager
        mcpAgentManager = new McpAgentManager("test-mcp-id", "test-mcp-name", testCapabilities, mockUdpSdk);
    }

    @Test
    public void testAgentInitialization() throws ExecutionException, InterruptedException {
        // 测试MCP Agent初始化
        CompletableFuture<Boolean> startFuture = mcpAgentManager.start();
        assertTrue(startFuture.get(), "MCP Agent should start successfully");

        // 验证初始化的列表为空
        assertTrue(mcpAgentManager.getRouteAgents().isEmpty(), "RouteAgents list should be empty initially");
        assertTrue(mcpAgentManager.getEndAgents().isEmpty(), "EndAgents list should be empty initially");
        assertTrue(mcpAgentManager.getScenes().isEmpty(), "Scenes list should be empty initially");
    }

    @Test
    public void testRouteAgentRegister() {
        // 测试RouteAgent注册
        String routeAgentId = "test-route-agent-1";
        Map<String, Object> params = new HashMap<>();
        params.put("agentName", "Test Route Agent");
        params.put("agentType", "ROUTE");
        params.put("capabilities", Collections.singletonMap("forwarding", true));

        // 模拟命令包
        CommandPacket packet = CommandPacket.builder()
                .command(CommandType.MCP_REGISTER)
                .params(params)
                .build();
        packet.setSenderId(routeAgentId);

        // 测试处理RouteAgent注册
        mcpAgentManager.handleCommand(packet);

        // 验证RouteAgent已注册
        List<McpAgentManager.RouteAgentInfo> routeAgents = mcpAgentManager.getRouteAgents();
        assertEquals(1, routeAgents.size(), "Should have 1 RouteAgent registered");
        assertEquals(routeAgentId, routeAgents.get(0).getAgentId(), "RouteAgent ID should match");
        assertEquals("Test Route Agent", routeAgents.get(0).getAgentName(), "RouteAgent name should match");
    }

    @Test
    public void testEndAgentRegister() {
        // 先注册RouteAgent
        String routeAgentId = "test-route-agent-1";
        Map<String, Object> routeParams = new HashMap<>();
        routeParams.put("agentName", "Test Route Agent");
        routeParams.put("agentType", "ROUTE");
        routeParams.put("capabilities", Collections.singletonMap("forwarding", true));

        CommandPacket routePacket = CommandPacket.builder()
                .command(CommandType.MCP_REGISTER)
                .params(routeParams)
                .build();
        routePacket.setSenderId(routeAgentId);
        mcpAgentManager.handleCommand(routePacket);

        // 测试EndAgent注册
        String endAgentId = "test-end-agent-1";
        Map<String, Object> endParams = new HashMap<>();
        endParams.put("agentName", "Test End Agent");
        endParams.put("agentType", "END");
        endParams.put("capabilities", Collections.singletonMap("execution", true));
        endParams.put("routeAgentId", routeAgentId);

        CommandPacket endPacket = CommandPacket.builder()
                .command(CommandType.ROUTE_ENDAGENT_REGISTER)
                .params(endParams)
                .build();
        endPacket.setSenderId(endAgentId);

        // 测试处理EndAgent注册
        mcpAgentManager.handleCommand(endPacket);

        // 验证EndAgent已注册
        List<McpAgentManager.EndAgentInfo> endAgents = mcpAgentManager.getEndAgents();
        assertEquals(1, endAgents.size(), "Should have 1 EndAgent registered");
        assertEquals(endAgentId, endAgents.get(0).getAgentId(), "EndAgent ID should match");
        assertEquals("Test End Agent", endAgents.get(0).getAgentName(), "EndAgent name should match");
        assertEquals(routeAgentId, endAgents.get(0).getRouteAgentId(), "EndAgent should be associated with correct RouteAgent");
    }

    @Test
    public void testSceneCreate() {
        // 测试场景创建
        String sceneId = "test-scene-1";
        Map<String, Object> params = new HashMap<>();
        params.put("sceneId", sceneId);
        params.put("sceneName", "Test Scene");
        params.put("description", "A test scene");
        params.put("communicationProtocol", "UDP");
        params.put("securityPolicy", "PUBLIC");

        // 准备角色数据
        List<Map<String, Object>> rolesData = new ArrayList<>();
        Map<String, Object> roleData = new HashMap<>();
        roleData.put("roleId", "ADMIN");
        roleData.put("roleName", "Administrator");
        roleData.put("capabilities", Arrays.asList("read", "write", "execute"));
        roleData.put("required", true);
        rolesData.add(roleData);
        params.put("memberRoles", rolesData);

        CommandPacket packet = CommandPacket.builder()
                .command(CommandType.SCENE_CREATE)
                .params(params)
                .build();
        packet.setSenderId("test-user");

        // 测试处理场景创建
        mcpAgentManager.handleCommand(packet);

        // 验证场景已创建
        List<SceneDefinition> scenes = mcpAgentManager.getScenes();
        assertEquals(1, scenes.size(), "Should have 1 scene created");
        assertEquals(sceneId, scenes.get(0).getSceneId(), "Scene ID should match");
        assertEquals("Test Scene", scenes.get(0).getName(), "Scene name should match");
        assertEquals(1, scenes.get(0).getMemberRoles().size(), "Scene should have 1 member role");
    }



    @Test
    public void testSkillInvoke() {
        // 测试技能调用
        String senderId = "test-agent-1";
        Map<String, Object> params = new HashMap<>();
        params.put("skillId", "test-skill-1");
        params.put("skillType", "GENERAL");
        params.put("params", Collections.singletonMap("input", "test data"));

        CommandPacket packet = CommandPacket.builder()
                .command(CommandType.SKILL_INVOKE)
                .params(params)
                .build();
        packet.setSenderId(senderId);

        // 测试处理技能调用
        mcpAgentManager.handleCommand(packet);

        // 验证技能调用响应已发送
        verify(mockUdpSdk, atLeastOnce()).sendCommand(any(CommandPacket.class));
    }



    @Test
    public void testSouthboundProtocolSendToRouteAgent() {
        // 测试南下协议：向RouteAgent发送命令
        String routeAgentId = "test-route-agent-1";
        CommandPacket packet = CommandPacket.builder()
                .command(CommandType.END_EXECUTE)
                .params(Collections.singletonMap("taskId", "test-task-1"))
                .build();

        // 设置模拟响应
        when(mockUdpSdk.sendCommand(any(CommandPacket.class)))
                .thenReturn(CompletableFuture.completedFuture(new SendResult(true, "Success")));

        // 测试发送命令
        CompletableFuture<SendResult> resultFuture = mcpAgentManager.sendToRouteAgent(routeAgentId, packet);
        SendResult result = resultFuture.join();

        // 验证结果
        assertTrue(result.isSuccess(), "Send to RouteAgent should succeed");
        assertEquals("Success", result.getMessage(), "Send message should match");
    }

    @Test
    public void testSouthboundProtocolSendToEndAgent() {
        // 先注册RouteAgent和EndAgent
        String routeAgentId = "test-route-agent-1";
        String endAgentId = "test-end-agent-1";

        // 注册RouteAgent
        Map<String, Object> routeParams = new HashMap<>();
        routeParams.put("agentName", "Test Route Agent");
        routeParams.put("agentType", "ROUTE");
        routeParams.put("capabilities", Collections.singletonMap("forwarding", true));

        CommandPacket routePacket = CommandPacket.builder()
                .command(CommandType.MCP_REGISTER)
                .params(routeParams)
                .build();
        routePacket.setSenderId(routeAgentId);
        mcpAgentManager.handleCommand(routePacket);

        // 注册EndAgent
        Map<String, Object> endParams = new HashMap<>();
        endParams.put("agentName", "Test End Agent");
        endParams.put("agentType", "END");
        endParams.put("capabilities", Collections.singletonMap("execution", true));
        endParams.put("routeAgentId", routeAgentId);

        CommandPacket endPacket = CommandPacket.builder()
                .command(CommandType.ROUTE_ENDAGENT_REGISTER)
                .params(endParams)
                .build();
        endPacket.setSenderId(endAgentId);
        mcpAgentManager.handleCommand(endPacket);

        // 测试南下协议：向EndAgent发送命令
        CommandPacket packet = CommandPacket.builder()
                .command(CommandType.END_EXECUTE)
                .params(Collections.singletonMap("taskId", "test-task-1"))
                .build();

        // 设置模拟响应
        when(mockUdpSdk.sendCommand(any(CommandPacket.class)))
                .thenReturn(CompletableFuture.completedFuture(new SendResult(true, "Success")));

        // 测试发送命令
        CompletableFuture<SendResult> resultFuture = mcpAgentManager.sendToEndAgent(endAgentId, packet);
        SendResult result = resultFuture.join();

        // 验证结果
        assertTrue(result.isSuccess(), "Send to EndAgent should succeed");
        assertEquals("Success", result.getMessage(), "Send message should match");
    }

    @Test
    public void testDataStructures() {
        // 测试RouteAgentInfo数据结构
        Map<String, Object> capabilities = Collections.singletonMap("test", true);
        McpAgentManager.RouteAgentInfo routeAgent = new McpAgentManager.RouteAgentInfo("route-1", "Route 1", "ROUTE", capabilities);

        assertEquals("route-1", routeAgent.getAgentId(), "RouteAgent ID should match");
        assertEquals("Route 1", routeAgent.getAgentName(), "RouteAgent name should match");
        assertEquals("ROUTE", routeAgent.getAgentType(), "RouteAgent type should match");
        assertEquals(capabilities, routeAgent.getCapabilities(), "RouteAgent capabilities should match");
        assertEquals("ONLINE", routeAgent.getStatus(), "RouteAgent should be ONLINE initially");

        // 测试EndAgentInfo数据结构
        McpAgentManager.EndAgentInfo endAgent = new McpAgentManager.EndAgentInfo("end-1", "End 1", "END", capabilities, "route-1");

        assertEquals("end-1", endAgent.getAgentId(), "EndAgent ID should match");
        assertEquals("End 1", endAgent.getAgentName(), "EndAgent name should match");
        assertEquals("END", endAgent.getAgentType(), "EndAgent type should match");
        assertEquals("route-1", endAgent.getRouteAgentId(), "EndAgent RouteAgent ID should match");
        assertEquals("ONLINE", endAgent.getStatus(), "EndAgent should be ONLINE initially");

        // 测试状态更新
        routeAgent.updateStatus("OFFLINE", "Connection lost", Collections.singletonMap("error", "timeout"));
        assertEquals("OFFLINE", routeAgent.getStatus(), "RouteAgent status should be OFFLINE after update");
        assertEquals("Connection lost", routeAgent.getError(), "RouteAgent error should match");
        assertTrue(routeAgent.getMetrics().containsKey("error"), "RouteAgent metrics should contain error");
    }
}
