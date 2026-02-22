package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.AdminGroupService;
import net.ooder.nexus.domain.admin.model.AdminGroup;
import net.ooder.nexus.domain.admin.model.AdminGroupMember;
import net.ooder.nexus.infrastructure.repository.AdminGroupRepository;
import net.ooder.nexus.infrastructure.repository.AdminGroupMemberRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Admin Group Service Implementation with JPA persistence
 *
 * @author ooder Team
 * @version 0.7.0
 */
@Service
public class AdminGroupServiceImpl implements AdminGroupService {

    private static final Logger log = LoggerFactory.getLogger(AdminGroupServiceImpl.class);

    private final AdminGroupRepository groupRepository;
    private final AdminGroupMemberRepository memberRepository;
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Autowired
    public AdminGroupServiceImpl(AdminGroupRepository groupRepository, 
                                   AdminGroupMemberRepository memberRepository) {
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
        initializeDefaultGroups();
        log.info("AdminGroupServiceImpl initialized with JPA persistence");
    }

    private void initializeDefaultGroups() {
        if (groupRepository.count() == 0) {
            createGroup(createGroupData("Development Team", "dev-team", "Development team group"));
            createGroup(createGroupData("Operations Team", "ops-team", "Operations team group"));
            createGroup(createGroupData("Security Team", "security-team", "Security team group"));
            log.info("Default groups initialized");
        }
    }

    private Map<String, Object> createGroupData(String name, String code, String description) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", name);
        data.put("code", code);
        data.put("description", description);
        return data;
    }

    @Override
    public List<Map<String, Object>> getAllGroups() {
        log.info("Getting all groups");
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<AdminGroup> groups = groupRepository.findAll();
        
        for (AdminGroup group : groups) {
            Map<String, Object> groupMap = new HashMap<String, Object>();
            groupMap.put("id", group.getGroupId());
            groupMap.put("name", group.getName());
            groupMap.put("code", group.getCode());
            groupMap.put("description", group.getDescription());
            groupMap.put("status", group.getStatus());
            groupMap.put("memberCount", group.getMemberCount());
            groupMap.put("createTime", group.getCreateTime());
            groupMap.put("updateTime", group.getUpdateTime());
            result.add(groupMap);
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getGroupById(String groupId) {
        log.info("Getting group by id: {}", groupId);
        Optional<AdminGroup> groupOpt = groupRepository.findByGroupId(groupId);
        
        if (!groupOpt.isPresent()) {
            return null;
        }
        
        AdminGroup group = groupOpt.get();
        Map<String, Object> groupMap = new HashMap<String, Object>();
        groupMap.put("id", group.getGroupId());
        groupMap.put("name", group.getName());
        groupMap.put("code", group.getCode());
        groupMap.put("description", group.getDescription());
        groupMap.put("status", group.getStatus());
        groupMap.put("memberCount", group.getMemberCount());
        groupMap.put("createTime", group.getCreateTime());
        groupMap.put("updateTime", group.getUpdateTime());
        
        return groupMap;
    }

    @Override
    @Transactional
    public Map<String, Object> createGroup(Map<String, Object> groupData) {
        log.info("Creating group: {}", groupData.get("name"));
        
        String groupId = "group-" + idGenerator.getAndIncrement();
        
        AdminGroup group = new AdminGroup(groupId, (String) groupData.get("name"));
        group.setCode((String) groupData.get("code"));
        group.setDescription((String) groupData.get("description"));
        
        AdminGroup saved = groupRepository.save(group);
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("id", saved.getGroupId());
        result.put("name", saved.getName());
        result.put("code", saved.getCode());
        result.put("description", saved.getDescription());
        result.put("status", saved.getStatus());
        result.put("memberCount", saved.getMemberCount());
        result.put("createTime", saved.getCreateTime());
        
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> updateGroup(String groupId, Map<String, Object> groupData) {
        log.info("Updating group: {}", groupId);
        
        Optional<AdminGroup> groupOpt = groupRepository.findByGroupId(groupId);
        if (!groupOpt.isPresent()) {
            return null;
        }
        
        AdminGroup group = groupOpt.get();
        
        if (groupData.get("name") != null) {
            group.setName((String) groupData.get("name"));
        }
        if (groupData.get("code") != null) {
            group.setCode((String) groupData.get("code"));
        }
        if (groupData.get("description") != null) {
            group.setDescription((String) groupData.get("description"));
        }
        if (groupData.get("status") != null) {
            group.setStatus((String) groupData.get("status"));
        }
        
        group.setUpdateTime(System.currentTimeMillis());
        
        AdminGroup saved = groupRepository.save(group);
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("id", saved.getGroupId());
        result.put("name", saved.getName());
        result.put("code", saved.getCode());
        result.put("description", saved.getDescription());
        result.put("status", saved.getStatus());
        result.put("memberCount", saved.getMemberCount());
        result.put("updateTime", saved.getUpdateTime());
        
        return result;
    }

    @Override
    @Transactional
    public boolean deleteGroup(String groupId) {
        log.info("Deleting group: {}", groupId);
        
        Optional<AdminGroup> groupOpt = groupRepository.findByGroupId(groupId);
        if (!groupOpt.isPresent()) {
            return false;
        }
        
        memberRepository.deleteByGroupId(groupId);
        groupRepository.deleteByGroupId(groupId);
        
        return true;
    }

    @Override
    public List<Map<String, Object>> getGroupMembers(String groupId) {
        log.info("Getting members for group: {}", groupId);
        
        List<AdminGroupMember> members = memberRepository.findByGroupId(groupId);
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        
        for (AdminGroupMember member : members) {
            Map<String, Object> memberMap = new HashMap<String, Object>();
            memberMap.put("id", member.getId());
            memberMap.put("userId", member.getUserId());
            memberMap.put("userName", member.getUserName());
            memberMap.put("groupId", member.getGroupId());
            memberMap.put("joinTime", member.getJoinTime());
            result.add(memberMap);
        }
        
        return result;
    }

    @Override
    @Transactional
    public boolean addMember(String groupId, String userId) {
        log.info("Adding member {} to group {}", userId, groupId);
        
        Optional<AdminGroup> groupOpt = groupRepository.findByGroupId(groupId);
        if (!groupOpt.isPresent()) {
            return false;
        }
        
        Optional<AdminGroupMember> existing = memberRepository.findByGroupIdAndUserId(groupId, userId);
        if (existing.isPresent()) {
            return false;
        }
        
        AdminGroupMember member = new AdminGroupMember(groupId, userId);
        memberRepository.save(member);
        
        AdminGroup group = groupOpt.get();
        int count = memberRepository.countByGroupId(groupId);
        group.setMemberCount(count);
        group.setUpdateTime(System.currentTimeMillis());
        groupRepository.save(group);
        
        return true;
    }

    @Override
    @Transactional
    public boolean removeMember(String groupId, String userId) {
        log.info("Removing member {} from group {}", userId, groupId);
        
        Optional<AdminGroupMember> memberOpt = memberRepository.findByGroupIdAndUserId(groupId, userId);
        if (!memberOpt.isPresent()) {
            return false;
        }
        
        memberRepository.deleteByGroupIdAndUserId(groupId, userId);
        
        Optional<AdminGroup> groupOpt = groupRepository.findByGroupId(groupId);
        if (groupOpt.isPresent()) {
            AdminGroup group = groupOpt.get();
            int count = memberRepository.countByGroupId(groupId);
            group.setMemberCount(count);
            group.setUpdateTime(System.currentTimeMillis());
            groupRepository.save(group);
        }
        
        return true;
    }

    @Override
    public GroupStatistics getStatistics() {
        log.info("Getting group statistics");
        GroupStatistics stats = new GroupStatistics();
        
        List<AdminGroup> groups = groupRepository.findAll();
        stats.setTotalGroups(groups.size());
        
        int totalMembers = 0;
        int activeGroups = 0;
        int inactiveGroups = 0;
        
        for (AdminGroup group : groups) {
            totalMembers += group.getMemberCount();
            if ("active".equals(group.getStatus())) {
                activeGroups++;
            } else {
                inactiveGroups++;
            }
        }
        
        stats.setTotalMembers(totalMembers);
        stats.setActiveGroups(activeGroups);
        stats.setInactiveGroups(inactiveGroups);
        
        return stats;
    }
}
