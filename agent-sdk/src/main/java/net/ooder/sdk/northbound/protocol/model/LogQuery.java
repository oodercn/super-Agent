package net.ooder.sdk.northbound.protocol.model;

public class LogQuery {
    
    private int level;
    private String keyword;
    private long startTime;
    private long endTime;
    private int limit;
    
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
