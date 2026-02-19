package net.ooder.sdk.api.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Link Information
 *
 * @author ooder Team
 * @since 0.7.1
 */
public class LinkInfo {

    private String linkId;
    private String sourceId;
    private String targetId;
    private LinkType type;
    private LinkStatus status;
    private long createTime;
    private long establishedTime;
    private long lastActiveTime;
    private int reconnectCount;
    private long totalBytesSent;
    private long totalBytesReceived;
    private double avgLatency;
    private double packetLossRate;
    private String qualityLevel;
    private LinkQualityInfo quality;
    private Map<String, Object> metadata;

    public LinkInfo() {
        this.createTime = System.currentTimeMillis();
        this.establishedTime = System.currentTimeMillis();
        this.lastActiveTime = System.currentTimeMillis();
        this.status = LinkStatus.ACTIVE;
        this.quality = new LinkQualityInfo();
        this.metadata = new ConcurrentHashMap<>();
        this.reconnectCount = 0;
        this.totalBytesSent = 0;
        this.totalBytesReceived = 0;
        this.avgLatency = 0.0;
        this.packetLossRate = 0.0;
        this.qualityLevel = "GOOD";
    }

    public enum LinkType {
        DIRECT,
        ROUTED,
        TUNNEL,
        P2P,
        RELAY
    }

    public enum LinkStatus {
        ACTIVE,
        INACTIVE,
        DEGRADED,
        FAILED,
        PENDING
    }

    public String getLinkId() { return linkId; }
    public void setLinkId(String linkId) { this.linkId = linkId; }

    public String getSourceId() { return sourceId; }
    public void setSourceId(String sourceId) { this.sourceId = sourceId; }

    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }

    public LinkType getType() { return type; }
    public void setType(LinkType type) { this.type = type; }

    public LinkStatus getStatus() { return status; }
    public void setStatus(LinkStatus status) { this.status = status; }

    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }

    public long getLastActiveTime() { return lastActiveTime; }
    public void setLastActiveTime(long lastActiveTime) { this.lastActiveTime = lastActiveTime; }

    public LinkQualityInfo getQuality() { return quality; }
    public void setQuality(LinkQualityInfo quality) { this.quality = quality; }

    public long getEstablishedTime() { return establishedTime; }
    public void setEstablishedTime(long establishedTime) { this.establishedTime = establishedTime; }

    public int getReconnectCount() { return reconnectCount; }
    public void setReconnectCount(int reconnectCount) { this.reconnectCount = reconnectCount; }
    public void incrementReconnectCount() { this.reconnectCount++; }

    public long getTotalBytesSent() { return totalBytesSent; }
    public void setTotalBytesSent(long totalBytesSent) { this.totalBytesSent = totalBytesSent; }
    public void addBytesSent(long bytes) { this.totalBytesSent += bytes; }

    public long getTotalBytesReceived() { return totalBytesReceived; }
    public void setTotalBytesReceived(long totalBytesReceived) { this.totalBytesReceived = totalBytesReceived; }
    public void addBytesReceived(long bytes) { this.totalBytesReceived += bytes; }

    public double getAvgLatency() { return avgLatency; }
    public void setAvgLatency(double avgLatency) { this.avgLatency = avgLatency; }

    public double getPacketLossRate() { return packetLossRate; }
    public void setPacketLossRate(double packetLossRate) { this.packetLossRate = packetLossRate; }

    public String getQualityLevel() { return qualityLevel; }
    public void setQualityLevel(String qualityLevel) { this.qualityLevel = qualityLevel; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    public void addMetadata(String key, Object value) { this.metadata.put(key, value); }
    public Object getMetadata(String key) { return this.metadata.get(key); }

    @Override
    public String toString() {
        return "LinkInfo{" +
                "linkId='" + linkId + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", targetId='" + targetId + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", avgLatency=" + avgLatency +
                ", qualityLevel='" + qualityLevel + '\'' +
                '}';
    }
}
