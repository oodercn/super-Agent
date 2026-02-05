package net.ooder.sdk.agent.impl;

import net.ooder.sdk.network.udp.UDPSDK;
import net.ooder.sdk.network.udp.SendResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AbstractMcpAgentTest {
    private TestableAbstractMcpAgent mcpAgent;
    private UDPSDK mockUdpSDK;
    private Map<String, Object> capabilities;
    
    @BeforeEach
    public void setUp() throws Exception {
        capabilities = new java.util.HashMap<>();
        capabilities.put("skill", "test");
        capabilities.put("version", "1.0.0");
        
        // 创建模拟的 UDPSDK
        mockUdpSDK = Mockito.mock(UDPSDK.class);
        
        // 创建 TestableAbstractMcpAgent 实例
        mcpAgent = new TestableAbstractMcpAgent(mockUdpSDK, "test-mcp-123", "Test MCP Agent", capabilities);
    }
    
    @Test
    public void testBroadcastToRouteAgents_NoRouteAgents() throws Exception {
        // 测试没有注册 RouteAgent 的情况
        CompletableFuture<Boolean> result = mcpAgent.broadcastToRouteAgents("test message");
        boolean success = result.join();
        assertTrue(success);
    }
    
    @Test
    public void testBroadcastToRouteAgents_AllSuccess() throws Exception {
        // 注册一个 RouteAgent
        String routeAgentId = "test-route-123";
        mcpAgent.registerRouteAgent(routeAgentId, "Test Route Agent", capabilities).join();
        
        // 模拟发送命令成功
        SendResult mockResult = Mockito.mock(SendResult.class);
        when(mockResult.isSuccess()).thenReturn(true);
        when(mockResult.getMessage()).thenReturn("Success");
        when(mockUdpSDK.sendCommand(any())).thenReturn(CompletableFuture.completedFuture(mockResult));
        
        // 测试广播
        CompletableFuture<Boolean> result = mcpAgent.broadcastToRouteAgents("test message");
        boolean success = result.join();
        assertTrue(success);
        
        // 验证发送命令被调用
        verify(mockUdpSDK, times(1)).sendCommand(any());
    }
    
    @Test
    public void testBroadcastToRouteAgents_AllFailure() throws Exception {
        // 注册一个 RouteAgent
        String routeAgentId = "test-route-123";
        mcpAgent.registerRouteAgent(routeAgentId, "Test Route Agent", capabilities).join();
        
        // 模拟发送命令失败
        when(mockUdpSDK.sendCommand(any())).thenThrow(new RuntimeException("Test exception"));
        
        // 测试广播
        CompletableFuture<Boolean> result = mcpAgent.broadcastToRouteAgents("test message");
        boolean success = result.join();
        assertFalse(success);
        
        // 验证发送命令被调用
        verify(mockUdpSDK, times(1)).sendCommand(any());
    }
    
    @Test
    public void testBroadcastToRouteAgents_SendCommandException() throws Exception {
        // 注册一个 RouteAgent
        String routeAgentId = "test-route-123";
        mcpAgent.registerRouteAgent(routeAgentId, "Test Route Agent", capabilities).join();
        
        // 模拟发送命令抛出异常
        when(mockUdpSDK.sendCommand(any())).thenThrow(new RuntimeException("Test exception"));
        
        // 测试广播
        CompletableFuture<Boolean> result = mcpAgent.broadcastToRouteAgents("test message");
        boolean success = result.join();
        assertFalse(success);
        
        // 验证发送命令被调用
        verify(mockUdpSDK, times(1)).sendCommand(any());
    }
    
    @Test
    public void testBroadcastToRouteAgents_MultipleRouteAgents() throws Exception {
        // 注册多个 RouteAgent
        String routeAgentId1 = "test-route-123";
        String routeAgentId2 = "test-route-456";
        mcpAgent.registerRouteAgent(routeAgentId1, "Test Route Agent 1", capabilities).join();
        mcpAgent.registerRouteAgent(routeAgentId2, "Test Route Agent 2", capabilities).join();
        
        // 模拟发送命令成功
        SendResult mockResult = Mockito.mock(SendResult.class);
        when(mockUdpSDK.sendCommand(any())).thenReturn(CompletableFuture.completedFuture(mockResult));
        
        // 测试广播
        CompletableFuture<Boolean> result = mcpAgent.broadcastToRouteAgents("test message");
        // 等待足够的时间让异步操作完成
        Thread.sleep(1000);
        boolean success = result.join();
        
        // 验证发送命令被调用两次
        verify(mockUdpSDK, times(2)).sendCommand(any());
    }
    
    // 可测试的 AbstractMcpAgent 子类
    private static class TestableAbstractMcpAgent extends AbstractMcpAgent {
        public TestableAbstractMcpAgent(UDPSDK udpSDK, String agentId, String agentName, Map<String, Object> capabilities) {
            super(udpSDK, agentId, agentName, capabilities);
        }
        
        @Override
        protected net.ooder.sdk.network.packet.ResponsePacket createResponse(net.ooder.sdk.network.packet.TaskPacket taskPacket, boolean success, String message) {
            return net.ooder.sdk.network.packet.ResponsePacket.builder()
                    .commandId(taskPacket.getTaskId())
                    .status(success ? "success" : "error")
                    .data(message)
                    .build();
        }
    }
}
