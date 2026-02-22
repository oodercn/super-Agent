package net.ooder.skillcenter.service;

import net.ooder.skillcenter.dto.*;

import java.util.List;
import java.util.Map;

/**
 * 管理中心服务接口
 * 包含仪表盘、技能管理、市场管理、认证、群组、用户、托管、存储、系统管理等所有功能
 */
public interface AdminService {

    // ==================== 管理仪表盘 ====================
    Map<String, Object> getDashboardStats();

    // ==================== 技能管理 ====================
    PageResult<SkillDTO> getAllSkills(String category, String status, String keyword, int pageNum, int pageSize);
    SkillDTO getSkillById(String skillId);
    SkillDTO addSkill(SkillDTO skillDTO);
    SkillDTO updateSkill(String skillId, SkillDTO skillDTO);
    boolean deleteSkill(String skillId);
    boolean approveSkill(String skillId);
    boolean rejectSkill(String skillId);

    // ==================== 市场管理 ====================
    PageResult<SkillDTO> getMarketSkills(String category, String status, String keyword, int pageNum, int pageSize);
    SkillDTO getMarketSkill(String skillId);
    SkillDTO addMarketSkill(SkillDTO skillDTO);
    SkillDTO updateMarketSkill(String skillId, SkillDTO skillDTO);
    boolean removeMarketSkill(String skillId);
    List<SkillDTO> getMarketSkillsByCategory(String category);
    List<SkillDTO> getPopularMarketSkills(int limit);
    List<SkillDTO> getLatestMarketSkills(int limit);

    // ==================== 技能认证 ====================
    List<SkillAuthenticationDTO> getAuthenticationRequests();
    SkillAuthenticationDTO getAuthentication(String id);
    SkillAuthenticationDTO createAuthentication(SkillAuthenticationDTO authentication);
    SkillAuthenticationDTO updateAuthenticationStatus(String id, String status, String reviewer, String comments);
    boolean deleteAuthentication(String id);
    SkillAuthenticationDTO issueCertificate(SkillAuthenticationDTO request);

    // ==================== 群组管理 ====================
    List<GroupDTO> getAllGroups();
    GroupDTO getGroup(String groupId);
    GroupDTO createGroup(GroupDTO group);
    GroupDTO updateGroup(String groupId, GroupDTO group);
    boolean deleteGroup(String groupId);
    List<GroupDTO> searchGroups(String keyword);

    // ==================== 用户管理 ====================
    List<UserDTO> getAllUsers();
    UserDTO getUser(String userId);
    UserDTO createUser(UserDTO user);
    UserDTO updateUser(String userId, UserDTO user);
    boolean deleteUser(String userId);
    List<UserDTO> searchUsers(String keyword);
    List<UserDTO> getGroupMembers(String groupId);

    // ==================== 远程托管 ====================
    List<HostingInstanceDTO> getHostingInstances();
    HostingInstanceDTO getHostingInstance(String instanceId);
    HostingInstanceDTO createHostingInstance(HostingInstanceDTO instance);
    HostingInstanceDTO updateHostingInstance(String instanceId, HostingInstanceDTO instance);
    boolean deleteHostingInstance(String instanceId);
    HostingInstanceDTO startHostingInstance(String instanceId);
    HostingInstanceDTO stopHostingInstance(String instanceId);
    List<HostingInstanceDTO> searchHostingInstances(String keyword);
    Map<String, Object> getHostingStats();

    // ==================== 存储管理 ====================
    List<StorageItemDTO> getStorageList();
    List<StorageItemDTO> getStorageListByType(String type);
    StorageItemDTO getStorageItem(String storageId);
    StorageItemDTO createStorageItem(StorageItemDTO storageItem);
    boolean deleteStorageItem(String storageId);
    Map<String, Object> getStorageStats();

    // ==================== 系统管理 ====================
    Map<String, Object> getSystemInfo();
    Map<String, Object> getSystemStats();
    Map<String, Object> getSystemConfig();
    boolean saveSystemConfig(Map<String, Object> config);
    List<SystemLogDTO> getSystemLogs(int limit);
    boolean restartSystem();
    boolean shutdownSystem();
}
