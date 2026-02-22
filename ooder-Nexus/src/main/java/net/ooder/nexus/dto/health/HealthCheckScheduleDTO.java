package net.ooder.nexus.dto.health;

import java.io.Serializable;

/**
 * Health check schedule request DTO
 */
public class HealthCheckScheduleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String scheduleId;
    private String name;
    private String cronExpression;
    private Long intervalMinutes;
    private Boolean enabled;

    public String getScheduleId() { return scheduleId; }
    public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCronExpression() { return cronExpression; }
    public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }
    public Long getIntervalMinutes() { return intervalMinutes; }
    public void setIntervalMinutes(Long intervalMinutes) { this.intervalMinutes = intervalMinutes; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
}
