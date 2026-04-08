package net.ooder.mvp.skill.scene.capability.service;

import net.ooder.mvp.skill.scene.capability.model.CapabilityState;
import net.ooder.mvp.skill.scene.capability.model.CapabilityStatus;

import java.util.List;
import java.util.Map;

public interface CapabilityStateService {
    
    CapabilityState getState(String capabilityId);
    
    boolean isInstalled(String capabilityId);
    
    CapabilityStatus getStatus(String capabilityId);
    
    void setInstalled(String capabilityId, boolean installed);
    
    void setInstalled(String capabilityId, boolean installed, String userId, String source);
    
    void setStatus(String capabilityId, CapabilityStatus status);
    
    void updateState(String capabilityId, boolean installed, CapabilityStatus status);
    
    void updateState(String capabilityId, boolean installed, CapabilityStatus status, 
                     String userId, String source, String sceneGroupId);
    
    void removeState(String capabilityId);
    
    List<CapabilityState> listAllStates();
    
    List<CapabilityState> listInstalledStates();
    
    List<CapabilityState> listByStatus(CapabilityStatus status);
    
    Map<String, CapabilityState> getAllStates();
    
    void saveAll();
    
    void reload();
    
    String getSceneGroupId(String capabilityId);
    
    void setSceneGroupId(String capabilityId, String sceneGroupId);
}
