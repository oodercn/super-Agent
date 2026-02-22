package net.ooder.nexus.domain.task.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 列表数据抽取任务实体类
 * 用于从各种数据源抽取列表数据
 */
public class ListDataExtractTask {
    
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
     * 数据源类型枚举
     */
    public enum SourceType {
        DATABASE("数据库"),
        API("API接口"),
        FILE("文件"),
        MESSAGE_QUEUE("消息队列"),
        CUSTOM("自定义");
        
        private final String description;
        
        SourceType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    private String id;
    private String name;
    private String description;
    private SourceType sourceType;
    private TaskStatus status;
    
    // 数据源配置
    private String sourceConfig;
    private String connectionString;
    private String username;
    private String password;
    private String query;
    private String apiUrl;
    private String apiMethod;
    private Map<String, String> apiHeaders;
    private String filePath;
    private String fileFormat;
    
    // 抽取配置
    private String targetCollection;
    private String primaryKey;
    private List<String> fieldMappings;
    private String filterCondition;
    private int batchSize;
    private int maxRecords;
    
    // 调度配置
    private String cronExpression;
    private boolean enabled;
    
    // 执行统计
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime lastExecuteTime;
    private LocalDateTime nextExecuteTime;
    private String lastErrorMessage;
    private int executeCount;
    private int successCount;
    private int failCount;
    private long totalExtractedRecords;
    private long lastExtractedRecords;
    
    // 额外参数
    private Map<String, Object> extraParams;
    
    public ListDataExtractTask() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.status = TaskStatus.PENDING;
        this.enabled = true;
        this.executeCount = 0;
        this.successCount = 0;
        this.failCount = 0;
        this.batchSize = 100;
        this.maxRecords = 10000;
        this.totalExtractedRecords = 0;
        this.lastExtractedRecords = 0;
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
    
    public SourceType getSourceType() {
        return sourceType;
    }
    
    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
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
    
    public String getConnectionString() {
        return connectionString;
    }
    
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getQuery() {
        return query;
    }
    
    public void setQuery(String query) {
        this.query = query;
    }
    
    public String getApiUrl() {
        return apiUrl;
    }
    
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
    
    public String getApiMethod() {
        return apiMethod;
    }
    
    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }
    
    public Map<String, String> getApiHeaders() {
        return apiHeaders;
    }
    
    public void setApiHeaders(Map<String, String> apiHeaders) {
        this.apiHeaders = apiHeaders;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public String getFileFormat() {
        return fileFormat;
    }
    
    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }
    
    public String getTargetCollection() {
        return targetCollection;
    }
    
    public void setTargetCollection(String targetCollection) {
        this.targetCollection = targetCollection;
    }
    
    public String getPrimaryKey() {
        return primaryKey;
    }
    
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    public List<String> getFieldMappings() {
        return fieldMappings;
    }
    
    public void setFieldMappings(List<String> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }
    
    public String getFilterCondition() {
        return filterCondition;
    }
    
    public void setFilterCondition(String filterCondition) {
        this.filterCondition = filterCondition;
    }
    
    public int getBatchSize() {
        return batchSize;
    }
    
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
    
    public int getMaxRecords() {
        return maxRecords;
    }
    
    public void setMaxRecords(int maxRecords) {
        this.maxRecords = maxRecords;
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
    
    public long getTotalExtractedRecords() {
        return totalExtractedRecords;
    }
    
    public void setTotalExtractedRecords(long totalExtractedRecords) {
        this.totalExtractedRecords = totalExtractedRecords;
    }
    
    public long getLastExtractedRecords() {
        return lastExtractedRecords;
    }
    
    public void setLastExtractedRecords(long lastExtractedRecords) {
        this.lastExtractedRecords = lastExtractedRecords;
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
    
    /**
     * 增加抽取记录数
     */
    public void addExtractedRecords(long count) {
        this.lastExtractedRecords = count;
        this.totalExtractedRecords += count;
    }
    
    @Override
    public String toString() {
        return "ListDataExtractTask{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sourceType=" + sourceType +
                ", status=" + status +
                ", targetCollection='" + targetCollection + '\'' +
                ", enabled=" + enabled +
                ", createTime=" + createTime +
                '}';
    }
}
