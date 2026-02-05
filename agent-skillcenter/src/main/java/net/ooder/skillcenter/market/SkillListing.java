package net.ooder.skillcenter.market;

/**
 * 技能列表项，用于在技能市场中展示技能信息
 */
public class SkillListing {
    private String skillId;
    private String name;
    private String description;
    private String category;
    private String version;
    private String downloadUrl;
    private String author;
    private int downloadCount;
    private double rating;
    private int reviewCount;
    private long lastUpdated;
    
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
     * 获取技能名称
     * @return 技能名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 设置技能名称
     * @param name 技能名称
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 获取技能描述
     * @return 技能描述
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 设置技能描述
     * @param description 技能描述
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * 获取技能分类
     * @return 技能分类
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * 设置技能分类
     * @param category 技能分类
     */
    public void setCategory(String category) {
        this.category = category;
    }
    
    /**
     * 获取技能版本
     * @return 技能版本
     */
    public String getVersion() {
        return version;
    }
    
    /**
     * 设置技能版本
     * @param version 技能版本
     */
    public void setVersion(String version) {
        this.version = version;
    }
    
    /**
     * 获取下载URL
     * @return 下载URL
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }
    
    /**
     * 设置下载URL
     * @param downloadUrl 下载URL
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    
    /**
     * 获取作者
     * @return 作者
     */
    public String getAuthor() {
        return author;
    }
    
    /**
     * 设置作者
     * @param author 作者
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    
    /**
     * 获取下载次数
     * @return 下载次数
     */
    public int getDownloadCount() {
        return downloadCount;
    }
    
    /**
     * 设置下载次数
     * @param downloadCount 下载次数
     */
    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
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
     * 获取评价次数
     * @return 评价次数
     */
    public int getReviewCount() {
        return reviewCount;
    }
    
    /**
     * 设置评价次数
     * @param reviewCount 评价次数
     */
    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
    
    /**
     * 获取最后更新时间
     * @return 最后更新时间
     */
    public long getLastUpdated() {
        return lastUpdated;
    }
    
    /**
     * 设置最后更新时间
     * @param lastUpdated 最后更新时间
     */
    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}