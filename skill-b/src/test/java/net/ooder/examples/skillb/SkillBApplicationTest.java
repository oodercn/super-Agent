package net.ooder.examples.skillb;

import net.ooder.sdk.OoderSDK;
import net.ooder.sdk.api.agent.RouteAgent;
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
class SkillBApplicationTest {

    @Mock
    private OoderSDK sdk;

    @Mock
    private RouteAgent routeAgent;

    @Mock
    private SkillPackageManager packageManager;

    private SDKConfiguration config;

    @BeforeEach
    void setUp() {
        config = new SDKConfiguration();
        config.setAgentId("skill-b-001");
        config.setAgentName("SkillB");
        config.setEndpoint("http://localhost:9002");
        config.setUdpPort(9003);
        config.setHeartbeatInterval(30000);

        when(sdk.getConfiguration()).thenReturn(config);
        when(sdk.createRouteAgent()).thenReturn(routeAgent);
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
    void testCreateRouteAgent() {
        when(sdk.createRouteAgent()).thenReturn(routeAgent);
        when(routeAgent.start()).thenReturn(CompletableFuture.completedFuture(null));

        RouteAgent agent = sdk.createRouteAgent();
        agent.start();

        assertNotNull(agent);
        verify(sdk, times(1)).createRouteAgent();
        verify(routeAgent, times(1)).start();
    }

    @Test
    void testInstallSkillFromGitee() {
        when(packageManager.install(any())).thenReturn(CompletableFuture.completedFuture(null));

        packageManager.install(any());

        verify(packageManager, times(1)).install(any());
    }

    @Test
    void testDiscoveryMethod() {
        DiscoveryMethod method = DiscoveryMethod.GITEE;
        assertEquals("gitee", method.getCode());
        assertEquals("Gitee repository discovery", method.getDescription());
    }

    @Test
    void testSdkShutdown() {
        doNothing().when(sdk).stop();

        sdk.stop();

        verify(sdk, times(1)).stop();
    }
}
