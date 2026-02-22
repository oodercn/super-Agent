package net.ooder.nexus.skillcenter.dto.network;

public class NetworkStatsDTO {

    private int totalNodes;
    private int activeNodes;
    private int totalLinks;
    private int activeLinks;
    private int totalRoutes;
    private int activeRoutes;
    private double avgLatency;
    private double avgBandwidth;
    private long totalBytesSent;
    private long totalBytesReceived;
    private long timestamp;

    public NetworkStatsDTO() {}

    public int getTotalNodes() {
        return totalNodes;
    }

    public void setTotalNodes(int totalNodes) {
        this.totalNodes = totalNodes;
    }

    public int getActiveNodes() {
        return activeNodes;
    }

    public void setActiveNodes(int activeNodes) {
        this.activeNodes = activeNodes;
    }

    public int getTotalLinks() {
        return totalLinks;
    }

    public void setTotalLinks(int totalLinks) {
        this.totalLinks = totalLinks;
    }

    public int getActiveLinks() {
        return activeLinks;
    }

    public void setActiveLinks(int activeLinks) {
        this.activeLinks = activeLinks;
    }

    public int getTotalRoutes() {
        return totalRoutes;
    }

    public void setTotalRoutes(int totalRoutes) {
        this.totalRoutes = totalRoutes;
    }

    public int getActiveRoutes() {
        return activeRoutes;
    }

    public void setActiveRoutes(int activeRoutes) {
        this.activeRoutes = activeRoutes;
    }

    public double getAvgLatency() {
        return avgLatency;
    }

    public void setAvgLatency(double avgLatency) {
        this.avgLatency = avgLatency;
    }

    public double getAvgBandwidth() {
        return avgBandwidth;
    }

    public void setAvgBandwidth(double avgBandwidth) {
        this.avgBandwidth = avgBandwidth;
    }

    public long getTotalBytesSent() {
        return totalBytesSent;
    }

    public void setTotalBytesSent(long totalBytesSent) {
        this.totalBytesSent = totalBytesSent;
    }

    public long getTotalBytesReceived() {
        return totalBytesReceived;
    }

    public void setTotalBytesReceived(long totalBytesReceived) {
        this.totalBytesReceived = totalBytesReceived;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
