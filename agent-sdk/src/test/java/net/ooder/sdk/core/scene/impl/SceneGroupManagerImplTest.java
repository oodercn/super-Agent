package net.ooder.sdk.core.scene.impl;

import net.ooder.sdk.api.scene.SceneGroup;
import net.ooder.sdk.api.scene.SceneMember;
import net.ooder.sdk.common.enums.MemberRole;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SceneGroupManagerImplTest {
    
    private SceneGroupManagerImpl groupManager;
    
    @Before
    public void setUp() {
        groupManager = new SceneGroupManagerImpl();
    }
    
    @Test
    public void testCreate() throws Exception {
        SceneGroup group = groupManager.create("scene-001", null).get(10, TimeUnit.SECONDS);
        
        assertNotNull(group);
        assertNotNull(group.getSceneGroupId());
    }
    
    @Test
    public void testJoin() throws Exception {
        SceneGroup group = groupManager.create("scene-002", null).get(10, TimeUnit.SECONDS);
        
        groupManager.join(group.getSceneGroupId(), "agent-001", MemberRole.BACKUP).get(10, TimeUnit.SECONDS);
        
        List<SceneMember> members = group.getMembers();
        assertNotNull(members);
        assertEquals(1, members.size());
    }
    
    @Test
    public void testLeave() throws Exception {
        SceneGroup group = groupManager.create("scene-003", null).get(10, TimeUnit.SECONDS);
        groupManager.join(group.getSceneGroupId(), "agent-001", MemberRole.BACKUP).get(10, TimeUnit.SECONDS);
        
        groupManager.leave(group.getSceneGroupId(), "agent-001").get(10, TimeUnit.SECONDS);
        
        List<SceneMember> members = group.getMembers();
        assertTrue(members == null || members.isEmpty());
    }
    
    @Test
    public void testChangeRole() throws Exception {
        SceneGroup group = groupManager.create("scene-004", null).get(10, TimeUnit.SECONDS);
        groupManager.join(group.getSceneGroupId(), "agent-001", MemberRole.BACKUP).get(10, TimeUnit.SECONDS);
        
        groupManager.changeRole(group.getSceneGroupId(), "agent-001", MemberRole.PRIMARY).get(10, TimeUnit.SECONDS);
        
        MemberRole role = groupManager.getRole(group.getSceneGroupId(), "agent-001").get(10, TimeUnit.SECONDS);
        assertEquals(MemberRole.PRIMARY, role);
    }
    
    @Test
    public void testGetRole() throws Exception {
        SceneGroup group = groupManager.create("scene-005", null).get(10, TimeUnit.SECONDS);
        groupManager.join(group.getSceneGroupId(), "agent-001", MemberRole.PRIMARY).get(10, TimeUnit.SECONDS);
        
        MemberRole role = groupManager.getRole(group.getSceneGroupId(), "agent-001").get(10, TimeUnit.SECONDS);
        
        assertEquals(MemberRole.PRIMARY, role);
    }
    
    @Test
    public void testGetPrimary() throws Exception {
        SceneGroup group = groupManager.create("scene-006", null).get(10, TimeUnit.SECONDS);
        groupManager.join(group.getSceneGroupId(), "agent-001", MemberRole.PRIMARY).get(10, TimeUnit.SECONDS);
        groupManager.join(group.getSceneGroupId(), "agent-002", MemberRole.BACKUP).get(10, TimeUnit.SECONDS);
        
        SceneMember primary = groupManager.getPrimary(group.getSceneGroupId()).get(10, TimeUnit.SECONDS);
        
        assertNotNull(primary);
        assertEquals("agent-001", primary.getAgentId());
    }
    
    @Test
    public void testGetBackups() throws Exception {
        SceneGroup group = groupManager.create("scene-007", null).get(10, TimeUnit.SECONDS);
        groupManager.join(group.getSceneGroupId(), "agent-001", MemberRole.PRIMARY).get(10, TimeUnit.SECONDS);
        groupManager.join(group.getSceneGroupId(), "agent-002", MemberRole.BACKUP).get(10, TimeUnit.SECONDS);
        groupManager.join(group.getSceneGroupId(), "agent-003", MemberRole.BACKUP).get(10, TimeUnit.SECONDS);
        
        List<SceneMember> backups = groupManager.getBackups(group.getSceneGroupId()).get(10, TimeUnit.SECONDS);
        
        assertEquals(2, backups.size());
    }
    
    @Test
    public void testHandleFailover() throws Exception {
        SceneGroup group = groupManager.create("scene-008", null).get(10, TimeUnit.SECONDS);
        groupManager.join(group.getSceneGroupId(), "agent-001", MemberRole.PRIMARY).get(10, TimeUnit.SECONDS);
        groupManager.join(group.getSceneGroupId(), "agent-002", MemberRole.BACKUP).get(10, TimeUnit.SECONDS);
        
        groupManager.handleFailover(group.getSceneGroupId(), "agent-001").get(10, TimeUnit.SECONDS);
        
        SceneMember newPrimary = groupManager.getPrimary(group.getSceneGroupId()).get(10, TimeUnit.SECONDS);
        assertNotNull(newPrimary);
        assertNotEquals("agent-001", newPrimary.getAgentId());
    }
}
