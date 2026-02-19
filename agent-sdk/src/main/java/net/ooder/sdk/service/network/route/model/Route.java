
package net.ooder.sdk.service.network.route.model;

import java.util.List;

public class Route {
    
    private String routeId;
    private String sourceId;
    private String destinationId;
    private List<String> hops;
    private String status;
    private long createTime;
    private long updateTime;
    private int metric;
    
    public Route() {
        this.createTime = System.currentTimeMillis();
        this.updateTime = System.currentTimeMillis();
        this.status = "inactive";
        this.metric = 0;
    }
    
    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }
    
    public String getSourceId() { return sourceId; }
    public void setSourceId(String sourceId) { this.sourceId = sourceId; }
    
    public String getDestinationId() { return destinationId; }
    public void setDestinationId(String destinationId) { this.destinationId = destinationId; }
    
    public List<String> getHops() { return hops; }
    public void setHops(List<String> hops) { this.hops = hops; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status; 
        this.updateTime = System.currentTimeMillis();
    }
    
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    
    public long getUpdateTime() { return updateTime; }
    public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
    
    public int getMetric() { return metric; }
    public void setMetric(int metric) { this.metric = metric; }
    
    public int getHopCount() {
        return hops != null ? hops.size() - 1 : 0;
    }
    
    public boolean isActive() {
        return "active".equals(status);
    }
}
