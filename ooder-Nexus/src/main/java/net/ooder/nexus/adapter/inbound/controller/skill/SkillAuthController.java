package net.ooder.nexus.adapter.inbound.controller.skill;

import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.nexus.domain.skill.model.SkillAuthorization;
import net.ooder.nexus.domain.skill.model.SkillResourceLog;
import net.ooder.nexus.service.skill.SkillAuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/skill/auth", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class SkillAuthController {

    private static final Logger log = LoggerFactory.getLogger(SkillAuthController.class);

    @Autowired
    private SkillAuthService skillAuthService;

    @PostMapping("/pending")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getPendingAuthorizations(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        log.info("Get pending authorizations request: userId={}", userId);
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            List<SkillAuthorization> auths = skillAuthService.getPendingAuthorizations(userId);
            List<Map<String, Object>> data = new java.util.ArrayList<>();
            for (SkillAuthorization auth : auths) {
                data.add(auth.toMap());
            }
            result.setData(data);
            result.setSize(data.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting pending authorizations", e);
            result.setRequestStatus(500);
            result.setMessage("获取待授权列表失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getAllAuthorizations(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String skillId = request.get("skillId");
        log.info("Get all authorizations request: userId={}, skillId={}", userId, skillId);
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            List<SkillAuthorization> auths;
            if (skillId != null && !skillId.isEmpty()) {
                auths = skillAuthService.getSkillAuthorizations(userId, skillId);
            } else {
                auths = skillAuthService.getAllAuthorizations(userId);
            }
            List<Map<String, Object>> data = new java.util.ArrayList<>();
            for (SkillAuthorization auth : auths) {
                data.add(auth.toMap());
            }
            result.setData(data);
            result.setSize(data.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting authorizations", e);
            result.setRequestStatus(500);
            result.setMessage("获取授权列表失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/request")
    @ResponseBody
    public ResultModel<Map<String, Object>> createAuthorizationRequest(@RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        String skillId = (String) request.get("skillId");
        String skillName = (String) request.get("skillName");
        String authType = (String) request.get("authType");
        String authReason = (String) request.get("authReason");
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> resourcePermissions = (List<Map<String, Object>>) request.get("resourcePermissions");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> scenePermissions = (List<Map<String, Object>>) request.get("scenePermissions");
        
        log.info("Create authorization request: skill={}, user={}", skillId, userId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            SkillAuthorization auth = skillAuthService.createAuthorizationRequest(
                    userId, skillId, skillName, authType, resourcePermissions, scenePermissions, authReason);
            result.setData(auth.toMap());
            result.setRequestStatus(200);
            result.setMessage("授权请求已创建");
        } catch (Exception e) {
            log.error("Error creating authorization request", e);
            result.setRequestStatus(500);
            result.setMessage("创建授权请求失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/approve")
    @ResponseBody
    public ResultModel<Boolean> approveAuthorization(@RequestBody Map<String, String> request) {
        String authId = request.get("authId");
        String userId = request.get("userId");
        log.info("Approve authorization request: authId={}, userId={}", authId, userId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = skillAuthService.approveAuthorization(authId, userId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "授权已批准" : "授权不存在或无权操作");
        } catch (Exception e) {
            log.error("Error approving authorization", e);
            result.setRequestStatus(500);
            result.setMessage("批准授权失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/reject")
    @ResponseBody
    public ResultModel<Boolean> rejectAuthorization(@RequestBody Map<String, String> request) {
        String authId = request.get("authId");
        String userId = request.get("userId");
        String reason = request.get("reason");
        log.info("Reject authorization request: authId={}, userId={}", authId, userId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = skillAuthService.rejectAuthorization(authId, userId, reason);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "授权已拒绝" : "授权不存在或无权操作");
        } catch (Exception e) {
            log.error("Error rejecting authorization", e);
            result.setRequestStatus(500);
            result.setMessage("拒绝授权失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/revoke")
    @ResponseBody
    public ResultModel<Boolean> revokeAuthorization(@RequestBody Map<String, String> request) {
        String authId = request.get("authId");
        String userId = request.get("userId");
        log.info("Revoke authorization request: authId={}, userId={}", authId, userId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = skillAuthService.revokeAuthorization(authId, userId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "授权已撤销" : "授权不存在或无权操作");
        } catch (Exception e) {
            log.error("Error revoking authorization", e);
            result.setRequestStatus(500);
            result.setMessage("撤销授权失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/check")
    @ResponseBody
    public ResultModel<Boolean> checkPermission(@RequestBody Map<String, String> request) {
        String skillId = request.get("skillId");
        String userId = request.get("userId");
        String resourceType = request.get("resourceType");
        String resourceId = request.get("resourceId");
        String action = request.get("action");
        log.info("Check permission request: skill={}, user={}, resource={}, action={}", 
                skillId, userId, resourceId, action);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean hasPermission = skillAuthService.checkPermission(skillId, userId, resourceType, resourceId, action);
            result.setData(hasPermission);
            result.setRequestStatus(200);
            result.setMessage(hasPermission ? "有权限" : "无权限");
        } catch (Exception e) {
            log.error("Error checking permission", e);
            result.setRequestStatus(500);
            result.setMessage("检查权限失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/log/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getResourceLogs(@RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        log.info("Get resource logs request: userId={}", userId);
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            List<SkillResourceLog> logs = skillAuthService.getResourceLogs(userId, request);
            List<Map<String, Object>> data = new java.util.ArrayList<>();
            for (SkillResourceLog logEntry : logs) {
                data.add(logEntry.toMap());
            }
            result.setData(data);
            result.setSize(data.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting resource logs", e);
            result.setRequestStatus(500);
            result.setMessage("获取资源日志失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/log/stats")
    @ResponseBody
    public ResultModel<Map<String, Object>> getResourceUsageStats(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String skillId = request.get("skillId");
        log.info("Get resource usage stats request: userId={}, skillId={}", userId, skillId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            Map<String, Object> stats = skillAuthService.getResourceUsageStats(userId, skillId);
            result.setData(stats);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting resource usage stats", e);
            result.setRequestStatus(500);
            result.setMessage("获取使用统计失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/install/requests")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getInstallRequests(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        log.info("Get install requests request: userId={}", userId);
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            List<Map<String, Object>> requests = skillAuthService.getInstallRequests(userId);
            result.setData(requests);
            result.setSize(requests.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting install requests", e);
            result.setRequestStatus(500);
            result.setMessage("获取安装请求失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/install/process")
    @ResponseBody
    public ResultModel<Map<String, Object>> processInstallRequest(@RequestBody Map<String, Object> request) {
        String requestId = (String) request.get("requestId");
        String userId = (String) request.get("userId");
        Boolean approved = (Boolean) request.get("approved");
        String reason = (String) request.get("reason");
        log.info("Process install request: requestId={}, approved={}", requestId, approved);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            Map<String, Object> processResult = skillAuthService.processInstallRequest(requestId, userId, approved, reason);
            result.setData(processResult);
            result.setRequestStatus(200);
            result.setMessage((String) processResult.get("message"));
        } catch (Exception e) {
            log.error("Error processing install request", e);
            result.setRequestStatus(500);
            result.setMessage("处理安装请求失败: " + e.getMessage());
        }
        return result;
    }
}
