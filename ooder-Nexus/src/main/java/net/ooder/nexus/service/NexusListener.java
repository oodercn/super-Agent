package net.ooder.nexus.service;

/**
 * Nexus 事件监听器
 */
public interface NexusListener {
    
    /**
     * 状态变更事件
     */
    void onStatusChanged(NexusStatus oldStatus, NexusStatus newStatus);
    
    /**
     * 对等节点发现事件
     */
    void onPeerDiscovered(PeerInfo peer);
    
    /**
     * 对等节点离线事件
     */
    void onPeerLost(String peerId);
    
    /**
     * 角色变更事件
     */
    void onRoleChanged(RoleDecision newRole);
    
    /**
     * 场景组加入事件
     */
    void onSceneGroupJoined(String groupId);
    
    /**
     * 场景组离开事件
     */
    void onSceneGroupLeft(String groupId);
    
    /**
     * 网络状态变更事件
     */
    void onNetworkStateChanged(boolean online);
}
