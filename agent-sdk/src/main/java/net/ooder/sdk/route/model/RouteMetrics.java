package net.ooder.sdk.route.model;

import com.alibaba.fastjson.annotation.JSONField;

public class RouteMetrics {
    @JSONField(name = "latency")
    private long latency;
    
    @JSONField(name = "bandwidth")
    private long bandwidth;
    
    @JSONField(name = "packetLoss")
    private double packetLoss;
    
    @JSONField(name = "jitter")
    private long jitter;
    
    @JSONField(name = "hops")
    private int hops;
    
    @JSONField(name = "lastUpdated")
    private long lastUpdated;
    
    public RouteMetrics() {
        this.latency = 0;
        this.bandwidth = 0;
        this.packetLoss = 0.0;
        this.jitter = 0;
        this.hops = 0;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getLatency() {
        return latency;
    }

    public void setLatency(long latency) {
        this.latency = latency;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(long bandwidth) {
        this.bandwidth = bandwidth;
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

    public int getHops() {
        return hops;
    }

    public void setHops(int hops) {
        this.hops = hops;
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
        return "RouteMetrics{" +
                "latency=" + latency + "ms, " +
                "bandwidth=" + bandwidth + "bps, " +
                "packetLoss=" + packetLoss + "%, " +
                "jitter=" + jitter + "ms, " +
                "hops=" + hops +
                '}';
    }
}
