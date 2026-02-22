package net.ooder.skillcenter.service;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.GroupDTO;

public interface GroupService {
    
    PageResult<GroupDTO> getAllGroups(int pageNum, int pageSize);
    
    PageResult<GroupDTO> searchGroups(String keyword, int pageNum, int pageSize);
    
    GroupDTO getGroupById(String groupId);
    
    GroupDTO createGroup(GroupDTO groupDTO);
    
    GroupDTO updateGroup(String groupId, GroupDTO groupDTO);
    
    boolean deleteGroup(String groupId);
}
