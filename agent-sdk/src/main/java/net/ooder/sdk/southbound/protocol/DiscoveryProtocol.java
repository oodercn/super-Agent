package net.ooder.sdk.southbound.protocol;

import net.ooder.sdk.southbound.protocol.model.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DiscoveryProtocol {
    
    CompletableFuture<DiscoveryResult> discover(DiscoveryRequest request);
    
    CompletableFuture<List<PeerInfo>> discoverPeers();
    
    CompletableFuture<PeerInfo> discoverMcp();
    
    void addDiscoveryListener(DiscoveryListener listener);
    
    void removeDiscoveryListener(DiscoveryListener listener);
    
    void startBroadcast();
    
    void stopBroadcast();
    
    boolean isBroadcasting();
}
