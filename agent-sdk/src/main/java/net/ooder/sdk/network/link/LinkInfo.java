package net.ooder.sdk.network.link;

import java.time.Instant;
import java.util.Map;

public class LinkInfo {
    private final String linkId;
    private final String sourceNodeId;
    private final String targetNodeId;
    private LinkStatus status;
    private double quality;
    private String sceneId;
    private String groupId;
    private Map<String, Object> metadata;
    private Instant lastUpdated;
    
    // 性能指标
    private long latency;
    private long bandwidth;
    private double packetLoss;
    private long jitter;
    private long throughput;
    private double utilization;
    private double errorRate;
    
    // 统计信息
    private long uptime;
    private long downtime;
    private int totalPacketsSent;
    private int totalPacketsReceived;
    private int totalErrors;
    
    public LinkInfo(String linkId, String sourceNodeId, String targetNodeId) {
        this.linkId = linkId;
        this.sourceNodeId = sourceNodeId;
        this.targetNodeId = targetNodeId;
        this.status = LinkStatus.ACTIVE;
        this.quality = 1.0;
        this.metadata = new java.util.HashMap<>();
        this.lastUpdated = Instant.now();
        
        // 初始化性能指标
        this.latency = 0;
        this.bandwidth = 0;
        this.packetLoss = 0.0;
        this.jitter = 0;
        this.throughput = 0;
        this.utilization = 0.0;
        this.errorRate = 0.0;
        
        // 初始化统计信息
        this.uptime = 0;
        this.downtime = 0;
        this.totalPacketsSent = 0;
        this.totalPacketsReceived = 0;
        this.totalErrors = 0;
    }
    
    public String getLinkId() {
        return linkId;
    }
    
    public String getSourceNodeId() {
        return sourceNodeId;
    }
    
    public String getTargetNodeId() {
        return targetNodeId;
    }
    
    public LinkStatus getStatus() {
        return status;
    }
    
    public void setStatus(LinkStatus status) {
        this.status = status;
        this.lastUpdated = Instant.now();
    }
    
    public double getQuality() {
        return quality;
    }
    
    public void setQuality(double quality) {
        this.quality = quality;
        this.lastUpdated = Instant.now();
    }
    
    public String getSceneId() {
        return sceneId;
    }
    
    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
        this.lastUpdated = Instant.now();
    }
    
    public String getGroupId() {
        return groupId;
    }
    
    public void setGroupId(String groupId) {
        this.groupId = groupId;
        this.lastUpdated = Instant.now();
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
        this.lastUpdated = Instant.now();
    }
    
    public Instant getLastUpdated() {
        return lastUpdated;
    }
    
    // 性能指标的getter和setter方法
    public long getLatency() {
        return latency;
    }
    
    public void setLatency(long latency) {
        this.latency = latency;
        this.lastUpdated = Instant.now();
    }
    
    public long getBandwidth() {
        return bandwidth;
    }
    
    public void setBandwidth(long bandwidth) {
        this.bandwidth = bandwidth;
        this.lastUpdated = Instant.now();
    }
    
    public double getPacketLoss() {
        return packetLoss;
    }
    
    public void setPacketLoss(double packetLoss) {
        this.packetLoss = packetLoss;
        this.lastUpdated = Instant.now();
    }
    
    public long getJitter() {
        return jitter;
    }
    
    public void setJitter(long jitter) {
        this.jitter = jitter;
        this.lastUpdated = Instant.now();
    }
    
    public long getThroughput() {
        return throughput;
    }
    
    public void setThroughput(long throughput) {
        this.throughput = throughput;
        this.lastUpdated = Instant.now();
    }
    
    public double getUtilization() {
        return utilization;
    }
    
    public void setUtilization(double utilization) {
        this.utilization = utilization;
        this.lastUpdated = Instant.now();
    }
    
    public double getErrorRate() {
        return errorRate;
    }
    
    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
        this.lastUpdated = Instant.now();
    }
    
    // 统计信息的getter和setter方法
    public long getUptime() {
        return uptime;
    }
    
    public void setUptime(long uptime) {
        this.uptime = uptime;
        this.lastUpdated = Instant.now();
    }
    
    public long getDowntime() {
        return downtime;
    }
    
    public void setDowntime(long downtime) {
        this.downtime = downtime;
        this.lastUpdated = Instant.now();
    }
    
    public int getTotalPacketsSent() {
        return totalPacketsSent;
    }
    
    public void setTotalPacketsSent(int totalPacketsSent) {
        this.totalPacketsSent = totalPacketsSent;
        this.lastUpdated = Instant.now();
    }
    
    public int getTotalPacketsReceived() {
        return totalPacketsReceived;
    }
    
    public void setTotalPacketsReceived(int totalPacketsReceived) {
        this.totalPacketsReceived = totalPacketsReceived;
        this.lastUpdated = Instant.now();
    }
    
    public int getTotalErrors() {
        return totalErrors;
    }
    
    public void setTotalErrors(int totalErrors) {
        this.totalErrors = totalErrors;
        this.lastUpdated = Instant.now();
    }
    
    // 增加数据包统计
    public void incrementPacketsSent() {
        this.totalPacketsSent++;
        this.lastUpdated = Instant.now();
    }
    
    public void incrementPacketsReceived() {
        this.totalPacketsReceived++;
        this.lastUpdated = Instant.now();
    }
    
    public void incrementErrors() {
        this.totalErrors++;
        this.lastUpdated = Instant.now();
    }
    
    @Override
    public String toString() {
        return "LinkInfo{" +
                "linkId='" + linkId + '\'' +
                ", sourceNodeId='" + sourceNodeId + '\'' +
                ", targetNodeId='" + targetNodeId + '\'' +
                ", status=" + status +
                ", quality=" + quality +
                ", latency=" + latency + "ms" +
                ", bandwidth=" + bandwidth + "bps" +
                ", packetLoss=" + packetLoss + "%" +
                ", throughput=" + throughput + "bps" +
                ", utilization=" + utilization + "%" +
                ", uptime=" + uptime + "s" +
                ", sceneId='" + sceneId + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
