package net.ooder.sdk.southbound.protocol.model;

import net.ooder.sdk.southbound.protocol.model.DiscoveryResult;
import net.ooder.sdk.southbound.protocol.model.PeerInfo;

public interface DiscoveryListener {
    
    void onPeerDiscovered(PeerInfo peer);
    
    void onPeerLost(String peerId);
    
    void onMcpDiscovered(PeerInfo mcp);
    
    void onDiscoveryComplete(DiscoveryResult result);
}
