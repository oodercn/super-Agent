package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.dto.audit.AuditLogDTO;
import net.ooder.mvp.skill.scene.dto.audit.AuditStatsDTO;
import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.service.AuditService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequestMapping("/api/v1/audit")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuditController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(AuditController.class);

    @Autowired
    private AuditService auditService;

    @GetMapping("/logs")
    public ResultModel<PageResult<AuditLogDTO>> listLogs(
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String result,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String resourceId,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        
        long start = System.currentTimeMillis();
        logRequestStart("listLogs", eventType);
        
        try {
            PageResult<AuditLogDTO> pageResult = auditService.listLogs(
                eventType, result, userId, resourceId, startTime, endTime, pageNum, pageSize);
            logRequestEnd("listLogs", pageResult.getTotal() + " records", System.currentTimeMillis() - start);
            return ResultModel.success(pageResult);
        } catch (Exception e) {
            logRequestError("listLogs", e);
            return ResultModel.error(500, "获取审计日志失败: " + e.getMessage());
        }
    }

    @GetMapping("/logs/{recordId}")
    public ResultModel<AuditLogDTO> getLogById(@PathVariable String recordId) {
        long start = System.currentTimeMillis();
        logRequestStart("getLogById", recordId);
        
        try {
            AuditLogDTO log = auditService.getLogById(recordId);
            if (log == null) {
                logRequestEnd("getLogById", "Not found", System.currentTimeMillis() - start);
                return ResultModel.notFound("审计日志不存在");
            }
            logRequestEnd("getLogById", log.getRecordId(), System.currentTimeMillis() - start);
            return ResultModel.success(log);
        } catch (Exception e) {
            logRequestError("getLogById", e);
            return ResultModel.error(500, "获取审计日志失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats")
    public ResultModel<AuditStatsDTO> getStats() {
        long start = System.currentTimeMillis();
        logRequestStart("getStats", null);
        
        try {
            AuditStatsDTO stats = auditService.getStats();
            logRequestEnd("getStats", stats.getTotalEvents() + " events", System.currentTimeMillis() - start);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getStats", e);
            return ResultModel.error(500, "获取审计统计失败: " + e.getMessage());
        }
    }

    @PostMapping("/logs")
    public ResultModel<Boolean> createLog(@RequestBody AuditLogDTO logDTO) {
        long start = System.currentTimeMillis();
        logRequestStart("createLog", logDTO.getEventType());
        
        try {
            auditService.logEvent(logDTO);
            logRequestEnd("createLog", logDTO.getRecordId(), System.currentTimeMillis() - start);
            return ResultModel.success(true);
        } catch (Exception e) {
            logRequestError("createLog", e);
            return ResultModel.error(500, "创建审计日志失败: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    public void exportLogs(
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String result,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String resourceId,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime,
            HttpServletResponse response) throws IOException {
        
        logRequestStart("exportLogs", eventType);
        
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=audit_logs.csv");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter writer = response.getWriter();
        writer.write("\uFEFF");
        writer.println("记录ID,事件类型,结果,时间,用户ID,Agent ID,资源类型,资源ID,操作,详情,IP地址");
        
        PageResult<AuditLogDTO> pageResult = auditService.listLogs(
            eventType, result, userId, resourceId, startTime, endTime, 1, 10000);
        
        for (AuditLogDTO log : pageResult.getList()) {
            StringBuilder line = new StringBuilder();
            line.append(escapeCsv(log.getRecordId())).append(",");
            line.append(escapeCsv(log.getEventType() != null ? log.getEventType().getName() : "")).append(",");
            line.append(escapeCsv(log.getResult() != null ? log.getResult().getName() : "")).append(",");
            line.append(log.getTimestamp()).append(",");
            line.append(escapeCsv(log.getUserId())).append(",");
            line.append(escapeCsv(log.getAgentId())).append(",");
            line.append(escapeCsv(log.getResourceType())).append(",");
            line.append(escapeCsv(log.getResourceId())).append(",");
            line.append(escapeCsv(log.getAction())).append(",");
            line.append(escapeCsv(log.getDetail())).append(",");
            line.append(escapeCsv(log.getIpAddress()));
            writer.println(line.toString());
        }
        
        writer.flush();
        logRequestEnd("exportLogs", pageResult.getTotal() + " records", System.currentTimeMillis() - System.currentTimeMillis());
    }
    
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
