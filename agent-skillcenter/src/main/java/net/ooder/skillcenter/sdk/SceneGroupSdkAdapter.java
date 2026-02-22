package net.ooder.skillcenter.sdk;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.scene.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public interface SceneGroupSdkAdapter {
    
    SceneGroupDTO createSceneGroup(String sceneId, SceneGroupConfigDTO config);
    
    boolean destroySceneGroup(String sceneGroupId);
    
    SceneGroupDTO getSceneGroup(String sceneGroupId);
    
    PageResult<SceneGroupDTO> listSceneGroups(int pageNum, int pageSize);
    
    boolean joinSceneGroup(String sceneGroupId, String agentId, String role);
    
    boolean leaveSceneGroup(String sceneGroupId, String agentId);
    
    PageResult<SceneMemberDTO> listMembers(String sceneGroupId, int pageNum, int pageSize);
    
    SceneMemberDTO getPrimaryMember(String sceneGroupId);
    
    boolean handleFailover(String sceneGroupId, String failedMemberId);
    
    FailoverStatusDTO getFailoverStatus(String sceneGroupId);
    
    SceneGroupKeyDTO generateKey(String sceneGroupId);
    
    SceneGroupKeyDTO reconstructKey(String sceneGroupId, List<KeyShareDTO> shares);
    
    boolean distributeKeyShares(String sceneGroupId, SceneGroupKeyDTO key);
    
    VfsPermissionDTO getVfsPermission(String sceneGroupId, String agentId);
    
    PageResult<VfsPermissionDTO> listVfsPermissions(String sceneGroupId, int pageNum, int pageSize);
    
    VfsPermissionDTO addVfsPermission(String sceneGroupId, String agentId, String permissionType, String path);
    
    boolean isAvailable();
}
