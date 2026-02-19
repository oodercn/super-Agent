package net.ooder.sdk.api.event;

public class PeerDiscoveredEvent extends Event {
    
    private String peerId;
    private String peerName;
    private String ipAddress;
    
    public PeerDiscoveredEvent() {
        super();
    }
    
    public PeerDiscoveredEvent(String peerId, String peerName, String ipAddress) {
        super("DiscoveryProtocol");
        this.peerId = peerId;
        this.peerName = peerName;
        this.ipAddress = ipAddress;
    }
    
    public String getPeerId() { return peerId; }
    public void setPeerId(String peerId) { this.peerId = peerId; }
    
    public String getPeerName() { return peerName; }
    public void setPeerName(String peerName) { this.peerName = peerName; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}
