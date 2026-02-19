
package net.ooder.sdk.service.network.route;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.service.network.route.model.Route;
import net.ooder.sdk.service.network.route.model.RouteInfo;

public class RouteManager {
    
    private static final Logger log = LoggerFactory.getLogger(RouteManager.class);
    
    private final Map<String, Route> routes = new java.util.concurrent.ConcurrentHashMap<>();
    private RouteCalculator calculator;
    private RouteDiscoverer discoverer;
    
    public RouteManager() {
        this.calculator = new ShortestPathCalculator();
    }
    
    public void addRoute(Route route) {
        routes.put(route.getRouteId(), route);
        log.info("Route added: {}", route.getRouteId());
    }
    
    public void removeRoute(String routeId) {
        routes.remove(routeId);
        log.info("Route removed: {}", routeId);
    }
    
    public Route getRoute(String routeId) {
        return routes.get(routeId);
    }
    
    public List<Route> getAllRoutes() {
        return new java.util.ArrayList<>(routes.values());
    }
    
    public CompletableFuture<Route> calculateRoute(String sourceId, String destinationId) {
        return CompletableFuture.supplyAsync(() -> {
            return calculator.calculate(sourceId, destinationId, routes.values());
        });
    }
    
    public CompletableFuture<List<Route>> discoverRoutes() {
        if (discoverer == null) {
            return CompletableFuture.completedFuture(new java.util.ArrayList<>());
        }
        return discoverer.discover();
    }
    
    public void setCalculator(RouteCalculator calculator) {
        this.calculator = calculator;
    }
    
    public void setDiscoverer(RouteDiscoverer discoverer) {
        this.discoverer = discoverer;
    }
    
    public RouteInfo getRouteInfo(String routeId) {
        Route route = routes.get(routeId);
        if (route == null) {
            return null;
        }
        
        RouteInfo info = new RouteInfo();
        info.setRouteId(routeId);
        info.setSourceId(route.getSourceId());
        info.setDestinationId(route.getDestinationId());
        info.setHopCount(route.getHops() != null ? route.getHops().size() : 0);
        info.setStatus(route.getStatus());
        
        return info;
    }
    
    public void updateRouteStatus(String routeId, String status) {
        Route route = routes.get(routeId);
        if (route != null) {
            route.setStatus(status);
            log.debug("Route {} status updated to {}", routeId, status);
        }
    }
    
    public List<Route> findRoutesBySource(String sourceId) {
        return routes.values().stream()
            .filter(r -> sourceId.equals(r.getSourceId()))
            .collect(java.util.stream.Collectors.toList());
    }
    
    public List<Route> findRoutesByDestination(String destinationId) {
        return routes.values().stream()
            .filter(r -> destinationId.equals(r.getDestinationId()))
            .collect(java.util.stream.Collectors.toList());
    }
}
