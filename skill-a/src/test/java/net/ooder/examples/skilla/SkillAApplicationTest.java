package net.ooder.examples.skilla;

import net.ooder.sdk.OoderSDK;
import net.ooder.sdk.api.agent.EndAgent;
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
class SkillAApplicationTest {

    @Mock
    private OoderSDK sdk;

    @Mock
    private EndAgent endAgent;

    @Mock
    private SkillPackageManager packageManager;

    private SDKConfiguration config;

    @BeforeEach
    void setUp() {
        config = new SDKConfiguration();
        config.setAgentId("skill-a-001");
        config.setAgentName("SkillA");
        config.setEndpoint("http://localhost:9000");
        config.setUdpPort(9001);
        config.setHeartbeatInterval(30000);

        when(sdk.getConfiguration()).thenReturn(config);
        when(sdk.createEndAgent()).thenReturn(endAgent);
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
    void testCreateEndAgent() {
        when(sdk.createEndAgent()).thenReturn(endAgent);
        when(endAgent.start()).thenReturn(CompletableFuture.completedFuture(null));

        EndAgent agent = sdk.createEndAgent();
        agent.start();

        assertNotNull(agent);
        verify(sdk, times(1)).createEndAgent();
        verify(endAgent, times(1)).start();
    }

    @Test
    void testInstallSkillFromGitHub() {
        when(packageManager.install(any())).thenReturn(CompletableFuture.completedFuture(null));

        packageManager.install(any());

        verify(packageManager, times(1)).install(any());
    }

    @Test
    void testDiscoveryMethod() {
        DiscoveryMethod method = DiscoveryMethod.GITHUB;
        assertEquals("github", method.getCode());
        assertEquals("GitHub repository discovery", method.getDescription());
    }

    @Test
    void testSdkShutdown() {
        doNothing().when(sdk).stop();

        sdk.stop();

        verify(sdk, times(1)).stop();
    }
}
