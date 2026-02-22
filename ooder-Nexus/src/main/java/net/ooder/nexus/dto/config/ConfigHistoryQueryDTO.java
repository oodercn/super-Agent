package net.ooder.nexus.dto.config;

import java.io.Serializable;

/**
 * Config history query request DTO
 */
public class ConfigHistoryQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String configType;
    private Long startTime;
    private Long endTime;
    private Integer limit;
    private String user;

    public String getConfigType() { return configType; }
    public void setConfigType(String configType) { this.configType = configType; }
    public Long getStartTime() { return startTime; }
    public void setStartTime(Long startTime) { this.startTime = startTime; }
    public Long getEndTime() { return endTime; }
    public void setEndTime(Long endTime) { this.endTime = endTime; }
    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
}
