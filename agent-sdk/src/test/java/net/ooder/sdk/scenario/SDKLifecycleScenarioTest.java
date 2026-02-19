package net.ooder.sdk.scenario;

import net.ooder.sdk.api.OoderSDK;
import net.ooder.sdk.infra.config.SDKConfiguration;
import net.ooder.sdk.infra.lifecycle.LifecycleManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SDKLifecycleScenarioTest {

    private OoderSDK sdk;
    private SDKConfiguration config;

    @Before
    public void setUp() {
        LifecycleManager.getInstance().reset();
        config = new SDKConfiguration();
        config.setSkillRootPath(System.getProperty("java.io.tmpdir") + "/ooder-sdk-test-" + System.currentTimeMillis());
    }

    @After
    public void tearDown() {
        if (sdk != null) {
            try {
                sdk.shutdown();
            } catch (Exception e) {
            }
        }
        LifecycleManager.getInstance().reset();
    }

    @Test
    public void testScenario_FullLifecycle() throws Exception {
        System.out.println("=== 场景测试: SDK完整生命周期 ===");

        System.out.println("步骤1: 创建SDK实例");
        sdk = OoderSDK.builder()
            .configuration(config)
            .build();
        assertNotNull("SDK实例不应为空", sdk);
        assertFalse("SDK不应已初始化", sdk.isInitialized());
        assertFalse("SDK不应已启动", sdk.isStarted());

        System.out.println("步骤2: 初始化SDK");
        sdk.initialize();
        assertTrue("SDK应已初始化", sdk.isInitialized());
        assertFalse("SDK不应已启动", sdk.isStarted());

        System.out.println("步骤3: 启动SDK");
        sdk.start();
        assertTrue("SDK应已初始化", sdk.isInitialized());
        assertTrue("SDK应已启动", sdk.isStarted());
        assertTrue("SDK应正在运行", sdk.isRunning());

        System.out.println("步骤4: 验证SDK组件");
        assertNotNull("配置不应为空", sdk.getConfiguration());
        assertNotNull("AgentFactory不应为空", sdk.getAgentFactory());

        System.out.println("=== 场景测试通过 ===");
    }

    @Test
    public void testScenario_DoubleInitialize() throws Exception {
        System.out.println("=== 场景测试: 重复初始化 ===");

        sdk = OoderSDK.builder()
            .configuration(config)
            .build();

        System.out.println("步骤1: 第一次初始化");
        sdk.initialize();
        assertTrue(sdk.isInitialized());

        System.out.println("步骤2: 第二次初始化(应幂等)");
        sdk.initialize();
        assertTrue(sdk.isInitialized());

        System.out.println("=== 场景测试通过 ===");
    }

    @Test
    public void testScenario_DoubleStart() throws Exception {
        System.out.println("=== 场景测试: 重复启动 ===");

        sdk = OoderSDK.builder()
            .configuration(config)
            .build();

        sdk.initialize();

        System.out.println("步骤1: 第一次启动");
        sdk.start();
        assertTrue(sdk.isStarted());

        System.out.println("步骤2: 第二次启动(应幂等)");
        sdk.start();
        assertTrue(sdk.isStarted());

        System.out.println("=== 场景测试通过 ===");
    }

    @Test
    public void testScenario_ConfigurationAccess() throws Exception {
        System.out.println("=== 场景测试: 配置访问 ===");

        SDKConfiguration customConfig = new SDKConfiguration();
        customConfig.setSkillRootPath("/custom/skills/");
        customConfig.setHeartbeatInterval(60);
        customConfig.setThreadPoolSize(20);

        sdk = OoderSDK.builder()
            .configuration(customConfig)
            .build();

        SDKConfiguration retrieved = sdk.getConfiguration();
        assertNotNull("配置不应为空", retrieved);

        System.out.println("=== 场景测试通过 ===");
    }

    @Test
    public void testScenario_StopAndRestart() throws Exception {
        System.out.println("=== 场景测试: 停止并重启 ===");

        sdk = OoderSDK.builder()
            .configuration(config)
            .build();
        sdk.initialize();
        sdk.start();

        System.out.println("步骤1: 停止SDK");
        sdk.stop();
        assertFalse("SDK不应已启动", sdk.isStarted());

        System.out.println("步骤2: 重新初始化并启动SDK");
        LifecycleManager.getInstance().reset();
        sdk = OoderSDK.builder()
            .configuration(config)
            .build();
        sdk.initialize();
        sdk.start();
        assertTrue("SDK应已启动", sdk.isStarted());

        System.out.println("=== 场景测试通过 ===");
    }
}
