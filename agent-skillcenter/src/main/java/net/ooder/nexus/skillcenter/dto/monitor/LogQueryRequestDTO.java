package net.ooder.nexus.skillcenter.dto.monitor;

public class LogQueryRequestDTO {
    private String level;
    private String keyword;
    private long startTime;
    private long endTime;
    private int pageNum = 1;
    private int pageSize = 20;

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    public int getPageNum() { return pageNum; }
    public void setPageNum(int pageNum) { this.pageNum = pageNum; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}
