package net.ooder.nexus.service;

import net.ooder.nexus.domain.task.model.DataExtractTask;

import java.util.List;

/**
 * 数据抽取任务服务接口
 */
public interface DataExtractTaskService {
    
    /**
     * 创建任务
     */
    DataExtractTask createTask(DataExtractTask task);
    
    /**
     * 更新任务
     */
    DataExtractTask updateTask(String id, DataExtractTask task);
    
    /**
     * 删除任务
     */
    boolean deleteTask(String id);
    
    /**
     * 根据ID获取任务
     */
    DataExtractTask getTaskById(String id);
    
    /**
     * 获取所有任务
     */
    List<DataExtractTask> getAllTasks();
    
    /**
     * 获取启用的任务列表
     */
    List<DataExtractTask> getEnabledTasks();
    
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
     * 任务统计信息
     */
    class TaskStats {
        private int totalCount;
        private int enabledCount;
        private int runningCount;
        private int completedCount;
        private int failedCount;
        
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
    }
}
