package net.ooder.nexus.adapter.inbound.controller.mcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/api/mcp/management")
public class McpAgentManagementController {

    private static final Logger log = LoggerFactory.getLogger(McpAgentManagementController.class);
    
    // MCP Agent 实例存储
    private final Map<String, McpAgentInstance> agentInstances = new HashMap<>();
    
    // 初始化默认实例
    public McpAgentManagementController() {
        initializeDefaultAgents();
    }
    
    private void initializeDefaultAgents() {
        // 默认MCP Agent实例
        agentInstances.put("default", new McpAgentInstance(
            "default", "Default MCP Agent", "local", "running", "0.6.5",
            "localhost:9876", 30000,
            "默认MCP Agent实例，用于本地开发和测试"
        ));
    }
    
    /**
     * 获取所有MCP Agent实例
     */
    @GetMapping("/agents")
    public Map<String, Object> getAllAgents() {
        log.info("Get all MCP Agent instances requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Map<String, Object>> agents = new ArrayList<>();
            for (McpAgentInstance agent : agentInstances.values()) {
                agents.add(agent.toMap());
            }
            
            // 计算实例状态统计
            long runningCount = agentInstances.values().stream().filter(a -> "running".equals(a.getStatus())).count();
            long stoppedCount = agentInstances.values().stream().filter(a -> "stopped".equals(a.getStatus())).count();
            long errorCount = agentInstances.values().stream().filter(a -> "error".equals(a.getStatus())).count();
            
            Map<String, Object> summary = new HashMap<>();
            summary.put("total", agentInstances.size());
            summary.put("running", runningCount);
            summary.put("stopped", stoppedCount);
            summary.put("error", errorCount);
            
            response.put("status", "success");
            response.put("message", "MCP Agent instances retrieved successfully");
            response.put("summary", summary);
            response.put("data", agents);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting MCP Agent instances: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "AGENTS_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取单个MCP Agent实例详情
     */
    @GetMapping("/agents/{agentId}")
    public Map<String, Object> getAgentDetails(@PathVariable String agentId) {
        log.info("Get MCP Agent details requested: {}", agentId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            McpAgentInstance agent = agentInstances.get(agentId);
            if (agent == null) {
                response.put("status", "error");
                response.put("message", "MCP Agent instance not found");
                response.put("code", "AGENT_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            response.put("status", "success");
            response.put("message", "MCP Agent details retrieved successfully");
            response.put("data", agent.toMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting MCP Agent details: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "AGENT_DETAILS_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 创建MCP Agent实例
     */
    @PostMapping("/agents")
    public Map<String, Object> createAgent(@RequestBody Map<String, Object> agentData) {
        log.info("Create MCP Agent instance requested: {}", agentData);
        Map<String, Object> response = new HashMap<>();
        
        try {
            String id = UUID.randomUUID().toString().substring(0, 8);
            String name = (String) agentData.getOrDefault("name", "MCP Agent " + id);
            String type = (String) agentData.getOrDefault("type", "local");
            String endpoint = (String) agentData.getOrDefault("endpoint", "localhost:9876");
            int heartbeatInterval = agentData.containsKey("heartbeatInterval") ? 
                Integer.parseInt(agentData.get("heartbeatInterval").toString()) : 30000;
            String description = (String) agentData.getOrDefault("description", "New MCP Agent instance");
            
            McpAgentInstance agent = new McpAgentInstance(
                id, name, type, "stopped", "0.6.5",
                endpoint, heartbeatInterval, description
            );
            
            agentInstances.put(id, agent);
            
            response.put("status", "success");
            response.put("message", "MCP Agent instance created successfully");
            response.put("data", agent.toMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error creating MCP Agent instance: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "AGENT_CREATION_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 更新MCP Agent实例
     */
    @PutMapping("/agents/{agentId}")
    public Map<String, Object> updateAgent(@PathVariable String agentId, @RequestBody Map<String, Object> agentData) {
        log.info("Update MCP Agent instance requested: {}, data: {}", agentId, agentData);
        Map<String, Object> response = new HashMap<>();
        
        try {
            McpAgentInstance agent = agentInstances.get(agentId);
            if (agent == null) {
                response.put("status", "error");
                response.put("message", "MCP Agent instance not found");
                response.put("code", "AGENT_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            // 更新字段
            if (agentData.containsKey("name")) {
                agent.setName((String) agentData.get("name"));
            }
            if (agentData.containsKey("type")) {
                agent.setType((String) agentData.get("type"));
            }
            if (agentData.containsKey("endpoint")) {
                agent.setEndpoint((String) agentData.get("endpoint"));
            }
            if (agentData.containsKey("heartbeatInterval")) {
                agent.setHeartbeatInterval(Integer.parseInt(agentData.get("heartbeatInterval").toString()));
            }
            if (agentData.containsKey("description")) {
                agent.setDescription((String) agentData.get("description"));
            }
            
            response.put("status", "success");
            response.put("message", "MCP Agent instance updated successfully");
            response.put("data", agent.toMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error updating MCP Agent instance: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "AGENT_UPDATE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 删除MCP Agent实例
     */
    @DeleteMapping("/agents/{agentId}")
    public Map<String, Object> deleteAgent(@PathVariable String agentId) {
        log.info("Delete MCP Agent instance requested: {}", agentId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (agentId.equals("default")) {
                response.put("status", "error");
                response.put("message", "Cannot delete default MCP Agent instance");
                response.put("code", "AGENT_DELETE_ERROR");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            McpAgentInstance agent = agentInstances.remove(agentId);
            if (agent == null) {
                response.put("status", "error");
                response.put("message", "MCP Agent instance not found");
                response.put("code", "AGENT_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            response.put("status", "success");
            response.put("message", "MCP Agent instance deleted successfully");
            response.put("agentId", agentId);
            response.put("agentName", agent.getName());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error deleting MCP Agent instance: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "AGENT_DELETE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 启动MCP Agent实例
     */
    @PostMapping("/agents/{agentId}/start")
    public Map<String, Object> startAgent(@PathVariable String agentId) {
        log.info("Start MCP Agent instance requested: {}", agentId);
        return handleAgentOperation(agentId, "start");
    }
    
    /**
     * 停止MCP Agent实例
     */
    @PostMapping("/agents/{agentId}/stop")
    public Map<String, Object> stopAgent(@PathVariable String agentId) {
        log.info("Stop MCP Agent instance requested: {}", agentId);
        return handleAgentOperation(agentId, "stop");
    }
    
    /**
     * 重启MCP Agent实例
     */
    @PostMapping("/agents/{agentId}/restart")
    public Map<String, Object> restartAgent(@PathVariable String agentId) {
        log.info("Restart MCP Agent instance requested: {}", agentId);
        return handleAgentOperation(agentId, "restart");
    }
    
    /**
     * 检查MCP Agent实例
     */
    @PostMapping("/agents/{agentId}/check")
    public Map<String, Object> checkAgent(@PathVariable String agentId) {
        log.info("Check MCP Agent instance requested: {}", agentId);
        return handleAgentOperation(agentId, "check");
    }
    
    /**
     * 处理MCP Agent操作
     */
    private Map<String, Object> handleAgentOperation(String agentId, String operation) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            McpAgentInstance agent = agentInstances.get(agentId);
            if (agent == null) {
                response.put("status", "error");
                response.put("message", "MCP Agent instance not found");
                response.put("code", "AGENT_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            String result = "success";
            String message = "";
            
            switch (operation) {
                case "start":
                    if (agent.getStatus().equals("running")) {
                        result = "error";
                        message = "MCP Agent instance is already running";
                    } else {
                        agent.setStatus("running");
                        agent.setLastHeartbeat(System.currentTimeMillis());
                        message = "MCP Agent instance started successfully";
                    }
                    break;
                case "stop":
                    if (agent.getStatus().equals("stopped")) {
                        result = "error";
                        message = "MCP Agent instance is already stopped";
                    } else {
                        agent.setStatus("stopped");
                        message = "MCP Agent instance stopped successfully";
                    }
                    break;
                case "restart":
                    agent.setStatus("running");
                    agent.setLastHeartbeat(System.currentTimeMillis());
                    message = "MCP Agent instance restarted successfully";
                    break;
                case "check":
                    if (agent.getStatus().equals("running")) {
                        agent.setLastHeartbeat(System.currentTimeMillis());
                        message = "MCP Agent instance health check passed";
                    } else {
                        result = "error";
                        message = "MCP Agent instance is not running";
                    }
                    break;
            }
            
            response.put("status", result);
            response.put("message", message);
            response.put("agentId", agentId);
            response.put("agentName", agent.getName());
            response.put("agentStatus", agent.getStatus());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error handling MCP Agent operation: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "AGENT_OPERATION_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    // MCP Agent 实例类
    private static class McpAgentInstance {
        private final String id;
        private String name;
        private String type;
        private String status;
        private final String version;
        private String endpoint;
        private int heartbeatInterval;
        private String description;
        private long lastHeartbeat;
        private final long createdAt;
        
        public McpAgentInstance(String id, String name, String type, String status, String version,
                              String endpoint, int heartbeatInterval, String description) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.status = status;
            this.version = version;
            this.endpoint = endpoint;
            this.heartbeatInterval = heartbeatInterval;
            this.description = description;
            this.lastHeartbeat = System.currentTimeMillis();
            this.createdAt = System.currentTimeMillis();
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("type", type);
            map.put("status", status);
            map.put("version", version);
            map.put("endpoint", endpoint);
            map.put("heartbeatInterval", heartbeatInterval);
            map.put("description", description);
            map.put("lastHeartbeat", lastHeartbeat);
            map.put("createdAt", createdAt);
            
            // 计算心跳状态
            long now = System.currentTimeMillis();
            long timeSinceHeartbeat = now - lastHeartbeat;
            boolean heartbeatStatus = timeSinceHeartbeat < heartbeatInterval * 2;
            map.put("heartbeatStatus", heartbeatStatus);
            map.put("timeSinceHeartbeat", timeSinceHeartbeat);
            
            return map;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getVersion() { return version; }
        public String getEndpoint() { return endpoint; }
        public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
        public int getHeartbeatInterval() { return heartbeatInterval; }
        public void setHeartbeatInterval(int heartbeatInterval) { this.heartbeatInterval = heartbeatInterval; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public long getLastHeartbeat() { return lastHeartbeat; }
        public void setLastHeartbeat(long lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
        public long getCreatedAt() { return createdAt; }
    }
}