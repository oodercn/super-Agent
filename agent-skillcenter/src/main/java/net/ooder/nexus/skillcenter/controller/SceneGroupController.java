package net.ooder.nexus.skillcenter.controller;

import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.scene.*;
import net.ooder.skillcenter.service.SceneGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scene-groups")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class SceneGroupController extends BaseController {

    private final SceneGroupService sceneGroupService;

    @Autowired
    public SceneGroupController(SceneGroupService sceneGroupService) {
        this.sceneGroupService = sceneGroupService;
    }

    @PostMapping("/create")
    public ResultModel<SceneGroupDTO> create(@RequestBody SceneGroupCreateRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("create", request);

        try {
            SceneGroupDTO result = sceneGroupService.create(request.getSceneId(), request.getConfig());
            logRequestEnd("create", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("create", e);
            return ResultModel.error(500, "创建场景组失败: " + e.getMessage());
        }
    }

    @PostMapping("/destroy")
    public ResultModel<Boolean> destroy(@RequestBody SceneGroupIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("destroy", request);

        try {
            boolean result = sceneGroupService.destroy(request.getSceneGroupId());
            logRequestEnd("destroy", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("destroy", e);
            return ResultModel.error(500, "销毁场景组失败: " + e.getMessage());
        }
    }

    @PostMapping("/get")
    public ResultModel<SceneGroupDTO> get(@RequestBody SceneGroupIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("get", request);

        try {
            SceneGroupDTO result = sceneGroupService.get(request.getSceneGroupId());
            if (result == null) {
                logRequestEnd("get", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("场景组不存在");
            }
            logRequestEnd("get", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("get", e);
            return ResultModel.error(500, "获取场景组失败: " + e.getMessage());
        }
    }

    @PostMapping("/list")
    public ResultModel<PageResult<SceneGroupDTO>> listAll(@RequestBody PageRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listAll", request);

        try {
            PageResult<SceneGroupDTO> result = sceneGroupService.listAll(
                request.getPageNum(), request.getPageSize());
            logRequestEnd("listAll", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listAll", e);
            return ResultModel.error(500, "获取场景组列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/join")
    public ResultModel<Boolean> join(@RequestBody JoinRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("join", request);

        try {
            boolean result = sceneGroupService.join(
                request.getSceneGroupId(), request.getAgentId(), request.getRole());
            logRequestEnd("join", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("join", e);
            return ResultModel.error(500, "加入场景组失败: " + e.getMessage());
        }
    }

    @PostMapping("/leave")
    public ResultModel<Boolean> leave(@RequestBody LeaveRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("leave", request);

        try {
            boolean result = sceneGroupService.leave(
                request.getSceneGroupId(), request.getAgentId());
            logRequestEnd("leave", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("leave", e);
            return ResultModel.error(500, "离开场景组失败: " + e.getMessage());
        }
    }

    @PostMapping("/members")
    public ResultModel<PageResult<SceneMemberDTO>> listMembers(@RequestBody SceneGroupIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listMembers", request);

        try {
            PageResult<SceneMemberDTO> result = sceneGroupService.listMembers(
                request.getSceneGroupId(), request.getPageNum(), request.getPageSize());
            logRequestEnd("listMembers", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listMembers", e);
            return ResultModel.error(500, "获取成员列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/primary")
    public ResultModel<SceneMemberDTO> getPrimary(@RequestBody SceneGroupIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getPrimary", request);

        try {
            SceneMemberDTO result = sceneGroupService.getPrimary(request.getSceneGroupId());
            logRequestEnd("getPrimary", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getPrimary", e);
            return ResultModel.error(500, "获取主节点失败: " + e.getMessage());
        }
    }

    @PostMapping("/failover")
    public ResultModel<Boolean> handleFailover(@RequestBody FailoverRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("handleFailover", request);

        try {
            String failedMemberId = request.getFailedMemberId();
            if (failedMemberId == null || failedMemberId.isEmpty()) {
                failedMemberId = request.getNewPrimaryAgentId();
            }
            boolean result = sceneGroupService.handleFailover(
                request.getSceneGroupId(), failedMemberId);
            logRequestEnd("handleFailover", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("handleFailover", e);
            return ResultModel.error(500, "故障转移失败: " + e.getMessage());
        }
    }

    @PostMapping("/failover-status")
    public ResultModel<FailoverStatusDTO> getFailoverStatus(@RequestBody SceneGroupIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getFailoverStatus", request);

        try {
            FailoverStatusDTO result = sceneGroupService.getFailoverStatus(request.getSceneGroupId());
            logRequestEnd("getFailoverStatus", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getFailoverStatus", e);
            return ResultModel.error(500, "获取故障转移状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/key/generate")
    public ResultModel<SceneGroupKeyDTO> generateKey(@RequestBody SceneGroupIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("generateKey", request);

        try {
            SceneGroupKeyDTO result = sceneGroupService.generateKey(request.getSceneGroupId());
            logRequestEnd("generateKey", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("generateKey", e);
            return ResultModel.error(500, "生成密钥失败: " + e.getMessage());
        }
    }

    @PostMapping("/vfs-permission")
    public ResultModel<VfsPermissionDTO> getVfsPermission(@RequestBody VfsPermissionRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getVfsPermission", request);

        try {
            VfsPermissionDTO result = sceneGroupService.getVfsPermission(
                request.getSceneGroupId(), request.getAgentId());
            logRequestEnd("getVfsPermission", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getVfsPermission", e);
            return ResultModel.error(500, "获取VFS权限失败: " + e.getMessage());
        }
    }

    @PostMapping("/vfs/permissions")
    public ResultModel<PageResult<VfsPermissionDTO>> listVfsPermissions(@RequestBody SceneGroupIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listVfsPermissions", request);

        try {
            PageResult<VfsPermissionDTO> result = sceneGroupService.listVfsPermissions(
                request.getSceneGroupId(), request.getPageNum(), request.getPageSize());
            logRequestEnd("listVfsPermissions", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listVfsPermissions", e);
            return ResultModel.error(500, "获取VFS权限列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/vfs/permissions/add")
    public ResultModel<VfsPermissionDTO> addVfsPermission(@RequestBody AddVfsPermissionRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("addVfsPermission", request);

        try {
            VfsPermissionDTO result = sceneGroupService.addVfsPermission(
                request.getSceneGroupId(), request.getAgentId(), 
                request.getPermissionType(), request.getPath());
            logRequestEnd("addVfsPermission", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("addVfsPermission", e);
            return ResultModel.error(500, "添加VFS权限失败: " + e.getMessage());
        }
    }

    public static class SceneGroupCreateRequest {
        private String sceneId;
        private SceneGroupConfigDTO config;
        public String getSceneId() { return sceneId; }
        public void setSceneId(String sceneId) { this.sceneId = sceneId; }
        public SceneGroupConfigDTO getConfig() { return config; }
        public void setConfig(SceneGroupConfigDTO config) { this.config = config; }
    }

    public static class SceneGroupIdRequest {
        private String sceneGroupId;
        private int pageNum = 1;
        private int pageSize = 10;
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public int getPageNum() { return pageNum; }
        public void setPageNum(int pageNum) { this.pageNum = pageNum; }
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    }

    public static class PageRequest {
        private int pageNum = 1;
        private int pageSize = 10;
        public int getPageNum() { return pageNum; }
        public void setPageNum(int pageNum) { this.pageNum = pageNum; }
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    }

    public static class JoinRequest {
        private String sceneGroupId;
        private String agentId;
        private String role;
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    public static class LeaveRequest {
        private String sceneGroupId;
        private String agentId;
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
    }

    public static class FailoverRequest {
        private String sceneGroupId;
        private String failedMemberId;
        private String newPrimaryAgentId;
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public String getFailedMemberId() { return failedMemberId; }
        public void setFailedMemberId(String failedMemberId) { this.failedMemberId = failedMemberId; }
        public String getNewPrimaryAgentId() { return newPrimaryAgentId; }
        public void setNewPrimaryAgentId(String newPrimaryAgentId) { this.newPrimaryAgentId = newPrimaryAgentId; }
    }

    public static class VfsPermissionRequest {
        private String sceneGroupId;
        private String agentId;
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
    }

    public static class AddVfsPermissionRequest {
        private String sceneGroupId;
        private String agentId;
        private String permissionType;
        private String path;
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getPermissionType() { return permissionType; }
        public void setPermissionType(String permissionType) { this.permissionType = permissionType; }
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
    }
}
