/*
 * Copyright (c) 2024 Ooder Team
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */
package net.ooder.skillcenter.market;

import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Skill Market Manager - Core functionality for skill marketplace
 * Handles skill publishing, searching, rating, etc.
 */
public class SkillMarketManager {
    private static final Logger logger = LoggerFactory.getLogger(SkillMarketManager.class);

    // 单例实例
    private static SkillMarketManager instance;
    
    // 技能列表，key为技能ID，value为技能列表项
    private Map<String, SkillListing> skillListings;
    
    // 技能分类映射，key为分类名称，value为技能列表
    private Map<String, List<SkillListing>> categoryMap;
    
    // 技能评分映射，key为技能ID，value为技能评分信息
    private Map<String, SkillRatingInfo> skillRatings;
    
    // 技能存储接口
    private SkillStorage skillStorage;
    
    /**
     * 私有构造方法
     */
    private SkillMarketManager() {
        this.skillListings = new ConcurrentHashMap<>();
        this.categoryMap = new ConcurrentHashMap<>();
        this.skillRatings = new ConcurrentHashMap<>();
        
        // 初始化存储
        this.skillStorage = new SDKSkillStorage();
        this.skillStorage.initialize();
        
        // 从存储加载数据
        loadDataFromStorage();
        
        // 初始化内置技能市场数据
        initializeMarketData();
    }
    
    /**
     * 获取实例
     * @return 技能市场管理器实例
     */
    public static synchronized SkillMarketManager getInstance() {
        if (instance == null) {
            instance = new SkillMarketManager();
        }
        return instance;
    }
    
    /**
     * 从存储加载数据
     */
    private void loadDataFromStorage() {
        try {
            // 加载技能列表项
            Map<String, SkillListing> loadedListings = skillStorage.getAllSkillListings();
            if (loadedListings != null && !loadedListings.isEmpty()) {
                skillListings.putAll(loadedListings);
                
                // 更新分类映射
                for (SkillListing listing : loadedListings.values()) {
                    String category = listing.getCategory();
                    categoryMap.computeIfAbsent(category, k -> new java.util.ArrayList<>())
                              .add(listing);
                }
            }
            
            // 加载技能评分信息
            for (String skillId : skillListings.keySet()) {
                SkillRatingInfo ratingInfo = skillStorage.getSkillRatingInfo(skillId);
                if (ratingInfo != null) {
                    skillRatings.put(skillId, ratingInfo);
                }
            }
            
            logger.info("Loaded {} skills from storage", skillListings.size());
        } catch (Exception e) {
            logger.error("Failed to load data from storage: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 初始化市场数据
     */
    private void initializeMarketData() {
        // 仅在存储为空时初始化默认技能
        if (skillListings.isEmpty()) {
            addSkillListing(createMockSkillListing("code-generation-skill", "代码生成技能", 
                "生成各种编程语言的代码", "development", "1.0.0", "https://example.com/skills/code-generation"));
            
            addSkillListing(createMockSkillListing("text-to-uppercase-skill", "文本转大写技能", 
                "将文本转换为大写格式", "utilities", "1.0.0", "https://example.com/skills/text-to-uppercase"));
            
            addSkillListing(createMockSkillListing("media-streaming-skill", "媒体流技能", 
                "提供媒体流服务", "media", "1.0.0", "https://example.com/skills/media-streaming"));
            
            addSkillListing(createMockSkillListing("file-storage-skill", "文件存储技能", 
                "提供文件存储服务", "storage", "1.0.0", "https://example.com/skills/file-storage"));
            
            addSkillListing(createMockSkillListing("device-control-skill", "设备控制技能", 
                "控制智能设备", "iot", "1.0.0", "https://example.com/skills/device-control"));
            
            // 保存初始数据
            for (SkillListing listing : skillListings.values()) {
                skillStorage.saveSkillListing(listing);
            }
            
            logger.info("Initialized 5 default skills");
        } else {
            logger.info("Loaded {} existing skills from storage, skipping initialization", skillListings.size());
        }
    }
    
    /**
     * 生成测试技能数据
     */
    private void generateTestSkills() {
        String[] categories = {"development", "utilities", "media", "storage", "iot", "ai", "finance", "education", "health", "entertainment"};
        String[] skillNames = {
            "代码生成", "文本处理", "媒体转换", "文件管理", "设备控制", "智能分析", "金融计算", "在线教育", "健康监测", "游戏娱乐",
            "数据可视化", "网络工具", "安全防护", "办公助手", "智能家居", "语音识别", "图像处理", "视频编辑", "音乐创作", "内容创作"
        };
        String[] skillDescriptions = {
            "专业的代码生成工具，支持多种编程语言",
            "强大的文本处理工具，支持各种文本操作",
            "高效的媒体转换工具，支持多种格式",
            "安全可靠的文件管理工具",
            "智能设备控制工具，支持多种设备",
            "先进的智能分析工具，提供数据分析",
            "专业的金融计算工具，支持各种金融运算",
            "丰富的在线教育资源",
            "精准的健康监测工具",
            "有趣的游戏娱乐内容",
            "直观的数据可视化工具",
            "实用的网络工具集合",
            "全面的安全防护工具",
            "便捷的办公助手",
            "智能的家居控制工具",
            "准确的语音识别工具",
            "专业的图像处理工具",
            "强大的视频编辑工具",
            "创意的音乐创作工具",
            "高效的内容创作工具"
        };
        String[] authors = {
            "Ooder Team", "Skill Center", "Tech Experts", "Dev Team", "AI Lab", "Media Studio", "Finance Pro", "Educators", "Health Experts", "Entertainment Hub"
        };
        
        java.util.Random random = new java.util.Random();
        
        // 生成50个测试技能
        for (int i = 1; i <= 50; i++) {
            String category = categories[random.nextInt(categories.length)];
            String name = skillNames[random.nextInt(skillNames.length)];
            String description = skillDescriptions[random.nextInt(skillDescriptions.length)];
            String author = authors[random.nextInt(authors.length)];
            
            String skillId = "skill-test-" + System.currentTimeMillis() + "-" + i;
            String skillName = name + " 技能 v" + (random.nextInt(3) + 1) + "." + (random.nextInt(10) + 1);
            String version = (random.nextInt(3) + 1) + "." + (random.nextInt(10) + 1) + "." + (random.nextInt(10) + 1);
            String downloadUrl = "https://example.com/skills/" + category + "/" + name.replace(" ", "-").toLowerCase();
            
            addSkillListing(createMockSkillListing(skillId, skillName, description, category, version, downloadUrl));
            
            // 为每个技能生成一些评分和评价
            generateSkillReviews(skillId, random.nextInt(5) + 1);
        }
        
        logger.info("Generated 50 test skills with reviews");
    }
    
    /**
     * 为技能生成评分和评价
     */
    private void generateSkillReviews(String skillId, int reviewCount) {
        String[] userIds = {"user1", "user2", "user3", "user4", "user5", "user6", "user7", "user8", "user9", "user10"};
        String[] reviewComments = {
            "非常好用的工具！",
            "功能强大，推荐使用",
            "界面友好，操作简单",
            "效果超出预期",
            "值得购买的优质技能",
            "使用体验很棒",
            "解决了我的实际问题",
            "性能稳定，响应迅速",
            "客服态度很好",
            "持续更新，不断改进"
        };
        
        java.util.Random random = new java.util.Random();
        
        for (int i = 0; i < reviewCount; i++) {
            double rating = random.nextDouble() * 2 + 3; // 3-5
            String comment = reviewComments[random.nextInt(reviewComments.length)];
            String userId = userIds[random.nextInt(userIds.length)];
            
            try {
                rateSkill(skillId, rating, comment, userId);
            } catch (Exception e) {
                logger.error("Failed to generate review for skill {}: {}", skillId, e.getMessage());
            }
        }
    }
    
    /**
     * 创建模拟技能列表项 - 符合v0.7.0协议规范
     */
    private SkillListing createMockSkillListing(String id, String name, String description, 
                                               String category, String version, String url) {
        SkillListing listing = new SkillListing();
        listing.setSkillId(id);
        listing.setName(name);
        listing.setDescription(description);
        listing.setCategory(category);
        listing.setVersion(version);
        listing.setDownloadUrl(url);
        listing.setAuthor("Ooder Team");
        listing.setDownloadCount(1000);
        listing.setRating(4.5);
        listing.setReviewCount(50);
        listing.setLastUpdated(System.currentTimeMillis());
        
        String type = inferSkillType(category);
        listing.setType(type);
        listing.setCapabilities(generateCapabilities(category));
        listing.setScenes(generateScenes(type));
        listing.setEndpoint("https://skillcenter.ooder.net/skills/" + id);
        listing.setHomepage("https://github.com/ooder/" + id);
        listing.setRepository("https://github.com/ooder/" + id + ".git");
        listing.setLicense("Apache-2.0");
        
        return listing;
    }
    
    private String inferSkillType(String category) {
        if ("development".equals(category) || "ai".equals(category)) {
            return "tool-skill";
        } else if ("iot".equals(category) || "storage".equals(category)) {
            return "infrastructure-skill";
        } else if ("finance".equals(category) || "education".equals(category) || "health".equals(category)) {
            return "enterprise-skill";
        }
        return "tool-skill";
    }
    
    private java.util.List<String> generateCapabilities(String category) {
        java.util.List<String> caps = new java.util.ArrayList<>();
        if ("development".equals(category)) {
            caps.add("code-generation");
            caps.add("code-analysis");
        } else if ("ai".equals(category)) {
            caps.add("data-analysis");
            caps.add("prediction");
        } else if ("iot".equals(category)) {
            caps.add("device-control");
            caps.add("data-collection");
        } else if ("storage".equals(category)) {
            caps.add("file-read");
            caps.add("file-write");
        } else if ("finance".equals(category)) {
            caps.add("org-data-read");
            caps.add("user-auth");
        } else {
            caps.add("data-processing");
        }
        return caps;
    }
    
    private java.util.List<String> generateScenes(String type) {
        java.util.List<String> scenes = new java.util.ArrayList<>();
        if ("enterprise-skill".equals(type)) {
            scenes.add("auth");
            scenes.add("messaging");
        } else if ("infrastructure-skill".equals(type)) {
            scenes.add("storage");
            scenes.add("network");
        } else {
            scenes.add("processing");
        }
        return scenes;
    }
    
    /**
     * 发布技能到市场
     * @param skill 技能实例
     * @param listing 技能列表项
     * @return 发布结果
     * @throws SkillException 发布异常
     */
    public boolean publishSkill(Skill skill, SkillListing listing) throws SkillException {
        if (skill == null || listing == null) {
            throw new SkillException("unknown", "Skill and listing cannot be null", 
                                     SkillException.ErrorCode.PARAMETER_ERROR);
        }
        
        if (skillListings.containsKey(listing.getSkillId())) {
            throw new SkillException(listing.getSkillId(), "Skill already exists in market", 
                                     SkillException.ErrorCode.EXECUTION_EXCEPTION);
        }
        
        // 添加技能列表项
        addSkillListing(listing);
        
        // 保存到存储
        skillStorage.saveSkillListing(listing);
        
        // 初始化技能评分信息
        SkillRatingInfo ratingInfo = new SkillRatingInfo();
        ratingInfo.setSkillId(listing.getSkillId());
        skillRatings.put(listing.getSkillId(), ratingInfo);
        
        // 保存评分信息到存储
        skillStorage.saveSkillRatingInfo(ratingInfo);
        
        return true;
    }
    
    /**
     * 更新技能列表项
     * @param listing 技能列表项
     * @return 更新结果
     * @throws SkillException 更新异常
     */
    public boolean updateSkill(SkillListing listing) throws SkillException {
        if (listing == null) {
            throw new SkillException("unknown", "Listing cannot be null", 
                                     SkillException.ErrorCode.PARAMETER_ERROR);
        }
        
        if (!skillListings.containsKey(listing.getSkillId())) {
            throw new SkillException(listing.getSkillId(), "Skill not found in market", 
                                     SkillException.ErrorCode.SKILL_NOT_FOUND);
        }
        
        // 更新技能列表项
        addSkillListing(listing);
        
        // 保存到存储
        skillStorage.saveSkillListing(listing);
        
        return true;
    }
    
    /**
     * 从市场中移除技能
     * @param skillId 技能ID
     * @return 移除结果
     */
    public boolean removeSkill(String skillId) {
        if (skillId == null || skillId.isEmpty()) {
            return false;
        }
        
        // 移除技能列表项
        SkillListing listing = skillListings.remove(skillId);
        if (listing != null) {
            // 从分类映射中移除
            List<SkillListing> listings = categoryMap.get(listing.getCategory());
            if (listings != null) {
                listings.remove(listing);
                if (listings.isEmpty()) {
                    categoryMap.remove(listing.getCategory());
                }
            }
            
            // 移除技能评分信息
            skillRatings.remove(skillId);
            
            // 从存储中删除
            skillStorage.deleteSkillListing(skillId);
            skillStorage.deleteSkillRatingInfo(skillId);
            
            return true;
        }
        
        return false;
    }
    
    /**
     * 添加技能列表项
     */
    private void addSkillListing(SkillListing listing) {
        skillListings.put(listing.getSkillId(), listing);
        
        // 添加到分类映射
        categoryMap.computeIfAbsent(listing.getCategory(), k -> new java.util.ArrayList<>())
                  .add(listing);
    }
    
    /**
     * 公共方法：添加技能列表项
     * @param listing 技能列表项
     */
    public void addSkill(SkillListing listing) {
        addSkillListing(listing);
        // 保存到存储
        skillStorage.saveSkillListing(listing);
        
        // 初始化技能评分信息
        SkillRatingInfo ratingInfo = new SkillRatingInfo();
        ratingInfo.setSkillId(listing.getSkillId());
        skillRatings.put(listing.getSkillId(), ratingInfo);
        
        // 保存评分信息到存储
        skillStorage.saveSkillRatingInfo(ratingInfo);
    }
    
    /**
     * 搜索技能
     * @param keyword 关键词
     * @return 匹配的技能列表
     */
    public List<SkillListing> searchSkills(String keyword) {
        List<SkillListing> results = new java.util.ArrayList<>();
        
        if (keyword == null || keyword.isEmpty()) {
            return results;
        }
        
        String lowerKeyword = keyword.toLowerCase();
        
        for (SkillListing listing : skillListings.values()) {
            if (listing.getName().toLowerCase().contains(lowerKeyword) ||
                listing.getDescription().toLowerCase().contains(lowerKeyword) ||
                listing.getCategory().toLowerCase().contains(lowerKeyword)) {
                results.add(listing);
            }
        }
        
        return results;
    }
    
    /**
     * 根据分类获取技能
     * @param category 分类名称
     * @return 技能列表
     */
    public List<SkillListing> getSkillsByCategory(String category) {
        if (category == null || category.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        
        return categoryMap.getOrDefault(category, new java.util.ArrayList<>());
    }
    
    /**
     * 获取所有技能
     * @return 技能列表
     */
    public List<SkillListing> getAllSkills() {
        return new java.util.ArrayList<>(skillListings.values());
    }
    
    /**
     * 获取技能总数
     * @return 技能总数
     */
    public int getSkillCount() {
        return skillListings.size();
    }
    
    /**
     * 获取技能详情
     * @param skillId 技能ID
     * @return 技能列表项
     */
    public SkillListing getSkillListing(String skillId) {
        return skillListings.get(skillId);
    }
    
    /**
     * 下载技能
     * @param skillId 技能ID
     * @return 下载结果
     * @throws SkillException 下载异常
     */
    public byte[] downloadSkill(String skillId) throws SkillException {
        SkillListing listing = skillListings.get(skillId);
        if (listing == null) {
            throw new SkillException(skillId, "Skill not found in market", 
                                     SkillException.ErrorCode.SKILL_NOT_FOUND);
        }
        
        // 实际项目中，这里应该从下载URL获取技能数据
        // 这里模拟下载
        logger.info("Downloading skill: {} from {}", skillId, listing.getDownloadUrl());
        
        // 增加下载计数
        listing.setDownloadCount(listing.getDownloadCount() + 1);
        
        // 模拟返回技能数据
        return new byte[0];
    }
    
    /**
     * 评价技能
     * @param skillId 技能ID
     * @param rating 评分（1-5）
     * @param comment 评价内容
     * @param userId 用户ID
     * @return 评价结果
     * @throws SkillException 评价异常
     */
    public boolean rateSkill(String skillId, double rating, String comment, String userId) throws SkillException {
        if (skillListings.containsKey(skillId)) {
            SkillRatingInfo ratingInfo = skillRatings.computeIfAbsent(skillId, k -> new SkillRatingInfo());
            ratingInfo.setSkillId(skillId);
            
            // 添加评价
            SkillReview review = new SkillReview();
            review.setSkillId(skillId);
            review.setUserId(userId);
            review.setRating(rating);
            review.setComment(comment);
            review.setTimestamp(System.currentTimeMillis());
            
            ratingInfo.addReview(review);
            
            // 更新技能列表项的评分
            SkillListing listing = skillListings.get(skillId);
            listing.setRating(ratingInfo.getAverageRating());
            listing.setReviewCount(ratingInfo.getReviewCount());
            
            // 保存到存储
            skillStorage.saveSkillReview(review);
            skillStorage.saveSkillRatingInfo(ratingInfo);
            skillStorage.saveSkillListing(listing);
            
            return true;
        }
        
        throw new SkillException(skillId, "Skill not found in market", 
                                 SkillException.ErrorCode.SKILL_NOT_FOUND);
    }
    
    /**
     * 获取技能的评价
     * @param skillId 技能ID
     * @return 评价列表
     */
    public List<SkillReview> getSkillReviews(String skillId) {
        // 首先从内存中获取
        SkillRatingInfo ratingInfo = skillRatings.get(skillId);
        if (ratingInfo != null) {
            return ratingInfo.getReviews();
        }
        
        // 从存储中获取
        return skillStorage.getSkillReviews(skillId);
    }
    
    /**
     * 获取技能分类列表
     * @return 分类列表
     */
    public List<String> getCategories() {
        return new java.util.ArrayList<>(categoryMap.keySet());
    }
    
    /**
     * 获取热门技能
     * @param limit 限制数量
     * @return 热门技能列表
     */
    public List<SkillListing> getPopularSkills(int limit) {
        List<SkillListing> skills = new java.util.ArrayList<>(skillListings.values());
        
        // 按下载次数排序
        skills.sort((a, b) -> Integer.compare(b.getDownloadCount(), a.getDownloadCount()));
        
        // 限制数量
        if (skills.size() > limit) {
            skills = skills.subList(0, limit);
        }
        
        return skills;
    }
    
    /**
     * 获取最新技能
     * @param limit 限制数量
     * @return 最新技能列表
     */
    public List<SkillListing> getLatestSkills(int limit) {
        List<SkillListing> skills = new java.util.ArrayList<>(skillListings.values());
        
        // 按最后更新时间排序
        skills.sort((a, b) -> Long.compare(b.getLastUpdated(), a.getLastUpdated()));
        
        // 限制数量
        if (skills.size() > limit) {
            skills = skills.subList(0, limit);
        }
        
        return skills;
    }
    
    /**
     * 清理市场数据
     */
    public void clear() {
        skillListings.clear();
        categoryMap.clear();
        skillRatings.clear();
        
        // 清理存储
        try {
            Path storageDir = Paths.get(System.getProperty("user.dir"), "skillcenter", "storage");
            Files.walk(storageDir)
                 .filter(Files::isRegularFile)
                 .forEach(file -> {
                     try {
                         Files.delete(file);
                     } catch (IOException e) {
                         logger.error("Failed to delete file: {}: {}", file, e.getMessage());
                     }
                 });
        } catch (IOException e) {
            logger.error("Failed to clear storage: {}", e.getMessage());
        }
    }
    
    /**
     * 关闭存储
     */
    public void close() {
        if (skillStorage != null) {
            skillStorage.close();
        }
    }
}
