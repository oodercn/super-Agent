package net.ooder.mcp.agent.module.network.service;

import java.util.Map;

public interface NetworkService {
    
    Map<String, Object> getNetworkStatus();
    
    Map<String, Object> getNetworkTopology();
    
    Map<String, Object> getNetworkLinks();
    
    Map<String, Object> addNetworkLink(Map<String, Object> request);
    
    Map<String, Object> removeNetworkLink(String linkId);
    
    Map<String, Object> resetNetworkStats();
    
    // RouteAgent管理方法
    Map<String, Object> getRouteAgentDetails(String routeAgentId);
    
    Map<String, Object> getRouteAgentVFS(String routeAgentId, String path);
    
    Map<String, Object> getRouteAgentCapabilities(String routeAgentId);
    
    Map<String, Object> getRouteAgentLinks(String routeAgentId);
}
