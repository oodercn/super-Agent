package net.ooder.os.skill.spi.org;

import java.util.List;

public interface OrgService {
    
    String getSkillId();
    
    OrgUserDTO getUser(String userId);
    
    OrgUserDTO getCurrentUser();
    
    List<OrgUserDTO> getAllUsers();
    
    List<OrgUserDTO> getUsersByRole(String role);
    
    List<OrgUserDTO> getUsersByDepartment(String departmentId);
    
    boolean isUserInRole(String userId, String role);
    
    void addUser(OrgUserDTO user);
    
    void updateUser(OrgUserDTO user);
    
    boolean deleteUser(String userId);
}
