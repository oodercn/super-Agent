package net.ooder.nexus.controller;

import net.ooder.nexus.model.ApiResponse;
import net.ooder.nexus.model.llm.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/skillbridge")
@CrossOrigin(origins = "*")
public class SkillBridgeController {

    private final Map<String, SkillRegistration> skillRegistry = new HashMap<>();
    private final Map<String, UserPreferences> userPreferencesMap = new HashMap<>();
    private final List<SkillDescription> skillDescriptions = new ArrayList<>();

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<SkillRegistration>> registerSkill(@RequestBody SkillRegistrationRequest request) {
        String skillId = UUID.randomUUID().toString();
        
        SkillRegistration registration = new SkillRegistration();
        registration.setSkillId(skillId);
        registration.setSkillName(request.getSkillName());
        registration.setSkillVersion(request.getSkillVersion());
        registration.setDescription(request.getDescription());
        registration.setCategory(request.getCategory());
        registration.setTags(request.getTags());
        registration.setAuthor(request.getAuthor());
        registration.setIcon(request.getIcon());
        
        skillRegistry.put(skillId, registration);
        
        return ResponseEntity.ok(ApiResponse.success(registration));
    }

    @GetMapping("/discover")
    public ResponseEntity<ApiResponse<List<SkillDescription>>> discoverSkills(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {
        
        List<SkillDescription> skills = new ArrayList<>();
        
        for (SkillRegistration registration : skillRegistry.values()) {
            if (category != null && !category.isEmpty() && !category.equals(registration.getCategory())) {
                continue;
            }
            if (keyword != null && !keyword.isEmpty() && 
                !registration.getSkillName().toLowerCase().contains(keyword.toLowerCase()) &&
                !registration.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                continue;
            }
            
            SkillDescription description = new SkillDescription();
            description.setSkillId(registration.getSkillId());
            description.setSkillName(registration.getSkillName());
            description.setDescription(registration.getDescription());
            description.setCategory(registration.getCategory());
            description.setTags(registration.getTags());
            
            SkillDescription.SkillMetadata metadata = new SkillDescription.SkillMetadata();
            metadata.setVersion(registration.getSkillVersion());
            metadata.setAuthor(registration.getAuthor());
            metadata.setCreateTime(registration.getCreateTime());
            metadata.setUpdateTime(registration.getUpdateTime());
            metadata.setDownloadCount(0);
            metadata.setRating(0.0);
            description.setMetadata(metadata);
            
            skills.add(description);
        }
        
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    @PostMapping("/execute")
    public ResponseEntity<ApiResponse<ExecutionResult>> executeSkill(@RequestBody SkillExecutionRequest request) {
        SkillRegistration skill = skillRegistry.get(request.getSkillId());
        if (skill == null) {
            return ResponseEntity.ok(ApiResponse.error("技能不存在"));
        }
        
        ExecutionResult result = new ExecutionResult();
        result.setExecutionId(UUID.randomUUID().toString());
        result.setSkillId(request.getSkillId());
        result.setSkillName(skill.getSkillName());
        result.setStatus(ExecutionResult.ExecutionStatus.COMPLETED);
        result.setExecuteTime(System.currentTimeMillis());
        result.setResult("技能执行成功");
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/preferences")
    public ResponseEntity<ApiResponse<UserPreferences>> getUserPreferences(@RequestParam String userId) {
        UserPreferences preferences = userPreferencesMap.get(userId);
        if (preferences == null) {
            preferences = new UserPreferences();
            preferences.setUserId(userId);
            userPreferencesMap.put(userId, preferences);
        }
        
        return ResponseEntity.ok(ApiResponse.success(preferences));
    }

    @PutMapping("/preferences")
    public ResponseEntity<ApiResponse<Void>> updateUserPreferences(@RequestBody UserPreferencesRequest request) {
        UserPreferences preferences = userPreferencesMap.get(request.getUserId());
        if (preferences == null) {
            preferences = new UserPreferences();
            preferences.setUserId(request.getUserId());
            userPreferencesMap.put(request.getUserId(), preferences);
        }
        
        if (request.getApiToken() != null) {
            preferences.setApiToken(request.getApiToken());
        }
        if (request.getDefaultCategory() != null) {
            preferences.setDefaultCategory(request.getDefaultCategory());
        }
        if (request.getSkillSort() != null) {
            preferences.setSkillSort(request.getSkillSort());
        }
        if (request.getTheme() != null) {
            preferences.setTheme(request.getTheme());
        }
        if (request.getLanguage() != null) {
            preferences.setLanguage(request.getLanguage());
        }
        
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/context")
    public ResponseEntity<ApiResponse<ContextAnalysis>> analyzeContext(@RequestBody ContextAnalysisRequest request) {
        ContextAnalysis analysis = new ContextAnalysis();
        analysis.setRequestId(UUID.randomUUID().toString());
        analysis.setIntent("execute_skill");
        analysis.setEntities(Arrays.asList(request.getContext().split(" ")));
        analysis.setContext(request.getContext());
        analysis.setConfidence(0.95);
        
        List<ContextAnalysis.SkillRecommendation> recommendations = new ArrayList<>();
        for (SkillRegistration skill : skillRegistry.values()) {
            ContextAnalysis.SkillRecommendation recommendation = new ContextAnalysis.SkillRecommendation();
            recommendation.setSkillId(skill.getSkillId());
            recommendation.setSkillName(skill.getSkillName());
            recommendation.setConfidence(0.9);
            recommendation.setReason("匹配度较高");
            recommendations.add(recommendation);
        }
        analysis.setRecommendedSkills(recommendations);
        
        return ResponseEntity.ok(ApiResponse.success(analysis));
    }

    @GetMapping("/skill/{skillId}/description")
    public ResponseEntity<ApiResponse<SkillDescription>> getSkillDescription(@PathVariable String skillId) {
        SkillRegistration registration = skillRegistry.get(skillId);
        if (registration == null) {
            return ResponseEntity.ok(ApiResponse.error("技能不存在"));
        }
        
        SkillDescription description = new SkillDescription();
        description.setSkillId(registration.getSkillId());
        description.setSkillName(registration.getSkillName());
        description.setDescription(registration.getDescription());
        description.setCategory(registration.getCategory());
        description.setTags(registration.getTags());
        
        SkillDescription.SkillMetadata metadata = new SkillDescription.SkillMetadata();
        metadata.setVersion(registration.getSkillVersion());
        metadata.setAuthor(registration.getAuthor());
        metadata.setCreateTime(registration.getCreateTime());
        metadata.setUpdateTime(registration.getUpdateTime());
        metadata.setDownloadCount(0);
        metadata.setRating(0.0);
        description.setMetadata(metadata);
        
        return ResponseEntity.ok(ApiResponse.success(description));
    }

    @GetMapping("/registered")
    public ResponseEntity<ApiResponse<List<SkillRegistration>>> getRegisteredSkills(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        
        List<SkillRegistration> skills = new ArrayList<>();
        
        for (SkillRegistration registration : skillRegistry.values()) {
            if (category != null && !category.isEmpty() && !category.equals(registration.getCategory())) {
                continue;
            }
            if (keyword != null && !keyword.isEmpty() && 
                !registration.getSkillName().toLowerCase().contains(keyword.toLowerCase())) {
                continue;
            }
            if (status != null && !status.isEmpty() && !"all".equals(status) && !status.equals(registration.getStatus().name().toLowerCase())) {
                continue;
            }
            
            skills.add(registration);
        }
        
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    @DeleteMapping("/skill/{skillId}")
    public ResponseEntity<ApiResponse<Void>> deleteSkill(@PathVariable String skillId) {
        if (!skillRegistry.containsKey(skillId)) {
            return ResponseEntity.ok(ApiResponse.error("技能不存在"));
        }
        
        skillRegistry.remove(skillId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/skill/{skillId}")
    public ResponseEntity<ApiResponse<Void>> updateSkill(
            @PathVariable String skillId,
            @RequestBody SkillRegistrationRequest request) {
        
        SkillRegistration registration = skillRegistry.get(skillId);
        if (registration == null) {
            return ResponseEntity.ok(ApiResponse.error("技能不存在"));
        }
        
        if (request.getSkillName() != null) {
            registration.setSkillName(request.getSkillName());
        }
        if (request.getSkillVersion() != null) {
            registration.setSkillVersion(request.getSkillVersion());
        }
        if (request.getDescription() != null) {
            registration.setDescription(request.getDescription());
        }
        if (request.getCategory() != null) {
            registration.setCategory(request.getCategory());
        }
        if (request.getTags() != null) {
            registration.setTags(request.getTags());
        }
        if (request.getIcon() != null) {
            registration.setIcon(request.getIcon());
        }
        registration.setUpdateTime(System.currentTimeMillis());
        
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/test/{skillId}")
    public ResponseEntity<ApiResponse<ExecutionResult>> testSkill(
            @PathVariable String skillId,
            @RequestBody Map<String, Object> params) {
        
        SkillRegistration skill = skillRegistry.get(skillId);
        if (skill == null) {
            return ResponseEntity.ok(ApiResponse.error("技能不存在"));
        }
        
        ExecutionResult result = new ExecutionResult();
        result.setExecutionId(UUID.randomUUID().toString());
        result.setSkillId(skillId);
        result.setSkillName(skill.getSkillName());
        result.setStatus(ExecutionResult.ExecutionStatus.COMPLETED);
        result.setExecuteTime(System.currentTimeMillis());
        result.setResult("技能测试成功，参数：" + params.toString());
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/execution/history")
    public ResponseEntity<ApiResponse<List<ExecutionResult>>> getExecutionHistory() {
        List<ExecutionResult> history = new ArrayList<>();
        return ResponseEntity.ok(ApiResponse.success(history));
    }

    @GetMapping("/execution/result/{executionId}")
    public ResponseEntity<ApiResponse<ExecutionResult>> getExecutionResult(@PathVariable String executionId) {
        ExecutionResult result = new ExecutionResult();
        result.setExecutionId(executionId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/execution/log/{executionId}")
    public ResponseEntity<ApiResponse<String>> getExecutionLog(@PathVariable String executionId) {
        return ResponseEntity.ok(ApiResponse.success("执行日志：暂无日志"));
    }

    public static class SkillRegistrationRequest {
        private String skillName;
        private String skillVersion;
        private String description;
        private String category;
        private List<String> tags;
        private String author;
        private String icon;

        public String getSkillName() {
            return skillName;
        }

        public void setSkillName(String skillName) {
            this.skillName = skillName;
        }

        public String getSkillVersion() {
            return skillVersion;
        }

        public void setSkillVersion(String skillVersion) {
            this.skillVersion = skillVersion;
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

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    public static class SkillExecutionRequest {
        private String skillId;
        private Map<String, Object> parameters;

        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }
    }

    public static class UserPreferencesRequest {
        private String userId;
        private String apiToken;
        private String defaultCategory;
        private String skillSort;
        private String theme;
        private String language;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getApiToken() {
            return apiToken;
        }

        public void setApiToken(String apiToken) {
            this.apiToken = apiToken;
        }

        public String getDefaultCategory() {
            return defaultCategory;
        }

        public void setDefaultCategory(String defaultCategory) {
            this.defaultCategory = defaultCategory;
        }

        public String getSkillSort() {
            return skillSort;
        }

        public void setSkillSort(String skillSort) {
            this.skillSort = skillSort;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }

    public static class ContextAnalysisRequest {
        private String context;

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }
    }

    public static class ExecutionResult {
        private String executionId;
        private String skillId;
        private String skillName;
        private ExecutionStatus status;
        private long executeTime;
        private String result;

        public enum ExecutionStatus {
            RUNNING,
            COMPLETED,
            FAILED,
            CANCELLED
        }

        public String getExecutionId() {
            return executionId;
        }

        public void setExecutionId(String executionId) {
            this.executionId = executionId;
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

        public ExecutionStatus getStatus() {
            return status;
        }

        public void setStatus(ExecutionStatus status) {
            this.status = status;
        }

        public long getExecuteTime() {
            return executeTime;
        }

        public void setExecuteTime(long executeTime) {
            this.executeTime = executeTime;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
