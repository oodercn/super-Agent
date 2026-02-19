package net.ooder.sdk.api.event;

public class PeerLostEvent extends Event {
    
    private String peerId;
    
    public PeerLostEvent() {
        super();
    }
    
    public PeerLostEvent(String peerId) {
        super("DiscoveryProtocol");
        this.peerId = peerId;
    }
    
    public String getPeerId() { return peerId; }
    public void setPeerId(String peerId) { this.peerId = peerId; }
}
