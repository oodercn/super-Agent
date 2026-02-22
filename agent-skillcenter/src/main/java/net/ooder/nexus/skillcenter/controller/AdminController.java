package net.ooder.nexus.skillcenter.controller;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.SkillDTO;
import net.ooder.skillcenter.dto.UserDTO;
import net.ooder.skillcenter.dto.GroupDTO;
import net.ooder.skillcenter.dto.AuthenticationRequestDTO;
import net.ooder.skillcenter.service.SkillService;
import net.ooder.skillcenter.service.UserService;
import net.ooder.skillcenter.service.GroupService;
import net.ooder.skillcenter.service.AuthenticationService;
import net.ooder.skillcenter.market.SkillListing;
import net.ooder.skillcenter.market.SkillMarketManager;
import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.nexus.skillcenter.dto.admin.*;
import net.ooder.nexus.skillcenter.dto.common.PaginationDTO;
import net.ooder.nexus.skillcenter.dto.common.SearchDTO;
import net.ooder.nexus.skillcenter.dto.common.OperationResultDTO;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class AdminController extends BaseController {

    private final SkillService skillService;
    private final UserService userService;
    private final GroupService groupService;
    private final AuthenticationService authService;
    private final SkillMarketManager marketManager;

    public AdminController(SkillService skillService, UserService userService, 
                          GroupService groupService, AuthenticationService authService,
                          SkillMarketManager marketManager) {
        this.skillService = skillService;
        this.userService = userService;
        this.groupService = groupService;
        this.authService = authService;
        this.marketManager = marketManager;
    }

    @PostMapping("/dashboard/stats")
    public ResultModel<AdminDashboardStatsDTO> getDashboardStats() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getDashboardStats", null);

        try {
            AdminDashboardStatsDTO stats = new AdminDashboardStatsDTO();
            
            stats.setTotalSkills(skillService.getSkillCount());
            stats.setTotalMarketSkills(marketManager.getSkillCount());
            stats.setTotalUsers(userService.getUserCount());
            stats.setActiveUsers(userService.getActiveUserCount());
            stats.setTotalExecutions(skillService.getExecutionCount());
            stats.setSuccessfulExecutions(skillService.getSuccessfulExecutionCount());
            stats.setFailedExecutions(skillService.getFailedExecutionCount());
            stats.setSuccessRate(calculateSuccessRate(stats.getSuccessfulExecutions(), stats.getTotalExecutions()));
            stats.setSharedSkills(skillService.getSharedSkillCount());

            AdminDashboardStatsDTO.SystemInfoDTO systemInfo = getSystemInfo();
            stats.setSystemInfo(systemInfo);

            List<AdminDashboardStatsDTO.ActivityDTO> activities = getRecentActivities();
            stats.setRecentActivities(activities);

            logRequestEnd("getDashboardStats", stats, System.currentTimeMillis() - startTime);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getDashboardStats", e);
            return ResultModel.error(500, "获取仪表盘统计数据失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills")
    public ResultModel<PageResult<SkillDTO>> getSkills(@RequestBody SkillQueryDTO query) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getSkills", query);

        try {
            PageResult<SkillDTO> result = skillService.getAllSkills(
                query.getCategory(), query.getStatus(), query.getKeyword(),
                query.getPageNum(), query.getPageSize());
            logRequestEnd("getSkills", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getSkills", e);
            return ResultModel.error(500, "获取技能列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/{skillId}/delete")
    public ResultModel<OperationResultDTO> deleteSkill(@PathVariable String skillId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deleteSkill", skillId);

        try {
            boolean success = skillService.deleteSkill(skillId);
            OperationResultDTO result = success ? 
                OperationResultDTO.success("删除技能成功") : 
                OperationResultDTO.failure("删除技能失败");
            logRequestEnd("deleteSkill", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("deleteSkill", e);
            return ResultModel.error(500, "删除技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/users")
    public ResultModel<PageResult<UserDTO>> getUsers(@RequestBody PaginationDTO pagination) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getUsers", pagination);

        try {
            PageResult<UserDTO> result = userService.getAllUsers(pagination.getPageNum(), pagination.getPageSize());
            logRequestEnd("getUsers", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getUsers", e);
            return ResultModel.error(500, "获取用户列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/users/search")
    public ResultModel<PageResult<UserDTO>> searchUsers(@RequestBody SearchDTO search) {
        long startTime = System.currentTimeMillis();
        logRequestStart("searchUsers", search);

        try {
            PageResult<UserDTO> result = userService.searchUsers(
                search.getKeyword(), search.getPageNum(), search.getPageSize());
            logRequestEnd("searchUsers", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("searchUsers", e);
            return ResultModel.error(500, "搜索用户失败: " + e.getMessage());
        }
    }

    @PostMapping("/users/{userId}/delete")
    public ResultModel<OperationResultDTO> deleteUser(@PathVariable String userId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deleteUser", userId);

        try {
            boolean success = userService.deleteUser(userId);
            OperationResultDTO result = success ? 
                OperationResultDTO.success("删除用户成功") : 
                OperationResultDTO.failure("删除用户失败");
            logRequestEnd("deleteUser", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("deleteUser", e);
            return ResultModel.error(500, "删除用户失败: " + e.getMessage());
        }
    }

    @PostMapping("/users/add")
    public ResultModel<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        long startTime = System.currentTimeMillis();
        logRequestStart("createUser", userDTO);

        try {
            UserDTO created = userService.createUser(userDTO);
            logRequestEnd("createUser", created.getId(), System.currentTimeMillis() - startTime);
            return ResultModel.success(created);
        } catch (Exception e) {
            logRequestError("createUser", e);
            return ResultModel.error(500, "创建用户失败: " + e.getMessage());
        }
    }

    @PostMapping("/users/{userId}/update")
    public ResultModel<UserDTO> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateUser", userId);

        try {
            UserDTO updated = userService.updateUser(userId, userDTO);
            if (updated == null) {
                logRequestEnd("updateUser", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("用户不存在");
            }
            logRequestEnd("updateUser", updated.getId(), System.currentTimeMillis() - startTime);
            return ResultModel.success(updated);
        } catch (Exception e) {
            logRequestError("updateUser", e);
            return ResultModel.error(500, "更新用户失败: " + e.getMessage());
        }
    }

    @PostMapping("/groups")
    public ResultModel<PageResult<GroupDTO>> getGroups(@RequestBody PaginationDTO pagination) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getGroups", pagination);

        try {
            PageResult<GroupDTO> result = groupService.getAllGroups(pagination.getPageNum(), pagination.getPageSize());
            logRequestEnd("getGroups", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getGroups", e);
            return ResultModel.error(500, "获取群组列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/groups/{groupId}")
    public ResultModel<GroupDTO> getGroupById(@PathVariable String groupId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getGroupById", groupId);

        try {
            GroupDTO group = groupService.getGroupById(groupId);
            if (group == null) {
                logRequestEnd("getGroupById", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("群组不存在");
            }
            logRequestEnd("getGroupById", group, System.currentTimeMillis() - startTime);
            return ResultModel.success(group);
        } catch (Exception e) {
            logRequestError("getGroupById", e);
            return ResultModel.error(500, "获取群组详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/groups/{groupId}/members")
    public ResultModel<PageResult<UserDTO>> getGroupMembers(@PathVariable String groupId, @RequestBody PaginationDTO pagination) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getGroupMembers", groupId);

        try {
            PageResult<UserDTO> members = userService.getUsersByGroup(groupId, pagination.getPageNum(), pagination.getPageSize());
            logRequestEnd("getGroupMembers", members, System.currentTimeMillis() - startTime);
            return ResultModel.success(members);
        } catch (Exception e) {
            logRequestError("getGroupMembers", e);
            return ResultModel.error(500, "获取群组成员失败: " + e.getMessage());
        }
    }

    @PostMapping("/groups/search")
    public ResultModel<PageResult<GroupDTO>> searchGroups(@RequestBody SearchDTO search) {
        long startTime = System.currentTimeMillis();
        logRequestStart("searchGroups", search);

        try {
            PageResult<GroupDTO> result = groupService.searchGroups(
                search.getKeyword(), search.getPageNum(), search.getPageSize());
            logRequestEnd("searchGroups", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("searchGroups", e);
            return ResultModel.error(500, "搜索群组失败: " + e.getMessage());
        }
    }

    @PostMapping("/groups/{groupId}/delete")
    public ResultModel<OperationResultDTO> deleteGroup(@PathVariable String groupId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deleteGroup", groupId);

        try {
            boolean success = groupService.deleteGroup(groupId);
            OperationResultDTO result = success ? 
                OperationResultDTO.success("删除群组成功") : 
                OperationResultDTO.failure("删除群组失败");
            logRequestEnd("deleteGroup", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("deleteGroup", e);
            return ResultModel.error(500, "删除群组失败: " + e.getMessage());
        }
    }

    @PostMapping("/groups/add")
    public ResultModel<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO) {
        long startTime = System.currentTimeMillis();
        logRequestStart("createGroup", groupDTO);

        try {
            GroupDTO created = groupService.createGroup(groupDTO);
            logRequestEnd("createGroup", created.getId(), System.currentTimeMillis() - startTime);
            return ResultModel.success(created);
        } catch (Exception e) {
            logRequestError("createGroup", e);
            return ResultModel.error(500, "创建群组失败: " + e.getMessage());
        }
    }

    @PostMapping("/groups/{groupId}/update")
    public ResultModel<GroupDTO> updateGroup(@PathVariable String groupId, @RequestBody GroupDTO groupDTO) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateGroup", groupId);

        try {
            GroupDTO updated = groupService.updateGroup(groupId, groupDTO);
            if (updated == null) {
                logRequestEnd("updateGroup", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("群组不存在");
            }
            logRequestEnd("updateGroup", updated.getId(), System.currentTimeMillis() - startTime);
            return ResultModel.success(updated);
        } catch (Exception e) {
            logRequestError("updateGroup", e);
            return ResultModel.error(500, "更新群组失败: " + e.getMessage());
        }
    }

    @PostMapping("/authentication/requests")
    public ResultModel<PageResult<AuthenticationRequestDTO>> getAuthenticationRequests(@RequestBody PaginationDTO pagination) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getAuthenticationRequests", pagination);

        try {
            PageResult<AuthenticationRequestDTO> result = authService.getAllRequests(
                pagination.getPageNum(), pagination.getPageSize());
            logRequestEnd("getAuthenticationRequests", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getAuthenticationRequests", e);
            return ResultModel.error(500, "获取认证请求列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/authentication/requests/{id}/status")
    public ResultModel<OperationResultDTO> updateAuthenticationRequestStatus(
            @PathVariable String id, 
            @RequestBody AuthStatusUpdateDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateAuthenticationRequestStatus", request);

        try {
            boolean success = authService.updateRequestStatus(id, request.getStatus(), request.getComments());
            OperationResultDTO result = success ? 
                OperationResultDTO.success("更新认证请求状态成功") : 
                OperationResultDTO.failure("更新认证请求状态失败");
            logRequestEnd("updateAuthenticationRequestStatus", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("updateAuthenticationRequestStatus", e);
            return ResultModel.error(500, "更新认证请求状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/market/skills")
    public ResultModel<List<SkillListing>> getMarketSkills(@RequestBody PaginationDTO pagination) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getMarketSkills", pagination);

        try {
            List<SkillListing> skills = marketManager.getAllSkills();
            logRequestEnd("getMarketSkills", skills.size() + " skills", System.currentTimeMillis() - startTime);
            return ResultModel.success(skills);
        } catch (Exception e) {
            logRequestError("getMarketSkills", e);
            return ResultModel.error(500, "获取市场技能列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/market/skills/popular")
    public ResultModel<List<SkillListing>> getPopularMarketSkills(@RequestBody PaginationDTO pagination) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getPopularMarketSkills", pagination);

        try {
            List<SkillListing> skills = marketManager.getPopularSkills(pagination.getPageSize());
            logRequestEnd("getPopularMarketSkills", skills.size() + " skills", System.currentTimeMillis() - startTime);
            return ResultModel.success(skills);
        } catch (Exception e) {
            logRequestError("getPopularMarketSkills", e);
            return ResultModel.error(500, "获取热门市场技能列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/market/skills/latest")
    public ResultModel<List<SkillListing>> getLatestMarketSkills(@RequestBody PaginationDTO pagination) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getLatestMarketSkills", pagination);

        try {
            List<SkillListing> skills = marketManager.getLatestSkills(pagination.getPageSize());
            logRequestEnd("getLatestMarketSkills", skills.size() + " skills", System.currentTimeMillis() - startTime);
            return ResultModel.success(skills);
        } catch (Exception e) {
            logRequestError("getLatestMarketSkills", e);
            return ResultModel.error(500, "获取最新市场技能列表失败: " + e.getMessage());
        }
    }

    private double calculateSuccessRate(int successful, int total) {
        if (total == 0) return 0;
        return (double) successful / total * 100;
    }

    private AdminDashboardStatsDTO.SystemInfoDTO getSystemInfo() {
        AdminDashboardStatsDTO.SystemInfoDTO systemInfo = new AdminDashboardStatsDTO.SystemInfoDTO();
        
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        
        systemInfo.setCpuUsage(osBean.getSystemLoadAverage());
        systemInfo.setAvailableProcessors(osBean.getAvailableProcessors());
        
        long totalMemory = memoryBean.getHeapMemoryUsage().getMax();
        long usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
        long freeMemory = totalMemory - usedMemory;
        
        systemInfo.setTotalMemory(formatMemory(totalMemory));
        systemInfo.setUsedMemory(formatMemory(usedMemory));
        systemInfo.setFreeMemory(formatMemory(freeMemory));
        systemInfo.setMemoryUsage((double) usedMemory / totalMemory * 100);
        systemInfo.setSystemLoad(osBean.getSystemLoadAverage());
        
        return systemInfo;
    }

    private String formatMemory(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
        if (bytes < 1024 * 1024 * 1024) return (bytes / (1024 * 1024)) + " MB";
        return (bytes / (1024 * 1024 * 1024)) + " GB";
    }

    private List<AdminDashboardStatsDTO.ActivityDTO> getRecentActivities() {
        List<AdminDashboardStatsDTO.ActivityDTO> activities = new ArrayList<>();
        return activities;
    }
}
