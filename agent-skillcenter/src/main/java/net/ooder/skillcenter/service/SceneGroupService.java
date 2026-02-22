package net.ooder.skillcenter.service;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.scene.*;

public interface SceneGroupService {
    
    SceneGroupDTO create(String sceneId, SceneGroupConfigDTO config);
    
    boolean destroy(String sceneGroupId);
    
    SceneGroupDTO get(String sceneGroupId);
    
    PageResult<SceneGroupDTO> listAll(int pageNum, int pageSize);
    
    PageResult<SceneGroupDTO> listByScene(String sceneId, int pageNum, int pageSize);
    
    boolean join(String sceneGroupId, String agentId, String role);
    
    boolean leave(String sceneGroupId, String agentId);
    
    boolean changeRole(String sceneGroupId, String agentId, String newRole);
    
    String getRole(String sceneGroupId, String agentId);
    
    PageResult<SceneMemberDTO> listMembers(String sceneGroupId, int pageNum, int pageSize);
    
    SceneMemberDTO getPrimary(String sceneGroupId);
    
    PageResult<SceneMemberDTO> getBackups(String sceneGroupId, int pageNum, int pageSize);
    
    boolean handleFailover(String sceneGroupId, String failedMemberId);
    
    FailoverStatusDTO getFailoverStatus(String sceneGroupId);
    
    boolean startHeartbeat(String sceneGroupId);
    
    boolean stopHeartbeat(String sceneGroupId);
    
    SceneGroupKeyDTO generateKey(String sceneGroupId);
    
    SceneGroupKeyDTO reconstructKey(String sceneGroupId, java.util.List<KeyShareDTO> shares);
    
    boolean distributeKeyShares(String sceneGroupId, SceneGroupKeyDTO key);
    
    VfsPermissionDTO getVfsPermission(String sceneGroupId, String agentId);
    
    PageResult<VfsPermissionDTO> listVfsPermissions(String sceneGroupId, int pageNum, int pageSize);
    
    VfsPermissionDTO addVfsPermission(String sceneGroupId, String agentId, String permissionType, String path);
}
