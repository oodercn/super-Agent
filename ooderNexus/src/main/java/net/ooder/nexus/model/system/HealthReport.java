package net.ooder.nexus.model.system;

import java.util.Date;
import java.util.List;

public class HealthReport {
    private String id;
    private String status;
    private Date timestamp;
    private List<HealthCheckResult> results;
    private int totalChecks;
    private int passedChecks;
    private int failedChecks;
    private long duration;
    private String summary;

    public HealthReport() {
    }

    public HealthReport(String id, String status, Date timestamp, List<HealthCheckResult> results, int totalChecks, int passedChecks, int failedChecks, long duration, String summary) {
        this.id = id;
        this.status = status;
        this.timestamp = timestamp;
        this.results = results;
        this.totalChecks = totalChecks;
        this.passedChecks = passedChecks;
        this.failedChecks = failedChecks;
        this.duration = duration;
        this.summary = summary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<HealthCheckResult> getResults() {
        return results;
    }

    public void setResults(List<HealthCheckResult> results) {
        this.results = results;
    }

    public int getTotalChecks() {
        return totalChecks;
    }

    public void setTotalChecks(int totalChecks) {
        this.totalChecks = totalChecks;
    }

    public int getPassedChecks() {
        return passedChecks;
    }

    public void setPassedChecks(int passedChecks) {
        this.passedChecks = passedChecks;
    }

    public int getFailedChecks() {
        return failedChecks;
    }

    public void setFailedChecks(int failedChecks) {
        this.failedChecks = failedChecks;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}