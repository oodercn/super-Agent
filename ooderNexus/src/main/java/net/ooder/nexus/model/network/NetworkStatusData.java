package net.ooder.nexus.model.network;

import java.io.Serializable;

public class NetworkStatusData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String status;
    private String message;
    private long timestamp;
    private int endAgentCount;
    private int routeAgentCount;
    private int totalConnections;
    private int activeConnections;
    private double packetLossRate;
    private double avgResponseTime;

    public NetworkStatusData() {
    }

    public NetworkStatusData(String status, String message, long timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getEndAgentCount() {
        return endAgentCount;
    }

    public void setEndAgentCount(int endAgentCount) {
        this.endAgentCount = endAgentCount;
    }

    public int getRouteAgentCount() {
        return routeAgentCount;
    }

    public void setRouteAgentCount(int routeAgentCount) {
        this.routeAgentCount = routeAgentCount;
    }

    public int getTotalConnections() {
        return totalConnections;
    }

    public void setTotalConnections(int totalConnections) {
        this.totalConnections = totalConnections;
    }

    public int getActiveConnections() {
        return activeConnections;
    }

    public void setActiveConnections(int activeConnections) {
        this.activeConnections = activeConnections;
    }

    public double getPacketLossRate() {
        return packetLossRate;
    }

    public void setPacketLossRate(double packetLossRate) {
        this.packetLossRate = packetLossRate;
    }

    public double getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(double avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }
}
