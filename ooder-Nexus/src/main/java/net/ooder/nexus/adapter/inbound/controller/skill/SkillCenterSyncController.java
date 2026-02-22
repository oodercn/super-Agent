package net.ooder.nexus.adapter.inbound.controller.skill;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.model.llm.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 技能中心同步控制器
 * 处理技能上传下载、分类管理、批量同步等操作
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
@RestController
@RequestMapping("/api/skillcenter/sync")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class SkillCenterSyncController {

    private final Map<String, SkillInfo> skillInfoMap = new HashMap<>();
    private final Map<String, Category> categoryMap = new HashMap<>();
    private final Map<String, SyncStatus> syncStatusMap = new HashMap<>();

    @PostMapping("/upload")
    @ResponseBody
    public ResultModel<SkillUploadResult> uploadSkill(@RequestParam("file") MultipartFile file,
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

            return ResultModel.success(result);
        } catch (Exception e) {
            SkillUploadResult result = new SkillUploadResult();
            result.setSuccess(false);
            result.setMessage("技能上传失败：" + e.getMessage());
            return ResultModel.error("技能上传失败：" + e.getMessage(), 500);
        }
    }

    @PostMapping("/download")
    @ResponseBody
    public ResponseEntity<byte[]> downloadSkill(@RequestBody Map<String, String> request) {
        String skillId = request.get("skillId");
        SkillInfo skillInfo = skillInfoMap.get(skillId);
        if (skillInfo == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] skillData = ("模拟技能数据：" + skillInfo.getSkillName()).getBytes();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + skillInfo.getSkillName() + ".zip\"")
                .body(skillData);
    }

    @PostMapping("/list")
    @ResponseBody
    public ResultModel<List<SkillInfo>> getSkillList(@RequestBody Map<String, Object> request) {
        String category = (String) request.get("category");
        Integer page = request.get("page") != null ? (Integer) request.get("page") : null;
        Integer size = request.get("size") != null ? (Integer) request.get("size") : null;

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

        return ResultModel.success(skills);
    }

    @PostMapping("/version/update")
    @ResponseBody
    public ResultModel<Boolean> updateSkillVersion(@RequestBody Map<String, Object> request) {
        String skillId = (String) request.get("skillId");
        String version = (String) request.get("version");

        SkillInfo skillInfo = skillInfoMap.get(skillId);
        if (skillInfo == null) {
            return ResultModel.error("技能不存在", 404);
        }

        if (version != null) {
            skillInfo.setVersion(version);
            skillInfo.setUpdateTime(System.currentTimeMillis());
        }

        return ResultModel.success(true);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteSkill(@RequestBody Map<String, String> request) {
        String skillId = request.get("skillId");

        if (!skillInfoMap.containsKey(skillId)) {
            return ResultModel.error("技能不存在", 404);
        }

        skillInfoMap.remove(skillId);
        return ResultModel.success(true);
    }

    @PostMapping("/search")
    @ResponseBody
    public ResultModel<List<SkillInfo>> searchSkills(@RequestBody Map<String, Object> request) {
        String keyword = (String) request.get("keyword");
        String category = (String) request.get("category");

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

        return ResultModel.success(skills);
    }

    @PostMapping("/categories")
    @ResponseBody
    public ResultModel<List<Category>> getCategories() {
        List<Category> categories = new ArrayList<>(categoryMap.values());
        return ResultModel.success(categories);
    }

    @PostMapping("/category/create")
    @ResponseBody
    public ResultModel<Category> createCategory(@RequestBody CategoryRequest request) {
        String categoryId = UUID.randomUUID().toString();

        Category category = new Category();
        category.setId(categoryId);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setIcon(request.getIcon());
        category.setSkillCount(0);

        categoryMap.put(categoryId, category);

        return ResultModel.success(category);
    }

    @PostMapping("/category/update")
    @ResponseBody
    public ResultModel<Boolean> updateCategory(@RequestBody Map<String, Object> request) {
        String categoryId = (String) request.get("categoryId");
        Map<String, Object> categoryData = (Map<String, Object>) request.get("category");

        Category category = categoryMap.get(categoryId);
        if (category == null) {
            return ResultModel.error("分类不存在", 404);
        }

        if (categoryData.containsKey("name")) {
            category.setName((String) categoryData.get("name"));
        }
        if (categoryData.containsKey("description")) {
            category.setDescription((String) categoryData.get("description"));
        }
        if (categoryData.containsKey("icon")) {
            category.setIcon((String) categoryData.get("icon"));
        }
        if (categoryData.containsKey("sortOrder")) {
            category.setSortOrder((Integer) categoryData.get("sortOrder"));
        }
        category.setUpdateTime(System.currentTimeMillis());

        return ResultModel.success(true);
    }

    @PostMapping("/category/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteCategory(@RequestBody Map<String, String> request) {
        String categoryId = request.get("categoryId");

        if (!categoryMap.containsKey(categoryId)) {
            return ResultModel.error("分类不存在", 404);
        }

        categoryMap.remove(categoryId);
        return ResultModel.success(true);
    }

    @PostMapping("/batch")
    @ResponseBody
    public ResultModel<BatchSyncResult> batchSyncSkills(@RequestBody BatchSyncRequest request) {
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

        return ResultModel.success(result);
    }

    @PostMapping("/status")
    @ResponseBody
    public ResultModel<SyncStatus> getSyncStatus() {
        SyncStatus status = new SyncStatus();
        status.setState(SyncStatus.SyncState.COMPLETED);
        status.setTotalSkills(0);
        status.setCompletedSkills(0);
        status.setFailedSkills(0);
        status.setTasks(new ArrayList<>());

        return ResultModel.success(status);
    }

    @PostMapping("/cancel")
    @ResponseBody
    public ResultModel<Boolean> cancelSync(@RequestBody Map<String, String> request) {
        String batchId = request.get("batchId");

        SyncStatus status = syncStatusMap.get(batchId);
        if (status == null) {
            return ResultModel.error("同步任务不存在", 404);
        }

        status.setState(SyncStatus.SyncState.CANCELLED);
        status.setEndTime(System.currentTimeMillis());

        return ResultModel.success(true);
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
