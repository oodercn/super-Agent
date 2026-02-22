package net.ooder.nexus.service;

/**
 * 对等节点信息
 */
public class PeerInfo {
    
    private String peerId;
    private String peerName;
    private String address;
    private int port;
    private String role;
    private long lastSeen;
    private boolean online;

    public PeerInfo() {}

    public String getPeerId() { return peerId; }
    public void setPeerId(String peerId) { this.peerId = peerId; }
    
    public String getPeerName() { return peerName; }
    public void setPeerName(String peerName) { this.peerName = peerName; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public long getLastSeen() { return lastSeen; }
    public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }
    
    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }
}
