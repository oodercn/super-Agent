package net.ooder.skillcenter.sdk.protocol;

import net.ooder.nexus.skillcenter.dto.protocol.CollaborationDTO;
import net.ooder.nexus.skillcenter.dto.protocol.CollaborationDTO.InvitationDTO;
import net.ooder.nexus.skillcenter.dto.protocol.CollaborationDTO.JoinRequestDTO;
import net.ooder.nexus.skillcenter.dto.protocol.CollaborationDTO.MemberDTO;
import net.ooder.nexus.skillcenter.dto.protocol.CollaborationDTO.SceneGroupInfoDTO;
import net.ooder.nexus.skillcenter.dto.protocol.CollaborationDTO.TaskDTO;
import net.ooder.nexus.skillcenter.dto.protocol.CollaborationDTO.TaskResultDTO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CollaborationProtocolAdapter {

    CompletableFuture<SceneGroupInfoDTO> joinSceneGroup(String groupId, JoinRequestDTO request);

    CompletableFuture<Void> leaveSceneGroup(String groupId);

    CompletableFuture<Void> acceptInvitation(String invitationId);

    CompletableFuture<Void> declineInvitation(String invitationId);

    CompletableFuture<List<InvitationDTO>> getPendingInvitations();

    CompletableFuture<TaskDTO> receiveTask(String groupId);

    CompletableFuture<Void> submitTaskResult(String groupId, String taskId, TaskResultDTO result);

    CompletableFuture<List<TaskDTO>> getPendingTasks(String groupId);

    CompletableFuture<Void> syncState(String groupId, Map<String, Object> state);

    CompletableFuture<Map<String, Object>> getGroupState(String groupId);

    CompletableFuture<List<MemberDTO>> getGroupMembers(String groupId);

    void addCollaborationListener(CollaborationEventListener listener);

    void removeCollaborationListener(CollaborationEventListener listener);

    boolean isAvailable();

    interface CollaborationEventListener {
        void onInvitationReceived(InvitationDTO invitation);
        void onGroupJoined(SceneGroupInfoDTO group);
        void onGroupLeft(String groupId);
        void onTaskAssigned(TaskDTO task);
        void onTaskCompleted(String taskId, TaskResultDTO result);
        void onMemberJoined(String groupId, MemberDTO member);
        void onMemberLeft(String groupId, String memberId);
        void onStateChanged(String groupId, Map<String, Object> state);
    }
}
