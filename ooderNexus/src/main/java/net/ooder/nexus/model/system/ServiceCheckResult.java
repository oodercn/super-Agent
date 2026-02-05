package net.ooder.nexus.model.system;

import java.util.Date;

public class ServiceCheckResult {
    private String id;
    private String serviceName;
    private String status;
    private String message;
    private long responseTime;
    private Date timestamp;
    private String serviceType;
    private String endpoint;
    private String details;

    public ServiceCheckResult() {
    }

    public ServiceCheckResult(String id, String serviceName, String status, String message, long responseTime, Date timestamp, String serviceType, String endpoint, String details) {
        this.id = id;
        this.serviceName = serviceName;
        this.status = status;
        this.message = message;
        this.responseTime = responseTime;
        this.timestamp = timestamp;
        this.serviceType = serviceType;
        this.endpoint = endpoint;
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}