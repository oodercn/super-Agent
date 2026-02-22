package net.ooder.nexus.protocol;

import net.ooder.nexus.core.protocol.*;
import net.ooder.nexus.core.protocol.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProtocolHub测试")
public class ProtocolHubTest {

    private ProtocolHubImpl protocolHub;

    @BeforeEach
    public void setUp() {
        protocolHub = new ProtocolHubImpl();
    }

    @Test
    @DisplayName("测试协议中枢初始化")
    public void testProtocolHubInitialization() {
        assertNotNull(protocolHub, "ProtocolHub should not be null");
        assertNotNull(protocolHub.getSupportedProtocols(), "Supported protocols list should not be null");
        assertTrue(protocolHub.getSupportedProtocols().isEmpty(), "Initially no protocols registered");
    }

    @Test
    @DisplayName("测试协议注册和注销")
    public void testProtocolRegistrationAndUnregistration() {
        TestProtocolHandler handler = new TestProtocolHandler("TEST");
        
        // 测试注册
        protocolHub.registerProtocolHandler("TEST", handler);
        assertTrue(protocolHub.isProtocolRegistered("TEST"), "TEST protocol should be registered");
        assertEquals(1, protocolHub.getSupportedProtocols().size(), "Should have 1 registered protocol");
        
        ProtocolStats stats = protocolHub.getProtocolStats("TEST");
        assertNotNull(stats, "Protocol stats should exist");
        assertEquals("TEST", stats.getProtocolType(), "Protocol type should match");
        
        // 测试注销
        protocolHub.unregisterProtocolHandler("TEST");
        assertFalse(protocolHub.isProtocolRegistered("TEST"), "TEST protocol should be unregistered");
        assertEquals(0, protocolHub.getSupportedProtocols().size(), "Should have 0 registered protocols");
    }

    @Test
    @DisplayName("测试多协议注册")
    public void testMultipleProtocolRegistration() {
        TestProtocolHandler handler1 = new TestProtocolHandler("PROTOCOL_A");
        TestProtocolHandler handler2 = new TestProtocolHandler("PROTOCOL_B");
        TestProtocolHandler handler3 = new TestProtocolHandler("PROTOCOL_C");
        
        protocolHub.registerProtocolHandler("PROTOCOL_A", handler1);
        protocolHub.registerProtocolHandler("PROTOCOL_B", handler2);
        protocolHub.registerProtocolHandler("PROTOCOL_C", handler3);
        
        assertEquals(3, protocolHub.getSupportedProtocols().size(), "Should have 3 registered protocols");
        assertTrue(protocolHub.isProtocolRegistered("PROTOCOL_A"));
        assertTrue(protocolHub.isProtocolRegistered("PROTOCOL_B"));
        assertTrue(protocolHub.isProtocolRegistered("PROTOCOL_C"));
    }

    @Test
    @DisplayName("测试命令处理")
    public void testCommandProcessing() {
        TestProtocolHandler handler = new TestProtocolHandler("CMD_TEST");
        protocolHub.registerProtocolHandler("CMD_TEST", handler);
        
        CommandPacket packet = createCommandPacket("CMD_TEST", "TEST_COMMAND");
        CommandResult result = protocolHub.handleCommand(packet);
        
        assertNotNull(result, "Command result should not be null");
        assertTrue(result.isSuccess(), "Command should be successful");
        assertEquals(200, result.getCode(), "Response code should be 200");
        assertEquals("cmd-001", result.getCommandId(), "Command ID should match");
    }

    @Test
    @DisplayName("测试命令处理统计")
    public void testCommandStatistics() {
        TestProtocolHandler handler = new TestProtocolHandler("STATS_TEST");
        protocolHub.registerProtocolHandler("STATS_TEST", handler);
        
        // 发送3个成功命令
        for (int i = 0; i < 3; i++) {
            CommandPacket packet = createCommandPacket("STATS_TEST", "STATS_CMD");
            protocolHub.handleCommand(packet);
        }
        
        ProtocolStats stats = protocolHub.getProtocolStats("STATS_TEST");
        assertNotNull(stats, "Stats should exist");
        assertEquals(3, stats.getTotalCommands(), "Should have 3 total commands");
        assertEquals(3, stats.getSuccessCommands(), "Should have 3 successful commands");
        assertEquals(0, stats.getFailedCommands(), "Should have 0 failed commands");
    }

    @Test
    @DisplayName("测试获取所有协议统计")
    public void testGetAllProtocolStats() {
        TestProtocolHandler handler1 = new TestProtocolHandler("PROTOCOL_1");
        TestProtocolHandler handler2 = new TestProtocolHandler("PROTOCOL_2");
        
        protocolHub.registerProtocolHandler("PROTOCOL_1", handler1);
        protocolHub.registerProtocolHandler("PROTOCOL_2", handler2);
        
        java.util.List<ProtocolStats> allStats = protocolHub.getAllProtocolStats();
        assertEquals(2, allStats.size(), "Should have stats for 2 protocols");
    }

    @Test
    @DisplayName("测试无效命令处理")
    public void testInvalidCommandHandling() {
        TestProtocolHandler handler = new TestProtocolHandler("INVALID_TEST");
        protocolHub.registerProtocolHandler("INVALID_TEST", handler);
        
        // 测试空包
        CommandResult result = protocolHub.handleCommand(null);
        assertFalse(result.isSuccess(), "Null packet should fail");
        assertEquals(400, result.getCode(), "Should return 400 for invalid packet");
        
        // 测试无协议类型
        CommandPacket packetNoProtocol = new CommandPacket();
        packetNoProtocol.setHeader(new CommandHeader());
        result = protocolHub.handleCommand(packetNoProtocol);
        assertFalse(result.isSuccess(), "Packet without protocol should fail");
        
        // 测试未注册的协议
        CommandPacket packetUnregistered = createCommandPacket("UNREGISTERED", "TEST_CMD");
        result = protocolHub.handleCommand(packetUnregistered);
        assertFalse(result.isSuccess(), "Unregistered protocol should fail");
        assertEquals(404, result.getCode(), "Should return 404 for unregistered protocol");
    }

    @Test
    @DisplayName("测试协议处理器获取")
    public void testGetProtocolHandler() {
        TestProtocolHandler handler = new TestProtocolHandler("GET_HANDLER_TEST");
        protocolHub.registerProtocolHandler("GET_HANDLER_TEST", handler);
        
        ProtocolHandler retrieved = protocolHub.getProtocolHandler("GET_HANDLER_TEST");
        assertNotNull(retrieved, "Should retrieve handler");
        assertTrue(retrieved instanceof TestProtocolHandler, "Retrieved handler should be TestProtocolHandler");
        
        ProtocolHandler notFound = protocolHub.getProtocolHandler("NON_EXISTENT");
        assertNull(notFound, "Should return null for non-existent protocol");
    }

    @Test
    @DisplayName("测试重复注册覆盖")
    public void testRepeatedRegistration() {
        TestProtocolHandler handler1 = new TestProtocolHandler("REPEAT_TEST");
        TestProtocolHandler handler2 = new TestProtocolHandler("REPEAT_TEST");
        
        protocolHub.registerProtocolHandler("REPEAT_TEST", handler1);
        assertEquals(1, protocolHub.getSupportedProtocols().size());
        
        // 重新注册应该覆盖旧的
        protocolHub.registerProtocolHandler("REPEAT_TEST", handler2);
        assertEquals(1, protocolHub.getSupportedProtocols().size(), "Should still have 1 protocol");
    }

    // 辅助方法：创建命令包
    private CommandPacket createCommandPacket(String protocolType, String commandType) {
        CommandHeader header = new CommandHeader();
        header.setProtocolType(protocolType);
        header.setCommandType(commandType);
        header.setCommandId("cmd-001");
        header.setTimestamp(System.currentTimeMillis());
        header.setSourceId("test-source");
        header.setTargetId("test-target");
        
        CommandPacket packet = new CommandPacket(header, new HashMap<>());
        return packet;
    }

    // 测试用的协议处理器实现
    private static class TestProtocolHandler implements ProtocolHandler {
        private final String protocolType;
        private int commandCount = 0;
        
        public TestProtocolHandler(String protocolType) {
            this.protocolType = protocolType;
        }
        
        @Override
        public String getProtocolType() {
            return protocolType;
        }
        
        @Override
        public CommandResult handleCommand(CommandPacket packet) {
            commandCount++;
            String commandId = packet.getHeader() != null ? packet.getHeader().getCommandId() : null;
            return CommandResult.success(commandId);
        }
        
        @Override
        public boolean validateCommand(CommandPacket packet) {
            return true;
        }
        
        @Override
        public ProtocolStatus getStatus() {
            ProtocolStatus status = new ProtocolStatus(protocolType);
            status.markRunning();
            return status;
        }
        
        @Override
        public void initialize() {
            // 空实现
        }
        
        @Override
        public void destroy() {
            // 空实现
        }
        
        @Override
        public boolean supportsCommand(String commandType) {
            return true;
        }
        
        @Override
        public String getDescription() {
            return "Test protocol handler for " + protocolType;
        }
        
        public int getCommandCount() {
            return commandCount;
        }
    }
}
