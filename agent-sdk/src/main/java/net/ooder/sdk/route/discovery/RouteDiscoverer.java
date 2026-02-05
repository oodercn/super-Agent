package net.ooder.sdk.route.discovery;

import net.ooder.sdk.route.RouteManager;
import net.ooder.sdk.route.model.Route;

import java.util.List;

public interface RouteDiscoverer {
    void startDiscovery();
    void stopDiscovery();
    boolean isDiscoveryRunning();
    List<Route> discoverRoutes();
    void setRouteManager(RouteManager routeManager);
}
