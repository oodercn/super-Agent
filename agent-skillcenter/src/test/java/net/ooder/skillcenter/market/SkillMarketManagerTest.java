package net.ooder.skillcenter.market;

import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillException;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class SkillMarketManagerTest {
    
    private SkillMarketManager marketManager;
    
    @Before
    public void setUp() {
        // 初始化技能市场管理器
        marketManager = SkillMarketManager.getInstance();
        // 清理市场数据
        marketManager.clear();
    }
    
    @After
    public void tearDown() {
        // 清理市场数据
        marketManager.clear();
        // 关闭存储
        marketManager.close();
    }
    
    @Test
    public void testPublishSkill() throws SkillException {
        // 创建测试技能
        Skill skill = new Skill() {
            @Override
            public String getId() {
                return "test-skill-1";
            }
            
            @Override
            public String getName() {
                return "测试技能";
            }
            
            @Override
            public String getDescription() {
                return "这是一个测试技能";
            }
            
            @Override
            public boolean isAvailable() {
                return true;
            }
            
            @Override
            public java.util.Map<String, net.ooder.skillcenter.model.SkillParam> getParams() {
                return new java.util.HashMap<>();
            }
            
            @Override
            public net.ooder.skillcenter.model.SkillResult execute(net.ooder.skillcenter.model.SkillContext context) throws net.ooder.skillcenter.model.SkillException {
                net.ooder.skillcenter.model.SkillResult result = new net.ooder.skillcenter.model.SkillResult();
                result.setMessage("Skill executed successfully");
                return result;
            }
        };
        
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
        
        // 发布技能
        boolean result = marketManager.publishSkill(skill, listing);
        assertTrue("技能应该被成功发布", result);
        
        // 验证技能已发布
        SkillListing retrievedListing = marketManager.getSkillListing("test-skill-1");
        assertNotNull("技能列表项应该被成功保存和检索", retrievedListing);
        assertEquals("技能ID应该匹配", "test-skill-1", retrievedListing.getSkillId());
        assertEquals("技能名称应该匹配", "测试技能", retrievedListing.getName());
    }
    
    @Test
    public void testUpdateSkill() throws SkillException {
        // 先发布一个技能
        Skill skill = new Skill() {
            @Override
            public String getId() {
                return "test-skill-1";
            }
            
            @Override
            public String getName() {
                return "测试技能";
            }
            
            @Override
            public String getDescription() {
                return "这是一个测试技能";
            }
            
            @Override
            public boolean isAvailable() {
                return true;
            }
            
            @Override
            public java.util.Map<String, net.ooder.skillcenter.model.SkillParam> getParams() {
                return new java.util.HashMap<>();
            }
            
            @Override
            public net.ooder.skillcenter.model.SkillResult execute(net.ooder.skillcenter.model.SkillContext context) throws net.ooder.skillcenter.model.SkillException {
                net.ooder.skillcenter.model.SkillResult result = new net.ooder.skillcenter.model.SkillResult();
                result.setMessage("Skill executed successfully");
                return result;
            }
        };
        
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
        
        marketManager.publishSkill(skill, listing);
        
        // 更新技能
        listing.setName("更新后的测试技能");
        listing.setVersion("1.1.0");
        listing.setDescription("这是一个更新后的测试技能");
        
        boolean updateResult = marketManager.updateSkill(listing);
        assertTrue("技能应该被成功更新", updateResult);
        
        // 验证技能已更新
        SkillListing updatedListing = marketManager.getSkillListing("test-skill-1");
        assertNotNull("更新后的技能列表项应该被成功检索", updatedListing);
        assertEquals("技能名称应该被更新", "更新后的测试技能", updatedListing.getName());
        assertEquals("技能版本应该被更新", "1.1.0", updatedListing.getVersion());
        assertEquals("技能描述应该被更新", "这是一个更新后的测试技能", updatedListing.getDescription());
    }
    
    @Test
    public void testRemoveSkill() throws SkillException {
        // 先发布一个技能
        Skill skill = new Skill() {
            @Override
            public String getId() {
                return "test-skill-1";
            }
            
            @Override
            public String getName() {
                return "测试技能";
            }
            
            @Override
            public String getDescription() {
                return "这是一个测试技能";
            }
            
            @Override
            public boolean isAvailable() {
                return true;
            }
            
            @Override
            public java.util.Map<String, net.ooder.skillcenter.model.SkillParam> getParams() {
                return new java.util.HashMap<>();
            }
            
            @Override
            public net.ooder.skillcenter.model.SkillResult execute(net.ooder.skillcenter.model.SkillContext context) throws net.ooder.skillcenter.model.SkillException {
                net.ooder.skillcenter.model.SkillResult result = new net.ooder.skillcenter.model.SkillResult();
                result.setMessage("Skill executed successfully");
                return result;
            }
        };
        
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
        
        marketManager.publishSkill(skill, listing);
        
        // 验证技能已发布
        assertNotNull("技能列表项应该被成功保存和检索", marketManager.getSkillListing("test-skill-1"));
        
        // 移除技能
        boolean removeResult = marketManager.removeSkill("test-skill-1");
        assertTrue("技能应该被成功移除", removeResult);
        
        // 验证技能已移除
        assertNull("技能列表项应该被成功移除", marketManager.getSkillListing("test-skill-1"));
    }
    
    @Test
    public void testSearchSkills() throws SkillException {
        // 发布多个技能
        publishTestSkills();
        
        // 搜索技能
        List<SkillListing> results = marketManager.searchSkills("测试");
        assertNotNull("搜索结果应该不为空", results);
        assertTrue("搜索结果应该包含至少一个技能", results.size() > 0);
        
        // 验证搜索结果
        boolean found = false;
        for (SkillListing listing : results) {
            if (listing.getSkillId().equals("test-skill-1")) {
                found = true;
                break;
            }
        }
        assertTrue("搜索结果应该包含测试技能", found);
    }
    
    @Test
    public void testRateSkill() throws SkillException {
        // 先发布一个技能
        publishTestSkills();
        
        // 评价技能
        boolean result = marketManager.rateSkill("test-skill-1", 4.5, "这是一个很好的技能", "test-user-1");
        assertTrue("技能应该被成功评价", result);
        
        // 验证技能已评价
        SkillListing listing = marketManager.getSkillListing("test-skill-1");
        assertNotNull("技能列表项应该被成功检索", listing);
        assertTrue("技能评分应该大于0", listing.getRating() > 0);
        assertTrue("技能评论数应该大于0", listing.getReviewCount() > 0);
    }
    
    @Test
    public void testGetSkillsByCategory() throws SkillException {
        // 发布多个技能
        publishTestSkills();
        
        // 按分类获取技能
        List<SkillListing> testCategorySkills = marketManager.getSkillsByCategory("test");
        assertNotNull("test分类的技能列表应该不为空", testCategorySkills);
        assertTrue("test分类应该包含至少一个技能", testCategorySkills.size() > 0);
        
        List<SkillListing> devCategorySkills = marketManager.getSkillsByCategory("development");
        assertNotNull("development分类的技能列表应该不为空", devCategorySkills);
        assertTrue("development分类应该包含至少一个技能", devCategorySkills.size() > 0);
    }
    
    @Test
    public void testGetAllSkills() throws SkillException {
        // 发布多个技能
        publishTestSkills();
        
        // 获取所有技能
        List<SkillListing> allSkills = marketManager.getAllSkills();
        assertNotNull("所有技能列表应该不为空", allSkills);
        assertTrue("所有技能列表应该包含至少两个技能", allSkills.size() >= 2);
    }
    
    @Test
    public void testGetPopularSkills() throws SkillException {
        // 发布多个技能
        publishTestSkills();
        
        // 获取热门技能
        List<SkillListing> popularSkills = marketManager.getPopularSkills(5);
        assertNotNull("热门技能列表应该不为空", popularSkills);
    }
    
    @Test
    public void testGetLatestSkills() throws SkillException {
        // 发布多个技能
        publishTestSkills();
        
        // 获取最新技能
        List<SkillListing> latestSkills = marketManager.getLatestSkills(5);
        assertNotNull("最新技能列表应该不为空", latestSkills);
    }
    
    /**
     * 发布测试技能的辅助方法
     */
    private void publishTestSkills() throws SkillException {
        // 发布第一个技能
        Skill skill1 = new Skill() {
            @Override
            public String getId() {
                return "test-skill-1";
            }
            
            @Override
            public String getName() {
                return "测试技能";
            }
            
            @Override
            public String getDescription() {
                return "这是一个测试技能";
            }
            
            @Override
            public boolean isAvailable() {
                return true;
            }
            
            @Override
            public java.util.Map<String, net.ooder.skillcenter.model.SkillParam> getParams() {
                return new java.util.HashMap<>();
            }
            
            @Override
            public net.ooder.skillcenter.model.SkillResult execute(net.ooder.skillcenter.model.SkillContext context) throws net.ooder.skillcenter.model.SkillException {
                net.ooder.skillcenter.model.SkillResult result = new net.ooder.skillcenter.model.SkillResult();
                result.setMessage("Skill executed successfully");
                return result;
            }
        };
        
        SkillListing listing1 = new SkillListing();
        listing1.setSkillId("test-skill-1");
        listing1.setName("测试技能");
        listing1.setDescription("这是一个测试技能");
        listing1.setCategory("test");
        listing1.setVersion("1.0.0");
        listing1.setDownloadUrl("https://example.com/test-skill");
        listing1.setAuthor("Test Author");
        listing1.setDownloadCount(0);
        listing1.setRating(0.0);
        listing1.setReviewCount(0);
        listing1.setLastUpdated(System.currentTimeMillis());
        
        marketManager.publishSkill(skill1, listing1);
        
        // 发布第二个技能
        Skill skill2 = new Skill() {
            @Override
            public String getId() {
                return "dev-skill-1";
            }
            
            @Override
            public String getName() {
                return "开发技能";
            }
            
            @Override
            public String getDescription() {
                return "这是一个开发技能";
            }
            
            @Override
            public boolean isAvailable() {
                return true;
            }
            
            @Override
            public java.util.Map<String, net.ooder.skillcenter.model.SkillParam> getParams() {
                return new java.util.HashMap<>();
            }
            
            @Override
            public net.ooder.skillcenter.model.SkillResult execute(net.ooder.skillcenter.model.SkillContext context) throws net.ooder.skillcenter.model.SkillException {
                net.ooder.skillcenter.model.SkillResult result = new net.ooder.skillcenter.model.SkillResult();
                result.setMessage("Skill executed successfully");
                return result;
            }
        };
        
        SkillListing listing2 = new SkillListing();
        listing2.setSkillId("dev-skill-1");
        listing2.setName("开发技能");
        listing2.setDescription("这是一个开发技能");
        listing2.setCategory("development");
        listing2.setVersion("1.0.0");
        listing2.setDownloadUrl("https://example.com/dev-skill");
        listing2.setAuthor("Dev Author");
        listing2.setDownloadCount(0);
        listing2.setRating(0.0);
        listing2.setReviewCount(0);
        listing2.setLastUpdated(System.currentTimeMillis());
        
        marketManager.publishSkill(skill2, listing2);
    }
}
