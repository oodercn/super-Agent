
package net.ooder.sdk.service.scene;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.scene.CapabilityInvoker;
import net.ooder.sdk.api.scene.SceneDefinition;
import net.ooder.sdk.api.scene.SceneGroup;
import net.ooder.sdk.api.scene.SceneGroupManager;
import net.ooder.sdk.api.scene.SceneManager;
import net.ooder.sdk.api.scene.SceneMember;
import net.ooder.sdk.api.skill.Capability;
import net.ooder.sdk.common.enums.MemberRole;

public class SceneService {
    
    private static final Logger log = LoggerFactory.getLogger(SceneService.class);
    
    private final SceneManager sceneManager;
    private final SceneGroupManager sceneGroupManager;
    private final CapabilityInvoker capabilityInvoker;
    
    public SceneService(SceneManager sceneManager, 
                        SceneGroupManager sceneGroupManager,
                        CapabilityInvoker capabilityInvoker) {
        this.sceneManager = sceneManager;
        this.sceneGroupManager = sceneGroupManager;
        this.capabilityInvoker = capabilityInvoker;
    }
    
    public CompletableFuture<SceneDefinition> createScene(SceneDefinition definition) {
        log.info("Creating scene: {}", definition.getSceneId());
        return sceneManager.create(definition);
    }
    
    public CompletableFuture<Void> deleteScene(String sceneId) {
        log.info("Deleting scene: {}", sceneId);
        return sceneManager.delete(sceneId);
    }
    
    public CompletableFuture<SceneDefinition> getScene(String sceneId) {
        return sceneManager.get(sceneId);
    }
    
    public CompletableFuture<List<SceneDefinition>> listAllScenes() {
        return sceneManager.listAll();
    }
    
    public CompletableFuture<Void> activateScene(String sceneId) {
        log.info("Activating scene: {}", sceneId);
        return sceneManager.activate(sceneId);
    }
    
    public CompletableFuture<Void> deactivateScene(String sceneId) {
        log.info("Deactivating scene: {}", sceneId);
        return sceneManager.deactivate(sceneId);
    }
    
    public CompletableFuture<Void> addCapability(String sceneId, Capability capability) {
        log.info("Adding capability {} to scene {}", capability.getCapId(), sceneId);
        return sceneManager.addCapability(sceneId, capability);
    }
    
    public CompletableFuture<List<Capability>> listCapabilities(String sceneId) {
        return sceneManager.listCapabilities(sceneId);
    }
    
    public CompletableFuture<SceneGroup> createSceneGroup(String sceneId, int minMembers, int maxMembers) {
        log.info("Creating scene group for scene: {}", sceneId);
        SceneGroupManager.SceneGroupConfig config = new SceneGroupManager.SceneGroupConfig();
        config.setSceneId(sceneId);
        config.setMinMembers(minMembers);
        config.setMaxMembers(maxMembers);
        return sceneGroupManager.create(sceneId, config);
    }
    
    public CompletableFuture<Void> joinSceneGroup(String sceneGroupId, String agentId, MemberRole role) {
        log.info("Agent {} joining scene group {} as {}", agentId, sceneGroupId, role);
        return sceneGroupManager.join(sceneGroupId, agentId, role);
    }
    
    public CompletableFuture<Void> leaveSceneGroup(String sceneGroupId, String agentId) {
        log.info("Agent {} leaving scene group {}", agentId, sceneGroupId);
        return sceneGroupManager.leave(sceneGroupId, agentId);
    }
    
    public CompletableFuture<Void> changeRole(String sceneGroupId, String agentId, MemberRole newRole) {
        log.info("Changing role of agent {} to {} in scene group {}", agentId, newRole, sceneGroupId);
        return sceneGroupManager.changeRole(sceneGroupId, agentId, newRole);
    }
    
    public CompletableFuture<MemberRole> getRole(String sceneGroupId, String agentId) {
        return sceneGroupManager.getRole(sceneGroupId, agentId);
    }
    
    public CompletableFuture<List<SceneMember>> listMembers(String sceneGroupId) {
        return sceneGroupManager.listMembers(sceneGroupId);
    }
    
    public CompletableFuture<Void> handleFailover(String sceneGroupId, String failedMemberId) {
        log.warn("Handling failover for {} in scene group {}", failedMemberId, sceneGroupId);
        return sceneGroupManager.handleFailover(sceneGroupId, failedMemberId);
    }
    
    public CompletableFuture<Object> invokeCapability(String sceneId, String capId, Map<String, Object> params) {
        log.info("Invoking capability {} in scene {}", capId, sceneId);
        return capabilityInvoker.invoke(sceneId, capId, params);
    }
    
    public CompletableFuture<Object> invokeCapability(String capId, Map<String, Object> params) {
        log.info("Invoking capability {}", capId);
        return capabilityInvoker.invoke(capId, params);
    }
    
    public CompletableFuture<net.ooder.sdk.api.scene.SceneManager.SceneState> getSceneState(String sceneId) {
        return sceneManager.getState(sceneId);
    }
}
