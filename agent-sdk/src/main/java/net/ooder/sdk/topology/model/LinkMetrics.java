package net.ooder.sdk.topology.model;

import com.alibaba.fastjson.annotation.JSONField;

public class LinkMetrics {
    @JSONField(name = "bandwidth")
    private long bandwidth;
    
    @JSONField(name = "latency")
    private long latency;
    
    @JSONField(name = "packetLoss")
    private double packetLoss;
    
    @JSONField(name = "jitter")
    private long jitter;
    
    @JSONField(name = "throughput")
    private long throughput;
    
    @JSONField(name = "utilization")
    private double utilization;
    
    @JSONField(name = "errorRate")
    private double errorRate;
    
    @JSONField(name = "lastUpdated")
    private long lastUpdated;
    
    public LinkMetrics() {
        this.bandwidth = 0;
        this.latency = 0;
        this.packetLoss = 0.0;
        this.jitter = 0;
        this.throughput = 0;
        this.utilization = 0.0;
        this.errorRate = 0.0;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(long bandwidth) {
        this.bandwidth = bandwidth;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getLatency() {
        return latency;
    }

    public void setLatency(long latency) {
        this.latency = latency;
        this.lastUpdated = System.currentTimeMillis();
    }

    public double getPacketLoss() {
        return packetLoss;
    }

    public void setPacketLoss(double packetLoss) {
        this.packetLoss = packetLoss;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getJitter() {
        return jitter;
    }

    public void setJitter(long jitter) {
        this.jitter = jitter;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getThroughput() {
        return throughput;
    }

    public void setThroughput(long throughput) {
        this.throughput = throughput;
        this.lastUpdated = System.currentTimeMillis();
    }

    public double getUtilization() {
        return utilization;
    }

    public void setUtilization(double utilization) {
        this.utilization = utilization;
        this.lastUpdated = System.currentTimeMillis();
    }

    public double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "LinkMetrics{" +
                "bandwidth=" + bandwidth + "bps, " +
                "latency=" + latency + "ms, " +
                "packetLoss=" + packetLoss + "%, " +
                "throughput=" + throughput + "bps, " +
                "utilization=" + utilization + "%" +
                '}';
    }
}
