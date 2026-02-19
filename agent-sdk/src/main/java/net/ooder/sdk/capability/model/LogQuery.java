package net.ooder.sdk.capability.model;

public class LogQuery {
    
    private String executionId;
    private int level;
    private String keyword;
    private long startTime;
    private long endTime;
    private int limit;
    
    public String getExecutionId() { return executionId; }
    public void setExecutionId(String executionId) { this.executionId = executionId; }
    
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    
    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }
}
