
package net.ooder.sdk.service.network.route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import net.ooder.sdk.service.network.route.model.Route;

public class ShortestPathCalculator implements RouteCalculator {
    
    @Override
    public Route calculate(String sourceId, String destinationId, Collection<Route> availableRoutes) {
        Map<String, List<Route>> adjacency = buildAdjacencyMap(availableRoutes);
        
        Map<String, Integer> distances = new HashMap<>();
        Map<String, Route> previousRoute = new HashMap<>();
        
        for (Route route : availableRoutes) {
            distances.put(route.getSourceId(), Integer.MAX_VALUE);
            distances.put(route.getDestinationId(), Integer.MAX_VALUE);
        }
        distances.put(sourceId, 0);
        
        Queue<String> queue = new LinkedList<>();
        queue.offer(sourceId);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            if (current.equals(destinationId)) {
                break;
            }
            
            List<Route> neighbors = adjacency.get(current);
            if (neighbors != null) {
                for (Route route : neighbors) {
                    String next = route.getDestinationId();
                    int newDist = distances.get(current) + 1;
                    
                    if (newDist < distances.getOrDefault(next, Integer.MAX_VALUE)) {
                        distances.put(next, newDist);
                        previousRoute.put(next, route);
                        queue.offer(next);
                    }
                }
            }
        }
        
        if (!previousRoute.containsKey(destinationId)) {
            return null;
        }
        
        List<String> hops = new ArrayList<>();
        String current = destinationId;
        while (!current.equals(sourceId)) {
            hops.add(0, current);
            Route prev = previousRoute.get(current);
            if (prev == null) break;
            current = prev.getSourceId();
        }
        hops.add(0, sourceId);
        
        Route result = new Route();
        result.setRouteId(sourceId + "-" + destinationId);
        result.setSourceId(sourceId);
        result.setDestinationId(destinationId);
        result.setHops(hops);
        result.setStatus("active");
        
        return result;
    }
    
    @Override
    public List<Route> calculateAlternatives(String sourceId, String destinationId, int maxAlternatives) {
        if (maxAlternatives <= 0) {
            return new ArrayList<>();
        }
        
        List<Route> allRoutes = new ArrayList<>();
        Route shortestPath = calculate(sourceId, destinationId, new HashSet<>());
        
        if (shortestPath == null) {
            return new ArrayList<>();
        }
        
        allRoutes.add(shortestPath);
        
        if (maxAlternatives == 1) {
            return allRoutes;
        }
        
        List<PathCandidate> candidates = new ArrayList<>();
        Set<String> seenPaths = new HashSet<>();
        seenPaths.add(pathToString(shortestPath));
        
        for (int k = 1; k < maxAlternatives; k++) {
            Route previousPath = allRoutes.get(k - 1);
            List<String> previousHops = previousPath.getHops();
            
            if (previousHops == null || previousHops.size() < 2) {
                break;
            }
            
            for (int i = 0; i < previousHops.size() - 1; i++) {
                String spurNode = previousHops.get(i);
                List<String> rootPath = new ArrayList<>(previousHops.subList(0, i + 1));
                
                Set<String> removedEdges = new HashSet<>();
                for (Route path : allRoutes) {
                    List<String> pathHops = path.getHops();
                    if (pathHops != null && pathHops.size() > i && 
                        pathHops.subList(0, i + 1).equals(rootPath) && 
                        pathHops.size() > i + 1) {
                        String edgeSource = pathHops.get(i);
                        String edgeDest = pathHops.get(i + 1);
                        removedEdges.add(edgeSource + "->" + edgeDest);
                    }
                }
                
                Set<String> removedNodes = new HashSet<>();
                for (String node : rootPath) {
                    if (!node.equals(spurNode)) {
                        removedNodes.add(node);
                    }
                }
                
                Route spurPath = calculateWithExclusions(spurNode, destinationId, removedEdges, removedNodes);
                
                if (spurPath != null && spurPath.getHops() != null) {
                    List<String> totalPath = new ArrayList<>(rootPath);
                    totalPath.addAll(spurPath.getHops().subList(1, spurPath.getHops().size()));
                    
                    String pathKey = String.join("->", totalPath);
                    if (!seenPaths.contains(pathKey)) {
                        Route newPath = createRoute(sourceId, destinationId, totalPath);
                        candidates.add(new PathCandidate(newPath, totalPath.size()));
                        seenPaths.add(pathKey);
                    }
                }
            }
            
            if (candidates.isEmpty()) {
                break;
            }
            
            Collections.sort(candidates, Comparator.comparingInt(c -> c.cost));
            
            Route bestCandidate = candidates.remove(0).path;
            allRoutes.add(bestCandidate);
        }
        
        return allRoutes;
    }
    
    private Route calculateWithExclusions(String sourceId, String destinationId, 
            Set<String> excludedEdges, Set<String> excludedNodes) {
        
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Map<String, Route> previousRoute = new HashMap<>();
        
        distances.put(sourceId, 0);
        
        PriorityQueue<NodeDistance> queue = new PriorityQueue<>(
            Comparator.comparingInt(n -> n.distance)
        );
        queue.offer(new NodeDistance(sourceId, 0));
        
        Set<String> visited = new HashSet<>();
        
        while (!queue.isEmpty()) {
            NodeDistance current = queue.poll();
            
            if (visited.contains(current.node)) {
                continue;
            }
            visited.add(current.node);
            
            if (current.node.equals(destinationId)) {
                break;
            }
            
            for (Map.Entry<String, Route> neighbor : getNeighbors(current.node).entrySet()) {
                String nextNode = neighbor.getKey();
                Route route = neighbor.getValue();
                
                if (excludedNodes.contains(nextNode)) {
                    continue;
                }
                
                String edgeKey = current.node + "->" + nextNode;
                if (excludedEdges.contains(edgeKey)) {
                    continue;
                }
                
                int newDist = distances.get(current.node) + 1;
                
                if (newDist < distances.getOrDefault(nextNode, Integer.MAX_VALUE)) {
                    distances.put(nextNode, newDist);
                    previous.put(nextNode, current.node);
                    previousRoute.put(nextNode, route);
                    queue.offer(new NodeDistance(nextNode, newDist));
                }
            }
        }
        
        if (!previous.containsKey(destinationId)) {
            return null;
        }
        
        List<String> hops = new ArrayList<>();
        String current = destinationId;
        while (current != null) {
            hops.add(0, current);
            current = previous.get(current);
        }
        
        return createRoute(sourceId, destinationId, hops);
    }
    
    private Map<String, Route> getNeighbors(String nodeId) {
        Map<String, Route> neighbors = new HashMap<>();
        return neighbors;
    }
    
    private Route createRoute(String sourceId, String destinationId, List<String> hops) {
        Route route = new Route();
        route.setRouteId(sourceId + "-" + destinationId + "-" + System.nanoTime());
        route.setSourceId(sourceId);
        route.setDestinationId(destinationId);
        route.setHops(new ArrayList<>(hops));
        route.setStatus("active");
        return route;
    }
    
    private String pathToString(Route route) {
        if (route == null || route.getHops() == null) {
            return "";
        }
        return String.join("->", route.getHops());
    }
    
    private Map<String, List<Route>> buildAdjacencyMap(Collection<Route> routes) {
        Map<String, List<Route>> map = new HashMap<>();
        
        for (Route route : routes) {
            map.computeIfAbsent(route.getSourceId(), k -> new ArrayList<>()).add(route);
        }
        
        return map;
    }
    
    private static class NodeDistance {
        final String node;
        final int distance;
        
        NodeDistance(String node, int distance) {
            this.node = node;
            this.distance = distance;
        }
    }
    
    private static class PathCandidate {
        final Route path;
        final int cost;
        
        PathCandidate(Route path, int cost) {
            this.path = path;
            this.cost = cost;
        }
    }
}
