package net.ooder.nexus.skillcenter.dto.network;

public class NetworkQualityDTO {

    private int overallScore;
    private int latencyScore;
    private int bandwidthScore;
    private int stabilityScore;
    private double packetLoss;
    private double jitter;
    private double avgLatency;
    private double maxLatency;
    private double minLatency;
    private Long timestamp;

    public NetworkQualityDTO() {}

    public int getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(int overallScore) {
        this.overallScore = overallScore;
    }

    public int getLatencyScore() {
        return latencyScore;
    }

    public void setLatencyScore(int latencyScore) {
        this.latencyScore = latencyScore;
    }

    public int getBandwidthScore() {
        return bandwidthScore;
    }

    public void setBandwidthScore(int bandwidthScore) {
        this.bandwidthScore = bandwidthScore;
    }

    public int getStabilityScore() {
        return stabilityScore;
    }

    public void setStabilityScore(int stabilityScore) {
        this.stabilityScore = stabilityScore;
    }

    public double getPacketLoss() {
        return packetLoss;
    }

    public void setPacketLoss(double packetLoss) {
        this.packetLoss = packetLoss;
    }

    public double getJitter() {
        return jitter;
    }

    public void setJitter(double jitter) {
        this.jitter = jitter;
    }

    public double getAvgLatency() {
        return avgLatency;
    }

    public void setAvgLatency(double avgLatency) {
        this.avgLatency = avgLatency;
    }

    public double getMaxLatency() {
        return maxLatency;
    }

    public void setMaxLatency(double maxLatency) {
        this.maxLatency = maxLatency;
    }

    public double getMinLatency() {
        return minLatency;
    }

    public void setMinLatency(double minLatency) {
        this.minLatency = minLatency;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
