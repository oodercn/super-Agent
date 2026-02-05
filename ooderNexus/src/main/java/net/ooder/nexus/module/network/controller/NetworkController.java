package net.ooder.nexus.module.network.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.ooder.nexus.module.network.service.NetworkService;

import java.util.Map;

@RestController
@RequestMapping("/api/network")
public class NetworkController {
    
    @Autowired
    private NetworkService networkService;
    
    @GetMapping("/status")
    public Map<String, Object> getNetworkStatus() {
        return networkService.getNetworkStatus();
    }
    
    @GetMapping("/topology")
    public Map<String, Object> getNetworkTopology() {
        return networkService.getNetworkTopology();
    }
    
    @GetMapping("/links")
    public Map<String, Object> getNetworkLinks() {
        return networkService.getNetworkLinks();
    }
    
    @PostMapping("/link")
    public Map<String, Object> addNetworkLink(@RequestBody Map<String, Object> request) {
        return networkService.addNetworkLink(request);
    }
    
    @DeleteMapping("/link/{linkId}")
    public Map<String, Object> removeNetworkLink(@PathVariable String linkId) {
        return networkService.removeNetworkLink(linkId);
    }
    
    @PostMapping("/reset")
    public Map<String, Object> resetNetworkStats() {
        return networkService.resetNetworkStats();
    }
    
    // RouteAgent管理API
    @GetMapping("/routeagent/{routeAgentId}")
    public Map<String, Object> getRouteAgentDetails(@PathVariable String routeAgentId) {
        return networkService.getRouteAgentDetails(routeAgentId);
    }
    
    @GetMapping("/routeagent/{routeAgentId}/vfs")
    public Map<String, Object> getRouteAgentVFS(@PathVariable String routeAgentId, @RequestParam(defaultValue = "/") String path) {
        return networkService.getRouteAgentVFS(routeAgentId, path);
    }
    
    @GetMapping("/routeagent/{routeAgentId}/capabilities")
    public Map<String, Object> getRouteAgentCapabilities(@PathVariable String routeAgentId) {
        return networkService.getRouteAgentCapabilities(routeAgentId);
    }
    
    @GetMapping("/routeagent/{routeAgentId}/links")
    public Map<String, Object> getRouteAgentLinks(@PathVariable String routeAgentId) {
        return networkService.getRouteAgentLinks(routeAgentId);
    }
}
