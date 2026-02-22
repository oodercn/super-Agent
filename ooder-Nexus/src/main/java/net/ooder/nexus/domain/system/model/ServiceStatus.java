package net.ooder.nexus.domain.system.model;

import java.io.Serializable;

public class ServiceStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String status;
    private String message;
    private long startTime;
    private long lastUpdated;
    private int restartCount;

    public ServiceStatus() {
    }

    public ServiceStatus(String id, String name, String status, String message) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.message = message;
        this.startTime = System.currentTimeMillis();
        this.lastUpdated = System.currentTimeMillis();
        this.restartCount = 0;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getRestartCount() {
        return restartCount;
    }

    public void setRestartCount(int restartCount) {
        this.restartCount = restartCount;
    }
}
