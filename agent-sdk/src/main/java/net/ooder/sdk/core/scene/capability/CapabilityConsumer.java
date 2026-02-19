
package net.ooder.sdk.core.scene.capability;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CapabilityConsumer {
    
    String getConsumerId();
    
    List<Capability> discoverCapabilities(String query);
    
    Capability getCapability(String capabilityId);
    
    Object invoke(String capabilityId, Map<String, Object> params) throws CapabilityException;
    
    CompletableFuture<Object> invokeAsync(String capabilityId, Map<String, Object> params);
    
    void subscribe(String capabilityId, CapabilityListener listener);
    
    void unsubscribe(String capabilityId, CapabilityListener listener);
    
    interface CapabilityListener {
        void onCapabilityChanged(Capability capability);
        void onCapabilityRemoved(String capabilityId);
    }
}
