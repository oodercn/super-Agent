package net.ooder.skillcenter.market;

import java.util.ArrayList;
import java.util.List;

/**
 * 技能评分信息，用于管理技能的评分和评价
 */
public class SkillRatingInfo {
    private String skillId;
    private List<SkillReview> reviews;
    private double totalRating;
    private int reviewCount;
    
    /**
     * 构造方法
     */
    public SkillRatingInfo() {
        this.reviews = new ArrayList<>();
        this.totalRating = 0.0;
        this.reviewCount = 0;
    }
    
    /**
     * 获取技能ID
     * @return 技能ID
     */
    public String getSkillId() {
        return skillId;
    }
    
    /**
     * 设置技能ID
     * @param skillId 技能ID
     */
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    /**
     * 获取评价列表
     * @return 评价列表
     */
    public List<SkillReview> getReviews() {
        return reviews;
    }
    
    /**
     * 设置评价列表
     * @param reviews 评价列表
     */
    public void setReviews(List<SkillReview> reviews) {
        this.reviews = reviews;
        // 更新统计信息
        updateStats();
    }
    
    /**
     * 添加评价
     * @param review 评价
     */
    public void addReview(SkillReview review) {
        reviews.add(review);
        totalRating += review.getRating();
        reviewCount++;
    }
    
    /**
     * 获取平均评分
     * @return 平均评分
     */
    public double getAverageRating() {
        if (reviewCount == 0) {
            return 0.0;
        }
        return totalRating / reviewCount;
    }
    
    /**
     * 获取评价次数
     * @return 评价次数
     */
    public int getReviewCount() {
        return reviewCount;
    }
    
    /**
     * 更新统计信息
     */
    private void updateStats() {
        totalRating = 0.0;
        reviewCount = reviews.size();
        
        for (SkillReview review : reviews) {
            totalRating += review.getRating();
        }
    }
}
