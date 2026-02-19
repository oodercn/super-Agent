
package net.ooder.sdk.service.skillcenter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.skill.SkillCenterClient;
import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.service.skillcenter.RemoteDeploymentService.DeploymentResult;

public class SkillCenterService {
    
    private static final Logger log = LoggerFactory.getLogger(SkillCenterService.class);
    
    private final SkillCenterClientImpl client;
    private final RemoteDeploymentService deploymentService;
    
    public SkillCenterService() {
        this.client = new SkillCenterClientImpl();
        this.deploymentService = new RemoteDeploymentService(client);
    }
    
    public SkillCenterService(String endpoint) {
        this.client = new SkillCenterClientImpl(endpoint);
        this.deploymentService = new RemoteDeploymentService(client);
    }
    
    public void setEndpoint(String endpoint) {
        client.setEndpoint(endpoint);
    }
    
    public String getEndpoint() {
        return client.getEndpoint();
    }
    
    public CompletableFuture<Void> connect() {
        return client.connect();
    }
    
    public CompletableFuture<Void> disconnect() {
        return client.disconnect();
    }
    
    public boolean isConnected() {
        return client.isConnected();
    }
    
    public CompletableFuture<List<SkillPackage>> listSkills() {
        return client.listSkills();
    }
    
    public CompletableFuture<SkillPackage> getSkill(String skillId) {
        return client.getSkill(skillId);
    }
    
    public CompletableFuture<List<SkillPackage>> searchSkills(String query) {
        return client.searchSkills(query);
    }
    
    public CompletableFuture<List<SkillPackage>> getSkillsByScene(String sceneId) {
        return client.getSkillsByScene(sceneId);
    }
    
    public CompletableFuture<String> getDownloadUrl(String skillId, String version) {
        return client.getDownloadUrl(skillId, version);
    }
    
    public CompletableFuture<Void> registerSkill(SkillPackage skillPackage) {
        return client.registerSkill(skillPackage);
    }
    
    public CompletableFuture<Void> unregisterSkill(String skillId) {
        return client.unregisterSkill(skillId);
    }
    
    public CompletableFuture<List<SkillCenterClient.SceneInfo>> listScenes() {
        return client.listScenes();
    }
    
    public CompletableFuture<SkillCenterClient.SceneInfo> getScene(String sceneId) {
        return client.getScene(sceneId);
    }
    
    public CompletableFuture<SkillCenterClient.SceneJoinResult> joinScene(String sceneId, String agentId) {
        return client.joinScene(sceneId, agentId);
    }
    
    public CompletableFuture<Void> leaveScene(String sceneId, String agentId) {
        return client.leaveScene(sceneId, agentId);
    }
    
    public CompletableFuture<DeploymentResult> deploySkill(String skillId, String targetAgentId) {
        return deploymentService.deploySkill(skillId, targetAgentId);
    }
    
    public CompletableFuture<List<DeploymentResult>> deployToMultipleAgents(
            String skillId, List<String> targetAgentIds) {
        return deploymentService.deployToMultipleAgents(skillId, targetAgentIds);
    }
    
    public RemoteDeploymentService.DeploymentStatus getDeploymentStatus(String deploymentId) {
        return deploymentService.getDeploymentStatus(deploymentId);
    }
    
    public List<RemoteDeploymentService.DeploymentStatus> getDeploymentsBySkill(String skillId) {
        return deploymentService.getDeploymentsBySkill(skillId);
    }
    
    public List<RemoteDeploymentService.DeploymentStatus> getDeploymentsByAgent(String agentId) {
        return deploymentService.getDeploymentsByAgent(agentId);
    }
    
    public CompletableFuture<Void> cancelDeployment(String deploymentId) {
        return deploymentService.cancelDeployment(deploymentId);
    }
    
    public void addSkillToCache(SkillPackage skillPackage) {
        client.addSkillToCache(skillPackage);
    }
    
    public void addSceneToCache(SkillCenterClient.SceneInfo sceneInfo) {
        client.addSceneToCache(sceneInfo);
    }
    
    public void clearCache() {
        client.clearCache();
    }
}
