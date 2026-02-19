
package net.ooder.sdk.api.skill;

import java.util.Map;

public interface CapabilityInfo {
    
    String getCapabilityId();
    
    String getCapabilityName();
    
    String getDescription();
    
    String getInterfaceId();
    
    String getEndpoint();
    
    String getProtocol();
    
    Map<String, Object> getConfig();
    
    CapabilityType getType();
    
    enum CapabilityType {
        SERVICE,
        STORAGE,
        COMMUNICATION,
        COMPUTATION
    }
}
