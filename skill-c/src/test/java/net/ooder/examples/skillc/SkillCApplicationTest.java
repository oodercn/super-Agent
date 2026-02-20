package net.ooder.examples.skillc;

import net.ooder.sdk.OoderSDK;
import net.ooder.sdk.api.agent.McpAgent;
import net.ooder.sdk.infra.config.SDKConfiguration;
import net.ooder.sdk.api.skill.SkillPackageManager;
import net.ooder.sdk.common.enums.DiscoveryMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillCApplicationTest {

    @Mock
    private OoderSDK sdk;

    @Mock
    private McpAgent mcpAgent;

    @Mock
    private SkillPackageManager packageManager;

    private SDKConfiguration config;

    @BeforeEach
    void setUp() {
        config = new SDKConfiguration();
        config.setAgentId("skill-c-001");
        config.setAgentName("SkillC");
        config.setEndpoint("http://localhost:9004");
        config.setUdpPort(9005);
        config.setHeartbeatInterval(30000);

        when(sdk.getConfiguration()).thenReturn(config);
        when(sdk.createMcpAgent()).thenReturn(mcpAgent);
        when(sdk.getSkillPackageManager()).thenReturn(packageManager);
    }

    @Test
    void testSdkInitialization() {
        doNothing().when(sdk).initialize();
        doNothing().when(sdk).start();

        sdk.initialize();
        sdk.start();

        verify(sdk, times(1)).initialize();
        verify(sdk, times(1)).start();
    }

    @Test
    void testCreateMcpAgent() {
        when(sdk.createMcpAgent()).thenReturn(mcpAgent);
        when(mcpAgent.start()).thenReturn(CompletableFuture.completedFuture(null));

        McpAgent agent = sdk.createMcpAgent();
        agent.start();

        assertNotNull(agent);
        verify(sdk, times(1)).createMcpAgent();
        verify(mcpAgent, times(1)).start();
    }

    @Test
    void testInstallSkillFromSkillCenter() {
        when(packageManager.install(any())).thenReturn(CompletableFuture.completedFuture(null));

        packageManager.install(any());

        verify(packageManager, times(1)).install(any());
    }

    @Test
    void testDiscoveryMethod() {
        DiscoveryMethod method = DiscoveryMethod.SKILL_CENTER;
        assertEquals("skill_center", method.getCode());
        assertEquals("SkillCenter API discovery", method.getDescription());
    }

    @Test
    void testSdkShutdown() {
        doNothing().when(sdk).stop();

        sdk.stop();

        verify(sdk, times(1)).stop();
    }
}
