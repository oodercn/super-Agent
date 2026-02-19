package net.ooder.sdk.nexus.model;

import net.ooder.sdk.southbound.protocol.model.PeerInfo;
import net.ooder.sdk.southbound.protocol.model.SceneGroupInfo;

public interface NexusListener {
    
    void onStateChanged(NexusState oldState, NexusState newState);
    
    void onRoleChanged(String oldRole, String newRole);
    
    void onLoginSuccess(UserSession session);
    
    void onLogout();
    
    void onNetworkChanged(boolean online);
    
    void onPeerDiscovered(PeerInfo peer);
    
    void onSceneGroupJoined(SceneGroupInfo group);
    
    void onSceneGroupLeft(String groupId);
}
