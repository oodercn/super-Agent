package net.ooder.skillcenter.controller;

import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillResult;
import net.ooder.skillcenter.model.PageResponse;
import net.ooder.skillcenter.model.ApiResponse;
import net.ooder.skillcenter.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 技能管理REST API控制器 - 符合 ooderNexus 规范
 */
@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;

    /**
     * 构造方法，依赖注入技能服务
     * @param skillService 技能服务
     */
    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    /**
     * 获取所有技能（支持分页和排序）
     * @param page 页码
     * @param size 每页大小
     * @param sortBy 排序字段
     * @param sortDirection 排序方向
     * @return 分页技能列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<Skill>>> getAllSkills(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        PageResponse<Skill> pageResponse = skillService.getAllSkills(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    /**
     * 获取指定ID的技能
     * @param id 技能ID
     * @return 技能实例
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Skill>> getSkill(@PathVariable String id) {
        Skill skill = skillService.getSkill(id);
        if (skill == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Skill not found"));
        }
        return ResponseEntity.ok(ApiResponse.success(skill));
    }

    /**
     * 执行指定ID的技能
     * @param id 技能ID
     * @param request 执行请求，包含参数和上下文信息
     * @return 执行结果
     */
    @PostMapping("/{id}/execute")
    public ResponseEntity<ApiResponse<SkillResult>> executeSkill(@PathVariable String id, @RequestBody ExecuteSkillRequest request) {
        SkillResult result = skillService.executeSkill(id, request.getParameters(), request.getAttributes());
        if (result.getStatus() == SkillResult.Status.FAILED) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(result.getMessage()));
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * 获取技能分类列表
     * @return 分类列表
     */
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> getCategories() {
        List<String> categories = skillService.getCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    /**
     * 根据分类获取技能列表（支持分页和排序）
     * @param category 分类名称
     * @param page 页码
     * @param size 每页大小
     * @param sortBy 排序字段
     * @param sortDirection 排序方向
     * @return 分页技能列表
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<PageResponse<Skill>>> getSkillsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        PageResponse<Skill> pageResponse = skillService.getSkillsByCategory(category, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    /**
     * 删除技能
     * @param id 技能ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteSkill(@PathVariable String id) {
        boolean success = skillService.deleteSkill(id);
        if (!success) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete skill"));
        }
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    /**
     * 发布新技能
     * @param skill 技能实例
     * @return 发布结果
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Boolean>> publishSkill(@RequestBody Skill skill) {
        boolean success = skillService.publishSkill(skill);
        if (!success) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to publish skill"));
        }
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    /**
     * 更新技能
     * @param id 技能ID
     * @param skill 技能实例
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> updateSkill(@PathVariable String id, @RequestBody Skill skill) {
        boolean success = skillService.updateSkill(id, skill);
        if (!success) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update skill"));
        }
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    /**
     * 批量获取技能
     * @param ids 技能ID列表
     * @return 技能列表
     */
    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<List<Skill>>> getSkillsByIds(@RequestBody List<String> ids) {
        List<Skill> skills = skillService.getSkillsByIds(ids);
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    /**
     * 搜索技能
     * @param keyword 搜索关键词
     * @param page 页码
     * @param size 每页大小
     * @return 分页技能列表
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<Skill>>> searchSkills(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<Skill> pageResponse = skillService.searchSkills(keyword, page, size);
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    /**
     * 执行技能请求体
     */
    static class ExecuteSkillRequest {
        private Map<String, Object> parameters;
        private Map<String, Object> attributes;

        // Getters and setters
        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        public Map<String, Object> getAttributes() {
            return attributes;
        }

        public void setAttributes(Map<String, Object> attributes) {
            this.attributes = attributes;
        }
    }
}
