package net.ooder.sdk.core.scene.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface EndpointAllocator {
    
    String allocateEndpoint(String skillId, String protocol);
    
    void releaseEndpoint(String endpoint);
    
    boolean isEndpointAvailable(String endpoint);
    
    List<String> getAllocatedEndpoints();
    
    EndpointInfo getEndpointInfo(String endpoint);
    
    List<EndpointInfo> getEndpointsBySkill(String skillId);
}
