package net.ooder.skillcenter.controller;

import net.ooder.skillcenter.market.SkillListing;
import net.ooder.skillcenter.market.SkillMarketManager;
import net.ooder.skillcenter.market.SkillReview;
import net.ooder.skillcenter.model.PageResponse;
import net.ooder.skillcenter.model.ApiResponse;
import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 技能市场REST API控制器
 */
@RestController
@RequestMapping("/api/market")
public class MarketController {

    private final SkillMarketManager marketManager;

    /**
     * 构造方法，初始化市场管理器
     */
    public MarketController() {
        this.marketManager = SkillMarketManager.getInstance();
    }

    /**
     * 构造方法，用于依赖注入（测试用）
     */
    public MarketController(SkillMarketManager marketManager) {
        this.marketManager = marketManager;
    }

    /**
     * 获取市场技能列表（支持分页）
     * @param page 页码
     * @param size 每页大小
     * @param sortBy 排序字段
     * @param sortDirection 排序方向
     * @return 分页技能列表
     */
    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<PageResponse<SkillListing>>> getMarketSkills(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        List<SkillListing> allSkills = marketManager.getAllSkills();
        long totalElements = allSkills.size();
        
        // 排序
        if (sortBy != null && !sortBy.isEmpty()) {
            allSkills.sort((s1, s2) -> {
                int compareResult = 0;
                switch (sortBy) {
                    case "id":
                        compareResult = s1.getSkillId().compareTo(s2.getSkillId());
                        break;
                    case "name":
                        compareResult = s1.getName().compareTo(s2.getName());
                        break;
                    case "category":
                        compareResult = s1.getCategory().compareTo(s2.getCategory());
                        break;
                    case "rating":
                        compareResult = Double.compare(s1.getRating(), s2.getRating());
                        break;
                    case "downloadCount":
                        compareResult = Integer.compare(s1.getDownloadCount(), s2.getDownloadCount());
                        break;
                    default:
                        compareResult = s1.getSkillId().compareTo(s2.getSkillId());
                }
                return "desc".equalsIgnoreCase(sortDirection) ? -compareResult : compareResult;
            });
        }
        
        // 分页
        int start = (page - 1) * size;
        int end = Math.min(start + size, allSkills.size());
        List<SkillListing> pagedSkills = start < allSkills.size() ? allSkills.subList(start, end) : new ArrayList<>();
        
        PageResponse<SkillListing> pageResponse = PageResponse.of(pagedSkills, page, size, totalElements);
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    /**
     * 获取技能详情
     * @param id 技能ID
     * @return 技能详情
     */
    @GetMapping("/skills/{id}")
    public ResponseEntity<ApiResponse<SkillListing>> getSkillDetails(@PathVariable String id) {
        SkillListing skill = marketManager.getSkillListing(id);
        if (skill == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Skill not found"));
        }
        return ResponseEntity.ok(ApiResponse.success(skill));
    }

    /**
     * 搜索技能（支持分页）
     * @param keyword 搜索关键词
     * @param page 页码
     * @param size 每页大小
     * @param sortBy 排序字段
     * @param sortDirection 排序方向
     * @return 分页搜索结果
     */
    @GetMapping("/skills/search")
    public ResponseEntity<ApiResponse<PageResponse<SkillListing>>> searchSkills(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        List<SkillListing> searchResults = marketManager.searchSkills(keyword);
        long totalElements = searchResults.size();
        
        // 排序
        if (sortBy != null && !sortBy.isEmpty()) {
            searchResults.sort((s1, s2) -> {
                int compareResult = 0;
                switch (sortBy) {
                    case "id":
                        compareResult = s1.getSkillId().compareTo(s2.getSkillId());
                        break;
                    case "name":
                        compareResult = s1.getName().compareTo(s2.getName());
                        break;
                    case "category":
                        compareResult = s1.getCategory().compareTo(s2.getCategory());
                        break;
                    case "rating":
                        compareResult = Double.compare(s1.getRating(), s2.getRating());
                        break;
                    case "downloadCount":
                        compareResult = Integer.compare(s1.getDownloadCount(), s2.getDownloadCount());
                        break;
                    default:
                        compareResult = s1.getSkillId().compareTo(s2.getSkillId());
                }
                return "desc".equalsIgnoreCase(sortDirection) ? -compareResult : compareResult;
            });
        }
        
        // 分页
        int start = (page - 1) * size;
        int end = Math.min(start + size, searchResults.size());
        List<SkillListing> pagedResults = start < searchResults.size() ? searchResults.subList(start, end) : new ArrayList<>();
        
        PageResponse<SkillListing> pageResponse = PageResponse.of(pagedResults, page, size, totalElements);
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    /**
     * 获取技能分类
     * @return 分类列表
     */
    @GetMapping("/skills/categories")
    public ResponseEntity<ApiResponse<List<String>>> getSkillCategories() {
        List<String> categories = marketManager.getCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    /**
     * 获取分类下的技能（支持分页）
     * @param category 分类名称
     * @param page 页码
     * @param size 每页大小
     * @param sortBy 排序字段
     * @param sortDirection 排序方向
     * @return 分页技能列表
     */
    @GetMapping("/skills/category/{category}")
    public ResponseEntity<ApiResponse<PageResponse<SkillListing>>> getSkillsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        List<SkillListing> categorySkills = marketManager.getSkillsByCategory(category);
        long totalElements = categorySkills.size();
        
        // 排序
        if (sortBy != null && !sortBy.isEmpty()) {
            categorySkills.sort((s1, s2) -> {
                int compareResult = 0;
                switch (sortBy) {
                    case "id":
                        compareResult = s1.getSkillId().compareTo(s2.getSkillId());
                        break;
                    case "name":
                        compareResult = s1.getName().compareTo(s2.getName());
                        break;
                    case "rating":
                        compareResult = Double.compare(s1.getRating(), s2.getRating());
                        break;
                    case "downloadCount":
                        compareResult = Integer.compare(s1.getDownloadCount(), s2.getDownloadCount());
                        break;
                    default:
                        compareResult = s1.getSkillId().compareTo(s2.getSkillId());
                }
                return "desc".equalsIgnoreCase(sortDirection) ? -compareResult : compareResult;
            });
        }
        
        // 分页
        int start = (page - 1) * size;
        int end = Math.min(start + size, categorySkills.size());
        List<SkillListing> pagedSkills = start < categorySkills.size() ? categorySkills.subList(start, end) : new ArrayList<>();
        
        PageResponse<SkillListing> pageResponse = PageResponse.of(pagedSkills, page, size, totalElements);
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    /**
     * 获取热门技能
     * @param limit 限制数量
     * @return 热门技能列表
     */
    @GetMapping("/skills/popular")
    public ResponseEntity<ApiResponse<List<SkillListing>>> getPopularSkills(@RequestParam(defaultValue = "10") int limit) {
        List<SkillListing> skills = marketManager.getPopularSkills(limit);
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    /**
     * 获取最新技能
     * @param limit 限制数量
     * @return 最新技能列表
     */
    @GetMapping("/skills/latest")
    public ResponseEntity<ApiResponse<List<SkillListing>>> getLatestSkills(@RequestParam(defaultValue = "10") int limit) {
        List<SkillListing> skills = marketManager.getLatestSkills(limit);
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    /**
     * 评价技能
     * @param id 技能ID
     * @param rating 评分
     * @param comment 评价内容
     * @param userId 用户ID
     * @return 评价结果
     */
    @PostMapping("/skills/{id}/rate")
    public ResponseEntity<ApiResponse<Boolean>> rateSkill(@PathVariable String id, 
                                           @RequestParam double rating, 
                                           @RequestParam String comment, 
                                           @RequestParam String userId) {
        try {
            boolean result = marketManager.rateSkill(id, rating, comment, userId);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (SkillException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    /**
     * 获取技能评价（支持分页）
     * @param id 技能ID
     * @param page 页码
     * @param size 每页大小
     * @param sortBy 排序字段
     * @param sortDirection 排序方向
     * @return 分页评价列表
     */
    @GetMapping("/skills/{id}/reviews")
    public ResponseEntity<ApiResponse<PageResponse<SkillReview>>> getSkillReviews(
            @PathVariable String id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        List<SkillReview> allReviews = marketManager.getSkillReviews(id);
        long totalElements = allReviews.size();
        
        // 排序
        if (sortBy != null && !sortBy.isEmpty()) {
            allReviews.sort((r1, r2) -> {
                int compareResult = 0;
                switch (sortBy) {
                    case "rating":
                        compareResult = Double.compare(r1.getRating(), r2.getRating());
                        break;
                    case "date":
                        compareResult = Long.compare(r1.getTimestamp(), r2.getTimestamp());
                        break;
                    case "user":
                        compareResult = r1.getUserId().compareTo(r2.getUserId());
                        break;
                    default:
                        compareResult = Long.compare(r1.getTimestamp(), r2.getTimestamp());
                }
                return "desc".equalsIgnoreCase(sortDirection) ? -compareResult : compareResult;
            });
        }
        
        // 分页
        int start = (page - 1) * size;
        int end = Math.min(start + size, allReviews.size());
        List<SkillReview> pagedReviews = start < allReviews.size() ? allReviews.subList(start, end) : new ArrayList<>();
        
        PageResponse<SkillReview> pageResponse = PageResponse.of(pagedReviews, page, size, totalElements);
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    /**
     * 下载技能
     * @param id 技能ID
     * @return 下载结果
     */
    @PostMapping("/skills/{id}/download")
    public ResponseEntity<ApiResponse<byte[]>> downloadSkill(@PathVariable String id) {
        try {
            byte[] data = marketManager.downloadSkill(id);
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (SkillException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    /**
     * 发布技能
     * @param listing 技能列表项
     * @return 发布结果
     */
    @PostMapping("/skills")
    public ResponseEntity<ApiResponse<Boolean>> publishSkill(@RequestBody SkillListing listing) {
        try {
            // 这里需要获取技能实例，实际项目中可能需要从请求中获取或从存储中加载
            // 暂时使用null，实际实现需要修改
            boolean result = marketManager.publishSkill(null, listing);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (SkillException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    /**
     * 更新技能
     * @param id 技能ID
     * @param listing 技能列表项
     * @return 更新结果
     */
    @PutMapping("/skills/{id}")
    public ResponseEntity<ApiResponse<Boolean>> updateSkill(@PathVariable String id, @RequestBody SkillListing listing) {
        try {
            listing.setSkillId(id);
            boolean result = marketManager.updateSkill(listing);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (SkillException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    /**
     * 删除技能
     * @param id 技能ID
     * @return 删除结果
     */
    @DeleteMapping("/skills/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteSkill(@PathVariable String id) {
        boolean result = marketManager.removeSkill(id);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
