package net.ooder.mvp.skill.scene.service.impl;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.audit.AuditEventType;
import net.ooder.mvp.skill.scene.dto.audit.AuditLogDTO;
import net.ooder.mvp.skill.scene.dto.audit.AuditResultType;
import net.ooder.mvp.skill.scene.dto.audit.AuditStatsDTO;
import net.ooder.mvp.skill.scene.service.AuditService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuditServiceFallbackImpl implements AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditServiceFallbackImpl.class);

    private final List<AuditLogDTO> logs = Collections.synchronizedList(new ArrayList<>());

    @Override
    public PageResult<AuditLogDTO> listLogs(String eventType, String result, String userId,
            String resourceId, Long startTime, Long endTime, int pageNum, int pageSize) {
        log.debug("[listLogs] Querying audit logs - eventType: {}, userId: {}", eventType, userId);
        
        List<AuditLogDTO> filtered = new ArrayList<>();
        for (AuditLogDTO logEntry : logs) {
            boolean match = true;
            if (eventType != null && !eventType.isEmpty() && logEntry.getEventType() != null) {
                match = eventType.equals(logEntry.getEventType().getCode());
            }
            if (result != null && !result.isEmpty() && logEntry.getResult() != null) {
                match = match && result.equals(logEntry.getResult().getCode());
            }
            if (userId != null && !userId.isEmpty()) {
                match = match && userId.equals(logEntry.getUserId());
            }
            if (startTime != null && logEntry.getTimestamp() < startTime) {
                match = false;
            }
            if (endTime != null && logEntry.getTimestamp() > endTime) {
                match = false;
            }
            if (match) {
                filtered.add(logEntry);
            }
        }
        
        int total = filtered.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        
        if (fromIndex >= total) {
            return PageResult.empty();
        }
        
        return PageResult.of(filtered.subList(fromIndex, toIndex), total, pageNum, pageSize);
    }

    @Override
    public AuditLogDTO getLogById(String recordId) {
        log.debug("[getLogById] Getting audit log: {}", recordId);
        for (AuditLogDTO logEntry : logs) {
            if (recordId.equals(logEntry.getRecordId())) {
                return logEntry;
            }
        }
        return null;
    }

    @Override
    public AuditStatsDTO getStats() {
        log.debug("[getStats] Getting audit statistics");
        
        AuditStatsDTO stats = new AuditStatsDTO();
        stats.setTotalEvents(logs.size());
        
        long successCount = logs.stream()
            .filter(l -> l.getResult() == AuditResultType.SUCCESS)
            .count();
        long failureCount = logs.stream()
            .filter(l -> l.getResult() == AuditResultType.FAILURE)
            .count();
        
        stats.setSuccessCount(successCount);
        stats.setFailureCount(failureCount);
        stats.setDeniedCount(0L);
        stats.setTodayCount((long) logs.size());
        stats.setWeekCount((long) logs.size());
        stats.setMonthCount((long) logs.size());
        
        return stats;
    }

    @Override
    public void logEvent(AuditLogDTO logEntry) {
        log.debug("[logEvent] Logging audit event: {}", logEntry.getEventType());
        
        if (logEntry.getRecordId() == null || logEntry.getRecordId().isEmpty()) {
            logEntry.setRecordId("audit-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8));
        }
        if (logEntry.getTimestamp() == 0) {
            logEntry.setTimestamp(System.currentTimeMillis());
        }
        
        logs.add(logEntry);
        
        if (logs.size() > 10000) {
            logs.remove(0);
        }
    }

    @Override
    public void exportLogs(String eventType, String result, String userId,
            String resourceId, Long startTime, Long endTime) {
        log.info("[exportLogs] Exporting audit logs - eventType: {}", eventType);
    }
}
