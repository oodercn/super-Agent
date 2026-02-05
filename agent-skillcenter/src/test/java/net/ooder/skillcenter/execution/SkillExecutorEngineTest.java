package net.ooder.skillcenter.execution;

import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * 技能执行引擎单元测试
 */
public class SkillExecutorEngineTest {
    private SkillExecutorEngine executorEngine;
    private Skill mockSkill;
    private SkillContext mockContext;
    private SkillResult mockResult;

    @Before
    public void setUp() {
        // 获取执行引擎实例
        executorEngine = SkillExecutorEngine.getInstance();
        
        // 模拟技能对象
        mockSkill = mock(Skill.class);
        when(mockSkill.getId()).thenReturn("test-skill-123");
        when(mockSkill.isAvailable()).thenReturn(true);
        
        // 模拟执行上下文
        mockContext = mock(SkillContext.class);
        
        // 模拟执行结果
        mockResult = mock(SkillResult.class);
        when(mockResult.isSuccess()).thenReturn(true);
    }

    @After
    public void tearDown() {
        // 关闭执行引擎
        executorEngine.shutdown();
    }

    @Test
    public void testExecuteSkill_Success() throws SkillException {
        // 准备
        when(mockSkill.execute(mockContext)).thenReturn(mockResult);
        
        // 执行
        SkillResult result = executorEngine.executeSkill(mockSkill, mockContext);
        
        // 验证
        assertNotNull(result);
        verify(mockSkill).execute(mockContext);
    }

    @Test(expected = SkillException.class)
    public void testExecuteSkill_NullSkill() throws SkillException {
        // 执行：传入null技能
        executorEngine.executeSkill(null, mockContext);
    }

    @Test(expected = SkillException.class)
    public void testExecuteSkill_UnavailableSkill() throws SkillException {
        // 准备：设置技能不可用
        when(mockSkill.isAvailable()).thenReturn(false);
        
        // 执行
        executorEngine.executeSkill(mockSkill, mockContext);
    }

    @Test(expected = SkillException.class)
    public void testExecuteSkill_SkillExecutionFailure() throws SkillException {
        // 准备：模拟技能执行失败
        SkillException skillException = new SkillException(
            "test-skill-123", "Execution failed", 
            SkillException.ErrorCode.EXECUTION_EXCEPTION
        );
        when(mockSkill.execute(mockContext)).thenThrow(skillException);
        
        // 执行
        executorEngine.executeSkill(mockSkill, mockContext);
    }

    @Test(expected = SkillException.class)
    public void testExecuteSkill_GeneralException() throws SkillException {
        // 准备：模拟一般异常
        when(mockSkill.execute(mockContext)).thenThrow(new RuntimeException("General error"));
        
        // 执行
        executorEngine.executeSkill(mockSkill, mockContext);
    }

    @Test
    public void testExecuteSkillAsync_Success() throws Exception {
        // 准备
        when(mockSkill.execute(mockContext)).thenReturn(mockResult);
        
        // 执行
        SkillExecutorEngine.SkillExecutionCallback callback = mock(SkillExecutorEngine.SkillExecutionCallback.class);
        
        // 执行异步技能
        executorEngine.executeSkillAsync(mockSkill, mockContext, callback)
            .thenAccept(result -> {
                assertNotNull(result);
                verify(callback).onSuccess(result);
            })
            .join();
        
        // 验证
        verify(mockSkill).execute(mockContext);
    }

    @Test
    public void testExecuteSkillAsync_Failure() throws Exception {
        // 准备：模拟技能执行失败
        SkillException skillException = new SkillException(
            "test-skill-123", "Execution failed", 
            SkillException.ErrorCode.EXECUTION_EXCEPTION
        );
        when(mockSkill.execute(mockContext)).thenThrow(skillException);
        
        // 执行
        SkillExecutorEngine.SkillExecutionCallback callback = mock(SkillExecutorEngine.SkillExecutionCallback.class);
        
        // 执行异步技能
        executorEngine.executeSkillAsync(mockSkill, mockContext, callback)
            .exceptionally(ex -> {
                verify(callback).onFailure(skillException);
                return null;
            })
            .join();
        
        // 验证
        verify(mockSkill).execute(mockContext);
    }

    @Test
    public void testExecuteSkillWithTimeout_Success() throws Exception {
        // 准备
        when(mockSkill.execute(mockContext)).thenReturn(mockResult);
        
        // 执行：设置10秒超时
        SkillResult result = executorEngine.executeSkillWithTimeout(mockSkill, mockContext, 10000);
        
        // 验证
        assertNotNull(result);
        verify(mockSkill).execute(mockContext);
    }

    @Test(expected = TimeoutException.class)
    public void testExecuteSkillWithTimeout_Timeout() throws Exception {
        // 准备：模拟技能执行超时
        when(mockSkill.execute(mockContext)).thenAnswer(invocation -> {
            Thread.sleep(2000); // 模拟长时间执行
            return mockResult;
        });
        
        // 执行：设置100毫秒超时
        executorEngine.executeSkillWithTimeout(mockSkill, mockContext, 100);
    }

    @Test
    public void testExecuteSkillsBatch() throws Exception {
        // 准备：创建多个模拟技能
        List<Skill> skills = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            Skill skill = mock(Skill.class);
            when(skill.getId()).thenReturn("test-skill-" + i);
            when(skill.isAvailable()).thenReturn(true);
            when(skill.execute(mockContext)).thenReturn(mockResult);
            skills.add(skill);
        }
        
        // 执行
        Map<String, SkillResult> results = executorEngine.executeSkillsBatch(skills, mockContext);
        
        // 验证
        assertNotNull(results);
        assertEquals(5, results.size());
        for (Skill skill : skills) {
            assertTrue(results.containsKey(skill.getId()));
        }
    }

    @Test
    public void testGetExecutionStats() {
        // 执行
        SkillExecutionStats stats = executorEngine.getExecutionStats();
        
        // 验证
        assertNotNull(stats);
        assertTrue(stats.getTotalExecutions() >= 0);
        assertTrue(stats.getSuccessCount() >= 0);
        assertTrue(stats.getFailureCount() >= 0);
    }

    @Test
    public void testSetExecutionTimeout() {
        // 执行：设置超时时间
        long newTimeout = 60000; // 60秒
        executorEngine.setExecutionTimeout(newTimeout);
        
        // 验证：通过执行一个技能来间接验证
        try {
            when(mockSkill.execute(mockContext)).thenReturn(mockResult);
            SkillResult result = executorEngine.executeSkill(mockSkill, mockContext);
            assertNotNull(result);
        } catch (SkillException e) {
            fail("Skill execution should succeed");
        }
    }

    @Test
    public void testSingletonInstance() {
        // 执行：获取多次实例
        SkillExecutorEngine instance1 = SkillExecutorEngine.getInstance();
        SkillExecutorEngine instance2 = SkillExecutorEngine.getInstance();
        
        // 验证：应为同一实例
        assertSame(instance1, instance2);
    }
}
