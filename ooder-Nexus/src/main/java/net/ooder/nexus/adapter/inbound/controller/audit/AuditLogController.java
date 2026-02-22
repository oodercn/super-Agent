package net.ooder.nexus.adapter.inbound.controller.audit;

import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.nexus.common.audit.Auditable;
import net.ooder.nexus.domain.skill.model.SkillResourceLog;
import net.ooder.nexus.service.audit.AuditLogService;
import net.ooder.nexus.service.audit.AuditLogService.AuditLogQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/audit", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class AuditLogController {

    private static final Logger log = LoggerFactory.getLogger(AuditLogController.class);

    @Autowired
    private AuditLogService auditLogService;

    @PostMapping("/log/list")
    @ResponseBody
    @Auditable(action = "query", resourceType = "audit_log", description = "查询审计日志")
    public ListResultModel<List<Map<String, Object>>> queryLogs(@RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        String skillId = (String) request.get("skillId");
        String action = (String) request.get("action");
        String resourceType = (String) request.get("resourceType");
        Integer status = request.get("status") != null ? (Integer) request.get("status") : null;
        Long startTime = request.get("startTime") != null ? ((Number) request.get("startTime")).longValue() : null;
        Long endTime = request.get("endTime") != null ? ((Number) request.get("endTime")).longValue() : null;
        Integer page = request.get("page") != null ? (Integer) request.get("page") : 1;
        Integer pageSize = request.get("pageSize") != null ? (Integer) request.get("pageSize") : 20;
        
        log.info("Query audit logs: userId={}, skillId={}, action={}", userId, skillId, action);
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        
        try {
            AuditLogQuery query = new AuditLogQuery();
            query.setUserId(userId);
            query.setSkillId(skillId);
            query.setAction(action);
            query.setResourceType(resourceType);
            query.setStatus(status);
            query.setStartTime(startTime);
            query.setEndTime(endTime);
            query.setPage(page);
            query.setPageSize(pageSize);
            
            List<SkillResourceLog> logs = auditLogService.queryLogs(query);
            
            List<Map<String, Object>> data = new ArrayList<>();
            for (SkillResourceLog logEntry : logs) {
                data.add(logEntry.toMap());
            }
            
            result.setData(data);
            result.setSize(data.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error querying audit logs", e);
            result.setRequestStatus(500);
            result.setMessage("查询失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/log/stats")
    @ResponseBody
    public ResultModel<Map<String, Object>> getStatistics(@RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        String skillId = (String) request.get("skillId");
        Long startTime = request.get("startTime") != null ? ((Number) request.get("startTime")).longValue() : 0L;
        Long endTime = request.get("endTime") != null ? ((Number) request.get("endTime")).longValue() : 0L;
        
        log.info("Get audit statistics: userId={}, skillId={}", userId, skillId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        try {
            Map<String, Object> stats = auditLogService.getStatistics(userId, skillId, startTime, endTime);
            result.setData(stats);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting audit statistics", e);
            result.setRequestStatus(500);
            result.setMessage("获取统计失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/alerts")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getAlerts(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        
        log.info("Get alerts: userId={}", userId);
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        
        try {
            List<Map<String, Object>> alerts = auditLogService.getAlerts(userId);
            result.setData(alerts);
            result.setSize(alerts.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting alerts", e);
            result.setRequestStatus(500);
            result.setMessage("获取告警失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/alerts/read")
    @ResponseBody
    public ResultModel<Boolean> markAsRead(@RequestBody Map<String, String> request) {
        String logId = request.get("logId");
        String userId = request.get("userId");
        
        log.info("Mark alert as read: logId={}, userId={}", logId, userId);
        ResultModel<Boolean> result = new ResultModel<>();
        
        try {
            auditLogService.markAsRead(logId, userId);
            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("标记成功");
        } catch (Exception e) {
            log.error("Error marking alert as read", e);
            result.setRequestStatus(500);
            result.setMessage("标记失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/alerts/count")
    @ResponseBody
    public ResultModel<Map<String, Object>> countUnread(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        
        log.info("Count unread alerts: userId={}", userId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        try {
            long count = auditLogService.countUnread(userId);
            Map<String, Object> data = new HashMap<>();
            data.put("unreadCount", count);
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error counting unread alerts", e);
            result.setRequestStatus(500);
            result.setMessage("获取失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/export")
    @ResponseBody
    public ResultModel<String> exportLogs(@RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        Long startTime = request.get("startTime") != null ? ((Number) request.get("startTime")).longValue() : 0L;
        Long endTime = request.get("endTime") != null ? ((Number) request.get("endTime")).longValue() : 0L;
        String format = (String) request.get("format");
        if (format == null) format = "json";
        
        log.info("Export logs: userId={}, format={}", userId, format);
        ResultModel<String> result = new ResultModel<>();
        
        try {
            auditLogService.exportLogs(userId, startTime, endTime, format);
            result.setData("/download/audit_" + userId + "_" + System.currentTimeMillis() + "." + format);
            result.setRequestStatus(200);
            result.setMessage("导出成功");
        } catch (Exception e) {
            log.error("Error exporting logs", e);
            result.setRequestStatus(500);
            result.setMessage("导出失败: " + e.getMessage());
        }
        
        return result;
    }
}
