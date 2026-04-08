package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.scene.UserSceneGroupDTO;
import net.ooder.mvp.skill.scene.service.UserSceneGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user/scene-groups")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class UserSceneGroupController extends BaseController {
    
    private static final Logger log = LoggerFactory.getLogger(UserSceneGroupController.class);
    
    @Autowired
    private UserSceneGroupService userSceneGroupService;
    
    @GetMapping
    public ResultModel<List<UserSceneGroupDTO>> listMySceneGroups(
            @RequestParam(required = false) String userId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listMySceneGroups", "userId=" + userId);
        
        try {
            String effectiveUserId = userId != null ? userId : getCurrentUserId();
            List<UserSceneGroupDTO> result = userSceneGroupService.getUserSceneGroups(effectiveUserId);
            logRequestEnd("listMySceneGroups", result.size() + " groups", System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listMySceneGroups", e);
            return ResultModel.error(500, "获取用户场景组列表失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/{sceneGroupId}")
    public ResultModel<UserSceneGroupDTO> getMySceneGroup(
            @PathVariable String sceneGroupId,
            @RequestParam(required = false) String userId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getMySceneGroup", sceneGroupId);
        
        try {
            String effectiveUserId = userId != null ? userId : getCurrentUserId();
            UserSceneGroupDTO result = userSceneGroupService.getUserSceneGroup(sceneGroupId, effectiveUserId);
            
            if (result == null) {
                logRequestEnd("getMySceneGroup", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("用户未加入该场景组");
            }
            
            logRequestEnd("getMySceneGroup", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getMySceneGroup", e);
            return ResultModel.error(500, "获取用户场景组失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/{sceneGroupId}/join")
    public ResultModel<UserSceneGroupDTO> joinSceneGroup(
            @PathVariable String sceneGroupId,
            @RequestBody(required = false) Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("joinSceneGroup", sceneGroupId);
        
        try {
            String userId = request != null ? request.get("userId") : null;
            String role = request != null ? request.get("role") : "MEMBER";
            
            String effectiveUserId = userId != null ? userId : getCurrentUserId();
            UserSceneGroupDTO result = userSceneGroupService.joinSceneGroup(sceneGroupId, effectiveUserId, role);
            
            if (result == null) {
                logRequestEnd("joinSceneGroup", "Failed", System.currentTimeMillis() - startTime);
                return ResultModel.error(500, "加入场景组失败");
            }
            
            logRequestEnd("joinSceneGroup", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("joinSceneGroup", e);
            return ResultModel.error(500, "加入场景组失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/{sceneGroupId}/leave")
    public ResultModel<Boolean> leaveSceneGroup(
            @PathVariable String sceneGroupId,
            @RequestParam(required = false) String userId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("leaveSceneGroup", sceneGroupId);
        
        try {
            String effectiveUserId = userId != null ? userId : getCurrentUserId();
            boolean result = userSceneGroupService.leaveSceneGroup(sceneGroupId, effectiveUserId);
            logRequestEnd("leaveSceneGroup", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("leaveSceneGroup", e);
            return ResultModel.error(500, "离开场景组失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/{sceneGroupId}/role")
    public ResultModel<UserSceneGroupDTO> updateRole(
            @PathVariable String sceneGroupId,
            @RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateRole", sceneGroupId);
        
        try {
            String userId = request.get("userId");
            String newRole = request.get("role");
            
            if (userId == null || newRole == null) {
                return ResultModel.error(400, "userId 和 role 不能为空");
            }
            
            UserSceneGroupDTO result = userSceneGroupService.updateRole(sceneGroupId, userId, newRole);
            
            if (result == null) {
                logRequestEnd("updateRole", "Failed", System.currentTimeMillis() - startTime);
                return ResultModel.error(500, "更新角色失败");
            }
            
            logRequestEnd("updateRole", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("updateRole", e);
            return ResultModel.error(500, "更新角色失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/{sceneGroupId}/members")
    public ResultModel<PageResult<UserSceneGroupDTO>> listMembers(
            @PathVariable String sceneGroupId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listMembers", sceneGroupId);
        
        try {
            PageResult<UserSceneGroupDTO> result = 
                userSceneGroupService.listSceneGroupMembers(sceneGroupId, pageNum, pageSize);
            logRequestEnd("listMembers", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listMembers", e);
            return ResultModel.error(500, "获取成员列表失败: " + e.getMessage());
        }
    }
    
    private String getCurrentUserId() {
        return "user-current";
    }
}
