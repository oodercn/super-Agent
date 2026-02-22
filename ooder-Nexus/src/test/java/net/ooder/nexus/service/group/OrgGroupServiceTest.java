package net.ooder.nexus.service.group;

import net.ooder.nexus.domain.group.model.GroupExt;
import net.ooder.nexus.dto.group.GroupCreateDTO;
import net.ooder.nexus.service.group.impl.OrgGroupServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 组织群组服务测试
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
class OrgGroupServiceTest {

    private OrgGroupService orgGroupService;

    @BeforeEach
    void setUp() {
        orgGroupService = new OrgGroupServiceImpl();
    }

    @Test
    void testCreateGroup() {
        GroupCreateDTO dto = new GroupCreateDTO();
        dto.setName("Test Group");
        dto.setDescription("Test group description");
        dto.setOrgId("org-001");
        dto.setDepartmentId("dept-001");
        dto.setGroupType("org");
        dto.setOwnerId("user-001");
        dto.setOwnerName("Test Owner");

        GroupExt group = orgGroupService.createGroup(dto);

        assertNotNull(group);
        assertNotNull(group.getId());
        assertEquals("Test Group", group.getName());
        assertEquals("Test group description", group.getDescription());
        assertEquals("org-001", group.getOrgId());
        assertEquals("dept-001", group.getDepartmentId());
        assertEquals("org", group.getGroupType());
        assertEquals("user-001", group.getOwnerId());
        assertEquals("active", group.getStatus());
        assertEquals(1, group.getMemberCount());
    }

    @Test
    void testGetGroup() {
        GroupCreateDTO dto = new GroupCreateDTO();
        dto.setName("Get Test Group");
        dto.setOwnerId("user-001");
        GroupExt createdGroup = orgGroupService.createGroup(dto);

        GroupExt group = orgGroupService.getGroup(createdGroup.getId());

        assertNotNull(group);
        assertEquals(createdGroup.getId(), group.getId());
        assertEquals("Get Test Group", group.getName());
    }

    @Test
    void testGetGroupsByOrg() {
        GroupCreateDTO dto = new GroupCreateDTO();
        dto.setName("Org Group 1");
        dto.setOrgId("org-002");
        dto.setOwnerId("user-001");
        orgGroupService.createGroup(dto);

        dto.setName("Org Group 2");
        orgGroupService.createGroup(dto);

        List<GroupExt> groups = orgGroupService.getGroupsByOrg("org-002");

        assertNotNull(groups);
        assertEquals(2, groups.size());
    }

    @Test
    void testGetGroupsByDepartment() {
        GroupCreateDTO dto = new GroupCreateDTO();
        dto.setName("Dept Group 1");
        dto.setDepartmentId("dept-003");
        dto.setOwnerId("user-001");
        orgGroupService.createGroup(dto);

        dto.setName("Dept Group 2");
        orgGroupService.createGroup(dto);

        List<GroupExt> groups = orgGroupService.getGroupsByDepartment("dept-003");

        assertNotNull(groups);
        assertEquals(2, groups.size());
    }

    @Test
    void testGetGroupsByUser() {
        GroupCreateDTO dto = new GroupCreateDTO();
        dto.setName("User Group 1");
        dto.setOwnerId("user-001");
        orgGroupService.createGroup(dto);

        dto.setName("User Group 2");
        orgGroupService.createGroup(dto);

        List<GroupExt> groups = orgGroupService.getGroupsByUser("user-001");

        assertNotNull(groups);
        assertEquals(2, groups.size());
    }

    @Test
    void testUpdateGroup() {
        GroupCreateDTO dto = new GroupCreateDTO();
        dto.setName("Original Name");
        dto.setDescription("Original description");
        dto.setOwnerId("user-001");
        GroupExt createdGroup = orgGroupService.createGroup(dto);

        GroupExt updatedGroup = orgGroupService.updateGroup(
                createdGroup.getId(), 
                "Updated Name", 
                "Updated description"
        );

        assertNotNull(updatedGroup);
        assertEquals("Updated Name", updatedGroup.getName());
        assertEquals("Updated description", updatedGroup.getDescription());
    }

    @Test
    void testDeleteGroup() {
        GroupCreateDTO dto = new GroupCreateDTO();
        dto.setName("Group to Delete");
        dto.setOwnerId("user-001");
        GroupExt createdGroup = orgGroupService.createGroup(dto);

        boolean result = orgGroupService.deleteGroup(createdGroup.getId(), "user-001");

        assertTrue(result);

        GroupExt deletedGroup = orgGroupService.getGroup(createdGroup.getId());
        assertEquals("deleted", deletedGroup.getStatus());
    }

    @Test
    void testDeleteGroupByOtherUser() {
        GroupCreateDTO dto = new GroupCreateDTO();
        dto.setName("Group owned by user-001");
        dto.setOwnerId("user-001");
        GroupExt createdGroup = orgGroupService.createGroup(dto);

        boolean result = orgGroupService.deleteGroup(createdGroup.getId(), "user-002");

        assertFalse(result);
    }

    @Test
    void testAddMember() {
        GroupCreateDTO dto = new GroupCreateDTO();
        dto.setName("Group for member test");
        dto.setOwnerId("user-001");
        GroupExt createdGroup = orgGroupService.createGroup(dto);

        boolean result = orgGroupService.addMember(
                createdGroup.getId(), 
                "user-002", 
                "New Member", 
                "member"
        );

        assertTrue(result);

        List<OrgGroupService.GroupMember> members = orgGroupService.getMembers(createdGroup.getId());
        assertEquals(2, members.size());
    }

    @Test
    void testRemoveMember() {
        GroupCreateDTO dto = new GroupCreateDTO();
        dto.setName("Group for remove test");
        dto.setOwnerId("user-001");
        GroupExt createdGroup = orgGroupService.createGroup(dto);

        orgGroupService.addMember(createdGroup.getId(), "user-002", "Member to remove", "member");

        boolean result = orgGroupService.removeMember(createdGroup.getId(), "user-002", "user-001");

        assertTrue(result);
    }

    @Test
    void testGetMembers() {
        GroupCreateDTO dto = new GroupCreateDTO();
        dto.setName("Group for members list");
        dto.setOwnerName("Owner Name");
        dto.setOwnerId("user-001");
        GroupExt createdGroup = orgGroupService.createGroup(dto);

        orgGroupService.addMember(createdGroup.getId(), "user-002", "Member 1", "member");
        orgGroupService.addMember(createdGroup.getId(), "user-003", "Member 2", "member");

        List<OrgGroupService.GroupMember> members = orgGroupService.getMembers(createdGroup.getId());

        assertNotNull(members);
        assertEquals(3, members.size());
    }

    @Test
    void testGenerateInviteCode() {
        GroupCreateDTO dto = new GroupCreateDTO();
        dto.setName("Group for invite code");
        dto.setOwnerId("user-001");
        GroupExt createdGroup = orgGroupService.createGroup(dto);

        String inviteCode = orgGroupService.generateInviteCode(createdGroup.getId());

        assertNotNull(inviteCode);
        assertEquals(8, inviteCode.length());

        GroupExt group = orgGroupService.getGroup(createdGroup.getId());
        assertEquals(inviteCode, group.getInviteCode());
    }

    @Test
    void testJoinByInviteCode() {
        GroupCreateDTO dto = new GroupCreateDTO();
        dto.setName("Group for join test");
        dto.setOwnerId("user-001");
        GroupExt createdGroup = orgGroupService.createGroup(dto);

        String inviteCode = orgGroupService.generateInviteCode(createdGroup.getId());

        GroupExt joinedGroup = orgGroupService.joinByInviteCode(inviteCode, "user-002", "New User");

        assertNotNull(joinedGroup);
        assertEquals(createdGroup.getId(), joinedGroup.getId());

        List<OrgGroupService.GroupMember> members = orgGroupService.getMembers(createdGroup.getId());
        assertEquals(2, members.size());
    }

    @Test
    void testJoinByInvalidInviteCode() {
        GroupExt joinedGroup = orgGroupService.joinByInviteCode("INVALID", "user-002", "New User");

        assertNull(joinedGroup);
    }
}
