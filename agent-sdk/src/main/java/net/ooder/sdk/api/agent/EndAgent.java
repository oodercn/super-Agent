
package net.ooder.sdk.api.agent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.api.scene.SceneGroupKey;

public interface EndAgent {
    
    String getAgentId();
    
    String getAgentName();
    
    String getEndpoint();
    
    String getRouteAgentId();
    
    void start();
    
    void stop();
    
    boolean isHealthy();
    
    CompletableFuture<Void> register(String routeAgentId);
    
    CompletableFuture<Void> deregister();
    
    CompletableFuture<Void> heartbeat();
    
    CompletableFuture<Void> installSkill(SkillPackage skillPackage);
    
    CompletableFuture<Void> uninstallSkill(String skillId);
    
    CompletableFuture<List<String>> listInstalledSkills();
    
    CompletableFuture<Map<String, Object>> invokeSkill(String skillId, Map<String, Object> params);
    
    CompletableFuture<Void> configureSkill(String skillId, Map<String, Object> config);
    
    CompletableFuture<SkillStatus> getSkillStatus(String skillId);
    
    CompletableFuture<Void> startSkill(String skillId);
    
    CompletableFuture<Void> stopSkill(String skillId);
    
    CompletableFuture<Void> joinSceneGroup(String sceneGroupId, SceneGroupKey key);
    
    CompletableFuture<Void> leaveSceneGroup(String sceneGroupId);
    
    CompletableFuture<String> getCurrentRole(String sceneGroupId);
    
    CompletableFuture<Void> promoteToPrimary(String sceneGroupId);
    
    CompletableFuture<Void> demoteToBackup(String sceneGroupId);
    
    CompletableFuture<Void> handleFailover(String sceneGroupId, String failedMemberId);
    
    CompletableFuture<Map<String, Object>> getStatus();
    
    CompletableFuture<Void> updateConfig(Map<String, Object> config);
    
    CompletableFuture<Void> reset();
    
    CompletableFuture<Void> upgrade(String version, String upgradeUrl);
    
    class SkillStatus {
        private String skillId;
        private String status;
        private long installTime;
        private long lastInvokeTime;
        private int invokeCount;
        private String version;
        
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getInstallTime() { return installTime; }
        public void setInstallTime(long installTime) { this.installTime = installTime; }
        public long getLastInvokeTime() { return lastInvokeTime; }
        public void setLastInvokeTime(long lastInvokeTime) { this.lastInvokeTime = lastInvokeTime; }
        public int getInvokeCount() { return invokeCount; }
        public void setInvokeCount(int invokeCount) { this.invokeCount = invokeCount; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
    }
}
