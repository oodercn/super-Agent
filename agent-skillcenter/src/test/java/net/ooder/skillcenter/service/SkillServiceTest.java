package net.ooder.skillcenter.service;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.SkillDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 技能服务测试类
 */
@SpringBootTest
@TestPropertySource(properties = "skillcenter.sdk.mode=mock")
class SkillServiceTest {

    @Autowired
    private SkillService skillService;

    @Test
    void testGetAllSkills() {
        // 测试获取所有技能
        PageResult<SkillDTO> result = skillService.getAllSkills(null, null, null, 1, 10);
        
        assertNotNull(result);
        assertTrue(result.getTotal() > 0);
        assertNotNull(result.getList());
        assertFalse(result.getList().isEmpty());
    }

    @Test
    void testGetAllSkillsWithFilter() {
        // 测试带筛选条件的查询
        PageResult<SkillDTO> result = skillService.getAllSkills("text-processing", "active", null, 1, 10);
        
        assertNotNull(result);
        // 验证返回的数据符合筛选条件
        result.getList().forEach(skill -> {
            assertEquals("text-processing", skill.getCategory());
            assertEquals("active", skill.getStatus());
        });
    }

    @Test
    void testGetAllSkillsWithKeyword() {
        // 测试关键词搜索
        PageResult<SkillDTO> result = skillService.getAllSkills(null, null, "文本", 1, 10);
        
        assertNotNull(result);
        // 验证返回的数据包含关键词
        result.getList().forEach(skill -> {
            assertTrue(
                skill.getName().contains("文本") || skill.getId().contains("文本"),
                "技能名称或ID应包含关键词"
            );
        });
    }

    @Test
    void testGetSkillById() {
        // 先获取一个存在的技能ID
        PageResult<SkillDTO> allSkills = skillService.getAllSkills(null, null, null, 1, 1);
        String skillId = allSkills.getList().get(0).getId();
        
        // 测试根据ID获取技能
        SkillDTO skill = skillService.getSkillById(skillId);
        
        assertNotNull(skill);
        assertEquals(skillId, skill.getId());
    }

    @Test
    void testGetSkillById_NotFound() {
        // 测试获取不存在的技能
        SkillDTO skill = skillService.getSkillById("non-existent-id");
        
        assertNull(skill);
    }

    @Test
    void testAddSkill() {
        // 测试添加技能
        SkillDTO newSkill = new SkillDTO();
        newSkill.setName("测试技能");
        newSkill.setDescription("这是一个测试技能");
        newSkill.setCategory("test");
        newSkill.setStatus("active");
        newSkill.setVersion("1.0.0");
        newSkill.setAuthor("Test");
        
        SkillDTO result = skillService.addSkill(newSkill);
        
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("测试技能", result.getName());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void testUpdateSkill() {
        // 先添加一个技能
        SkillDTO newSkill = new SkillDTO();
        newSkill.setName("更新测试技能");
        newSkill.setDescription("原始描述");
        newSkill.setCategory("test");
        newSkill.setStatus("active");
        SkillDTO added = skillService.addSkill(newSkill);
        
        // 更新技能
        added.setDescription("更新后的描述");
        SkillDTO updated = skillService.updateSkill(added.getId(), added);
        
        assertNotNull(updated);
        assertEquals("更新后的描述", updated.getDescription());
    }

    @Test
    void testUpdateSkill_NotFound() {
        // 测试更新不存在的技能
        SkillDTO skill = new SkillDTO();
        skill.setName("不存在的技能");
        
        SkillDTO result = skillService.updateSkill("non-existent-id", skill);
        
        assertNull(result);
    }

    @Test
    void testDeleteSkill() {
        // 先添加一个技能
        SkillDTO newSkill = new SkillDTO();
        newSkill.setName("删除测试技能");
        newSkill.setCategory("test");
        SkillDTO added = skillService.addSkill(newSkill);
        
        // 删除技能
        boolean result = skillService.deleteSkill(added.getId());
        assertTrue(result);
        
        // 验证技能已被删除
        SkillDTO deleted = skillService.getSkillById(added.getId());
        assertNull(deleted);
    }

    @Test
    void testApproveSkill() {
        // 先添加一个待审核的技能
        SkillDTO newSkill = new SkillDTO();
        newSkill.setName("审核测试技能");
        newSkill.setCategory("test");
        newSkill.setStatus("pending");
        SkillDTO added = skillService.addSkill(newSkill);
        
        // 审核通过
        boolean result = skillService.approveSkill(added.getId());
        assertTrue(result);
        
        // 验证状态已更新
        SkillDTO approved = skillService.getSkillById(added.getId());
        assertEquals("active", approved.getStatus());
        assertTrue(approved.isAvailable());
    }

    @Test
    void testRejectSkill() {
        // 先添加一个待审核的技能
        SkillDTO newSkill = new SkillDTO();
        newSkill.setName("拒绝测试技能");
        newSkill.setCategory("test");
        newSkill.setStatus("pending");
        SkillDTO added = skillService.addSkill(newSkill);
        
        // 拒绝
        boolean result = skillService.rejectSkill(added.getId());
        assertTrue(result);
        
        // 验证状态已更新
        SkillDTO rejected = skillService.getSkillById(added.getId());
        assertEquals("rejected", rejected.getStatus());
        assertFalse(rejected.isAvailable());
    }

    // ==================== 市场管理测试 ====================

    @Test
    void testGetMarketSkills() {
        // 测试获取市场技能列表
        PageResult<SkillDTO> result = skillService.getMarketSkills(null, null, null, 1, 10);
        
        assertNotNull(result);
        assertNotNull(result.getList());
    }

    @Test
    void testAddMarketSkill() {
        // 测试添加市场技能
        SkillDTO newSkill = new SkillDTO();
        newSkill.setName("市场测试技能");
        newSkill.setDescription("这是一个市场测试技能");
        newSkill.setCategory("market");
        newSkill.setStatus("active");
        
        SkillDTO result = skillService.addMarketSkill(newSkill);
        
        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(result.getId().startsWith("market-"));
    }

    @Test
    void testUpdateMarketSkill() {
        // 先获取一个市场技能
        PageResult<SkillDTO> marketSkills = skillService.getMarketSkills(null, null, null, 1, 1);
        if (marketSkills.getTotal() > 0) {
            SkillDTO skill = marketSkills.getList().get(0);
            skill.setDescription("更新后的市场描述");
            
            SkillDTO updated = skillService.updateMarketSkill(skill.getId(), skill);
            
            assertNotNull(updated);
            assertEquals("更新后的市场描述", updated.getDescription());
        }
    }

    @Test
    void testRemoveMarketSkill() {
        // 先添加一个市场技能
        SkillDTO newSkill = new SkillDTO();
        newSkill.setName("删除市场技能");
        newSkill.setCategory("market");
        SkillDTO added = skillService.addMarketSkill(newSkill);
        
        // 删除
        boolean result = skillService.removeMarketSkill(added.getId());
        assertTrue(result);
    }
}
