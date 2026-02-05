package net.ooder.nexus.service.log;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.model.mcp.LogEntry;
import net.ooder.nexus.model.LogExportResult;

import java.util.List;
import java.util.Map;

/**
 * 日志管理服务接口
 * 负责系统日志、安全日志、操作日志等功能
 */
public interface ILogService {
    
    /**
     * 获取日志列表
     * @param limit 限制数量
     * @return 日志列表
     */
    Result<List<LogEntry>> getLogList(int limit);

    /**
     * 清空日志
     * @return 清空结果
     */
    Result<Void> clearLog();

    /**
     * 获取日志列表
     * @param params 查询参数
     * @return 日志列表
     */
    Result<List<LogEntry>> getLogs(Map<String, Object> params);

    /**
     * 刷新日志
     * @return 刷新结果
     */
    Result<List<LogEntry>> refreshLogs();

    /**
     * 导出日志
     * @param params 导出参数
     * @return 导出结果
     */
    Result<LogExportResult> exportLogs(Map<String, Object> params);

    /**
     * 清空日志
     * @return 清空结果
     */
    Result<Void> clearLogs();

    /**
     * 过滤日志
     * @param filters 过滤条件
     * @return 过滤结果
     */
    Result<List<LogEntry>> filterLogs(Map<String, Object> filters);

    /**
     * 获取日志详情
     * @param logId 日志 ID
     * @return 日志详情
     */
    Result<LogEntry> getLogDetails(String logId);
}
