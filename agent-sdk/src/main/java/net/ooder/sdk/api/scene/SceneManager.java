
package net.ooder.sdk.api.scene;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.api.skill.Capability;

public interface SceneManager {
    
    CompletableFuture<SceneDefinition> create(SceneDefinition definition);
    
    CompletableFuture<Void> delete(String sceneId);
    
    CompletableFuture<SceneDefinition> get(String sceneId);
    
    CompletableFuture<List<SceneDefinition>> listAll();
    
    CompletableFuture<Void> activate(String sceneId);
    
    CompletableFuture<Void> deactivate(String sceneId);
    
    CompletableFuture<SceneState> getState(String sceneId);
    
    CompletableFuture<Void> addCapability(String sceneId, Capability capability);
    
    CompletableFuture<Void> removeCapability(String sceneId, String capId);
    
    CompletableFuture<List<Capability>> listCapabilities(String sceneId);
    
    CompletableFuture<Capability> getCapability(String sceneId, String capId);
    
    CompletableFuture<Void> addCollaborativeScene(String sceneId, String collaborativeSceneId);
    
    CompletableFuture<Void> removeCollaborativeScene(String sceneId, String collaborativeSceneId);
    
    CompletableFuture<List<String>> listCollaborativeScenes(String sceneId);
    
    CompletableFuture<Void> updateConfig(String sceneId, Map<String, Object> config);
    
    CompletableFuture<Map<String, Object>> getConfig(String sceneId);
    
    CompletableFuture<SceneSnapshot> createSnapshot(String sceneId);
    
    CompletableFuture<Void> restoreSnapshot(String sceneId, SceneSnapshot snapshot);
    
    class SceneState {
        private String sceneId;
        private boolean active;
        private int memberCount;
        private List<String> installedSkills;
        private long createTime;
        private long lastUpdateTime;
        
        public String getSceneId() { return sceneId; }
        public void setSceneId(String sceneId) { this.sceneId = sceneId; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
        public int getMemberCount() { return memberCount; }
        public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
        public List<String> getInstalledSkills() { return installedSkills; }
        public void setInstalledSkills(List<String> installedSkills) { this.installedSkills = installedSkills; }
        public long getCreateTime() { return createTime; }
        public void setCreateTime(long createTime) { this.createTime = createTime; }
        public long getLastUpdateTime() { return lastUpdateTime; }
        public void setLastUpdateTime(long lastUpdateTime) { this.lastUpdateTime = lastUpdateTime; }
    }
}
