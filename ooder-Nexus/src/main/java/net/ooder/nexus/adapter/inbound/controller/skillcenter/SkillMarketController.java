package net.ooder.nexus.adapter.inbound.controller.skillcenter;

import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.nexus.domain.skill.model.SkillPackage;
import net.ooder.nexus.domain.skill.model.SkillPackage.AuthStatus;
import net.ooder.nexus.service.skill.SkillMarketService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 技能市场控制器
 *
 * <p>提供技能市场 API：</p>
 * <ul>
 *   <li>技能发现</li>
 *   <li>技能搜索</li>
 *   <li>技能安装/卸载</li>
 *   <li>技能认证</li>
 * </ul>
 *
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@RestController
@RequestMapping(value = "/api/skillcenter/market", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS})
public class SkillMarketController {

    private static final Logger log = LoggerFactory.getLogger(SkillMarketController.class);

    @Autowired
    private SkillMarketService skillMarketService;

    /**
     * 获取技能列表
     */
    @GetMapping("/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getSkillList(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        log.info("Get skill list: category={}, page={}, size={}", category, page, pageSize);
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            List<SkillPackage> skills = skillMarketService.getSkillList(category, page, pageSize);
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            for (SkillPackage skill : skills) {
                data.add(convertSkillToMap(skill));
            }
            result.setData(data);
            result.setSize(skillMarketService.getSkillCount(category));
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting skill list", e);
            result.setRequestStatus(500);
            result.setMessage("获取技能列表失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 搜索技能
     */
    @PostMapping("/search")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> searchSkills(@RequestBody Map<String, Object> request) {
        String keyword = (String) request.get("keyword");
        String category = (String) request.get("category");
        int page = request.containsKey("page") ? ((Number) request.get("page")).intValue() : 1;
        int pageSize = request.containsKey("pageSize") ? ((Number) request.get("pageSize")).intValue() : 20;

        log.info("Search skills: keyword={}, category={}", keyword, category);
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            List<SkillPackage> skills = skillMarketService.searchSkills(keyword, category, page, pageSize);
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            for (SkillPackage skill : skills) {
                data.add(convertSkillToMap(skill));
            }
            result.setData(data);
            result.setSize(data.size());
            result.setRequestStatus(200);
            result.setMessage("搜索成功");
        } catch (Exception e) {
            log.error("Error searching skills", e);
            result.setRequestStatus(500);
            result.setMessage("搜索技能失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取技能详情
     */
    @GetMapping("/{skillId}")
    @ResponseBody
    public ResultModel<Map<String, Object>> getSkillDetail(@PathVariable String skillId) {
        log.info("Get skill detail: {}", skillId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            SkillPackage skill = skillMarketService.getSkillDetail(skillId);
            if (skill != null) {
                result.setData(convertSkillToMap(skill));
                result.setRequestStatus(200);
                result.setMessage("获取成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("技能不存在");
            }
        } catch (Exception e) {
            log.error("Error getting skill detail", e);
            result.setRequestStatus(500);
            result.setMessage("获取技能详情失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 安装技能
     */
    @PostMapping("/{skillId}/install")
    @ResponseBody
    public ResultModel<Map<String, Object>> installSkill(
            @PathVariable String skillId,
            @RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        @SuppressWarnings("unchecked")
        Map<String, Object> options = (Map<String, Object>) request.get("options");

        log.info("Install skill: {} for user: {}", skillId, userId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            Map<String, Object> installResult = skillMarketService.installSkill(skillId, userId, options);
            Boolean success = (Boolean) installResult.get("success");
            result.setData(installResult);
            result.setRequestStatus(Boolean.TRUE.equals(success) ? 200 : 400);
            result.setMessage((String) installResult.get("message"));
        } catch (Exception e) {
            log.error("Error installing skill", e);
            result.setRequestStatus(500);
            result.setMessage("安装技能失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 卸载技能
     */
    @DeleteMapping("/{skillId}")
    @ResponseBody
    public ResultModel<Boolean> uninstallSkill(
            @PathVariable String skillId,
            @RequestParam String userId) {
        log.info("Uninstall skill: {} for user: {}", skillId, userId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = skillMarketService.uninstallSkill(skillId, userId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 400);
            result.setMessage(success ? "卸载成功" : "卸载失败");
        } catch (Exception e) {
            log.error("Error uninstalling skill", e);
            result.setRequestStatus(500);
            result.setMessage("卸载技能失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 更新技能
     */
    @PutMapping("/{skillId}/update")
    @ResponseBody
    public ResultModel<Map<String, Object>> updateSkill(
            @PathVariable String skillId,
            @RequestParam String userId) {
        log.info("Update skill: {} for user: {}", skillId, userId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            Map<String, Object> updateResult = skillMarketService.updateSkill(skillId, userId);
            Boolean success = (Boolean) updateResult.get("success");
            result.setData(updateResult);
            result.setRequestStatus(Boolean.TRUE.equals(success) ? 200 : 400);
            result.setMessage((String) updateResult.get("message"));
        } catch (Exception e) {
            log.error("Error updating skill", e);
            result.setRequestStatus(500);
            result.setMessage("更新技能失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取技能认证状态
     */
    @GetMapping("/{skillId}/auth")
    @ResponseBody
    public ResultModel<Map<String, Object>> getAuthStatus(@PathVariable String skillId) {
        log.info("Get auth status for skill: {}", skillId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            AuthStatus status = skillMarketService.getAuthStatus(skillId);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("skillId", skillId);
            data.put("authStatus", status.name());
            data.put("verified", status == AuthStatus.VERIFIED);
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting auth status", e);
            result.setRequestStatus(500);
            result.setMessage("获取认证状态失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取分类列表
     */
    @GetMapping("/categories")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getCategories() {
        log.info("Get skill categories");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            List<Map<String, Object>> categories = skillMarketService.getCategories();
            result.setData(categories);
            result.setSize(categories.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting categories", e);
            result.setRequestStatus(500);
            result.setMessage("获取分类失败: " + e.getMessage());
        }
        return result;
    }

    private Map<String, Object> convertSkillToMap(SkillPackage skill) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("skillId", skill.getSkillId());
        map.put("name", skill.getName());
        map.put("version", skill.getVersion());
        map.put("description", skill.getDescription());
        map.put("category", skill.getCategory());
        map.put("author", skill.getAuthor());
        map.put("icon", skill.getIcon());
        map.put("tags", skill.getTags());
        map.put("downloadUrl", skill.getDownloadUrl());
        map.put("checksum", skill.getChecksum());
        map.put("authStatus", skill.getAuthStatus() != null ? skill.getAuthStatus().name() : "UNVERIFIED");
        map.put("downloadCount", skill.getDownloadCount());
        map.put("installCount", skill.getInstallCount());
        map.put("createTime", skill.getCreateTime());
        map.put("updateTime", skill.getUpdateTime());
        map.put("status", skill.getStatus());
        return map;
    }
}
