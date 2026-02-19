package net.ooder.sdk.api.scene;

import java.util.List;
import java.util.Map;

import net.ooder.sdk.common.enums.MemberRole;
import net.ooder.sdk.common.enums.SceneType;

public class SceneGroup {
    
    private String sceneGroupId;
    private String sceneId;
    private List<SceneMember> members;
    private SceneGroupKey key;
    private String status;
    private long createTime;
    private long lastUpdateTime;
    private Map<String, Object> properties;
    
    private int maxMembers;
    private int heartbeatInterval;
    private int heartbeatTimeout;
    private boolean autoFailover;
    private String previousPrimaryId;
    private long primarySince;
    private int failoverCount;
    private Map<String, Object> sharedState;
    private long lastStateUpdate;
    private List<String> pendingInvitations;
    
    public String getSceneGroupId() {
        return sceneGroupId;
    }
    
    public void setSceneGroupId(String sceneGroupId) {
        this.sceneGroupId = sceneGroupId;
    }
    
    public String getSceneId() {
        return sceneId;
    }
    
    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
    
    public List<SceneMember> getMembers() {
        return members;
    }
    
    public void setMembers(List<SceneMember> members) {
        this.members = members;
    }
    
    public SceneGroupKey getKey() {
        return key;
    }
    
    public void setKey(SceneGroupKey key) {
        this.key = key;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public long getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    
    public Map<String, Object> getProperties() {
        return properties;
    }
    
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
    
    public int getMaxMembers() {
        return maxMembers;
    }
    
    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }
    
    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }
    
    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }
    
    public int getHeartbeatTimeout() {
        return heartbeatTimeout;
    }
    
    public void setHeartbeatTimeout(int heartbeatTimeout) {
        this.heartbeatTimeout = heartbeatTimeout;
    }
    
    public boolean isAutoFailover() {
        return autoFailover;
    }
    
    public void setAutoFailover(boolean autoFailover) {
        this.autoFailover = autoFailover;
    }
    
    public String getPreviousPrimaryId() {
        return previousPrimaryId;
    }
    
    public void setPreviousPrimaryId(String previousPrimaryId) {
        this.previousPrimaryId = previousPrimaryId;
    }
    
    public long getPrimarySince() {
        return primarySince;
    }
    
    public void setPrimarySince(long primarySince) {
        this.primarySince = primarySince;
    }
    
    public int getFailoverCount() {
        return failoverCount;
    }
    
    public void setFailoverCount(int failoverCount) {
        this.failoverCount = failoverCount;
    }
    
    public Map<String, Object> getSharedState() {
        return sharedState;
    }
    
    public void setSharedState(Map<String, Object> sharedState) {
        this.sharedState = sharedState;
    }
    
    public long getLastStateUpdate() {
        return lastStateUpdate;
    }
    
    public void setLastStateUpdate(long lastStateUpdate) {
        this.lastStateUpdate = lastStateUpdate;
    }
    
    public List<String> getPendingInvitations() {
        return pendingInvitations;
    }
    
    public void setPendingInvitations(List<String> pendingInvitations) {
        this.pendingInvitations = pendingInvitations;
    }
    
    public SceneMember getPrimary() {
        if (members == null) return null;
        return members.stream()
            .filter(m -> m.getRole() == MemberRole.PRIMARY)
            .findFirst()
            .orElse(null);
    }
    
    public List<SceneMember> getBackups() {
        if (members == null) return java.util.Collections.emptyList();
        return members.stream()
            .filter(m -> m.getRole() == MemberRole.BACKUP)
            .collect(java.util.stream.Collectors.toList());
    }
    
    public int getMemberCount() {
        return members != null ? members.size() : 0;
    }
    
    public boolean hasMember(String agentId) {
        if (members == null) return false;
        return members.stream().anyMatch(m -> agentId.equals(m.getAgentId()));
    }
    
    public SceneMember getMember(String agentId) {
        if (members == null) return null;
        return members.stream()
            .filter(m -> agentId.equals(m.getAgentId()))
            .findFirst()
            .orElse(null);
    }
}
