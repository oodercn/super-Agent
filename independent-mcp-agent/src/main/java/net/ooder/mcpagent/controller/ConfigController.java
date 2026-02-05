package net.ooder.mcpagent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    private static final Logger log = LoggerFactory.getLogger(ConfigController.class);

    // MCP配置存储
    private final Map<String, Object> mcpConfig = new ConcurrentHashMap<>();

    // Route配置存储
    private final Map<String, Object> routeConfig = new ConcurrentHashMap<>();

    // End配置存储
    private final Map<String, Object> endConfig = new ConcurrentHashMap<>();

    // 配置修改历史
    private final List<ConfigChange> configChanges = new ArrayList<>();

    // 初始化默认配置数据
    public ConfigController() {
        initializeDefaultConfigs();
    }

    private void initializeDefaultConfigs() {
        // 初始化MCP配置
        initializeMcpConfig();
        
        // 初始化Route配置
        initializeRouteConfig();
        
        // 初始化End配置
        initializeEndConfig();
    }

    private void initializeMcpConfig() {
        mcpConfig.put("version", "0.6.5");
        mcpConfig.put("servicePort", 8080);
        mcpConfig.put("heartbeatInterval", 30000); // 30秒
        mcpConfig.put("logLevel", "INFO");
        mcpConfig.put("maxConnections", 1000);
        mcpConfig.put("connectionTimeout", 60000); // 60秒
        mcpConfig.put("threadPoolSize", 20);
        mcpConfig.put("bufferSize", 8192);
        mcpConfig.put("enableTls", false);
        mcpConfig.put("enableCompression", true);
        mcpConfig.put("enableRateLimiting", true);
        mcpConfig.put("rateLimitPerSecond", 100);
        mcpConfig.put("enableCaching", true);
        mcpConfig.put("cacheExpiryTime", 300000); // 5分钟
        mcpConfig.put("enableMetrics", true);
        mcpConfig.put("metricsInterval", 60000); // 1分钟
    }

    private void initializeRouteConfig() {
        routeConfig.put("maxRoutes", 1000);
        routeConfig.put("routeTimeout", 30000); // 30秒
        routeConfig.put("routeRetryCount", 3);
        routeConfig.put("routeRetryDelay", 1000); // 1秒
        routeConfig.put("enableRouteDiscovery", true);
        routeConfig.put("routeDiscoveryInterval", 300000); // 5分钟
        routeConfig.put("routeDiscoveryTimeout", 10000); // 10秒
        routeConfig.put("maxRouteHistory", 1000);
        routeConfig.put("routeHealthCheckInterval", 60000); // 1分钟
        routeConfig.put("routeHealthCheckTimeout", 5000); // 5秒
        routeConfig.put("minRouteLatency", 1);
        routeConfig.put("maxRouteLatency", 5000); // 5秒
        routeConfig.put("routeLatencyThreshold", 1000); // 1秒
        routeConfig.put("routeReliabilityThreshold", 90.0);
    }

    private void initializeEndConfig() {
        endConfig.put("maxEndAgents", 500);
        endConfig.put("endAgentTimeout", 60000); // 60秒
        endConfig.put("endAgentHeartbeatInterval", 45000); // 45秒
        endConfig.put("endAgentMaxHeartbeatMisses", 3);
        endConfig.put("enableEndAgentDiscovery", true);
        endConfig.put("endAgentDiscoveryInterval", 600000); // 10分钟
        endConfig.put("endAgentDiscoveryTimeout", 15000); // 15秒
        endConfig.put("endAgentConnectionTimeout", 30000); // 30秒
        endConfig.put("endAgentReadTimeout", 60000); // 60秒
        endConfig.put("endAgentWriteTimeout", 30000); // 30秒
        endConfig.put("maxEndAgentCommands", 100);
        endConfig.put("endAgentCommandTimeout", 30000); // 30秒
        endConfig.put("endAgentQueueSize", 1000);
        endConfig.put("endAgentMaxRetries", 3);
    }

    /**
     * 获取MCP配置
     */
    @GetMapping("/mcp")
    public Map<String, Object> getMcpConfig() {
        log.info("Get MCP config requested");
        Map<String, Object> response = new HashMap<>();

        try {
            response.put("status", "success");
            response.put("message", "MCP config retrieved successfully");
            response.put("data", mcpConfig);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting MCP config: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get MCP config: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 更新MCP配置
     */
    @PutMapping("/mcp")
    public Map<String, Object> updateMcpConfig(@RequestBody Map<String, Object> configData) {
        log.info("Update MCP config requested: {}", configData);
        Map<String, Object> response = new HashMap<>();

        try {
            // 记录配置变更
            recordConfigChange("MCP", configData);

            // 更新配置
            for (Map.Entry<String, Object> entry : configData.entrySet()) {
                mcpConfig.put(entry.getKey(), entry.getValue());
            }

            response.put("status", "success");
            response.put("message", "MCP config updated successfully");
            response.put("data", mcpConfig);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error updating MCP config: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to update MCP config: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取Route配置
     */
    @GetMapping("/route")
    public Map<String, Object> getRouteConfig() {
        log.info("Get Route config requested");
        Map<String, Object> response = new HashMap<>();

        try {
            response.put("status", "success");
            response.put("message", "Route config retrieved successfully");
            response.put("data", routeConfig);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting Route config: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get Route config: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 更新Route配置
     */
    @PutMapping("/route")
    public Map<String, Object> updateRouteConfig(@RequestBody Map<String, Object> configData) {
        log.info("Update Route config requested: {}", configData);
        Map<String, Object> response = new HashMap<>();

        try {
            // 记录配置变更
            recordConfigChange("Route", configData);

            // 更新配置
            for (Map.Entry<String, Object> entry : configData.entrySet()) {
                routeConfig.put(entry.getKey(), entry.getValue());
            }

            response.put("status", "success");
            response.put("message", "Route config updated successfully");
            response.put("data", routeConfig);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error updating Route config: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to update Route config: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取End配置
     */
    @GetMapping("/end")
    public Map<String, Object> getEndConfig() {
        log.info("Get End config requested");
        Map<String, Object> response = new HashMap<>();

        try {
            response.put("status", "success");
            response.put("message", "End config retrieved successfully");
            response.put("data", endConfig);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting End config: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get End config: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 更新End配置
     */
    @PutMapping("/end")
    public Map<String, Object> updateEndConfig(@RequestBody Map<String, Object> configData) {
        log.info("Update End config requested: {}", configData);
        Map<String, Object> response = new HashMap<>();

        try {
            // 记录配置变更
            recordConfigChange("End", configData);

            // 更新配置
            for (Map.Entry<String, Object> entry : configData.entrySet()) {
                endConfig.put(entry.getKey(), entry.getValue());
            }

            response.put("status", "success");
            response.put("message", "End config updated successfully");
            response.put("data", endConfig);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error updating End config: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to update End config: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取所有配置
     */
    @GetMapping("/all")
    public Map<String, Object> getAllConfigs() {
        log.info("Get all configs requested");
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> allConfigs = new HashMap<>();
            allConfigs.put("mcp", mcpConfig);
            allConfigs.put("route", routeConfig);
            allConfigs.put("end", endConfig);

            response.put("status", "success");
            response.put("message", "All configs retrieved successfully");
            response.put("data", allConfigs);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting all configs: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get all configs: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 重置配置为默认值
     */
    @PostMapping("/reset/{configType}")
    public Map<String, Object> resetConfig(@PathVariable String configType) {
        log.info("Reset config requested: configType={}", configType);
        Map<String, Object> response = new HashMap<>();

        try {
            switch (configType.toLowerCase()) {
                case "mcp":
                    mcpConfig.clear();
                    initializeMcpConfig();
                    Map<String, Object> mcpResetMap = new HashMap<>();
                    mcpResetMap.put("action", "reset");
                    recordConfigChange("MCP", mcpResetMap);
                    response.put("data", mcpConfig);
                    break;
                case "route":
                    routeConfig.clear();
                    initializeRouteConfig();
                    Map<String, Object> routeResetMap = new HashMap<>();
                    routeResetMap.put("action", "reset");
                    recordConfigChange("Route", routeResetMap);
                    response.put("data", routeConfig);
                    break;
                case "end":
                    endConfig.clear();
                    initializeEndConfig();
                    Map<String, Object> endResetMap = new HashMap<>();
                    endResetMap.put("action", "reset");
                    recordConfigChange("End", endResetMap);
                    response.put("data", endConfig);
                    break;
                case "all":
                    mcpConfig.clear();
                    routeConfig.clear();
                    endConfig.clear();
                    initializeDefaultConfigs();
                    Map<String, Object> allResetMap = new HashMap<>();
                    allResetMap.put("action", "reset");
                    recordConfigChange("All", allResetMap);
                    Map<String, Object> allConfigs = new HashMap<>();
                    allConfigs.put("mcp", mcpConfig);
                    allConfigs.put("route", routeConfig);
                    allConfigs.put("end", endConfig);
                    response.put("data", allConfigs);
                    break;
                default:
                    response.put("status", "error");
                    response.put("message", "Invalid config type: " + configType);
                    response.put("code", 400);
                    response.put("timestamp", System.currentTimeMillis());
                    return response;
            }

            response.put("status", "success");
            response.put("message", configType + " config reset to default values successfully");
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error resetting config: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to reset config: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取配置变更历史
     */
    @GetMapping("/history")
    public Map<String, Object> getConfigHistory(
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(required = false) String configType) {
        log.info("Get config history requested: limit={}, configType={}", limit, configType);
        Map<String, Object> response = new HashMap<>();

        try {
            List<ConfigChange> filteredHistory = new ArrayList<>();
            for (ConfigChange change : configChanges) {
                if (configType == null || change.getConfigType().equalsIgnoreCase(configType)) {
                    filteredHistory.add(change);
                    if (filteredHistory.size() >= limit) {
                        break;
                    }
                }
            }

            // 按时间倒序排序
            filteredHistory.sort(Comparator.comparingLong(ConfigChange::getTimestamp).reversed());

            response.put("status", "success");
            response.put("message", "Config history retrieved successfully");
            response.put("data", filteredHistory);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting config history: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get config history: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取配置统计
     */
    @GetMapping("/stats")
    public Map<String, Object> getConfigStats() {
        log.info("Get config stats requested");
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("mcpConfigCount", mcpConfig.size());
            stats.put("routeConfigCount", routeConfig.size());
            stats.put("endConfigCount", endConfig.size());
            stats.put("totalConfigCount", mcpConfig.size() + routeConfig.size() + endConfig.size());
            stats.put("configChangeCount", configChanges.size());
            stats.put("lastConfigChange", configChanges.isEmpty() ? null : configChanges.get(configChanges.size() - 1));

            response.put("status", "success");
            response.put("message", "Config stats retrieved successfully");
            response.put("data", stats);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting config stats: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get config stats: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    // 记录配置变更
    private void recordConfigChange(String configType, Map<String, Object> changes) {
        ConfigChange change = new ConfigChange(
                UUID.randomUUID().toString(),
                configType,
                changes,
                System.currentTimeMillis()
        );
        configChanges.add(change);
        
        // 限制历史记录数量
        if (configChanges.size() > 1000) {
            configChanges.remove(0);
        }
    }

    // 配置变更类
    private static class ConfigChange {
        private final String id;
        private final String configType;
        private final Map<String, Object> changes;
        private final long timestamp;

        public ConfigChange(String id, String configType, Map<String, Object> changes, long timestamp) {
            this.id = id;
            this.configType = configType;
            this.changes = changes;
            this.timestamp = timestamp;
        }

        public String getId() {
            return id;
        }

        public String getConfigType() {
            return configType;
        }

        public Map<String, Object> getChanges() {
            return changes;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
