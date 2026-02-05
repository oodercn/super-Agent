package net.ooder.sdk.persistence;

import net.ooder.sdk.route.model.Route;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface RouteStorage {
    // 存储路由
    CompletableFuture<Boolean> saveRoute(Route route);
    CompletableFuture<Boolean> saveRoutes(List<Route> routes);
    
    // 加载路由
    CompletableFuture<Route> loadRoute(String routeId);
    CompletableFuture<List<Route>> loadAllRoutes();
    CompletableFuture<List<Route>> loadRoutesBySource(String source);
    CompletableFuture<List<Route>> loadRoutesByDestination(String destination);
    CompletableFuture<List<Route>> loadRoutesByStatus(String status);
    
    // 删除路由
    CompletableFuture<Boolean> deleteRoute(String routeId);
    CompletableFuture<Boolean> deleteRoutes(List<String> routeIds);
    CompletableFuture<Boolean> deleteAllRoutes();
    
    // 检查路由
    CompletableFuture<Boolean> existsRoute(String routeId);
    CompletableFuture<Long> countRoutes();
    CompletableFuture<Long> countRoutesByStatus(String status);
    
    // 路由查询
    CompletableFuture<Route> findRoute(String source, String destination);
    CompletableFuture<List<Route>> findRoutes(Map<String, Object> criteria);
    CompletableFuture<List<Route>> findRoutesByAttribute(String attributeName, Object attributeValue);
    
    // 路由统计
    CompletableFuture<Map<String, Long>> getRouteStatusCounts();
    CompletableFuture<Map<String, Object>> getRouteStatistics();
    
    // 路由更新
    CompletableFuture<Boolean> updateRouteStatus(String routeId, String status);
    CompletableFuture<Boolean> updateRouteMetrics(String routeId, Map<String, Object> metrics);
    CompletableFuture<Boolean> updateRouteAttributes(String routeId, Map<String, Object> attributes);
}
