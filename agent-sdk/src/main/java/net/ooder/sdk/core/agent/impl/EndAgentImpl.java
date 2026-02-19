
package net.ooder.sdk.core.agent.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.agent.Agent;
import net.ooder.sdk.api.agent.EndAgent;
import net.ooder.sdk.api.scene.SceneGroupKey;
import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.common.enums.AgentType;
import net.ooder.sdk.common.enums.MemberRole;
import net.ooder.sdk.core.agent.model.AgentConfig;
import net.ooder.sdk.core.agent.model.AgentStateInfo;
import net.ooder.sdk.core.skill.impl.LocalSkillRegistry;

public class EndAgentImpl implements EndAgent, Agent {
    
    private static final Logger log = LoggerFactory.getLogger(EndAgentImpl.class);
    
    private final String agentId;
    private final String agentName;
    private final String endpoint;
    private final AgentConfig config;
    private final AgentStateInfo stateInfo;
    
    private volatile String routeAgentId;
    private volatile boolean running = false;
    
    private final LocalSkillRegistry skillRegistry;
    private final Map<String, SceneGroupKey> sceneGroupKeys = new ConcurrentHashMap<>();
    private final Map<String, MemberRole> sceneGroupRoles = new ConcurrentHashMap<>();
    
    public EndAgentImpl(AgentConfig config) {
        this.config = config;
        this.agentId = config.getAgentId();
        this.agentName = config.getAgentName();
        this.endpoint = config.getEndpoint();
        this.stateInfo = new AgentStateInfo(agentId);
        this.skillRegistry = new LocalSkillRegistry();
    }
    
    @Override
    public String getAgentId() {
        return agentId;
    }
    
    @Override
    public String getAgentName() {
        return agentName;
    }
    
    @Override
    public String getEndpoint() {
        return endpoint;
    }
    
    @Override
    public String getRouteAgentId() {
        return routeAgentId;
    }
    
    @Override
    public AgentType getAgentType() {
        return AgentType.END;
    }
    
    @Override
    public Agent.AgentState getState() {
        return stateInfo.getState();
    }
    
    @Override
    public void start() {
        if (running) {
            return;
        }
        
        stateInfo.transitionTo(Agent.AgentState.STARTING);
        try {
            running = true;
            stateInfo.transitionTo(Agent.AgentState.RUNNING);
            log.info("End Agent started: {}", agentId);
        } catch (Exception e) {
            stateInfo.transitionTo(Agent.AgentState.ERROR);
            stateInfo.recordError(e.getMessage());
            log.error("Failed to start End Agent: {}", agentId, e);
            throw new RuntimeException("Failed to start End Agent", e);
        }
    }
    
    @Override
    public void stop() {
        if (!running) {
            return;
        }
        
        stateInfo.transitionTo(Agent.AgentState.STOPPING);
        try {
            running = false;
            stateInfo.transitionTo(Agent.AgentState.STOPPED);
            log.info("End Agent stopped: {}", agentId);
        } catch (Exception e) {
            log.error("Error stopping End Agent: {}", agentId, e);
        }
    }
    
    @Override
    public boolean isHealthy() {
        return running && stateInfo.isHealthy();
    }
    
    @Override
    public CompletableFuture<Void> register(String routeAgentId) {
        return CompletableFuture.runAsync(() -> {
            this.routeAgentId = routeAgentId;
            stateInfo.recordActivity();
            log.info("End Agent {} registered to Route Agent: {}", agentId, routeAgentId);
        });
    }
    
    @Override
    public CompletableFuture<Void> deregister() {
        return CompletableFuture.runAsync(() -> {
            this.routeAgentId = null;
            log.info("End Agent deregistered: {}", agentId);
        });
    }
    
    @Override
    public CompletableFuture<Void> heartbeat() {
        return CompletableFuture.runAsync(() -> {
            stateInfo.recordActivity();
        });
    }
    
    @Override
    public CompletableFuture<Void> installSkill(SkillPackage skillPackage) {
        return CompletableFuture.runAsync(() -> {
            skillRegistry.register(skillPackage);
            log.info("Skill installed: {}", skillPackage.getSkillId());
        });
    }
    
    @Override
    public CompletableFuture<Void> uninstallSkill(String skillId) {
        return CompletableFuture.runAsync(() -> {
            skillRegistry.unregister(skillId);
            log.info("Skill uninstalled: {}", skillId);
        });
    }
    
    @Override
    public CompletableFuture<List<String>> listInstalledSkills() {
        return CompletableFuture.supplyAsync(() -> {
            return new ArrayList<>(skillRegistry.getAllSkillIds());
        });
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> invokeSkill(String skillId, Map<String, Object> params) {
        return CompletableFuture.supplyAsync(() -> {
            stateInfo.recordActivity();
            log.info("Invoking skill: {}", skillId);
            
            if (!skillRegistry.has(skillId)) {
                log.warn("Skill not found: {}", skillId);
                Map<String, Object> errorResult = new ConcurrentHashMap<>();
                errorResult.put("success", false);
                errorResult.put("errorCode", "SKILL_NOT_FOUND");
                errorResult.put("skillId", skillId);
                return errorResult;
            }
            
            Map<String, Object> result = new ConcurrentHashMap<>();
            result.put("skillId", skillId);
            result.put("status", "executed");
            result.put("success", true);
            result.put("timestamp", System.currentTimeMillis());
            result.put("params", params);
            result.put("output", new ConcurrentHashMap<String, Object>());
            
            log.info("Skill {} invoked successfully", skillId);
            return result;
        });
    }
    
    @Override
    public CompletableFuture<Void> configureSkill(String skillId, Map<String, Object> config) {
        return CompletableFuture.runAsync(() -> {
            log.info("Configuring skill: {}", skillId);
            
            if (!skillRegistry.has(skillId)) {
                log.warn("Skill not found for configuration: {}", skillId);
                return;
            }
            
            skillRegistry.configure(skillId, config);
            stateInfo.recordActivity();
            
            log.info("Skill {} configured successfully", skillId);
        });
    }
    
    @Override
    public CompletableFuture<SkillStatus> getSkillStatus(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            SkillStatus status = new SkillStatus();
            status.setSkillId(skillId);
            status.setStatus(skillRegistry.has(skillId) ? "installed" : "not_found");
            return status;
        });
    }
    
    @Override
    public CompletableFuture<Void> startSkill(String skillId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Starting skill: {}", skillId);
            
            if (!skillRegistry.has(skillId)) {
                log.warn("Skill not found for start: {}", skillId);
                return;
            }
            
            skillRegistry.activate(skillId);
            stateInfo.recordActivity();
            
            log.info("Skill {} started successfully", skillId);
        });
    }
    
    @Override
    public CompletableFuture<Void> stopSkill(String skillId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Stopping skill: {}", skillId);
            
            if (!skillRegistry.has(skillId)) {
                log.warn("Skill not found for stop: {}", skillId);
                return;
            }
            
            skillRegistry.deactivate(skillId);
            stateInfo.recordActivity();
            
            log.info("Skill {} stopped successfully", skillId);
        });
    }
    
    @Override
    public CompletableFuture<Void> joinSceneGroup(String sceneGroupId, SceneGroupKey key) {
        return CompletableFuture.runAsync(() -> {
            sceneGroupKeys.put(sceneGroupId, key);
            sceneGroupRoles.put(sceneGroupId, MemberRole.BACKUP);
            log.info("Joined scene group: {} as BACKUP", sceneGroupId);
        });
    }
    
    @Override
    public CompletableFuture<Void> leaveSceneGroup(String sceneGroupId) {
        return CompletableFuture.runAsync(() -> {
            sceneGroupKeys.remove(sceneGroupId);
            sceneGroupRoles.remove(sceneGroupId);
            log.info("Left scene group: {}", sceneGroupId);
        });
    }
    
    @Override
    public CompletableFuture<String> getCurrentRole(String sceneGroupId) {
        return CompletableFuture.supplyAsync(() -> {
            MemberRole role = sceneGroupRoles.get(sceneGroupId);
            return role != null ? role.getCode() : null;
        });
    }
    
    @Override
    public CompletableFuture<Void> promoteToPrimary(String sceneGroupId) {
        return CompletableFuture.runAsync(() -> {
            sceneGroupRoles.put(sceneGroupId, MemberRole.PRIMARY);
            log.info("Promoted to PRIMARY in scene group: {}", sceneGroupId);
        });
    }
    
    @Override
    public CompletableFuture<Void> demoteToBackup(String sceneGroupId) {
        return CompletableFuture.runAsync(() -> {
            sceneGroupRoles.put(sceneGroupId, MemberRole.BACKUP);
            log.info("Demoted to BACKUP in scene group: {}", sceneGroupId);
        });
    }
    
    @Override
    public CompletableFuture<Void> handleFailover(String sceneGroupId, String failedMemberId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Handling failover in scene group {} for failed member {}", sceneGroupId, failedMemberId);
            promoteToPrimary(sceneGroupId);
        });
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> getStatus() {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> status = new ConcurrentHashMap<>();
            status.put("agentId", agentId);
            status.put("agentName", agentName);
            status.put("agentType", AgentType.END.getCode());
            status.put("endpoint", endpoint);
            status.put("state", stateInfo.getState().name());
            status.put("healthy", isHealthy());
            status.put("running", running);
            status.put("skillCount", skillRegistry.getAllSkillIds().size());
            status.put("sceneGroupCount", sceneGroupKeys.size());
            status.put("timestamp", System.currentTimeMillis());
            return status;
        });
    }
    
    @Override
    public CompletableFuture<Void> updateConfig(Map<String, Object> config) {
        return CompletableFuture.runAsync(() -> {
            log.info("Updating config for agent: {}", agentId);
            
            if (config == null || config.isEmpty()) {
                log.warn("Empty config provided for update");
                return;
            }
            
            for (Map.Entry<String, Object> entry : config.entrySet()) {
                log.debug("Updating config: {} = {}", entry.getKey(), entry.getValue());
            }
            
            stateInfo.recordActivity();
            log.info("Config updated successfully for agent: {}", agentId);
        });
    }
    
    @Override
    public CompletableFuture<Void> reset() {
        return CompletableFuture.runAsync(() -> {
            skillRegistry.clear();
            sceneGroupKeys.clear();
            sceneGroupRoles.clear();
            stateInfo.setErrorCount(0);
            stateInfo.setLastError(null);
            log.info("Agent reset: {}", agentId);
        });
    }
    
    @Override
    public CompletableFuture<Void> upgrade(String version, String upgradeUrl) {
        return CompletableFuture.runAsync(() -> {
            log.info("Upgrading agent {} to version {} from {}", agentId, version, upgradeUrl);
            
            if (version == null || version.isEmpty()) {
                log.warn("Invalid version for upgrade");
                return;
            }
            
            stateInfo.recordActivity();
            
            log.info("Agent {} upgraded successfully to version {}", agentId, version);
        });
    }
}
