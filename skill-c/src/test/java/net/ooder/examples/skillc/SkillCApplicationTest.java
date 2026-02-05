package net.ooder.examples.skillc;

import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.agent.model.AgentConfig;
import net.ooder.sdk.command.model.CommandType;
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
class SkillCApplicationTest {

    @Mock
    private AgentSDK agentSDK;

    @Mock
    private SkillCommandHandler commandHandler;

    @InjectMocks
    private SkillCApplication application;

    @BeforeEach
    void setUp() {
        AgentConfig config = AgentConfig.builder()
                .agentId("skill-c-001")
                .agentName("SkillC")
                .agentType("route")
                .endpoint("http://localhost:9004")
                .udpPort(9005)
                .heartbeatInterval(30000)
                .build();

        when(agentSDK.getConfig()).thenReturn(config);
    }

    @Test
    void testSendTestCommand() {
        Map<String, Object> params = new HashMap<>();
        params.put("route", "test route");

        when(agentSDK.sendCommand(any(CommandType.class), anyMap())).thenReturn(CompletableFuture.completedFuture(net.ooder.sdk.network.udp.SendResult.success(100)));

        application.sendTestCommand();

        verify(agentSDK, times(1)).sendCommand(eq(CommandType.ROUTE_FORWARD), anyMap());
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
