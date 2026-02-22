package net.ooder.nexus.skillcenter.dto.domain;

import java.util.List;
import java.util.Map;

public class DynamicRoutingDTO {
    private String routingId;
    private String domainId;
    private RoutingState state;
    private List<RouteDTO> routes;
    private List<RoutingEventDTO> events;
    private long timestamp;

    public String getRoutingId() { return routingId; }
    public void setRoutingId(String routingId) { this.routingId = routingId; }
    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
    public RoutingState getState() { return state; }
    public void setState(RoutingState state) { this.state = state; }
    public List<RouteDTO> getRoutes() { return routes; }
    public void setRoutes(List<RouteDTO> routes) { this.routes = routes; }
    public List<RoutingEventDTO> getEvents() { return events; }
    public void setEvents(List<RoutingEventDTO> events) { this.events = events; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public static class RoutingState {
        private int totalRoutes;
        private int activeRoutes;
        private int failedRoutes;
        private double avgLatency;
        private double avgBandwidth;
        private String healthStatus;

        public int getTotalRoutes() { return totalRoutes; }
        public void setTotalRoutes(int totalRoutes) { this.totalRoutes = totalRoutes; }
        public int getActiveRoutes() { return activeRoutes; }
        public void setActiveRoutes(int activeRoutes) { this.activeRoutes = activeRoutes; }
        public int getFailedRoutes() { return failedRoutes; }
        public void setFailedRoutes(int failedRoutes) { this.failedRoutes = failedRoutes; }
        public double getAvgLatency() { return avgLatency; }
        public void setAvgLatency(double avgLatency) { this.avgLatency = avgLatency; }
        public double getAvgBandwidth() { return avgBandwidth; }
        public void setAvgBandwidth(double avgBandwidth) { this.avgBandwidth = avgBandwidth; }
        public String getHealthStatus() { return healthStatus; }
        public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    }

    public static class RouteDTO {
        private String routeId;
        private String sourceNodeId;
        private String targetNodeId;
        private List<String> hops;
        private String status;
        private int currentLatency;
        private int currentBandwidth;
        private long lastUpdated;
        private int hopCount;

        public String getRouteId() { return routeId; }
        public void setRouteId(String routeId) { this.routeId = routeId; }
        public String getSourceNodeId() { return sourceNodeId; }
        public void setSourceNodeId(String sourceNodeId) { this.sourceNodeId = sourceNodeId; }
        public String getTargetNodeId() { return targetNodeId; }
        public void setTargetNodeId(String targetNodeId) { this.targetNodeId = targetNodeId; }
        public List<String> getHops() { return hops; }
        public void setHops(List<String> hops) { this.hops = hops; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getCurrentLatency() { return currentLatency; }
        public void setCurrentLatency(int currentLatency) { this.currentLatency = currentLatency; }
        public int getCurrentBandwidth() { return currentBandwidth; }
        public void setCurrentBandwidth(int currentBandwidth) { this.currentBandwidth = currentBandwidth; }
        public long getLastUpdated() { return lastUpdated; }
        public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }
        public int getHopCount() { return hopCount; }
        public void setHopCount(int hopCount) { this.hopCount = hopCount; }
    }

    public static class RoutingEventDTO {
        private String eventId;
        private String eventType;
        private String routeId;
        private String message;
        private String severity;
        private long eventTime;
        private Map<String, Object> details;

        public String getEventId() { return eventId; }
        public void setEventId(String eventId) { this.eventId = eventId; }
        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }
        public String getRouteId() { return routeId; }
        public void setRouteId(String routeId) { this.routeId = routeId; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }
        public long getEventTime() { return eventTime; }
        public void setEventTime(long eventTime) { this.eventTime = eventTime; }
        public Map<String, Object> getDetails() { return details; }
        public void setDetails(Map<String, Object> details) { this.details = details; }
    }
}
