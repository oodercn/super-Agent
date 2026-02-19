package net.ooder.sdk.scenario;

import net.ooder.sdk.api.OoderSDK;
import net.ooder.sdk.api.agent.McpAgent;
import net.ooder.sdk.api.agent.RouteAgent;
import net.ooder.sdk.api.agent.EndAgent;
import net.ooder.sdk.api.scene.SceneGroup;
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

public class SceneGroupScenarioTest {

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
    public void testScenario_CreateSceneGroup() throws Exception {
        System.out.println("=== 场景测试: 创建场景组 ===");

        System.out.println("步骤1: 创建场景组");
        SceneGroup sceneGroup = sceneGroupManager.create("scene-001", null).join();
        assertNotNull("场景组不应为空", sceneGroup);
        assertNotNull("场景组ID不应为空", sceneGroup.getSceneGroupId());

        System.out.println("步骤2: 验证场景组存在");
        SceneGroup retrieved = sceneGroupManager.get(sceneGroup.getSceneGroupId()).join();
        assertNotNull("场景组应存在", retrieved);

        System.out.println("步骤3: 销毁场景组");
        sceneGroupManager.destroy(sceneGroup.getSceneGroupId()).join();

        System.out.println("=== 场景测试通过 ===");
    }

    @Test
    public void testScenario_AgentJoinSceneGroup() throws Exception {
        System.out.println("=== 场景测试: 代理加入场景组 ===");

        System.out.println("步骤1: 创建场景组");
        SceneGroup sceneGroup = sceneGroupManager.create("scene-002", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();

        System.out.println("步骤2: 创建代理");
        McpAgent mcp = sdk.createMcpAgent();
        RouteAgent route = sdk.createRouteAgent();
        EndAgent end = sdk.createEndAgent();

        System.out.println("步骤3: 代理加入场景组");
        sceneGroupManager.join(sceneGroupId, mcp.getAgentId(), MemberRole.PRIMARY).join();
        sceneGroupManager.join(sceneGroupId, route.getAgentId(), MemberRole.BACKUP).join();
        sceneGroupManager.join(sceneGroupId, end.getAgentId(), MemberRole.OBSERVER).join();

        System.out.println("步骤4: 验证成员数量");
        List<SceneMember> members = sceneGroupManager.listMembers(sceneGroupId).join();
        assertEquals("成员数量应为3", 3, members.size());

        System.out.println("步骤5: 清理");
        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景测试通过 ===");
    }

    @Test
    public void testScenario_AgentLeaveSceneGroup() throws Exception {
        System.out.println("=== 场景测试: 代理离开场景组 ===");

        System.out.println("步骤1: 创建场景组并加入代理");
        SceneGroup sceneGroup = sceneGroupManager.create("scene-003", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();
        McpAgent mcp = sdk.createMcpAgent();
        EndAgent end = sdk.createEndAgent();

        sceneGroupManager.join(sceneGroupId, mcp.getAgentId(), MemberRole.PRIMARY).join();
        sceneGroupManager.join(sceneGroupId, end.getAgentId(), MemberRole.OBSERVER).join();

        List<SceneMember> members = sceneGroupManager.listMembers(sceneGroupId).join();
        assertEquals("成员数量应为2", 2, members.size());

        System.out.println("步骤2: 代理离开场景组");
        sceneGroupManager.leave(sceneGroupId, end.getAgentId()).join();

        System.out.println("步骤3: 验证成员变化");
        members = sceneGroupManager.listMembers(sceneGroupId).join();
        assertEquals("成员数量应为1", 1, members.size());

        System.out.println("步骤4: 清理");
        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景测试通过 ===");
    }

    @Test
    public void testScenario_ChangeMemberRole() throws Exception {
        System.out.println("=== 场景测试: 更改成员角色 ===");

        System.out.println("步骤1: 创建场景组");
        SceneGroup sceneGroup = sceneGroupManager.create("scene-004", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();

        System.out.println("步骤2: 创建并加入代理");
        EndAgent end = sdk.createEndAgent();
        sceneGroupManager.join(sceneGroupId, end.getAgentId(), MemberRole.OBSERVER).join();

        System.out.println("步骤3: 验证初始角色");
        MemberRole initialRole = sceneGroupManager.getRole(sceneGroupId, end.getAgentId()).join();
        assertEquals("初始角色应为OBSERVER", MemberRole.OBSERVER, initialRole);

        System.out.println("步骤4: 更改角色");
        sceneGroupManager.changeRole(sceneGroupId, end.getAgentId(), MemberRole.BACKUP).join();

        System.out.println("步骤5: 验证新角色");
        MemberRole newRole = sceneGroupManager.getRole(sceneGroupId, end.getAgentId()).join();
        assertEquals("新角色应为BACKUP", MemberRole.BACKUP, newRole);

        System.out.println("步骤6: 清理");
        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景测试通过 ===");
    }

    @Test
    public void testScenario_GetPrimaryAndBackups() throws Exception {
        System.out.println("=== 场景测试: 获取主代理和备份代理 ===");

        System.out.println("步骤1: 创建场景组");
        SceneGroup sceneGroup = sceneGroupManager.create("scene-005", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();

        System.out.println("步骤2: 创建并加入代理");
        McpAgent mcp = sdk.createMcpAgent();
        EndAgent backup1 = sdk.createEndAgent();
        EndAgent backup2 = sdk.createEndAgent();

        sceneGroupManager.join(sceneGroupId, mcp.getAgentId(), MemberRole.PRIMARY).join();
        sceneGroupManager.join(sceneGroupId, backup1.getAgentId(), MemberRole.BACKUP).join();
        sceneGroupManager.join(sceneGroupId, backup2.getAgentId(), MemberRole.BACKUP).join();

        System.out.println("步骤3: 获取主代理");
        SceneMember primary = sceneGroupManager.getPrimary(sceneGroupId).join();
        assertNotNull("主代理不应为空", primary);
        assertEquals("主代理ID应匹配", mcp.getAgentId(), primary.getAgentId());

        System.out.println("步骤4: 获取备份代理");
        List<SceneMember> backups = sceneGroupManager.getBackups(sceneGroupId).join();
        assertEquals("备份代理数量应为2", 2, backups.size());

        System.out.println("步骤5: 清理");
        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景测试通过 ===");
    }

    @Test
    public void testScenario_MultipleSceneGroups() throws Exception {
        System.out.println("=== 场景测试: 多场景组管理 ===");

        System.out.println("步骤1: 创建多个场景组");
        SceneGroup group1 = sceneGroupManager.create("scene-multi-1", null).join();
        SceneGroup group2 = sceneGroupManager.create("scene-multi-2", null).join();
        SceneGroup group3 = sceneGroupManager.create("scene-multi-3", null).join();

        System.out.println("步骤2: 验证场景组ID唯一性");
        assertNotEquals("场景组ID应不同", group1.getSceneGroupId(), group2.getSceneGroupId());
        assertNotEquals("场景组ID应不同", group2.getSceneGroupId(), group3.getSceneGroupId());

        System.out.println("步骤3: 列出所有场景组");
        List<SceneGroup> allGroups = sceneGroupManager.listAll().join();
        assertTrue("场景组数量应>=3", allGroups.size() >= 3);

        System.out.println("步骤4: 清理所有场景组");
        sceneGroupManager.destroy(group1.getSceneGroupId()).join();
        sceneGroupManager.destroy(group2.getSceneGroupId()).join();
        sceneGroupManager.destroy(group3.getSceneGroupId()).join();

        System.out.println("=== 场景测试通过 ===");
    }

    @Test
    public void testScenario_Heartbeat() throws Exception {
        System.out.println("=== 场景测试: 心跳管理 ===");

        System.out.println("步骤1: 创建场景组");
        SceneGroup sceneGroup = sceneGroupManager.create("heartbeat-scene", null).join();
        String sceneGroupId = sceneGroup.getSceneGroupId();

        System.out.println("步骤2: 启动心跳");
        sceneGroupManager.startHeartbeat(sceneGroupId).join();

        System.out.println("步骤3: 停止心跳");
        sceneGroupManager.stopHeartbeat(sceneGroupId).join();

        System.out.println("步骤4: 清理");
        sceneGroupManager.destroy(sceneGroupId).join();

        System.out.println("=== 场景测试通过 ===");
    }
}
