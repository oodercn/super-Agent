
package net.ooder.sdk.southbound.adapter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.ooder.sdk.southbound.adapter.CollaborationProtocolAdapter;
import net.ooder.sdk.southbound.adapter.model.CollaborationListener;
import net.ooder.sdk.southbound.adapter.model.CollaborationMember;
import net.ooder.sdk.southbound.adapter.model.CollaborationMessage;
import net.ooder.sdk.southbound.protocol.CollaborationProtocol;
import net.ooder.sdk.southbound.protocol.model.InvitationInfo;
import net.ooder.sdk.southbound.protocol.model.JoinRequest;
import net.ooder.sdk.southbound.protocol.model.MemberInfo;
import net.ooder.sdk.southbound.protocol.model.MemberRole;
import net.ooder.sdk.southbound.protocol.model.MemberStatus;
import net.ooder.sdk.southbound.protocol.model.SceneGroupInfo;
import net.ooder.sdk.southbound.protocol.model.TaskInfo;
import net.ooder.sdk.southbound.protocol.model.TaskResult;

public class CollaborationProtocolAdapterImpl implements CollaborationProtocolAdapter {
    
    private final CollaborationProtocol collaborationProtocol;
    private final Map<String, String> sceneRoles = new ConcurrentHashMap<>();
    private final List<CollaborationListener> listeners = new CopyOnWriteArrayList<>();
    private final String localAgentId;
    
    public CollaborationProtocolAdapterImpl(CollaborationProtocol collaborationProtocol, String localAgentId) {
        this.collaborationProtocol = collaborationProtocol;
        this.localAgentId = localAgentId;
        setupInternalListener();
    }
    
    private void setupInternalListener() {
        collaborationProtocol.addCollaborationListener(new net.ooder.sdk.southbound.protocol.model.CollaborationListener() {
            @Override
            public void onInvitationReceived(InvitationInfo invitation) {
            }
            
            @Override
            public void onGroupJoined(SceneGroupInfo group) {
                sceneRoles.put(group.getGroupId(), localAgentId);
            }
            
            @Override
            public void onGroupLeft(String groupId) {
                sceneRoles.remove(groupId);
            }
            
            @Override
            public void onTaskAssigned(TaskInfo task) {
            }
            
            @Override
            public void onTaskCompleted(String taskId, TaskResult result) {
            }
            
            @Override
            public void onMemberJoined(String groupId, MemberInfo member) {
                CollaborationMember cm = convertToCollaborationMember(member);
                notifyMemberJoined(groupId, cm);
            }
            
            @Override
            public void onMemberLeft(String groupId, String memberId) {
                notifyMemberLeft(groupId, memberId);
            }
            
            @Override
            public void onStateChanged(String groupId, Map<String, Object> state) {
                notifyStateChanged(groupId, state);
            }
        });
    }
    
    @Override
    public CompletableFuture<Void> joinCollaboration(String sceneId, String agentId, String role) {
        JoinRequest request = new JoinRequest();
        request.setAgentId(agentId);
        
        return collaborationProtocol.joinSceneGroup(sceneId, request)
            .thenAccept(groupInfo -> {
                sceneRoles.put(sceneId, role);
            });
    }
    
    @Override
    public CompletableFuture<Void> leaveCollaboration(String sceneId, String agentId) {
        return collaborationProtocol.leaveSceneGroup(sceneId)
            .thenRun(() -> {
                sceneRoles.remove(sceneId);
            });
    }
    
    @Override
    public CompletableFuture<Void> broadcastMessage(String sceneId, CollaborationMessage message) {
        Map<String, Object> state = new ConcurrentHashMap<>();
        state.put("messageType", message.getMessageType());
        state.put("content", message.getContent());
        state.put("senderId", message.getSenderId());
        state.put("timestamp", message.getTimestamp());
        
        return collaborationProtocol.syncState(sceneId, state);
    }
    
    @Override
    public CompletableFuture<Void> sendDirectMessage(String sceneId, String targetAgent, CollaborationMessage message) {
        Map<String, Object> state = new ConcurrentHashMap<>();
        state.put("messageType", "direct");
        state.put("targetAgent", targetAgent);
        state.put("content", message.getContent());
        state.put("senderId", message.getSenderId());
        state.put("timestamp", message.getTimestamp());
        
        return collaborationProtocol.syncState(sceneId, state);
    }
    
    @Override
    public CompletableFuture<List<CollaborationMember>> getMembers(String sceneId) {
        return collaborationProtocol.getGroupMembers(sceneId)
            .thenApply(members -> {
                List<CollaborationMember> result = new ArrayList<>();
                if (members != null) {
                    for (MemberInfo mi : members) {
                        result.add(convertToCollaborationMember(mi));
                    }
                }
                return result;
            });
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> getSharedState(String sceneId) {
        return collaborationProtocol.getGroupState(sceneId);
    }
    
    @Override
    public CompletableFuture<Void> updateSharedState(String sceneId, Map<String, Object> state) {
        return collaborationProtocol.syncState(sceneId, state);
    }
    
    @Override
    public void addCollaborationListener(CollaborationListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }
    
    @Override
    public void removeCollaborationListener(CollaborationListener listener) {
        listeners.remove(listener);
    }
    
    @Override
    public boolean isInCollaboration(String sceneId) {
        return sceneRoles.containsKey(sceneId);
    }
    
    @Override
    public String getCurrentRole(String sceneId) {
        return sceneRoles.get(sceneId);
    }
    
    private CollaborationMember convertToCollaborationMember(MemberInfo memberInfo) {
        CollaborationMember member = new CollaborationMember();
        member.setAgentId(memberInfo.getMemberId());
        member.setAgentName(memberInfo.getMemberName());
        member.setRole(memberInfo.getRole() != null ? memberInfo.getRole().name() : "UNKNOWN");
        member.setStatus(memberInfo.getStatus() != null ? memberInfo.getStatus().name() : "UNKNOWN");
        member.setJoinedAt(memberInfo.getJoinedAt());
        member.setLastActiveAt(memberInfo.getLastActiveAt());
        return member;
    }
    
    private void notifyMemberJoined(String sceneId, CollaborationMember member) {
        for (CollaborationListener listener : listeners) {
            try {
                listener.onMemberJoined(sceneId, member);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void notifyMemberLeft(String sceneId, String agentId) {
        for (CollaborationListener listener : listeners) {
            try {
                listener.onMemberLeft(sceneId, agentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void notifyStateChanged(String sceneId, Map<String, Object> state) {
        for (CollaborationListener listener : listeners) {
            try {
                listener.onStateChanged(sceneId, state);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
