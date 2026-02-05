package net.ooder.nexus.model.system;

import java.util.Date;
import java.util.List;

public class HealthCheckSchedule {
    private String id;
    private String name;
    private String cronExpression;
    private List<String> checkNames;
    private boolean enabled;
    private String description;
    private Date createdAt;
    private Date lastUpdated;
    private Date lastRun;
    private String lastRunStatus;

    public HealthCheckSchedule() {
    }

    public HealthCheckSchedule(String id, String name, String cronExpression, List<String> checkNames, boolean enabled, String description, Date createdAt, Date lastUpdated, Date lastRun, String lastRunStatus) {
        this.id = id;
        this.name = name;
        this.cronExpression = cronExpression;
        this.checkNames = checkNames;
        this.enabled = enabled;
        this.description = description;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.lastRun = lastRun;
        this.lastRunStatus = lastRunStatus;
    }

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

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public List<String> getCheckNames() {
        return checkNames;
    }

    public void setCheckNames(List<String> checkNames) {
        this.checkNames = checkNames;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getLastRun() {
        return lastRun;
    }

    public void setLastRun(Date lastRun) {
        this.lastRun = lastRun;
    }

    public String getLastRunStatus() {
        return lastRunStatus;
    }

    public void setLastRunStatus(String lastRunStatus) {
        this.lastRunStatus = lastRunStatus;
    }
}