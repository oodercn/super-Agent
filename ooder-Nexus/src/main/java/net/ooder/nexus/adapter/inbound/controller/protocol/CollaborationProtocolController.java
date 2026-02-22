package net.ooder.nexus.adapter.inbound.controller.protocol;

import net.ooder.config.ResultModel;
import net.ooder.sdk.southbound.protocol.CollaborationProtocol;
import net.ooder.sdk.southbound.protocol.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/protocol/collaboration")
public class CollaborationProtocolController {

    private static final Logger log = LoggerFactory.getLogger(CollaborationProtocolController.class);

    private final CollaborationProtocol collaborationProtocol;

    @Autowired
    public CollaborationProtocolController(@Autowired(required = false) CollaborationProtocol collaborationProtocol) {
        this.collaborationProtocol = collaborationProtocol;
        log.info("CollaborationProtocolController initialized: {}", 
            collaborationProtocol != null ? "SDK protocol available" : "using mock");
    }

    @PostMapping("/join")
    @ResponseBody
    public ResultModel<SceneGroupInfo> joinSceneGroup(@RequestBody Map<String, Object> params) {
        log.info("Join scene group requested: {}", params);
        ResultModel<SceneGroupInfo> result = new ResultModel<>();

        try {
            String groupId = (String) params.get("groupId");
            String inviteCode = (String) params.get("inviteCode");
            
            if (collaborationProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                JoinRequest request = new JoinRequest();
                request.setAgentId((String) params.get("agentId"));
                request.setInviteCode(inviteCode);
                
                CompletableFuture<SceneGroupInfo> future = collaborationProtocol.joinSceneGroup(groupId, request);
                SceneGroupInfo groupInfo = future.get();
                
                result.setData(groupInfo);
                result.setRequestStatus(200);
                result.setMessage("Joined scene group successfully");
            }
        } catch (Exception e) {
            log.error("Join scene group failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to join scene group: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/leave")
    @ResponseBody
    public ResultModel<Void> leaveSceneGroup(@RequestBody Map<String, Object> params) {
        log.info("Leave scene group requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String groupId = (String) params.get("groupId");
            
            if (collaborationProtocol != null) {
                collaborationProtocol.leaveSceneGroup(groupId);
            }
            result.setRequestStatus(200);
            result.setMessage("Left scene group successfully");
        } catch (Exception e) {
            log.error("Leave scene group failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to leave scene group: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/invitations")
    @ResponseBody
    public ResultModel<List<InvitationInfo>> getPendingInvitations() {
        log.info("Get pending invitations requested");
        ResultModel<List<InvitationInfo>> result = new ResultModel<>();

        try {
            if (collaborationProtocol == null) {
                result.setData(new ArrayList<>());
                result.setRequestStatus(200);
            } else {
                CompletableFuture<List<InvitationInfo>> future = collaborationProtocol.getPendingInvitations();
                List<InvitationInfo> invitations = future.get();
                result.setData(invitations);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Get pending invitations failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get pending invitations: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/invitation/accept")
    @ResponseBody
    public ResultModel<Void> acceptInvitation(@RequestBody Map<String, Object> params) {
        log.info("Accept invitation requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String invitationId = (String) params.get("invitationId");
            
            if (collaborationProtocol != null) {
                collaborationProtocol.acceptInvitation(invitationId);
            }
            result.setRequestStatus(200);
            result.setMessage("Invitation accepted");
        } catch (Exception e) {
            log.error("Accept invitation failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to accept invitation: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/invitation/decline")
    @ResponseBody
    public ResultModel<Void> declineInvitation(@RequestBody Map<String, Object> params) {
        log.info("Decline invitation requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String invitationId = (String) params.get("invitationId");
            
            if (collaborationProtocol != null) {
                collaborationProtocol.declineInvitation(invitationId);
            }
            result.setRequestStatus(200);
            result.setMessage("Invitation declined");
        } catch (Exception e) {
            log.error("Decline invitation failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to decline invitation: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/tasks")
    @ResponseBody
    public ResultModel<List<TaskInfo>> getPendingTasks(@RequestBody Map<String, Object> params) {
        log.info("Get pending tasks requested: {}", params);
        ResultModel<List<TaskInfo>> result = new ResultModel<>();

        try {
            String groupId = (String) params.get("groupId");
            
            if (collaborationProtocol == null) {
                result.setData(new ArrayList<>());
                result.setRequestStatus(200);
            } else {
                CompletableFuture<List<TaskInfo>> future = collaborationProtocol.getPendingTasks(groupId);
                List<TaskInfo> tasks = future.get();
                result.setData(tasks);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Get pending tasks failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get pending tasks: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/task/receive")
    @ResponseBody
    public ResultModel<TaskInfo> receiveTask(@RequestBody Map<String, Object> params) {
        log.info("Receive task requested: {}", params);
        ResultModel<TaskInfo> result = new ResultModel<>();

        try {
            String groupId = (String) params.get("groupId");
            
            if (collaborationProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                CompletableFuture<TaskInfo> future = collaborationProtocol.receiveTask(groupId);
                TaskInfo task = future.get();
                result.setData(task);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Receive task failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to receive task: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/task/submit")
    @ResponseBody
    public ResultModel<Void> submitTaskResult(@RequestBody Map<String, Object> params) {
        log.info("Submit task result requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String groupId = (String) params.get("groupId");
            String taskId = (String) params.get("taskId");
            Map<String, Object> resultData = (Map<String, Object>) params.get("result");
            
            if (collaborationProtocol != null) {
                TaskResult taskResult = new TaskResult();
                taskResult.setTaskId(taskId);
                taskResult.setOutput(resultData);
                collaborationProtocol.submitTaskResult(groupId, taskId, taskResult);
            }
            result.setRequestStatus(200);
            result.setMessage("Task result submitted");
        } catch (Exception e) {
            log.error("Submit task result failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to submit task result: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/sync")
    @ResponseBody
    public ResultModel<Void> syncState(@RequestBody Map<String, Object> params) {
        log.info("Sync state requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String groupId = (String) params.get("groupId");
            Map<String, Object> state = (Map<String, Object>) params.get("state");
            
            if (collaborationProtocol != null) {
                collaborationProtocol.syncState(groupId, state);
            }
            result.setRequestStatus(200);
            result.setMessage("State synced");
        } catch (Exception e) {
            log.error("Sync state failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to sync state: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/members")
    @ResponseBody
    public ResultModel<List<MemberInfo>> getGroupMembers(@RequestBody Map<String, Object> params) {
        log.info("Get group members requested: {}", params);
        ResultModel<List<MemberInfo>> result = new ResultModel<>();

        try {
            String groupId = (String) params.get("groupId");
            
            if (collaborationProtocol == null) {
                result.setData(new ArrayList<>());
                result.setRequestStatus(200);
            } else {
                CompletableFuture<List<MemberInfo>> future = collaborationProtocol.getGroupMembers(groupId);
                List<MemberInfo> members = future.get();
                result.setData(members);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Get group members failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get group members: " + e.getMessage());
        }

        return result;
    }

    @GetMapping("/status")
    @ResponseBody
    public ResultModel<Map<String, Object>> getStatus() {
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        Map<String, Object> status = new HashMap<>();
        status.put("sdkAvailable", collaborationProtocol != null);
        status.put("protocolType", "CollaborationProtocol");
        
        result.setData(status);
        result.setRequestStatus(200);
        return result;
    }
}
