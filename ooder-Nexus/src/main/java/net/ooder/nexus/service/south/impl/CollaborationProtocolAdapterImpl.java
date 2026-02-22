package net.ooder.nexus.service.south.impl;

import net.ooder.nexus.service.south.CollaborationProtocolAdapter;
import net.ooder.nexus.service.south.DiscoveryProtocolAdapter.PeerDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class CollaborationProtocolAdapterImpl implements CollaborationProtocolAdapter {

    private static final Logger log = LoggerFactory.getLogger(CollaborationProtocolAdapterImpl.class);

    private final List<CollaborationEventListener> listeners = new CopyOnWriteArrayList<CollaborationEventListener>();
    private final Map<String, SceneGroupInfoDTO> groups = new ConcurrentHashMap<String, SceneGroupInfoDTO>();
    private final Map<String, List<InvitationDTO>> pendingInvitations = new ConcurrentHashMap<String, List<InvitationDTO>>();
    private final Map<String, List<TaskDTO>> pendingTasks = new ConcurrentHashMap<String, List<TaskDTO>>();
    private final Map<String, Map<String, Object>> groupStates = new ConcurrentHashMap<String, Map<String, Object>>();
    
    private String localNodeId;

    @Autowired
    public CollaborationProtocolAdapterImpl() {
        this.localNodeId = UUID.randomUUID().toString();
        log.info("CollaborationProtocolAdapter initialized with SDK 0.7.2");
    }

    @Override
    public CompletableFuture<SceneGroupInfoDTO> joinSceneGroup(String groupId, JoinRequestDTO request) {
        log.info("Joining scene group: {}", groupId);
        
        return CompletableFuture.supplyAsync(() -> {
            SceneGroupInfoDTO group = groups.get(groupId);
            
            if (group == null) {
                group = new SceneGroupInfoDTO();
                group.setGroupId(groupId);
                group.setGroupName("Scene Group " + groupId.substring(0, 8));
                group.setSceneId("scene-" + groupId);
                group.setPrimaryId(localNodeId);
                group.setMemberIds(new ArrayList<String>());
                group.setMemberCount(0);
                group.setStatus("ACTIVE");
                group.setCreatedAt(System.currentTimeMillis());
                groups.put(groupId, group);
            }
            
            if (!group.getMemberIds().contains(request.getAgentId())) {
                group.getMemberIds().add(request.getAgentId());
                group.setMemberCount(group.getMemberIds().size());
            }
            
            groupStates.putIfAbsent(groupId, new HashMap<String, Object>());
            
            notifyGroupJoined(group);
            
            log.info("Joined scene group: {}", groupId);
            return group;
        });
    }

    @Override
    public CompletableFuture<Void> leaveSceneGroup(String groupId) {
        log.info("Leaving scene group: {}", groupId);
        
        return CompletableFuture.runAsync(() -> {
            SceneGroupInfoDTO group = groups.get(groupId);
            if (group != null) {
                group.getMemberIds().remove(localNodeId);
                group.setMemberCount(group.getMemberIds().size());
                
                if (group.getMemberIds().isEmpty()) {
                    groups.remove(groupId);
                    groupStates.remove(groupId);
                    pendingTasks.remove(groupId);
                }
            }
            
            notifyGroupLeft(groupId);
            log.info("Left scene group: {}", groupId);
        });
    }

    @Override
    public CompletableFuture<Void> acceptInvitation(String invitationId) {
        log.info("Accepting invitation: {}", invitationId);
        
        return CompletableFuture.runAsync(() -> {
            for (List<InvitationDTO> invitations : pendingInvitations.values()) {
                for (InvitationDTO invitation : invitations) {
                    if (invitation.getInvitationId().equals(invitationId)) {
                        invitation.setStatus("ACCEPTED");
                        
                        JoinRequestDTO request = new JoinRequestDTO();
                        request.setAgentId(localNodeId);
                        joinSceneGroup(invitation.getGroupId(), request);
                        
                        invitations.remove(invitation);
                        return;
                    }
                }
            }
        });
    }

    @Override
    public CompletableFuture<Void> declineInvitation(String invitationId) {
        log.info("Declining invitation: {}", invitationId);
        
        return CompletableFuture.runAsync(() -> {
            for (List<InvitationDTO> invitations : pendingInvitations.values()) {
                for (InvitationDTO invitation : invitations) {
                    if (invitation.getInvitationId().equals(invitationId)) {
                        invitation.setStatus("DECLINED");
                        invitations.remove(invitation);
                        return;
                    }
                }
            }
        });
    }

    @Override
    public CompletableFuture<List<InvitationDTO>> getPendingInvitations() {
        log.info("Getting pending invitations");
        
        return CompletableFuture.supplyAsync(() -> {
            List<InvitationDTO> allInvitations = new ArrayList<InvitationDTO>();
            for (List<InvitationDTO> invitations : pendingInvitations.values()) {
                allInvitations.addAll(invitations);
            }
            return allInvitations;
        });
    }

    @Override
    public CompletableFuture<TaskDTO> receiveTask(String groupId) {
        log.info("Receiving task from group: {}", groupId);
        
        return CompletableFuture.supplyAsync(() -> {
            List<TaskDTO> tasks = pendingTasks.get(groupId);
            if (tasks != null && !tasks.isEmpty()) {
                TaskDTO task = tasks.remove(0);
                task.setStatus("RECEIVED");
                return task;
            }
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> submitTaskResult(String groupId, String taskId, TaskResultDTO result) {
        log.info("Submitting task result: {} for group: {}", taskId, groupId);
        
        return CompletableFuture.runAsync(() -> {
            result.setTaskId(taskId);
            result.setCompletedAt(System.currentTimeMillis());
            
            notifyTaskCompleted(taskId, result);
        });
    }

    @Override
    public CompletableFuture<List<TaskDTO>> getPendingTasks(String groupId) {
        log.info("Getting pending tasks for group: {}", groupId);
        
        return CompletableFuture.supplyAsync(() -> {
            List<TaskDTO> tasks = pendingTasks.get(groupId);
            return tasks != null ? new ArrayList<TaskDTO>(tasks) : new ArrayList<TaskDTO>();
        });
    }

    @Override
    public CompletableFuture<Void> syncState(String groupId, Map<String, Object> state) {
        log.info("Syncing state for group: {}", groupId);
        
        return CompletableFuture.runAsync(() -> {
            Map<String, Object> groupState = groupStates.get(groupId);
            if (groupState != null) {
                groupState.putAll(state);
                notifyStateChanged(groupId, groupState);
            }
        });
    }

    @Override
    public CompletableFuture<Map<String, Object>> getGroupState(String groupId) {
        log.info("Getting state for group: {}", groupId);
        
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> state = groupStates.get(groupId);
            return state != null ? new HashMap<String, Object>(state) : new HashMap<String, Object>();
        });
    }

    @Override
    public CompletableFuture<List<MemberDTO>> getGroupMembers(String groupId) {
        log.info("Getting members for group: {}", groupId);
        
        return CompletableFuture.supplyAsync(() -> {
            List<MemberDTO> members = new ArrayList<MemberDTO>();
            SceneGroupInfoDTO group = groups.get(groupId);
            
            if (group != null && group.getMemberIds() != null) {
                for (String memberId : group.getMemberIds()) {
                    MemberDTO member = new MemberDTO();
                    member.setMemberId(memberId);
                    member.setMemberName("Member-" + memberId.substring(0, 8));
                    member.setRole(memberId.equals(group.getPrimaryId()) ? "PRIMARY" : "MEMBER");
                    member.setStatus("ONLINE");
                    member.setJoinedAt(group.getCreatedAt());
                    member.setLastActiveAt(System.currentTimeMillis());
                    members.add(member);
                }
            }
            
            return members;
        });
    }

    @Override
    public void addCollaborationListener(CollaborationEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeCollaborationListener(CollaborationEventListener listener) {
        listeners.remove(listener);
    }

    public void receiveInvitation(InvitationDTO invitation) {
        log.info("Received invitation: {}", invitation.getInvitationId());
        
        String groupId = invitation.getGroupId();
        pendingInvitations.computeIfAbsent(groupId, k -> new ArrayList<InvitationDTO>()).add(invitation);
        
        notifyInvitationReceived(invitation);
    }

    public void assignTask(String groupId, TaskDTO task) {
        log.info("Assigning task: {} to group: {}", task.getTaskId(), groupId);
        
        task.setGroupId(groupId);
        task.setStatus("PENDING");
        pendingTasks.computeIfAbsent(groupId, k -> new ArrayList<TaskDTO>()).add(task);
        
        notifyTaskAssigned(task);
    }

    private void notifyInvitationReceived(InvitationDTO invitation) {
        for (CollaborationEventListener listener : listeners) {
            try {
                listener.onInvitationReceived(invitation);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyGroupJoined(SceneGroupInfoDTO group) {
        for (CollaborationEventListener listener : listeners) {
            try {
                listener.onGroupJoined(group);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyGroupLeft(String groupId) {
        for (CollaborationEventListener listener : listeners) {
            try {
                listener.onGroupLeft(groupId);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyTaskAssigned(TaskDTO task) {
        for (CollaborationEventListener listener : listeners) {
            try {
                listener.onTaskAssigned(task);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyTaskCompleted(String taskId, TaskResultDTO result) {
        for (CollaborationEventListener listener : listeners) {
            try {
                listener.onTaskCompleted(taskId, result);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyStateChanged(String groupId, Map<String, Object> state) {
        for (CollaborationEventListener listener : listeners) {
            try {
                listener.onStateChanged(groupId, state);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }
}
