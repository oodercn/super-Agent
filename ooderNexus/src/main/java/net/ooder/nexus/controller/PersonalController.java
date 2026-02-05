package net.ooder.nexus.controller;

import net.ooder.nexus.manager.SkillManager;
import net.ooder.nexus.model.ApiResponse;
import net.ooder.nexus.model.Skill;
import net.ooder.nexus.model.SkillContext;
import net.ooder.nexus.model.SkillException;
import net.ooder.nexus.model.SkillResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/personal")
public class PersonalController {

    private final SkillManager skillManager;
    private final Map<String, SkillResult> executionResults;

    public PersonalController() {
        this.skillManager = SkillManager.getInstance();
        this.executionResults = new ConcurrentHashMap<>();
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPersonalDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        try {
            int totalSkills = skillManager.getAllSkills().size();
            stats.put("totalSkills", totalSkills);

            stats.put("totalExecutions", 156);
            stats.put("successfulExecutions", 142);
            stats.put("failedExecutions", 14);
            stats.put("successRate", 91.0);

            stats.put("sharedSkills", 8);
            stats.put("receivedSkills", 12);

            List<Map<String, Object>> recentActivities = new ArrayList<>();
            Map<String, Object> activity1 = new HashMap<>();
            activity1.put("id", "1");
            activity1.put("type", "execution");
            activity1.put("skillName", "Text to Uppercase");
            activity1.put("status", "success");
            activity1.put("timestamp", "2026-01-31 10:30:00");
            recentActivities.add(activity1);

            Map<String, Object> activity2 = new HashMap<>();
            activity2.put("id", "2");
            activity2.put("type", "publish");
            activity2.put("skillName", "Code Generator");
            activity2.put("status", "success");
            activity2.put("timestamp", "2026-01-31 09:15:00");
            recentActivities.add(activity2);

            stats.put("recentActivities", recentActivities);

            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取个人仪表盘统计数据失败: " + e.getMessage()));
        }
    }

    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<List<Skill>>> getPersonalSkills() {
        List<Skill> skills = skillManager.getAllSkills();
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    @PostMapping("/skills")
    public ResponseEntity<ApiResponse<Boolean>> publishPersonalSkill(@RequestBody Skill skill) {
        try {
            skillManager.registerSkill(skill);
            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "发布技能失败: " + e.getMessage()));
        }
    }

    @PutMapping("/skills/{id}")
    public ResponseEntity<ApiResponse<Boolean>> updatePersonalSkill(@PathVariable String id, @RequestBody Skill skill) {
        try {
            skillManager.unregisterSkill(id);
            skillManager.registerSkill(skill);
            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "更新技能失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deletePersonalSkill(@PathVariable String id) {
        try {
            skillManager.unregisterSkill(id);
            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "删除技能失败: " + e.getMessage()));
        }
    }

    @PostMapping("/execution/execute/{skillId}")
    public ResponseEntity<ApiResponse<SkillResult>> executePersonalSkill(@PathVariable String skillId, @RequestBody Map<String, Object> parameters) {
        String executionId = UUID.randomUUID().toString();

        try {
            SkillContext context = new SkillContext();
            if (parameters != null) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    context.addParameter(entry.getKey(), entry.getValue());
                }
            }

            SkillResult result = skillManager.executeSkill(skillId, context);
            executionResults.put(executionId, result);

            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (SkillException e) {
            SkillResult result = new SkillResult(SkillResult.Status.FAILED, e.getMessage());
            result.setException(e);
            executionResults.put(executionId, result);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "执行技能失败: " + e.getMessage()));
        }
    }

    @GetMapping("/execution/history")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getExecutionHistory() {
        List<Map<String, Object>> history = new ArrayList<>();

        Map<String, Object> exec1 = new HashMap<>();
        exec1.put("id", "1");
        exec1.put("skillName", "Text to Uppercase");
        exec1.put("status", "success");
        exec1.put("timestamp", "2026-01-31 10:30:00");
        exec1.put("executionTime", "1.2s");
        history.add(exec1);

        Map<String, Object> exec2 = new HashMap<>();
        exec2.put("id", "2");
        exec2.put("skillName", "Code Generator");
        exec2.put("status", "success");
        exec2.put("timestamp", "2026-01-31 09:15:00");
        exec2.put("executionTime", "3.5s");
        history.add(exec2);

        Map<String, Object> exec3 = new HashMap<>();
        exec3.put("id", "3");
        exec3.put("skillName", "Local Deployment");
        exec3.put("status", "failed");
        exec3.put("timestamp", "2026-01-30 16:45:00");
        exec3.put("executionTime", "2.1s");
        history.add(exec3);

        return ResponseEntity.ok(ApiResponse.success(history));
    }

    @GetMapping("/execution/result/{executionId}")
    public ResponseEntity<ApiResponse<SkillResult>> getPersonalExecutionResult(@PathVariable String executionId) {
        SkillResult result = executionResults.get(executionId);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "执行结果不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/sharing/shared")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getSharedSkills() {
        List<Map<String, Object>> sharedSkills = new ArrayList<>();

        Map<String, Object> skill1 = new HashMap<>();
        skill1.put("id", "1");
        skill1.put("skillId", "text-to-uppercase-skill");
        skill1.put("skillName", "Text to Uppercase");
        skill1.put("sharedWith", "user123");
        skill1.put("sharedAt", "2026-01-31 10:00:00");
        skill1.put("status", "active");
        sharedSkills.add(skill1);

        Map<String, Object> skill2 = new HashMap<>();
        skill2.put("id", "2");
        skill2.put("skillId", "code-generation-skill");
        skill2.put("skillName", "Code Generator");
        skill2.put("sharedWith", "group456");
        skill2.put("sharedAt", "2026-01-30 15:30:00");
        skill2.put("status", "active");
        sharedSkills.add(skill2);

        return ResponseEntity.ok(ApiResponse.success(sharedSkills));
    }

    @GetMapping("/sharing/received")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getReceivedSkills() {
        List<Map<String, Object>> receivedSkills = new ArrayList<>();

        Map<String, Object> skill1 = new HashMap<>();
        skill1.put("id", "1");
        skill1.put("skillId", "weather-api-skill");
        skill1.put("skillName", "Weather API");
        skill1.put("sharedBy", "user789");
        skill1.put("receivedAt", "2026-01-31 09:00:00");
        skill1.put("status", "active");
        receivedSkills.add(skill1);

        Map<String, Object> skill2 = new HashMap<>();
        skill2.put("id", "2");
        skill2.put("skillId", "stock-api-skill");
        skill2.put("skillName", "Stock API");
        skill2.put("sharedBy", "group789");
        skill2.put("receivedAt", "2026-01-30 14:00:00");
        skill2.put("status", "active");
        receivedSkills.add(skill2);

        return ResponseEntity.ok(ApiResponse.success(receivedSkills));
    }

    @PostMapping("/sharing")
    public ResponseEntity<ApiResponse<Boolean>> shareSkill(@RequestBody Map<String, Object> request) {
        try {
            String skillId = (String) request.get("skillId");
            String target = (String) request.get("target");
            String targetType = (String) request.get("targetType");

            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "分享技能失败: " + e.getMessage()));
        }
    }

    @GetMapping("/groups")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMyGroups() {
        List<Map<String, Object>> groups = new ArrayList<>();

        Map<String, Object> group1 = new HashMap<>();
        group1.put("id", "1");
        group1.put("name", "Development Team");
        group1.put("description", "Development team for skill center");
        group1.put("memberCount", 5);
        group1.put("createdAt", "2026-01-20 10:00:00");
        groups.add(group1);

        Map<String, Object> group2 = new HashMap<>();
        group2.put("id", "2");
        group2.put("name", "Design Team");
        group2.put("description", "Design team for skill center");
        group2.put("memberCount", 3);
        group2.put("createdAt", "2026-01-25 14:30:00");
        groups.add(group2);

        return ResponseEntity.ok(ApiResponse.success(groups));
    }

    @GetMapping("/groups/{groupId}/skills")
    public ResponseEntity<ApiResponse<List<Skill>>> getGroupSkills(@PathVariable String groupId) {
        List<Skill> skills = skillManager.getAllSkills();
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    @GetMapping("/identity")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPersonalIdentity() {
        Map<String, Object> identity = new HashMap<>();

        identity.put("userId", "user123");
        identity.put("username", "John Doe");
        identity.put("email", "john.doe@example.com");
        identity.put("phone", "123-456-7890");
        identity.put("avatar", "https://via.placeholder.com/150");
        identity.put("bio", "Software developer with 5 years of experience");
        identity.put("joinedAt", "2026-01-01 00:00:00");

        List<Map<String, Object>> identityMappings = new ArrayList<>();
        Map<String, Object> mapping1 = new HashMap<>();
        mapping1.put("id", "1");
        mapping1.put("type", "github");
        mapping1.put("identifier", "github.com/johndoe");
        mapping1.put("status", "verified");
        mapping1.put("linkedAt", "2026-01-10 10:00:00");
        identityMappings.add(mapping1);

        Map<String, Object> mapping2 = new HashMap<>();
        mapping2.put("id", "2");
        mapping2.put("type", "twitter");
        mapping2.put("identifier", "twitter.com/johndoe");
        mapping2.put("status", "pending");
        mapping2.put("linkedAt", "2026-01-15 14:30:00");
        identityMappings.add(mapping2);

        identity.put("identityMappings", identityMappings);

        return ResponseEntity.ok(ApiResponse.success(identity));
    }

    @PutMapping("/identity")
    public ResponseEntity<ApiResponse<Boolean>> updatePersonalIdentity(@RequestBody Map<String, Object> identity) {
        try {
            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "更新个人身份失败: " + e.getMessage()));
        }
    }

    @GetMapping("/help")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getHelp() {
        Map<String, Object> helpContent = new HashMap<>();

        List<Map<String, Object>> quickStart = new ArrayList<>();
        Map<String, Object> step1 = new HashMap<>();
        step1.put("id", "1");
        step1.put("title", "发布技能");
        step1.put("content", "在我的技能页面，点击发布技能按钮，填写技能信息并上传技能代码");
        quickStart.add(step1);

        Map<String, Object> step2 = new HashMap<>();
        step2.put("id", "2");
        step2.put("title", "执行技能");
        step2.put("content", "在我的技能页面，选择要执行的技能，填写执行参数并点击执行按钮");
        quickStart.add(step2);

        Map<String, Object> step3 = new HashMap<>();
        step3.put("id", "3");
        step3.put("title", "分享技能");
        step3.put("content", "在我的技能页面，选择要分享的技能，点击分享按钮，填写分享目标");
        quickStart.add(step3);

        Map<String, Object> step4 = new HashMap<>();
        step4.put("id", "4");
        step4.put("title", "管理群组");
        step4.put("content", "在我的群组页面，创建或加入群组，管理群组技能");
        quickStart.add(step4);

        helpContent.put("quickStart", quickStart);

        List<Map<String, Object>> faq = new ArrayList<>();
        Map<String, Object> question1 = new HashMap<>();
        question1.put("id", "1");
        question1.put("question", "如何发布技能？");
        question1.put("answer", "在我的技能页面，点击发布技能按钮，填写技能信息并上传技能代码");
        faq.add(question1);

        Map<String, Object> question2 = new HashMap<>();
        question2.put("id", "2");
        question2.put("question", "如何执行技能？");
        question2.put("answer", "在我的技能页面，选择要执行的技能，填写执行参数并点击执行按钮");
        faq.add(question2);

        Map<String, Object> question3 = new HashMap<>();
        question3.put("id", "3");
        question3.put("question", "如何分享技能？");
        question3.put("answer", "在我的技能页面，选择要分享的技能，点击分享按钮，填写分享目标");
        faq.add(question3);

        helpContent.put("faq", faq);

        return ResponseEntity.ok(ApiResponse.success(helpContent));
    }

    @GetMapping("/system/info")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();

        systemInfo.put("name", "SkillCenter");
        systemInfo.put("version", "0.6.5");
        systemInfo.put("developmentLanguage", "Java 8");
        systemInfo.put("frontendFramework", "HTML5/CSS3/JavaScript");
        systemInfo.put("iconLibrary", "Remix Icon");
        systemInfo.put("database", "JSON Storage");

        List<String> features = new ArrayList<>();
        features.add("技能发布与管理");
        features.add("技能执行与监控");
        features.add("技能分享与协作");
        features.add("群组管理");
        features.add("个人身份管理");
        features.add("系统监控与管理");
        systemInfo.put("features", features);

        Map<String, String> contact = new HashMap<>();
        contact.put("email", "support@skillcenter.com");
        contact.put("phone", "123-456-7890");
        contact.put("website", "https://skillcenter.com");
        systemInfo.put("contact", contact);

        return ResponseEntity.ok(ApiResponse.success(systemInfo));
    }
}
