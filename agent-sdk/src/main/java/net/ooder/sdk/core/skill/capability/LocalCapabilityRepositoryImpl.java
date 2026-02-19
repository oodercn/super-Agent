
package net.ooder.sdk.core.skill.capability;

import net.ooder.sdk.api.skill.CapabilityInfo;
import net.ooder.sdk.api.skill.LocalCapabilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalCapabilityRepositoryImpl implements LocalCapabilityRepository {
    
    private static final Logger log = LoggerFactory.getLogger(LocalCapabilityRepositoryImpl.class);
    
    private final Map<String, CapabilityInfo> capabilities = new ConcurrentHashMap<>();
    private final Map<String, List<CapabilityInfo>> sceneCapabilities = new ConcurrentHashMap<>();
    
    @Override
    public void scanLocalCapabilities() {
        log.info("Scanning local capabilities...");
    }
    
    @Override
    public void registerCapability(CapabilityInfo capability) {
        if (capability == null || capability.getCapabilityId() == null) {
            return;
        }
        
        capabilities.put(capability.getCapabilityId(), capability);
        
        if (capability instanceof CapabilityInfoImpl) {
            String sceneName = ((CapabilityInfoImpl) capability).getSceneName();
            if (sceneName != null) {
                sceneCapabilities.computeIfAbsent(sceneName, k -> new ArrayList<>())
                    .add(capability);
            }
        }
        
        log.info("Registered capability: {}", capability.getCapabilityId());
    }
    
    @Override
    public void unregisterCapability(String capabilityId) {
        CapabilityInfo removed = capabilities.remove(capabilityId);
        if (removed != null) {
            for (List<CapabilityInfo> list : sceneCapabilities.values()) {
                list.remove(removed);
            }
            log.info("Unregistered capability: {}", capabilityId);
        }
    }
    
    @Override
    public CapabilityInfo findCapability(String capabilityId) {
        return capabilities.get(capabilityId);
    }
    
    @Override
    public List<CapabilityInfo> findCapabilitiesByScene(String sceneName) {
        return sceneCapabilities.getOrDefault(sceneName, new ArrayList<>());
    }
    
    @Override
    public List<CapabilityInfo> findCapabilitiesByType(CapabilityInfo.CapabilityType type) {
        List<CapabilityInfo> result = new ArrayList<>();
        for (CapabilityInfo cap : capabilities.values()) {
            if (cap.getType() == type) {
                result.add(cap);
            }
        }
        return result;
    }
    
    @Override
    public boolean hasCapability(String capabilityId) {
        return capabilities.containsKey(capabilityId);
    }
    
    @Override
    public List<CapabilityInfo> getAllCapabilities() {
        return new ArrayList<>(capabilities.values());
    }
    
    @Override
    public int getCapabilityCount() {
        return capabilities.size();
    }
}
