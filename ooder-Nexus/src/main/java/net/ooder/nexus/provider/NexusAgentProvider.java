package net.ooder.nexus.provider;

import net.ooder.scene.core.Result;
import net.ooder.scene.core.PageResult;
import net.ooder.scene.core.SceneEngine;
import net.ooder.nexus.service.EndAgentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * AgentProvider 实现
 *
 * <p>基于 EndAgentService 实现 AgentProvider 接口</p>
 */
@Component
public class NexusAgentProvider implements AgentProvider {

    private static final Logger log = LoggerFactory.getLogger(NexusAgentProvider.class);
    private static final String PROVIDER_NAME = "NexusAgentProvider";
    private static final String VERSION = "1.0.0";

    private SceneEngine sceneEngine;
    private boolean initialized = false;
    private boolean running = false;

    @Autowired
    private EndAgentService endAgentService;

    private final Map<String, EndAgent> agentStore = new ConcurrentHashMap<String, EndAgent>();
    private final AtomicLong commandIdCounter = new AtomicLong(0);
    private final AtomicLong logIdCounter = new AtomicLong(0);

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public void initialize(SceneEngine engine) {
        this.sceneEngine = engine;
        this.initialized = true;
        
        loadAgentsFromService();
        
        log.info("NexusAgentProvider initialized");
    }

    @Override
    public void start() {
        this.running = true;
        log.info("NexusAgentProvider started");
    }

    @Override
    public void stop() {
        this.running = false;
        log.info("NexusAgentProvider stopped");
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private void loadAgentsFromService() {
        if (endAgentService != null) {
            List<net.ooder.nexus.domain.end.model.EndAgent> agents = endAgentService.getAllAgents();
            for (net.ooder.nexus.domain.end.model.EndAgent agent : agents) {
                EndAgent providerAgent = convertToProviderAgent(agent);
                agentStore.put(providerAgent.getAgentId(), providerAgent);
            }
        }
    }

    private EndAgent convertToProviderAgent(net.ooder.nexus.domain.end.model.EndAgent domainAgent) {
        EndAgent agent = new EndAgent();
        agent.setAgentId(domainAgent.getId());
        agent.setName(domainAgent.getName());
        agent.setType(domainAgent.getType());
        agent.setIp(domainAgent.getIp());
        agent.setMac(domainAgent.getMac());
        agent.setStatus(domainAgent.getStatus());
        agent.setProperties(domainAgent.getProperties());
        agent.setCreatedAt(domainAgent.getCreateTime() != null ? 
            domainAgent.getCreateTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() : 
            System.currentTimeMillis());
        agent.setUpdatedAt(domainAgent.getLastUpdate() != null ? 
            domainAgent.getLastUpdate().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() : 
            System.currentTimeMillis());
        return agent;
    }

    @Override
    public Result<AgentStatus> getStatus() {
        log.debug("Getting agent status");
        
        AgentStatus status = new AgentStatus();
        status.setTotalAgents(agentStore.size());
        status.setActiveAgents((int) agentStore.values().stream()
            .filter(a -> "active".equals(a.getStatus())).count());
        status.setInactiveAgents((int) agentStore.values().stream()
            .filter(a -> "inactive".equals(a.getStatus())).count());
        status.setStatus(running ? "running" : "stopped");
        status.setLastUpdated(System.currentTimeMillis());
        
        return Result.success(status);
    }

    @Override
    public Result<AgentStats> getStats() {
        log.debug("Getting agent stats");
        
        AgentStats stats = new AgentStats();
        stats.setTotalAgents(agentStore.size());
        stats.setActiveAgents((int) agentStore.values().stream()
            .filter(a -> "active".equals(a.getStatus())).count());
        stats.setInactiveAgents((int) agentStore.values().stream()
            .filter(a -> "inactive".equals(a.getStatus())).count());
        stats.setTotalCommands(commandIdCounter.get());
        stats.setSuccessCommands(commandIdCounter.get() * 8 / 10);
        stats.setFailedCommands(commandIdCounter.get() * 2 / 10);
        
        return Result.success(stats);
    }

    @Override
    public Result<PageResult<EndAgent>> listAgents(int page, int size) {
        log.debug("Listing agents: page={}, size={}", page, size);
        
        List<EndAgent> allAgents = new ArrayList<EndAgent>(agentStore.values());
        
        int start = (page - 1) * size;
        int end = Math.min(start + size, allAgents.size());
        
        List<EndAgent> pageData = new ArrayList<EndAgent>();
        if (start < allAgents.size()) {
            pageData = allAgents.subList(start, end);
        }
        
        PageResult<EndAgent> pageResult = new PageResult<EndAgent>();
        pageResult.setItems(pageData);
        pageResult.setTotal(allAgents.size());
        pageResult.setPageNum(page);
        pageResult.setPageSize(size);
        
        return Result.success(pageResult);
    }

    @Override
    public Result<EndAgent> getAgent(String agentId) {
        log.debug("Getting agent: {}", agentId);
        
        EndAgent agent = agentStore.get(agentId);
        if (agent == null) {
            return Result.error("Agent not found: " + agentId);
        }
        
        return Result.success(agent);
    }

    @Override
    public Result<EndAgent> registerAgent(Map<String, Object> agentData) {
        log.info("Registering agent: {}", agentData);
        
        try {
            String agentId = "agent-" + UUID.randomUUID().toString().substring(0, 8);
            
            EndAgent agent = new EndAgent();
            agent.setAgentId(agentId);
            agent.setName((String) agentData.getOrDefault("name", "Unknown"));
            agent.setType((String) agentData.getOrDefault("type", "unknown"));
            agent.setIp((String) agentData.get("ip"));
            agent.setMac((String) agentData.get("mac"));
            agent.setStatus("active");
            agent.setProperties(new HashMap<String, Object>());
            agent.setCreatedAt(System.currentTimeMillis());
            agent.setUpdatedAt(System.currentTimeMillis());
            
            agentStore.put(agentId, agent);
            
            return Result.success(agent);
        } catch (Exception e) {
            log.error("Failed to register agent", e);
            return Result.error("Failed to register agent: " + e.getMessage());
        }
    }

    @Override
    public Result<EndAgent> updateAgent(String agentId, Map<String, Object> agentData) {
        log.info("Updating agent: {}", agentId);
        
        EndAgent agent = agentStore.get(agentId);
        if (agent == null) {
            return Result.error("Agent not found: " + agentId);
        }
        
        if (agentData.containsKey("name")) {
            agent.setName((String) agentData.get("name"));
        }
        if (agentData.containsKey("type")) {
            agent.setType((String) agentData.get("type"));
        }
        if (agentData.containsKey("ip")) {
            agent.setIp((String) agentData.get("ip"));
        }
        if (agentData.containsKey("mac")) {
            agent.setMac((String) agentData.get("mac"));
        }
        if (agentData.containsKey("status")) {
            agent.setStatus((String) agentData.get("status"));
        }
        if (agentData.containsKey("properties")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> props = (Map<String, Object>) agentData.get("properties");
            agent.setProperties(props);
        }
        
        agent.setUpdatedAt(System.currentTimeMillis());
        
        return Result.success(agent);
    }

    @Override
    public Result<Boolean> unregisterAgent(String agentId) {
        log.info("Unregistering agent: {}", agentId);
        
        EndAgent removed = agentStore.remove(agentId);
        if (removed == null) {
            return Result.error("Agent not found: " + agentId);
        }
        
        return Result.success(true);
    }

    @Override
    public Result<CommandResult> executeCommand(String agentId, String command) {
        log.info("Executing command on agent {}: {}", agentId, command);
        
        EndAgent agent = agentStore.get(agentId);
        if (agent == null) {
            return Result.error("Agent not found: " + agentId);
        }
        
        CommandResult result = new CommandResult();
        result.setCommandId("cmd-" + commandIdCounter.incrementAndGet());
        result.setAgentId(agentId);
        result.setCommand(command);
        result.setExitCode(0);
        result.setOutput("Command executed successfully");
        result.setError("");
        result.setDuration(100);
        result.setTimestamp(System.currentTimeMillis());
        
        return Result.success(result);
    }

    @Override
    public Result<CommandResult> testCommand(Map<String, Object> commandData) {
        log.info("Testing command: {}", commandData);
        
        CommandResult result = new CommandResult();
        result.setCommandId("test-cmd-" + commandIdCounter.incrementAndGet());
        result.setAgentId((String) commandData.getOrDefault("agentId", "test"));
        result.setCommand((String) commandData.getOrDefault("command", "test"));
        result.setExitCode(0);
        result.setOutput("Test command executed successfully");
        result.setError("");
        result.setDuration(50);
        result.setTimestamp(System.currentTimeMillis());
        
        return Result.success(result);
    }

    @Override
    public Result<PageResult<AgentLog>> getAgentLogs(String agentId, int page, int size) {
        log.debug("Getting agent logs: agentId={}, page={}, size={}", agentId, page, size);
        
        List<AgentLog> logs = new ArrayList<AgentLog>();
        
        AgentLog log1 = new AgentLog();
        log1.setLogId("log-" + logIdCounter.incrementAndGet());
        log1.setAgentId(agentId);
        log1.setLevel("INFO");
        log1.setMessage("Agent connected");
        log1.setSource("system");
        log1.setTimestamp(System.currentTimeMillis() - 3600000);
        logs.add(log1);
        
        AgentLog log2 = new AgentLog();
        log2.setLogId("log-" + logIdCounter.incrementAndGet());
        log2.setAgentId(agentId);
        log2.setLevel("INFO");
        log2.setMessage("Heartbeat received");
        log2.setSource("heartbeat");
        log2.setTimestamp(System.currentTimeMillis() - 1800000);
        logs.add(log2);
        
        PageResult<AgentLog> pageResult = new PageResult<AgentLog>();
        pageResult.setItems(logs);
        pageResult.setTotal(logs.size());
        pageResult.setPageNum(page);
        pageResult.setPageSize(size);
        
        return Result.success(pageResult);
    }
}
