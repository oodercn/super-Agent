package net.ooder.skillcenter.test;

import net.ooder.skillcenter.market.SkillListing;
import net.ooder.skillcenter.market.SkillMarketManager;
import net.ooder.skillcenter.market.SkillRatingInfo;
import net.ooder.skillcenter.market.SkillReview;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 测试数据生成器，用于生成技能市场的测试数据
 */
public class TestDataGenerator {
    
    private static final Random RANDOM = new Random();
    private static final String[] CATEGORIES = {
        "development", "utilities", "media", "storage", "iot", "ai", "finance", "education", "health", "entertainment"
    };
    
    private static final String[] SKILL_NAMES = {
        "代码生成", "文本处理", "媒体转换", "文件管理", "设备控制", "智能分析", "金融计算", "在线教育", "健康监测", "游戏娱乐",
        "数据可视化", "网络工具", "安全防护", "办公助手", "智能家居", "语音识别", "图像处理", "视频编辑", "音乐创作", "内容创作"
    };
    
    private static final String[] SKILL_DESCRIPTIONS = {
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
    
    private static final String[] AUTHORS = {
        "Ooder Team", "Skill Center", "Tech Experts", "Dev Team", "AI Lab", "Media Studio", "Finance Pro", "Educators", "Health Experts", "Entertainment Hub"
    };
    
    private static final String[] USER_IDS = {
        "user1", "user2", "user3", "user4", "user5", "user6", "user7", "user8", "user9", "user10"
    };
    
    private static final String[] REVIEW_COMMENTS = {
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
    
    /**
     * 生成测试技能数据
     * @param count 生成的技能数量
     */
    public static void generateTestSkills(int count) {
        SkillMarketManager marketManager = SkillMarketManager.getInstance();
        
        System.out.println("开始生成 " + count + " 个测试技能...");
        
        List<SkillListing> generatedSkills = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            SkillListing skill = generateSkillListing(i + 1);
            generatedSkills.add(skill);
            
            // 生成评分和评价
            generateSkillRatingsAndReviews(skill.getSkillId(), RANDOM.nextInt(5) + 1);
            
            System.out.println("生成技能: " + skill.getName() + " (" + skill.getCategory() + ")");
        }
        
        System.out.println("测试数据生成完成！");
        System.out.println("共生成 " + generatedSkills.size() + " 个技能");
        
        // 统计各分类的技能数量
        System.out.println("\n技能分类统计:");
        for (String category : CATEGORIES) {
            long countByCategory = generatedSkills.stream()
                    .filter(skill -> category.equals(skill.getCategory()))
                    .count();
            if (countByCategory > 0) {
                System.out.println(category + ": " + countByCategory + " 个");
            }
        }
    }
    
    /**
     * 生成单个技能列表项
     * @param index 技能索引
     * @return 技能列表项
     */
    private static SkillListing generateSkillListing(int index) {
        String category = CATEGORIES[RANDOM.nextInt(CATEGORIES.length)];
        String name = SKILL_NAMES[RANDOM.nextInt(SKILL_NAMES.length)];
        String description = SKILL_DESCRIPTIONS[RANDOM.nextInt(SKILL_DESCRIPTIONS.length)];
        String author = AUTHORS[RANDOM.nextInt(AUTHORS.length)];
        
        SkillListing listing = new SkillListing();
        listing.setSkillId("skill-" + System.currentTimeMillis() + "-" + index);
        listing.setName(name + " 技能 v" + (RANDOM.nextInt(3) + 1) + "." + (RANDOM.nextInt(10) + 1));
        listing.setDescription(description);
        listing.setCategory(category);
        listing.setVersion((RANDOM.nextInt(3) + 1) + "." + (RANDOM.nextInt(10) + 1) + "." + (RANDOM.nextInt(10) + 1));
        listing.setDownloadUrl("https://example.com/skills/" + category + "/" + name.replace(" ", "-").toLowerCase());
        listing.setAuthor(author);
        listing.setDownloadCount(RANDOM.nextInt(10000) + 100);
        listing.setRating(RANDOM.nextDouble() * 1.5 + 3.5); // 3.5-5.0
        listing.setReviewCount(RANDOM.nextInt(1000) + 10);
        listing.setLastUpdated(System.currentTimeMillis() - RANDOM.nextInt(30 * 24 * 60 * 60 * 1000)); // 最近30天内
        
        // 保存到市场
        SkillMarketManager.getInstance().addSkill(listing);
        
        return listing;
    }
    
    /**
     * 为技能生成评分和评价
     * @param skillId 技能ID
     * @param reviewCount 评价数量
     */
    private static void generateSkillRatingsAndReviews(String skillId, int reviewCount) {
        SkillMarketManager marketManager = SkillMarketManager.getInstance();
        
        for (int i = 0; i < reviewCount; i++) {
            double rating = RANDOM.nextDouble() * 2 + 3; // 3-5
            String comment = REVIEW_COMMENTS[RANDOM.nextInt(REVIEW_COMMENTS.length)];
            String userId = USER_IDS[RANDOM.nextInt(USER_IDS.length)];
            
            try {
                marketManager.rateSkill(skillId, rating, comment, userId);
            } catch (Exception e) {
                System.err.println("生成评价失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * 主方法，用于运行测试数据生成
     */
    public static void main(String[] args) {
        // 生成50个测试技能
        generateTestSkills(50);
    }
}
