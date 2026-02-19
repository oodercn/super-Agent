
package net.ooder.sdk.service.network.route.model;

public class RouteInfo {
    
    private String routeId;
    private String sourceId;
    private String destinationId;
    private int hopCount;
    private String status;
    private long latency;
    
    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }
    
    public String getSourceId() { return sourceId; }
    public void setSourceId(String sourceId) { this.sourceId = sourceId; }
    
    public String getDestinationId() { return destinationId; }
    public void setDestinationId(String destinationId) { this.destinationId = destinationId; }
    
    public int getHopCount() { return hopCount; }
    public void setHopCount(int hopCount) { this.hopCount = hopCount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public long getLatency() { return latency; }
    public void setLatency(long latency) { this.latency = latency; }
}
