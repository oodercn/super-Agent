package net.ooder.nexus.controller.skillcenter;

import net.ooder.nexus.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/skillcenter/personal")
public class SkillCenterPersonalController {

    private final Map<String, Skill> skillMap;
    private final List<ExecutionHistory> executionHistory;
    private final Map<String, ShareRecord> shareMap;
    private final Map<String, Group> groupMap;

    public SkillCenterPersonalController() {
        this.skillMap = new HashMap<>();
        this.executionHistory = new ArrayList<>();
        this.shareMap = new HashMap<>();
        this.groupMap = new HashMap<>();
        initializeData();
    }

    private void initializeData() {
        Skill skill1 = new Skill();
        skill1.setId("skill-001");
        skill1.setName("文本分析");
        skill1.setDescription("分析文本内容");
        skill1.setCategory("text-processing");
        skill1.setCreatedAt(LocalDateTime.now());
        skillMap.put(skill1.getId(), skill1);

        Skill skill2 = new Skill();
        skill2.setId("skill-002");
        skill2.setName("代码生成");
        skill2.setDescription("生成代码片段");
        skill2.setCategory("development");
        skill2.setCreatedAt(LocalDateTime.now());
        skillMap.put(skill2.getId(), skill2);

        ExecutionHistory history1 = new ExecutionHistory();
        history1.setId("exec-001");
        history1.setSkillId("skill-001");
        history1.setSkillName("文本分析");
        history1.setStatus("success");
        history1.setTimestamp(LocalDateTime.now().minusHours(1));
        executionHistory.add(history1);

        Group group1 = new Group();
        group1.setId("group-001");
        group1.setName("开发团队");
        group1.setDescription("技能开发团队");
        group1.setType("private");
        group1.setCreatedAt(LocalDateTime.now());
        groupMap.put(group1.getId(), group1);
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSkills", skillMap.size());
        stats.put("totalExecutions", executionHistory.size());
        stats.put("sharedSkills", shareMap.size());
        stats.put("myGroups", groupMap);
        stats.put("recentActivities", getRecentActivities());
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<List<Skill>>> getSkills() {
        return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(skillMap.values())));
    }

    @PostMapping("/skills")
    public ResponseEntity<ApiResponse<Skill>> publishSkill(@RequestBody Skill skill) {
        skill.setId("skill-" + System.currentTimeMillis());
        skill.setCreatedAt(LocalDateTime.now());
        skillMap.put(skill.getId(), skill);
        return ResponseEntity.ok(ApiResponse.success(skill));
    }

    @DeleteMapping("/skills/{skillId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteSkill(@PathVariable String skillId) {
        boolean result = skillMap.remove(skillId) != null;
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/execution/history")
    public ResponseEntity<ApiResponse<List<ExecutionHistory>>> getExecutionHistory() {
        return ResponseEntity.ok(ApiResponse.success(executionHistory));
    }

    @PostMapping("/execution/execute/{skillId}")
    public ResponseEntity<ApiResponse<ExecutionResult>> executeSkill(@PathVariable String skillId, @RequestBody Map<String, Object> parameters) {
        Skill skill = skillMap.get(skillId);
        if (skill == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Skill not found"));
        }

        ExecutionHistory history = new ExecutionHistory();
        history.setId("exec-" + System.currentTimeMillis());
        history.setSkillId(skillId);
        history.setSkillName(skill.getName());
        history.setStatus("success");
        history.setTimestamp(LocalDateTime.now());
        executionHistory.add(history);

        ExecutionResult result = new ExecutionResult();
        result.setExecutionId(history.getId());
        result.setStatus("success");
        result.setOutput("Execution completed successfully");
        result.setTimestamp(LocalDateTime.now());

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/execution/result/{executionId}")
    public ResponseEntity<ApiResponse<ExecutionResult>> getExecutionResult(@PathVariable String executionId) {
        ExecutionHistory history = executionHistory.stream()
                .filter(h -> h.getId().equals(executionId))
                .findFirst()
                .orElse(null);

        if (history == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Execution not found"));
        }

        ExecutionResult result = new ExecutionResult();
        result.setExecutionId(executionId);
        result.setStatus(history.getStatus());
        result.setOutput("Execution result");
        result.setTimestamp(history.getTimestamp());

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/sharing/shared")
    public ResponseEntity<ApiResponse<List<ShareRecord>>> getSharedSkills() {
        return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(shareMap.values())));
    }

    @GetMapping("/sharing/received")
    public ResponseEntity<ApiResponse<List<ShareRecord>>> getReceivedSkills() {
        List<ShareRecord> received = new ArrayList<>();
        ShareRecord record = new ShareRecord();
        record.setId("share-001");
        record.setSkillName("图片处理");
        record.setSharedBy("张三");
        record.setReceivedAt(LocalDateTime.now().minusDays(1));
        received.add(record);
        return ResponseEntity.ok(ApiResponse.success(received));
    }

    @PostMapping("/sharing")
    public ResponseEntity<ApiResponse<ShareRecord>> shareSkill(@RequestBody Map<String, Object> request) {
        ShareRecord record = new ShareRecord();
        record.setId("share-" + System.currentTimeMillis());
        record.setSkillId((String) request.get("skillId"));
        record.setTarget((String) request.get("target"));
        record.setTargetType((String) request.get("targetType"));
        record.setSharedAt(LocalDateTime.now());
        shareMap.put(record.getId(), record);
        return ResponseEntity.ok(ApiResponse.success(record));
    }

    @DeleteMapping("/sharing/{shareId}")
    public ResponseEntity<ApiResponse<Boolean>> cancelShare(@PathVariable String shareId) {
        boolean result = shareMap.remove(shareId) != null;
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/sharing/{shareId}")
    public ResponseEntity<ApiResponse<Boolean>> acceptShare(@PathVariable String shareId) {
        ShareRecord record = shareMap.get(shareId);
        if (record == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Share not found"));
        }
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @GetMapping("/groups")
    public ResponseEntity<ApiResponse<List<Group>>> getGroups() {
        return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(groupMap.values())));
    }

    @PostMapping("/groups")
    public ResponseEntity<ApiResponse<Group>> createGroup(@RequestBody Map<String, Object> request) {
        Group group = new Group();
        group.setId("group-" + System.currentTimeMillis());
        group.setName((String) request.get("name"));
        group.setDescription((String) request.get("description"));
        group.setType("private");
        group.setCreatedAt(LocalDateTime.now());
        groupMap.put(group.getId(), group);
        return ResponseEntity.ok(ApiResponse.success(group));
    }

    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteGroup(@PathVariable String groupId) {
        boolean result = groupMap.remove(groupId) != null;
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<Group>> getGroup(@PathVariable String groupId) {
        Group group = groupMap.get(groupId);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Group not found"));
        }
        return ResponseEntity.ok(ApiResponse.success(group));
    }

    @PostMapping("/groups/{groupId}/members")
    public ResponseEntity<ApiResponse<Boolean>> addMember(@PathVariable String groupId, @RequestBody Map<String, Object> request) {
        Group group = groupMap.get(groupId);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Group not found"));
        }
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @DeleteMapping("/groups/{groupId}/members/{memberId}")
    public ResponseEntity<ApiResponse<Boolean>> removeMember(@PathVariable String groupId, @PathVariable String memberId) {
        Group group = groupMap.get(groupId);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Group not found"));
        }
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    private List<Activity> getRecentActivities() {
        List<Activity> activities = new ArrayList<>();
        Activity activity1 = new Activity();
        activity1.setType("execution");
        activity1.setDescription("执行了文本分析技能");
        activity1.setTimestamp(LocalDateTime.now().minusHours(2));
        activities.add(activity1);
        return activities;
    }

    public static class Skill {
        private String id;
        private String name;
        private String description;
        private String category;
        private LocalDateTime createdAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }

    public static class ExecutionHistory {
        private String id;
        private String skillId;
        private String skillName;
        private String status;
        private LocalDateTime timestamp;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public String getSkillName() {
            return skillName;
        }

        public void setSkillName(String skillName) {
            this.skillName = skillName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class ExecutionResult {
        private String executionId;
        private String status;
        private String output;
        private LocalDateTime timestamp;

        public String getExecutionId() {
            return executionId;
        }

        public void setExecutionId(String executionId) {
            this.executionId = executionId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOutput() {
            return output;
        }

        public void setOutput(String output) {
            this.output = output;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class ShareRecord {
        private String id;
        private String skillId;
        private String skillName;
        private String target;
        private String targetType;
        private String sharedBy;
        private LocalDateTime sharedAt;
        private LocalDateTime receivedAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public String getSkillName() {
            return skillName;
        }

        public void setSkillName(String skillName) {
            this.skillName = skillName;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getTargetType() {
            return targetType;
        }

        public void setTargetType(String targetType) {
            this.targetType = targetType;
        }

        public String getSharedBy() {
            return sharedBy;
        }

        public void setSharedBy(String sharedBy) {
            this.sharedBy = sharedBy;
        }

        public LocalDateTime getSharedAt() {
            return sharedAt;
        }

        public void setSharedAt(LocalDateTime sharedAt) {
            this.sharedAt = sharedAt;
        }

        public LocalDateTime getReceivedAt() {
            return receivedAt;
        }

        public void setReceivedAt(LocalDateTime receivedAt) {
            this.receivedAt = receivedAt;
        }
    }

    public static class Group {
        private String id;
        private String name;
        private String description;
        private String type;
        private LocalDateTime createdAt;
        private List<Member> members;

        public Group() {
            this.members = new ArrayList<>();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public List<Member> getMembers() {
            return members;
        }

        public void setMembers(List<Member> members) {
            this.members = members;
        }
    }

    public static class Member {
        private String id;
        private String name;
        private String role;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    public static class Activity {
        private String type;
        private String description;
        private LocalDateTime timestamp;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }
}
