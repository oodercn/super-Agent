package net.ooder.sdk.agent.scene;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 行政单元测试
 * 测试基于行政区域的场景管理功能
 */
public class AdministrativeUnitTest {
    private SceneManager sceneManager;
    
    // 行政单元相关的场景定义
    private SceneDefinition provinceScene;
    private SceneDefinition cityScene;
    private SceneDefinition districtScene;
    
    // 不同行政单元的成员
    private SceneMember provinceMember;
    private SceneMember cityMember;
    private SceneMember districtMember;

    @BeforeEach
    public void setUp() {
        sceneManager = new SceneManager();
        
        // 创建省级行政单元的场景（包含省级角色）
        MemberRole provinceRole = new MemberRole("province-admin", "省级管理员", 
                Arrays.asList("province-control", "resource-allocation"), true);
        
        provinceScene = new SceneDefinition("province-scene-1", "Province Scene", 
                Arrays.asList(provinceRole), "udp-protocol", "high");
        
        // 创建市级行政单元的场景（包含市级角色）
        MemberRole cityRole = new MemberRole("city-admin", "市级管理员", 
                Arrays.asList("city-control", "local-resource-management"), true);
        
        cityScene = new SceneDefinition("city-scene-1", "City Scene", 
                Arrays.asList(cityRole), "udp-protocol", "medium");
        
        // 创建区级行政单元的场景（包含区级角色）
        MemberRole districtRole = new MemberRole("district-admin", "区级管理员", 
                Arrays.asList("district-control", "local-service-management"), true);
        
        districtScene = new SceneDefinition("district-scene-1", "District Scene", 
                Arrays.asList(districtRole), "udp-protocol", "low");
        
        // 创建不同行政级别的成员
        provinceMember = new SceneMember("province-agent-1", "province-admin", 
                Arrays.asList("province-control", "resource-allocation"),
                Collections.singletonMap("main", "udp://127.0.0.1:9001"));
        
        cityMember = new SceneMember("city-agent-1", "city-admin", 
                Arrays.asList("city-control", "local-resource-management"),
                Collections.singletonMap("main", "udp://127.0.0.1:9002"));
        
        districtMember = new SceneMember("district-agent-1", "district-admin", 
                Arrays.asList("district-control", "local-service-management"),
                Collections.singletonMap("main", "udp://127.0.0.1:9003"));
        
    }

    @Test
    public void testAdministrativeHierarchyScenes() throws ExecutionException, InterruptedException {
        // 创建省级场景
        boolean provinceResult = sceneManager.createScene(provinceScene).get();
        assertTrue(provinceResult, "Province scene creation should succeed");
        
        // 创建市级场景
        boolean cityResult = sceneManager.createScene(cityScene).get();
        assertTrue(cityResult, "City scene creation should succeed");
        
        // 创建区级场景
        boolean districtResult = sceneManager.createScene(districtScene).get();
        assertTrue(districtResult, "District scene creation should succeed");
        
        // 验证所有场景都已创建
        assertNotNull(sceneManager.getScene("province-scene-1"), "Province scene should exist");
        assertNotNull(sceneManager.getScene("city-scene-1"), "City scene should exist");
        assertNotNull(sceneManager.getScene("district-scene-1"), "District scene should exist");
        
        // 验证场景状态
        assertEquals(SceneState.StateType.CREATING, sceneManager.getSceneState("province-scene-1").getCurrentState(), "Province scene initial state should be CREATING");
        assertEquals(SceneState.StateType.CREATING, sceneManager.getSceneState("city-scene-1").getCurrentState(), "City scene initial state should be CREATING");
        assertEquals(SceneState.StateType.CREATING, sceneManager.getSceneState("district-scene-1").getCurrentState(), "District scene initial state should be CREATING");
    }

    @Test
    public void testAdministrativeUnitMembers() throws ExecutionException, InterruptedException {
        // 创建各级行政单元场景
        sceneManager.createScene(provinceScene).get();
        sceneManager.createScene(cityScene).get();
        sceneManager.createScene(districtScene).get();
        
        // 向省级场景添加省级成员
        boolean provinceAddResult = sceneManager.addMember("province-scene-1", provinceMember).get();
        assertTrue(provinceAddResult, "Adding province member should succeed");
        
        // 向市级场景添加市级成员
        boolean cityAddResult = sceneManager.addMember("city-scene-1", cityMember).get();
        assertTrue(cityAddResult, "Adding city member should succeed");
        
        // 向区级场景添加区级成员
        boolean districtAddResult = sceneManager.addMember("district-scene-1", districtMember).get();
        assertTrue(districtAddResult, "Adding district member should succeed");
        
        // 验证成员已添加到正确的行政单元场景
        assertNotNull(sceneManager.getMembers("province-scene-1").get("province-agent-1"), "Province member should be in province scene");
        assertNotNull(sceneManager.getMembers("city-scene-1").get("city-agent-1"), "City member should be in city scene");
        assertNotNull(sceneManager.getMembers("district-scene-1").get("district-agent-1"), "District member should be in district scene");
        
        // 验证成员状态
        SceneState provinceState = sceneManager.getSceneState("province-scene-1");
        SceneState.MemberState provinceMemberState = provinceState.getMemberState("province-agent-1");
        assertEquals(SceneState.MemberState.MemberStateType.JOINING, provinceMemberState.getState(), "Province member initial state should be JOINING");
    }

    @Test
    public void testCrossAdministrativeUnitCommunication() throws ExecutionException, InterruptedException {
        // 创建跨行政单元的协作场景
        MemberRole provinceCoordinator = new MemberRole("province-coordinator", "省级协调员", 
                Arrays.asList("province-control", "cross-unit-communication"), true);
        MemberRole cityCoordinator = new MemberRole("city-coordinator", "市级协调员", 
                Arrays.asList("city-control", "cross-unit-communication"), false);
        MemberRole districtCoordinator = new MemberRole("district-coordinator", "区级协调员", 
                Arrays.asList("district-control", "cross-unit-communication"), false);
        
        SceneDefinition crossUnitScene = new SceneDefinition("cross-unit-scene-1", "Cross Administrative Unit Scene", 
                Arrays.asList(provinceCoordinator, cityCoordinator, districtCoordinator), 
                "cross-unit-protocol", "medium");
        
        // 创建跨行政单元的场景
        boolean createResult = sceneManager.createScene(crossUnitScene).get();
        assertTrue(createResult, "Cross-unit scene creation should succeed");
        
        // 创建跨行政单元的协作成员
        SceneMember crossUnitProvinceMember = new SceneMember("province-agent-1", "province-coordinator", 
                Arrays.asList("province-control", "cross-unit-communication"),
                Collections.singletonMap("main", "udp://127.0.0.1:9001"));
        
        SceneMember crossUnitCityMember = new SceneMember("city-agent-1", "city-coordinator", 
                Arrays.asList("city-control", "cross-unit-communication"),
                Collections.singletonMap("main", "udp://127.0.0.1:9002"));
        
        
        
        // 添加协作成员到跨行政单元场景
        boolean addProvinceResult = sceneManager.addMember("cross-unit-scene-1", crossUnitProvinceMember).get();
        boolean addCityResult = sceneManager.addMember("cross-unit-scene-1", crossUnitCityMember).get();
        
        assertTrue(addProvinceResult, "Adding province member to cross-unit scene should succeed");
        assertTrue(addCityResult, "Adding city member to cross-unit scene should succeed");
        
        // 验证跨行政单元场景的成员组成
        Map<String, SceneMember> members = sceneManager.getMembers("cross-unit-scene-1");
        assertEquals(2, members.size(), "Cross-unit scene should have 2 members");
        assertNotNull(members.get("province-agent-1"), "Province member should be in cross-unit scene");
        assertNotNull(members.get("city-agent-1"), "City member should be in cross-unit scene");
    }

    @Test
    public void testAdministrativeUnitIsolation() throws ExecutionException, InterruptedException {
        // 创建两个不同省级的场景
        SceneDefinition provinceScene1 = new SceneDefinition("province-scene-1", "Province Scene 1", 
                Arrays.asList(new MemberRole("province-admin", "省级管理员", 
                        Arrays.asList("province-control"), true)), "udp-protocol", "high");
        
        SceneDefinition provinceScene2 = new SceneDefinition("province-scene-2", "Province Scene 2", 
                Arrays.asList(new MemberRole("province-admin", "省级管理员", 
                        Arrays.asList("province-control"), true)), "udp-protocol", "high");
        
        // 创建场景
        sceneManager.createScene(provinceScene1).get();
        sceneManager.createScene(provinceScene2).get();
        
        // 创建不同省份的成员
        SceneMember member1 = new SceneMember("agent-1", "province-admin", 
                Arrays.asList("province-control"),
                Collections.singletonMap("main", "udp://127.0.0.1:9001"));
        
        SceneMember member2 = new SceneMember("agent-2", "province-admin", 
                Arrays.asList("province-control"),
                Collections.singletonMap("main", "udp://127.0.0.1:9002"));
        
        
        // 向各自的省级场景添加成员
        sceneManager.addMember("province-scene-1", member1).get();
        sceneManager.addMember("province-scene-2", member2).get();
        
        // 验证成员隔离 - 成员1不应在省份2的场景中
        assertNull(sceneManager.getMembers("province-scene-2").get("agent-1"), "Member 1 should not be in province scene 2");
        
        // 验证成员隔离 - 成员2不应在省份1的场景中
        assertNull(sceneManager.getMembers("province-scene-1").get("agent-2"), "Member 2 should not be in province scene 1");
    }

    @Test
    public void testAdministrativeUnitSnapshots() throws ExecutionException, InterruptedException {
        // 创建行政单元场景
        sceneManager.createScene(provinceScene).get();
        sceneManager.addMember("province-scene-1", provinceMember).get();
        
        // 更新场景状态为运行中
        sceneManager.updateSceneState("province-scene-1", SceneState.StateType.READY, null);
        sceneManager.updateMemberState("province-scene-1", "province-agent-1", 
                SceneState.MemberState.MemberStateType.ACTIVE, null);
        
        // 创建行政单元场景快照
        SceneSnapshot snapshot = sceneManager.createSnapshot("province-scene-1", "Province Scene Snapshot", "system-admin");
        assertNotNull(snapshot, "Snapshot should be created");
        assertEquals(snapshot.getSceneId(), "province-scene-1", "Snapshot scene ID should match");
        
        // 验证快照包含完整的行政单元场景信息
        assertTrue(snapshot.hasSceneDefinition(), "Snapshot should contain scene definition");
        assertTrue(snapshot.hasSceneMembers(), "Snapshot should contain scene members");
        assertTrue(snapshot.hasSceneState(), "Snapshot should contain scene state");
        
        // 恢复快照
        boolean restoreResult = sceneManager.restoreSnapshot("province-scene-1", snapshot.getSnapshotId()).get();
        assertTrue(restoreResult, "Restoring snapshot should succeed");
        
        // 验证恢复后的场景状态
        SceneState restoredState = sceneManager.getSceneState("province-scene-1");
        assertEquals(SceneState.StateType.READY, restoredState.getCurrentState(), "Restored scene state should be READY");
    }
}