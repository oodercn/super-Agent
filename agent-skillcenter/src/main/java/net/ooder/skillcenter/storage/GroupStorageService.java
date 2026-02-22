/*
 * Copyright (c) 2024 Ooder Team
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */
package net.ooder.skillcenter.storage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import net.ooder.skillcenter.model.Group;
import net.ooder.skillcenter.model.GroupMember;
import net.ooder.skillcenter.model.GroupSkill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Group Data Storage Service
 * Uses JSON files to store groups, group skills, and group members
 */
@Service
public class GroupStorageService {
    private static final Logger logger = LoggerFactory.getLogger(GroupStorageService.class);

    private static final String STORAGE_DIR = "skillcenter/storage";
    private static final String GROUPS_FILE = "groups.json";
    private static final String GROUP_SKILLS_FILE = "group_skills.json";
    private static final String GROUP_MEMBERS_FILE = "group_members.json";

    private Path storagePath;
    private List<Group> groupsCache;
    private List<GroupSkill> groupSkillsCache;
    private List<GroupMember> groupMembersCache;

    @PostConstruct
    public void init() {
        this.storagePath = Paths.get(System.getProperty("user.dir"), STORAGE_DIR);
        try {
            Files.createDirectories(storagePath);
            loadData();
            logger.info("[GroupStorageService] Initialized, loaded {} groups, {} group skills",
                    groupsCache.size(), groupSkillsCache.size());
        } catch (IOException e) {
            logger.error("[GroupStorageService] Initialization failed: {}", e.getMessage(), e);
        }
    }

    /**
     * Load data from files
     */
    private void loadData() throws IOException {
        // Load groups data
        Path groupsFile = storagePath.resolve(GROUPS_FILE);
        if (Files.exists(groupsFile)) {
            String content = new String(Files.readAllBytes(groupsFile));
            groupsCache = JSON.parseObject(content, new TypeReference<List<Group>>() {});
            if (groupsCache == null) {
                groupsCache = new ArrayList<>();
                initDefaultGroups();
            }
        } else {
            groupsCache = new ArrayList<>();
            initDefaultGroups();
        }

        // Load group skills data
        Path groupSkillsFile = storagePath.resolve(GROUP_SKILLS_FILE);
        if (Files.exists(groupSkillsFile)) {
            String content = new String(Files.readAllBytes(groupSkillsFile));
            groupSkillsCache = JSON.parseObject(content, new TypeReference<List<GroupSkill>>() {});
            if (groupSkillsCache == null) {
                groupSkillsCache = new ArrayList<>();
                initDefaultGroupSkills();
            }
        } else {
            groupSkillsCache = new ArrayList<>();
            initDefaultGroupSkills();
        }

        // Load group members data
        Path groupMembersFile = storagePath.resolve(GROUP_MEMBERS_FILE);
        if (Files.exists(groupMembersFile)) {
            String content = new String(Files.readAllBytes(groupMembersFile));
            groupMembersCache = JSON.parseObject(content, new TypeReference<List<GroupMember>>() {});
            if (groupMembersCache == null) {
                groupMembersCache = new ArrayList<>();
                initDefaultGroupMembers();
            }
        } else {
            groupMembersCache = new ArrayList<>();
            initDefaultGroupMembers();
        }
    }

    /**
     * Save data to files
     */
    private void saveData() throws IOException {
        Path groupsFile = storagePath.resolve(GROUPS_FILE);
        String groupsJson = JSON.toJSONString(groupsCache, true);
        Files.write(groupsFile, groupsJson.getBytes());

        Path groupSkillsFile = storagePath.resolve(GROUP_SKILLS_FILE);
        String groupSkillsJson = JSON.toJSONString(groupSkillsCache, true);
        Files.write(groupSkillsFile, groupSkillsJson.getBytes());

        Path groupMembersFile = storagePath.resolve(GROUP_MEMBERS_FILE);
        String groupMembersJson = JSON.toJSONString(groupMembersCache, true);
        Files.write(groupMembersFile, groupMembersJson.getBytes());
    }

    /**
     * Initialize default groups data
     */
    private void initDefaultGroups() {
        groupsCache.add(new Group("dev-team", "Development Team",
                "Company development team responsible for product technical development", 15, "2026-01-01", "Member"));
        groupsCache.add(new Group("marketing-team", "Marketing Team",
                "Company marketing team responsible for product promotion", 8, "2026-01-05", "Member"));
        groupsCache.add(new Group("hr-team", "HR Team",
                "Company HR team responsible for personnel management", 5, "2026-01-10", "Admin"));

        try {
            saveData();
        } catch (IOException e) {
            logger.error("[GroupStorageService] Failed to save default groups data: {}", e.getMessage());
        }
    }

    /**
     * Initialize default group skills data
     */
    private void initDefaultGroupSkills() {
        groupSkillsCache.add(new GroupSkill("group-skill-001", "dev-team", "Development Team",
                "code-generator", "Code Generation", "Zhang San", "2026-01-20", "Used to generate various code templates"));
        groupSkillsCache.add(new GroupSkill("group-skill-002", "dev-team", "Development Team",
                "json-formatter", "JSON Formatter", "Li Si", "2026-01-22", "Format JSON data"));
        groupSkillsCache.add(new GroupSkill("group-skill-003", "marketing-team", "Marketing Team",
                "image-resizer", "Image Resizer", "Wang Wu", "2026-01-25", "Resize images"));

        try {
            saveData();
        } catch (IOException e) {
            logger.error("[GroupStorageService] Failed to save default group skills data: {}", e.getMessage());
        }
    }

    // ==================== Group Operations ====================

    public List<Group> getAllGroups() {
        return new ArrayList<>(groupsCache);
    }

    public Group getGroupById(String id) {
        return groupsCache.stream()
                .filter(g -> g.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Group addGroup(Group group) {
        groupsCache.add(group);
        try {
            saveData();
        } catch (IOException e) {
            logger.error("[GroupStorageService] Failed to save groups data: {}", e.getMessage());
        }
        return group;
    }

    public Group updateGroup(Group group) {
        for (int i = 0; i < groupsCache.size(); i++) {
            if (groupsCache.get(i).getId().equals(group.getId())) {
                groupsCache.set(i, group);
                try {
                    saveData();
                } catch (IOException e) {
                    logger.error("[GroupStorageService] Failed to save groups data: {}", e.getMessage());
                }
                return group;
            }
        }
        return null;
    }

    public boolean deleteGroup(String id) {
        boolean removed = groupsCache.removeIf(g -> g.getId().equals(id));
        if (removed) {
            try {
                saveData();
            } catch (IOException e) {
                logger.error("[GroupStorageService] Failed to save groups data: {}", e.getMessage());
            }
        }
        return removed;
    }

    // ==================== Group Skill Operations ====================

    public List<GroupSkill> getAllGroupSkills() {
        return new ArrayList<>(groupSkillsCache);
    }

    public List<GroupSkill> getGroupSkillsByGroupId(String groupId) {
        return groupSkillsCache.stream()
                .filter(s -> s.getGroupId().equals(groupId))
                .collect(Collectors.toList());
    }

    public GroupSkill getGroupSkillById(String id) {
        return groupSkillsCache.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public GroupSkill addGroupSkill(GroupSkill groupSkill) {
        groupSkillsCache.add(groupSkill);
        try {
            saveData();
        } catch (IOException e) {
            logger.error("[GroupStorageService] Failed to save group skills data: {}", e.getMessage());
        }
        return groupSkill;
    }

    public GroupSkill updateGroupSkill(GroupSkill groupSkill) {
        for (int i = 0; i < groupSkillsCache.size(); i++) {
            if (groupSkillsCache.get(i).getId().equals(groupSkill.getId())) {
                groupSkillsCache.set(i, groupSkill);
                try {
                    saveData();
                } catch (IOException e) {
                    logger.error("[GroupStorageService] Failed to save group skills data: {}", e.getMessage());
                }
                return groupSkill;
            }
        }
        return null;
    }

    public boolean deleteGroupSkill(String id) {
        boolean removed = groupSkillsCache.removeIf(s -> s.getId().equals(id));
        if (removed) {
            try {
                saveData();
            } catch (IOException e) {
                logger.error("[GroupStorageService] Failed to save group skills data: {}", e.getMessage());
            }
        }
        return removed;
    }

    /**
     * Initialize default group members data
     */
    private void initDefaultGroupMembers() {
        groupMembersCache.add(new GroupMember("member-001", "dev-team", "user-001", "Zhang San", "admin", "2026-01-01"));
        groupMembersCache.add(new GroupMember("member-002", "dev-team", "user-002", "Li Si", "member", "2026-01-05"));
        groupMembersCache.add(new GroupMember("member-003", "dev-team", "user-003", "Wang Wu", "member", "2026-01-10"));
        groupMembersCache.add(new GroupMember("member-004", "marketing-team", "user-004", "Zhao Liu", "admin", "2026-01-02"));
        groupMembersCache.add(new GroupMember("member-005", "hr-team", "user-005", "Qian Qi", "admin", "2026-01-03"));

        try {
            saveData();
        } catch (IOException e) {
            logger.error("[GroupStorageService] Failed to save default group members data: {}", e.getMessage());
        }
    }

    // ==================== Group Member Operations ====================

    public List<GroupMember> getAllGroupMembers() {
        return new ArrayList<>(groupMembersCache);
    }

    public List<GroupMember> getGroupMembersByGroupId(String groupId) {
        return groupMembersCache.stream()
                .filter(m -> m.getGroupId().equals(groupId))
                .collect(Collectors.toList());
    }

    public GroupMember getGroupMemberById(String id) {
        return groupMembersCache.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public GroupMember addGroupMember(GroupMember member) {
        groupMembersCache.add(member);
        Group group = getGroupById(member.getGroupId());
        if (group != null) {
            group.setMemberCount(group.getMemberCount() + 1);
            updateGroup(group);
        }
        try {
            saveData();
        } catch (IOException e) {
            logger.error("[GroupStorageService] Failed to save group members data: {}", e.getMessage());
        }
        return member;
    }

    public GroupMember updateGroupMember(GroupMember member) {
        for (int i = 0; i < groupMembersCache.size(); i++) {
            if (groupMembersCache.get(i).getId().equals(member.getId())) {
                groupMembersCache.set(i, member);
                try {
                    saveData();
                } catch (IOException e) {
                    logger.error("[GroupStorageService] Failed to save group members data: {}", e.getMessage());
                }
                return member;
            }
        }
        return null;
    }

    public boolean deleteGroupMember(String id) {
        GroupMember member = getGroupMemberById(id);
        if (member != null) {
            boolean removed = groupMembersCache.removeIf(m -> m.getId().equals(id));
            if (removed) {
                Group group = getGroupById(member.getGroupId());
                if (group != null) {
                    group.setMemberCount(Math.max(0, group.getMemberCount() - 1));
                    updateGroup(group);
                }
                try {
                    saveData();
                } catch (IOException e) {
                    logger.error("[GroupStorageService] Failed to save group members data: {}", e.getMessage());
                }
            }
            return removed;
        }
        return false;
    }
}
