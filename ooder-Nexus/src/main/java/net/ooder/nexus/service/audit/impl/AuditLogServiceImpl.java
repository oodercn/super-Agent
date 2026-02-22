package net.ooder.nexus.service.audit.impl;

import net.ooder.nexus.domain.skill.model.SkillResourceLog;
import net.ooder.nexus.service.audit.AuditLogService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogServiceImpl.class);

    private final ConcurrentHashMap<String, List<SkillResourceLog>> userLogs = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<Map<String, Object>>> userAlerts = new ConcurrentHashMap<>();

    @Override
    public void logAccess(SkillResourceLog entry) {
        if (entry.getLogId() == null) {
            entry.setLogId(UUID.randomUUID().toString());
        }
        if (entry.getTimestamp() == 0) {
            entry.setTimestamp(System.currentTimeMillis());
        }
        
        String userId = entry.getUserId();
        userLogs.computeIfAbsent(userId, k -> new ArrayList<>()).add(entry);
        
        if (entry.getStatus() == SkillResourceLog.STATUS_DENIED) {
            addAlert(userId, entry);
        }
        
        log.debug("Audit log added: skill={}, action={}, status={}", 
                entry.getSkillId(), entry.getAction(), entry.getStatus());
    }

    @Override
    public List<SkillResourceLog> queryLogs(AuditLogQuery query) {
        log.info("Querying audit logs for user: {}", query.getUserId());
        
        List<SkillResourceLog> logs = userLogs.getOrDefault(query.getUserId(), new ArrayList<>());
        
        return logs.stream()
                .filter(l -> query.getSkillId() == null || l.getSkillId().equals(query.getSkillId()))
                .filter(l -> query.getAction() == null || l.getAction().equals(query.getAction()))
                .filter(l -> query.getResourceType() == null || l.getResourceType().equals(query.getResourceType()))
                .filter(l -> query.getStatus() == null || l.getStatus() == query.getStatus())
                .filter(l -> query.getStartTime() == null || l.getTimestamp() >= query.getStartTime())
                .filter(l -> query.getEndTime() == null || l.getTimestamp() <= query.getEndTime())
                .sorted((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()))
                .skip((long) (query.getPage() - 1) * query.getPageSize())
                .limit(query.getPageSize())
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getStatistics(String userId, String skillId, long startTime, long endTime) {
        log.info("Getting audit statistics for user: {}, skill: {}", userId, skillId);
        
        Map<String, Object> stats = new HashMap<>();
        
        List<SkillResourceLog> logs = userLogs.getOrDefault(userId, new ArrayList<>());
        
        List<SkillResourceLog> filtered = logs.stream()
                .filter(l -> skillId == null || l.getSkillId().equals(skillId))
                .filter(l -> startTime == 0 || l.getTimestamp() >= startTime)
                .filter(l -> endTime == 0 || l.getTimestamp() <= endTime)
                .collect(Collectors.toList());
        
        stats.put("totalAccess", filtered.size());
        
        Map<String, Long> actionCounts = new HashMap<>();
        Map<String, Long> resourceTypeCounts = new HashMap<>();
        Map<String, Long> skillCounts = new HashMap<>();
        int successCount = 0;
        int failedCount = 0;
        int deniedCount = 0;
        
        for (SkillResourceLog l : filtered) {
            actionCounts.merge(l.getAction(), 1L, Long::sum);
            resourceTypeCounts.merge(l.getResourceType(), 1L, Long::sum);
            skillCounts.merge(l.getSkillName(), 1L, Long::sum);
            
            if (l.getStatus() == SkillResourceLog.STATUS_SUCCESS) successCount++;
            else if (l.getStatus() == SkillResourceLog.STATUS_FAILED) failedCount++;
            else if (l.getStatus() == SkillResourceLog.STATUS_DENIED) deniedCount++;
        }
        
        stats.put("actionCounts", actionCounts);
        stats.put("resourceTypeCounts", resourceTypeCounts);
        stats.put("skillCounts", skillCounts);
        stats.put("successCount", successCount);
        stats.put("failedCount", failedCount);
        stats.put("deniedCount", deniedCount);
        
        return stats;
    }

    @Override
    public List<Map<String, Object>> getAlerts(String userId) {
        log.info("Getting alerts for user: {}", userId);
        return userAlerts.getOrDefault(userId, new ArrayList<>());
    }

    @Override
    public void markAsRead(String logId, String userId) {
        List<Map<String, Object>> alerts = userAlerts.get(userId);
        if (alerts != null) {
            alerts.removeIf(a -> logId.equals(a.get("logId")));
        }
    }

    @Override
    public long countUnread(String userId) {
        List<Map<String, Object>> alerts = userAlerts.get(userId);
        return alerts != null ? alerts.size() : 0;
    }

    @Override
    public void exportLogs(String userId, long startTime, long endTime, String format) {
        log.info("Exporting logs for user: {}, format: {}", userId, format);
    }

    private void addAlert(String userId, SkillResourceLog entry) {
        Map<String, Object> alert = new HashMap<>();
        alert.put("alertId", UUID.randomUUID().toString());
        alert.put("logId", entry.getLogId());
        alert.put("skillId", entry.getSkillId());
        alert.put("skillName", entry.getSkillName());
        alert.put("action", entry.getAction());
        alert.put("actionText", entry.getActionText());
        alert.put("resourceName", entry.getResourceName());
        alert.put("timestamp", entry.getTimestamp());
        alert.put("type", "access_denied");
        alert.put("message", "技能 [" + entry.getSkillName() + "] 尝试访问未授权资源: " + entry.getResourceName());
        alert.put("read", false);
        
        userAlerts.computeIfAbsent(userId, k -> new ArrayList<>()).add(alert);
        
        log.warn("Access denied alert added for user {}: {}", userId, alert.get("message"));
    }
}
