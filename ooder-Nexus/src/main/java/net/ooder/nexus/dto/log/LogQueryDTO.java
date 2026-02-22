package net.ooder.nexus.dto.log;

import java.io.Serializable;

/**
 * Log query request DTO
 */
public class LogQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String level;
    private String source;
    private Long startTime;
    private Long endTime;
    private Integer limit;
    private String keyword;

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public Long getStartTime() { return startTime; }
    public void setStartTime(Long startTime) { this.startTime = startTime; }
    public Long getEndTime() { return endTime; }
    public void setEndTime(Long endTime) { this.endTime = endTime; }
    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
}
