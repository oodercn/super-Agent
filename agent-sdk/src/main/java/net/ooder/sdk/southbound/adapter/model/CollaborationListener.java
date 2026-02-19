
package net.ooder.sdk.southbound.adapter.model;

public interface CollaborationListener {
    
    void onMemberJoined(String sceneId, CollaborationMember member);
    
    void onMemberLeft(String sceneId, String agentId);
    
    void onMessageReceived(String sceneId, CollaborationMessage message);
    
    void onStateChanged(String sceneId, java.util.Map<String, Object> newState);
    
    void onRoleChanged(String sceneId, String agentId, String newRole);
    
    void onError(String sceneId, String errorCode, String errorMessage);
}
