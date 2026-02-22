package net.ooder.skillcenter.sdk;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.scene.*;

public interface SceneSdkAdapter {
    
    SceneDefinitionDTO createScene(SceneDefinitionDTO definition);
    
    boolean deleteScene(String sceneId);
    
    SceneDefinitionDTO getScene(String sceneId);
    
    PageResult<SceneDefinitionDTO> listScenes(int pageNum, int pageSize);
    
    boolean activateScene(String sceneId);
    
    boolean deactivateScene(String sceneId);
    
    SceneStateDTO getSceneState(String sceneId);
    
    boolean addCapability(String sceneId, CapabilityDTO capability);
    
    boolean removeCapability(String sceneId, String capabilityId);
    
    PageResult<CapabilityDTO> listCapabilities(String sceneId, int pageNum, int pageSize);
    
    CapabilityDTO getCapability(String sceneId, String capabilityId);
    
    boolean addRole(String sceneId, SceneRoleDTO role);
    
    boolean removeRole(String sceneId, String roleId);
    
    PageResult<SceneRoleDTO> listRoles(String sceneId, int pageNum, int pageSize);
    
    SceneSnapshotDTO createSnapshot(String sceneId);
    
    boolean restoreSnapshot(String sceneId, String snapshotId);
    
    PageResult<SceneSnapshotDTO> listSnapshots(String sceneId, int pageNum, int pageSize);
    
    boolean deleteSnapshot(String sceneId, String snapshotId);
    
    boolean isAvailable();
}
