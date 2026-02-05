package net.ooder.sdk.route.calculator;

public class RouteConstraints {
    private int maxHops;
    private long maxLatency;
    private long minBandwidth;
    private double maxPacketLoss;
    private long maxJitter;
    private boolean avoidSpecificNodes;
    private boolean preferHighBandwidth;
    private boolean preferLowLatency;
    
    public RouteConstraints() {
        this.maxHops = Integer.MAX_VALUE;
        this.maxLatency = Long.MAX_VALUE;
        this.minBandwidth = 0;
        this.maxPacketLoss = 1.0;
        this.maxJitter = Long.MAX_VALUE;
        this.avoidSpecificNodes = false;
        this.preferHighBandwidth = false;
        this.preferLowLatency = false;
    }

    public int getMaxHops() {
        return maxHops;
    }

    public void setMaxHops(int maxHops) {
        this.maxHops = maxHops;
    }

    public long getMaxLatency() {
        return maxLatency;
    }

    public void setMaxLatency(long maxLatency) {
        this.maxLatency = maxLatency;
    }

    public long getMinBandwidth() {
        return minBandwidth;
    }

    public void setMinBandwidth(long minBandwidth) {
        this.minBandwidth = minBandwidth;
    }

    public double getMaxPacketLoss() {
        return maxPacketLoss;
    }

    public void setMaxPacketLoss(double maxPacketLoss) {
        this.maxPacketLoss = maxPacketLoss;
    }

    public long getMaxJitter() {
        return maxJitter;
    }

    public void setMaxJitter(long maxJitter) {
        this.maxJitter = maxJitter;
    }

    public boolean isAvoidSpecificNodes() {
        return avoidSpecificNodes;
    }

    public void setAvoidSpecificNodes(boolean avoidSpecificNodes) {
        this.avoidSpecificNodes = avoidSpecificNodes;
    }

    public boolean isPreferHighBandwidth() {
        return preferHighBandwidth;
    }

    public void setPreferHighBandwidth(boolean preferHighBandwidth) {
        this.preferHighBandwidth = preferHighBandwidth;
    }

    public boolean isPreferLowLatency() {
        return preferLowLatency;
    }

    public void setPreferLowLatency(boolean preferLowLatency) {
        this.preferLowLatency = preferLowLatency;
    }
}
