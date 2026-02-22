package net.ooder.nexus.service;

import net.ooder.nexus.domain.task.model.ListDataExtractTask;

import java.util.List;

/**
 * 列表数据抽取任务服务接口
 */
public interface ListDataExtractTaskService {
    
    /**
     * 创建任务
     */
    ListDataExtractTask createTask(ListDataExtractTask task);
    
    /**
     * 更新任务
     */
    ListDataExtractTask updateTask(String id, ListDataExtractTask task);
    
    /**
     * 删除任务
     */
    boolean deleteTask(String id);
    
    /**
     * 根据ID获取任务
     */
    ListDataExtractTask getTaskById(String id);
    
    /**
     * 获取所有任务
     */
    List<ListDataExtractTask> getAllTasks();
    
    /**
     * 获取启用的任务列表
     */
    List<ListDataExtractTask> getEnabledTasks();
    
    /**
     * 执行任务
     */
    boolean executeTask(String id);
    
    /**
     * 启用任务
     */
    boolean enableTask(String id);
    
    /**
     * 禁用任务
     */
    boolean disableTask(String id);
    
    /**
     * 获取任务统计信息
     */
    TaskStats getTaskStats();
    
    /**
     * 测试连接
     */
    boolean testConnection(String id);
    
    /**
     * 预览数据
     */
    List<java.util.Map<String, Object>> previewData(String id, int limit);
    
    /**
     * 任务统计信息
     */
    class TaskStats {
        private int totalCount;
        private int enabledCount;
        private int runningCount;
        private int completedCount;
        private int failedCount;
        private long totalExtractedRecords;
        
        public int getTotalCount() {
            return totalCount;
        }
        
        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
        
        public int getEnabledCount() {
            return enabledCount;
        }
        
        public void setEnabledCount(int enabledCount) {
            this.enabledCount = enabledCount;
        }
        
        public int getRunningCount() {
            return runningCount;
        }
        
        public void setRunningCount(int runningCount) {
            this.runningCount = runningCount;
        }
        
        public int getCompletedCount() {
            return completedCount;
        }
        
        public void setCompletedCount(int completedCount) {
            this.completedCount = completedCount;
        }
        
        public int getFailedCount() {
            return failedCount;
        }
        
        public void setFailedCount(int failedCount) {
            this.failedCount = failedCount;
        }
        
        public long getTotalExtractedRecords() {
            return totalExtractedRecords;
        }
        
        public void setTotalExtractedRecords(long totalExtractedRecords) {
            this.totalExtractedRecords = totalExtractedRecords;
        }
    }
}
