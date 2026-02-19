package net.ooder.sdk.api.network;

/**
 * Link Quality Information
 *
 * @author ooder Team
 * @since 0.7.1
 */
public class LinkQualityInfo {

    private String linkId;
    private QualityLevel qualityLevel;
    private int latency;
    private double packetLoss;
    private double bandwidth;
    private long lastCheckTime;
    private int consecutiveFailures;
    private double score;

    public LinkQualityInfo() {
        this.qualityLevel = QualityLevel.UNKNOWN;
        this.lastCheckTime = System.currentTimeMillis();
        this.consecutiveFailures = 0;
        this.score = 100.0;
    }

    public enum QualityLevel {
        EXCELLENT(5),
        GOOD(4),
        FAIR(3),
        POOR(2),
        BAD(1),
        UNKNOWN(0);

        private final int level;

        QualityLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public static QualityLevel fromScore(double score) {
            if (score >= 90) return EXCELLENT;
            if (score >= 70) return GOOD;
            if (score >= 50) return FAIR;
            if (score >= 30) return POOR;
            if (score >= 10) return BAD;
            return UNKNOWN;
        }
    }

    public String getLinkId() { return linkId; }
    public void setLinkId(String linkId) { this.linkId = linkId; }

    public QualityLevel getQualityLevel() { return qualityLevel; }
    public void setQualityLevel(QualityLevel qualityLevel) { this.qualityLevel = qualityLevel; }

    public int getLatency() { return latency; }
    public void setLatency(int latency) { this.latency = latency; }

    public double getPacketLoss() { return packetLoss; }
    public void setPacketLoss(double packetLoss) { this.packetLoss = packetLoss; }

    public double getBandwidth() { return bandwidth; }
    public void setBandwidth(double bandwidth) { this.bandwidth = bandwidth; }

    public long getLastCheckTime() { return lastCheckTime; }
    public void setLastCheckTime(long lastCheckTime) { this.lastCheckTime = lastCheckTime; }

    public int getConsecutiveFailures() { return consecutiveFailures; }
    public void setConsecutiveFailures(int consecutiveFailures) { this.consecutiveFailures = consecutiveFailures; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public void calculateScore() {
        double latencyScore = Math.max(0, 100 - latency / 10.0);
        double lossScore = Math.max(0, 100 - packetLoss * 100);
        double failureScore = Math.max(0, 100 - consecutiveFailures * 20);
        
        this.score = (latencyScore * 0.4 + lossScore * 0.4 + failureScore * 0.2);
        this.qualityLevel = QualityLevel.fromScore(this.score);
    }

    @Override
    public String toString() {
        return "LinkQualityInfo{" +
                "linkId='" + linkId + '\'' +
                ", qualityLevel=" + qualityLevel +
                ", latency=" + latency + "ms" +
                ", packetLoss=" + String.format("%.2f", packetLoss * 100) + "%" +
                ", score=" + String.format("%.1f", score) +
                '}';
    }
}
