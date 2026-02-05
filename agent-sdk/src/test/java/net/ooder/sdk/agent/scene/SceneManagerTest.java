package net.ooder.sdk.agent.scene;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class SceneManagerTest {
    private SceneManager sceneManager;
    private SceneDefinition testScene;
    private SceneMember testMember1;
    private SceneMember testMember2;

    @BeforeEach
    public void setUp() {
        sceneManager = new SceneManager();
        
        // 创建测试场景定义
        MemberRole role1 = new MemberRole("role1", "Role 1", Arrays.asList("cap1", "cap2"), true);
        MemberRole role2 = new MemberRole("role2", "Role 2", Arrays.asList("cap3"), false);
        
        testScene = new SceneDefinition("test-scene-1", "Test Scene", 
                Arrays.asList(role1, role2), "test-protocol", "high");
        
        // 创建测试成员
        testMember1 = new SceneMember("agent-1", "role1", Arrays.asList("cap1", "cap2"), 
                Collections.singletonMap("main", "udp://127.0.0.1:8001"));
        
        testMember2 = new SceneMember("agent-2", "role2", Arrays.asList("cap3"), 
                Collections.singletonMap("main", "udp://127.0.0.1:8002"));
        
    }

    @Test
    public void testCreateScene() throws ExecutionException, InterruptedException {
        // 测试创建场景
        boolean result = sceneManager.createScene(testScene).get();
        assertTrue(result, "Scene creation should succeed");
        
        // 验证场景存在
        SceneDefinition retrievedScene = sceneManager.getScene("test-scene-1");
        assertNotNull(retrievedScene, "Retrieved scene should not be null");
        assertEquals("test-scene-1", retrievedScene.getSceneId(), "Scene IDs should match");
        
        // 验证场景状态
        SceneState sceneState = sceneManager.getSceneState("test-scene-1");
        assertNotNull(sceneState, "Scene state should be created");
        assertEquals(SceneState.StateType.CREATING, sceneState.getCurrentState(), "Initial scene state should be CREATING");
        
    }

    @Test
    public void testAddAndRemoveMember() throws ExecutionException, InterruptedException {
        // 创建场景
        sceneManager.createScene(testScene).get();
        
        // 测试添加成员
        boolean addResult = sceneManager.addMember("test-scene-1", testMember1).get();
        assertTrue(addResult, "Adding member should succeed");
        
        // 验证成员已添加
        assertNotNull(sceneManager.getMembers("test-scene-1").get("agent-1"), 
                "Member should be in scene");
        
        // 验证成员状态
        SceneState sceneState = sceneManager.getSceneState("test-scene-1");
        SceneState.MemberState memberState = sceneState.getMemberState("agent-1");
        assertNotNull(memberState, "Member state should be created");
        assertEquals(SceneState.MemberState.MemberStateType.JOINING, memberState.getState(), "Initial member state should be JOINING");
        
        // 测试移除成员
        boolean removeResult = sceneManager.removeMember("test-scene-1", "agent-1").get();
        assertTrue(removeResult, "Removing member should succeed");
        
        // 验证成员已移除
        assertNull(sceneManager.getMembers("test-scene-1").get("agent-1"), 
                "Member should not be in scene after removal");
        
    }

    @Test
    public void testSceneStateTransitions() throws ExecutionException, InterruptedException {
        // 创建场景
        sceneManager.createScene(testScene).get();
        
        // 测试更新场景状态
        boolean updateResult = sceneManager.updateSceneState("test-scene-1", 
                SceneState.StateType.READY, null);
        assertTrue(updateResult, "Updating scene state should succeed");
        
        // 验证状态已更新
        SceneState sceneState = sceneManager.getSceneState("test-scene-1");
        assertEquals(SceneState.StateType.READY, sceneState.getCurrentState(), "Scene state should be READY");
        
        // 测试成员状态更新
        updateResult = sceneManager.updateMemberState("test-scene-1", "agent-1",
                SceneState.MemberState.MemberStateType.ACTIVE, null);
        assertTrue(updateResult, "Updating member state should succeed");
        
        // 验证成员状态已更新
        SceneState.MemberState memberState = sceneState.getMemberState("agent-1");
        assertNotNull(memberState, "Member state should exist");
        assertEquals(SceneState.MemberState.MemberStateType.ACTIVE, memberState.getState(), "Member state should be ACTIVE");
        
    }

    @Test
    public void testSceneSnapshots() throws ExecutionException, InterruptedException {
        // 创建场景并添加成员
        sceneManager.createScene(testScene).get();
        sceneManager.addMember("test-scene-1", testMember1).get();
        sceneManager.addMember("test-scene-1", testMember2).get();
        
        // 测试创建快照
        SceneSnapshot snapshot = sceneManager.createSnapshot("test-scene-1", "Test snapshot", "test-user");
        assertNotNull(snapshot, "Snapshot should be created");
        assertEquals("test-scene-1", snapshot.getSceneId(), "Snapshot scene ID should match");
        
        // 验证快照包含场景定义
        assertTrue(snapshot.hasSceneDefinition(), "Snapshot should contain scene definition");
        assertEquals("test-scene-1", snapshot.getSceneDefinition().getSceneId(), "Snapshot scene definition should match");
        
        // 验证快照包含成员
        assertTrue(snapshot.hasSceneMembers(), "Snapshot should contain scene members");
        assertEquals(2, snapshot.getSceneMembers().size(), "Snapshot should contain 2 members");
        
        // 测试列出快照
        List<SceneSnapshot> snapshots = sceneManager.listSnapshots("test-scene-1");
        assertFalse(snapshots.isEmpty(), "Snapshots list should not be empty");
        
        // 测试获取特定快照
        SceneSnapshot retrievedSnapshot = sceneManager.getSnapshot("test-scene-1", snapshot.getSnapshotId());
        assertNotNull(retrievedSnapshot, "Retrieved snapshot should not be null");
        assertEquals(snapshot.getSnapshotId(), retrievedSnapshot.getSnapshotId(), "Retrieved snapshot ID should match");
        
        // 测试删除快照
        boolean deleteResult = sceneManager.deleteSnapshot("test-scene-1", snapshot.getSnapshotId());
        assertTrue(deleteResult, "Deleting snapshot should succeed");
        
        // 验证快照已删除
        retrievedSnapshot = sceneManager.getSnapshot("test-scene-1", snapshot.getSnapshotId());
        assertNull(retrievedSnapshot, "Snapshot should not be found after deletion");
        
    }

    @Test
    public void testRestoreSnapshot() throws ExecutionException, InterruptedException {
        // 创建初始场景
        sceneManager.createScene(testScene).get();
        sceneManager.addMember("test-scene-1", testMember1).get();
        
        // 创建快照
        SceneSnapshot snapshot = sceneManager.createSnapshot("test-scene-1", "Before changes", "test-user");
        
        // 修改场景
        sceneManager.addMember("test-scene-1", testMember2).get();
        sceneManager.updateSceneState("test-scene-1", SceneState.StateType.RUNNING, null);
        
        // 验证修改已应用
        assertEquals(2, sceneManager.getMembers("test-scene-1").size(), "Scene should have 2 members after modification");
        assertEquals(SceneState.StateType.RUNNING, sceneManager.getSceneState("test-scene-1").getCurrentState(), "Scene state should be RUNNING after modification");
        
        // 恢复到快照
        boolean restoreResult = sceneManager.restoreSnapshot("test-scene-1", snapshot.getSnapshotId()).get();
        assertTrue(restoreResult, "Restoring snapshot should succeed");
        
        // 验证恢复后的状态
        assertEquals(1, sceneManager.getMembers("test-scene-1").size(), "Scene should have 1 member after restoration");
        assertNotNull(sceneManager.getMembers("test-scene-1").get("agent-1"), 
                "Original member should be restored");
        assertNull(sceneManager.getMembers("test-scene-1").get("agent-2"), 
                "Added member should be removed after restoration");
        
        
        // 注意：恢复后的状态将取决于快照时的状态
        // 这里我们只验证成员数量和存在性
    }

    @Test
    public void testIsSceneReady() throws ExecutionException, InterruptedException {
        // 创建场景
        sceneManager.createScene(testScene).get();
        
        // 场景初始状态应为NOT READY（缺少必填角色）
        assertFalse(sceneManager.isSceneReady("test-scene-1"), "Scene should not be ready initially");
        
        // 添加必填角色的成员
        sceneManager.addMember("test-scene-1", testMember1).get();
        
        // 现在场景应该READY（所有必填角色都已满足）
        assertTrue(sceneManager.isSceneReady("test-scene-1"), "Scene should be ready after adding required member");
        
        // 验证场景状态已自动更新为READY
        SceneState sceneState = sceneManager.getSceneState("test-scene-1");
        assertEquals(SceneState.StateType.READY, sceneState.getCurrentState(), "Scene state should be READY after adding required member");
        
        // 移除必填角色的成员
        sceneManager.removeMember("test-scene-1", "agent-1").get();
        
        // 场景应该不再READY
        assertFalse(sceneManager.isSceneReady("test-scene-1"), 
                "Scene should not be ready after removing required member");
        
        
        // 验证场景状态已自动更新为PAUSED
        sceneState = sceneManager.getSceneState("test-scene-1");
        assertEquals(SceneState.StateType.PAUSED, sceneState.getCurrentState(), "Scene state should be PAUSED after removing required member");
    }

    @Test
    public void testDeleteScene() throws ExecutionException, InterruptedException {
        // 创建场景
        sceneManager.createScene(testScene).get();
        sceneManager.addMember("test-scene-1", testMember1).get();
        
        // 测试删除场景
        boolean deleteResult = sceneManager.deleteScene("test-scene-1").get();
        assertTrue(deleteResult, "Deleting scene should succeed");
        
        // 验证场景已删除
        assertNull(sceneManager.getScene("test-scene-1"), 
                "Scene should not be found after deletion");
        assertNull(sceneManager.getSceneState("test-scene-1"), 
                "Scene state should not be found after deletion");
        
    }
}
