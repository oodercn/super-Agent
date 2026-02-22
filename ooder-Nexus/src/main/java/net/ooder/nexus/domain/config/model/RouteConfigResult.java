package net.ooder.nexus.domain.config.model;

import java.io.Serializable;
import java.util.List;

/**
 * Route配置结果
 * 用于ConfigController中getRouteConfig和updateRouteConfig方法的返回类型
 */
public class RouteConfigResult implements Serializable {
    private int routeRefreshInterval;
    private int routeTimeout;
    private int routeMaxRetries;
    private boolean enableRouteCaching;
    private int routeCacheExpiry;
    private boolean enableRouteHealthCheck;
    private int routeHealthCheckInterval;
    private boolean enableRouteMetrics;
    private int routeMetricsInterval;
    private List<String> routePriorityOrder;
    private List<String> routeBlacklist;
    private List<String> routeWhitelist;
    private boolean enableRouteLoadBalancing;
    private String loadBalancingStrategy;
    private int maxRoutesPerAgent;
    private int routeQueueSize;

    public int getRouteRefreshInterval() {
        return routeRefreshInterval;
    }

    public void setRouteRefreshInterval(int routeRefreshInterval) {
        this.routeRefreshInterval = routeRefreshInterval;
    }

    public int getRouteTimeout() {
        return routeTimeout;
    }

    public void setRouteTimeout(int routeTimeout) {
        this.routeTimeout = routeTimeout;
    }

    public int getRouteMaxRetries() {
        return routeMaxRetries;
    }

    public void setRouteMaxRetries(int routeMaxRetries) {
        this.routeMaxRetries = routeMaxRetries;
    }

    public boolean isEnableRouteCaching() {
        return enableRouteCaching;
    }

    public void setEnableRouteCaching(boolean enableRouteCaching) {
        this.enableRouteCaching = enableRouteCaching;
    }

    public int getRouteCacheExpiry() {
        return routeCacheExpiry;
    }

    public void setRouteCacheExpiry(int routeCacheExpiry) {
        this.routeCacheExpiry = routeCacheExpiry;
    }

    public boolean isEnableRouteHealthCheck() {
        return enableRouteHealthCheck;
    }

    public void setEnableRouteHealthCheck(boolean enableRouteHealthCheck) {
        this.enableRouteHealthCheck = enableRouteHealthCheck;
    }

    public int getRouteHealthCheckInterval() {
        return routeHealthCheckInterval;
    }

    public void setRouteHealthCheckInterval(int routeHealthCheckInterval) {
        this.routeHealthCheckInterval = routeHealthCheckInterval;
    }

    public boolean isEnableRouteMetrics() {
        return enableRouteMetrics;
    }

    public void setEnableRouteMetrics(boolean enableRouteMetrics) {
        this.enableRouteMetrics = enableRouteMetrics;
    }

    public int getRouteMetricsInterval() {
        return routeMetricsInterval;
    }

    public void setRouteMetricsInterval(int routeMetricsInterval) {
        this.routeMetricsInterval = routeMetricsInterval;
    }

    public List<String> getRoutePriorityOrder() {
        return routePriorityOrder;
    }

    public void setRoutePriorityOrder(List<String> routePriorityOrder) {
        this.routePriorityOrder = routePriorityOrder;
    }

    public List<String> getRouteBlacklist() {
        return routeBlacklist;
    }

    public void setRouteBlacklist(List<String> routeBlacklist) {
        this.routeBlacklist = routeBlacklist;
    }

    public List<String> getRouteWhitelist() {
        return routeWhitelist;
    }

    public void setRouteWhitelist(List<String> routeWhitelist) {
        this.routeWhitelist = routeWhitelist;
    }

    public boolean isEnableRouteLoadBalancing() {
        return enableRouteLoadBalancing;
    }

    public void setEnableRouteLoadBalancing(boolean enableRouteLoadBalancing) {
        this.enableRouteLoadBalancing = enableRouteLoadBalancing;
    }

    public String getLoadBalancingStrategy() {
        return loadBalancingStrategy;
    }

    public void setLoadBalancingStrategy(String loadBalancingStrategy) {
        this.loadBalancingStrategy = loadBalancingStrategy;
    }

    public int getMaxRoutesPerAgent() {
        return maxRoutesPerAgent;
    }

    public void setMaxRoutesPerAgent(int maxRoutesPerAgent) {
        this.maxRoutesPerAgent = maxRoutesPerAgent;
    }

    public int getRouteQueueSize() {
        return routeQueueSize;
    }

    public void setRouteQueueSize(int routeQueueSize) {
        this.routeQueueSize = routeQueueSize;
    }
}