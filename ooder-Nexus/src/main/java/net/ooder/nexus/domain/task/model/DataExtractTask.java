package net.ooder.nexus.domain.task.model;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 数据抽取任务实体类
 */
public class DataExtractTask {
    
    /**
     * 任务状态枚举
     */
    public enum TaskStatus {
        PENDING("待执行"),
        RUNNING("执行中"),
        COMPLETED("已完成"),
        FAILED("失败"),
        CANCELLED("已取消");
        
        private final String description;
        
        TaskStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * 任务类型枚举
     */
    public enum TaskType {
        DATABASE("数据库抽取"),
        API("API接口抽取"),
        FILE("文件抽取"),
        LOG("日志抽取"),
        CUSTOM("自定义抽取");
        
        private final String description;
        
        TaskType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    private String id;
    private String name;
    private String description;
    private TaskType type;
    private TaskStatus status;
    private String sourceConfig;
    private String targetConfig;
    private String cronExpression;
    private boolean enabled;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime lastExecuteTime;
    private LocalDateTime nextExecuteTime;
    private String lastErrorMessage;
    private int executeCount;
    private int successCount;
    private int failCount;
    private Map<String, Object> extraParams;
    
    public DataExtractTask() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.status = TaskStatus.PENDING;
        this.enabled = true;
        this.executeCount = 0;
        this.successCount = 0;
        this.failCount = 0;
    }
    
    // Getters and Setters
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
    
    public TaskType getType() {
        return type;
    }
    
    public void setType(TaskType type) {
        this.type = type;
    }
    
    public TaskStatus getStatus() {
        return status;
    }
    
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
    
    public String getSourceConfig() {
        return sourceConfig;
    }
    
    public void setSourceConfig(String sourceConfig) {
        this.sourceConfig = sourceConfig;
    }
    
    public String getTargetConfig() {
        return targetConfig;
    }
    
    public void setTargetConfig(String targetConfig) {
        this.targetConfig = targetConfig;
    }
    
    public String getCronExpression() {
        return cronExpression;
    }
    
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public LocalDateTime getLastExecuteTime() {
        return lastExecuteTime;
    }
    
    public void setLastExecuteTime(LocalDateTime lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }
    
    public LocalDateTime getNextExecuteTime() {
        return nextExecuteTime;
    }
    
    public void setNextExecuteTime(LocalDateTime nextExecuteTime) {
        this.nextExecuteTime = nextExecuteTime;
    }
    
    public String getLastErrorMessage() {
        return lastErrorMessage;
    }
    
    public void setLastErrorMessage(String lastErrorMessage) {
        this.lastErrorMessage = lastErrorMessage;
    }
    
    public int getExecuteCount() {
        return executeCount;
    }
    
    public void setExecuteCount(int executeCount) {
        this.executeCount = executeCount;
    }
    
    public int getSuccessCount() {
        return successCount;
    }
    
    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }
    
    public int getFailCount() {
        return failCount;
    }
    
    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }
    
    public Map<String, Object> getExtraParams() {
        return extraParams;
    }
    
    public void setExtraParams(Map<String, Object> extraParams) {
        this.extraParams = extraParams;
    }
    
    /**
     * 增加执行次数
     */
    public void incrementExecuteCount() {
        this.executeCount++;
    }
    
    /**
     * 增加成功次数
     */
    public void incrementSuccessCount() {
        this.successCount++;
    }
    
    /**
     * 增加失败次数
     */
    public void incrementFailCount() {
        this.failCount++;
    }
    
    @Override
    public String toString() {
        return "DataExtractTask{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", enabled=" + enabled +
                ", createTime=" + createTime +
                '}';
    }
}
