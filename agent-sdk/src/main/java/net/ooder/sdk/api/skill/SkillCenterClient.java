
package net.ooder.sdk.api.skill;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface SkillCenterClient {
    
    CompletableFuture<List<SkillPackage>> listSkills();
    
    CompletableFuture<SkillPackage> getSkill(String skillId);
    
    CompletableFuture<List<SkillPackage>> searchSkills(String query);
    
    CompletableFuture<List<SkillPackage>> getSkillsByScene(String sceneId);
    
    CompletableFuture<String> getDownloadUrl(String skillId, String version);
    
    CompletableFuture<SkillManifest> getManifest(String skillId);
    
    CompletableFuture<List<String>> getVersions(String skillId);
    
    CompletableFuture<Void> registerSkill(SkillPackage skillPackage);
    
    CompletableFuture<Void> unregisterSkill(String skillId);
    
    CompletableFuture<Void> updateSkillMetadata(String skillId, Map<String, Object> metadata);
    
    CompletableFuture<List<SceneInfo>> listScenes();
    
    CompletableFuture<SceneInfo> getScene(String sceneId);
    
    CompletableFuture<SceneJoinResult> joinScene(String sceneId, String agentId);
    
    CompletableFuture<Void> leaveScene(String sceneId, String agentId);
    
    String getEndpoint();
    
    void setEndpoint(String endpoint);
    
    boolean isConnected();
    
    CompletableFuture<Void> connect();
    
    CompletableFuture<Void> disconnect();
    
    class SceneInfo {
        private String sceneId;
        private String sceneName;
        private String description;
        private String version;
        private List<String> requiredCapabilities;
        private int memberCount;
        private int maxMembers;
        
        public String getSceneId() { return sceneId; }
        public void setSceneId(String sceneId) { this.sceneId = sceneId; }
        public String getSceneName() { return sceneName; }
        public void setSceneName(String sceneName) { this.sceneName = sceneName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public List<String> getRequiredCapabilities() { return requiredCapabilities; }
        public void setRequiredCapabilities(List<String> requiredCapabilities) { this.requiredCapabilities = requiredCapabilities; }
        public int getMemberCount() { return memberCount; }
        public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
        public int getMaxMembers() { return maxMembers; }
        public void setMaxMembers(int maxMembers) { this.maxMembers = maxMembers; }
    }
    
    class SceneJoinResult {
        private boolean success;
        private String sceneGroupId;
        private String error;
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}
