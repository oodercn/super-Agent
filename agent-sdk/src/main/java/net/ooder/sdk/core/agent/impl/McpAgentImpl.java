
package net.ooder.sdk.core.agent.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.agent.Agent;
import net.ooder.sdk.api.agent.McpAgent;
import net.ooder.sdk.api.scene.SceneGroup;
import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.common.enums.AgentType;
import net.ooder.sdk.core.agent.model.AgentConfig;
import net.ooder.sdk.core.agent.model.AgentStateInfo;

public class McpAgentImpl implements McpAgent, Agent {
    
    private static final Logger log = LoggerFactory.getLogger(McpAgentImpl.class);
    
    private final String agentId;
    private final String agentName;
    private final String endpoint;
    private final AgentConfig config;
    private final AgentStateInfo stateInfo;
    
    private final Map<String, RouteAgentInfo> routeAgents = new ConcurrentHashMap<>();
    private final Map<String, EndAgentInfo> endAgents = new ConcurrentHashMap<>();
    private final Map<String, SceneGroup> sceneGroups = new ConcurrentHashMap<>();
    
    private volatile boolean running = false;
    
    public McpAgentImpl(AgentConfig config) {
        this.config = config;
        this.agentId = config.getAgentId();
        this.agentName = config.getAgentName();
        this.endpoint = config.getEndpoint();
        this.stateInfo = new AgentStateInfo(agentId);
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
    public AgentType getAgentType() {
        return AgentType.MCP;
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
            log.info("MCP Agent started: {}", agentId);
        } catch (Exception e) {
            stateInfo.transitionTo(Agent.AgentState.ERROR);
            stateInfo.recordError(e.getMessage());
            log.error("Failed to start MCP Agent: {}", agentId, e);
            throw new RuntimeException("Failed to start MCP Agent", e);
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
            log.info("MCP Agent stopped: {}", agentId);
        } catch (Exception e) {
            log.error("Error stopping MCP Agent: {}", agentId, e);
        }
    }
    
    @Override
    public boolean isHealthy() {
        return running && stateInfo.isHealthy();
    }
    
    @Override
    public CompletableFuture<Void> register() {
        return CompletableFuture.runAsync(() -> {
            log.info("MCP Agent registered: {}", agentId);
            stateInfo.recordActivity();
        });
    }
    
    @Override
    public CompletableFuture<Void> deregister() {
        return CompletableFuture.runAsync(() -> {
            routeAgents.clear();
            endAgents.clear();
            log.info("MCP Agent deregistered: {}", agentId);
        });
    }
    
    @Override
    public CompletableFuture<Void> heartbeat() {
        return CompletableFuture.runAsync(() -> {
            stateInfo.recordActivity();
        });
    }
    
    @Override
    public CompletableFuture<List<RouteAgentInfo>> listRouteAgents() {
        return CompletableFuture.supplyAsync(() -> {
            return new ArrayList<>(routeAgents.values());
        });
    }
    
    @Override
    public CompletableFuture<List<EndAgentInfo>> listEndAgents() {
        return CompletableFuture.supplyAsync(() -> {
            return new ArrayList<>(endAgents.values());
        });
    }
    
    @Override
    public CompletableFuture<RouteAgentInfo> getRouteAgent(String routeAgentId) {
        return CompletableFuture.supplyAsync(() -> {
            return routeAgents.get(routeAgentId);
        });
    }
    
    @Override
    public CompletableFuture<EndAgentInfo> getEndAgent(String endAgentId) {
        return CompletableFuture.supplyAsync(() -> {
            return endAgents.get(endAgentId);
        });
    }
    
    @Override
    public CompletableFuture<Void> registerRouteAgent(RouteAgentInfo routeAgent) {
        return CompletableFuture.runAsync(() -> {
            routeAgent.setRegisterTime(System.currentTimeMillis());
            routeAgent.setLastHeartbeat(System.currentTimeMillis());
            routeAgents.put(routeAgent.getAgentId(), routeAgent);
            log.info("Route Agent registered: {}", routeAgent.getAgentId());
        });
    }
    
    @Override
    public CompletableFuture<Void> deregisterRouteAgent(String routeAgentId) {
        return CompletableFuture.runAsync(() -> {
            routeAgents.remove(routeAgentId);
            log.info("Route Agent deregistered: {}", routeAgentId);
        });
    }
    
    @Override
    public CompletableFuture<Void> updateRouteTable(String routeAgentId, List<RouteEntry> routes) {
        return CompletableFuture.runAsync(() -> {
            RouteAgentInfo routeAgent = routeAgents.get(routeAgentId);
            if (routeAgent != null) {
                routeAgent.setLastHeartbeat(System.currentTimeMillis());
            }
            log.debug("Route table updated for: {}", routeAgentId);
        });
    }
    
    @Override
    public CompletableFuture<List<RouteEntry>> queryRouteTable(String routeAgentId) {
        return CompletableFuture.supplyAsync(() -> {
            List<RouteEntry> routes = new ArrayList<RouteEntry>();
            
            RouteAgentInfo routeAgent = routeAgents.get(routeAgentId);
            if (routeAgent == null) {
                log.warn("Route Agent not found: {}", routeAgentId);
                return routes;
            }
            
            for (EndAgentInfo endAgent : endAgents.values()) {
                RouteEntry entry = new RouteEntry();
                entry.setRouteId("route-" + java.util.UUID.randomUUID().toString().substring(0, 8));
                entry.setDestination(endAgent.getAgentId());
                
                Map<String, Object> metadata = new java.util.HashMap<String, Object>();
                metadata.put("agentName", endAgent.getAgentName());
                metadata.put("endpoint", endAgent.getEndpoint());
                metadata.put("type", "END_AGENT");
                metadata.put("lastUpdated", System.currentTimeMillis());
                entry.setMetadata(metadata);
                
                routes.add(entry);
            }
            
            for (RouteAgentInfo otherRouteAgent : routeAgents.values()) {
                if (!otherRouteAgent.getAgentId().equals(routeAgentId)) {
                    RouteEntry entry = new RouteEntry();
                    entry.setRouteId("route-" + java.util.UUID.randomUUID().toString().substring(0, 8));
                    entry.setDestination(otherRouteAgent.getAgentId());
                    
                    Map<String, Object> metadata = new java.util.HashMap<String, Object>();
                    metadata.put("agentName", otherRouteAgent.getAgentName());
                    metadata.put("endpoint", otherRouteAgent.getEndpoint());
                    metadata.put("type", "ROUTE_AGENT");
                    metadata.put("lastUpdated", System.currentTimeMillis());
                    entry.setMetadata(metadata);
                    
                    routes.add(entry);
                }
            }
            
            log.debug("Query route table for {}: {} routes found", routeAgentId, routes.size());
            return routes;
        });
    }
    
    @Override
    public CompletableFuture<SkillPackage> deploySkill(String skillId, String targetAgentId) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Deploying skill {} to agent {}", skillId, targetAgentId);
            
            EndAgentInfo endAgent = endAgents.get(targetAgentId);
            RouteAgentInfo routeAgent = routeAgents.get(targetAgentId);
            
            if (endAgent == null && routeAgent == null) {
                log.warn("Target agent not found: {}", targetAgentId);
                return null;
            }
            
            SkillPackage skillPackage = new SkillPackage();
            skillPackage.setSkillId(skillId);
            skillPackage.setName("Deployed-" + skillId);
            skillPackage.setVersion("1.0.0");
            
            Map<String, Object> metadata = new java.util.HashMap<String, Object>();
            metadata.put("deployedAt", System.currentTimeMillis());
            metadata.put("targetAgentId", targetAgentId);
            metadata.put("status", "DEPLOYED");
            skillPackage.setMetadata(metadata);
            
            if (endAgent != null) {
                endAgent.setLastHeartbeat(System.currentTimeMillis());
                log.info("Skill {} deployed to EndAgent {}", skillId, targetAgentId);
            } else {
                routeAgent.setLastHeartbeat(System.currentTimeMillis());
                log.info("Skill {} deployed to RouteAgent {}", skillId, targetAgentId);
            }
            
            return skillPackage;
        });
    }
    
    @Override
    public CompletableFuture<Void> invokeSkill(String skillId, Map<String, Object> params) {
        return CompletableFuture.runAsync(() -> {
            log.info("Invoking skill: {}", skillId);
            
            String targetAgentId = null;
            for (EndAgentInfo endAgent : endAgents.values()) {
                if (endAgent.getSkills() != null && endAgent.getSkills().contains(skillId)) {
                    targetAgentId = endAgent.getAgentId();
                    break;
                }
            }
            
            if (targetAgentId == null) {
                for (RouteAgentInfo routeAgent : routeAgents.values()) {
                    if (routeAgent.getSkills() != null && routeAgent.getSkills().contains(skillId)) {
                        targetAgentId = routeAgent.getAgentId();
                        break;
                    }
                }
            }
            
            if (targetAgentId == null) {
                log.warn("No agent found with skill: {}", skillId);
                return;
            }
            
            log.info("Skill {} invoked on agent: {}", skillId, targetAgentId);
            stateInfo.recordActivity();
        });
    }
    
    @Override
    public CompletableFuture<SceneGroup> createSceneGroup(String sceneId, SceneGroupConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            String sceneGroupId = sceneId + "-" + java.util.UUID.randomUUID().toString().substring(0, 8);
            SceneGroup group = new SceneGroup();
            group.setSceneGroupId(sceneGroupId);
            group.setSceneId(sceneId);                                                                                                          
            group.setCreateTime(System.currentTimeMillis());
            group.setMembers(new ArrayList<>());
            sceneGroups.put(sceneGroupId, group);
            log.info("Scene group created: {}", sceneGroupId);
            return group;
        });
    }
    
    @Override
    public CompletableFuture<Void> joinSceneGroup(String sceneGroupId) {
        return CompletableFuture.runAsync(() -> {
            SceneGroup group = sceneGroups.get(sceneGroupId);
            if (group == null) {
                log.warn("Scene group not found: {}", sceneGroupId);
                return;
            }
            
            List<net.ooder.sdk.api.scene.SceneMember> members = group.getMembers();
            if (members == null) {
                members = new ArrayList<>();
                group.setMembers(members);
            }
            
            net.ooder.sdk.api.scene.SceneMember member = new net.ooder.sdk.api.scene.SceneMember();
            member.setAgentId(agentId);
            member.setAgentName(agentName);
            member.setRole(net.ooder.sdk.common.enums.MemberRole.BACKUP);
            member.setJoinTime(System.currentTimeMillis());
            member.setLastHeartbeat(System.currentTimeMillis());
            members.add(member);
            
            group.setLastUpdateTime(System.currentTimeMillis());
            log.info("Joined scene group: {} as member", sceneGroupId);
            stateInfo.recordActivity();
        });
    }
    
    @Override
    public CompletableFuture<Void> leaveSceneGroup(String sceneGroupId) {
        return CompletableFuture.runAsync(() -> {
            SceneGroup group = sceneGroups.get(sceneGroupId);
            if (group == null) {
                log.warn("Scene group not found: {}", sceneGroupId);
                return;
            }
            
            List<net.ooder.sdk.api.scene.SceneMember> members = group.getMembers();
            if (members != null) {
                members.removeIf(m -> agentId.equals(m.getAgentId()));
                group.setLastUpdateTime(System.currentTimeMillis());
            }
            
            log.info("Left scene group: {}", sceneGroupId);
            stateInfo.recordActivity();
        });
    }
    
    @Override
    public CompletableFuture<List<SceneGroup>> listSceneGroups() {
        return CompletableFuture.supplyAsync(() -> {
            return new ArrayList<>(sceneGroups.values());
        });
    }
}
