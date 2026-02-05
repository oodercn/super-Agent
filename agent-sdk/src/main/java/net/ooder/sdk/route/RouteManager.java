package net.ooder.sdk.route;

import net.ooder.sdk.route.model.Route;
import net.ooder.sdk.route.model.RouteEvent;
import net.ooder.sdk.route.model.RouteMetrics;
import net.ooder.sdk.route.model.RouteStatus;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface RouteManager {
    // 路由发现相关方法
    void startRouteDiscovery();
    void stopRouteDiscovery();
    boolean isRouteDiscoveryRunning();
    List<Route> discoverRoutes();
    
    // 路由管理相关方法
    Route createRoute(String sourceId, String destinationId, List<String> path);
    void updateRoute(String routeId, Map<String, Object> updates);
    void deleteRoute(String routeId);
    Route getRoute(String routeId);
    List<Route> getAllRoutes();
    List<Route> getRoutesBySource(String sourceId);
    List<Route> getRoutesByDestination(String destinationId);
    List<Route> getRoutesByStatus(RouteStatus status);
    
    // 最优路由计算相关方法
    Route calculateBestRoute(String sourceId, String destinationId);
    List<Route> calculateMultipleRoutes(String sourceId, String destinationId, int maxRoutes);
    
    // 路由状态管理相关方法
    void updateRouteStatus(String routeId, RouteStatus status);
    void updateRouteMetrics(String routeId, RouteMetrics metrics);
    void syncRouteStatus(String routeId);
    
    // 路由事件相关方法
    void publishRouteEvent(RouteEvent event);
    void subscribeToRouteEvents(Consumer<RouteEvent> eventHandler);
    void unsubscribeFromRouteEvents(Consumer<RouteEvent> eventHandler);
    List<RouteEvent> getRecentRouteEvents(int limit);
    
    // 路由监控相关方法
    void startRouteMonitoring();
    void stopRouteMonitoring();
    Map<String, Object> getRouteStats(String routeId);
    Map<String, Object> getOverallRouteStats();
    
    // 拓扑变化处理相关方法
    void handleTopologyChange();
    void rebuildRoutes();
}
