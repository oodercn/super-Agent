package net.ooder.nexus.controller;

import net.ooder.nexus.model.ApiResponse;
import net.ooder.nexus.model.llm.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/skillcenter/sync")
@CrossOrigin(origins = "*")
public class SkillCenterSyncController {

    private final Map<String, SkillInfo> skillInfoMap = new HashMap<>();
    private final Map<String, Category> categoryMap = new HashMap<>();
    private final Map<String, SyncStatus> syncStatusMap = new HashMap<>();

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<SkillUploadResult>> uploadSkill(@RequestParam("file") MultipartFile file,
                                                                     @RequestParam("metadata") String metadataJson) {
        try {
            String skillId = UUID.randomUUID().toString();
            
            SkillUploadResult result = new SkillUploadResult();
            result.setSkillId(skillId);
            result.setSkillName(file.getOriginalFilename());
            result.setVersion("1.0.0");
            result.setSuccess(true);
            result.setMessage("技能上传成功");
            result.setUploadUrl("/api/skillcenter/sync/download/" + skillId);
            
            SkillInfo skillInfo = new SkillInfo();
            skillInfo.setSkillId(skillId);
            skillInfo.setSkillName(file.getOriginalFilename());
            skillInfo.setDescription("上传的技能");
            skillInfo.setVersion("1.0.0");
            skillInfo.setRating(0.0);
            skillInfo.setDownloadCount(0);
            skillInfo.setIcon("ri-file-line");
            skillInfo.setCreateTime(System.currentTimeMillis());
            skillInfo.setUpdateTime(System.currentTimeMillis());
            skillInfoMap.put(skillId, skillInfo);
            
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            SkillUploadResult result = new SkillUploadResult();
            result.setSuccess(false);
            result.setMessage("技能上传失败：" + e.getMessage());
            return ResponseEntity.ok(ApiResponse.success(result));
        }
    }

    @GetMapping("/download/{skillId}")
    public ResponseEntity<byte[]> downloadSkill(@PathVariable String skillId) {
        SkillInfo skillInfo = skillInfoMap.get(skillId);
        if (skillInfo == null) {
            return ResponseEntity.notFound().build();
        }
        
        byte[] skillData = ("模拟技能数据：" + skillInfo.getSkillName()).getBytes();
        
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + skillInfo.getSkillName() + ".zip\"")
                .body(skillData);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<SkillInfo>>> getSkillList(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        List<SkillInfo> skills = new ArrayList<>(skillInfoMap.values());
        
        if (category != null && !category.isEmpty()) {
            skills.removeIf(skill -> !category.equals(skill.getCategory()));
        }
        
        if (page != null && size != null) {
            int start = page * size;
            int end = Math.min(start + size, skills.size());
            if (start < skills.size()) {
                skills = skills.subList(start, end);
            } else {
                skills = new ArrayList<>();
            }
        }
        
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    @PutMapping("/version/{skillId}")
    public ResponseEntity<ApiResponse<Void>> updateSkillVersion(
            @PathVariable String skillId,
            @RequestBody Map<String, String> request) {
        
        SkillInfo skillInfo = skillInfoMap.get(skillId);
        if (skillInfo == null) {
            return ResponseEntity.ok(ApiResponse.error("技能不存在"));
        }
        
        String version = request.get("version");
        if (version != null) {
            skillInfo.setVersion(version);
            skillInfo.setUpdateTime(System.currentTimeMillis());
        }
        
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<ApiResponse<Void>> deleteSkill(@PathVariable String skillId) {
        if (!skillInfoMap.containsKey(skillId)) {
            return ResponseEntity.ok(ApiResponse.error("技能不存在"));
        }
        
        skillInfoMap.remove(skillId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SkillInfo>>> searchSkills(
            @RequestParam String keyword,
            @RequestParam(required = false) String category) {
        
        List<SkillInfo> skills = new ArrayList<>();
        
        for (SkillInfo skill : skillInfoMap.values()) {
            if (category != null && !category.isEmpty() && !category.equals(skill.getCategory())) {
                continue;
            }
            if (skill.getSkillName().toLowerCase().contains(keyword.toLowerCase()) ||
                skill.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                skills.add(skill);
            }
        }
        
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<Category>>> getCategories() {
        List<Category> categories = new ArrayList<>(categoryMap.values());
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @PostMapping("/category")
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody CategoryRequest request) {
        String categoryId = UUID.randomUUID().toString();
        
        Category category = new Category();
        category.setId(categoryId);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setIcon(request.getIcon());
        category.setSkillCount(0);
        
        categoryMap.put(categoryId, category);
        
        return ResponseEntity.ok(ApiResponse.success(category));
    }

    @PutMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> updateCategory(
            @PathVariable String categoryId,
            @RequestBody CategoryRequest request) {
        
        Category category = categoryMap.get(categoryId);
        if (category == null) {
            return ResponseEntity.ok(ApiResponse.error("分类不存在"));
        }
        
        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getIcon() != null) {
            category.setIcon(request.getIcon());
        }
        if (request.getSortOrder() != null) {
            category.setSortOrder(request.getSortOrder());
        }
        category.setUpdateTime(System.currentTimeMillis());
        
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable String categoryId) {
        if (!categoryMap.containsKey(categoryId)) {
            return ResponseEntity.ok(ApiResponse.error("分类不存在"));
        }
        
        categoryMap.remove(categoryId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<BatchSyncResult>> batchSyncSkills(@RequestBody BatchSyncRequest request) {
        String batchId = UUID.randomUUID().toString();
        
        BatchSyncResult result = new BatchSyncResult();
        result.setBatchId(batchId);
        result.setTotalSkills(request.getSkills().size());
        result.setSuccessCount(0);
        result.setFailureCount(0);
        result.setStatus(BatchSyncResult.SyncStatus.IN_PROGRESS);
        
        List<SkillUploadResult> results = new ArrayList<>();
        for (String skillId : request.getSkills()) {
            SkillUploadResult uploadResult = new SkillUploadResult();
            uploadResult.setSkillId(skillId);
            uploadResult.setSuccess(true);
            uploadResult.setMessage("同步成功");
            results.add(uploadResult);
            result.setSuccessCount(result.getSuccessCount() + 1);
        }
        
        result.setResults(results);
        result.setStatus(BatchSyncResult.SyncStatus.COMPLETED);
        
        SyncStatus syncStatus = new SyncStatus();
        syncStatus.setBatchId(batchId);
        syncStatus.setState(SyncStatus.SyncState.COMPLETED);
        syncStatus.setTotalSkills(request.getSkills().size());
        syncStatus.setCompletedSkills(request.getSkills().size());
        syncStatus.setFailedSkills(0);
        syncStatus.setEndTime(System.currentTimeMillis());
        
        List<SyncStatus.SyncTask> tasks = new ArrayList<>();
        for (String skillId : request.getSkills()) {
            SyncStatus.SyncTask task = new SyncStatus.SyncTask();
            task.setTaskId(UUID.randomUUID().toString());
            task.setSkillId(skillId);
            task.setState(SyncStatus.SyncTask.TaskState.COMPLETED);
            task.setEndTime(System.currentTimeMillis());
            tasks.add(task);
        }
        syncStatus.setTasks(tasks);
        
        syncStatusMap.put(batchId, syncStatus);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<SyncStatus>> getSyncStatus() {
        SyncStatus status = new SyncStatus();
        status.setState(SyncStatus.SyncState.COMPLETED);
        status.setTotalSkills(0);
        status.setCompletedSkills(0);
        status.setFailedSkills(0);
        status.setTasks(new ArrayList<>());
        
        return ResponseEntity.ok(ApiResponse.success(status));
    }

    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelSync(@RequestBody Map<String, String> request) {
        String batchId = request.get("batchId");
        
        SyncStatus status = syncStatusMap.get(batchId);
        if (status == null) {
            return ResponseEntity.ok(ApiResponse.error("同步任务不存在"));
        }
        
        status.setState(SyncStatus.SyncState.CANCELLED);
        status.setEndTime(System.currentTimeMillis());
        
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    public static class CategoryRequest {
        private String name;
        private String description;
        private String icon;
        private Integer sortOrder;

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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Integer getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
        }
    }

    public static class BatchSyncRequest {
        private List<String> skills;

        public List<String> getSkills() {
            return skills;
        }

        public void setSkills(List<String> skills) {
            this.skills = skills;
        }
    }

    public static class CancelSyncRequest {
        private String batchId;

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }
    }
}
