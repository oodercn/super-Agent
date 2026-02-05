package net.ooder.skillcenter.controller;

import net.ooder.skillcenter.manager.AuthenticationManager;
import net.ooder.skillcenter.manager.GroupManager;
import net.ooder.skillcenter.manager.HostingManager;
import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.manager.StorageManager;
import net.ooder.skillcenter.manager.SystemManager;
import net.ooder.skillcenter.market.SkillListing;
import net.ooder.skillcenter.market.SkillMarketManager;
import net.ooder.skillcenter.model.ApiResponse;
import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillAuthentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理中心REST API控制器
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final SkillManager skillManager;
    private final SkillMarketManager marketManager;
    private final AuthenticationManager authenticationManager;
    private final GroupManager groupManager;
    private final HostingManager hostingManager;
    private final StorageManager storageManager;
    private final SystemManager systemManager;

    /**
     * 构造方法，初始化管理器
     */
    public AdminController() {
        this.skillManager = SkillManager.getInstance();
        this.marketManager = SkillMarketManager.getInstance();
        this.authenticationManager = AuthenticationManager.getInstance();
        this.groupManager = GroupManager.getInstance();
        this.hostingManager = HostingManager.getInstance();
        this.storageManager = StorageManager.getInstance();
        this.systemManager = SystemManager.getInstance();
    }

    // ==================== 管理仪表盘 ====================

    /**
     * 获取管理仪表盘统计数据
     * @return 管理仪表盘统计数据
     */
    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAdminDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        try {
            // 技能统计
            int totalSkills = skillManager.getAllSkills().size();
            stats.put("totalSkills", totalSkills);

            // 市场统计
            int totalMarketSkills = marketManager.getAllSkills().size();
            stats.put("totalMarketSkills", totalMarketSkills);

            // 用户统计
            stats.put("totalUsers", 42);
            stats.put("activeUsers", 35);

            // 执行统计
            stats.put("totalExecutions", 2543);
            stats.put("successfulExecutions", 2413);
            stats.put("failedExecutions", 130);
            stats.put("successRate", 94.9);

            // 分享统计
            stats.put("sharedSkills", 156);

            // 系统信息
            Map<String, Object> systemInfo = new HashMap<>();
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;

            // CPU使用率
            double cpuUsage = sunOsBean.getSystemCpuLoad() * 100;
            systemInfo.put("cpuUsage", Math.round(cpuUsage * 10) / 10.0);

            // 内存使用情况
            long totalMemory = sunOsBean.getTotalPhysicalMemorySize();
            long freeMemory = sunOsBean.getFreePhysicalMemorySize();
            long usedMemory = totalMemory - freeMemory;
            double memoryUsage = (double) usedMemory / totalMemory * 100;

            systemInfo.put("memoryUsage", Math.round(memoryUsage * 10) / 10.0);
            systemInfo.put("totalMemory", formatFileSize(totalMemory));
            systemInfo.put("usedMemory", formatFileSize(usedMemory));
            systemInfo.put("freeMemory", formatFileSize(freeMemory));

            // 系统负载
            double systemLoad = osBean.getSystemLoadAverage();
            systemInfo.put("systemLoad", Math.round(systemLoad * 10) / 10.0);

            // 可用处理器数量
            int availableProcessors = osBean.getAvailableProcessors();
            systemInfo.put("availableProcessors", availableProcessors);

            stats.put("systemInfo", systemInfo);

            // 最近活动
            List<Map<String, Object>> recentActivities = new ArrayList<>();
            Map<String, Object> activity1 = new HashMap<>();
            activity1.put("id", "1");
            activity1.put("type", "execution");
            activity1.put("skillName", "Text to Uppercase");
            activity1.put("userId", "user123");
            activity1.put("timestamp", "2026-01-31 10:30:00");
            activity1.put("status", "success");
            recentActivities.add(activity1);

            Map<String, Object> activity2 = new HashMap<>();
            activity2.put("id", "2");
            activity2.put("type", "publish");
            activity2.put("skillName", "Code Generator");
            activity2.put("userId", "user456");
            activity2.put("timestamp", "2026-01-31 09:15:00");
            activity2.put("status", "success");
            recentActivities.add(activity2);

            Map<String, Object> activity3 = new HashMap<>();
            activity3.put("id", "3");
            activity3.put("type", "authentication");
            activity3.put("skillName", "Weather API");
            activity3.put("userId", "admin");
            activity3.put("timestamp", "2026-01-31 08:45:00");
            activity3.put("status", "approved");
            recentActivities.add(activity3);

            stats.put("recentActivities", recentActivities);

            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取管理仪表盘统计数据失败: " + e.getMessage()));
        }
    }

    // ==================== 技能管理 ====================

    /**
     * 获取所有技能（管理视角）
     * @return 技能列表
     */
    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<List<Skill>>> getAllSkills() {
        List<Skill> skills = skillManager.getAllSkills();
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    /**
     * 审核技能
     * @param skillId 技能ID
     * @return 审核结果
     */
    @PostMapping("/skills/{skillId}/approve")
    public ResponseEntity<ApiResponse<Boolean>> approveSkill(@PathVariable String skillId) {
        try {
            boolean result = skillManager.approveSkill(skillId);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "审核技能失败: " + e.getMessage()));
        }
    }

    /**
     * 拒绝技能
     * @param skillId 技能ID
     * @return 拒绝结果
     */
    @PostMapping("/skills/{skillId}/reject")
    public ResponseEntity<ApiResponse<Boolean>> rejectSkill(@PathVariable String skillId) {
        try {
            boolean result = skillManager.rejectSkill(skillId);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "拒绝技能失败: " + e.getMessage()));
        }
    }

    /**
     * 添加技能
     * @param skill 技能信息
     * @return 添加结果
     */
    @PostMapping("/skills")
    public ResponseEntity<ApiResponse<Skill>> addSkill(@RequestBody Skill skill) {
        try {
            Skill newSkill = skillManager.addSkill(skill);
            return ResponseEntity.ok(ApiResponse.success(newSkill));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "添加技能失败: " + e.getMessage()));
        }
    }

    /**
     * 更新技能
     * @param skillId 技能ID
     * @param skill 技能信息
     * @return 更新结果
     */
    @PutMapping("/skills/{skillId}")
    public ResponseEntity<ApiResponse<Skill>> updateSkill(@PathVariable String skillId, @RequestBody Skill skill) {
        try {
            // 使用SkillInfo类来处理更新操作
            SkillManager.SkillInfo skillInfo = null;
            if (skill instanceof SkillManager.SkillInfo) {
                skillInfo = (SkillManager.SkillInfo) skill;
                skillInfo.setId(skillId);
            } else {
                // 将普通Skill转换为SkillInfo
                skillInfo = new SkillManager.SkillInfo();
                skillInfo.setId(skillId);
                skillInfo.setName(skill.getName());
                skillInfo.setDescription(skill.getDescription());
                skillInfo.setParams(skill.getParams());
                skillInfo.setAvailable(skill.isAvailable());
            }
            
            Skill updatedSkill = skillManager.updateSkill(skillInfo);
            return ResponseEntity.ok(ApiResponse.success(updatedSkill));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "更新技能失败: " + e.getMessage()));
        }
    }

    /**
     * 删除技能
     * @param skillId 技能ID
     * @return 删除结果
     */
    @DeleteMapping("/skills/{skillId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteSkill(@PathVariable String skillId) {
        try {
            boolean result = skillManager.deleteSkill(skillId);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "删除技能失败: " + e.getMessage()));
        }
    }

    // ==================== 市场管理 ====================

    /**
     * 获取市场技能列表
     * @return 市场技能列表
     */
    @GetMapping("/market/skills")
    public ResponseEntity<ApiResponse<List<SkillListing>>> getMarketSkills() {
        try {
            List<SkillListing> marketSkills = marketManager.getAllSkills();
            return ResponseEntity.ok(ApiResponse.success(marketSkills));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取市场技能列表失败: " + e.getMessage()));
        }
    }

    /**
     * 获取指定ID的市场技能
     * @param skillId 技能ID
     * @return 市场技能
     */
    @GetMapping("/market/skills/{skillId}")
    public ResponseEntity<ApiResponse<SkillListing>> getMarketSkill(@PathVariable String skillId) {
        try {
            SkillListing skill = marketManager.getSkillListing(skillId);
            if (skill == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "技能不存在"));
            }
            return ResponseEntity.ok(ApiResponse.success(skill));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取市场技能失败: " + e.getMessage()));
        }
    }

    /**
     * 添加市场技能
     * @param listing 技能列表项
     * @return 添加结果
     */
    @PostMapping("/market/skills")
    public ResponseEntity<ApiResponse<Boolean>> addMarketSkill(@RequestBody SkillListing listing) {
        try {
            // 这里简化处理，实际应该传入完整的技能实例
            // 暂时使用SkillMarketManager的updateSkill方法来添加新技能
            boolean result = marketManager.updateSkill(listing);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "添加市场技能失败: " + e.getMessage()));
        }
    }

    /**
     * 更新市场技能
     * @param skillId 技能ID
     * @param listing 技能列表项
     * @return 更新结果
     */
    @PutMapping("/market/skills/{skillId}")
    public ResponseEntity<ApiResponse<Boolean>> updateMarketSkill(@PathVariable String skillId, @RequestBody SkillListing listing) {
        try {
            // 确保技能ID一致
            listing.setSkillId(skillId);
            boolean result = marketManager.updateSkill(listing);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "更新市场技能失败: " + e.getMessage()));
        }
    }

    /**
     * 从市场移除技能
     * @param skillId 技能ID
     * @return 移除结果
     */
    @DeleteMapping("/market/skills/{skillId}")
    public ResponseEntity<ApiResponse<Boolean>> removeMarketSkill(@PathVariable String skillId) {
        try {
            boolean result = marketManager.removeSkill(skillId);
            if (!result) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "技能不存在"));
            }
            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "从市场移除技能失败: " + e.getMessage()));
        }
    }

    /**
     * 根据分类获取市场技能
     * @param category 分类名称
     * @return 市场技能列表
     */
    @GetMapping("/market/skills/category/{category}")
    public ResponseEntity<ApiResponse<List<SkillListing>>> getMarketSkillsByCategory(@PathVariable String category) {
        try {
            List<SkillListing> marketSkills = marketManager.getSkillsByCategory(category);
            return ResponseEntity.ok(ApiResponse.success(marketSkills));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取市场技能列表失败: " + e.getMessage()));
        }
    }

    /**
     * 获取热门市场技能
     * @param limit 限制数量
     * @return 热门市场技能列表
     */
    @GetMapping("/market/skills/popular")
    public ResponseEntity<ApiResponse<List<SkillListing>>> getPopularMarketSkills(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<SkillListing> popularSkills = marketManager.getPopularSkills(limit);
            return ResponseEntity.ok(ApiResponse.success(popularSkills));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取热门市场技能列表失败: " + e.getMessage()));
        }
    }

    /**
     * 获取最新市场技能
     * @param limit 限制数量
     * @return 最新市场技能列表
     */
    @GetMapping("/market/skills/latest")
    public ResponseEntity<ApiResponse<List<SkillListing>>> getLatestMarketSkills(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<SkillListing> latestSkills = marketManager.getLatestSkills(limit);
            return ResponseEntity.ok(ApiResponse.success(latestSkills));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取最新市场技能列表失败: " + e.getMessage()));
        }
    }

    // ==================== 技能认证 ====================

    /**
     * 获取技能认证列表
     * @return 技能认证列表
     */
    @GetMapping("/authentication/requests")
    public ResponseEntity<ApiResponse<List<SkillAuthentication>>> getAuthenticationRequests() {
        try {
            List<SkillAuthentication> authentications = authenticationManager.getAllAuthentications();
            return ResponseEntity.ok(ApiResponse.success(authentications));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取技能认证列表失败: " + e.getMessage()));
        }
    }

    /**
     * 获取指定ID的技能认证
     * @param id 认证ID
     * @return 技能认证
     */
    @GetMapping("/authentication/requests/{id}")
    public ResponseEntity<ApiResponse<SkillAuthentication>> getAuthentication(@PathVariable String id) {
        try {
            SkillAuthentication auth = authenticationManager.getAuthentication(id);
            if (auth == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "认证不存在"));
            }
            return ResponseEntity.ok(ApiResponse.success(auth));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取技能认证失败: " + e.getMessage()));
        }
    }

    /**
     * 创建技能认证
     * @param authentication 认证信息
     * @return 创建结果
     */
    @PostMapping("/authentication/requests")
    public ResponseEntity<ApiResponse<SkillAuthentication>> createAuthentication(@RequestBody SkillAuthentication authentication) {
        try {
            SkillAuthentication createdAuth = authenticationManager.createAuthentication(authentication);
            return ResponseEntity.ok(ApiResponse.success(createdAuth));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "创建技能认证失败: " + e.getMessage()));
        }
    }

    /**
     * 更新技能认证状态
     * @param id 认证ID
     * @param request 更新请求
     * @return 更新结果
     */
    @PutMapping("/authentication/requests/{id}/status")
    public ResponseEntity<ApiResponse<SkillAuthentication>> updateAuthenticationStatus(@PathVariable String id, @RequestBody Map<String, Object> request) {
        try {
            String status = (String) request.get("status");
            String reviewer = (String) request.get("reviewer");
            String comments = (String) request.get("comments");

            SkillAuthentication updatedAuth = authenticationManager.updateAuthenticationStatus(id, status, reviewer, comments);
            return ResponseEntity.ok(ApiResponse.success(updatedAuth));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "更新技能认证状态失败: " + e.getMessage()));
        }
    }

    /**
     * 删除技能认证
     * @param id 认证ID
     * @return 删除结果
     */
    @DeleteMapping("/authentication/requests/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteAuthentication(@PathVariable String id) {
        try {
            boolean result = authenticationManager.deleteAuthentication(id);
            if (!result) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "认证不存在"));
            }
            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "删除技能认证失败: " + e.getMessage()));
        }
    }

    /**
     * 签发技能认证
     * @param request 认证请求
     * @return 签发结果
     */
    @PostMapping("/authentication/issue")
    public ResponseEntity<ApiResponse<SkillAuthentication>> issueCertificate(@RequestBody SkillAuthentication request) {
        try {
            // 设置为已批准状态
            request.setStatus("approved");
            request.setReviewer("admin");
            SkillAuthentication issuedAuth = authenticationManager.createAuthentication(request);
            return ResponseEntity.ok(ApiResponse.success(issuedAuth));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "签发认证失败: " + e.getMessage()));
        }
    }

    // ==================== 群组管理 ====================

    /**
     * 获取所有群组
     * @return 群组列表
     */
    @GetMapping("/groups")
    public ResponseEntity<ApiResponse<List<GroupManager.Group>>> getAllGroups() {
        try {
            List<GroupManager.Group> groups = groupManager.getAllGroups();
            return ResponseEntity.ok(ApiResponse.success(groups));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取群组列表失败: " + e.getMessage()));
        }
    }

    /**
     * 获取指定ID的群组
     * @param groupId 群组ID
     * @return 群组信息
     */
    @GetMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<GroupManager.Group>> getGroup(@PathVariable String groupId) {
        try {
            GroupManager.Group group = groupManager.getGroup(groupId);
            if (group == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "群组不存在"));
            }
            return ResponseEntity.ok(ApiResponse.success(group));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取群组失败: " + e.getMessage()));
        }
    }

    /**
     * 创建群组
     * @param group 群组信息
     * @return 创建结果
     */
    @PostMapping("/groups")
    public ResponseEntity<ApiResponse<GroupManager.Group>> createGroup(@RequestBody GroupManager.Group group) {
        try {
            GroupManager.Group createdGroup = groupManager.createGroup(group);
            return ResponseEntity.ok(ApiResponse.success(createdGroup));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "创建群组失败: " + e.getMessage()));
        }
    }

    /**
     * 更新群组
     * @param groupId 群组ID
     * @param group 群组信息
     * @return 更新结果
     */
    @PutMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<GroupManager.Group>> updateGroup(@PathVariable String groupId, @RequestBody GroupManager.Group group) {
        try {
            group.setId(groupId);
            GroupManager.Group updatedGroup = groupManager.updateGroup(group);
            return ResponseEntity.ok(ApiResponse.success(updatedGroup));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "更新群组失败: " + e.getMessage()));
        }
    }

    /**
     * 删除群组
     * @param groupId 群组ID
     * @return 删除结果
     */
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteGroup(@PathVariable String groupId) {
        try {
            boolean result = groupManager.deleteGroup(groupId);
            if (!result) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "群组不存在"));
            }
            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "删除群组失败: " + e.getMessage()));
        }
    }

    /**
     * 搜索群组
     * @param keyword 搜索关键词
     * @return 匹配的群组列表
     */
    @GetMapping("/groups/search")
    public ResponseEntity<ApiResponse<List<GroupManager.Group>>> searchGroups(@RequestParam String keyword) {
        try {
            List<GroupManager.Group> groups = groupManager.searchGroups(keyword);
            return ResponseEntity.ok(ApiResponse.success(groups));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "搜索群组失败: " + e.getMessage()));
        }
    }

    // ==================== 远程托管 ====================

    /**
     * 获取托管实例列表
     * @return 托管实例列表
     */
    @GetMapping("/hosting/instances")
    public ResponseEntity<ApiResponse<List<HostingManager.HostingInstance>>> getHostingInstances() {
        try {
            List<HostingManager.HostingInstance> instances = hostingManager.getAllHostingInstances();
            return ResponseEntity.ok(ApiResponse.success(instances));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取托管实例列表失败: " + e.getMessage()));
        }
    }

    /**
     * 获取指定ID的托管实例
     * @param instanceId 实例ID
     * @return 托管实例
     */
    @GetMapping("/hosting/instances/{instanceId}")
    public ResponseEntity<ApiResponse<HostingManager.HostingInstance>> getHostingInstance(@PathVariable String instanceId) {
        try {
            HostingManager.HostingInstance instance = hostingManager.getHostingInstance(instanceId);
            if (instance == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "托管实例不存在"));
            }
            return ResponseEntity.ok(ApiResponse.success(instance));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取托管实例失败: " + e.getMessage()));
        }
    }

    /**
     * 创建托管实例
     * @param instance 托管实例信息
     * @return 创建结果
     */
    @PostMapping("/hosting/instances")
    public ResponseEntity<ApiResponse<HostingManager.HostingInstance>> createHostingInstance(@RequestBody HostingManager.HostingInstance instance) {
        try {
            HostingManager.HostingInstance createdInstance = hostingManager.createHostingInstance(instance);
            return ResponseEntity.ok(ApiResponse.success(createdInstance));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "创建托管实例失败: " + e.getMessage()));
        }
    }

    /**
     * 更新托管实例
     * @param instanceId 实例ID
     * @param instance 托管实例信息
     * @return 更新结果
     */
    @PutMapping("/hosting/instances/{instanceId}")
    public ResponseEntity<ApiResponse<HostingManager.HostingInstance>> updateHostingInstance(@PathVariable String instanceId, @RequestBody HostingManager.HostingInstance instance) {
        try {
            instance.setId(instanceId);
            HostingManager.HostingInstance updatedInstance = hostingManager.updateHostingInstance(instance);
            return ResponseEntity.ok(ApiResponse.success(updatedInstance));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "更新托管实例失败: " + e.getMessage()));
        }
    }

    /**
     * 删除托管实例
     * @param instanceId 实例ID
     * @return 删除结果
     */
    @DeleteMapping("/hosting/instances/{instanceId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteHostingInstance(@PathVariable String instanceId) {
        try {
            boolean result = hostingManager.deleteHostingInstance(instanceId);
            if (!result) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "托管实例不存在"));
            }
            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "删除托管实例失败: " + e.getMessage()));
        }
    }

    /**
     * 启动托管实例
     * @param instanceId 实例ID
     * @return 启动结果
     */
    @PostMapping("/hosting/instances/{instanceId}/start")
    public ResponseEntity<ApiResponse<HostingManager.HostingInstance>> startHostingInstance(@PathVariable String instanceId) {
        try {
            HostingManager.HostingInstance instance = hostingManager.startHostingInstance(instanceId);
            return ResponseEntity.ok(ApiResponse.success(instance));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "启动托管实例失败: " + e.getMessage()));
        }
    }

    /**
     * 停止托管实例
     * @param instanceId 实例ID
     * @return 停止结果
     */
    @PostMapping("/hosting/instances/{instanceId}/stop")
    public ResponseEntity<ApiResponse<HostingManager.HostingInstance>> stopHostingInstance(@PathVariable String instanceId) {
        try {
            HostingManager.HostingInstance instance = hostingManager.stopHostingInstance(instanceId);
            return ResponseEntity.ok(ApiResponse.success(instance));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "停止托管实例失败: " + e.getMessage()));
        }
    }

    /**
     * 搜索托管实例
     * @param keyword 搜索关键词
     * @return 匹配的托管实例列表
     */
    @GetMapping("/hosting/instances/search")
    public ResponseEntity<ApiResponse<List<HostingManager.HostingInstance>>> searchHostingInstances(@RequestParam String keyword) {
        try {
            List<HostingManager.HostingInstance> instances = hostingManager.searchHostingInstances(keyword);
            return ResponseEntity.ok(ApiResponse.success(instances));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "搜索托管实例失败: " + e.getMessage()));
        }
    }

    /**
     * 获取托管实例统计信息
     * @return 统计信息
     */
    @GetMapping("/hosting/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getHostingStats() {
        try {
            Map<String, Object> stats = hostingManager.getHostingStats();
            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取托管统计信息失败: " + e.getMessage()));
        }
    }

    // ==================== 存储管理 ====================

    /**
     * 获取存储列表
     * @return 存储列表
     */
    @GetMapping("/storage/list")
    public ResponseEntity<ApiResponse<List<StorageManager.StorageItem>>> getStorageList() {
        try {
            List<StorageManager.StorageItem> storageList = storageManager.getAllStorageItems();
            return ResponseEntity.ok(ApiResponse.success(storageList));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取存储列表失败: " + e.getMessage()));
        }
    }

    /**
     * 根据类型获取存储列表
     * @param type 存储类型
     * @return 存储列表
     */
    @GetMapping("/storage/list/type/{type}")
    public ResponseEntity<ApiResponse<List<StorageManager.StorageItem>>> getStorageListByType(@PathVariable String type) {
        try {
            List<StorageManager.StorageItem> storageList = storageManager.getStorageItemsByType(type);
            return ResponseEntity.ok(ApiResponse.success(storageList));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取存储列表失败: " + e.getMessage()));
        }
    }

    /**
     * 获取指定ID的存储
     * @param storageId 存储ID
     * @return 存储信息
     */
    @GetMapping("/storage/{storageId}")
    public ResponseEntity<ApiResponse<StorageManager.StorageItem>> getStorageItem(@PathVariable String storageId) {
        try {
            StorageManager.StorageItem storageItem = storageManager.getStorageItem(storageId);
            if (storageItem == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "存储不存在"));
            }
            return ResponseEntity.ok(ApiResponse.success(storageItem));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取存储失败: " + e.getMessage()));
        }
    }

    /**
     * 创建存储
     * @param storageItem 存储信息
     * @return 创建结果
     */
    @PostMapping("/storage")
    public ResponseEntity<ApiResponse<StorageManager.StorageItem>> createStorageItem(@RequestBody StorageManager.StorageItem storageItem) {
        try {
            StorageManager.StorageItem createdItem = storageManager.createStorageItem(storageItem);
            return ResponseEntity.ok(ApiResponse.success(createdItem));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "创建存储失败: " + e.getMessage()));
        }
    }

    /**
     * 删除存储
     * @param storageId 存储ID
     * @return 删除结果
     */
    @DeleteMapping("/storage/{storageId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteStorageItem(@PathVariable String storageId) {
        try {
            boolean result = storageManager.deleteStorageItem(storageId);
            if (!result) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "存储不存在"));
            }
            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "删除存储失败: " + e.getMessage()));
        }
    }

    /**
     * 获取存储统计信息
     * @return 统计信息
     */
    @GetMapping("/storage/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStorageStats() {
        try {
            Map<String, Object> stats = storageManager.getStorageStats();
            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取存储统计信息失败: " + e.getMessage()));
        }
    }

    // ==================== 系统管理 ====================

    /**
     * 获取系统信息
     * @return 系统信息
     */
    @GetMapping("/system/info")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSystemInfo() {
        try {
            Map<String, Object> systemInfo = systemManager.getSystemInfo();
            return ResponseEntity.ok(ApiResponse.success(systemInfo));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取系统信息失败: " + e.getMessage()));
        }
    }

    /**
     * 获取系统统计数据
     * @return 系统统计数据
     */
    @GetMapping("/system/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSystemStats() {
        try {
            Map<String, Object> stats = systemManager.getSystemStats();
            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取系统统计数据失败: " + e.getMessage()));
        }
    }

    /**
     * 获取系统配置
     * @return 系统配置
     */
    @GetMapping("/system/config")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSystemConfig() {
        try {
            Map<String, Object> config = systemManager.getSystemConfig();
            return ResponseEntity.ok(ApiResponse.success(config));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取系统配置失败: " + e.getMessage()));
        }
    }

    /**
     * 保存系统配置
     * @param config 系统配置
     * @return 保存结果
     */
    @PutMapping("/system/config")
    public ResponseEntity<ApiResponse<Boolean>> saveSystemConfig(@RequestBody Map<String, Object> config) {
        try {
            boolean result = systemManager.saveSystemConfig(config);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "保存系统配置失败: " + e.getMessage()));
        }
    }

    /**
     * 获取系统日志
     * @param limit 限制数量
     * @return 系统日志
     */
    @GetMapping("/system/logs")
    public ResponseEntity<ApiResponse<List<SystemManager.SystemLog>>> getSystemLogs(@RequestParam(defaultValue = "100") int limit) {
        try {
            List<SystemManager.SystemLog> logs = systemManager.getSystemLogs(limit);
            return ResponseEntity.ok(ApiResponse.success(logs));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取系统日志失败: " + e.getMessage()));
        }
    }

    /**
     * 重启系统
     * @return 重启结果
     */
    @PostMapping("/system/restart")
    public ResponseEntity<ApiResponse<Boolean>> restartSystem() {
        try {
            boolean result = systemManager.restartSystem();
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "重启系统失败: " + e.getMessage()));
        }
    }

    /**
     * 关闭系统
     * @return 关闭结果
     */
    @PostMapping("/system/shutdown")
    public ResponseEntity<ApiResponse<Boolean>> shutdownSystem() {
        try {
            boolean result = systemManager.shutdownSystem();
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "关闭系统失败: " + e.getMessage()));
        }
    }

    // ==================== 工具方法 ====================

    /**
     * 格式化文件大小
     * @param size 字节大小
     * @return 格式化后的大小
     */
    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return (size / 1024) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            return (size / (1024 * 1024)) + " MB";
        } else {
            return (size / (1024 * 1024 * 1024)) + " GB";
        }
    }

    /**
     * 格式化运行时间
     * @param seconds 秒数
     * @return 格式化后的时间
     */
    private String formatUptime(long seconds) {
        long days = seconds / (24 * 3600);
        long hours = (seconds % (24 * 3600)) / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        if (days > 0) {
            return days + "d " + hours + "h " + minutes + "m " + secs + "s";
        } else if (hours > 0) {
            return hours + "h " + minutes + "m " + secs + "s";
        } else if (minutes > 0) {
            return minutes + "m " + secs + "s";
        } else {
            return secs + "s";
        }
    }
}
