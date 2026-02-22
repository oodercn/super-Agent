package net.ooder.nexus.service;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.domain.mcp.model.LogEntry;
import net.ooder.nexus.model.LogExportResult;

import java.util.List;
import java.util.Map;

/**
 * 日志管理服务接口
 * 提供日志查询、导出、清理等功能
 */
public interface LogService {

    /**
     * 获取日志列表
     */
    Result<List<LogEntry>> getLogs(Map<String, Object> params);

    /**
     * 刷新日志
     */
    Result<List<LogEntry>> refreshLogs();

    /**
     * 导出日志
     */
    Result<LogExportResult> exportLogs(Map<String, Object> params);

    /**
     * 清空日志
     */
    Result<Void> clearLogs();

    /**
     * 过滤日志
     */
    Result<List<LogEntry>> filterLogs(Map<String, Object> filters);

    /**
     * 获取日志详情
     */
    Result<LogEntry> getLogDetails(String logId);
}
