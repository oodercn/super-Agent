package net.ooder.sdk.nexus.offline.model;

public class NetworkState {
    
    private boolean available;
    private String connectionType;
    private int latency;
    private long checkTime;
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public String getConnectionType() { return connectionType; }
    public void setConnectionType(String connectionType) { this.connectionType = connectionType; }
    
    public int getLatency() { return latency; }
    public void setLatency(int latency) { this.latency = latency; }
    
    public long getCheckTime() { return checkTime; }
    public void setCheckTime(long checkTime) { this.checkTime = checkTime; }
}
