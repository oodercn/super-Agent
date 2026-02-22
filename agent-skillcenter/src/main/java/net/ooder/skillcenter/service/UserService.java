package net.ooder.skillcenter.service;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.UserDTO;

public interface UserService {
    
    int getUserCount();
    
    int getActiveUserCount();
    
    PageResult<UserDTO> getAllUsers(int pageNum, int pageSize);
    
    PageResult<UserDTO> searchUsers(String keyword, int pageNum, int pageSize);
    
    PageResult<UserDTO> getUsersByGroup(String groupId, int pageNum, int pageSize);
    
    UserDTO getUserById(String userId);
    
    UserDTO createUser(UserDTO userDTO);
    
    UserDTO updateUser(String userId, UserDTO userDTO);
    
    boolean deleteUser(String userId);
}
