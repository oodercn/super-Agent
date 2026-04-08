package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.scene.*;
import net.ooder.mvp.skill.scene.dto.UserSessionDTO;
import net.ooder.mvp.skill.scene.service.SceneGroupService;
import net.ooder.mvp.skill.scene.service.TodoService;
import net.ooder.mvp.skill.scene.service.MenuAutoRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/scene-groups")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class SceneGroupController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(SceneGroupController.class);

    private final SceneGroupService sceneGroupService;
    private final MenuAutoRegisterService menuAutoRegisterService;
    
    @Autowired(required = false)
    private TodoService todoService;

    @Autowired
    public SceneGroupController(SceneGroupService sceneGroupService, 
                                 MenuAutoRegisterService menuAutoRegisterService) {
        this.sceneGroupService = sceneGroupService;
        this.menuAutoRegisterService = menuAutoRegisterService;
    }

    @PostMapping
    public ResultModel<SceneGroupDTO> create(@RequestBody CreateSceneGroupRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("create", request);

        try {
            SceneGroupDTO result = sceneGroupService.create(request.getTemplateId(), request.getConfig());
            logRequestEnd("create", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("create", e);
            return ResultModel.error(500, "创建场景组失败: " + e.getMessage());
        }
    }

    @PutMapping("/{sceneGroupId}")
    public ResultModel<SceneGroupDTO> update(
            @PathVariable String sceneGroupId,
            @RequestBody UpdateSceneGroupRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("update", sceneGroupId);

        try {
            SceneGroupDTO result = sceneGroupService.update(sceneGroupId, request.getConfig());
            if (result == null) {
                logRequestEnd("update", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("场景组不存在");
            }
            logRequestEnd("update", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("update", e);
            return ResultModel.error(500, "更新场景组失败: " + e.getMessage());
        }
    }

    @GetMapping
    public ResultModel<PageResult<SceneGroupDTO>> listAll(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String templateId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listAll", "pageNum=" + pageNum);

        try {
            PageResult<SceneGroupDTO> result = templateId != null && !templateId.isEmpty()
                ? sceneGroupService.listByTemplate(templateId, pageNum, pageSize)
                : sceneGroupService.listAll(pageNum, pageSize);
            logRequestEnd("listAll", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listAll", e);
            return ResultModel.error(500, "获取场景组列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/{sceneGroupId}")
    public ResultModel<SceneGroupDTO> get(@PathVariable String sceneGroupId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("get", sceneGroupId);

        try {
            SceneGroupDTO result = sceneGroupService.get(sceneGroupId);
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

    @DeleteMapping("/{sceneGroupId}")
    public ResultModel<Boolean> destroy(@PathVariable String sceneGroupId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("destroy", sceneGroupId);

        try {
            boolean result = sceneGroupService.destroy(sceneGroupId);
            logRequestEnd("destroy", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("destroy", e);
            return ResultModel.error(500, "销毁场景组失败: " + e.getMessage());
        }
    }

    @PostMapping("/{sceneGroupId}/activate")
    public ResultModel<Boolean> activate(@PathVariable String sceneGroupId, 
                                          HttpServletRequest request,
                                          HttpSession session) {
        long startTime = System.currentTimeMillis();
        logRequestStart("activate", sceneGroupId);

        try {
            SceneGroupDTO group = sceneGroupService.get(sceneGroupId);
            if (group == null) {
                logRequestEnd("activate", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("场景组不存在");
            }
            
            boolean result = sceneGroupService.activate(sceneGroupId);
            
            if (result) {
                String userId = getCurrentUserId(session);
                String templateId = group.getTemplateId();
                String roleInScene = "MANAGER";
                
                try {
                    menuAutoRegisterService.registerMenusOnActivation(
                        sceneGroupId, 
                        templateId, 
                        userId, 
                        roleInScene
                    );
                    log.info("[activate] Menus registered for user: {}, sceneGroup: {}", userId, sceneGroupId);
                } catch (Exception e) {
                    log.warn("[activate] Failed to register menus: {}", e.getMessage());
                }
            }
            
            logRequestEnd("activate", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("activate", e);
            return ResultModel.error(500, "激活场景组失败: " + e.getMessage());
        }
    }
    
    private String getCurrentUserId(HttpSession session) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user != null) {
            return user.getUserId();
        }
        return "default-user";
    }

    @PostMapping("/{sceneGroupId}/deactivate")
    public ResultModel<Boolean> deactivate(@PathVariable String sceneGroupId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deactivate", sceneGroupId);

        try {
            boolean result = sceneGroupService.deactivate(sceneGroupId);
            logRequestEnd("deactivate", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("deactivate", e);
            return ResultModel.error(500, "停用场景组失败: " + e.getMessage());
        }
    }

    @PostMapping("/{sceneGroupId}/participants")
    public ResultModel<Boolean> join(@PathVariable String sceneGroupId, @RequestBody SceneParticipantDTO participant) {
        long startTime = System.currentTimeMillis();
        logRequestStart("join", sceneGroupId);

        try {
            boolean result = sceneGroupService.join(sceneGroupId, participant);
            logRequestEnd("join", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("join", e);
            return ResultModel.error(500, "加入场景失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/{sceneGroupId}/invite")
    public ResultModel<Boolean> inviteMember(
            @PathVariable String sceneGroupId,
            @RequestBody InviteMemberRequest request,
            HttpSession session) {
        long startTime = System.currentTimeMillis();
        logRequestStart("inviteMember", sceneGroupId);

        try {
            SceneGroupDTO group = sceneGroupService.get(sceneGroupId);
            if (group == null) {
                logRequestEnd("inviteMember", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("场景组不存在");
            }
            
            String fromUserId = getCurrentUserId(session);
            String toUserId = request.getUserId();
            String role = request.getRole() != null ? request.getRole() : "MEMBER";
            
            if (todoService != null) {
                boolean created = todoService.createInvitationTodo(
                    sceneGroupId, 
                    fromUserId, 
                    toUserId, 
                    role
                );
                log.info("[inviteMember] Created invitation todo: {} -> {}, sceneGroup: {}", 
                    fromUserId, toUserId, sceneGroupId);
            }
            
            logRequestEnd("inviteMember", true, System.currentTimeMillis() - startTime);
            return ResultModel.success(true);
        } catch (Exception e) {
            logRequestError("inviteMember", e);
            return ResultModel.error(500, "邀请成员失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/{sceneGroupId}/delegate")
    public ResultModel<Boolean> delegateTask(
            @PathVariable String sceneGroupId,
            @RequestBody DelegateTaskRequest request,
            HttpSession session) {
        long startTime = System.currentTimeMillis();
        logRequestStart("delegateTask", sceneGroupId);

        try {
            SceneGroupDTO group = sceneGroupService.get(sceneGroupId);
            if (group == null) {
                logRequestEnd("delegateTask", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("场景组不存在");
            }
            
            String fromUserId = getCurrentUserId(session);
            String toUserId = request.getUserId();
            String title = request.getTitle();
            Long deadline = request.getDeadline();
            
            if (todoService != null) {
                boolean created = todoService.createDelegationTodo(
                    sceneGroupId, 
                    fromUserId, 
                    toUserId, 
                    title, 
                    deadline
                );
                log.info("[delegateTask] Created delegation todo: {} -> {}, sceneGroup: {}", 
                    fromUserId, toUserId, sceneGroupId);
            }
            
            logRequestEnd("delegateTask", true, System.currentTimeMillis() - startTime);
            return ResultModel.success(true);
        } catch (Exception e) {
            logRequestError("delegateTask", e);
            return ResultModel.error(500, "委派任务失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/{sceneGroupId}/approval")
    public ResultModel<Boolean> requestApproval(
            @PathVariable String sceneGroupId,
            @RequestBody ApprovalRequest request,
            HttpSession session) {
        long startTime = System.currentTimeMillis();
        logRequestStart("requestApproval", sceneGroupId);

        try {
            SceneGroupDTO group = sceneGroupService.get(sceneGroupId);
            if (group == null) {
                logRequestEnd("requestApproval", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("场景组不存在");
            }
            
            String fromUserId = getCurrentUserId(session);
            String toUserId = request.getUserId();
            String title = request.getTitle();
            String description = request.getDescription();
            
            if (todoService != null) {
                boolean created = todoService.createApprovalTodo(
                    sceneGroupId, 
                    fromUserId, 
                    toUserId, 
                    title, 
                    description
                );
                log.info("[requestApproval] Created approval todo: {} -> {}, sceneGroup: {}", 
                    fromUserId, toUserId, sceneGroupId);
            }
            
            logRequestEnd("requestApproval", true, System.currentTimeMillis() - startTime);
            return ResultModel.success(true);
        } catch (Exception e) {
            logRequestError("requestApproval", e);
            return ResultModel.error(500, "请求审批失败: " + e.getMessage());
        }
    }

    @GetMapping("/{sceneGroupId}/participants")
    public ResultModel<PageResult<SceneParticipantDTO>> listParticipants(
            @PathVariable String sceneGroupId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listParticipants", sceneGroupId);

        try {
            PageResult<SceneParticipantDTO> result = sceneGroupService.listParticipants(sceneGroupId, pageNum, pageSize);
            logRequestEnd("listParticipants", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listParticipants", e);
            return ResultModel.error(500, "获取参与者列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/{sceneGroupId}/participants/{participantId}")
    public ResultModel<SceneParticipantDTO> getParticipant(
            @PathVariable String sceneGroupId,
            @PathVariable String participantId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getParticipant", participantId);

        try {
            SceneParticipantDTO result = sceneGroupService.getParticipant(sceneGroupId, participantId);
            if (result == null) {
                return ResultModel.notFound("参与者不存在");
            }
            logRequestEnd("getParticipant", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getParticipant", e);
            return ResultModel.error(500, "获取参与者失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{sceneGroupId}/participants/{participantId}")
    public ResultModel<Boolean> leave(
            @PathVariable String sceneGroupId,
            @PathVariable String participantId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("leave", participantId);

        try {
            boolean result = sceneGroupService.leave(sceneGroupId, participantId);
            logRequestEnd("leave", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("leave", e);
            return ResultModel.error(500, "离开场景失败: " + e.getMessage());
        }
    }

    @PutMapping("/{sceneGroupId}/participants/{participantId}/role")
    public ResultModel<Boolean> changeRole(
            @PathVariable String sceneGroupId,
            @PathVariable String participantId,
            @RequestBody ChangeRoleRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("changeRole", participantId);

        try {
            boolean result = sceneGroupService.changeRole(sceneGroupId, participantId, request.getNewRole());
            logRequestEnd("changeRole", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("changeRole", e);
            return ResultModel.error(500, "变更角色失败: " + e.getMessage());
        }
    }

    @PostMapping("/{sceneGroupId}/capabilities")
    public ResultModel<Boolean> bindCapability(@PathVariable String sceneGroupId, @RequestBody CapabilityBindingDTO binding) {
        long startTime = System.currentTimeMillis();
        logRequestStart("bindCapability", sceneGroupId);

        try {
            boolean result = sceneGroupService.bindCapability(sceneGroupId, binding);
            logRequestEnd("bindCapability", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("bindCapability", e);
            return ResultModel.error(500, "绑定能力失败: " + e.getMessage());
        }
    }

    @GetMapping("/{sceneGroupId}/capabilities")
    public ResultModel<PageResult<CapabilityBindingDTO>> listCapabilityBindings(
            @PathVariable String sceneGroupId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listCapabilityBindings", sceneGroupId);

        try {
            PageResult<CapabilityBindingDTO> result = sceneGroupService.listCapabilityBindings(sceneGroupId, pageNum, pageSize);
            logRequestEnd("listCapabilityBindings", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listCapabilityBindings", e);
            return ResultModel.error(500, "获取能力绑定列表失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{sceneGroupId}/capabilities/{bindingId}")
    public ResultModel<Boolean> unbindCapability(
            @PathVariable String sceneGroupId,
            @PathVariable String bindingId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("unbindCapability", bindingId);

        try {
            boolean result = sceneGroupService.unbindCapability(sceneGroupId, bindingId);
            logRequestEnd("unbindCapability", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("unbindCapability", e);
            return ResultModel.error(500, "解绑能力失败: " + e.getMessage());
        }
    }

    @PutMapping("/{sceneGroupId}/capabilities/{bindingId}")
    public ResultModel<Boolean> updateCapabilityBinding(
            @PathVariable String sceneGroupId,
            @PathVariable String bindingId,
            @RequestBody CapabilityBindingDTO binding) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateCapabilityBinding", bindingId);

        try {
            boolean result = sceneGroupService.updateCapabilityBinding(sceneGroupId, bindingId, binding);
            logRequestEnd("updateCapabilityBinding", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("updateCapabilityBinding", e);
            return ResultModel.error(500, "更新能力绑定失败: " + e.getMessage());
        }
    }

    @GetMapping("/{sceneGroupId}/snapshots")
    public ResultModel<List<SceneSnapshotDTO>> listSnapshots(@PathVariable String sceneGroupId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listSnapshots", sceneGroupId);

        try {
            List<SceneSnapshotDTO> result = sceneGroupService.listSnapshots(sceneGroupId);
            logRequestEnd("listSnapshots", result != null ? result.size() : 0, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listSnapshots", e);
            return ResultModel.error(500, "获取快照列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/{sceneGroupId}/snapshots")
    public ResultModel<SceneSnapshotDTO> createSnapshot(@PathVariable String sceneGroupId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("createSnapshot", sceneGroupId);

        try {
            SceneSnapshotDTO result = sceneGroupService.createSnapshot(sceneGroupId);
            logRequestEnd("createSnapshot", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("createSnapshot", e);
            return ResultModel.error(500, "创建快照失败: " + e.getMessage());
        }
    }

    @PostMapping("/{sceneGroupId}/snapshots/{snapshotId}/restore")
    public ResultModel<Boolean> restoreSnapshot(
            @PathVariable String sceneGroupId,
            @PathVariable String snapshotId,
            @RequestBody SceneSnapshotDTO snapshot) {
        long startTime = System.currentTimeMillis();
        logRequestStart("restoreSnapshot", snapshotId);

        try {
            boolean result = sceneGroupService.restoreSnapshot(sceneGroupId, snapshot);
            logRequestEnd("restoreSnapshot", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("restoreSnapshot", e);
            return ResultModel.error(500, "恢复快照失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{sceneGroupId}/snapshots/{snapshotId}")
    public ResultModel<Boolean> deleteSnapshot(
            @PathVariable String sceneGroupId,
            @PathVariable String snapshotId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deleteSnapshot", snapshotId);

        try {
            boolean result = sceneGroupService.deleteSnapshot(sceneGroupId, snapshotId);
            logRequestEnd("deleteSnapshot", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("deleteSnapshot", e);
            return ResultModel.error(500, "删除快照失败: " + e.getMessage());
        }
    }

    @GetMapping("/my/created")
    public ResultModel<PageResult<SceneGroupDTO>> listMyCreated(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listMyCreated", "pageNum=" + pageNum);

        try {
            String currentUserId = "current-user";
            PageResult<SceneGroupDTO> result = sceneGroupService.listByCreator(currentUserId, pageNum, pageSize);
            logRequestEnd("listMyCreated", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listMyCreated", e);
            return ResultModel.error(500, "获取我创建的场景失败: " + e.getMessage());
        }
    }

    @GetMapping("/my/participated")
    public ResultModel<PageResult<SceneGroupDTO>> listMyParticipated(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listMyParticipated", "pageNum=" + pageNum);

        try {
            String currentUserId = "current-user";
            PageResult<SceneGroupDTO> result = sceneGroupService.listByParticipant(currentUserId, pageNum, pageSize);
            logRequestEnd("listMyParticipated", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listMyParticipated", e);
            return ResultModel.error(500, "获取我参与的场景失败: " + e.getMessage());
        }
    }

    public static class CreateSceneGroupRequest {
        private String templateId;
        private SceneGroupConfigDTO config;

        public String getTemplateId() { return templateId; }
        public void setTemplateId(String templateId) { this.templateId = templateId; }
        public SceneGroupConfigDTO getConfig() { return config; }
        public void setConfig(SceneGroupConfigDTO config) { this.config = config; }
    }

    public static class UpdateSceneGroupRequest {
        private SceneGroupConfigDTO config;

        public SceneGroupConfigDTO getConfig() { return config; }
        public void setConfig(SceneGroupConfigDTO config) { this.config = config; }
    }

    public static class ChangeRoleRequest {
        private String newRole;

        public String getNewRole() { return newRole; }
        public void setNewRole(String newRole) { this.newRole = newRole; }
    }
    
    public static class InviteMemberRequest {
        private String userId;
        private String role;
        
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
    
    public static class DelegateTaskRequest {
        private String userId;
        private String title;
        private Long deadline;
        
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public Long getDeadline() { return deadline; }
        public void setDeadline(Long deadline) { this.deadline = deadline; }
    }
    
    public static class ApprovalRequest {
        private String userId;
        private String title;
        private String description;
        
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    @PostMapping("/{sceneGroupId}/knowledge-bases")
    public ResultModel<Boolean> bindKnowledgeBase(
            @PathVariable String sceneGroupId,
            @RequestBody KnowledgeBindingDTO binding) {
        long startTime = System.currentTimeMillis();
        logRequestStart("bindKnowledgeBase", sceneGroupId);

        try {
            boolean result = sceneGroupService.bindKnowledgeBase(sceneGroupId, binding);
            logRequestEnd("bindKnowledgeBase", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("bindKnowledgeBase", e);
            return ResultModel.error(500, "绑定知识库失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{sceneGroupId}/knowledge-bases/{kbId}")
    public ResultModel<Boolean> unbindKnowledgeBase(
            @PathVariable String sceneGroupId,
            @PathVariable String kbId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("unbindKnowledgeBase", kbId);

        try {
            boolean result = sceneGroupService.unbindKnowledgeBase(sceneGroupId, kbId);
            logRequestEnd("unbindKnowledgeBase", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("unbindKnowledgeBase", e);
            return ResultModel.error(500, "解绑知识库失败: " + e.getMessage());
        }
    }

    @PostMapping("/{sceneGroupId}/workflow/start")
    public ResultModel<Map<String, Object>> startWorkflow(@PathVariable String sceneGroupId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("startWorkflow", sceneGroupId);

        try {
            SceneGroupDTO group = sceneGroupService.get(sceneGroupId);
            if (group == null) {
                logRequestEnd("startWorkflow", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("场景组不存在");
            }

            boolean activated = sceneGroupService.activate(sceneGroupId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("sceneGroupId", sceneGroupId);
            result.put("workflowStatus", activated ? "RUNNING" : "FAILED");
            result.put("message", activated ? "工作流启动成功" : "工作流启动失败");
            result.put("startTime", System.currentTimeMillis());
            
            logRequestEnd("startWorkflow", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("startWorkflow", e);
            return ResultModel.error(500, "启动工作流失败: " + e.getMessage());
        }
    }

    @PostMapping("/{sceneGroupId}/workflow/stop")
    public ResultModel<Map<String, Object>> stopWorkflow(@PathVariable String sceneGroupId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("stopWorkflow", sceneGroupId);

        try {
            SceneGroupDTO group = sceneGroupService.get(sceneGroupId);
            if (group == null) {
                logRequestEnd("stopWorkflow", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("场景组不存在");
            }

            boolean deactivated = sceneGroupService.deactivate(sceneGroupId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("sceneGroupId", sceneGroupId);
            result.put("workflowStatus", deactivated ? "STOPPED" : "FAILED");
            result.put("message", deactivated ? "工作流已停止" : "工作流停止失败");
            result.put("stopTime", System.currentTimeMillis());
            
            logRequestEnd("stopWorkflow", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("stopWorkflow", e);
            return ResultModel.error(500, "停止工作流失败: " + e.getMessage());
        }
    }

    @GetMapping("/{sceneGroupId}/workflow/status")
    public ResultModel<Map<String, Object>> getWorkflowStatus(@PathVariable String sceneGroupId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getWorkflowStatus", sceneGroupId);

        try {
            SceneGroupDTO group = sceneGroupService.get(sceneGroupId);
            if (group == null) {
                logRequestEnd("getWorkflowStatus", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("场景组不存在");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("sceneGroupId", sceneGroupId);
            result.put("status", group.getStatus() != null ? group.getStatus().name() : "UNKNOWN");
            result.put("memberCount", group.getMemberCount());
            result.put("lastUpdateTime", group.getLastUpdateTime());
            
            logRequestEnd("getWorkflowStatus", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getWorkflowStatus", e);
            return ResultModel.error(500, "获取工作流状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/{sceneGroupId}/event-log")
    public ResultModel<List<SceneGroupEventLogDTO>> getRecentLogs(
            @PathVariable String sceneGroupId,
            @RequestParam(defaultValue = "50") int limit) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getRecentLogs", sceneGroupId);

        try {
            List<SceneGroupEventLogDTO> logs = sceneGroupService.getEventLog(sceneGroupId, limit);
            logRequestEnd("getRecentLogs", logs.size(), System.currentTimeMillis() - startTime);
            return ResultModel.success(logs);
        } catch (Exception e) {
            logRequestError("getRecentLogs", e);
            return ResultModel.error(500, "获取日志失败: " + e.getMessage());
        }
    }
}
