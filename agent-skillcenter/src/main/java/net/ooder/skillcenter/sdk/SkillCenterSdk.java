package net.ooder.skillcenter.sdk;

import net.ooder.skillcenter.manager.GroupManager;
import net.ooder.skillcenter.manager.HostingManager;
import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.manager.StorageManager;
import net.ooder.skillcenter.manager.UserManager;
import net.ooder.skillcenter.market.SkillListing;
import net.ooder.skillcenter.model.ApiResponse;
import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillAuthentication;

import java.util.List;
import java.util.Map;

/**
 * SkillCenter SDK接口
 * 定义所有对外提供的API接口
 */
public interface SkillCenterSdk {
    
    // ==================== 技能管理 ====================
    
    ApiResponse<List<Map<String, Object>>> getAllSkills(String category, String status, String keyword);
    
    ApiResponse<Boolean> approveSkill(String skillId);
    
    ApiResponse<Boolean> rejectSkill(String skillId);
    
    ApiResponse<Skill> addSkill(SkillManager.SkillInfo skillInfo);
    
    ApiResponse<Skill> updateSkill(String skillId, SkillManager.SkillInfo skillInfo);
    
    ApiResponse<Boolean> deleteSkill(String skillId);
    
    // ==================== 市场管理 ====================
    
    ApiResponse<List<SkillListing>> getMarketSkills();
    
    ApiResponse<SkillListing> getMarketSkill(String skillId);
    
    ApiResponse<Boolean> addMarketSkill(SkillListing listing);
    
    ApiResponse<Boolean> updateMarketSkill(String skillId, SkillListing listing);
    
    ApiResponse<Boolean> removeMarketSkill(String skillId);
    
    ApiResponse<List<SkillListing>> getMarketSkillsByCategory(String category);
    
    ApiResponse<List<SkillListing>> getPopularMarketSkills(int limit);
    
    ApiResponse<List<SkillListing>> getLatestMarketSkills(int limit);
    
    // ==================== 技能认证 ====================
    
    ApiResponse<List<SkillAuthentication>> getAuthenticationRequests();
    
    ApiResponse<SkillAuthentication> getAuthentication(String id);
    
    ApiResponse<SkillAuthentication> createAuthentication(SkillAuthentication authentication);
    
    ApiResponse<SkillAuthentication> updateAuthenticationStatus(String id, String status, String reviewer, String comments);
    
    ApiResponse<Boolean> deleteAuthentication(String id);
    
    ApiResponse<SkillAuthentication> issueCertificate(SkillAuthentication request);
    
    // ==================== 群组管理 ====================
    
    ApiResponse<List<GroupManager.Group>> getAllGroups();
    
    ApiResponse<GroupManager.Group> getGroup(String groupId);
    
    ApiResponse<GroupManager.Group> createGroup(GroupManager.Group group);
    
    ApiResponse<GroupManager.Group> updateGroup(String groupId, GroupManager.Group group);
    
    ApiResponse<Boolean> deleteGroup(String groupId);
    
    ApiResponse<List<GroupManager.Group>> searchGroups(String keyword);
    
    // ==================== 用户管理 ====================
    
    ApiResponse<List<UserManager.User>> getAllUsers();
    
    ApiResponse<UserManager.User> getUser(String userId);
    
    ApiResponse<UserManager.User> createUser(UserManager.User user);
    
    ApiResponse<UserManager.User> updateUser(String userId, UserManager.User user);
    
    ApiResponse<Boolean> deleteUser(String userId);
    
    ApiResponse<List<UserManager.User>> searchUsers(String keyword);
    
    ApiResponse<List<UserManager.User>> getGroupMembers(String groupId);
    
    // ==================== 远程托管 ====================
    
    ApiResponse<List<HostingManager.HostingInstance>> getHostingInstances();
    
    ApiResponse<HostingManager.HostingInstance> getHostingInstance(String instanceId);
    
    ApiResponse<HostingManager.HostingInstance> createHostingInstance(HostingManager.HostingInstance instance);
    
    ApiResponse<HostingManager.HostingInstance> updateHostingInstance(String instanceId, HostingManager.HostingInstance instance);
    
    ApiResponse<Boolean> deleteHostingInstance(String instanceId);
    
    ApiResponse<HostingManager.HostingInstance> startHostingInstance(String instanceId);
    
    ApiResponse<HostingManager.HostingInstance> stopHostingInstance(String instanceId);
    
    ApiResponse<List<HostingManager.HostingInstance>> searchHostingInstances(String keyword);
    
    ApiResponse<Map<String, Object>> getHostingStats();
    
    // ==================== 存储管理 ====================
    
    ApiResponse<List<StorageManager.StorageItem>> getStorageList();
    
    ApiResponse<List<StorageManager.StorageItem>> getStorageListByType(String type);
    
    ApiResponse<StorageManager.StorageItem> getStorageItem(String storageId);
    
    ApiResponse<StorageManager.StorageItem> createStorageItem(StorageManager.StorageItem storageItem);
    
    ApiResponse<Boolean> deleteStorageItem(String storageId);
    
    ApiResponse<Map<String, Object>> getStorageStats();
    
    // ==================== 仪表盘 ====================
    
    ApiResponse<Map<String, Object>> getDashboardStats();
}
