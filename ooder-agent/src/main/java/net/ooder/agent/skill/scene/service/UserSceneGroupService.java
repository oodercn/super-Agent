package net.ooder.mvp.skill.scene.service;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.scene.UserSceneGroupDTO;

import java.util.List;

public interface UserSceneGroupService {
    
    UserSceneGroupDTO getUserSceneGroup(String sceneGroupId, String userId);
    
    List<UserSceneGroupDTO> getUserSceneGroups(String userId);
    
    UserSceneGroupDTO joinSceneGroup(String sceneGroupId, String userId, String role);
    
    boolean leaveSceneGroup(String sceneGroupId, String userId);
    
    UserSceneGroupDTO updateRole(String sceneGroupId, String userId, String newRole);
    
    PageResult<UserSceneGroupDTO> listSceneGroupMembers(String sceneGroupId, int pageNum, int pageSize);
}
