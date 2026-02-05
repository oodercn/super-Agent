package net.ooder.sdk.persistence.impl;

import net.ooder.sdk.persistence.StorageManager;
import net.ooder.sdk.persistence.RouteStorage;
import net.ooder.sdk.route.model.Route;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class RouteStorageImpl implements RouteStorage {
    private static final String ROUTE_PREFIX = "route_";
    private final StorageManager storageManager;
    
    public RouteStorageImpl(StorageManager storageManager) {
        this.storageManager = storageManager;
    }
    
    @Override
    public CompletableFuture<Boolean> saveRoute(Route route) {
        String key = ROUTE_PREFIX + route.getRouteId();
        return storageManager.save(key, route);
    }
    
    @Override
    public CompletableFuture<Boolean> saveRoutes(List<Route> routes) {
        Map<String, Route> entries = new HashMap<>();
        for (Route route : routes) {
            String key = ROUTE_PREFIX + route.getRouteId();
            entries.put(key, route);
        }
        return storageManager.saveAll(entries);
    }
    
    @Override
    public CompletableFuture<Route> loadRoute(String routeId) {
        String key = ROUTE_PREFIX + routeId;
        return storageManager.load(key, Route.class);
    }
    
    @Override
    public CompletableFuture<List<Route>> loadAllRoutes() {
        return storageManager.loadAll(Route.class)
            .thenApply(map -> new ArrayList<>(map.values()));
    }
    
    @Override
    public CompletableFuture<List<Route>> loadRoutesBySource(String source) {
        return loadAllRoutes()
            .thenApply(routes -> routes.stream()
                .filter(route -> source.equals(route.getSourceId()))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<List<Route>> loadRoutesByDestination(String destination) {
        return loadAllRoutes()
            .thenApply(routes -> routes.stream()
                .filter(route -> destination.equals(route.getDestinationId()))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<List<Route>> loadRoutesByStatus(String status) {
        return loadAllRoutes()
            .thenApply(routes -> routes.stream()
                .filter(route -> status.equals(route.getStatus().toString()))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<Boolean> deleteRoute(String routeId) {
        String key = ROUTE_PREFIX + routeId;
        return storageManager.delete(key);
    }
    
    @Override
    public CompletableFuture<Boolean> deleteRoutes(List<String> routeIds) {
        List<String> keys = routeIds.stream()
            .map(id -> ROUTE_PREFIX + id)
            .collect(Collectors.toList());
        return storageManager.deleteAll(keys);
    }
    
    @Override
    public CompletableFuture<Boolean> deleteAllRoutes() {
        return loadAllRoutes()
            .thenCompose(routes -> {
                List<String> keys = routes.stream()
                    .map(route -> ROUTE_PREFIX + route.getRouteId())
                    .collect(Collectors.toList());
                return storageManager.deleteAll(keys);
            });
    }
    
    @Override
    public CompletableFuture<Boolean> existsRoute(String routeId) {
        String key = ROUTE_PREFIX + routeId;
        return storageManager.exists(key);
    }
    
    @Override
    public CompletableFuture<Long> countRoutes() {
        return loadAllRoutes()
            .thenApply(routes -> (long) routes.size());
    }
    
    @Override
    public CompletableFuture<Long> countRoutesByStatus(String status) {
        return loadRoutesByStatus(status)
            .thenApply(routes -> (long) routes.size());
    }
    
    @Override
    public CompletableFuture<Route> findRoute(String source, String destination) {
        return loadAllRoutes()
            .thenApply(routes -> routes.stream()
                .filter(route -> source.equals(route.getSourceId()) && destination.equals(route.getDestinationId()))
                .findFirst()
                .orElse(null));
    }
    
    @Override
    public CompletableFuture<List<Route>> findRoutes(Map<String, Object> criteria) {
        return loadAllRoutes()
            .thenApply(routes -> routes.stream()
                .filter(route -> matchesCriteria(route, criteria))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<List<Route>> findRoutesByAttribute(String attributeName, Object attributeValue) {
        return loadAllRoutes()
            .thenApply(routes -> routes.stream()
                .filter(route -> hasAttribute(route, attributeName, attributeValue))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<Map<String, Long>> getRouteStatusCounts() {
        return loadAllRoutes()
            .thenApply(routes -> {
                Map<String, Long> counts = new HashMap<>();
                for (Route route : routes) {
                    String status = route.getStatus().toString();
                    counts.put(status, counts.getOrDefault(status, 0L) + 1);
                }
                return counts;
            });
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> getRouteStatistics() {
        return loadAllRoutes()
            .thenApply(routes -> {
                Map<String, Object> stats = new HashMap<>();
                stats.put("totalRoutes", routes.size());
                
                Map<String, Long> statusCounts = new HashMap<>();
                for (Route route : routes) {
                    String status = route.getStatus().toString();
                    statusCounts.put(status, statusCounts.getOrDefault(status, 0L) + 1);
                }
                
                stats.put("statusCounts", statusCounts);
                
                return stats;
            });
    }
    
    @Override
    public CompletableFuture<Boolean> updateRouteStatus(String routeId, String status) {
        return loadRoute(routeId)
            .thenCompose(route -> {
                if (route == null) {
                    return CompletableFuture.completedFuture(false);
                }
                try {
                    route.setStatus(net.ooder.sdk.route.model.RouteStatus.valueOf(status.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return CompletableFuture.completedFuture(false);
                }
                return saveRoute(route);
            });
    }
    
    @Override
    public CompletableFuture<Boolean> updateRouteMetrics(String routeId, Map<String, Object> metrics) {
        return loadRoute(routeId)
            .thenCompose(route -> {
                if (route == null) {
                    return CompletableFuture.completedFuture(false);
                }
                net.ooder.sdk.route.model.RouteMetrics routeMetrics = new net.ooder.sdk.route.model.RouteMetrics();
                if (metrics != null) {
                    if (metrics.containsKey("latency")) {
                        routeMetrics.setLatency(((Number) metrics.get("latency")).longValue());
                    }
                    if (metrics.containsKey("bandwidth")) {
                        routeMetrics.setBandwidth(((Number) metrics.get("bandwidth")).intValue());
                    }
                    if (metrics.containsKey("packetLoss")) {
                        routeMetrics.setPacketLoss(((Number) metrics.get("packetLoss")).doubleValue());
                    }
                    if (metrics.containsKey("jitter")) {
                        routeMetrics.setJitter(((Number) metrics.get("jitter")).longValue());
                    }
                }
                route.setMetrics(routeMetrics);
                return saveRoute(route);
            });
    }
    
    @Override
    public CompletableFuture<Boolean> updateRouteAttributes(String routeId, Map<String, Object> attributes) {
        return loadRoute(routeId)
            .thenCompose(route -> {
                if (route == null) {
                    return CompletableFuture.completedFuture(false);
                }
                
                // 更新属性
                if (attributes.containsKey("source")) {
                    route.setSourceId((String) attributes.get("source"));
                }
                if (attributes.containsKey("destination")) {
                    route.setDestinationId((String) attributes.get("destination"));
                }
                if (attributes.containsKey("path")) {
                    route.setPath((List<String>) attributes.get("path"));
                }
                if (attributes.containsKey("status")) {
                    try {
                        route.setStatus(net.ooder.sdk.route.model.RouteStatus.valueOf(((String) attributes.get("status")).toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        // 忽略无效的状态值
                    }
                }
                if (attributes.containsKey("metrics")) {
                    net.ooder.sdk.route.model.RouteMetrics routeMetrics = new net.ooder.sdk.route.model.RouteMetrics();
                    Map<String, Object> metricsMap = (Map<String, Object>) attributes.get("metrics");
                    if (metricsMap != null) {
                        if (metricsMap.containsKey("latency")) {
                            routeMetrics.setLatency(((Number) metricsMap.get("latency")).longValue());
                        }
                        if (metricsMap.containsKey("bandwidth")) {
                            routeMetrics.setBandwidth(((Number) metricsMap.get("bandwidth")).intValue());
                        }
                        if (metricsMap.containsKey("packetLoss")) {
                            routeMetrics.setPacketLoss(((Number) metricsMap.get("packetLoss")).doubleValue());
                        }
                        if (metricsMap.containsKey("jitter")) {
                            routeMetrics.setJitter(((Number) metricsMap.get("jitter")).longValue());
                        }
                    }
                    route.setMetrics(routeMetrics);
                }
                
                return saveRoute(route);
            });
    }
    
    // 辅助方法：检查路由是否匹配查询条件
    private boolean matchesCriteria(Route route, Map<String, Object> criteria) {
        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            switch (key) {
                case "routeId":
                    if (!route.getRouteId().equals(value)) {
                        return false;
                    }
                    break;
                case "source":
                    if (!route.getSourceId().equals(value)) {
                        return false;
                    }
                    break;
                case "destination":
                    if (!route.getDestinationId().equals(value)) {
                        return false;
                    }
                    break;
                case "status":
                    if (!route.getStatus().toString().equals(value)) {
                        return false;
                    }
                    break;
                // 可以添加更多属性的匹配逻辑
            }
        }
        return true;
    }
    
    // 辅助方法：检查路由是否有指定的属性值
    private boolean hasAttribute(Route route, String attributeName, Object attributeValue) {
        switch (attributeName) {
            case "routeId":
                return route.getRouteId().equals(attributeValue);
            case "source":
                return route.getSourceId().equals(attributeValue);
            case "destination":
                return route.getDestinationId().equals(attributeValue);
            case "status":
                return route.getStatus().toString().equals(attributeValue);
            case "path":
                return route.getPath().equals(attributeValue);
            case "metrics":
                return route.getMetrics().equals(attributeValue);
            default:
                return false;
        }
    }
}
