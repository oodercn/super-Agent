
package net.ooder.sdk.southbound.adapter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.southbound.adapter.model.DiscoveryConfig;
import net.ooder.sdk.southbound.adapter.model.DiscoveredNode;
import net.ooder.sdk.southbound.protocol.model.DiscoveryListener;

public interface DiscoveryProtocolAdapter {
    
    void startDiscovery(DiscoveryConfig config);
    
    void stopDiscovery();
    
    boolean isDiscovering();
    
    List<DiscoveredNode> getDiscoveredNodes();
    
    CompletableFuture<List<DiscoveredNode>> discoverOnce(DiscoveryConfig config);
    
    void addDiscoveryListener(DiscoveryListener listener);
    
    void removeDiscoveryListener(DiscoveryListener listener);
}
