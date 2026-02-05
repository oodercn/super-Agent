package net.ooder.nexus.model.system;

import java.util.Date;

public class HealthCheckResult {
    private String id;
    private String name;
    private String status;
    private String message;
    private long duration;
    private Date timestamp;
    private String details;

    public HealthCheckResult() {
    }

    public HealthCheckResult(String id, String name, String status, String message, long duration, Date timestamp, String details) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.message = message;
        this.duration = duration;
        this.timestamp = timestamp;
        this.details = details;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}