package net.ooder.sdk.route.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Route {
    @JSONField(name = "routeId")
    private String routeId;
    
    @JSONField(name = "sourceId")
    private String sourceId;
    
    @JSONField(name = "destinationId")
    private String destinationId;
    
    @JSONField(name = "path")
    private List<String> path;
    
    @JSONField(name = "metrics")
    private RouteMetrics metrics;
    
    @JSONField(name = "status")
    private RouteStatus status;
    
    @JSONField(name = "priority")
    private int priority;
    
    @JSONField(name = "createdAt")
    private long createdAt;
    
    @JSONField(name = "updatedAt")
    private long updatedAt;
    
    @JSONField(name = "metadata")
    private Map<String, Object> metadata;
    
    public Route() {
        this.routeId = UUID.randomUUID().toString();
        this.status = RouteStatus.AVAILABLE;
        this.priority = 0;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
    
    public Route(String sourceId, String destinationId, List<String> path) {
        this();
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.path = path;
        this.metrics = new RouteMetrics();
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
        this.updatedAt = System.currentTimeMillis();
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
        this.updatedAt = System.currentTimeMillis();
    }

    public RouteMetrics getMetrics() {
        return metrics;
    }

    public void setMetrics(RouteMetrics metrics) {
        this.metrics = metrics;
        this.updatedAt = System.currentTimeMillis();
    }

    public RouteStatus getStatus() {
        return status;
    }

    public void setStatus(RouteStatus status) {
        this.status = status;
        this.updatedAt = System.currentTimeMillis();
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
        this.updatedAt = System.currentTimeMillis();
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Route{" +
                "routeId='" + routeId + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", destinationId='" + destinationId + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                '}';
    }
}
