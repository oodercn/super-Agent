package net.ooder.skillcenter.market;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

public class SDKSkillStorageTest {
    
    private SkillStorage skillStorage;
    
    @Before
    public void setUp() {
        // 初始化存储
        skillStorage = new SDKSkillStorage();
        skillStorage.initialize();
    }
    
    @After
    public void tearDown() {
        // 关闭存储
        if (skillStorage != null) {
            skillStorage.close();
        }
        
        // 清理测试数据
        try {
            java.nio.file.Path storageDir = java.nio.file.Paths.get(System.getProperty("user.dir"), "skillcenter", "storage");
            java.nio.file.Files.walk(storageDir)
                 .filter(java.nio.file.Files::isRegularFile)
                 .forEach(file -> {
                     try {
                         java.nio.file.Files.delete(file);
                     } catch (java.io.IOException e) {
                         System.err.println("Failed to delete file: " + file + ": " + e.getMessage());
                     }
                 });
        } catch (java.io.IOException e) {
            System.err.println("Failed to clear storage: " + e.getMessage());
        }
    }
    
    @Test
    public void testSaveAndGetSkillListing() {
        // 创建测试技能列表项
        SkillListing listing = new SkillListing();
        listing.setSkillId("test-skill-1");
        listing.setName("测试技能");
        listing.setDescription("这是一个测试技能");
        listing.setCategory("test");
        listing.setVersion("1.0.0");
        listing.setDownloadUrl("https://example.com/test-skill");
        listing.setAuthor("Test Author");
        listing.setDownloadCount(0);
        listing.setRating(0.0);
        listing.setReviewCount(0);
        listing.setLastUpdated(System.currentTimeMillis());
        
        // 保存技能列表项
        skillStorage.saveSkillListing(listing);
        
        // 获取技能列表项
        SkillListing retrievedListing = skillStorage.getSkillListing("test-skill-1");
        assertNotNull("技能列表项应该被成功保存和检索", retrievedListing);
        assertEquals("技能ID应该匹配", "test-skill-1", retrievedListing.getSkillId());
        assertEquals("技能名称应该匹配", "测试技能", retrievedListing.getName());
        assertEquals("技能描述应该匹配", "这是一个测试技能", retrievedListing.getDescription());
        assertEquals("技能分类应该匹配", "test", retrievedListing.getCategory());
    }
    
    @Test
    public void testSaveAndGetSkillListings() {
        // 创建多个测试技能列表项
        SkillListing listing1 = new SkillListing();
        listing1.setSkillId("test-skill-1");
        listing1.setName("测试技能1");
        listing1.setCategory("test");
        
        SkillListing listing2 = new SkillListing();
        listing2.setSkillId("test-skill-2");
        listing2.setName("测试技能2");
        listing2.setCategory("test");
        
        // 保存技能列表项
        java.util.List<SkillListing> listings = new java.util.ArrayList<>();
        listings.add(listing1);
        listings.add(listing2);
        skillStorage.saveSkillListings(listings);
        
        // 获取所有技能列表项
        Map<String, SkillListing> retrievedListings = skillStorage.getAllSkillListings();
        assertNotNull("技能列表项映射应该不为空", retrievedListings);
        assertEquals("应该有2个技能列表项", 2, retrievedListings.size());
        assertTrue("应该包含test-skill-1", retrievedListings.containsKey("test-skill-1"));
        assertTrue("应该包含test-skill-2", retrievedListings.containsKey("test-skill-2"));
    }
    
    @Test
    public void testGetSkillListingsByCategory() {
        // 创建不同分类的测试技能列表项
        SkillListing listing1 = new SkillListing();
        listing1.setSkillId("test-skill-1");
        listing1.setName("测试技能1");
        listing1.setCategory("test");
        
        SkillListing listing2 = new SkillListing();
        listing2.setSkillId("test-skill-2");
        listing2.setName("测试技能2");
        listing2.setCategory("test");
        
        SkillListing listing3 = new SkillListing();
        listing3.setSkillId("dev-skill-1");
        listing3.setName("开发技能1");
        listing3.setCategory("development");
        
        // 保存技能列表项
        skillStorage.saveSkillListing(listing1);
        skillStorage.saveSkillListing(listing2);
        skillStorage.saveSkillListing(listing3);
        
        // 按分类获取技能列表项
        List<SkillListing> testCategoryListings = skillStorage.getSkillListingsByCategory("test");
        assertNotNull("test分类的技能列表项应该不为空", testCategoryListings);
        assertEquals("test分类应该有2个技能列表项", 2, testCategoryListings.size());
        
        List<SkillListing> devCategoryListings = skillStorage.getSkillListingsByCategory("development");
        assertNotNull("development分类的技能列表项应该不为空", devCategoryListings);
        assertEquals("development分类应该有1个技能列表项", 1, devCategoryListings.size());
    }
    
    @Test
    public void testDeleteSkillListing() {
        // 创建测试技能列表项
        SkillListing listing = new SkillListing();
        listing.setSkillId("test-skill-1");
        listing.setName("测试技能");
        listing.setCategory("test");
        
        // 保存技能列表项
        skillStorage.saveSkillListing(listing);
        
        // 验证技能列表项已保存
        assertNotNull("技能列表项应该被成功保存", skillStorage.getSkillListing("test-skill-1"));
        
        // 删除技能列表项
        skillStorage.deleteSkillListing("test-skill-1");
        
        // 验证技能列表项已删除
        assertNull("技能列表项应该被成功删除", skillStorage.getSkillListing("test-skill-1"));
    }
    
    @Test
    public void testSaveAndGetSkillRatingInfo() {
        // 创建测试技能评分信息
        SkillRatingInfo ratingInfo = new SkillRatingInfo();
        ratingInfo.setSkillId("test-skill-1");
        
        // 添加评价
        SkillReview review = new SkillReview();
        review.setSkillId("test-skill-1");
        review.setUserId("test-user-1");
        review.setRating(4.5);
        review.setComment("这是一个很好的技能");
        review.setTimestamp(System.currentTimeMillis());
        ratingInfo.addReview(review);
        
        // 保存技能评分信息
        skillStorage.saveSkillRatingInfo(ratingInfo);
        
        // 获取技能评分信息
        SkillRatingInfo retrievedRatingInfo = skillStorage.getSkillRatingInfo("test-skill-1");
        assertNotNull("技能评分信息应该被成功保存和检索", retrievedRatingInfo);
        assertEquals("技能ID应该匹配", "test-skill-1", retrievedRatingInfo.getSkillId());
        assertEquals("评价数量应该匹配", 1, retrievedRatingInfo.getReviewCount());
        assertEquals("平均评分应该匹配", 4.5, retrievedRatingInfo.getAverageRating(), 0.01);
    }
    
    @Test
    public void testSaveAndGetSkillReview() {
        // 创建测试技能评价
        SkillReview review = new SkillReview();
        review.setSkillId("test-skill-1");
        review.setUserId("test-user-1");
        review.setRating(4.5);
        review.setComment("这是一个很好的技能");
        review.setTimestamp(System.currentTimeMillis());
        
        // 保存技能评价
        skillStorage.saveSkillReview(review);
        
        // 获取技能评价
        List<SkillReview> retrievedReviews = skillStorage.getSkillReviews("test-skill-1");
        assertNotNull("技能评价应该被成功保存和检索", retrievedReviews);
        assertEquals("评价数量应该匹配", 1, retrievedReviews.size());
        
        SkillReview retrievedReview = retrievedReviews.get(0);
        assertEquals("技能ID应该匹配", "test-skill-1", retrievedReview.getSkillId());
        assertEquals("用户ID应该匹配", "test-user-1", retrievedReview.getUserId());
        assertEquals("评分应该匹配", 4.5, retrievedReview.getRating(), 0.01);
        assertEquals("评价内容应该匹配", "这是一个很好的技能", retrievedReview.getComment());
    }
    
    @Test
    public void testPersistence() {
        // 创建测试技能列表项
        SkillListing listing = new SkillListing();
        listing.setSkillId("persistence-test-skill");
        listing.setName("持久化测试技能");
        listing.setDescription("这是一个持久化测试技能");
        listing.setCategory("test");
        
        // 保存技能列表项
        skillStorage.saveSkillListing(listing);
        
        // 关闭存储
        skillStorage.close();
        
        // 重新初始化存储
        skillStorage = new SDKSkillStorage();
        skillStorage.initialize();
        
        // 获取技能列表项
        SkillListing retrievedListing = skillStorage.getSkillListing("persistence-test-skill");
        assertNotNull("技能列表项应该在重新初始化后仍然存在", retrievedListing);
        assertEquals("技能ID应该匹配", "persistence-test-skill", retrievedListing.getSkillId());
        assertEquals("技能名称应该匹配", "持久化测试技能", retrievedListing.getName());
        assertEquals("技能描述应该匹配", "这是一个持久化测试技能", retrievedListing.getDescription());
        assertEquals("技能分类应该匹配", "test", retrievedListing.getCategory());
    }
}
