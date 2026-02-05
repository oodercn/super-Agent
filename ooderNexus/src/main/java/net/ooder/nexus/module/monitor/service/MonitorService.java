package net.ooder.nexus.module.monitor.service;

import java.util.Map;

public interface MonitorService {
    
    Map<String, Object> getSystemMetrics();
    
    Map<String, Object> getLogs(int limit);
    
    Map<String, Object> getAlerts();
    
    Map<String, Object> clearAlerts();
}
