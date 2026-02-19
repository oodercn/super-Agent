
package net.ooder.sdk.api.skill;

import java.util.List;

public interface LocalCapabilityRepository {
    
    void scanLocalCapabilities();
    
    void registerCapability(CapabilityInfo capability);
    
    void unregisterCapability(String capabilityId);
    
    CapabilityInfo findCapability(String capabilityId);
    
    List<CapabilityInfo> findCapabilitiesByScene(String sceneName);
    
    List<CapabilityInfo> findCapabilitiesByType(CapabilityInfo.CapabilityType type);
    
    boolean hasCapability(String capabilityId);
    
    List<CapabilityInfo> getAllCapabilities();
    
    int getCapabilityCount();
}
