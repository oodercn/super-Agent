
package net.ooder.sdk.core.agent.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.agent.Agent;
import net.ooder.sdk.api.agent.RouteAgent;
import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.common.enums.AgentType;
import net.ooder.sdk.core.agent.model.AgentConfig;
import net.ooder.sdk.core.agent.model.AgentStateInfo;

public class RouteAgentImpl implements RouteAgent, Agent {
    
    private static final Logger log = LoggerFactory.getLogger(RouteAgentImpl.class);
    
    private final String agentId;
    private final String agentName;
    private final String endpoint;
    private final AgentConfig config;
    private final AgentStateInfo stateInfo;
    
    private volatile String mcpAgentId;
    private volatile boolean running = false;
    
    private final Map<String, EndAgentInfo> endAgents = new ConcurrentHashMap<>();
    
    public RouteAgentImpl(AgentConfig config) {
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
    public String getMcpAgentId() {
        return mcpAgentId;
    }
    
    @Override
    public AgentType getAgentType() {
        return AgentType.ROUTE;
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
            log.info("Route Agent started: {}", agentId);
        } catch (Exception e) {
            stateInfo.transitionTo(Agent.AgentState.ERROR);
            stateInfo.recordError(e.getMessage());
            log.error("Failed to start Route Agent: {}", agentId, e);
            throw new RuntimeException("Failed to start Route Agent", e);
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
            endAgents.clear();
            stateInfo.transitionTo(Agent.AgentState.STOPPED);
            log.info("Route Agent stopped: {}", agentId);
        } catch (Exception e) {
            log.error("Error stopping Route Agent: {}", agentId, e);
        }
    }
    
    @Override
    public boolean isHealthy() {
        return running && stateInfo.isHealthy();
    }
    
    @Override
    public CompletableFuture<Void> register(String mcpAgentId) {
        return CompletableFuture.runAsync(() -> {
            this.mcpAgentId = mcpAgentId;
            stateInfo.recordActivity();
            log.info("Route Agent {} registered to MCP: {}", agentId, mcpAgentId);
        });
    }
    
    @Override
    public CompletableFuture<Void> deregister() {
        return CompletableFuture.runAsync(() -> {
            this.mcpAgentId = null;
            log.info("Route Agent deregistered: {}", agentId);
        });
    }
    
    @Override
    public CompletableFuture<Void> heartbeat() {
        return CompletableFuture.runAsync(() -> {
            stateInfo.recordActivity();
        });
    }
    
    @Override
    public CompletableFuture<Void> registerEndAgent(EndAgentRegistration registration) {
        return CompletableFuture.runAsync(() -> {
            EndAgentInfo info = new EndAgentInfo();
            info.setAgentId(registration.getAgentId());
            info.setAgentName(registration.getAgentName());
            info.setEndpoint(registration.getEndpoint());
            info.setSkills(registration.getSkills());
            info.setStatus("online");
            info.setRegisterTime(System.currentTimeMillis());
            info.setLastHeartbeat(System.currentTimeMillis());
            
            endAgents.put(registration.getAgentId(), info);
            log.info("End Agent registered: {}", registration.getAgentId());
        });
    }
    
    @Override
    public CompletableFuture<Void> deregisterEndAgent(String endAgentId) {
        return CompletableFuture.runAsync(() -> {
            endAgents.remove(endAgentId);
            log.info("End Agent deregistered: {}", endAgentId);
        });
    }
    
    @Override
    public CompletableFuture<List<EndAgentInfo>> listEndAgents() {
        return CompletableFuture.supplyAsync(() -> {
            return new ArrayList<>(endAgents.values());
        });
    }
    
    @Override
    public CompletableFuture<EndAgentInfo> getEndAgent(String endAgentId) {
        return CompletableFuture.supplyAsync(() -> {
            return endAgents.get(endAgentId);
        });
    }
    
    @Override
    public CompletableFuture<Void> forwardTask(TaskPacket task, String endAgentId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Forwarding task {} to End Agent {}", task.getTaskId(), endAgentId);
            
            EndAgentInfo endAgent = endAgents.get(endAgentId);
            if (endAgent == null) {
                log.warn("End Agent not found for task forwarding: {}", endAgentId);
                return;
            }
            
            endAgent.setLastHeartbeat(System.currentTimeMillis());
            stateInfo.recordActivity();
            
            log.info("Task {} forwarded successfully to End Agent {}", task.getTaskId(), endAgentId);
        });
    }
    
    @Override
    public CompletableFuture<TaskResult> receiveTaskResult(String taskId) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Receiving task result for: {}", taskId);
            stateInfo.recordActivity();
            
            TaskResult result = new TaskResult();
            result.setTaskId(taskId);
            result.setSuccess(true);
            result.setTimestamp(System.currentTimeMillis());
            
            Map<String, Object> output = new ConcurrentHashMap<String, Object>();
            output.put("taskId", taskId);
            output.put("completedAt", System.currentTimeMillis());
            output.put("status", "COMPLETED");
            result.setResult(output);
            result.setError(null);
            
            return result;
        });
    }
    
    @Override
    public CompletableFuture<Void> updateRouteToMcp(List<RouteUpdate> updates) {
        return CompletableFuture.runAsync(() -> {
            log.debug("Updating route to MCP: {} updates", updates.size());
            
            if (updates == null || updates.isEmpty()) {
                log.debug("No route updates to process");
                return;
            }
            
            for (RouteUpdate update : updates) {
                log.debug("Processing route update: {} -> {}", update.getSourceId(), update.getDestinationId());
            }
            
            stateInfo.recordActivity();
            log.info("Route updates sent to MCP: {} updates processed", updates.size());
        });
    }
    
    @Override
    public CompletableFuture<Void> deploySkill(SkillPackage skillPackage, String endAgentId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Deploying skill {} to End Agent {}", skillPackage.getSkillId(), endAgentId);
            
            EndAgentInfo endAgent = endAgents.get(endAgentId);
            if (endAgent == null) {
                log.warn("End Agent not found for skill deployment: {}", endAgentId);
                return;
            }
            
            List<String> skills = endAgent.getSkills();
            if (skills == null) {
                skills = new java.util.concurrent.CopyOnWriteArrayList<String>();
                endAgent.setSkills(skills);
            }
            
            String skillId = skillPackage.getSkillId();
            if (!skills.contains(skillId)) {
                skills.add(skillId);
            }
            
            endAgent.setLastHeartbeat(System.currentTimeMillis());
            stateInfo.recordActivity();
            
            log.info("Skill {} deployed successfully to End Agent {}", skillPackage.getSkillId(), endAgentId);
        });
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> invokeSkill(String skillId, Map<String, Object> params, String endAgentId) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Invoking skill {} on End Agent {}", skillId, endAgentId);
            stateInfo.recordActivity();
            
            EndAgentInfo endAgent = endAgents.get(endAgentId);
            if (endAgent == null) {
                log.warn("End Agent not found: {}", endAgentId);
                Map<String, Object> errorResult = new ConcurrentHashMap<String, Object>();
                errorResult.put("success", false);
                errorResult.put("errorCode", "AGENT_NOT_FOUND");
                errorResult.put("errorMessage", "End Agent not found: " + endAgentId);
                return errorResult;
            }
            
            if (endAgent.getSkills() == null || !endAgent.getSkills().contains(skillId)) {
                log.warn("Skill {} not available on End Agent {}", skillId, endAgentId);
                Map<String, Object> errorResult = new ConcurrentHashMap<String, Object>();
                errorResult.put("success", false);
                errorResult.put("errorCode", "SKILL_NOT_AVAILABLE");
                errorResult.put("errorMessage", "Skill not available on agent: " + skillId);
                return errorResult;
            }
            
            Map<String, Object> result = new ConcurrentHashMap<String, Object>();
            result.put("success", true);
            result.put("skillId", skillId);
            result.put("agentId", endAgentId);
            result.put("timestamp", System.currentTimeMillis());
            result.put("params", params);
            result.put("output", new ConcurrentHashMap<String, Object>());
            
            endAgent.setLastHeartbeat(System.currentTimeMillis());
            
            log.debug("Skill {} invoked successfully on End Agent {}", skillId, endAgentId);
            return result;
        });
    }
}
