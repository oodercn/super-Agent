package net.ooder.examples.skillb;

import net.ooder.sdk.agent.model.AgentConfig;
import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.network.packet.StatusReportPacket;
import net.ooder.sdk.network.udp.UDPMessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class SkillBApplicationTest {

    @Mock
    private AgentSDK agentSDK;

    @Mock
    private SkillCommandHandler commandHandler;

    @InjectMocks
    private SkillBApplication application;

    @BeforeEach
    void setUp() {
        AgentConfig config = AgentConfig.builder()
                .agentId("skill-b-001")
                .agentName("SkillB")
                .agentType("skill")
                .endpoint("http://localhost:9002")
                .udpPort(9003)
                .heartbeatInterval(30000)
                .build();

        when(agentSDK.getConfig()).thenReturn(config);
    }

    @Test
    void testSendTestCommand() {
        Map<String, Object> params = new HashMap<>();
        params.put("data", "test data");

        when(agentSDK.sendCommand(any(CommandType.class), anyMap())).thenReturn(CompletableFuture.completedFuture(net.ooder.sdk.network.udp.SendResult.success(100)));

        application.sendTestCommand();

        verify(agentSDK, times(1)).sendCommand(eq(CommandType.SKILL_SUBMIT), anyMap());
    }

    @Test
    void testSendStatusReport() {
        doNothing().when(agentSDK).sendStatusReport(any(StatusReportPacket.class));

        application.sendStatusReport();

        verify(agentSDK, times(1)).sendStatusReport(any(StatusReportPacket.class));
    }

    @Test
    void testShutdown() {
        doNothing().when(agentSDK).stop();

        application.shutdown();

        verify(agentSDK, times(1)).stop();
    }
}
