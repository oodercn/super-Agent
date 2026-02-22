package net.ooder.nexus.dto.config;

import java.io.Serializable;
import java.util.List;

/**
 * Route config update request DTO
 */
public class RouteConfigUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer routeRefreshInterval;
    private Integer routeTimeout;
    private Integer routeMaxRetries;
    private Boolean enableRouteCaching;
    private Integer routeCacheExpiry;
    private Boolean enableRouteHealthCheck;
    private Integer routeHealthCheckInterval;
    private Boolean enableRouteMetrics;
    private Integer routeMetricsInterval;
    private List<String> routePriorityOrder;
    private List<String> routeBlacklist;
    private List<String> routeWhitelist;
    private Boolean enableRouteLoadBalancing;
    private String loadBalancingStrategy;
    private Integer maxRoutesPerAgent;
    private Integer routeQueueSize;

    public Integer getRouteRefreshInterval() { return routeRefreshInterval; }
    public void setRouteRefreshInterval(Integer routeRefreshInterval) { this.routeRefreshInterval = routeRefreshInterval; }
    public Integer getRouteTimeout() { return routeTimeout; }
    public void setRouteTimeout(Integer routeTimeout) { this.routeTimeout = routeTimeout; }
    public Integer getRouteMaxRetries() { return routeMaxRetries; }
    public void setRouteMaxRetries(Integer routeMaxRetries) { this.routeMaxRetries = routeMaxRetries; }
    public Boolean getEnableRouteCaching() { return enableRouteCaching; }
    public void setEnableRouteCaching(Boolean enableRouteCaching) { this.enableRouteCaching = enableRouteCaching; }
    public Integer getRouteCacheExpiry() { return routeCacheExpiry; }
    public void setRouteCacheExpiry(Integer routeCacheExpiry) { this.routeCacheExpiry = routeCacheExpiry; }
    public Boolean getEnableRouteHealthCheck() { return enableRouteHealthCheck; }
    public void setEnableRouteHealthCheck(Boolean enableRouteHealthCheck) { this.enableRouteHealthCheck = enableRouteHealthCheck; }
    public Integer getRouteHealthCheckInterval() { return routeHealthCheckInterval; }
    public void setRouteHealthCheckInterval(Integer routeHealthCheckInterval) { this.routeHealthCheckInterval = routeHealthCheckInterval; }
    public Boolean getEnableRouteMetrics() { return enableRouteMetrics; }
    public void setEnableRouteMetrics(Boolean enableRouteMetrics) { this.enableRouteMetrics = enableRouteMetrics; }
    public Integer getRouteMetricsInterval() { return routeMetricsInterval; }
    public void setRouteMetricsInterval(Integer routeMetricsInterval) { this.routeMetricsInterval = routeMetricsInterval; }
    public List<String> getRoutePriorityOrder() { return routePriorityOrder; }
    public void setRoutePriorityOrder(List<String> routePriorityOrder) { this.routePriorityOrder = routePriorityOrder; }
    public List<String> getRouteBlacklist() { return routeBlacklist; }
    public void setRouteBlacklist(List<String> routeBlacklist) { this.routeBlacklist = routeBlacklist; }
    public List<String> getRouteWhitelist() { return routeWhitelist; }
    public void setRouteWhitelist(List<String> routeWhitelist) { this.routeWhitelist = routeWhitelist; }
    public Boolean getEnableRouteLoadBalancing() { return enableRouteLoadBalancing; }
    public void setEnableRouteLoadBalancing(Boolean enableRouteLoadBalancing) { this.enableRouteLoadBalancing = enableRouteLoadBalancing; }
    public String getLoadBalancingStrategy() { return loadBalancingStrategy; }
    public void setLoadBalancingStrategy(String loadBalancingStrategy) { this.loadBalancingStrategy = loadBalancingStrategy; }
    public Integer getMaxRoutesPerAgent() { return maxRoutesPerAgent; }
    public void setMaxRoutesPerAgent(Integer maxRoutesPerAgent) { this.maxRoutesPerAgent = maxRoutesPerAgent; }
    public Integer getRouteQueueSize() { return routeQueueSize; }
    public void setRouteQueueSize(Integer routeQueueSize) { this.routeQueueSize = routeQueueSize; }
}
