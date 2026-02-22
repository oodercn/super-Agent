package net.ooder.nexus.adapter.inbound.controller.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.ooder.nexus.model.Result;
import net.ooder.nexus.domain.config.model.ConfigChange;
import net.ooder.nexus.domain.config.model.NexusConfigResult;
import net.ooder.nexus.domain.config.model.RouteConfigResult;
import net.ooder.nexus.domain.config.model.EndConfigResult;
import net.ooder.nexus.domain.config.model.AllConfigsResult;
import net.ooder.nexus.domain.config.model.ConfigResetResult;
import net.ooder.nexus.domain.config.model.ConfigStatsResult;
import net.ooder.nexus.service.INexusService;
import net.ooder.nexus.service.NexusServiceFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    private static final Logger log = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    private NexusServiceFactory serviceFactory;

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

    private INexusService getService() {
        return serviceFactory.getService();
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
        routeConfig.put("routeRefreshInterval", 60000);
        routeConfig.put("routeTimeout", 30000);
        routeConfig.put("routeMaxRetries", 3);
        routeConfig.put("enableRouteCaching", true);
        routeConfig.put("routeCacheExpiry", 300000);
        routeConfig.put("enableRouteHealthCheck", true);
        routeConfig.put("routeHealthCheckInterval", 60000);
        routeConfig.put("enableRouteMetrics", true);
        routeConfig.put("routeMetricsInterval", 60000);
        routeConfig.put("routePriorityOrder", new ArrayList<String>());
        routeConfig.put("routeBlacklist", new ArrayList<String>());
        routeConfig.put("routeWhitelist", new ArrayList<String>());
        routeConfig.put("enableRouteLoadBalancing", true);
        routeConfig.put("loadBalancingStrategy", "round-robin");
        routeConfig.put("maxRoutesPerAgent", 100);
        routeConfig.put("routeQueueSize", 1000);
    }

    private void initializeEndConfig() {
        endConfig.put("endAgentDiscoveryInterval", 600000);
        endConfig.put("endAgentHeartbeatInterval", 45000);
        endConfig.put("endAgentTimeout", 60000);
        endConfig.put("endAgentMaxRetries", 3);
        endConfig.put("enableEndAgentCaching", true);
        endConfig.put("endAgentCacheExpiry", 300000);
        endConfig.put("enableEndAgentHealthCheck", true);
        endConfig.put("endAgentHealthCheckInterval", 60000);
        endConfig.put("enableEndAgentMetrics", true);
        endConfig.put("endAgentMetricsInterval", 60000);
        endConfig.put("enableEndAgentAutoDiscovery", true);
        endConfig.put("enableEndAgentAutoRegistration", false);
        endConfig.put("endAgentMaxConcurrentTasks", 100);
        endConfig.put("endAgentQueueSize", 1000);
        endConfig.put("endAgentCommandTimeout", 30000);
        endConfig.put("endAgentMaxConnections", 500);
        endConfig.put("enableEndAgentCompression", true);
        endConfig.put("enableEndAgentEncryption", false);
    }

    /**
     * 获取Nexus配置
     */
    @GetMapping("/nexus")
    public Result<NexusConfigResult> getNexusConfig() {
        log.info("Get Nexus config requested");

        try {
            NexusConfigResult result = new NexusConfigResult();
            result.setVersion((String) mcpConfig.get("version"));
            result.setServicePort((int) mcpConfig.get("servicePort"));
            result.setHeartbeatInterval((int) mcpConfig.get("heartbeatInterval"));
            result.setLogLevel((String) mcpConfig.get("logLevel"));
            result.setMaxConnections((int) mcpConfig.get("maxConnections"));
            result.setConnectionTimeout((int) mcpConfig.get("connectionTimeout"));
            result.setThreadPoolSize((int) mcpConfig.get("threadPoolSize"));
            result.setBufferSize((int) mcpConfig.get("bufferSize"));
            result.setEnableTls((boolean) mcpConfig.get("enableTls"));
            result.setEnableCompression((boolean) mcpConfig.get("enableCompression"));
            result.setEnableRateLimiting((boolean) mcpConfig.get("enableRateLimiting"));
            result.setRateLimitPerSecond((int) mcpConfig.get("rateLimitPerSecond"));
            result.setEnableCaching((boolean) mcpConfig.get("enableCaching"));
            result.setCacheExpiryTime((int) mcpConfig.get("cacheExpiryTime"));
            result.setEnableMetrics((boolean) mcpConfig.get("enableMetrics"));
            result.setMetricsInterval((int) mcpConfig.get("metricsInterval"));
            
            return Result.success("Nexus config retrieved successfully", result);
        } catch (Exception e) {
            log.error("Error getting Nexus config: {}", e.getMessage());
            return Result.error("Failed to get Nexus config: " + e.getMessage());
        }
    }

    /**
     * 更新Nexus配置
     */
    @PutMapping("/nexus")
    public Result<NexusConfigResult> updateNexusConfig(@RequestBody Map<String, Object> configData) {
        log.info("Update Nexus config requested: {}", configData);

        try {
            // 记录配置变更
            recordConfigChange("NEXUS", configData);

            // 更新配置
            for (Map.Entry<String, Object> entry : configData.entrySet()) {
                mcpConfig.put(entry.getKey(), entry.getValue());
            }

            NexusConfigResult result = new NexusConfigResult();
            result.setVersion((String) mcpConfig.get("version"));
            result.setServicePort((int) mcpConfig.get("servicePort"));
            result.setHeartbeatInterval((int) mcpConfig.get("heartbeatInterval"));
            result.setLogLevel((String) mcpConfig.get("logLevel"));
            result.setMaxConnections((int) mcpConfig.get("maxConnections"));
            result.setConnectionTimeout((int) mcpConfig.get("connectionTimeout"));
            result.setThreadPoolSize((int) mcpConfig.get("threadPoolSize"));
            result.setBufferSize((int) mcpConfig.get("bufferSize"));
            result.setEnableTls((boolean) mcpConfig.get("enableTls"));
            result.setEnableCompression((boolean) mcpConfig.get("enableCompression"));
            result.setEnableRateLimiting((boolean) mcpConfig.get("enableRateLimiting"));
            result.setRateLimitPerSecond((int) mcpConfig.get("rateLimitPerSecond"));
            result.setEnableCaching((boolean) mcpConfig.get("enableCaching"));
            result.setCacheExpiryTime((int) mcpConfig.get("cacheExpiryTime"));
            result.setEnableMetrics((boolean) mcpConfig.get("enableMetrics"));
            result.setMetricsInterval((int) mcpConfig.get("metricsInterval"));

            return Result.success("Nexus config updated successfully", result);
        } catch (Exception e) {
            log.error("Error updating Nexus config: {}", e.getMessage());
            return Result.error("Failed to update Nexus config: " + e.getMessage());
        }
    }

    /**
     * 获取Route配置
     */
    @GetMapping("/route")
    public Result<RouteConfigResult> getRouteConfig() {
        log.info("Get Route config requested");

        try {
            RouteConfigResult result = new RouteConfigResult();
            result.setRouteRefreshInterval((int) routeConfig.get("routeRefreshInterval"));
            result.setRouteTimeout((int) routeConfig.get("routeTimeout"));
            result.setRouteMaxRetries((int) routeConfig.get("routeMaxRetries"));
            result.setEnableRouteCaching((boolean) routeConfig.get("enableRouteCaching"));
            result.setRouteCacheExpiry((int) routeConfig.get("routeCacheExpiry"));
            result.setEnableRouteHealthCheck((boolean) routeConfig.get("enableRouteHealthCheck"));
            result.setRouteHealthCheckInterval((int) routeConfig.get("routeHealthCheckInterval"));
            result.setEnableRouteMetrics((boolean) routeConfig.get("enableRouteMetrics"));
            result.setRouteMetricsInterval((int) routeConfig.get("routeMetricsInterval"));
            result.setRoutePriorityOrder((List<String>) routeConfig.get("routePriorityOrder"));
            result.setRouteBlacklist((List<String>) routeConfig.get("routeBlacklist"));
            result.setRouteWhitelist((List<String>) routeConfig.get("routeWhitelist"));
            result.setEnableRouteLoadBalancing((boolean) routeConfig.get("enableRouteLoadBalancing"));
            result.setLoadBalancingStrategy((String) routeConfig.get("loadBalancingStrategy"));
            result.setMaxRoutesPerAgent((int) routeConfig.get("maxRoutesPerAgent"));
            result.setRouteQueueSize((int) routeConfig.get("routeQueueSize"));
            
            return Result.success("Route config retrieved successfully", result);
        } catch (Exception e) {
            log.error("Error getting Route config: {}", e.getMessage());
            return Result.error("Failed to get Route config: " + e.getMessage());
        }
    }

    /**
     * 更新Route配置
     */
    @PutMapping("/route")
    public Result<RouteConfigResult> updateRouteConfig(@RequestBody Map<String, Object> configData) {
        log.info("Update Route config requested: {}", configData);

        try {
            // 记录配置变更
            recordConfigChange("Route", configData);

            // 更新配置
            for (Map.Entry<String, Object> entry : configData.entrySet()) {
                routeConfig.put(entry.getKey(), entry.getValue());
            }

            RouteConfigResult result = new RouteConfigResult();
            result.setRouteRefreshInterval((int) routeConfig.get("routeRefreshInterval"));
            result.setRouteTimeout((int) routeConfig.get("routeTimeout"));
            result.setRouteMaxRetries((int) routeConfig.get("routeMaxRetries"));
            result.setEnableRouteCaching((boolean) routeConfig.get("enableRouteCaching"));
            result.setRouteCacheExpiry((int) routeConfig.get("routeCacheExpiry"));
            result.setEnableRouteHealthCheck((boolean) routeConfig.get("enableRouteHealthCheck"));
            result.setRouteHealthCheckInterval((int) routeConfig.get("routeHealthCheckInterval"));
            result.setEnableRouteMetrics((boolean) routeConfig.get("enableRouteMetrics"));
            result.setRouteMetricsInterval((int) routeConfig.get("routeMetricsInterval"));
            result.setRoutePriorityOrder((List<String>) routeConfig.get("routePriorityOrder"));
            result.setRouteBlacklist((List<String>) routeConfig.get("routeBlacklist"));
            result.setRouteWhitelist((List<String>) routeConfig.get("routeWhitelist"));
            result.setEnableRouteLoadBalancing((boolean) routeConfig.get("enableRouteLoadBalancing"));
            result.setLoadBalancingStrategy((String) routeConfig.get("loadBalancingStrategy"));
            result.setMaxRoutesPerAgent((int) routeConfig.get("maxRoutesPerAgent"));
            result.setRouteQueueSize((int) routeConfig.get("routeQueueSize"));

            return Result.success("Route config updated successfully", result);
        } catch (Exception e) {
            log.error("Error updating Route config: {}", e.getMessage());
            return Result.error("Failed to update Route config: " + e.getMessage());
        }
    }

    /**
     * 获取End配置
     */
    @GetMapping("/end")
    public Result<EndConfigResult> getEndConfig() {
        log.info("Get End config requested");

        try {
            EndConfigResult result = new EndConfigResult();
            result.setEndAgentDiscoveryInterval((int) endConfig.get("endAgentDiscoveryInterval"));
            result.setEndAgentHeartbeatInterval((int) endConfig.get("endAgentHeartbeatInterval"));
            result.setEndAgentTimeout((int) endConfig.get("endAgentTimeout"));
            result.setEndAgentMaxRetries((int) endConfig.get("endAgentMaxRetries"));
            result.setEnableEndAgentCaching((boolean) endConfig.get("enableEndAgentCaching"));
            result.setEndAgentCacheExpiry((int) endConfig.get("endAgentCacheExpiry"));
            result.setEnableEndAgentHealthCheck((boolean) endConfig.get("enableEndAgentHealthCheck"));
            result.setEndAgentHealthCheckInterval((int) endConfig.get("endAgentHealthCheckInterval"));
            result.setEnableEndAgentMetrics((boolean) endConfig.get("enableEndAgentMetrics"));
            result.setEndAgentMetricsInterval((int) endConfig.get("endAgentMetricsInterval"));
            result.setEnableEndAgentAutoDiscovery((boolean) endConfig.get("enableEndAgentAutoDiscovery"));
            result.setEnableEndAgentAutoRegistration((boolean) endConfig.get("enableEndAgentAutoRegistration"));
            result.setEndAgentMaxConcurrentTasks((int) endConfig.get("endAgentMaxConcurrentTasks"));
            result.setEndAgentQueueSize((int) endConfig.get("endAgentQueueSize"));
            result.setEndAgentCommandTimeout((int) endConfig.get("endAgentCommandTimeout"));
            result.setEndAgentMaxConnections((int) endConfig.get("endAgentMaxConnections"));
            result.setEnableEndAgentCompression((boolean) endConfig.get("enableEndAgentCompression"));
            result.setEnableEndAgentEncryption((boolean) endConfig.get("enableEndAgentEncryption"));
            
            return Result.success("End config retrieved successfully", result);
        } catch (Exception e) {
            log.error("Error getting End config: {}", e.getMessage());
            return Result.error("Failed to get End config: " + e.getMessage());
        }
    }

    /**
     * 更新End配置
     */
    @PutMapping("/end")
    public Result<EndConfigResult> updateEndConfig(@RequestBody Map<String, Object> configData) {
        log.info("Update End config requested: {}", configData);

        try {
            // 记录配置变更
            recordConfigChange("End", configData);

            // 更新配置
            for (Map.Entry<String, Object> entry : configData.entrySet()) {
                endConfig.put(entry.getKey(), entry.getValue());
            }

            EndConfigResult result = new EndConfigResult();
            result.setEndAgentDiscoveryInterval((int) endConfig.get("endAgentDiscoveryInterval"));
            result.setEndAgentHeartbeatInterval((int) endConfig.get("endAgentHeartbeatInterval"));
            result.setEndAgentTimeout((int) endConfig.get("endAgentTimeout"));
            result.setEndAgentMaxRetries((int) endConfig.get("endAgentMaxRetries"));
            result.setEnableEndAgentCaching((boolean) endConfig.get("enableEndAgentCaching"));
            result.setEndAgentCacheExpiry((int) endConfig.get("endAgentCacheExpiry"));
            result.setEnableEndAgentHealthCheck((boolean) endConfig.get("enableEndAgentHealthCheck"));
            result.setEndAgentHealthCheckInterval((int) endConfig.get("endAgentHealthCheckInterval"));
            result.setEnableEndAgentMetrics((boolean) endConfig.get("enableEndAgentMetrics"));
            result.setEndAgentMetricsInterval((int) endConfig.get("endAgentMetricsInterval"));
            result.setEnableEndAgentAutoDiscovery((boolean) endConfig.get("enableEndAgentAutoDiscovery"));
            result.setEnableEndAgentAutoRegistration((boolean) endConfig.get("enableEndAgentAutoRegistration"));
            result.setEndAgentMaxConcurrentTasks((int) endConfig.get("endAgentMaxConcurrentTasks"));
            result.setEndAgentQueueSize((int) endConfig.get("endAgentQueueSize"));
            result.setEndAgentCommandTimeout((int) endConfig.get("endAgentCommandTimeout"));
            result.setEndAgentMaxConnections((int) endConfig.get("endAgentMaxConnections"));
            result.setEnableEndAgentCompression((boolean) endConfig.get("enableEndAgentCompression"));
            result.setEnableEndAgentEncryption((boolean) endConfig.get("enableEndAgentEncryption"));

            return Result.success("End config updated successfully", result);
        } catch (Exception e) {
            log.error("Error updating End config: {}", e.getMessage());
            return Result.error("Failed to update End config: " + e.getMessage());
        }
    }

    /**
     * 获取所有配置
     */
    @GetMapping("/all")
    public Result<AllConfigsResult> getAllConfigs() {
        log.info("Get all configs requested");

        try {
            AllConfigsResult result = new AllConfigsResult();
            
            // 构建Nexus配置
            NexusConfigResult nexusResult = new NexusConfigResult();
            nexusResult.setVersion((String) mcpConfig.get("version"));
            nexusResult.setServicePort((int) mcpConfig.get("servicePort"));
            nexusResult.setHeartbeatInterval((int) mcpConfig.get("heartbeatInterval"));
            nexusResult.setLogLevel((String) mcpConfig.get("logLevel"));
            nexusResult.setMaxConnections((int) mcpConfig.get("maxConnections"));
            nexusResult.setConnectionTimeout((int) mcpConfig.get("connectionTimeout"));
            nexusResult.setThreadPoolSize((int) mcpConfig.get("threadPoolSize"));
            nexusResult.setBufferSize((int) mcpConfig.get("bufferSize"));
            nexusResult.setEnableTls((boolean) mcpConfig.get("enableTls"));
            nexusResult.setEnableCompression((boolean) mcpConfig.get("enableCompression"));
            nexusResult.setEnableRateLimiting((boolean) mcpConfig.get("enableRateLimiting"));
            nexusResult.setRateLimitPerSecond((int) mcpConfig.get("rateLimitPerSecond"));
            nexusResult.setEnableCaching((boolean) mcpConfig.get("enableCaching"));
            nexusResult.setCacheExpiryTime((int) mcpConfig.get("cacheExpiryTime"));
            nexusResult.setEnableMetrics((boolean) mcpConfig.get("enableMetrics"));
            nexusResult.setMetricsInterval((int) mcpConfig.get("metricsInterval"));
            result.setNexus(nexusResult);
            
            // 构建Route配置
            RouteConfigResult routeResult = new RouteConfigResult();
            routeResult.setRouteRefreshInterval((int) routeConfig.get("routeRefreshInterval"));
            routeResult.setRouteTimeout((int) routeConfig.get("routeTimeout"));
            routeResult.setRouteMaxRetries((int) routeConfig.get("routeMaxRetries"));
            routeResult.setEnableRouteCaching((boolean) routeConfig.get("enableRouteCaching"));
            routeResult.setRouteCacheExpiry((int) routeConfig.get("routeCacheExpiry"));
            routeResult.setEnableRouteHealthCheck((boolean) routeConfig.get("enableRouteHealthCheck"));
            routeResult.setRouteHealthCheckInterval((int) routeConfig.get("routeHealthCheckInterval"));
            routeResult.setEnableRouteMetrics((boolean) routeConfig.get("enableRouteMetrics"));
            routeResult.setRouteMetricsInterval((int) routeConfig.get("routeMetricsInterval"));
            routeResult.setRoutePriorityOrder((java.util.List<String>) routeConfig.get("routePriorityOrder"));
            routeResult.setRouteBlacklist((java.util.List<String>) routeConfig.get("routeBlacklist"));
            routeResult.setRouteWhitelist((java.util.List<String>) routeConfig.get("routeWhitelist"));
            routeResult.setEnableRouteLoadBalancing((boolean) routeConfig.get("enableRouteLoadBalancing"));
            routeResult.setLoadBalancingStrategy((String) routeConfig.get("loadBalancingStrategy"));
            routeResult.setMaxRoutesPerAgent((int) routeConfig.get("maxRoutesPerAgent"));
            routeResult.setRouteQueueSize((int) routeConfig.get("routeQueueSize"));
            result.setRoute(routeResult);
            
            // 构建End配置
            EndConfigResult endResult = new EndConfigResult();
            endResult.setEndAgentDiscoveryInterval((int) endConfig.get("endAgentDiscoveryInterval"));
            endResult.setEndAgentHeartbeatInterval((int) endConfig.get("endAgentHeartbeatInterval"));
            endResult.setEndAgentTimeout((int) endConfig.get("endAgentTimeout"));
            endResult.setEndAgentMaxRetries((int) endConfig.get("endAgentMaxRetries"));
            endResult.setEnableEndAgentCaching((boolean) endConfig.get("enableEndAgentCaching"));
            endResult.setEndAgentCacheExpiry((int) endConfig.get("endAgentCacheExpiry"));
            endResult.setEnableEndAgentHealthCheck((boolean) endConfig.get("enableEndAgentHealthCheck"));
            endResult.setEndAgentHealthCheckInterval((int) endConfig.get("endAgentHealthCheckInterval"));
            endResult.setEnableEndAgentMetrics((boolean) endConfig.get("enableEndAgentMetrics"));
            endResult.setEndAgentMetricsInterval((int) endConfig.get("endAgentMetricsInterval"));
            endResult.setEnableEndAgentAutoDiscovery((boolean) endConfig.get("enableEndAgentAutoDiscovery"));
            endResult.setEnableEndAgentAutoRegistration((boolean) endConfig.get("enableEndAgentAutoRegistration"));
            endResult.setEndAgentMaxConcurrentTasks((int) endConfig.get("endAgentMaxConcurrentTasks"));
            endResult.setEndAgentQueueSize((int) endConfig.get("endAgentQueueSize"));
            endResult.setEndAgentCommandTimeout((int) endConfig.get("endAgentCommandTimeout"));
            endResult.setEndAgentMaxConnections((int) endConfig.get("endAgentMaxConnections"));
            endResult.setEnableEndAgentCompression((boolean) endConfig.get("enableEndAgentCompression"));
            endResult.setEnableEndAgentEncryption((boolean) endConfig.get("enableEndAgentEncryption"));
            result.setEnd(endResult);
            
            return Result.success("All configs retrieved successfully", result);
        } catch (Exception e) {
            log.error("Error getting all configs: {}", e.getMessage());
            return Result.error("Failed to get all configs: " + e.getMessage());
        }
    }

    /**
     * 重置配置为默认值
     */
    @PostMapping("/reset/{configType}")
    public Result<ConfigResetResult> resetConfig(@PathVariable String configType) {
        log.info("Reset config requested: configType={}", configType);

        try {
            ConfigResetResult result = new ConfigResetResult();
            result.setConfigType(configType);
            result.setSuccess(true);
            
            switch (configType.toLowerCase()) {
                case "nexus":
                    mcpConfig.clear();
                    initializeMcpConfig();
                    Map<String, Object> mcpResetMap = new HashMap<>();
                    mcpResetMap.put("action", "reset");
                    recordConfigChange("NEXUS", mcpResetMap);
                    
                    NexusConfigResult nexusResult = new NexusConfigResult();
                    nexusResult.setVersion((String) mcpConfig.get("version"));
                    nexusResult.setServicePort((int) mcpConfig.get("servicePort"));
                    nexusResult.setHeartbeatInterval((int) mcpConfig.get("heartbeatInterval"));
                    nexusResult.setLogLevel((String) mcpConfig.get("logLevel"));
                    nexusResult.setMaxConnections((int) mcpConfig.get("maxConnections"));
                    nexusResult.setConnectionTimeout((int) mcpConfig.get("connectionTimeout"));
                    nexusResult.setThreadPoolSize((int) mcpConfig.get("threadPoolSize"));
                    nexusResult.setBufferSize((int) mcpConfig.get("bufferSize"));
                    nexusResult.setEnableTls((boolean) mcpConfig.get("enableTls"));
                    nexusResult.setEnableCompression((boolean) mcpConfig.get("enableCompression"));
                    nexusResult.setEnableRateLimiting((boolean) mcpConfig.get("enableRateLimiting"));
                    nexusResult.setRateLimitPerSecond((int) mcpConfig.get("rateLimitPerSecond"));
                    nexusResult.setEnableCaching((boolean) mcpConfig.get("enableCaching"));
                    nexusResult.setCacheExpiryTime((int) mcpConfig.get("cacheExpiryTime"));
                    nexusResult.setEnableMetrics((boolean) mcpConfig.get("enableMetrics"));
                    nexusResult.setMetricsInterval((int) mcpConfig.get("metricsInterval"));
                    result.setNexus(nexusResult);
                    
                    return Result.success(configType + " config reset to default values successfully", result);
                case "route":
                    routeConfig.clear();
                    initializeRouteConfig();
                    Map<String, Object> routeResetMap = new HashMap<>();
                    routeResetMap.put("action", "reset");
                    recordConfigChange("Route", routeResetMap);
                    
                    RouteConfigResult routeResult = new RouteConfigResult();
                    routeResult.setRouteRefreshInterval((int) routeConfig.get("routeRefreshInterval"));
                    routeResult.setRouteTimeout((int) routeConfig.get("routeTimeout"));
                    routeResult.setRouteMaxRetries((int) routeConfig.get("routeMaxRetries"));
                    routeResult.setEnableRouteCaching((boolean) routeConfig.get("enableRouteCaching"));
                    routeResult.setRouteCacheExpiry((int) routeConfig.get("routeCacheExpiry"));
                    routeResult.setEnableRouteHealthCheck((boolean) routeConfig.get("enableRouteHealthCheck"));
                    routeResult.setRouteHealthCheckInterval((int) routeConfig.get("routeHealthCheckInterval"));
                    routeResult.setEnableRouteMetrics((boolean) routeConfig.get("enableRouteMetrics"));
                    routeResult.setRouteMetricsInterval((int) routeConfig.get("routeMetricsInterval"));
                    routeResult.setRoutePriorityOrder((java.util.List<String>) routeConfig.get("routePriorityOrder"));
                    routeResult.setRouteBlacklist((java.util.List<String>) routeConfig.get("routeBlacklist"));
                    routeResult.setRouteWhitelist((java.util.List<String>) routeConfig.get("routeWhitelist"));
                    routeResult.setEnableRouteLoadBalancing((boolean) routeConfig.get("enableRouteLoadBalancing"));
                    routeResult.setLoadBalancingStrategy((String) routeConfig.get("loadBalancingStrategy"));
                    routeResult.setMaxRoutesPerAgent((int) routeConfig.get("maxRoutesPerAgent"));
                    routeResult.setRouteQueueSize((int) routeConfig.get("routeQueueSize"));
                    result.setRoute(routeResult);
                    
                    return Result.success(configType + " config reset to default values successfully", result);
                case "end":
                    endConfig.clear();
                    initializeEndConfig();
                    Map<String, Object> endResetMap = new HashMap<>();
                    endResetMap.put("action", "reset");
                    recordConfigChange("End", endResetMap);
                    
                    EndConfigResult endResult = new EndConfigResult();
                    endResult.setEndAgentDiscoveryInterval((int) endConfig.get("endAgentDiscoveryInterval"));
                    endResult.setEndAgentHeartbeatInterval((int) endConfig.get("endAgentHeartbeatInterval"));
                    endResult.setEndAgentTimeout((int) endConfig.get("endAgentTimeout"));
                    endResult.setEndAgentMaxRetries((int) endConfig.get("endAgentMaxRetries"));
                    endResult.setEnableEndAgentCaching((boolean) endConfig.get("enableEndAgentCaching"));
                    endResult.setEndAgentCacheExpiry((int) endConfig.get("endAgentCacheExpiry"));
                    endResult.setEnableEndAgentHealthCheck((boolean) endConfig.get("enableEndAgentHealthCheck"));
                    endResult.setEndAgentHealthCheckInterval((int) endConfig.get("endAgentHealthCheckInterval"));
                    endResult.setEnableEndAgentMetrics((boolean) endConfig.get("enableEndAgentMetrics"));
                    endResult.setEndAgentMetricsInterval((int) endConfig.get("endAgentMetricsInterval"));
                    endResult.setEnableEndAgentAutoDiscovery((boolean) endConfig.get("enableEndAgentAutoDiscovery"));
                    endResult.setEnableEndAgentAutoRegistration((boolean) endConfig.get("enableEndAgentAutoRegistration"));
                    endResult.setEndAgentMaxConcurrentTasks((int) endConfig.get("endAgentMaxConcurrentTasks"));
                    endResult.setEndAgentQueueSize((int) endConfig.get("endAgentQueueSize"));
                    endResult.setEndAgentCommandTimeout((int) endConfig.get("endAgentCommandTimeout"));
                    endResult.setEndAgentMaxConnections((int) endConfig.get("endAgentMaxConnections"));
                    endResult.setEnableEndAgentCompression((boolean) endConfig.get("enableEndAgentCompression"));
                    endResult.setEnableEndAgentEncryption((boolean) endConfig.get("enableEndAgentEncryption"));
                    result.setEnd(endResult);
                    
                    return Result.success(configType + " config reset to default values successfully", result);
                case "all":
                    mcpConfig.clear();
                    routeConfig.clear();
                    endConfig.clear();
                    initializeDefaultConfigs();
                    Map<String, Object> allResetMap = new HashMap<>();
                    allResetMap.put("action", "reset");
                    recordConfigChange("All", allResetMap);
                    
                    // 构建Nexus配置
                    NexusConfigResult allNexusResult = new NexusConfigResult();
                    allNexusResult.setVersion((String) mcpConfig.get("version"));
                    allNexusResult.setServicePort((int) mcpConfig.get("servicePort"));
                    allNexusResult.setHeartbeatInterval((int) mcpConfig.get("heartbeatInterval"));
                    allNexusResult.setLogLevel((String) mcpConfig.get("logLevel"));
                    allNexusResult.setMaxConnections((int) mcpConfig.get("maxConnections"));
                    allNexusResult.setConnectionTimeout((int) mcpConfig.get("connectionTimeout"));
                    allNexusResult.setThreadPoolSize((int) mcpConfig.get("threadPoolSize"));
                    allNexusResult.setBufferSize((int) mcpConfig.get("bufferSize"));
                    allNexusResult.setEnableTls((boolean) mcpConfig.get("enableTls"));
                    allNexusResult.setEnableCompression((boolean) mcpConfig.get("enableCompression"));
                    allNexusResult.setEnableRateLimiting((boolean) mcpConfig.get("enableRateLimiting"));
                    allNexusResult.setRateLimitPerSecond((int) mcpConfig.get("rateLimitPerSecond"));
                    allNexusResult.setEnableCaching((boolean) mcpConfig.get("enableCaching"));
                    allNexusResult.setCacheExpiryTime((int) mcpConfig.get("cacheExpiryTime"));
                    allNexusResult.setEnableMetrics((boolean) mcpConfig.get("enableMetrics"));
                    allNexusResult.setMetricsInterval((int) mcpConfig.get("metricsInterval"));
                    result.setNexus(allNexusResult);
                    
                    // 构建Route配置
                    RouteConfigResult allRouteResult = new RouteConfigResult();
                    allRouteResult.setRouteRefreshInterval((int) routeConfig.get("routeRefreshInterval"));
                    allRouteResult.setRouteTimeout((int) routeConfig.get("routeTimeout"));
                    allRouteResult.setRouteMaxRetries((int) routeConfig.get("routeMaxRetries"));
                    allRouteResult.setEnableRouteCaching((boolean) routeConfig.get("enableRouteCaching"));
                    allRouteResult.setRouteCacheExpiry((int) routeConfig.get("routeCacheExpiry"));
                    allRouteResult.setEnableRouteHealthCheck((boolean) routeConfig.get("enableRouteHealthCheck"));
                    allRouteResult.setRouteHealthCheckInterval((int) routeConfig.get("routeHealthCheckInterval"));
                    allRouteResult.setEnableRouteMetrics((boolean) routeConfig.get("enableRouteMetrics"));
                    allRouteResult.setRouteMetricsInterval((int) routeConfig.get("routeMetricsInterval"));
                    allRouteResult.setRoutePriorityOrder((java.util.List<String>) routeConfig.get("routePriorityOrder"));
                    allRouteResult.setRouteBlacklist((java.util.List<String>) routeConfig.get("routeBlacklist"));
                    allRouteResult.setRouteWhitelist((java.util.List<String>) routeConfig.get("routeWhitelist"));
                    allRouteResult.setEnableRouteLoadBalancing((boolean) routeConfig.get("enableRouteLoadBalancing"));
                    allRouteResult.setLoadBalancingStrategy((String) routeConfig.get("loadBalancingStrategy"));
                    allRouteResult.setMaxRoutesPerAgent((int) routeConfig.get("maxRoutesPerAgent"));
                    allRouteResult.setRouteQueueSize((int) routeConfig.get("routeQueueSize"));
                    result.setRoute(allRouteResult);
                    
                    // 构建End配置
                    EndConfigResult allEndResult = new EndConfigResult();
                    allEndResult.setEndAgentDiscoveryInterval((int) endConfig.get("endAgentDiscoveryInterval"));
                    allEndResult.setEndAgentHeartbeatInterval((int) endConfig.get("endAgentHeartbeatInterval"));
                    allEndResult.setEndAgentTimeout((int) endConfig.get("endAgentTimeout"));
                    allEndResult.setEndAgentMaxRetries((int) endConfig.get("endAgentMaxRetries"));
                    allEndResult.setEnableEndAgentCaching((boolean) endConfig.get("enableEndAgentCaching"));
                    allEndResult.setEndAgentCacheExpiry((int) endConfig.get("endAgentCacheExpiry"));
                    allEndResult.setEnableEndAgentHealthCheck((boolean) endConfig.get("enableEndAgentHealthCheck"));
                    allEndResult.setEndAgentHealthCheckInterval((int) endConfig.get("endAgentHealthCheckInterval"));
                    allEndResult.setEnableEndAgentMetrics((boolean) endConfig.get("enableEndAgentMetrics"));
                    allEndResult.setEndAgentMetricsInterval((int) endConfig.get("endAgentMetricsInterval"));
                    allEndResult.setEnableEndAgentAutoDiscovery((boolean) endConfig.get("enableEndAgentAutoDiscovery"));
                    allEndResult.setEnableEndAgentAutoRegistration((boolean) endConfig.get("enableEndAgentAutoRegistration"));
                    allEndResult.setEndAgentMaxConcurrentTasks((int) endConfig.get("endAgentMaxConcurrentTasks"));
                    allEndResult.setEndAgentQueueSize((int) endConfig.get("endAgentQueueSize"));
                    allEndResult.setEndAgentCommandTimeout((int) endConfig.get("endAgentCommandTimeout"));
                    allEndResult.setEndAgentMaxConnections((int) endConfig.get("endAgentMaxConnections"));
                    allEndResult.setEnableEndAgentCompression((boolean) endConfig.get("enableEndAgentCompression"));
                    allEndResult.setEnableEndAgentEncryption((boolean) endConfig.get("enableEndAgentEncryption"));
                    result.setEnd(allEndResult);
                    
                    return Result.success(configType + " config reset to default values successfully", result);
                default:
                    return Result.error("Invalid config type: " + configType);
            }
        } catch (Exception e) {
            log.error("Error resetting config: {}", e.getMessage());
            return Result.error("Failed to reset config: " + e.getMessage());
        }
    }

    /**
     * 获取配置变更历史
     */
    @GetMapping("/history")
    public Result<List<ConfigChange>> getConfigHistory(
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(required = false) String configType) {
        log.info("Get config history requested: limit={}, configType={}", limit, configType);

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

            return Result.success("Config history retrieved successfully", filteredHistory);
        } catch (Exception e) {
            log.error("Error getting config history: {}", e.getMessage());
            return Result.error("Failed to get config history: " + e.getMessage());
        }
    }

    /**
     * 获取配置统计
     */
    @GetMapping("/stats")
    public Result<ConfigStatsResult> getConfigStats() {
        log.info("Get config stats requested");

        try {
            ConfigStatsResult result = new ConfigStatsResult();
            result.setNexusConfigCount(mcpConfig.size());
            result.setRouteConfigCount(routeConfig.size());
            result.setEndConfigCount(endConfig.size());
            result.setTotalConfigCount(mcpConfig.size() + routeConfig.size() + endConfig.size());
            result.setConfigChangeCount(configChanges.size());
            result.setLastConfigChange(configChanges.isEmpty() ? null : configChanges.get(configChanges.size() - 1));

            return Result.success("Config stats retrieved successfully", result);
        } catch (Exception e) {
            log.error("Error getting config stats: {}", e.getMessage());
            return Result.error("Failed to get config stats: " + e.getMessage());
        }
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


}
