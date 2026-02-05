package net.ooder.skillcenter.market;

import java.util.List;
import java.util.Map;

/**
 * 技能存储接口，定义技能市场数据的持久化操作
 * 采用解耦设计，支持不同的存储实现
 */
public interface SkillStorage {
    
    /**
     * 存储技能列表项
     * @param listing 技能列表项
     */
    void saveSkillListing(SkillListing listing);
    
    /**
     * 批量存储技能列表项
     * @param listings 技能列表项列表
     */
    void saveSkillListings(List<SkillListing> listings);
    
    /**
     * 获取技能列表项
     * @param skillId 技能ID
     * @return 技能列表项
     */
    SkillListing getSkillListing(String skillId);
    
    /**
     * 获取所有技能列表项
     * @return 技能列表项映射，key为技能ID，value为技能列表项
     */
    Map<String, SkillListing> getAllSkillListings();
    
    /**
     * 根据分类获取技能列表项
     * @param category 分类名称
     * @return 技能列表项列表
     */
    List<SkillListing> getSkillListingsByCategory(String category);
    
    /**
     * 删除技能列表项
     * @param skillId 技能ID
     */
    void deleteSkillListing(String skillId);
    
    /**
     * 存储技能评分信息
     * @param ratingInfo 技能评分信息
     */
    void saveSkillRatingInfo(SkillRatingInfo ratingInfo);
    
    /**
     * 获取技能评分信息
     * @param skillId 技能ID
     * @return 技能评分信息
     */
    SkillRatingInfo getSkillRatingInfo(String skillId);
    
    /**
     * 删除技能评分信息
     * @param skillId 技能ID
     */
    void deleteSkillRatingInfo(String skillId);
    
    /**
     * 存储技能评价
     * @param review 技能评价
     */
    void saveSkillReview(SkillReview review);
    
    /**
     * 获取技能评价列表
     * @param skillId 技能ID
     * @return 技能评价列表
     */
    List<SkillReview> getSkillReviews(String skillId);
    
    /**
     * 初始化存储
     */
    void initialize();
    
    /**
     * 关闭存储
     */
    void close();
}
