package net.ooder.skillcenter.execution;

import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 技能执行监控器，负责监控技能执行状态和统计信息
 */
public class SkillExecutionMonitor {
    // 技能执行统计信息
    private SkillExecutionStats stats;
    
    // 正在执行的技能映射，key为执行ID，value为执行信息
    private Map<String, ExecutionInfo> executingSkills;
    
    // 技能执行成功计数
    private AtomicLong successCount;
    
    // 技能执行失败计数
    private AtomicLong failureCount;
    
    // 总执行时间
    private AtomicLong totalExecutionTime;
    
    /**
     * 构造方法
     */
    public SkillExecutionMonitor() {
        this.stats = new SkillExecutionStats();
        this.executingSkills = new ConcurrentHashMap<>();
        this.successCount = new AtomicLong(0);
        this.failureCount = new AtomicLong(0);
        this.totalExecutionTime = new AtomicLong(0);
    }
    
    /**
     * 技能执行开始
     * @param executionId 执行ID
     * @param skillId 技能ID
     */
    public void onExecutionStart(String executionId, String skillId) {
        ExecutionInfo info = new ExecutionInfo();
        info.setExecutionId(executionId);
        info.setSkillId(skillId);
        info.setStartTime(System.currentTimeMillis());
        
        executingSkills.put(executionId, info);
        stats.setTotalExecutions(stats.getTotalExecutions() + 1);
        stats.setExecutingCount(executingSkills.size());
    }
    
    /**
     * 技能执行成功
     * @param executionId 执行ID
     * @param skillId 技能ID
     * @param result 执行结果
     */
    public void onExecutionSuccess(String executionId, String skillId, SkillResult result) {
        ExecutionInfo info = executingSkills.remove(executionId);
        if (info != null) {
            long executionTime = System.currentTimeMillis() - info.getStartTime();
            
            // 更新统计信息
            successCount.incrementAndGet();
            totalExecutionTime.addAndGet(executionTime);
            
            stats.setSuccessCount(successCount.get());
            stats.setExecutingCount(executingSkills.size());
            stats.setAverageExecutionTime(
                totalExecutionTime.get() / (successCount.get() + failureCount.get())
            );
            
            // 更新技能执行统计
            stats.getSkillStats().computeIfAbsent(skillId, k -> new SkillStat())
                .incrementSuccessCount();
        }
    }
    
    /**
     * 技能执行失败
     * @param executionId 执行ID
     * @param skillId 技能ID
     * @param exception 执行异常
     */
    public void onExecutionFailure(String executionId, String skillId, SkillException exception) {
        ExecutionInfo info = executingSkills.remove(executionId);
        if (info != null) {
            long executionTime = System.currentTimeMillis() - info.getStartTime();
            
            // 更新统计信息
            failureCount.incrementAndGet();
            totalExecutionTime.addAndGet(executionTime);
            
            stats.setFailureCount(failureCount.get());
            stats.setExecutingCount(executingSkills.size());
            stats.setAverageExecutionTime(
                totalExecutionTime.get() / (successCount.get() + failureCount.get())
            );
            
            // 更新技能执行统计
            stats.getSkillStats().computeIfAbsent(skillId, k -> new SkillStat())
                .incrementFailureCount();
        }
    }
    
    /**
     * 获取统计信息
     * @return 执行统计信息
     */
    public SkillExecutionStats getStats() {
        return stats;
    }
    
    /**
     * 清理监控器
     */
    public void clear() {
        executingSkills.clear();
        successCount.set(0);
        failureCount.set(0);
        totalExecutionTime.set(0);
        stats = new SkillExecutionStats();
    }
    
    /**
     * 执行信息类
     */
    private static class ExecutionInfo {
        private String executionId;
        private String skillId;
        private long startTime;
        
        public String getExecutionId() {
            return executionId;
        }
        
        public void setExecutionId(String executionId) {
            this.executionId = executionId;
        }
        
        public String getSkillId() {
            return skillId;
        }
        
        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }
        
        public long getStartTime() {
            return startTime;
        }
        
        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }
    }
    
    /**
     * 技能执行统计类
     */
    public static class SkillStat {
        private long successCount;
        private long failureCount;
        
        public SkillStat() {
            this.successCount = 0;
            this.failureCount = 0;
        }
        
        public void incrementSuccessCount() {
            successCount++;
        }
        
        public void incrementFailureCount() {
            failureCount++;
        }
        
        public long getSuccessCount() {
            return successCount;
        }
        
        public long getFailureCount() {
            return failureCount;
        }
        
        public long getTotalCount() {
            return successCount + failureCount;
        }
    }
}
