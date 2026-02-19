
package net.ooder.sdk.api.scene;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.api.skill.LocalCapabilityRepository;
import net.ooder.sdk.api.skill.SceneDependencyResolver;
import net.ooder.sdk.api.skill.SkillInstaller;
import net.ooder.sdk.api.skill.SkillService;
import net.ooder.sdk.common.enums.MemberRole;

public interface SceneGroupManager {
    
    CompletableFuture<SceneGroup> create(String sceneId, SceneGroupConfig config);
    
    CompletableFuture<Void> destroy(String sceneGroupId);
    
    CompletableFuture<SceneGroup> get(String sceneGroupId);
    
    CompletableFuture<List<SceneGroup>> listAll();
    
    CompletableFuture<List<SceneGroup>> listByScene(String sceneId);
    
    CompletableFuture<Void> join(String sceneGroupId, String agentId, MemberRole role);
    
    CompletableFuture<Void> leave(String sceneGroupId, String agentId);
    
    CompletableFuture<Void> changeRole(String sceneGroupId, String agentId, MemberRole newRole);
    
    CompletableFuture<MemberRole> getRole(String sceneGroupId, String agentId);
    
    CompletableFuture<List<SceneMember>> listMembers(String sceneGroupId);
    
    CompletableFuture<SceneMember> getPrimary(String sceneGroupId);
    
    CompletableFuture<List<SceneMember>> getBackups(String sceneGroupId);
    
    CompletableFuture<Void> handleFailover(String sceneGroupId, String failedMemberId);
    
    CompletableFuture<FailoverStatus> getFailoverStatus(String sceneGroupId);
    
    CompletableFuture<Void> startHeartbeat(String sceneGroupId);
    
    CompletableFuture<Void> stopHeartbeat(String sceneGroupId);
    
    CompletableFuture<SceneGroupKey> generateKey(String sceneGroupId);
    
    CompletableFuture<SceneGroupKey> reconstructKey(String sceneGroupId, List<KeyShare> shares);
    
    CompletableFuture<Void> distributeKeyShares(String sceneGroupId, SceneGroupKey key);
    
    CompletableFuture<VfsPermission> getVfsPermission(String sceneGroupId, String agentId);
    
    default CompletableFuture<String> autoRegisterSkill(SkillService skill) {
        return CompletableFuture.supplyAsync(() -> {
            return skill.getSkillId();
        });
    }
    
    default CompletableFuture<String> ensureSceneExists(String sceneId, Map<String, Object> config) {
        return CompletableFuture.supplyAsync(() -> sceneId);
    }
    
    default CompletableFuture<Map<String, Object>> getDependencyStatus() {
        return CompletableFuture.supplyAsync(() -> java.util.Collections.emptyMap());
    }
    
    default void setDependencyResolver(SceneDependencyResolver resolver) {}
    
    default void setCapabilityRepository(LocalCapabilityRepository repository) {}
    
    default void setSkillInstaller(SkillInstaller installer) {}
    
    class SceneGroupConfig {
        private String sceneId;
        private int minMembers;
        private int maxMembers;
        private int heartbeatInterval;
        private int heartbeatTimeout;
        private int keyThreshold;
        private Map<String, Object> properties;
        
        public String getSceneId() { return sceneId; }
        public void setSceneId(String sceneId) { this.sceneId = sceneId; }
        public int getMinMembers() { return minMembers; }
        public void setMinMembers(int minMembers) { this.minMembers = minMembers; }
        public int getMaxMembers() { return maxMembers; }
        public void setMaxMembers(int maxMembers) { this.maxMembers = maxMembers; }
        public int getHeartbeatInterval() { return heartbeatInterval; }
        public void setHeartbeatInterval(int heartbeatInterval) { this.heartbeatInterval = heartbeatInterval; }
        public int getHeartbeatTimeout() { return heartbeatTimeout; }
        public void setHeartbeatTimeout(int heartbeatTimeout) { this.heartbeatTimeout = heartbeatTimeout; }
        public int getKeyThreshold() { return keyThreshold; }
        public void setKeyThreshold(int keyThreshold) { this.keyThreshold = keyThreshold; }
        public Map<String, Object> getProperties() { return properties; }
        public void setProperties(Map<String, Object> properties) { this.properties = properties; }
    }
    
    class FailoverStatus {
        private String sceneGroupId;
        private boolean inProgress;
        private String failedMemberId;
        private String newPrimaryId;
        private long startTime;
        private String phase;
        
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public boolean isInProgress() { return inProgress; }
        public void setInProgress(boolean inProgress) { this.inProgress = inProgress; }
        public String getFailedMemberId() { return failedMemberId; }
        public void setFailedMemberId(String failedMemberId) { this.failedMemberId = failedMemberId; }
        public String getNewPrimaryId() { return newPrimaryId; }
        public void setNewPrimaryId(String newPrimaryId) { this.newPrimaryId = newPrimaryId; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public String getPhase() { return phase; }
        public void setPhase(String phase) { this.phase = phase; }
    }
    
    class KeyShare {
        private String agentId;
        private int shareIndex;
        private String shareData;
        
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public int getShareIndex() { return shareIndex; }
        public void setShareIndex(int shareIndex) { this.shareIndex = shareIndex; }
        public String getShareData() { return shareData; }
        public void setShareData(String shareData) { this.shareData = shareData; }
    }
    
    class VfsPermission {
        private String agentId;
        private String sceneGroupId;
        private List<String> readablePaths;
        private List<String> writablePaths;
        private boolean fullAccess;
        
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public List<String> getReadablePaths() { return readablePaths; }
        public void setReadablePaths(List<String> readablePaths) { this.readablePaths = readablePaths; }
        public List<String> getWritablePaths() { return writablePaths; }
        public void setWritablePaths(List<String> writablePaths) { this.writablePaths = writablePaths; }
        public boolean isFullAccess() { return fullAccess; }
        public void setFullAccess(boolean fullAccess) { this.fullAccess = fullAccess; }
    }
}
