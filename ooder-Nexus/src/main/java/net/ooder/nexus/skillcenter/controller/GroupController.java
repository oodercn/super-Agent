package net.ooder.nexus.skillcenter.controller;

import net.ooder.config.ResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.nexus.skillcenter.model.Group;
import net.ooder.nexus.skillcenter.model.GroupMember;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/skillcenter/groups")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class GroupController {

    private final Map<String, Group> groupStore = new ConcurrentHashMap<>();
    private final Map<String, List<GroupMember>> groupMembers = new ConcurrentHashMap<>();

    public GroupController() {
        loadSampleData();
    }

    @PostMapping("/list")
    @ResponseBody
    public ListResultModel<List<Group>> getAllGroups() {
        ListResultModel<List<Group>> result = new ListResultModel<>();
        try {
            List<Group> groups = new ArrayList<>(groupStore.values());
            result.setData(groups);
            result.setSize(groups.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("获取群组列表失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/get")
    @ResponseBody
    public ResultModel<Group> getGroup(@RequestBody Map<String, String> request) {
        ResultModel<Group> result = new ResultModel<>();
        try {
            String groupId = request.get("groupId");
            Group group = groupStore.get(groupId);
            if (group == null) {
                result.setRequestStatus(404);
                result.setMessage("群组不存在");
                return result;
            }
            result.setData(group);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("获取群组详情失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResultModel<Group> createGroup(@RequestBody CreateGroupRequest req) {
        ResultModel<Group> result = new ResultModel<>();
        try {
            if (req.getName() == null || req.getName().isEmpty()) {
                result.setRequestStatus(400);
                result.setMessage("群组名称不能为空");
                return result;
            }

            String groupId = "group-" + UUID.randomUUID().toString().substring(0, 8);
            String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            Group group = new Group();
            group.setId(groupId);
            group.setName(req.getName());
            group.setDescription(req.getDescription());
            group.setCreatedAt(now);
            group.setRole("owner");
            group.setOwnerId("current-user");
            group.setStatus("active");
            group.setMemberCount(1);

            groupStore.put(groupId, group);

            GroupMember owner = new GroupMember();
            owner.setId("member-" + UUID.randomUUID().toString().substring(0, 8));
            owner.setGroupId(groupId);
            owner.setUserId("current-user");
            owner.setUsername("当前用户");
            owner.setRole("admin");
            owner.setJoinedAt(now);
            owner.setStatus("active");

            groupMembers.put(groupId, new ArrayList<>(Collections.singletonList(owner)));

            result.setData(group);
            result.setRequestStatus(200);
            result.setMessage("创建成功");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("创建群组失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public ResultModel<Group> updateGroup(@RequestBody UpdateGroupRequest req) {
        ResultModel<Group> result = new ResultModel<>();
        try {
            String groupId = req.getGroupId();
            Group group = groupStore.get(groupId);
            if (group == null) {
                result.setRequestStatus(404);
                result.setMessage("群组不存在");
                return result;
            }

            if (req.getName() != null) {
                group.setName(req.getName());
            }
            if (req.getDescription() != null) {
                group.setDescription(req.getDescription());
            }

            result.setData(group);
            result.setRequestStatus(200);
            result.setMessage("更新成功");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("更新群组失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteGroup(@RequestBody Map<String, String> request) {
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            String groupId = request.get("groupId");
            Group removed = groupStore.remove(groupId);
            if (removed == null) {
                result.setRequestStatus(404);
                result.setMessage("群组不存在");
                result.setData(false);
                return result;
            }
            groupMembers.remove(groupId);
            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("删除成功");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("删除群组失败: " + e.getMessage());
            result.setData(false);
        }
        return result;
    }

    @PostMapping("/members/list")
    @ResponseBody
    public ListResultModel<List<GroupMember>> getGroupMembers(@RequestBody Map<String, String> request) {
        ListResultModel<List<GroupMember>> result = new ListResultModel<>();
        try {
            String groupId = request.get("groupId");
            List<GroupMember> members = groupMembers.getOrDefault(groupId, new ArrayList<>());
            result.setData(members);
            result.setSize(members.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("获取成员列表失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/members/add")
    @ResponseBody
    public ResultModel<GroupMember> addMember(@RequestBody AddMemberRequest req) {
        ResultModel<GroupMember> result = new ResultModel<>();
        try {
            String groupId = req.getGroupId();
            Group group = groupStore.get(groupId);
            if (group == null) {
                result.setRequestStatus(404);
                result.setMessage("群组不存在");
                return result;
            }

            String memberId = "member-" + UUID.randomUUID().toString().substring(0, 8);
            String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            GroupMember member = new GroupMember();
            member.setId(memberId);
            member.setGroupId(groupId);
            member.setUserId(req.getUserId());
            member.setUsername(req.getUsername());
            member.setRole(req.getRole() != null ? req.getRole() : "member");
            member.setJoinedAt(now);
            member.setStatus("active");

            groupMembers.computeIfAbsent(groupId, k -> new ArrayList<>()).add(member);
            group.setMemberCount(groupMembers.get(groupId).size());

            result.setData(member);
            result.setRequestStatus(200);
            result.setMessage("添加成功");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("添加成员失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/members/remove")
    @ResponseBody
    public ResultModel<Boolean> removeMember(@RequestBody Map<String, String> request) {
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            String groupId = request.get("groupId");
            String memberId = request.get("memberId");
            List<GroupMember> members = groupMembers.get(groupId);
            if (members == null) {
                result.setRequestStatus(404);
                result.setMessage("群组不存在");
                result.setData(false);
                return result;
            }

            boolean removed = members.removeIf(m -> m.getId().equals(memberId));
            if (!removed) {
                result.setRequestStatus(404);
                result.setMessage("成员不存在");
                result.setData(false);
                return result;
            }

            Group group = groupStore.get(groupId);
            if (group != null) {
                group.setMemberCount(members.size());
            }

            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("移除成功");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("移除成员失败: " + e.getMessage());
            result.setData(false);
        }
        return result;
    }

    private void loadSampleData() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Group group1 = new Group();
        group1.setId("group-001");
        group1.setName("开发团队");
        group1.setDescription("Nexus开发团队，共享开发相关技能");
        group1.setCreatedAt(now);
        group1.setRole("owner");
        group1.setOwnerId("user001");
        group1.setStatus("active");
        group1.setMemberCount(5);
        groupStore.put(group1.getId(), group1);

        List<GroupMember> members1 = new ArrayList<>();
        members1.add(createMember("member-001", "group-001", "user001", "张三", "admin", now));
        members1.add(createMember("member-002", "group-001", "user002", "李四", "member", now));
        members1.add(createMember("member-003", "group-001", "user003", "王五", "member", now));
        groupMembers.put(group1.getId(), members1);

        Group group2 = new Group();
        group2.setId("group-002");
        group2.setName("家庭组");
        group2.setDescription("家庭网络管理组");
        group2.setCreatedAt(now);
        group2.setRole("member");
        group2.setOwnerId("user002");
        group2.setStatus("active");
        group2.setMemberCount(4);
        groupStore.put(group2.getId(), group2);

        List<GroupMember> members2 = new ArrayList<>();
        members2.add(createMember("member-004", "group-002", "user002", "李四", "admin", now));
        members2.add(createMember("member-005", "group-002", "user004", "赵六", "member", now));
        groupMembers.put(group2.getId(), members2);

        Group group3 = new Group();
        group3.setId("group-003");
        group3.setName("网络管理组");
        group3.setDescription("OpenWrt网络管理技能分享");
        group3.setCreatedAt(now);
        group3.setRole("member");
        group3.setOwnerId("user003");
        group3.setStatus("active");
        group3.setMemberCount(3);
        groupStore.put(group3.getId(), group3);

        List<GroupMember> members3 = new ArrayList<>();
        members3.add(createMember("member-006", "group-003", "user003", "王五", "admin", now));
        members3.add(createMember("member-007", "group-003", "user005", "钱七", "member", now));
        groupMembers.put(group3.getId(), members3);
    }

    private GroupMember createMember(String id, String groupId, String userId, String username, String role, String joinedAt) {
        GroupMember member = new GroupMember();
        member.setId(id);
        member.setGroupId(groupId);
        member.setUserId(userId);
        member.setUsername(username);
        member.setRole(role);
        member.setJoinedAt(joinedAt);
        member.setStatus("active");
        return member;
    }

    public static class CreateGroupRequest {
        private String name;
        private String description;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class UpdateGroupRequest {
        private String groupId;
        private String name;
        private String description;

        public String getGroupId() { return groupId; }
        public void setGroupId(String groupId) { this.groupId = groupId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class AddMemberRequest {
        private String groupId;
        private String userId;
        private String username;
        private String role;

        public String getGroupId() { return groupId; }
        public void setGroupId(String groupId) { this.groupId = groupId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}
