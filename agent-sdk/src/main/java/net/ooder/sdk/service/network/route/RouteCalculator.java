
package net.ooder.sdk.service.network.route;

import java.util.Collection;
import java.util.List;

import net.ooder.sdk.service.network.route.model.Route;

public interface RouteCalculator {
    
    Route calculate(String sourceId, String destinationId, Collection<Route> availableRoutes);
    
    List<Route> calculateAlternatives(String sourceId, String destinationId, int maxAlternatives);
}
