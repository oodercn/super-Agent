package net.ooder.sdk.scenario;

import net.ooder.sdk.api.OoderSDK;
import net.ooder.sdk.api.agent.McpAgent;
import net.ooder.sdk.api.agent.RouteAgent;
import net.ooder.sdk.api.agent.EndAgent;
import net.ooder.sdk.api.scene.SceneGroup;
import net.ooder.sdk.api.scene.SceneGroupKey;
import net.ooder.sdk.api.scene.SceneGroupManager;
import net.ooder.sdk.api.scene.SceneMember;
import net.ooder.sdk.common.enums.MemberRole;
import net.ooder.sdk.infra.config.SDKConfiguration;
import net.ooder.sdk.infra.lifecycle.LifecycleManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class MultiAgentCollaborationScenarioTest {

    private OoderSDK sdk;
    private SceneGroupManager sceneGroupManager;

    @Before
    public void setUp() throws Exception {
        LifecycleManager.getInstance().reset();
        
        SDKConfiguration config = new SDKConfiguration();
        config.setSkillRootPath(System.getProperty("java.io.tmpdir") + "/ooder-sdk-test-" + System.currentTimeMillis());

        sdk = OoderSDK.builder()
            .configuration(config)
            .build();
        sdk.initialize();
        sdk.start();
        sceneGroupManager = sdk.getSceneGroupManager();
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
    public void testScenario_McpRouteEndCollaboration() throws Exception {
        System.out.println("=== 场景测试: MCP+Route+End协作 ===");

        System.out.println("步骤1: 创建三种类型代理");
        McpAgent mcp = sdk.createMcpAgent();
        RouteAgent route = sdk.createRouteAgent();
        EndAgent end = sdk.createEndAgent();

        System.out.println("步骤2: 验证代理ID");
        assertNotNull("MCP代理ID不应为空", mcp.getAgentId());
        assertNotNull("Route代理ID不应为空", route.getAgentId());
        assertNotNull("End代理ID不应为空", end.getAgentId());

        System.out.println("步骤3: 创建场景组并加入代理");
        SceneGroup sceneGroup = sceneGroupManager.create("collab-scene-1", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();
        
        sceneGroupManager.join(sceneGroupId, mcp.getAgentId(), MemberRole.PRIMARY).join();
        sceneGroupManager.join(sceneGroupId, route.getAgentId(), MemberRole.BACKUP).join();
        sceneGroupManager.join(sceneGroupId, end.getAgentId(), MemberRole.OBSERVER).join();

        System.out.println("步骤4: 验证协作关系");
        List<SceneMember> members = sceneGroupManager.listMembers(sceneGroupId).join();
        assertEquals("成员数量应为3", 3, members.size());

        System.out.println("步骤5: 清理");
        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景测试通过 ===");
    }

    @Test
    public void testScenario_HierarchicalAgentStructure() throws Exception {
        System.out.println("=== 场景测试: 层级代理结构 ===");

        System.out.println("步骤1: 创建层级结构代理");
        McpAgent master = sdk.createMcpAgent();
        RouteAgent router1 = sdk.createRouteAgent();
        RouteAgent router2 = sdk.createRouteAgent();
        EndAgent worker1 = sdk.createEndAgent();
        EndAgent worker2 = sdk.createEndAgent();

        System.out.println("步骤2: 创建场景组");
        SceneGroup sceneGroup = sceneGroupManager.create("hierarchy-scene", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();

        System.out.println("步骤3: 按层级加入");
        sceneGroupManager.join(sceneGroupId, master.getAgentId(), MemberRole.PRIMARY).join();
        sceneGroupManager.join(sceneGroupId, router1.getAgentId(), MemberRole.BACKUP).join();
        sceneGroupManager.join(sceneGroupId, router2.getAgentId(), MemberRole.BACKUP).join();
        sceneGroupManager.join(sceneGroupId, worker1.getAgentId(), MemberRole.OBSERVER).join();
        sceneGroupManager.join(sceneGroupId, worker2.getAgentId(), MemberRole.OBSERVER).join();

        System.out.println("步骤4: 验证层级结构");
        List<SceneMember> members = sceneGroupManager.listMembers(sceneGroupId).join();
        assertEquals("成员数量应为5", 5, members.size());

        System.out.println("步骤5: 验证主代理");
        SceneMember primary = sceneGroupManager.getPrimary(sceneGroupId).join();
        assertEquals("主代理应为master", master.getAgentId(), primary.getAgentId());

        System.out.println("步骤6: 清理");
        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景测试通过 ===");
    }

    @Test
    public void testScenario_AgentHealthMonitoring() throws Exception {
        System.out.println("=== 场景测试: 代理健康监控 ===");

        System.out.println("步骤1: 创建代理");
        McpAgent mcp = sdk.createMcpAgent();
        EndAgent worker = sdk.createEndAgent();

        System.out.println("步骤2: 启动代理");
        mcp.start();
        worker.start();

        System.out.println("步骤3: 验证健康状态");
        assertTrue("MCP应健康", mcp.isHealthy());
        assertTrue("Worker应健康", worker.isHealthy());

        System.out.println("步骤4: 停止代理");
        mcp.stop();
        worker.stop();

        System.out.println("=== 场景测试通过 ===");
    }

    @Test
    public void testScenario_Failover() throws Exception {
        System.out.println("=== 场景测试: 故障转移 ===");

        System.out.println("步骤1: 创建场景组");
        SceneGroup sceneGroup = sceneGroupManager.create("failover-scene", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();

        System.out.println("步骤2: 创建并加入代理");
        McpAgent primary = sdk.createMcpAgent();
        EndAgent backup = sdk.createEndAgent();

        sceneGroupManager.join(sceneGroupId, primary.getAgentId(), MemberRole.PRIMARY).join();
        sceneGroupManager.join(sceneGroupId, backup.getAgentId(), MemberRole.BACKUP).join();

        System.out.println("步骤3: 启动心跳");
        sceneGroupManager.startHeartbeat(sceneGroupId).join();

        System.out.println("步骤4: 模拟故障转移");
        sceneGroupManager.handleFailover(sceneGroupId, primary.getAgentId()).join();

        System.out.println("步骤5: 验证故障转移状态");
        SceneGroupManager.FailoverStatus status = sceneGroupManager.getFailoverStatus(sceneGroupId).join();
        assertNotNull("故障转移状态不应为空", status);

        System.out.println("步骤6: 停止心跳并清理");
        sceneGroupManager.stopHeartbeat(sceneGroupId).join();
        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景测试通过 ===");
    }

    @Test
    public void testScenario_VfsPermission() throws Exception {
        System.out.println("=== 场景测试: VFS权限管理 ===");

        System.out.println("步骤1: 创建场景组");
        SceneGroup sceneGroup = sceneGroupManager.create("vfs-scene", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();

        System.out.println("步骤2: 创建并加入代理");
        EndAgent worker = sdk.createEndAgent();
        sceneGroupManager.join(sceneGroupId, worker.getAgentId(), MemberRole.OBSERVER).join();

        System.out.println("步骤3: 获取VFS权限");
        SceneGroupManager.VfsPermission permission = sceneGroupManager.getVfsPermission(sceneGroupId, worker.getAgentId()).join();
        assertNotNull("VFS权限不应为空", permission);

        System.out.println("步骤4: 清理");
        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景测试通过 ===");
    }

    @Test
    public void testScenario_KeyDistribution() throws Exception {
        System.out.println("=== 场景测试: 密钥分发 ===");

        System.out.println("步骤1: 创建场景组");
        SceneGroup sceneGroup = sceneGroupManager.create("key-scene", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();

        System.out.println("步骤2: 生成密钥");
        SceneGroupKey key = sceneGroupManager.generateKey(sceneGroupId).join();
        assertNotNull("密钥不应为空", key);

        System.out.println("步骤3: 创建并加入代理");
        McpAgent mcp = sdk.createMcpAgent();
        EndAgent end = sdk.createEndAgent();
        sceneGroupManager.join(sceneGroupId, mcp.getAgentId(), MemberRole.PRIMARY).join();
        sceneGroupManager.join(sceneGroupId, end.getAgentId(), MemberRole.OBSERVER).join();

        System.out.println("步骤4: 分发密钥份额");
        sceneGroupManager.distributeKeyShares(sceneGroupId, key).join();

        System.out.println("步骤5: 清理");
        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景测试通过 ===");
    }

    @Test
    public void testScenario_ListByScene() throws Exception {
        System.out.println("=== 场景测试: 按场景列出场景组 ===");

        System.out.println("步骤1: 创建同一场景的多个场景组");
        sceneGroupManager.create("shared-scene", null).join();
        sceneGroupManager.create("shared-scene", null).join();

        System.out.println("步骤2: 按场景ID列出");
        List<SceneGroup> groups = sceneGroupManager.listByScene("shared-scene").join();
        assertTrue("场景组数量应>=2", groups.size() >= 2);

        System.out.println("步骤3: 清理");
        for (SceneGroup group : groups) {
            sceneGroupManager.destroy(group.getSceneGroupId()).join();
        }

        System.out.println("=== 场景测试通过 ===");
    }
}
