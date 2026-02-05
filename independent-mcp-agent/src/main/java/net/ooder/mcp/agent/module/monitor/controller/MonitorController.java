package net.ooder.mcp.agent.module.monitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import net.ooder.mcp.agent.module.monitor.service.MonitorService;

import java.util.Map;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {
    
    @Autowired
    private MonitorService monitorService;
    
    @GetMapping("/metrics")
    public Map<String, Object> getSystemMetrics() {
        return monitorService.getSystemMetrics();
    }
    
    @GetMapping("/logs")
    public Map<String, Object> getLogs(@RequestParam(defaultValue = "50") int limit) {
        return monitorService.getLogs(limit);
    }
    
    @GetMapping("/alerts")
    public Map<String, Object> getAlerts() {
        return monitorService.getAlerts();
    }
    
    @PostMapping("/alerts/clear")
    public Map<String, Object> clearAlerts() {
        return monitorService.clearAlerts();
    }
}
