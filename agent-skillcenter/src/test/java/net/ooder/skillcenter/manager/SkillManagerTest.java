package net.ooder.skillcenter.manager;

import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * 技能管理器单元测试
 */
public class SkillManagerTest {
    private SkillManager skillManager;
    private Skill mockSkill;
    private SkillContext mockContext;
    private SkillResult mockResult;

    @Before
    public void setUp() {
        // 获取技能管理器实例
        skillManager = SkillManager.getInstance("test-instance");
        
        // 模拟技能对象
        mockSkill = mock(Skill.class);
        when(mockSkill.getId()).thenReturn("test-skill-456");
        when(mockSkill.isAvailable()).thenReturn(true);
        
        // 模拟执行上下文
        mockContext = mock(SkillContext.class);
        
        // 模拟执行结果
        mockResult = mock(SkillResult.class);
        when(mockResult.isSuccess()).thenReturn(true);
    }

    @After
    public void tearDown() {
        // 关闭技能管理器
        skillManager.shutdown();
    }

    @Test
    public void testRegisterSkill() {
        // 执行：注册技能
        skillManager.registerSkill(mockSkill);
        
        // 验证：技能是否注册成功
        Skill registeredSkill = skillManager.getSkill("test-skill-456");
        assertNotNull(registeredSkill);
        assertEquals("test-skill-456", registeredSkill.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterSkill_NullSkill() {
        // 执行：注册null技能
        skillManager.registerSkill(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterSkill_EmptySkillId() {
        // 准备：模拟空ID技能
        Skill skillWithEmptyId = mock(Skill.class);
        when(skillWithEmptyId.getId()).thenReturn("");
        
        // 执行：注册空ID技能
        skillManager.registerSkill(skillWithEmptyId);
    }

    @Test
    public void testUnregisterSkill() {
        // 准备：先注册技能
        skillManager.registerSkill(mockSkill);
        assertNotNull(skillManager.getSkill("test-skill-456"));
        
        // 执行：卸载技能
        skillManager.unregisterSkill("test-skill-456");
        
        // 验证：技能是否卸载成功
        assertNull(skillManager.getSkill("test-skill-456"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnregisterSkill_EmptySkillId() {
        // 执行：卸载空ID技能
        skillManager.unregisterSkill("");
    }

    @Test
    public void testGetSkill() {
        // 准备：注册技能
        skillManager.registerSkill(mockSkill);
        
        // 执行：获取技能
        Skill retrievedSkill = skillManager.getSkill("test-skill-456");
        
        // 验证：技能是否获取成功
        assertNotNull(retrievedSkill);
        assertEquals("test-skill-456", retrievedSkill.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSkill_EmptySkillId() {
        // 执行：获取空ID技能
        skillManager.getSkill("");
    }

    @Test
    public void testGetAllSkills() {
        // 准备：注册多个技能
        for (int i = 0; i < 3; i++) {
            Skill skill = mock(Skill.class);
            when(skill.getId()).thenReturn("skill-" + i);
            when(skill.isAvailable()).thenReturn(true);
            skillManager.registerSkill(skill);
        }
        
        // 执行：获取所有技能
        List<Skill> allSkills = skillManager.getAllSkills();
        
        // 验证：技能列表是否包含所有注册的技能
        assertNotNull(allSkills);
        assertTrue(allSkills.size() >= 3); // 至少包含我们注册的3个技能
    }

    @Test
    public void testGetSkillsByCategory() {
        // 准备：注册技能并获取分类
        skillManager.registerSkill(mockSkill);
        String category = mockSkill.getClass().getPackage().getName();
        
        // 执行：根据分类获取技能
        List<Skill> skillsByCategory = skillManager.getSkillsByCategory(category);
        
        // 验证：技能列表是否包含我们注册的技能
        assertNotNull(skillsByCategory);
        assertFalse(skillsByCategory.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSkillsByCategory_EmptyCategory() {
        // 执行：获取空分类技能
        skillManager.getSkillsByCategory("");
    }

    @Test
    public void testFindSkills() {
        // 准备：注册多个技能
        for (int i = 0; i < 3; i++) {
            Skill skill = mock(Skill.class);
            when(skill.getId()).thenReturn("search-skill-" + i);
            when(skill.isAvailable()).thenReturn(i % 2 == 0); // 偶数索引的技能可用
            skillManager.registerSkill(skill);
        }
        
        // 执行：根据条件查询技能（只查询可用技能）
        List<Skill> foundSkills = skillManager.findSkills(skill -> skill.isAvailable());
        
        // 验证：技能列表是否只包含可用技能
        assertNotNull(foundSkills);
        for (Skill skill : foundSkills) {
            assertTrue(skill.isAvailable());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSkills_NullCondition() {
        // 执行：使用null条件查询技能
        skillManager.findSkills(null);
    }

    @Test
    public void testSingletonInstance() {
        // 执行：获取多次实例
        SkillManager instance1 = SkillManager.getInstance("test-instance");
        SkillManager instance2 = SkillManager.getInstance("test-instance");
        
        // 验证：应为同一实例
        assertSame(instance1, instance2);
    }

    @Test
    public void testMultipleInstances() {
        // 执行：获取不同名称的实例
        SkillManager instance1 = SkillManager.getInstance("instance1");
        SkillManager instance2 = SkillManager.getInstance("instance2");
        
        // 验证：应为不同实例
        assertNotSame(instance1, instance2);
    }

    @Test
    public void testExecuteSkillAsync() throws Exception {
        // 准备：注册技能
        when(mockSkill.execute(mockContext)).thenReturn(mockResult);
        skillManager.registerSkill(mockSkill);
        
        // 执行：异步执行技能
        final boolean[] callbackCalled = {false};
        final SkillResult[] callbackResult = {null};
        
        skillManager.executeSkillAsync("test-skill-456", mockContext, new SkillManager.SkillCallback() {
            @Override
            public void onSuccess(SkillResult result) {
                callbackCalled[0] = true;
                callbackResult[0] = result;
            }

            @Override
            public void onFailure(SkillException exception) {
                callbackCalled[0] = true;
            }
        });
        
        // 等待异步执行完成
        Thread.sleep(1000);
        
        // 验证：回调是否被调用
        assertTrue(callbackCalled[0]);
    }
}
