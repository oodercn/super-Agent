package net.ooder.sdk.southbound.protocol.impl;

import net.ooder.sdk.southbound.protocol.*;
import net.ooder.sdk.southbound.protocol.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class CollaborationProtocolImpl implements CollaborationProtocol {
    
    private static final Logger log = LoggerFactory.getLogger(CollaborationProtocolImpl.class);
    
    private final Map<String, SceneGroupInfo> joinedGroups;
    private final Map<String, InvitationInfo> pendingInvitations;
    private final Map<String, List<TaskInfo>> groupTasks;
    private final Map<String, Map<String, Object>> groupStates;
    private final List<CollaborationListener> listeners;
    private final ExecutorService executor;
    
    public CollaborationProtocolImpl() {
        this.joinedGroups = new ConcurrentHashMap<String, SceneGroupInfo>();
        this.pendingInvitations = new ConcurrentHashMap<String, InvitationInfo>();
        this.groupTasks = new ConcurrentHashMap<String, List<TaskInfo>>();
        this.groupStates = new ConcurrentHashMap<String, Map<String, Object>>();
        this.listeners = new CopyOnWriteArrayList<CollaborationListener>();
        this.executor = Executors.newCachedThreadPool();
        log.info("CollaborationProtocolImpl initialized");
    }
    
    @Override
    public CompletableFuture<SceneGroupInfo> joinSceneGroup(String groupId, JoinRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Joining scene group: groupId={}, agentId={}", groupId, request.getAgentId());
            
            SceneGroupInfo existingGroup = joinedGroups.get(groupId);
            if (existingGroup != null) {
                List<String> members = new ArrayList<String>(existingGroup.getMemberIds());
                if (!members.contains(request.getAgentId())) {
                    members.add(request.getAgentId());
                    existingGroup.setMemberIds(members);
                    existingGroup.setMemberCount(members.size());
                }
                notifyGroupJoined(existingGroup);
                log.info("Rejoined existing scene group: groupId={}", groupId);
                return existingGroup;
            }
            
            SceneGroupInfo group = new SceneGroupInfo();
            group.setGroupId(groupId);
            group.setGroupName("SceneGroup-" + groupId.substring(0, Math.min(8, groupId.length())));
            group.setSceneId("scene-" + groupId);
            group.setPrimaryId(request.getAgentId());
            
            List<String> members = new ArrayList<String>();
            members.add(request.getAgentId());
            group.setMemberIds(members);
            group.setMemberCount(1);
            group.setStatus(GroupStatus.ACTIVE);
            group.setCreatedAt(System.currentTimeMillis());
            
            joinedGroups.put(groupId, group);
            groupTasks.put(groupId, new CopyOnWriteArrayList<TaskInfo>());
            groupStates.put(groupId, new ConcurrentHashMap<String, Object>());
            
            notifyGroupJoined(group);
            log.info("Joined new scene group: groupId={}", groupId);
            return group;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> leaveSceneGroup(String groupId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Leaving scene group: groupId={}", groupId);
            
            SceneGroupInfo removed = joinedGroups.remove(groupId);
            if (removed != null) {
                groupTasks.remove(groupId);
                groupStates.remove(groupId);
                notifyGroupLeft(groupId);
                log.info("Left scene group: groupId={}", groupId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> acceptInvitation(String invitationId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Accepting invitation: invitationId={}", invitationId);
            
            InvitationInfo invitation = pendingInvitations.remove(invitationId);
            if (invitation != null) {
                invitation.setStatus(InvitationStatus.ACCEPTED);
                JoinRequest request = new JoinRequest();
                request.setAgentId("agent-" + UUID.randomUUID().toString().substring(0, 8));
                joinSceneGroup(invitation.getGroupId(), request).join();
                log.info("Invitation accepted: invitationId={}", invitationId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> declineInvitation(String invitationId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Declining invitation: invitationId={}", invitationId);
            
            InvitationInfo invitation = pendingInvitations.remove(invitationId);
            if (invitation != null) {
                invitation.setStatus(InvitationStatus.DECLINED);
                log.info("Invitation declined: invitationId={}", invitationId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<InvitationInfo>> getPendingInvitations() {
        return CompletableFuture.supplyAsync(() -> new ArrayList<InvitationInfo>(pendingInvitations.values()), executor);
    }
    
    @Override
    public CompletableFuture<TaskInfo> receiveTask(String groupId) {
        return CompletableFuture.supplyAsync(() -> {
            List<TaskInfo> tasks = groupTasks.get(groupId);
            if (tasks != null && !tasks.isEmpty()) {
                for (TaskInfo task : tasks) {
                    if (task.getStatus() == TaskStatus.PENDING || task.getStatus() == TaskStatus.ASSIGNED) {
                        task.setStatus(TaskStatus.IN_PROGRESS);
                        notifyTaskAssigned(task);
                        return task;
                    }
                }
            }
            return null;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> submitTaskResult(String groupId, String taskId, TaskResult result) {
        return CompletableFuture.runAsync(() -> {
            log.info("Submitting task result: groupId={}, taskId={}", groupId, taskId);
            
            List<TaskInfo> tasks = groupTasks.get(groupId);
            if (tasks != null) {
                for (TaskInfo task : tasks) {
                    if (task.getTaskId().equals(taskId)) {
                        task.setStatus(result.isSuccess() ? TaskStatus.COMPLETED : TaskStatus.FAILED);
                        notifyTaskCompleted(taskId, result);
                        log.info("Task result submitted: taskId={}, success={}", taskId, result.isSuccess());
                        break;
                    }
                }
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<TaskInfo>> getPendingTasks(String groupId) {
        return CompletableFuture.supplyAsync(() -> {
            List<TaskInfo> tasks = groupTasks.get(groupId);
            if (tasks == null) {
                return new ArrayList<TaskInfo>();
            }
            
            List<TaskInfo> pending = new ArrayList<TaskInfo>();
            for (TaskInfo task : tasks) {
                if (task.getStatus() == TaskStatus.PENDING || task.getStatus() == TaskStatus.ASSIGNED) {
                    pending.add(task);
                }
            }
            return pending;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> syncState(String groupId, Map<String, Object> state) {
        return CompletableFuture.runAsync(() -> {
            Map<String, Object> groupState = groupStates.get(groupId);
            if (groupState != null) {
                groupState.putAll(state);
                notifyStateChanged(groupId, groupState);
                log.debug("State synced for group: {}", groupId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> getGroupState(String groupId) {
        return CompletableFuture.supplyAsync(() -> groupStates.get(groupId), executor);
    }
    
    @Override
    public CompletableFuture<List<MemberInfo>> getGroupMembers(String groupId) {
        return CompletableFuture.supplyAsync(() -> {
            SceneGroupInfo group = joinedGroups.get(groupId);
            if (group == null) {
                return new ArrayList<MemberInfo>();
            }
            
            List<MemberInfo> members = new ArrayList<MemberInfo>();
            for (String memberId : group.getMemberIds()) {
                MemberInfo member = new MemberInfo();
                member.setMemberId(memberId);
                member.setMemberName("Member-" + memberId);
                member.setRole(memberId.equals(group.getPrimaryId()) ? MemberRole.PRIMARY : MemberRole.MEMBER);
                member.setStatus(MemberStatus.ONLINE);
                member.setJoinedAt(group.getCreatedAt());
                member.setLastActiveAt(System.currentTimeMillis());
                members.add(member);
            }
            return members;
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<SceneGroupInfo>> listJoinedGroups() {
        return CompletableFuture.supplyAsync(() -> {
            return new ArrayList<SceneGroupInfo>(joinedGroups.values());
        }, executor);
    }
    
    @Override
    public void addCollaborationListener(CollaborationListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeCollaborationListener(CollaborationListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyGroupJoined(SceneGroupInfo group) {
        for (CollaborationListener listener : listeners) {
            try {
                listener.onGroupJoined(group);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyGroupLeft(String groupId) {
        for (CollaborationListener listener : listeners) {
            try {
                listener.onGroupLeft(groupId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyTaskAssigned(TaskInfo task) {
        for (CollaborationListener listener : listeners) {
            try {
                listener.onTaskAssigned(task);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyTaskCompleted(String taskId, TaskResult result) {
        for (CollaborationListener listener : listeners) {
            try {
                listener.onTaskCompleted(taskId, result);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyStateChanged(String groupId, Map<String, Object> state) {
        for (CollaborationListener listener : listeners) {
            try {
                listener.onStateChanged(groupId, state);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public void addInvitation(InvitationInfo invitation) {
        pendingInvitations.put(invitation.getInvitationId(), invitation);
        for (CollaborationListener listener : listeners) {
            try {
                listener.onInvitationReceived(invitation);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public void shutdown() {
        log.info("Shutting down CollaborationProtocol");
        executor.shutdown();
        joinedGroups.clear();
        pendingInvitations.clear();
        groupTasks.clear();
        groupStates.clear();
        log.info("CollaborationProtocol shutdown complete");
    }
}
