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

public class IntegrationScenarioTest {

    private OoderSDK sdk;
    private SceneGroupManager sceneGroupManager;

    @Before
    public void setUp() throws Exception {
        LifecycleManager.getInstance().reset();
        
        System.out.println("========================================");
        System.out.println("  Ooder Agent SDK 集成场景测试套件");
        System.out.println("========================================");

        SDKConfiguration config = new SDKConfiguration();
        config.setSkillRootPath(System.getProperty("java.io.tmpdir") + "/ooder-sdk-integration-test-" + System.currentTimeMillis());

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
        System.out.println("========================================");
        System.out.println("  集成场景测试套件完成");
        System.out.println("========================================");
    }

    @Test
    public void testScenario_SDKLifecycle() throws Exception {
        System.out.println("\n=== 场景1: SDK生命周期 ===");

        System.out.println("步骤1: 验证SDK状态");
        assertTrue("SDK应已初始化", sdk.isInitialized());
        assertTrue("SDK应已启动", sdk.isStarted());
        assertTrue("SDK应正在运行", sdk.isRunning());

        System.out.println("步骤2: 验证SDK组件");
        assertNotNull("配置不应为空", sdk.getConfiguration());
        assertNotNull("AgentFactory不应为空", sdk.getAgentFactory());
        assertNotNull("SceneGroupManager不应为空", sceneGroupManager);

        System.out.println("=== 场景1通过 ===");
    }

    @Test
    public void testScenario_CreateMcpAgent() throws Exception {
        System.out.println("\n=== 场景2: 创建MCP代理 ===");

        System.out.println("步骤1: 创建MCP代理");
        McpAgent mcpAgent = sdk.createMcpAgent();
        assertNotNull("MCP代理不应为空", mcpAgent);

        System.out.println("步骤2: 验证代理ID已生成");
        assertNotNull("代理ID不应为空", mcpAgent.getAgentId());
        assertFalse("代理ID不应为空字符串", mcpAgent.getAgentId().isEmpty());

        System.out.println("步骤3: 启动代理");
        mcpAgent.start();
        assertTrue("代理应健康", mcpAgent.isHealthy());

        System.out.println("步骤4: 停止代理");
        mcpAgent.stop();

        System.out.println("=== 场景2通过 ===");
    }

    @Test
    public void testScenario_CreateRouteAgent() throws Exception {
        System.out.println("\n=== 场景3: 创建Route代理 ===");

        RouteAgent routeAgent = sdk.createRouteAgent();
        assertNotNull("Route代理不应为空", routeAgent);
        assertNotNull("代理ID不应为空", routeAgent.getAgentId());

        routeAgent.start();
        assertTrue("代理应健康", routeAgent.isHealthy());
        routeAgent.stop();

        System.out.println("=== 场景3通过 ===");
    }

    @Test
    public void testScenario_CreateEndAgent() throws Exception {
        System.out.println("\n=== 场景4: 创建End代理 ===");

        EndAgent endAgent = sdk.createEndAgent();
        assertNotNull("End代理不应为空", endAgent);
        assertNotNull("代理ID不应为空", endAgent.getAgentId());

        endAgent.start();
        assertTrue("代理应健康", endAgent.isHealthy());
        endAgent.stop();

        System.out.println("=== 场景4通过 ===");
    }

    @Test
    public void testScenario_MultipleAgents() throws Exception {
        System.out.println("\n=== 场景5: 创建多个代理 ===");

        McpAgent mcp = sdk.createMcpAgent();
        RouteAgent route = sdk.createRouteAgent();
        EndAgent end = sdk.createEndAgent();

        assertNotEquals("MCP和Route的ID应不同", mcp.getAgentId(), route.getAgentId());
        assertNotEquals("Route和End的ID应不同", route.getAgentId(), end.getAgentId());
        assertNotEquals("MCP和End的ID应不同", mcp.getAgentId(), end.getAgentId());

        System.out.println("=== 场景5通过 ===");
    }

    @Test
    public void testScenario_CreateSceneGroup() throws Exception {
        System.out.println("\n=== 场景6: 创建场景组 ===");

        SceneGroup sceneGroup = sceneGroupManager.create("scene-001", null).join();
        assertNotNull("场景组不应为空", sceneGroup);
        assertNotNull("场景组ID不应为空", sceneGroup.getSceneGroupId());

        SceneGroup retrieved = sceneGroupManager.get(sceneGroup.getSceneGroupId()).join();
        assertNotNull("场景组应存在", retrieved);

        sceneGroupManager.destroy(sceneGroup.getSceneGroupId()).join();

        System.out.println("=== 场景6通过 ===");
    }

    @Test
    public void testScenario_AgentJoinSceneGroup() throws Exception {
        System.out.println("\n=== 场景7: 代理加入场景组 ===");

        SceneGroup sceneGroup = sceneGroupManager.create("scene-002", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();

        McpAgent mcp = sdk.createMcpAgent();
        RouteAgent route = sdk.createRouteAgent();
        EndAgent end = sdk.createEndAgent();

        sceneGroupManager.join(sceneGroupId, mcp.getAgentId(), MemberRole.PRIMARY).join();
        sceneGroupManager.join(sceneGroupId, route.getAgentId(), MemberRole.BACKUP).join();
        sceneGroupManager.join(sceneGroupId, end.getAgentId(), MemberRole.OBSERVER).join();

        List<SceneMember> members = sceneGroupManager.listMembers(sceneGroupId).join();
        assertEquals("成员数量应为3", 3, members.size());

        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景7通过 ===");
    }

    @Test
    public void testScenario_AgentLeaveSceneGroup() throws Exception {
        System.out.println("\n=== 场景8: 代理离开场景组 ===");

        SceneGroup sceneGroup = sceneGroupManager.create("scene-003", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();
        McpAgent mcp = sdk.createMcpAgent();
        EndAgent end = sdk.createEndAgent();

        sceneGroupManager.join(sceneGroupId, mcp.getAgentId(), MemberRole.PRIMARY).join();
        sceneGroupManager.join(sceneGroupId, end.getAgentId(), MemberRole.OBSERVER).join();

        List<SceneMember> members = sceneGroupManager.listMembers(sceneGroupId).join();
        assertEquals("成员数量应为2", 2, members.size());

        sceneGroupManager.leave(sceneGroupId, end.getAgentId()).join();

        members = sceneGroupManager.listMembers(sceneGroupId).join();
        assertEquals("成员数量应为1", 1, members.size());

        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景8通过 ===");
    }

    @Test
    public void testScenario_ChangeMemberRole() throws Exception {
        System.out.println("\n=== 场景9: 更改成员角色 ===");

        SceneGroup sceneGroup = sceneGroupManager.create("scene-004", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();

        EndAgent end = sdk.createEndAgent();
        sceneGroupManager.join(sceneGroupId, end.getAgentId(), MemberRole.OBSERVER).join();

        MemberRole initialRole = sceneGroupManager.getRole(sceneGroupId, end.getAgentId()).join();
        assertEquals("初始角色应为OBSERVER", MemberRole.OBSERVER, initialRole);

        sceneGroupManager.changeRole(sceneGroupId, end.getAgentId(), MemberRole.BACKUP).join();

        MemberRole newRole = sceneGroupManager.getRole(sceneGroupId, end.getAgentId()).join();
        assertEquals("新角色应为BACKUP", MemberRole.BACKUP, newRole);

        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景9通过 ===");
    }

    @Test
    public void testScenario_GetPrimaryAndBackups() throws Exception {
        System.out.println("\n=== 场景10: 获取主代理和备份代理 ===");

        SceneGroup sceneGroup = sceneGroupManager.create("scene-005", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();

        McpAgent mcp = sdk.createMcpAgent();
        EndAgent backup1 = sdk.createEndAgent();
        EndAgent backup2 = sdk.createEndAgent();

        sceneGroupManager.join(sceneGroupId, mcp.getAgentId(), MemberRole.PRIMARY).join();
        sceneGroupManager.join(sceneGroupId, backup1.getAgentId(), MemberRole.BACKUP).join();
        sceneGroupManager.join(sceneGroupId, backup2.getAgentId(), MemberRole.BACKUP).join();

        SceneMember primary = sceneGroupManager.getPrimary(sceneGroupId).join();
        assertNotNull("主代理不应为空", primary);
        assertEquals("主代理ID应匹配", mcp.getAgentId(), primary.getAgentId());

        List<SceneMember> backups = sceneGroupManager.getBackups(sceneGroupId).join();
        assertEquals("备份代理数量应为2", 2, backups.size());

        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景10通过 ===");
    }

    @Test
    public void testScenario_McpRouteEndCollaboration() throws Exception {
        System.out.println("\n=== 场景11: MCP+Route+End协作 ===");

        McpAgent mcp = sdk.createMcpAgent();
        RouteAgent route = sdk.createRouteAgent();
        EndAgent end = sdk.createEndAgent();

        SceneGroup sceneGroup = sceneGroupManager.create("collab-scene", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();
        
        sceneGroupManager.join(sceneGroupId, mcp.getAgentId(), MemberRole.PRIMARY).join();
        sceneGroupManager.join(sceneGroupId, route.getAgentId(), MemberRole.BACKUP).join();
        sceneGroupManager.join(sceneGroupId, end.getAgentId(), MemberRole.OBSERVER).join();

        List<SceneMember> members = sceneGroupManager.listMembers(sceneGroupId).join();
        assertEquals("成员数量应为3", 3, members.size());

        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景11通过 ===");
    }

    @Test
    public void testScenario_HierarchicalAgentStructure() throws Exception {
        System.out.println("\n=== 场景12: 层级代理结构 ===");

        McpAgent master = sdk.createMcpAgent();
        RouteAgent router1 = sdk.createRouteAgent();
        RouteAgent router2 = sdk.createRouteAgent();
        EndAgent worker1 = sdk.createEndAgent();
        EndAgent worker2 = sdk.createEndAgent();

        SceneGroup sceneGroup = sceneGroupManager.create("hierarchy-scene", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();

        sceneGroupManager.join(sceneGroupId, master.getAgentId(), MemberRole.PRIMARY).join();
        sceneGroupManager.join(sceneGroupId, router1.getAgentId(), MemberRole.BACKUP).join();
        sceneGroupManager.join(sceneGroupId, router2.getAgentId(), MemberRole.BACKUP).join();
        sceneGroupManager.join(sceneGroupId, worker1.getAgentId(), MemberRole.OBSERVER).join();
        sceneGroupManager.join(sceneGroupId, worker2.getAgentId(), MemberRole.OBSERVER).join();

        List<SceneMember> members = sceneGroupManager.listMembers(sceneGroupId).join();
        assertEquals("成员数量应为5", 5, members.size());

        SceneMember primary = sceneGroupManager.getPrimary(sceneGroupId).join();
        assertEquals("主代理应为master", master.getAgentId(), primary.getAgentId());

        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景12通过 ===");
    }

    @Test
    public void testScenario_AgentHealthMonitoring() throws Exception {
        System.out.println("\n=== 场景13: 代理健康监控 ===");

        McpAgent mcp = sdk.createMcpAgent();
        EndAgent worker = sdk.createEndAgent();

        mcp.start();
        worker.start();

        assertTrue("MCP应健康", mcp.isHealthy());
        assertTrue("Worker应健康", worker.isHealthy());

        mcp.stop();
        worker.stop();

        System.out.println("=== 场景13通过 ===");
    }

    @Test
    public void testScenario_Failover() throws Exception {
        System.out.println("\n=== 场景14: 故障转移 ===");

        SceneGroup sceneGroup = sceneGroupManager.create("failover-scene", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();

        McpAgent primary = sdk.createMcpAgent();
        EndAgent backup = sdk.createEndAgent();

        sceneGroupManager.join(sceneGroupId, primary.getAgentId(), MemberRole.PRIMARY).join();
        sceneGroupManager.join(sceneGroupId, backup.getAgentId(), MemberRole.BACKUP).join();

        sceneGroupManager.startHeartbeat(sceneGroupId).join();
        sceneGroupManager.handleFailover(sceneGroupId, primary.getAgentId()).join();

        SceneGroupManager.FailoverStatus status = sceneGroupManager.getFailoverStatus(sceneGroupId).join();
        assertNotNull("故障转移状态不应为空", status);

        sceneGroupManager.stopHeartbeat(sceneGroupId).join();
        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景14通过 ===");
    }

    @Test
    public void testScenario_VfsPermission() throws Exception {
        System.out.println("\n=== 场景15: VFS权限管理 ===");

        SceneGroup sceneGroup = sceneGroupManager.create("vfs-scene", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();

        EndAgent worker = sdk.createEndAgent();
        sceneGroupManager.join(sceneGroupId, worker.getAgentId(), MemberRole.OBSERVER).join();

        SceneGroupManager.VfsPermission permission = sceneGroupManager.getVfsPermission(sceneGroupId, worker.getAgentId()).join();
        assertNotNull("VFS权限不应为空", permission);

        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景15通过 ===");
    }

    @Test
    public void testScenario_KeyDistribution() throws Exception {
        System.out.println("\n=== 场景16: 密钥分发 ===");

        SceneGroup sceneGroup = sceneGroupManager.create("key-scene", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();

        SceneGroupKey key = sceneGroupManager.generateKey(sceneGroupId).join();
        assertNotNull("密钥不应为空", key);

        McpAgent mcp = sdk.createMcpAgent();
        EndAgent end = sdk.createEndAgent();
        sceneGroupManager.join(sceneGroupId, mcp.getAgentId(), MemberRole.PRIMARY).join();
        sceneGroupManager.join(sceneGroupId, end.getAgentId(), MemberRole.OBSERVER).join();

        sceneGroupManager.distributeKeyShares(sceneGroupId, key).join();

        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景16通过 ===");
    }

    @Test
    public void testScenario_Heartbeat() throws Exception {
        System.out.println("\n=== 场景17: 心跳管理 ===");

        SceneGroup sceneGroup = sceneGroupManager.create("heartbeat-scene", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();

        sceneGroupManager.startHeartbeat(sceneGroupId).join();
        sceneGroupManager.stopHeartbeat(sceneGroupId).join();

        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景17通过 ===");
    }

    @Test
    public void testScenario_MultipleSceneGroups() throws Exception {
        System.out.println("\n=== 场景18: 多场景组管理 ===");

        SceneGroup group1 = sceneGroupManager.create("scene-multi-1", null).join();
        SceneGroup group2 = sceneGroupManager.create("scene-multi-2", null).join();
        SceneGroup group3 = sceneGroupManager.create("scene-multi-3", null).join();

        assertNotEquals("场景组ID应不同", group1.getSceneGroupId(), group2.getSceneGroupId());
        assertNotEquals("场景组ID应不同", group2.getSceneGroupId(), group3.getSceneGroupId());

        List<SceneGroup> allGroups = sceneGroupManager.listAll().join();
        assertTrue("场景组数量应>=3", allGroups.size() >= 3);

        sceneGroupManager.destroy(group1.getSceneGroupId()).join();
        sceneGroupManager.destroy(group2.getSceneGroupId()).join();
        sceneGroupManager.destroy(group3.getSceneGroupId()).join();

        System.out.println("=== 场景18通过 ===");
    }
}
