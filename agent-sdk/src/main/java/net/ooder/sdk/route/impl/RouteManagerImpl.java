package net.ooder.sdk.route.impl;

import net.ooder.sdk.route.RouteManager;
import net.ooder.sdk.route.model.*;
import net.ooder.sdk.route.discovery.RouteDiscoverer;
import net.ooder.sdk.route.discovery.impl.NetworkRouteDiscoverer;
import net.ooder.sdk.route.calculator.RouteCalculator;
import net.ooder.sdk.route.calculator.impl.ShortestPathCalculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class RouteManagerImpl implements RouteManager {
    private static final Logger log = LoggerFactory.getLogger(RouteManagerImpl.class);
    private final Map<String, Route> routes = new ConcurrentHashMap<>();
    private final List<Consumer<RouteEvent>> eventHandlers = new CopyOnWriteArrayList<>();
    private final List<RouteEvent> recentEvents = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
    private final RouteDiscoverer routeDiscoverer;
    private final RouteCalculator routeCalculator;
    private boolean discoveryRunning = false;
    private boolean monitoringRunning = false;
    
    public RouteManagerImpl() {
        this.routeDiscoverer = new NetworkRouteDiscoverer(this);
        this.routeCalculator = new ShortestPathCalculator(this);
        startRouteMonitoring();
    }
    
    @Override
    public void startRouteDiscovery() {
        if (!discoveryRunning) {
            routeDiscoverer.startDiscovery();
            discoveryRunning = true;
        }
    }
    
    @Override
    public void stopRouteDiscovery() {
        if (discoveryRunning) {
            routeDiscoverer.stopDiscovery();
            discoveryRunning = false;
        }
    }
    
    @Override
    public boolean isRouteDiscoveryRunning() {
        return discoveryRunning;
    }
    
    @Override
    public List<Route> discoverRoutes() {
        return routeDiscoverer.discoverRoutes();
    }
    
    @Override
    public Route createRoute(String sourceId, String destinationId, List<String> path) {
        Route route = new Route(sourceId, destinationId, path);
        routes.put(route.getRouteId(), route);
        
        // 发布路由创建事件
        RouteEvent event = new RouteEvent(
            RouteEventType.ROUTE_CREATED,
            route.getRouteId(),
            Collections.singletonMap("route", route)
        );
        publishRouteEvent(event);
        
        return route;
    }
    
    @Override
    public void updateRoute(String routeId, Map<String, Object> updates) {
        Route route = routes.get(routeId);
        if (route != null) {
            // 应用更新
            if (updates.containsKey("sourceId")) {
                route.setSourceId((String) updates.get("sourceId"));
            }
            if (updates.containsKey("destinationId")) {
                route.setDestinationId((String) updates.get("destinationId"));
            }
            if (updates.containsKey("path")) {
                route.setPath((List<String>) updates.get("path"));
            }
            if (updates.containsKey("priority")) {
                route.setPriority((int) updates.get("priority"));
            }
            if (updates.containsKey("metadata")) {
                route.setMetadata((Map<String, Object>) updates.get("metadata"));
            }
            
            // 发布路由更新事件
            RouteEvent event = new RouteEvent(
                RouteEventType.ROUTE_UPDATED,
                routeId,
                Collections.singletonMap("updates", updates)
            );
            publishRouteEvent(event);
        }
    }
    
    @Override
    public void deleteRoute(String routeId) {
        Route route = routes.remove(routeId);
        if (route != null) {
            // 发布路由删除事件
            RouteEvent event = new RouteEvent(
                RouteEventType.ROUTE_DELETED,
                routeId,
                Collections.singletonMap("route", route)
            );
            publishRouteEvent(event);
        }
    }
    
    @Override
    public Route getRoute(String routeId) {
        return routes.get(routeId);
    }
    
    @Override
    public List<Route> getAllRoutes() {
        return new ArrayList<>(routes.values());
    }
    
    @Override
    public List<Route> getRoutesBySource(String sourceId) {
        return routes.values().stream()
            .filter(route -> route.getSourceId().equals(sourceId))
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<Route> getRoutesByDestination(String destinationId) {
        return routes.values().stream()
            .filter(route -> route.getDestinationId().equals(destinationId))
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<Route> getRoutesByStatus(RouteStatus status) {
        return routes.values().stream()
            .filter(route -> route.getStatus() == status)
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public Route calculateBestRoute(String sourceId, String destinationId) {
        return routeCalculator.calculateBestRoute(sourceId, destinationId);
    }
    
    @Override
    public List<Route> calculateMultipleRoutes(String sourceId, String destinationId, int maxRoutes) {
        return routeCalculator.calculateMultipleRoutes(sourceId, destinationId, maxRoutes);
    }
    
    @Override
    public void updateRouteStatus(String routeId, RouteStatus status) {
        Route route = routes.get(routeId);
        if (route != null) {
            RouteStatus oldStatus = route.getStatus();
            route.setStatus(status);
            
            // 发布状态变更事件
            if (oldStatus != status) {
                RouteEvent event = new RouteEvent(
                    RouteEventType.ROUTE_STATUS_CHANGED,
                    routeId,
                    new HashMap<String, Object>() {{
                        put("oldStatus", oldStatus);
                        put("newStatus", status);
                    }}
                );
                publishRouteEvent(event);
            }
        }
    }
    
    @Override
    public void updateRouteMetrics(String routeId, RouteMetrics metrics) {
        Route route = routes.get(routeId);
        if (route != null) {
            route.setMetrics(metrics);
            
            // 发布度量更新事件
            RouteEvent event = new RouteEvent(
                RouteEventType.ROUTE_METRICS_UPDATED,
                routeId,
                Collections.singletonMap("metrics", metrics)
            );
            publishRouteEvent(event);
        }
    }
    
    @Override
    public void syncRouteStatus(String routeId) {
        Route route = routes.get(routeId);
        if (route == null) {
            log.warn("Route not found for status sync: {}", routeId);
            return;
        }
        
        try {
            RouteMetrics metrics = route.getMetrics();
            if (metrics == null) {
                metrics = new RouteMetrics();
                route.setMetrics(metrics);
            }
            
            RouteStatus currentStatus = route.getStatus();
            RouteStatus newStatus = currentStatus;
            
            if (route.getPath() == null || route.getPath().isEmpty()) {
                newStatus = RouteStatus.UNAVAILABLE;
            } else {
                double latency = metrics.getLatency();
                double packetLoss = metrics.getPacketLoss();
                
                if (packetLoss > 0.5 || latency > 5000) {
                    newStatus = RouteStatus.UNAVAILABLE;
                } else if (packetLoss > 0.2 || latency > 2000) {
                    newStatus = RouteStatus.DEGRADED;
                } else {
                    newStatus = RouteStatus.AVAILABLE;
                }
            }
            
            if (currentStatus != newStatus) {
                route.setStatus(newStatus);
                metrics.setLastUpdated(System.currentTimeMillis());
                
                final RouteStatus finalOldStatus = currentStatus;
                final RouteStatus finalNewStatus = newStatus;
                RouteEvent event = new RouteEvent(
                    RouteEventType.ROUTE_STATUS_CHANGED,
                    routeId,
                    new HashMap<String, Object>() {{
                        put("oldStatus", finalOldStatus);
                        put("newStatus", finalNewStatus);
                        put("syncTime", System.currentTimeMillis());
                    }}
                );
                publishRouteEvent(event);
                
                log.info("Route status synced: {} -> {} for route {}", currentStatus, newStatus, routeId);
            }
            
            route.setMetrics(metrics);
            
        } catch (Exception e) {
            log.error("Failed to sync route status for {}: {}", routeId, e.getMessage());
        }
    }
    
    @Override
    public void publishRouteEvent(RouteEvent event) {
        recentEvents.add(event);
        // 只保留最近100个事件
        if (recentEvents.size() > 100) {
            recentEvents.remove(0);
        }
        
        // 通知所有事件处理器
        for (Consumer<RouteEvent> handler : eventHandlers) {
            try {
                handler.accept(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void subscribeToRouteEvents(Consumer<RouteEvent> eventHandler) {
        eventHandlers.add(eventHandler);
    }
    
    @Override
    public void unsubscribeFromRouteEvents(Consumer<RouteEvent> eventHandler) {
        eventHandlers.remove(eventHandler);
    }
    
    @Override
    public List<RouteEvent> getRecentRouteEvents(int limit) {
        int size = Math.min(limit, recentEvents.size());
        return recentEvents.subList(recentEvents.size() - size, recentEvents.size());
    }
    
    @Override
    public void startRouteMonitoring() {
        if (!monitoringRunning) {
            // 定期检查路由状态
            executorService.scheduleAtFixedRate(this::checkRouteStatus, 0, 30, TimeUnit.SECONDS);
            // 定期更新路由度量
            executorService.scheduleAtFixedRate(this::updateRouteMetrics, 0, 60, TimeUnit.SECONDS);
            monitoringRunning = true;
        }
    }
    
    @Override
    public void stopRouteMonitoring() {
        if (monitoringRunning) {
            executorService.shutdown();
            monitoringRunning = false;
        }
    }
    
    @Override
    public Map<String, Object> getRouteStats(String routeId) {
        Route route = routes.get(routeId);
        if (route == null) {
            return Collections.emptyMap();
        }
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("routeId", route.getRouteId());
        stats.put("sourceId", route.getSourceId());
        stats.put("destinationId", route.getDestinationId());
        stats.put("status", route.getStatus());
        stats.put("priority", route.getPriority());
        stats.put("metrics", route.getMetrics());
        stats.put("createdAt", route.getCreatedAt());
        stats.put("updatedAt", route.getUpdatedAt());
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getOverallRouteStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRoutes", routes.size());
        stats.put("availableRoutes", getRoutesByStatus(RouteStatus.AVAILABLE).size());
        stats.put("unavailableRoutes", getRoutesByStatus(RouteStatus.UNAVAILABLE).size());
        stats.put("activeRoutes", getRoutesByStatus(RouteStatus.ACTIVE).size());
        stats.put("discoveryRunning", discoveryRunning);
        stats.put("monitoringRunning", monitoringRunning);
        
        return stats;
    }
    
    @Override
    public void handleTopologyChange() {
        // 处理拓扑变更
        publishRouteEvent(new RouteEvent(
            RouteEventType.TOPOLOGY_CHANGED,
            null,
            Collections.singletonMap("timestamp", System.currentTimeMillis())
        ));
        
        // 重新构建路由
        rebuildRoutes();
    }
    
    @Override
    public void rebuildRoutes() {
        log.info("Rebuilding all routes");
        
        List<Route> oldRoutes = new ArrayList<>(routes.values());
        routes.clear();
        
        for (Route oldRoute : oldRoutes) {
            try {
                Route newRoute = routeCalculator.calculateBestRoute(
                    oldRoute.getSourceId(), 
                    oldRoute.getDestinationId()
                );
                if (newRoute != null) {
                    routes.put(newRoute.getRouteId(), newRoute);
                    
                    RouteEvent event = new RouteEvent(
                        RouteEventType.ROUTE_UPDATED,
                        newRoute.getRouteId(),
                        Collections.singletonMap("route", newRoute)
                    );
                    publishRouteEvent(event);
                }
            } catch (Exception e) {
                log.warn("Failed to rebuild route from {} to {}: {}", 
                    oldRoute.getSourceId(), oldRoute.getDestinationId(), e.getMessage());
            }
        }
        
        log.info("Route rebuild completed: {} routes rebuilt", routes.size());
    }
    
    private void checkRouteStatus() {
        log.debug("Checking route status for {} routes", routes.size());
        
        for (Map.Entry<String, Route> entry : routes.entrySet()) {
            Route route = entry.getValue();
            try {
                RouteStatus currentStatus = route.getStatus();
                
                if (route.getPath() == null || route.getPath().isEmpty()) {
                    if (currentStatus != RouteStatus.UNAVAILABLE) {
                        route.setStatus(RouteStatus.UNAVAILABLE);
                        publishRouteEvent(new RouteEvent(
                            RouteEventType.ROUTE_STATUS_CHANGED,
                            route.getRouteId(),
                            new HashMap<String, Object>() {{
                                put("oldStatus", currentStatus);
                                put("newStatus", RouteStatus.UNAVAILABLE);
                            }}
                        ));
                    }
                    continue;
                }
                
                RouteMetrics metrics = route.getMetrics();
                if (metrics != null) {
                    double latency = metrics.getLatency();
                    double lossRate = metrics.getPacketLoss();
                    
                    RouteStatus newStatus;
                    if (lossRate > 0.5 || latency > 5000) {
                        newStatus = RouteStatus.UNAVAILABLE;
                    } else if (lossRate > 0.2 || latency > 2000) {
                        newStatus = RouteStatus.DEGRADED;
                    } else {
                        newStatus = RouteStatus.AVAILABLE;
                    }
                    
                    if (currentStatus != newStatus) {
                        route.setStatus(newStatus);
                        publishRouteEvent(new RouteEvent(
                            RouteEventType.ROUTE_STATUS_CHANGED,
                            route.getRouteId(),
                            new HashMap<String, Object>() {{
                                put("oldStatus", currentStatus);
                                put("newStatus", newStatus);
                            }}
                        ));
                    }
                }
            } catch (Exception e) {
                log.warn("Error checking status for route {}: {}", route.getRouteId(), e.getMessage());
            }
        }
    }
    
    private void updateRouteMetrics() {
        log.debug("Updating route metrics for {} routes", routes.size());
        
        for (Map.Entry<String, Route> entry : routes.entrySet()) {
            Route route = entry.getValue();
            try {
                RouteMetrics metrics = route.getMetrics();
                if (metrics == null) {
                    metrics = new RouteMetrics();
                    route.setMetrics(metrics);
                }
                
                metrics.setLastUpdated(System.currentTimeMillis());
                route.setMetrics(metrics);
                
            } catch (Exception e) {
                log.warn("Error updating metrics for route {}: {}", route.getRouteId(), e.getMessage());
            }
        }
    }
    
    // 获取路由计算器（用于其他组件访问）
    public RouteCalculator getRouteCalculator() {
        return routeCalculator;
    }
}
