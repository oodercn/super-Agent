package net.ooder.mcp.agent.module.system.service;

import java.util.Map;

public interface SystemService {
    
    Map<String, Object> getSystemStatus();
    
    Map<String, Object> healthCheck();
    
    Map<String, Object> getSystemMetrics();
}
