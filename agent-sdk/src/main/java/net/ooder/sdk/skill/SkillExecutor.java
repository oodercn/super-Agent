package net.ooder.sdk.skill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 技能执行器，负责技能的执行逻辑
 */
public class SkillExecutor {
    
    private static final Logger log = LoggerFactory.getLogger(SkillExecutor.class);
    
    // 执行线程池
    private final ExecutorService executorService;
    
    // 执行超时时间（毫秒）
    private static final long EXECUTION_TIMEOUT = 30000;
    
    public SkillExecutor() {
        this.executorService = Executors.newCachedThreadPool();
    }
    
    /**
     * 执行技能
     * @param skill 技能实例
     * @param params 执行参数
     * @return 执行结果
     */
    public SkillResult execute(Skill skill, Map<String, Object> params) {
        if (skill == null) {
            return SkillResult.failure("Skill cannot be null", null);
        }
        
        String skillId = skill.getSkillId();
        log.info("Executing skill: {}", skillId);
        
        try {
            // 检查技能状态
            if (skill.getStatus() != SkillStatus.READY) {
                return SkillResult.failure("Skill not ready: " + skill.getStatus(), null);
            }
            
            // 提交执行任务
            Future<SkillResult> future = executorService.submit(() -> {
                try {
                    // 执行技能
                    return skill.execute(params);
                } catch (Exception e) {
                    log.error("Skill execution failed: {}", skillId, e);
                    return SkillResult.failure("Execution error: " + e.getMessage(), null);
                }
            });
            
            // 等待执行完成，设置超时
            return future.get(EXECUTION_TIMEOUT, TimeUnit.MILLISECONDS);
            
        } catch (TimeoutException e) {
            log.error("Skill execution timeout: {}", skillId);
            return SkillResult.failure("Execution timeout", null);
        } catch (Exception e) {
            log.error("Failed to execute skill: {}", skillId, e);
            return SkillResult.failure("Execution error: " + e.getMessage(), null);
        } finally {
            log.info("Skill execution completed: {}", skillId);
        }
    }
    
    /**
     * 关闭执行器
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("SkillExecutor shut down");
    }
}