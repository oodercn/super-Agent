package net.ooder.sdk.southbound.protocol;

import net.ooder.sdk.southbound.protocol.model.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CollaborationProtocol {
    
    CompletableFuture<SceneGroupInfo> joinSceneGroup(String groupId, JoinRequest request);
    
    CompletableFuture<Void> leaveSceneGroup(String groupId);
    
    CompletableFuture<Void> acceptInvitation(String invitationId);
    
    CompletableFuture<Void> declineInvitation(String invitationId);
    
    CompletableFuture<List<InvitationInfo>> getPendingInvitations();
    
    CompletableFuture<TaskInfo> receiveTask(String groupId);
    
    CompletableFuture<Void> submitTaskResult(String groupId, String taskId, TaskResult result);
    
    CompletableFuture<List<TaskInfo>> getPendingTasks(String groupId);
    
    CompletableFuture<Void> syncState(String groupId, Map<String, Object> state);
    
    CompletableFuture<Map<String, Object>> getGroupState(String groupId);
    
    CompletableFuture<List<MemberInfo>> getGroupMembers(String groupId);
    
    CompletableFuture<List<SceneGroupInfo>> listJoinedGroups();
    
    void addCollaborationListener(CollaborationListener listener);
    
    void removeCollaborationListener(CollaborationListener listener);
}
