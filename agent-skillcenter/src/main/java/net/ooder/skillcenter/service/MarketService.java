package net.ooder.skillcenter.service;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.SkillDTO;
import net.ooder.skillcenter.dto.SkillReviewDTO;

import java.util.List;

/**
 * 市场服务接口
 */
public interface MarketService {

    // ==================== 技能列表 ====================
    PageResult<SkillDTO> getMarketSkills(int page, int size, String sortBy, String sortDirection);
    SkillDTO getSkillDetails(String skillId);
    PageResult<SkillDTO> searchSkills(String keyword, int page, int size, String sortBy, String sortDirection);

    // ==================== 分类管理 ====================
    List<String> getSkillCategories();
    PageResult<SkillDTO> getSkillsByCategory(String category, int page, int size, String sortBy, String sortDirection);

    // ==================== 热门和最新 ====================
    List<SkillDTO> getPopularSkills(int limit);
    List<SkillDTO> getLatestSkills(int limit);

    // ==================== 技能评价 ====================
    boolean rateSkill(String skillId, double rating, String comment, String userId);
    PageResult<SkillReviewDTO> getSkillReviews(String skillId, int page, int size, String sortBy, String sortDirection);

    // ==================== 技能下载 ====================
    byte[] downloadSkill(String skillId);

    // ==================== 技能发布管理 ====================
    boolean publishSkill(SkillDTO skill);
    boolean updateSkill(String skillId, SkillDTO skill);
    boolean deleteSkill(String skillId);
}
