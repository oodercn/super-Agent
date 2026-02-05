package net.ooder.skillcenter.personalai;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据同步管理器，管理个人AI中心的数据同步
 */
public class DataSyncManager {
    // 同步任务映射，key为任务ID，value为同步任务
    private Map<String, SyncTask> syncTasks;
    
    /**
     * 构造方法
     */
    public DataSyncManager() {
        this.syncTasks = new ConcurrentHashMap<>();
    }
    
    /**
     * 启动数据同步管理器
     */
    public void start() {
        System.out.println("Data Sync Manager started");
    }
    
    /**
     * 停止数据同步管理器
     */
    public void stop() {
        System.out.println("Data Sync Manager stopped");
        // 取消所有同步任务
        for (SyncTask task : syncTasks.values()) {
            task.cancel();
        }
        syncTasks.clear();
    }
    
    /**
     * 同步数据到设备
     * @param deviceId 设备ID
     * @param dataType 数据类型
     * @param data 数据
     */
    public void syncData(String deviceId, String dataType, Map<String, Object> data) {
        String taskId = generateTaskId();
        SyncTask task = new SyncTask(taskId, deviceId, dataType, data);
        syncTasks.put(taskId, task);
        
        // 执行同步任务
        executeSyncTask(task);
    }
    
    /**
     * 共享技能到设备
     * @param skillId 技能ID
     * @param deviceId 设备ID
     */
    public void shareSkill(String skillId, String deviceId) {
        Map<String, Object> skillData = new HashMap<>();
        skillData.put("skillId", skillId);
        skillData.put("shareTime", System.currentTimeMillis());
        
        syncData(deviceId, "skill", skillData);
    }
    
    /**
     * 执行同步任务
     * @param task 同步任务
     */
    private void executeSyncTask(SyncTask task) {
        // 异步执行同步任务
        new Thread(() -> {
            try {
                System.out.println("Executing sync task: " + task.getTaskId());
                System.out.println("Syncing " + task.getDataType() + " to device: " + task.getDeviceId());
                
                // 模拟同步过程
                Thread.sleep(1000);
                
                // 标记任务完成
                task.complete();
                System.out.println("Sync task completed: " + task.getTaskId());
                
                // 从任务映射中移除
                syncTasks.remove(task.getTaskId());
            } catch (InterruptedException e) {
                System.err.println("Sync task interrupted: " + task.getTaskId());
                task.cancel();
                syncTasks.remove(task.getTaskId());
            } catch (Exception e) {
                System.err.println("Error executing sync task: " + e.getMessage());
                task.fail(e.getMessage());
                syncTasks.remove(task.getTaskId());
            }
        }).start();
    }
    
    /**
     * 生成任务ID
     * @return 任务ID
     */
    private String generateTaskId() {
        return "sync-task-" + UUID.randomUUID().toString();
    }
    
    /**
     * 获取同步任务
     * @param taskId 任务ID
     * @return 同步任务
     */
    public SyncTask getSyncTask(String taskId) {
        return syncTasks.get(taskId);
    }
    
    /**
     * 获取所有同步任务
     * @return 同步任务列表
     */
    public List<SyncTask> getAllSyncTasks() {
        return new ArrayList<>(syncTasks.values());
    }
    
    /**
     * 同步任务类
     */
    public static class SyncTask {
        private String taskId;
        private String deviceId;
        private String dataType;
        private Map<String, Object> data;
        private SyncStatus status;
        private String errorMessage;
        private long createdAt;
        private long startedAt;
        private long completedAt;
        
        public SyncTask(String taskId, String deviceId, String dataType, Map<String, Object> data) {
            this.taskId = taskId;
            this.deviceId = deviceId;
            this.dataType = dataType;
            this.data = data;
            this.status = SyncStatus.PENDING;
            this.createdAt = System.currentTimeMillis();
        }
        
        public void start() {
            this.status = SyncStatus.RUNNING;
            this.startedAt = System.currentTimeMillis();
        }
        
        public void complete() {
            this.status = SyncStatus.COMPLETED;
            this.completedAt = System.currentTimeMillis();
        }
        
        public void fail(String errorMessage) {
            this.status = SyncStatus.FAILED;
            this.errorMessage = errorMessage;
            this.completedAt = System.currentTimeMillis();
        }
        
        public void cancel() {
            this.status = SyncStatus.CANCELLED;
            this.completedAt = System.currentTimeMillis();
        }
        
        // Getters and setters
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getDeviceId() { return deviceId; }
        public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
        public String getDataType() { return dataType; }
        public void setDataType(String dataType) { this.dataType = dataType; }
        public Map<String, Object> getData() { return data; }
        public void setData(Map<String, Object> data) { this.data = data; }
        public SyncStatus getStatus() { return status; }
        public void setStatus(SyncStatus status) { this.status = status; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
        public long getStartedAt() { return startedAt; }
        public void setStartedAt(long startedAt) { this.startedAt = startedAt; }
        public long getCompletedAt() { return completedAt; }
        public void setCompletedAt(long completedAt) { this.completedAt = completedAt; }
        
        /**
         * 同步状态枚举
         */
        public enum SyncStatus {
            PENDING, RUNNING, COMPLETED, FAILED, CANCELLED
        }
    }
}