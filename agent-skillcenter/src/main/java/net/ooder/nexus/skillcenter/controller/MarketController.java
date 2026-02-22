package net.ooder.nexus.skillcenter.controller;

import net.ooder.skillcenter.market.SkillListing;
import net.ooder.skillcenter.market.SkillMarketManager;
import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.nexus.skillcenter.dto.market.*;
import net.ooder.nexus.skillcenter.dto.common.PaginationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class MarketController extends BaseController {

    private final SkillMarketManager marketManager;

    public MarketController() {
        this.marketManager = SkillMarketManager.getInstance();
    }

    @PostMapping("/skills")
    public ResultModel<List<SkillListing>> getMarketSkills(@RequestBody MarketQueryDTO query) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getMarketSkills", query);

        try {
            List<SkillListing> marketSkills = marketManager.getAllSkills();
            logRequestEnd("getMarketSkills", marketSkills.size() + " skills", System.currentTimeMillis() - startTime);
            return ResultModel.success(marketSkills);
        } catch (Exception e) {
            logRequestError("getMarketSkills", e);
            return ResultModel.error(500, "获取市场技能列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/{skillId}")
    public ResultModel<SkillListing> getMarketSkill(@PathVariable String skillId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getMarketSkill", skillId);

        try {
            SkillListing skill = marketManager.getSkillListing(skillId);
            if (skill == null) {
                logRequestEnd("getMarketSkill", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("技能不存在");
            }
            logRequestEnd("getMarketSkill", skill, System.currentTimeMillis() - startTime);
            return ResultModel.success(skill);
        } catch (Exception e) {
            logRequestError("getMarketSkill", e);
            return ResultModel.error(500, "获取市场技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/add")
    public ResultModel<Boolean> addMarketSkill(@RequestBody SkillListing listing) {
        long startTime = System.currentTimeMillis();
        logRequestStart("addMarketSkill", listing);

        try {
            boolean result = marketManager.updateSkill(listing);
            logRequestEnd("addMarketSkill", result, System.currentTimeMillis() - startTime);
            return ResultModel.success("添加市场技能成功", result);
        } catch (Exception e) {
            logRequestError("addMarketSkill", e);
            return ResultModel.error(500, "添加市场技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/{skillId}/update")
    public ResultModel<Boolean> updateMarketSkill(@PathVariable String skillId, @RequestBody SkillListing listing) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateMarketSkill", listing);

        try {
            listing.setSkillId(skillId);
            boolean result = marketManager.updateSkill(listing);
            logRequestEnd("updateMarketSkill", result, System.currentTimeMillis() - startTime);
            return ResultModel.success("更新市场技能成功", result);
        } catch (Exception e) {
            logRequestError("updateMarketSkill", e);
            return ResultModel.error(500, "更新市场技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/{skillId}/delete")
    public ResultModel<Boolean> removeMarketSkill(@PathVariable String skillId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("removeMarketSkill", skillId);

        try {
            boolean result = marketManager.removeSkill(skillId);
            if (!result) {
                logRequestEnd("removeMarketSkill", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("技能不存在");
            }
            logRequestEnd("removeMarketSkill", result, System.currentTimeMillis() - startTime);
            return ResultModel.success("移除市场技能成功", true);
        } catch (Exception e) {
            logRequestError("removeMarketSkill", e);
            return ResultModel.error(500, "从市场移除技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/category/{category}")
    public ResultModel<List<SkillListing>> getMarketSkillsByCategory(@PathVariable String category) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getMarketSkillsByCategory", category);

        try {
            List<SkillListing> marketSkills = marketManager.getSkillsByCategory(category);
            logRequestEnd("getMarketSkillsByCategory", marketSkills.size() + " skills", System.currentTimeMillis() - startTime);
            return ResultModel.success(marketSkills);
        } catch (Exception e) {
            logRequestError("getMarketSkillsByCategory", e);
            return ResultModel.error(500, "获取市场技能列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/popular")
    public ResultModel<List<SkillListing>> getPopularMarketSkills(@RequestBody PaginationDTO pagination) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getPopularMarketSkills", pagination);

        try {
            int limit = pagination.getPageSize();
            List<SkillListing> popularSkills = marketManager.getPopularSkills(limit);
            logRequestEnd("getPopularMarketSkills", popularSkills.size() + " skills", System.currentTimeMillis() - startTime);
            return ResultModel.success(popularSkills);
        } catch (Exception e) {
            logRequestError("getPopularMarketSkills", e);
            return ResultModel.error(500, "获取热门市场技能列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/latest")
    public ResultModel<List<SkillListing>> getLatestMarketSkills(@RequestBody PaginationDTO pagination) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getLatestMarketSkills", pagination);

        try {
            int limit = pagination.getPageSize();
            List<SkillListing> latestSkills = marketManager.getLatestSkills(limit);
            logRequestEnd("getLatestMarketSkills", latestSkills.size() + " skills", System.currentTimeMillis() - startTime);
            return ResultModel.success(latestSkills);
        } catch (Exception e) {
            logRequestError("getLatestMarketSkills", e);
            return ResultModel.error(500, "获取最新市场技能列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/download")
    public ResultModel<DownloadResultDTO> downloadSkill(@RequestBody DownloadRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("downloadSkill", request);

        try {
            String skillId = request.getId();
            if (skillId == null || skillId.isEmpty()) {
                return ResultModel.badRequest("技能ID不能为空");
            }

            byte[] skillData = marketManager.downloadSkill(skillId);

            DownloadResultDTO result = new DownloadResultDTO();
            result.setSuccess(true);
            result.setSkillId(skillId);
            result.setDataSize(skillData.length);
            result.setMessage("技能下载成功");

            logRequestEnd("downloadSkill", result, System.currentTimeMillis() - startTime);
            return ResultModel.success("技能下载成功", result);
        } catch (Exception e) {
            logRequestError("downloadSkill", e);
            return ResultModel.error(500, "技能下载失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/rate")
    public ResultModel<RateResultDTO> rateSkill(@RequestBody RateRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("rateSkill", request);

        try {
            String skillId = request.getId();
            if (skillId == null || skillId.isEmpty()) {
                return ResultModel.badRequest("技能ID不能为空");
            }

            double rating = request.getRating();
            String comment = request.getComment();
            String userId = request.getUserId();

            if (rating < 1 || rating > 5) {
                return ResultModel.badRequest("评分必须在1-5之间");
            }

            boolean success = marketManager.rateSkill(skillId, rating, comment, userId);

            RateResultDTO result = new RateResultDTO();
            result.setSuccess(success);
            result.setSkillId(skillId);
            result.setRating(rating);
            result.setMessage("技能评分成功");

            logRequestEnd("rateSkill", result, System.currentTimeMillis() - startTime);
            return ResultModel.success("技能评分成功", result);
        } catch (Exception e) {
            logRequestError("rateSkill", e);
            return ResultModel.error(500, "技能评分失败: " + e.getMessage());
        }
    }
}
