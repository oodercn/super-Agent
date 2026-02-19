
package net.ooder.sdk.core.scene.capability;

import java.util.List;
import java.util.Map;

public interface CapabilityProvider {
    
    String getProviderId();
    
    String getProviderName();
    
    List<Capability> getCapabilities();
    
    Capability getCapability(String capabilityId);
    
    boolean hasCapability(String capabilityId);
    
    Object invoke(String capabilityId, Map<String, Object> params) throws CapabilityException;
    
    void start();
    
    void stop();
    
    boolean isRunning();
}
