package net.ooder.nexus.service.group.impl;

import net.ooder.nexus.domain.group.model.GroupExt;
import net.ooder.nexus.dto.group.GroupCreateDTO;
import net.ooder.nexus.service.group.OrgGroupService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 组织群组服务实现
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@Service("orgGroupServiceImpl")
public class OrgGroupServiceImpl implements OrgGroupService {

    private static final Logger log = LoggerFactory.getLogger(OrgGroupServiceImpl.class);

    private final ConcurrentHashMap<String, GroupExt> groups = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<GroupMember>> groupMembers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> userGroups = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> inviteCodes = new ConcurrentHashMap<>();

    @Override
    public GroupExt createGroup(GroupCreateDTO dto) {
        log.info("Creating group: {}", dto.getName());
        
        GroupExt group = new GroupExt();
        group.setId(UUID.randomUUID().toString());
        group.setName(dto.getName());
        group.setDescription(dto.getDescription());
        group.setOrgId(dto.getOrgId());
        group.setDepartmentId(dto.getDepartmentId());
        group.setGroupType(dto.getGroupType() != null ? dto.getGroupType() : "personal");
        group.setMaxMembers(dto.getMaxMembers());
        group.setOwnerId(dto.getOwnerId());
        group.setOwnerName(dto.getOwnerName());
        group.setMemberCount(1);
        group.setStatus("active");
        group.setCreatedAt(System.currentTimeMillis());
        group.setUpdatedAt(System.currentTimeMillis());

        groups.put(group.getId(), group);
        
        GroupMember owner = new GroupMember();
        owner.setMemberId(dto.getOwnerId());
        owner.setMemberName(dto.getOwnerName());
        owner.setRole("admin");
        owner.setStatus("active");
        owner.setJoinedAt(System.currentTimeMillis());
        
        groupMembers.computeIfAbsent(group.getId(), k -> new ArrayList<>()).add(owner);
        userGroups.put(dto.getOwnerId(), group.getId());
        
        log.info("Group created: {}", group.getId());
        return group;
    }

    @Override
    public GroupExt getGroup(String groupId) {
        return groups.get(groupId);
    }

    @Override
    public List<GroupExt> getGroupsByOrg(String orgId) {
        log.info("Getting groups by org: {}", orgId);
        return groups.values().stream()
                .filter(g -> orgId.equals(g.getOrgId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupExt> getGroupsByDepartment(String departmentId) {
        log.info("Getting groups by department: {}", departmentId);
        return groups.values().stream()
                .filter(g -> departmentId.equals(g.getDepartmentId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupExt> getGroupsByUser(String userId) {
        log.info("Getting groups by user: {}", userId);
        List<GroupExt> result = new ArrayList<>();
        
        for (Map.Entry<String, List<GroupMember>> entry : groupMembers.entrySet()) {
            for (GroupMember member : entry.getValue()) {
                if (userId.equals(member.getMemberId()) && "active".equals(member.getStatus())) {
                    GroupExt group = groups.get(entry.getKey());
                    if (group != null && "active".equals(group.getStatus())) {
                        result.add(group);
                    }
                    break;
                }
            }
        }
        
        return result;
    }

    @Override
    public GroupExt updateGroup(String groupId, String name, String description) {
        log.info("Updating group: {}", groupId);
        
        GroupExt group = groups.get(groupId);
        if (group == null) {
            return null;
        }
        
        if (name != null) {
            group.setName(name);
        }
        if (description != null) {
            group.setDescription(description);
        }
        group.setUpdatedAt(System.currentTimeMillis());
        
        return group;
    }

    @Override
    public boolean deleteGroup(String groupId, String operatorId) {
        log.info("Deleting group: {} by user: {}", groupId, operatorId);
        
        GroupExt group = groups.get(groupId);
        if (group == null) {
            return false;
        }
        
        if (!group.getOwnerId().equals(operatorId)) {
            return false;
        }
        
        group.setStatus("deleted");
        group.setUpdatedAt(System.currentTimeMillis());
        
        return true;
    }

    @Override
    public boolean addMember(String groupId, String userId, String userName, String role) {
        log.info("Adding member to group: {}, user: {}", groupId, userId);
        
        GroupExt group = groups.get(groupId);
        if (group == null) {
            return false;
        }
        
        List<GroupMember> members = groupMembers.computeIfAbsent(groupId, k -> new ArrayList<>());
        
        for (GroupMember m : members) {
            if (userId.equals(m.getMemberId())) {
                m.setStatus("active");
                return true;
            }
        }
        
        GroupMember member = new GroupMember();
        member.setMemberId(userId);
        member.setMemberName(userName);
        member.setRole(role != null ? role : "member");
        member.setStatus("active");
        member.setJoinedAt(System.currentTimeMillis());
        
        members.add(member);
        group.setMemberCount(members.size());
        group.setUpdatedAt(System.currentTimeMillis());
        
        return true;
    }

    @Override
    public boolean removeMember(String groupId, String userId, String operatorId) {
        log.info("Removing member from group: {}, user: {}", groupId, userId);
        
        GroupExt group = groups.get(groupId);
        if (group == null) {
            return false;
        }
        
        boolean isOwner = group.getOwnerId().equals(operatorId);
        boolean isSelf = userId.equals(operatorId);
        
        if (!isOwner && !isSelf) {
            return false;
        }
        
        List<GroupMember> members = groupMembers.get(groupId);
        if (members != null) {
            for (GroupMember m : members) {
                if (userId.equals(m.getMemberId())) {
                    m.setStatus("inactive");
                    group.setMemberCount((int) members.stream().filter(m2 -> "active".equals(m2.getStatus())).count());
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public List<GroupMember> getMembers(String groupId) {
        List<GroupMember> members = groupMembers.get(groupId);
        if (members == null) {
            return new ArrayList<>();
        }
        return members.stream()
                .filter(m -> "active".equals(m.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public String generateInviteCode(String groupId) {
        log.info("Generating invite code for group: {}", groupId);
        
        String code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        inviteCodes.put(code, groupId);
        
        GroupExt group = groups.get(groupId);
        if (group != null) {
            group.setInviteCode(code);
        }
        
        return code;
    }

    @Override
    public GroupExt joinByInviteCode(String inviteCode, String userId, String userName) {
        log.info("Joining group by invite code: {}, user: {}", inviteCode, userId);
        
        String groupId = inviteCodes.get(inviteCode);
        if (groupId == null) {
            return null;
        }
        
        GroupExt group = groups.get(groupId);
        if (group == null || !"active".equals(group.getStatus())) {
            return null;
        }
        
        addMember(groupId, userId, userName, "member");
        
        return group;
    }
}
