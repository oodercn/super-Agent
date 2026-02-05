package net.ooder.skillcenter.market;

/**
 * 技能评价，用于表示用户对技能的评价
 */
public class SkillReview {
    private String skillId;
    private String userId;
    private double rating;
    private String comment;
    private long timestamp;
    
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
     * 获取用户ID
     * @return 用户ID
     */
    public String getUserId() {
        return userId;
    }
    
    /**
     * 设置用户ID
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    /**
     * 获取评分
     * @return 评分
     */
    public double getRating() {
        return rating;
    }
    
    /**
     * 设置评分
     * @param rating 评分
     */
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    /**
     * 获取评价内容
     * @return 评价内容
     */
    public String getComment() {
        return comment;
    }
    
    /**
     * 设置评价内容
     * @param comment 评价内容
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    /**
     * 获取评价时间戳
     * @return 评价时间戳
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * 设置评价时间戳
     * @param timestamp 评价时间戳
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}