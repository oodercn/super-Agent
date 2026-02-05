package net.ooder.skillcenter.execution;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 技能执行统计信息
 */
public class SkillExecutionStats {
    // 总执行次数
    private long totalExecutions;
    
    // 成功执行次数
    private long successCount;
    
    // 失败执行次数
    private long failureCount;
    
    // 正在执行的数量
    private int executingCount;
    
    // 平均执行时间（毫秒）
    private long averageExecutionTime;
    
    // 技能执行统计映射，key为技能ID，value为技能统计信息
    private Map<String, SkillExecutionMonitor.SkillStat> skillStats;
    
    /**
     * 构造方法
     */
    public SkillExecutionStats() {
        this.totalExecutions = 0;
        this.successCount = 0;
        this.failureCount = 0;
        this.executingCount = 0;
        this.averageExecutionTime = 0;
        this.skillStats = new ConcurrentHashMap<>();
    }
    
    /**
     * 获取总执行次数
     * @return 总执行次数
     */
    public long getTotalExecutions() {
        return totalExecutions;
    }
    
    /**
     * 设置总执行次数
     * @param totalExecutions 总执行次数
     */
    public void setTotalExecutions(long totalExecutions) {
        this.totalExecutions = totalExecutions;
    }
    
    /**
     * 获取成功执行次数
     * @return 成功执行次数
     */
    public long getSuccessCount() {
        return successCount;
    }
    
    /**
     * 设置成功执行次数
     * @param successCount 成功执行次数
     */
    public void setSuccessCount(long successCount) {
        this.successCount = successCount;
    }
    
    /**
     * 获取失败执行次数
     * @return 失败执行次数
     */
    public long getFailureCount() {
        return failureCount;
    }
    
    /**
     * 设置失败执行次数
     * @param failureCount 失败执行次数
     */
    public void setFailureCount(long failureCount) {
        this.failureCount = failureCount;
    }
    
    /**
     * 获取正在执行的数量
     * @return 正在执行的数量
     */
    public int getExecutingCount() {
        return executingCount;
    }
    
    /**
     * 设置正在执行的数量
     * @param executingCount 正在执行的数量
     */
    public void setExecutingCount(int executingCount) {
        this.executingCount = executingCount;
    }
    
    /**
     * 获取平均执行时间
     * @return 平均执行时间（毫秒）
     */
    public long getAverageExecutionTime() {
        return averageExecutionTime;
    }
    
    /**
     * 设置平均执行时间
     * @param averageExecutionTime 平均执行时间（毫秒）
     */
    public void setAverageExecutionTime(long averageExecutionTime) {
        this.averageExecutionTime = averageExecutionTime;
    }
    
    /**
     * 获取技能执行统计映射
     * @return 技能执行统计映射
     */
    public Map<String, SkillExecutionMonitor.SkillStat> getSkillStats() {
        return skillStats;
    }
    
    /**
     * 设置技能执行统计映射
     * @param skillStats 技能执行统计映射
     */
    public void setSkillStats(Map<String, SkillExecutionMonitor.SkillStat> skillStats) {
        this.skillStats = skillStats;
    }
    
    /**
     * 获取成功率
     * @return 成功率（百分比）
     */
    public double getSuccessRate() {
        if (totalExecutions == 0) {
            return 0.0;
        }
        return (double) successCount / totalExecutions * 100.0;
    }
    
    /**
     * 获取失败率
     * @return 失败率（百分比）
     */
    public double getFailureRate() {
        if (totalExecutions == 0) {
            return 0.0;
        }
        return (double) failureCount / totalExecutions * 100.0;
    }
    
    /**
     * 清空统计信息
     */
    public void clear() {
        this.totalExecutions = 0;
        this.successCount = 0;
        this.failureCount = 0;
        this.executingCount = 0;
        this.averageExecutionTime = 0;
        this.skillStats.clear();
    }
    
    @Override
    public String toString() {
        return String.format(
            "SkillExecutionStats{" +
            "totalExecutions=%d, " +
            "successCount=%d, " +
            "failureCount=%d, " +
            "executingCount=%d, " +
            "averageExecutionTime=%dms, " +
            "successRate=%.2f%%, " +
            "failureRate=%.2f%%" +
            "}",
            totalExecutions, successCount, failureCount, executingCount,
            averageExecutionTime, getSuccessRate(), getFailureRate()
        );
    }
}
