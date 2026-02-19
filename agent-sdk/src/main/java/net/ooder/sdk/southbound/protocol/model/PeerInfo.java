package net.ooder.sdk.southbound.protocol.model;

public class PeerInfo {
    
    private String peerId;
    private String peerName;
    private PeerType peerType;
    private String ipAddress;
    private int port;
    private long lastSeen;
    private String domainId;
    
    public String getPeerId() { return peerId; }
    public void setPeerId(String peerId) { this.peerId = peerId; }
    
    public String getPeerName() { return peerName; }
    public void setPeerName(String peerName) { this.peerName = peerName; }
    
    public PeerType getPeerType() { return peerType; }
    public void setPeerType(PeerType peerType) { this.peerType = peerType; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    
    public long getLastSeen() { return lastSeen; }
    public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }
    
    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
}
