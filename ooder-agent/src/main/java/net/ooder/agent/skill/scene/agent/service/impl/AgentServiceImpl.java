package net.ooder.mvp.skill.scene.agent.service.impl;

import net.ooder.mvp.skill.scene.agent.dto.AgentAlertConfigDTO;
import net.ooder.mvp.skill.scene.agent.dto.AgentBatchOperationDTO;
import net.ooder.mvp.skill.scene.agent.dto.AgentDTO;
import net.ooder.mvp.skill.scene.agent.dto.AgentMetricsDTO;
import net.ooder.mvp.skill.scene.agent.dto.AgentTopologyDTO;
import net.ooder.mvp.skill.scene.agent.dto.AgentTopologyDTO.AgentEdge;
import net.ooder.mvp.skill.scene.agent.dto.AgentTopologyDTO.AgentNode;
import net.ooder.mvp.skill.scene.agent.service.AgentService;
import net.ooder.mvp.skill.scene.capability.service.CapabilityBindingService;
import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.scene.ParticipantType;
import net.ooder.mvp.skill.scene.dto.scene.SceneGroupDTO;
import net.ooder.mvp.skill.scene.dto.scene.SceneParticipantDTO;
import net.ooder.mvp.skill.scene.service.SceneGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class AgentServiceImpl implements AgentService {

    private static final Logger log = LoggerFactory.getLogger(AgentServiceImpl.class);

    @Autowired
    private SceneGroupService sceneGroupService;

    @Autowired
    private CapabilityBindingService bindingService;

    private final Map<String, AgentDTO> agentCache = new ConcurrentHashMap<>();
    private final Map<String, Long> heartbeatCache = new ConcurrentHashMap<>();
    private final Map<String, AgentMetricsDTO> metricsCache = new ConcurrentHashMap<>();
    private final Map<Long, AgentAlertConfigDTO> alertConfigs = new ConcurrentHashMap<>();
    private final Map<String, AgentBatchOperationDTO> batchOperations = new ConcurrentHashMap<>();
    private Long alertIdCounter = 1L;

    @Override
    public PageResult<AgentDTO> listAgents(int pageNum, int pageSize) {
        List<AgentDTO> allAgents = collectAllAgents();
        
        int total = allAgents.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        
        List<AgentDTO> pagedList = start < total ? allAgents.subList(start, end) : new ArrayList<>();
        
        PageResult<AgentDTO> result = new PageResult<>();
        result.setList(pagedList);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    public AgentDTO getAgent(String agentId) {
        AgentDTO cached = agentCache.get(agentId);
        if (cached != null) {
            return cached;
        }
        
        List<AgentDTO> allAgents = collectAllAgents();
        for (AgentDTO agent : allAgents) {
            if (agentId.equals(agent.getAgentId())) {
                return agent;
            }
        }
        return null;
    }

    @Override
    public List<AgentDTO> searchAgents(String keyword) {
        List<AgentDTO> allAgents = collectAllAgents();
        if (keyword == null || keyword.trim().isEmpty()) {
            return allAgents;
        }
        
        String lowerKeyword = keyword.toLowerCase();
        List<AgentDTO> result = new ArrayList<>();
        for (AgentDTO agent : allAgents) {
            if ((agent.getAgentId() != null && agent.getAgentId().toLowerCase().contains(lowerKeyword)) ||
                (agent.getAgentName() != null && agent.getAgentName().toLowerCase().contains(lowerKeyword)) ||
                (agent.getIpAddress() != null && agent.getIpAddress().toLowerCase().contains(lowerKeyword)) ||
                (agent.getClusterId() != null && agent.getClusterId().toLowerCase().contains(lowerKeyword))) {
                result.add(agent);
            }
        }
        return result;
    }

    @Override
    public List<AgentDTO> listByType(String agentType) {
        List<AgentDTO> allAgents = collectAllAgents();
        List<AgentDTO> result = new ArrayList<>();
        for (AgentDTO agent : allAgents) {
            if (agentType.equals(agent.getAgentType())) {
                result.add(agent);
            }
        }
        return result;
    }

    @Override
    public List<AgentDTO> listByStatus(String status) {
        List<AgentDTO> allAgents = collectAllAgents();
        List<AgentDTO> result = new ArrayList<>();
        for (AgentDTO agent : allAgents) {
            if (status.equalsIgnoreCase(agent.getStatus())) {
                result.add(agent);
            }
        }
        return result;
    }

    @Override
    public List<AgentDTO> listByCluster(String clusterId) {
        List<AgentDTO> allAgents = collectAllAgents();
        List<AgentDTO> result = new ArrayList<>();
        for (AgentDTO agent : allAgents) {
            if (clusterId.equals(agent.getClusterId())) {
                result.add(agent);
            }
        }
        return result;
    }

    @Override
    public boolean sendHeartbeat(String agentId) {
        heartbeatCache.put(agentId, System.currentTimeMillis());
        
        AgentDTO agent = getAgent(agentId);
        if (agent != null) {
            agent.setLastHeartbeat(System.currentTimeMillis());
            agent.setStatus("online");
            agent.setHealthStatus("healthy");
            agentCache.put(agentId, agent);
            log.info("Agent heartbeat received: {}", agentId);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAgentStatus(String agentId, String status) {
        AgentDTO agent = getAgent(agentId);
        if (agent != null) {
            agent.setStatus(status);
            agentCache.put(agentId, agent);
            return true;
        }
        return false;
    }

    @Override
    public int getBindingCount(String agentId) {
        return bindingService.listByAgent(agentId).size();
    }

    @Override
    public AgentDTO registerAgent(AgentDTO agent) {
        if (agent.getAgentId() == null || agent.getAgentId().isEmpty()) {
            agent.setAgentId("agent-" + UUID.randomUUID().toString().substring(0, 8));
        }
        agent.setRegisterTime(System.currentTimeMillis());
        agent.setLastHeartbeat(System.currentTimeMillis());
        agent.setStatus("online");
        agent.setHealthStatus("healthy");
        agentCache.put(agent.getAgentId(), agent);
        log.info("Agent registered: {}", agent.getAgentId());
        return agent;
    }

    @Override
    public boolean unregisterAgent(String agentId) {
        AgentDTO removed = agentCache.remove(agentId);
        heartbeatCache.remove(agentId);
        metricsCache.remove(agentId);
        if (removed != null) {
            log.info("Agent unregistered: {}", agentId);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAgentConfig(String agentId, Map<String, Object> config) {
        AgentDTO agent = getAgent(agentId);
        if (agent != null) {
            if (agent.getExtendedConfig() == null) {
                agent.setExtendedConfig(new HashMap<>());
            }
            agent.getExtendedConfig().putAll(config);
            agentCache.put(agentId, agent);
            return true;
        }
        return false;
    }

    @Override
    public AgentTopologyDTO getTopology() {
        return buildTopology(collectAllAgents());
    }

    @Override
    public AgentTopologyDTO getTopologyByCluster(String clusterId) {
        List<AgentDTO> agents = listByCluster(clusterId);
        return buildTopology(agents);
    }

    private AgentTopologyDTO buildTopology(List<AgentDTO> agents) {
        AgentTopologyDTO topology = new AgentTopologyDTO();
        topology.setTimestamp(System.currentTimeMillis());
        
        List<AgentNode> nodes = new ArrayList<>();
        List<AgentEdge> edges = new ArrayList<>();
        
        if (agents == null || agents.isEmpty()) {
            topology.setNodes(nodes);
            topology.setEdges(edges);
            return topology;
        }
        
        Map<String, List<AgentDTO>> clusterGroups = agents.stream()
            .filter(a -> a != null && a.getClusterId() != null)
            .collect(Collectors.groupingBy(AgentDTO::getClusterId));
        
        int clusterIndex = 0;
        for (Map.Entry<String, List<AgentDTO>> entry : clusterGroups.entrySet()) {
            String clusterId = entry.getKey();
            List<AgentDTO> clusterAgents = entry.getValue();
            
            int baseX = (clusterIndex % 3) * 400 + 100;
            int baseY = (clusterIndex / 3) * 300 + 100;
            
            for (int i = 0; i < clusterAgents.size(); i++) {
                AgentDTO agent = clusterAgents.get(i);
                AgentNode node = new AgentNode();
                node.setId(agent.getAgentId());
                node.setName(agent.getAgentName());
                node.setType(agent.getAgentType());
                node.setStatus(agent.getStatus());
                node.setClusterId(clusterId);
                node.setX(baseX + (i % 4) * 100);
                node.setY(baseY + (i / 4) * 80);
                node.setIcon(getIconForType(agent.getAgentType()));
                node.setColor(getColorForStatus(agent.getStatus()));
                
                Map<String, Object> data = new HashMap<>();
                data.put("ipAddress", agent.getIpAddress());
                data.put("port", agent.getPort());
                data.put("version", agent.getVersion());
                data.put("load", agent.getLoadPercentage());
                data.put("health", agent.getHealthStatus());
                node.setData(data);
                
                nodes.add(node);
            }
            
            clusterIndex++;
        }
        
        List<AgentDTO> noCluster = agents.stream()
            .filter(a -> a.getClusterId() == null)
            .collect(Collectors.toList());
        
        int baseX = 900;
        int baseY = 100;
        for (int i = 0; i < noCluster.size(); i++) {
            AgentDTO agent = noCluster.get(i);
            AgentNode node = new AgentNode();
            node.setId(agent.getAgentId());
            node.setName(agent.getAgentName());
            node.setType(agent.getAgentType());
            node.setStatus(agent.getStatus());
            node.setX(baseX + (i % 3) * 100);
            node.setY(baseY + (i / 3) * 80);
            node.setIcon(getIconForType(agent.getAgentType()));
            node.setColor(getColorForStatus(agent.getStatus()));
            
            Map<String, Object> data = new HashMap<>();
            data.put("ipAddress", agent.getIpAddress());
            data.put("port", agent.getPort());
            data.put("load", agent.getLoadPercentage());
            node.setData(data);
            
            nodes.add(node);
        }
        
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                AgentNode n1 = nodes.get(i);
                AgentNode n2 = nodes.get(j);
                
                if (n1.getClusterId() != null && n1.getClusterId().equals(n2.getClusterId())) {
                    if ("SUPER_AGENT".equals(n1.getType()) || "LLM".equals(n1.getType())) {
                        AgentEdge edge = new AgentEdge();
                        edge.setId("edge-" + n1.getId() + "-" + n2.getId());
                        edge.setSource(n1.getId());
                        edge.setTarget(n2.getId());
                        edge.setType("manage");
                        edge.setAnimated("online".equals(n1.getStatus()));
                        edges.add(edge);
                    }
                }
            }
        }
        
        topology.setNodes(nodes);
        topology.setEdges(edges);
        
        Map<String, Object> layout = new HashMap<>();
        layout.put("type", "force");
        layout.put("width", 1200);
        layout.put("height", 800);
        topology.setLayout(layout);
        
        return topology;
    }

    private String getIconForType(String type) {
        if ("LLM".equals(type)) return "ri-robot-2-line";
        if ("WORKER".equals(type)) return "ri-cpu-line";
        if ("DEVICE".equals(type)) return "ri-device-line";
        if ("PLATFORM".equals(type)) return "ri-server-line";
        if ("SUPER_AGENT".equals(type)) return "ri-star-line";
        return "ri-robot-line";
    }

    private String getColorForStatus(String status) {
        if ("online".equals(status)) return "#4caf50";
        if ("busy".equals(status)) return "#ff9800";
        if ("offline".equals(status)) return "#f44336";
        return "#9e9e9e";
    }

    @Override
    public AgentMetricsDTO getAgentMetrics(String agentId) {
        AgentMetricsDTO metrics = metricsCache.get(agentId);
        if (metrics != null) {
            return metrics;
        }
        
        AgentDTO agent = getAgent(agentId);
        if (agent == null) {
            return null;
        }
        
        metrics = generateMockMetrics(agent);
        metricsCache.put(agentId, metrics);
        return metrics;
    }

    @Override
    public List<AgentMetricsDTO> getAllMetrics() {
        List<AgentMetricsDTO> result = new ArrayList<>();
        for (AgentDTO agent : collectAllAgents()) {
            result.add(getAgentMetrics(agent.getAgentId()));
        }
        return result;
    }

    private AgentMetricsDTO generateMockMetrics(AgentDTO agent) {
        AgentMetricsDTO metrics = new AgentMetricsDTO();
        metrics.setAgentId(agent.getAgentId());
        metrics.setAgentName(agent.getAgentName());
        metrics.setTimestamp(System.currentTimeMillis());
        
        ThreadLocalRandom random = ThreadLocalRandom.current();
        
        metrics.setCpuUsage(random.nextDouble(10, 80));
        metrics.setMemoryUsage(random.nextDouble(20, 70));
        metrics.setCurrentLoad(agent.getCurrentLoad() > 0 ? agent.getCurrentLoad() : random.nextInt(1, 10));
        metrics.setMaxConcurrency(agent.getMaxConcurrency() > 0 ? agent.getMaxConcurrency() : 20);
        metrics.setLoadPercentage(metrics.getCurrentLoad() * 100.0 / metrics.getMaxConcurrency());
        metrics.setTotalRequests(agent.getTotalRequests() > 0 ? agent.getTotalRequests() : random.nextLong(1000, 10000));
        metrics.setSuccessRequests((long)(metrics.getTotalRequests() * random.nextDouble(0.95, 0.99)));
        metrics.setFailedRequests(metrics.getTotalRequests() - metrics.getSuccessRequests());
        metrics.setSuccessRate(metrics.getSuccessRequests() * 100.0 / metrics.getTotalRequests());
        metrics.setAvgResponseTime(random.nextDouble(50, 500));
        metrics.setQueueSize(random.nextLong(0, 20));
        metrics.setHealthStatus(agent.getHealthStatus() != null ? agent.getHealthStatus() : "healthy");
        
        List<AgentMetricsDTO.MetricPoint> responseTimeHistory = new ArrayList<>();
        long now = System.currentTimeMillis();
        for (int i = 0; i < 20; i++) {
            responseTimeHistory.add(new AgentMetricsDTO.MetricPoint(
                now - (20 - i) * 60000,
                random.nextDouble(100, 400)
            ));
        }
        metrics.setResponseTimeHistory(responseTimeHistory);
        
        List<AgentMetricsDTO.MetricPoint> requestRateHistory = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            requestRateHistory.add(new AgentMetricsDTO.MetricPoint(
                now - (20 - i) * 60000,
                random.nextDouble(10, 100)
            ));
        }
        metrics.setRequestRateHistory(requestRateHistory);
        
        return metrics;
    }

    @Override
    public Map<String, Object> getClusterStats(String clusterId) {
        List<AgentDTO> agents = listByCluster(clusterId);
        return calculateStats(agents);
    }

    @Override
    public Map<String, Object> getOverallStats() {
        List<AgentDTO> agents = collectAllAgents();
        return calculateStats(agents);
    }

    private Map<String, Object> calculateStats(List<AgentDTO> agents) {
        Map<String, Object> stats = new HashMap<>();
        
        if (agents == null || agents.isEmpty()) {
            stats.put("total", 0);
            stats.put("online", 0);
            stats.put("busy", 0);
            stats.put("offline", 0);
            stats.put("healthy", 0);
            stats.put("unhealthy", 0);
            stats.put("avgCpuUsage", 0.0);
            stats.put("avgMemoryUsage", 0.0);
            stats.put("totalRequests", 0L);
            stats.put("totalSuccess", 0L);
            stats.put("successRate", 100.0);
            return stats;
        }
        
        int total = agents.size();
        int online = 0, busy = 0, offline = 0;
        int healthy = 0;
        double totalCpu = 0, totalMemory = 0;
        long totalRequests = 0, totalSuccess = 0;
        
        for (AgentDTO agent : agents) {
            if (agent == null) continue;
            
            String status = agent.getStatus();
            if ("online".equalsIgnoreCase(status)) online++;
            else if ("busy".equalsIgnoreCase(status)) busy++;
            else offline++;
            
            if (agent.isHealthy()) healthy++;
            totalCpu += agent.getCpuUsage() != null ? agent.getCpuUsage() : 0.0;
            totalMemory += agent.getMemoryUsage() != null ? agent.getMemoryUsage() : 0.0;
            totalRequests += agent.getTotalRequests() != null ? agent.getTotalRequests() : 0L;
            totalSuccess += agent.getSuccessRequests() != null ? agent.getSuccessRequests() : 0L;
        }
        
        stats.put("total", total);
        stats.put("online", online);
        stats.put("busy", busy);
        stats.put("offline", offline);
        stats.put("healthy", healthy);
        stats.put("unhealthy", total - healthy);
        stats.put("avgCpuUsage", total > 0 ? totalCpu / total : 0);
        stats.put("avgMemoryUsage", total > 0 ? totalMemory / total : 0);
        stats.put("totalRequests", totalRequests);
        stats.put("totalSuccess", totalSuccess);
        stats.put("successRate", totalRequests > 0 ? totalSuccess * 100.0 / totalRequests : 100);
        
        return stats;
    }

    @Override
    public AgentBatchOperationDTO executeBatchOperation(AgentBatchOperationDTO operation) {
        String operationId = "batch-" + UUID.randomUUID().toString().substring(0, 8);
        operation.setOperationId(operationId);
        operation.setCreatedAt(System.currentTimeMillis());
        operation.setStatus("processing");
        operation.setTotalCount(operation.getAgentIds().size());
        operation.setSuccessCount(0);
        operation.setFailedCount(0);
        
        List<AgentBatchOperationDTO.OperationResult> results = new ArrayList<>();
        
        for (String agentId : operation.getAgentIds()) {
            AgentBatchOperationDTO.OperationResult result = new AgentBatchOperationDTO.OperationResult();
            result.setAgentId(agentId);
            
            try {
                boolean success = executeSingleOperation(agentId, operation.getOperationType(), operation.getParameters());
                result.setSuccess(success);
                result.setMessage(success ? "Operation completed" : "Operation failed");
                
                if (success) {
                    operation.setSuccessCount(operation.getSuccessCount() + 1);
                } else {
                    operation.setFailedCount(operation.getFailedCount() + 1);
                }
            } catch (Exception e) {
                result.setSuccess(false);
                result.setMessage(e.getMessage());
                operation.setFailedCount(operation.getFailedCount() + 1);
            }
            
            results.add(result);
        }
        
        operation.setResults(results);
        operation.setStatus("completed");
        batchOperations.put(operationId, operation);
        
        return operation;
    }

    private boolean executeSingleOperation(String agentId, String operationType, Map<String, Object> params) {
        AgentDTO agent = getAgent(agentId);
        if (agent == null) return false;
        
        switch (operationType) {
            case AgentBatchOperationDTO.OP_ENABLE:
                agent.setStatus("online");
                agent.setEnabled(true);
                break;
            case AgentBatchOperationDTO.OP_DISABLE:
                agent.setStatus("offline");
                break;
            case AgentBatchOperationDTO.OP_RESTART:
                agent.setStatus("online");
                agent.setHealthStatus("healthy");
                break;
            case AgentBatchOperationDTO.OP_UPDATE_CONFIG:
                if (params != null) {
                    updateAgentConfig(agentId, params);
                }
                break;
            case AgentBatchOperationDTO.OP_CLEAR_CACHE:
                metricsCache.remove(agentId);
                break;
            case AgentBatchOperationDTO.OP_HEALTH_CHECK:
                healthCheck(agentId);
                break;
            default:
                return false;
        }
        
        agentCache.put(agentId, agent);
        return true;
    }

    @Override
    public AgentBatchOperationDTO getBatchOperationStatus(String operationId) {
        return batchOperations.get(operationId);
    }

    @Override
    public List<AgentAlertConfigDTO> listAlertConfigs() {
        return new ArrayList<>(alertConfigs.values());
    }

    @Override
    public List<AgentAlertConfigDTO> getAlertConfigsByAgent(String agentId) {
        return alertConfigs.values().stream()
            .filter(c -> agentId.equals(c.getAgentId()))
            .collect(Collectors.toList());
    }

    @Override
    public AgentAlertConfigDTO createAlertConfig(AgentAlertConfigDTO config) {
        config.setId(alertIdCounter++);
        config.setCreatedAt(System.currentTimeMillis());
        config.setUpdatedAt(System.currentTimeMillis());
        alertConfigs.put(config.getId(), config);
        return config;
    }

    @Override
    public AgentAlertConfigDTO updateAlertConfig(Long id, AgentAlertConfigDTO config) {
        AgentAlertConfigDTO existing = alertConfigs.get(id);
        if (existing == null) return null;
        
        config.setId(id);
        config.setCreatedAt(existing.getCreatedAt());
        config.setUpdatedAt(System.currentTimeMillis());
        alertConfigs.put(id, config);
        return config;
    }

    @Override
    public boolean deleteAlertConfig(Long id) {
        return alertConfigs.remove(id) != null;
    }

    @Override
    public boolean enableAlertConfig(Long id) {
        AgentAlertConfigDTO config = alertConfigs.get(id);
        if (config == null) return false;
        config.setEnabled(true);
        config.setUpdatedAt(System.currentTimeMillis());
        return true;
    }

    @Override
    public boolean disableAlertConfig(Long id) {
        AgentAlertConfigDTO config = alertConfigs.get(id);
        if (config == null) return false;
        config.setEnabled(false);
        config.setUpdatedAt(System.currentTimeMillis());
        return true;
    }

    @Override
    public List<AgentDTO> listByCapability(String capability) {
        List<AgentDTO> allAgents = collectAllAgents();
        List<AgentDTO> result = new ArrayList<>();
        for (AgentDTO agent : allAgents) {
            if (agent.getCapabilities() != null && agent.getCapabilities().contains(capability)) {
                result.add(agent);
            }
        }
        return result;
    }

    @Override
    public List<AgentDTO> listByTag(String tagKey, String tagValue) {
        List<AgentDTO> allAgents = collectAllAgents();
        List<AgentDTO> result = new ArrayList<>();
        for (AgentDTO agent : allAgents) {
            if (agent.getTags() != null && tagValue.equals(agent.getTags().get(tagKey))) {
                result.add(agent);
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> healthCheck(String agentId) {
        Map<String, Object> result = new HashMap<>();
        AgentDTO agent = getAgent(agentId);
        
        if (agent == null) {
            result.put("status", "not_found");
            result.put("healthy", false);
            return result;
        }
        
        long heartbeatAge = System.currentTimeMillis() - agent.getLastHeartbeat();
        boolean heartbeatOk = heartbeatAge < 60000;
        boolean loadOk = agent.getLoadPercentage() < 90;
        boolean errorRateOk = agent.getSuccessRate() > 95;
        
        boolean healthy = heartbeatOk && loadOk && errorRateOk;
        
        agent.setHealthStatus(healthy ? "healthy" : "unhealthy");
        agentCache.put(agentId, agent);
        
        result.put("agentId", agentId);
        result.put("status", agent.getStatus());
        result.put("healthy", healthy);
        result.put("heartbeatAge", heartbeatAge);
        result.put("heartbeatOk", heartbeatOk);
        result.put("loadOk", loadOk);
        result.put("errorRateOk", errorRateOk);
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }

    @Override
    public void updateAgentMetrics(String agentId, Map<String, Object> metrics) {
        AgentDTO agent = getAgent(agentId);
        if (agent != null) {
            if (metrics.containsKey("cpuUsage")) {
                agent.setCpuUsage(((Number) metrics.get("cpuUsage")).doubleValue());
            }
            if (metrics.containsKey("memoryUsage")) {
                agent.setMemoryUsage(((Number) metrics.get("memoryUsage")).doubleValue());
            }
            if (metrics.containsKey("currentLoad")) {
                agent.setCurrentLoad(((Number) metrics.get("currentLoad")).intValue());
            }
            if (metrics.containsKey("totalRequests")) {
                agent.setTotalRequests(((Number) metrics.get("totalRequests")).longValue());
            }
            if (metrics.containsKey("successRequests")) {
                agent.setSuccessRequests(((Number) metrics.get("successRequests")).longValue());
            }
            if (metrics.containsKey("failedRequests")) {
                agent.setFailedRequests(((Number) metrics.get("failedRequests")).longValue());
            }
            if (metrics.containsKey("avgResponseTime")) {
                agent.setAvgResponseTime(((Number) metrics.get("avgResponseTime")).doubleValue());
            }
            agentCache.put(agentId, agent);
        }
    }

    private List<AgentDTO> collectAllAgents() {
        List<AgentDTO> agents = new ArrayList<>();
        
        PageResult<SceneGroupDTO> groups = sceneGroupService.listAll(1, 1000);
        if (groups != null && groups.getList() != null) {
            for (SceneGroupDTO group : groups.getList()) {
                PageResult<SceneParticipantDTO> participants = 
                    sceneGroupService.listParticipants(group.getSceneGroupId(), 1, 100);
                
                if (participants != null && participants.getList() != null) {
                    for (SceneParticipantDTO p : participants.getList()) {
                        if (p.getParticipantType() == ParticipantType.AGENT || 
                            p.getParticipantType() == ParticipantType.SUPER_AGENT) {
                            AgentDTO agent = convertToAgent(p, group.getSceneGroupId());
                            agents.add(agent);
                            agentCache.put(agent.getAgentId(), agent);
                        }
                    }
                }
            }
        }
        
        Set<String> existingAgentIds = agents.stream()
            .map(AgentDTO::getAgentId)
            .collect(Collectors.toSet());
        
        agents.addAll(agentCache.values().stream()
            .filter(a -> !existingAgentIds.contains(a.getAgentId()))
            .collect(Collectors.toList()));
        
        if (agents.isEmpty()) {
            agents = getDefaultAgents();
        }
        
        return agents;
    }

    private AgentDTO convertToAgent(SceneParticipantDTO participant, String sceneGroupId) {
        AgentDTO agent = new AgentDTO();
        agent.setAgentId(participant.getParticipantId());
        agent.setAgentName(participant.getName() != null ? participant.getName() : participant.getParticipantId());
        agent.setAgentType(participant.getParticipantType() == ParticipantType.SUPER_AGENT ? "SUPER_AGENT" : "AGENT");
        agent.setSceneGroupId(sceneGroupId);
        agent.setRole(participant.getRole());
        agent.setRegisterTime(participant.getJoinTime());
        
        Long lastHeartbeat = heartbeatCache.get(participant.getParticipantId());
        if (lastHeartbeat != null) {
            agent.setLastHeartbeat(lastHeartbeat);
        } else {
            agent.setLastHeartbeat(participant.getLastHeartbeat() > 0 ? 
                participant.getLastHeartbeat() : System.currentTimeMillis());
        }
        
        long heartbeatAge = System.currentTimeMillis() - agent.getLastHeartbeat();
        if (heartbeatAge < 60000) {
            agent.setStatus("online");
        } else if (heartbeatAge < 300000) {
            agent.setStatus("busy");
        } else {
            agent.setStatus("offline");
        }
        
        agent.setBindingCount(getBindingCount(agent.getAgentId()));
        
        return agent;
    }

    private List<AgentDTO> getDefaultAgents() {
        List<AgentDTO> agents = new ArrayList<>();
        
        AgentDTO llmAgent = new AgentDTO();
        llmAgent.setAgentId("agent-llm-001");
        llmAgent.setAgentName("LLM Assistant");
        llmAgent.setAgentType("LLM");
        llmAgent.setStatus("online");
        llmAgent.setHealthStatus("healthy");
        llmAgent.setIpAddress("127.0.0.1");
        llmAgent.setPort(8080);
        llmAgent.setVersion("1.0.0");
        llmAgent.setClusterId("cluster-default");
        llmAgent.setRegisterTime(System.currentTimeMillis() - 86400000);
        llmAgent.setLastHeartbeat(System.currentTimeMillis());
        llmAgent.setMaxConcurrency(20);
        llmAgent.setCurrentLoad(5);
        llmAgent.setCpuUsage(35.5);
        llmAgent.setMemoryUsage(42.3);
        llmAgent.setTotalRequests(5000L);
        llmAgent.setSuccessRequests(4950L);
        llmAgent.setFailedRequests(50L);
        llmAgent.setAvgResponseTime(150.5);
        llmAgent.setCapabilities(Arrays.asList("chat", "completion", "embedding"));
        llmAgent.setSupportedModels(Arrays.asList("qwen-plus", "qwen-turbo", "deepseek-chat"));
        llmAgent.setBindingCount(getBindingCount("agent-llm-001"));
        agents.add(llmAgent);
        agentCache.put(llmAgent.getAgentId(), llmAgent);
        
        AgentDTO coordinatorAgent = new AgentDTO();
        coordinatorAgent.setAgentId("agent-coordinator-001");
        coordinatorAgent.setAgentName("Coordinator Agent");
        coordinatorAgent.setAgentType("WORKER");
        coordinatorAgent.setStatus("online");
        coordinatorAgent.setHealthStatus("healthy");
        coordinatorAgent.setIpAddress("127.0.0.1");
        coordinatorAgent.setPort(8081);
        coordinatorAgent.setVersion("1.0.0");
        coordinatorAgent.setClusterId("cluster-default");
        coordinatorAgent.setRegisterTime(System.currentTimeMillis() - 172800000);
        coordinatorAgent.setLastHeartbeat(System.currentTimeMillis() - 30000);
        coordinatorAgent.setMaxConcurrency(50);
        coordinatorAgent.setCurrentLoad(12);
        coordinatorAgent.setCpuUsage(28.2);
        coordinatorAgent.setMemoryUsage(35.1);
        coordinatorAgent.setTotalRequests(12000L);
        coordinatorAgent.setSuccessRequests(11800L);
        coordinatorAgent.setFailedRequests(200L);
        coordinatorAgent.setAvgResponseTime(85.3);
        coordinatorAgent.setCapabilities(Arrays.asList("coordinate", "dispatch", "monitor"));
        coordinatorAgent.setBindingCount(getBindingCount("agent-coordinator-001"));
        agents.add(coordinatorAgent);
        agentCache.put(coordinatorAgent.getAgentId(), coordinatorAgent);
        
        AgentDTO superAgent = new AgentDTO();
        superAgent.setAgentId("super-agent-001");
        superAgent.setAgentName("Super Agent");
        superAgent.setAgentType("SUPER_AGENT");
        superAgent.setStatus("online");
        superAgent.setHealthStatus("healthy");
        superAgent.setIpAddress("127.0.0.1");
        superAgent.setPort(8082);
        superAgent.setVersion("2.0.0");
        superAgent.setClusterId("cluster-default");
        superAgent.setRegisterTime(System.currentTimeMillis() - 259200000);
        superAgent.setLastHeartbeat(System.currentTimeMillis() - 60000);
        superAgent.setMaxConcurrency(100);
        superAgent.setCurrentLoad(35);
        superAgent.setCpuUsage(45.8);
        superAgent.setMemoryUsage(52.4);
        superAgent.setTotalRequests(25000L);
        superAgent.setSuccessRequests(24800L);
        superAgent.setFailedRequests(200L);
        superAgent.setAvgResponseTime(120.7);
        superAgent.setCapabilities(Arrays.asList("manage", "orchestrate", "analyze"));
        superAgent.setBindingCount(getBindingCount("super-agent-001"));
        agents.add(superAgent);
        agentCache.put(superAgent.getAgentId(), superAgent);
        
        return agents;
    }
}
