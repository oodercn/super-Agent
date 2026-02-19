
package net.ooder.sdk.service.network.link;

public class LinkStats {
    
    private long lastLatency;
    private double packetLoss;
    private long packetsSent;
    private long packetsReceived;
    private long lastCheckTime;
    private LinkQuality quality;
    
    public long getLastLatency() { return lastLatency; }
    public void setLastLatency(long lastLatency) { this.lastLatency = lastLatency; }
    
    public double getPacketLoss() { return packetLoss; }
    public void setPacketLoss(double packetLoss) { this.packetLoss = packetLoss; }
    
    public long getPacketsSent() { return packetsSent; }
    public long getPacketsReceived() { return packetsReceived; }
    
    public void incrementPacketsSent() { this.packetsSent++; }
    public void incrementPacketsReceived() { this.packetsReceived++; }
    
    public long getLastCheckTime() { return lastCheckTime; }
    public void setLastCheckTime(long lastCheckTime) { this.lastCheckTime = lastCheckTime; }
    
    public LinkQuality getQuality() { return quality; }
    public void setQuality(LinkQuality quality) { this.quality = quality; }
}
