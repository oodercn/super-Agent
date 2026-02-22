package net.ooder.skillcenter.sdk.cloud;

public class LogQueryOptions {
    private long startTime;
    private long endTime;
    private String level;
    private String keyword;
    private int limit;

    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }
}
