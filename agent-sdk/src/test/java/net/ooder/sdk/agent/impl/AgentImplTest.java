package net.ooder.sdk.agent.impl;

import net.ooder.sdk.network.udp.UDPSDK;
import net.ooder.sdk.network.packet.TaskPacket;
import net.ooder.sdk.network.packet.ResponsePacket;
import net.ooder.sdk.config.TestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import java.util.Map;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class AgentImplTest {
    private EndAgentImpl endAgent;
    private RouteAgentImpl routeAgent;
    private McpAgentImpl mcpAgent;
    private Map<String, Object> capabilities;
    
    @Autowired
    private UDPSDK udpSDK;
    
    @BeforeEach
    public void setUp() throws Exception {
        capabilities = new java.util.HashMap<>();
        capabilities.put("skill", "test");
        capabilities.put("version", "1.0.0");
        
        // 创建 Agent 实例
        endAgent = new EndAgentImpl(udpSDK, "test-end-123", "Test End Agent", capabilities);
        routeAgent = new RouteAgentImpl(udpSDK, "test-route-123", "Test Route Agent", capabilities);
        mcpAgent = new McpAgentImpl(udpSDK, "test-mcp-123", "Test MCP Agent", capabilities);
    }
    
    @Test
    public void testEndAgentCreateResponse() throws Exception {
        // 创建测试任务包
        TaskPacket taskPacket = TaskPacket.builder()
                .taskId("test-task-1")
                .taskType("test")
                .params(new java.util.HashMap<>())
                .build();
        
        // 使用反射调用 createResponse 方法
        Method createResponseMethod = EndAgentImpl.class.getDeclaredMethod("createResponse", TaskPacket.class, boolean.class, String.class);
        createResponseMethod.setAccessible(true);
        
        // 测试成功响应
        ResponsePacket successResponse = (ResponsePacket) createResponseMethod.invoke(endAgent, taskPacket, true, "Success message");
        assertNotNull(successResponse);
        assertEquals("test-task-1", successResponse.getCommandId());
        assertEquals("success", successResponse.getStatus());
        assertEquals("Success message", successResponse.getData());
        
        // 测试失败响应
        ResponsePacket errorResponse = (ResponsePacket) createResponseMethod.invoke(endAgent, taskPacket, false, "Error message");
        assertNotNull(errorResponse);
        assertEquals("test-task-1", errorResponse.getCommandId());
        assertEquals("error", errorResponse.getStatus());
        assertEquals("Error message", errorResponse.getData());
    }
    
    @Test
    public void testRouteAgentCreateResponse() throws Exception {
        // 创建测试任务包
        TaskPacket taskPacket = TaskPacket.builder()
                .taskId("test-task-2")
                .taskType("test")
                .params(new java.util.HashMap<>())
                .build();
        
        // 使用反射调用 createResponse 方法
        Method createResponseMethod = RouteAgentImpl.class.getDeclaredMethod("createResponse", TaskPacket.class, boolean.class, String.class);
        createResponseMethod.setAccessible(true);
        
        // 测试成功响应
        ResponsePacket successResponse = (ResponsePacket) createResponseMethod.invoke(routeAgent, taskPacket, true, "Success message");
        assertNotNull(successResponse);
        assertEquals("test-task-2", successResponse.getCommandId());
        assertEquals("success", successResponse.getStatus());
        assertEquals("Success message", successResponse.getData());
        
        // 测试失败响应
        ResponsePacket errorResponse = (ResponsePacket) createResponseMethod.invoke(routeAgent, taskPacket, false, "Error message");
        assertNotNull(errorResponse);
        assertEquals("test-task-2", errorResponse.getCommandId());
        assertEquals("error", errorResponse.getStatus());
        assertEquals("Error message", errorResponse.getData());
    }
    
    @Test
    public void testMcpAgentCreateResponse() throws Exception {
        // 创建测试任务包
        TaskPacket taskPacket = TaskPacket.builder()
                .taskId("test-task-3")
                .taskType("test")
                .params(new java.util.HashMap<>())
                .build();
        
        // 使用反射调用 createResponse 方法
        Method createResponseMethod = McpAgentImpl.class.getDeclaredMethod("createResponse", TaskPacket.class, boolean.class, String.class);
        createResponseMethod.setAccessible(true);
        
        // 测试成功响应
        ResponsePacket successResponse = (ResponsePacket) createResponseMethod.invoke(mcpAgent, taskPacket, true, "Success message");
        assertNotNull(successResponse);
        assertEquals("test-task-3", successResponse.getCommandId());
        assertEquals("success", successResponse.getStatus());
        assertEquals("Success message", successResponse.getData());
        
        // 测试失败响应
        ResponsePacket errorResponse = (ResponsePacket) createResponseMethod.invoke(mcpAgent, taskPacket, false, "Error message");
        assertNotNull(errorResponse);
        assertEquals("test-task-3", errorResponse.getCommandId());
        assertEquals("error", errorResponse.getStatus());
        assertEquals("Error message", errorResponse.getData());
    }
}
