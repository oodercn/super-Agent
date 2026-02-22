package net.ooder.nexus.service;

import net.ooder.nexus.domain.sync.model.SyncTask;

import java.util.List;

/**
 * 技能同步服务接口
 * 参考agent-skillcenter设计，支持个人使用和小规模办公分享
 */
public interface SkillSyncService {
    
    /**
     * 获取所有同步任务
     */
    List<SyncTask> getAllTasks();
    
    /**
     * 根据状态获取同步任务
     */
    List<SyncTask> getTasksByStatus(String status);
    
    /**
     * 创建同步任务
     */
    SyncTask createTask(SyncTask task);
    
    /**
     * 执行同步任务
     */
    SyncTask executeTask(String taskId);
    
    /**
     * 取消同步任务
     */
    SyncTask cancelTask(String taskId);
    
    /**
     * 删除同步任务
     */
    boolean deleteTask(String taskId);
    
    /**
     * 获取同步统计信息
     */
    SyncStatistics getStatistics();
    
    /**
     * 获取可同步的技能列表
     */
    List<SyncableSkill> getSyncableSkills();
    
    /**
     * 获取已同步的技能列表
     */
    List<SyncedSkill> getSyncedSkills();
    
    /**
     * 上传技能到云端
     */
    SyncTask uploadSkill(String skillId, String target);
    
    /**
     * 从云端下载技能
     */
    SyncTask downloadSkill(String skillId, String source);
    
    /**
     * 批量同步技能
     */
    SyncTask batchSync(List<String> skillIds, String type);
    
    /**
     * 初始化默认数据
     */
    void initDefaultData();
    
    /**
     * 同步统计信息
     */
    class SyncStatistics {
        private int totalTasks;
        private int completedTasks;
        private int failedTasks;
        private int runningTasks;
        private int pendingTasks;
        private int totalSyncedSkills;
        private String lastSyncTime;
        private double successRate;
        
        public int getTotalTasks() {
            return totalTasks;
        }
        
        public void setTotalTasks(int totalTasks) {
            this.totalTasks = totalTasks;
        }
        
        public int getCompletedTasks() {
            return completedTasks;
        }
        
        public void setCompletedTasks(int completedTasks) {
            this.completedTasks = completedTasks;
        }
        
        public int getFailedTasks() {
            return failedTasks;
        }
        
        public void setFailedTasks(int failedTasks) {
            this.failedTasks = failedTasks;
        }
        
        public int getRunningTasks() {
            return runningTasks;
        }
        
        public void setRunningTasks(int runningTasks) {
            this.runningTasks = runningTasks;
        }
        
        public int getPendingTasks() {
            return pendingTasks;
        }
        
        public void setPendingTasks(int pendingTasks) {
            this.pendingTasks = pendingTasks;
        }
        
        public int getTotalSyncedSkills() {
            return totalSyncedSkills;
        }
        
        public void setTotalSyncedSkills(int totalSyncedSkills) {
            this.totalSyncedSkills = totalSyncedSkills;
        }
        
        public String getLastSyncTime() {
            return lastSyncTime;
        }
        
        public void setLastSyncTime(String lastSyncTime) {
            this.lastSyncTime = lastSyncTime;
        }
        
        public double getSuccessRate() {
            return successRate;
        }
        
        public void setSuccessRate(double successRate) {
            this.successRate = successRate;
        }
    }
    
    /**
     * 可同步的技能
     */
    class SyncableSkill {
        private String id;
        private String name;
        private String description;
        private String version;
        private String category;
        private long size;
        private String lastModified;
        private boolean synced;
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public String getVersion() {
            return version;
        }
        
        public void setVersion(String version) {
            this.version = version;
        }
        
        public String getCategory() {
            return category;
        }
        
        public void setCategory(String category) {
            this.category = category;
        }
        
        public long getSize() {
            return size;
        }
        
        public void setSize(long size) {
            this.size = size;
        }
        
        public String getLastModified() {
            return lastModified;
        }
        
        public void setLastModified(String lastModified) {
            this.lastModified = lastModified;
        }
        
        public boolean isSynced() {
            return synced;
        }
        
        public void setSynced(boolean synced) {
            this.synced = synced;
        }
    }
    
    /**
     * 已同步的技能
     */
    class SyncedSkill {
        private String id;
        private String name;
        private String description;
        private String version;
        private String category;
        private String syncDirection; // upload, download
        private String syncTime;
        private String syncStatus;
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public String getVersion() {
            return version;
        }
        
        public void setVersion(String version) {
            this.version = version;
        }
        
        public String getCategory() {
            return category;
        }
        
        public void setCategory(String category) {
            this.category = category;
        }
        
        public String getSyncDirection() {
            return syncDirection;
        }
        
        public void setSyncDirection(String syncDirection) {
            this.syncDirection = syncDirection;
        }
        
        public String getSyncTime() {
            return syncTime;
        }
        
        public void setSyncTime(String syncTime) {
            this.syncTime = syncTime;
        }
        
        public String getSyncStatus() {
            return syncStatus;
        }
        
        public void setSyncStatus(String syncStatus) {
            this.syncStatus = syncStatus;
        }
    }
}
