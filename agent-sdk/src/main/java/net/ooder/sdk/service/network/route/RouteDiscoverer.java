
package net.ooder.sdk.service.network.route;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.service.network.route.model.Route;

public interface RouteDiscoverer {
    
    CompletableFuture<List<Route>> discover();
    
    CompletableFuture<List<Route>> discoverByDestination(String destinationId);
    
    void setTimeout(long timeoutMs);
    
    long getTimeout();
}
