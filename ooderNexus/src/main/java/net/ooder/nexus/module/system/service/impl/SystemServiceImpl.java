package net.ooder.nexus.module.system.service.impl;

import net.ooder.nexus.module.system.service.SystemService;
import net.ooder.nexus.management.NexusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SystemServiceImpl implements SystemService {
    
    @Autowired
    private NexusManager nexusManager;
    
    @Override
    public Map<String, Object> getSystemStatus() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> systemStatus = nexusManager.getSystemStatus();
            response.put("status", "success");
            response.put("data", systemStatus);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }
    
    @Override
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "healthy");
        response.put("service", "ooder-mcp-agent");
        response.put("timestamp", System.currentTimeMillis());
        
        try {
            Map<String, Object> systemStatus = nexusManager.getSystemStatus();
            response.put("systemStatus", systemStatus);
        } catch (Exception e) {
            response.put("systemStatus", "error: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> getSystemMetrics() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("metrics", getRuntimeMetrics());
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    private Map<String, Object> getRuntimeMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        metrics.put("availableProcessors", runtime.availableProcessors());
        metrics.put("freeMemory", runtime.freeMemory());
        metrics.put("totalMemory", runtime.totalMemory());
        metrics.put("maxMemory", runtime.maxMemory());
        return metrics;
    }
}
