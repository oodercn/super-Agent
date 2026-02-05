package net.ooder.sdk.agent;

import net.ooder.sdk.agent.impl.EndAgentImpl;
import net.ooder.sdk.network.packet.TaskPacket;
import net.ooder.sdk.system.enums.EndAgentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import static org.junit.jupiter.api.Assertions.*;

public class EndAgentTest {
    private EndAgent endAgent;
    private String agentId;
    private String agentName;
    private Map<String, Object> capabilities;
    
    @BeforeEach
    public void setUp() throws Exception {
        agentId = "test-end-123";
        agentName = "Test End Agent";
        capabilities = new java.util.HashMap<>();
        capabilities.put("skill", "test");
        capabilities.put("version", "1.0.0");
        
        // 创建 EndAgent，传入 null 作为 UDPSDK
        endAgent = new EndAgentImpl(null, agentId, agentName, capabilities);
    }
    
    @Test
    public void testGetAgentId() {
        assertEquals(agentId, endAgent.getAgentId());
    }
    
    @Test
    public void testGetAgentName() {
        assertEquals(agentName, endAgent.getAgentName());
    }
    
    @Test
    public void testGetAgentType() {
        assertEquals("EndAgent", endAgent.getAgentType());
    }
    
    @Test
    public void testGetStatus() {
        assertEquals(EndAgentStatus.OFFLINE, endAgent.getStatus());
    }
    
    @Test
    public void testGetCapabilities() {
        assertEquals(capabilities, endAgent.getCapabilities());
    }
    
    @Test
    public void testStart() {
        CompletableFuture<Boolean> result = endAgent.start();
        boolean success = result.join();
        assertTrue(success);
        assertEquals(EndAgentStatus.ONLINE, endAgent.getStatus());
        assertTrue(endAgent.isOnline());
    }
    
    @Test
    public void testStop() {
        // 先启动
        endAgent.start().join();
        assertTrue(endAgent.isOnline());
        
        // 再停止
        CompletableFuture<Boolean> result = endAgent.stop();
        boolean success = result.join();
        assertTrue(success);
        assertEquals(EndAgentStatus.OFFLINE, endAgent.getStatus());
        assertFalse(endAgent.isOnline());
    }
    
    @Test
    public void testExecuteTask() {
        // 启动 Agent
        endAgent.start().join();
        
        // 创建测试任务
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("key", "value");
        TaskPacket taskPacket = TaskPacket.builder()
                .taskId("test-task-1")
                .taskType("test")
                .params(params)
                .build();
        
        // 执行任务
        CompletableFuture<net.ooder.sdk.network.packet.ResponsePacket> result = endAgent.executeTask(taskPacket);
        net.ooder.sdk.network.packet.ResponsePacket response = result.join();
        
        // 验证响应
        assertNotNull(response);
        assertEquals("test-task-1", response.getCommandId());
        assertEquals("success", response.getStatus());
    }
    
    @Test
    public void testUpdateStatus() {
        endAgent.updateStatus(EndAgentStatus.ONLINE);
        assertEquals(EndAgentStatus.ONLINE, endAgent.getStatus());
        assertTrue(endAgent.isOnline());
        
        endAgent.updateStatus(EndAgentStatus.OFFLINE);
        assertEquals(EndAgentStatus.OFFLINE, endAgent.getStatus());
        assertFalse(endAgent.isOnline());
    }
}
