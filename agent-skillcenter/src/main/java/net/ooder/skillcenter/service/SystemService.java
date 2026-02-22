package net.ooder.skillcenter.service;

import java.util.List;
import java.util.Map;

public interface SystemService {
    Map<String, Object> getSystemStatus();
    Map<String, Object> getSystemConfig();
    Map<String, Object> getSystemVersion();
    Map<String, Object> getSystemResources();
    Map<String, Object> updateSystemConfig(Map<String, Object> config);
    Map<String, Object> restartSystem();
    Map<String, Object> shutdownSystem();
    Map<String, Object> getSystemHealth();
    Map<String, Object> clearSystemCache();
    List<Map<String, Object>> getSystemOperations();
    List<Map<String, Object>> getSystemLogs(String level);
    Map<String, Object> clearSystemLogs();
}
