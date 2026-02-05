package net.ooder.sdk.route.calculator;

import net.ooder.sdk.route.model.Route;

import java.util.List;

public interface RouteCalculator {
    Route calculateBestRoute(String sourceId, String destinationId);
    List<Route> calculateMultipleRoutes(String sourceId, String destinationId, int maxRoutes);
    List<Route> calculateRoutesWithConstraints(String sourceId, String destinationId, RouteConstraints constraints);
}
