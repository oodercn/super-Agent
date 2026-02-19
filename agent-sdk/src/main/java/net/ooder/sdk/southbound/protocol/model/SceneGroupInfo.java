package net.ooder.sdk.southbound.protocol.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SceneGroupInfo {
    
    private String groupId;
    private String groupName;
    private String sceneId;
    private String primaryId;
    private String previousPrimaryId;
    private List<String> memberIds;
    private int memberCount;
    private int maxMembers;
    private GroupStatus status;
    private long createdAt;
    private long primarySince;
    private long lastStateUpdate;
    private int heartbeatInterval;
    private int heartbeatTimeout;
    private boolean autoFailover;
    private int failoverCount;
    private Map<String, Object> config;
    private Map<String, Object> sharedState;
    private List<String> pendingInvitations;
    
    public SceneGroupInfo() {
        this.maxMembers = 10;
        this.heartbeatInterval = 5000;
        this.heartbeatTimeout = 15000;
        this.autoFailover = true;
        this.failoverCount = 0;
        this.sharedState = new ConcurrentHashMap<>();
        this.pendingInvitations = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.lastStateUpdate = System.currentTimeMillis();
    }
    
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    
    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    
    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    
    public String getPrimaryId() { return primaryId; }
    public void setPrimaryId(String primaryId) { this.primaryId = primaryId; }
    
    public String getPreviousPrimaryId() { return previousPrimaryId; }
    public void setPreviousPrimaryId(String previousPrimaryId) { this.previousPrimaryId = previousPrimaryId; }
    
    public List<String> getMemberIds() { return memberIds; }
    public void setMemberIds(List<String> memberIds) { this.memberIds = memberIds; }
    
    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
    
    public int getMaxMembers() { return maxMembers; }
    public void setMaxMembers(int maxMembers) { this.maxMembers = maxMembers; }
    
    public GroupStatus getStatus() { return status; }
    public void setStatus(GroupStatus status) { this.status = status; }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    public long getPrimarySince() { return primarySince; }
    public void setPrimarySince(long primarySince) { this.primarySince = primarySince; }
    
    public long getLastStateUpdate() { return lastStateUpdate; }
    public void setLastStateUpdate(long lastStateUpdate) { this.lastStateUpdate = lastStateUpdate; }
    public void touchLastStateUpdate() { this.lastStateUpdate = System.currentTimeMillis(); }
    
    public int getHeartbeatInterval() { return heartbeatInterval; }
    public void setHeartbeatInterval(int heartbeatInterval) { this.heartbeatInterval = heartbeatInterval; }
    
    public int getHeartbeatTimeout() { return heartbeatTimeout; }
    public void setHeartbeatTimeout(int heartbeatTimeout) { this.heartbeatTimeout = heartbeatTimeout; }
    
    public boolean isAutoFailover() { return autoFailover; }
    public void setAutoFailover(boolean autoFailover) { this.autoFailover = autoFailover; }
    
    public int getFailoverCount() { return failoverCount; }
    public void setFailoverCount(int failoverCount) { this.failoverCount = failoverCount; }
    public void incrementFailoverCount() { this.failoverCount++; }
    
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
    
    public Map<String, Object> getSharedState() { return sharedState; }
    public void setSharedState(Map<String, Object> sharedState) { this.sharedState = sharedState; }
    public void updateSharedState(String key, Object value) {
        this.sharedState.put(key, value);
        this.lastStateUpdate = System.currentTimeMillis();
    }
    
    public List<String> getPendingInvitations() { return pendingInvitations; }
    public void setPendingInvitations(List<String> pendingInvitations) { this.pendingInvitations = pendingInvitations; }
    public void addPendingInvitation(String invitationId) { this.pendingInvitations.add(invitationId); }
    public void removePendingInvitation(String invitationId) { this.pendingInvitations.remove(invitationId); }
    
    public boolean isFull() {
        return memberCount >= maxMembers;
    }
    
    public boolean hasMember(String memberId) {
        return memberIds != null && memberIds.contains(memberId);
    }
}
