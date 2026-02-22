package net.ooder.nexus.service;

import java.util.List;
import java.util.Map;

public interface AdminRemoteService {

    List<Map<String, Object>> getAllRemoteAgents();

    Map<String, Object> getRemoteAgentById(String agentId);

    Map<String, Object> registerRemoteAgent(Map<String, Object> agentData);

    Map<String, Object> updateRemoteAgent(String agentId, Map<String, Object> agentData);

    boolean unregisterRemoteAgent(String agentId);

    boolean testConnection(String agentId);

    List<Map<String, Object>> getConnectionLogs(String agentId, int limit);

    Map<String, Object> getRemoteStatistics();

    List<Map<String, Object>> getAllRemoteSkills();

    Map<String, Object> getRemoteSkillById(String skillId);

    boolean syncRemoteSkill(String skillId);

    boolean removeRemoteSkill(String skillId);

    List<Map<String, Object>> getAllHostings();

    Map<String, Object> getHostingById(String hostingId);

    boolean toggleHosting(String hostingId);

    boolean removeHosting(String hostingId);

    List<Map<String, Object>> getAllMonitorings();

    Map<String, Object> getMonitoringById(String monitoringId);

    boolean checkMonitoring(String monitoringId);

    boolean removeMonitoring(String monitoringId);
}
