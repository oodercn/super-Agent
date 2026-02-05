package net.ooder.skillcenter.execution;

import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillResult;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 技能执行引擎，负责技能执行的核心逻辑
 * 管理技能执行的生命周期、异常处理、监控等
 */
public class SkillExecutorEngine {
    // 单例实例
    private static SkillExecutorEngine instance;
    
    // 执行线程池
    private ExecutorService executorService;
    
    // 技能执行监控器
    private SkillExecutionMonitor executionMonitor;
    
    // 技能执行计数器
    private AtomicInteger executionCounter;
    
    // 技能执行超时设置（毫秒）
    private long executionTimeout;
    
    /**
     * 私有构造方法
     */
    private SkillExecutorEngine() {
        this.executorService = new ThreadPoolExecutor(
            5, // 核心线程数
            20, // 最大线程数
            60L, TimeUnit.SECONDS, // 空闲线程存活时间
            new LinkedBlockingQueue<>(100), // 工作队列
            new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
        );
        
        this.executionMonitor = new SkillExecutionMonitor();
        this.executionCounter = new AtomicInteger(0);
        this.executionTimeout = 30000; // 默认30秒超时
    }
    
    /**
     * 获取实例
     * @return 技能执行引擎实例
     */
    public static synchronized SkillExecutorEngine getInstance() {
        if (instance == null) {
            instance = new SkillExecutorEngine();
        }
        return instance;
    }
    
    /**
     * 执行技能
     * @param skill 技能实例
     * @param context 执行上下文
     * @return 执行结果
     * @throws SkillException 执行异常
     */
    public SkillResult executeSkill(Skill skill, SkillContext context) throws SkillException {
        if (skill == null) {
            throw new SkillException("unknown", "Skill cannot be null", 
                                     SkillException.ErrorCode.PARAMETER_ERROR);
        }
        
        if (!skill.isAvailable()) {
            throw new SkillException(skill.getId(), "Skill is not available", 
                                     SkillException.ErrorCode.SKILL_NOT_AVAILABLE);
        }
        
        // 生成执行ID
        String executionId = generateExecutionId(skill.getId());
        
        // 记录执行开始
        executionMonitor.onExecutionStart(executionId, skill.getId());
        
        try {
            // 执行技能
            SkillResult result = skill.execute(context);
            
            // 记录执行成功
            executionMonitor.onExecutionSuccess(executionId, skill.getId(), result);
            
            return result;
        } catch (SkillException e) {
            // 记录执行失败
            executionMonitor.onExecutionFailure(executionId, skill.getId(), e);
            throw e;
        } catch (Exception e) {
            // 包装其他异常
            SkillException skillException = new SkillException(
                skill.getId(), "Skill execution failed: " + e.getMessage(),
                SkillException.ErrorCode.EXECUTION_EXCEPTION, e
            );
            // 记录执行失败
            executionMonitor.onExecutionFailure(executionId, skill.getId(), skillException);
            throw skillException;
        }
    }
    
    /**
     * 异步执行技能
     * @param skill 技能实例
     * @param context 执行上下文
     * @param callback 执行回调
     * @return 执行任务的Future
     */
    public CompletableFuture<SkillResult> executeSkillAsync(
            Skill skill, SkillContext context, SkillExecutionCallback callback) {
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                SkillResult result = executeSkill(skill, context);
                if (callback != null) {
                    callback.onSuccess(result);
                }
                return result;
            } catch (SkillException e) {
                if (callback != null) {
                    callback.onFailure(e);
                }
                throw new CompletionException(e);
            }
        }, executorService);
    }
    
    /**
     * 执行技能（带超时控制）
     * @param skill 技能实例
     * @param context 执行上下文
     * @param timeout 超时时间（毫秒）
     * @return 执行结果
     * @throws SkillException 执行异常
     * @throws TimeoutException 超时异常
     */
    public SkillResult executeSkillWithTimeout(
            Skill skill, SkillContext context, long timeout) throws SkillException, TimeoutException {
        
        CompletableFuture<SkillResult> future = CompletableFuture.supplyAsync(
            () -> {
                try {
                    return executeSkill(skill, context);
                } catch (SkillException e) {
                    throw new RuntimeException(e);
                }
            },
            executorService
        );
        
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SkillException(skill.getId(), "Skill execution interrupted", 
                                     SkillException.ErrorCode.EXECUTION_EXCEPTION, e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof SkillException) {
                throw (SkillException) e.getCause();
            } else {
                throw new SkillException(skill.getId(), "Skill execution failed", 
                                         SkillException.ErrorCode.EXECUTION_EXCEPTION, e.getCause());
            }
        }
    }
    
    /**
     * 批量执行技能
     * @param skills 技能列表
     * @param context 执行上下文
     * @return 执行结果映射，key为技能ID，value为执行结果
     */
    public Map<String, SkillResult> executeSkillsBatch(
            Iterable<Skill> skills, SkillContext context) {
        
        Map<String, SkillResult> results = new ConcurrentHashMap<>();
        CountDownLatch latch = new CountDownLatch(10); // 限制并发数
        
        for (Skill skill : skills) {
            try {
                latch.await();
                
                executorService.submit(() -> {
                    try {
                        SkillResult result = executeSkill(skill, context);
                        results.put(skill.getId(), result);
                    } catch (SkillException e) {
                        // 记录失败结果
                        SkillResult errorResult = new SkillResult();
                        errorResult.setMessage("Execution failed: " + e.getMessage());
                        errorResult.addData("errorCode", e.getErrorCode());
                        results.put(skill.getId(), errorResult);
                    } finally {
                        latch.countDown();
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        return results;
    }
    
    /**
     * 生成执行ID
     * @param skillId 技能ID
     * @return 执行ID
     */
    private String generateExecutionId(String skillId) {
        int count = executionCounter.incrementAndGet();
        return String.format("exec_%s_%d_%d", 
            skillId, count, System.currentTimeMillis());
    }
    
    /**
     * 获取执行监控信息
     * @return 执行监控信息
     */
    public SkillExecutionStats getExecutionStats() {
        return executionMonitor.getStats();
    }
    
    /**
     * 设置执行超时时间
     * @param timeout 超时时间（毫秒）
     */
    public void setExecutionTimeout(long timeout) {
        this.executionTimeout = timeout;
    }
    
    /**
     * 关闭执行引擎
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 技能执行回调接口
     */
    public interface SkillExecutionCallback {
        /**
         * 执行成功回调
         * @param result 执行结果
         */
        void onSuccess(SkillResult result);
        
        /**
         * 执行失败回调
         * @param exception 执行异常
         */
        void onFailure(SkillException exception);
    }
}
