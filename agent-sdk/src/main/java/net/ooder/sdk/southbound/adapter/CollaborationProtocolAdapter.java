
package net.ooder.sdk.southbound.adapter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.southbound.adapter.model.CollaborationMember;
import net.ooder.sdk.southbound.adapter.model.CollaborationMessage;
import net.ooder.sdk.southbound.adapter.model.CollaborationListener;

public interface CollaborationProtocolAdapter {
    
    CompletableFuture<Void> joinCollaboration(String sceneId, String agentId, String role);
    
    CompletableFuture<Void> leaveCollaboration(String sceneId, String agentId);
    
    CompletableFuture<Void> broadcastMessage(String sceneId, CollaborationMessage message);
    
    CompletableFuture<Void> sendDirectMessage(String sceneId, String targetAgent, CollaborationMessage message);
    
    CompletableFuture<List<CollaborationMember>> getMembers(String sceneId);
    
    CompletableFuture<Map<String, Object>> getSharedState(String sceneId);
    
    CompletableFuture<Void> updateSharedState(String sceneId, Map<String, Object> state);
    
    void addCollaborationListener(CollaborationListener listener);
    
    void removeCollaborationListener(CollaborationListener listener);
    
    boolean isInCollaboration(String sceneId);
    
    String getCurrentRole(String sceneId);
}
