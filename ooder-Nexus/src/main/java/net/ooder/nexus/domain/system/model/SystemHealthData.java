package net.ooder.nexus.domain.system.model;

import java.io.Serializable;
import java.util.Map;

public class SystemHealthData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String status;
    private long timestamp;
    private long uptime;
    private Map<String, ServiceStatus> serviceStatuses;
    private Map<String, ResourceUsage> resourceUsage;
    private Map<String, Object> details;

    public SystemHealthData() {
    }

    public SystemHealthData(String status, long timestamp, long uptime, Map<String, ServiceStatus> serviceStatuses, Map<String, ResourceUsage> resourceUsage, Map<String, Object> details) {
        this.status = status;
        this.timestamp = timestamp;
        this.uptime = uptime;
        this.serviceStatuses = serviceStatuses;
        this.resourceUsage = resourceUsage;
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public Map<String, ServiceStatus> getServiceStatuses() {
        return serviceStatuses;
    }

    public void setServiceStatuses(Map<String, ServiceStatus> serviceStatuses) {
        this.serviceStatuses = serviceStatuses;
    }

    public Map<String, ResourceUsage> getResourceUsage() {
        return resourceUsage;
    }

    public void setResourceUsage(Map<String, ResourceUsage> resourceUsage) {
        this.resourceUsage = resourceUsage;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
}
