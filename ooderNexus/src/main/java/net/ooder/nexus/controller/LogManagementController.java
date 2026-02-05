package net.ooder.nexus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/logs")
public class LogManagementController {

    private static final Logger log = LoggerFactory.getLogger(LogManagementController.class);
    
    // 日志存储
    private final ConcurrentLinkedQueue<LogEntry> logEntries = new ConcurrentLinkedQueue<>();
    
    // 日志文件路径
    private static final String LOG_DIR = "logs";
    private static final String SYSTEM_LOG_FILE = "system.log";
    private static final String ERROR_LOG_FILE = "error.log";
    private static final String AUDIT_LOG_FILE = "audit.log";
    
    // 日志级别
    private static final String[] LOG_LEVELS = {"DEBUG", "INFO", "WARN", "ERROR", "FATAL"};
    
    // 日志来源
    private static final String[] LOG_SOURCES = {"system", "mcp", "skillcenter", "skillflow", "vfs", "network", "security", "user"};
    
    // 初始化
    public LogManagementController() {
        initializeLogDirectory();
        generateSampleLogs();
    }
    
    /**
     * 初始化日志目录
     */
    private void initializeLogDirectory() {
        File logDir = new File(LOG_DIR);
        if (!logDir.exists()) {
            if (logDir.mkdirs()) {
                log.info("Log directory created: {}", LOG_DIR);
            } else {
                log.error("Failed to create log directory: {}", LOG_DIR);
            }
        }
        
        // 创建日志文件
        createLogFile(SYSTEM_LOG_FILE);
        createLogFile(ERROR_LOG_FILE);
        createLogFile(AUDIT_LOG_FILE);
    }
    
    /**
     * 创建日志文件
     */
    private void createLogFile(String fileName) {
        File logFile = new File(LOG_DIR, fileName);
        if (!logFile.exists()) {
            try {
                if (logFile.createNewFile()) {
                    log.info("Log file created: {}", logFile.getAbsolutePath());
                } else {
                    log.error("Failed to create log file: {}", logFile.getAbsolutePath());
                }
            } catch (IOException e) {
                log.error("Error creating log file: {}", e.getMessage(), e);
            }
        }
    }
    
    /**
     * 生成示例日志数据
     */
    private void generateSampleLogs() {
        // 生成过去24小时的示例日志
        long now = System.currentTimeMillis();
        long oneDayAgo = now - 24 * 60 * 60 * 1000;
        
        for (int i = 0; i < 500; i++) {
            long timestamp = oneDayAgo + (long)(Math.random() * (now - oneDayAgo));
            String level = LOG_LEVELS[(int)(Math.random() * LOG_LEVELS.length)];
            String source = LOG_SOURCES[(int)(Math.random() * LOG_SOURCES.length)];
            String message = generateLogMessage(source, level);
            
            LogEntry logEntry = new LogEntry(
                "log-" + (System.currentTimeMillis() + i),
                level,
                source,
                message,
                timestamp,
                "127.0.0.1",
                "system"
            );
            
            logEntries.offer(logEntry);
        }
        
        log.info("Generated {} sample log entries", logEntries.size());
    }
    
    /**
     * 生成日志消息
     */
    private String generateLogMessage(String source, String level) {
        Map<String, String[]> messageTemplates = new HashMap<>();
        
        messageTemplates.put("system", new String[]{
            "系统启动成功",
            "系统配置已更新",
            "系统资源使用正常",
            "系统时间同步完成",
            "系统服务状态检查完成"
        });
        
        messageTemplates.put("mcp", new String[]{
            "MCP Agent初始化完成",
            "网络拓扑更新成功",
            "命令处理完成",
            "心跳包发送成功",
            "终端设备发现"
        });
        
        messageTemplates.put("skillcenter", new String[]{
            "技能模块加载成功",
            "技能执行完成",
            "技能状态更新",
            "技能依赖检查通过",
            "技能配置已更新"
        });
        
        messageTemplates.put("skillflow", new String[]{
            "工作流执行开始",
            "工作流节点处理完成",
            "工作流执行成功",
            "工作流执行失败",
            "工作流状态更新"
        });
        
        messageTemplates.put("vfs", new String[]{
            "虚拟文件系统初始化",
            "文件操作完成",
            "存储空间检查完成",
            "文件系统状态正常",
            "文件访问权限检查"
        });
        
        messageTemplates.put("network", new String[]{
            "网络连接建立",
            "网络数据包发送",
            "网络状态检查",
            "网络拓扑发现",
            "网络链路状态更新"
        });
        
        messageTemplates.put("security", new String[]{
            "安全检查通过",
            "认证成功",
            "授权检查完成",
            "安全事件记录",
            "访问控制规则更新"
        });
        
        messageTemplates.put("user", new String[]{
            "用户登录成功",
            "用户操作执行",
            "用户权限检查",
            "用户配置更新",
            "用户会话管理"
        });
        
        String[] templates = messageTemplates.getOrDefault(source, messageTemplates.get("system"));
        String template = templates[(int)(Math.random() * templates.length)];
        
        if (level.equals("ERROR") || level.equals("FATAL")) {
            return template + " - 错误: " + "操作失败，请检查相关配置";
        }
        
        return template;
    }
    
    /**
     * 获取日志列表
     */
    @GetMapping("/list")
    public Map<String, Object> getLogs(
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime,
            @RequestParam(required = false) String keyword) {
        log.info("Get logs requested: limit={}, offset={}, level={}, source={}, startTime={}, endTime={}, keyword={}", 
                limit, offset, level, source, startTime, endTime, keyword);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 过滤日志
            List<LogEntry> filteredLogs = filterLogs(level, source, startTime, endTime, keyword);
            
            // 分页
            int total = filteredLogs.size();
            int toIndex = Math.min(offset + limit, total);
            List<LogEntry> pagedLogs = filteredLogs.subList(offset, toIndex);
            
            // 转换为响应格式
            List<Map<String, Object>> logList = pagedLogs.stream()
                    .map(LogEntry::toMap)
                    .collect(Collectors.toList());
            
            // 日志统计
            Map<String, Object> stats = calculateLogStats(filteredLogs);
            
            response.put("status", "success");
            response.put("message", "Logs retrieved successfully");
            response.put("data", logList);
            response.put("total", total);
            response.put("offset", offset);
            response.put("limit", limit);
            response.put("stats", stats);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting logs: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "LOGS_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取单个日志详情
     */
    @GetMapping("/detail/{logId}")
    public Map<String, Object> getLogDetail(@PathVariable String logId) {
        log.info("Get log detail requested: {}", logId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            LogEntry logEntry = logEntries.stream()
                    .filter(entry -> entry.getId().equals(logId))
                    .findFirst()
                    .orElse(null);
            
            if (logEntry == null) {
                response.put("status", "error");
                response.put("message", "Log entry not found");
                response.put("code", "LOG_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            // 扩展日志详情
            Map<String, Object> logDetail = logEntry.toMap();
            logDetail.put("context", generateLogContext(logEntry));
            logDetail.put("relatedLogs", getRelatedLogs(logEntry, 5));
            
            response.put("status", "success");
            response.put("message", "Log detail retrieved successfully");
            response.put("data", logDetail);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting log detail: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "LOG_DETAIL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取日志统计
     */
    @GetMapping("/stats")
    public Map<String, Object> getLogStats(
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime) {
        log.info("Get log stats requested: startTime={}, endTime={}", startTime, endTime);
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 过滤日志
            List<LogEntry> filteredLogs = filterLogs(null, null, startTime, endTime, null);
            
            // 计算统计信息
            Map<String, Object> stats = calculateLogStats(filteredLogs);
            
            // 时间分布
            Map<String, Object> timeDistribution = calculateTimeDistribution(filteredLogs);
            stats.put("timeDistribution", timeDistribution);
            
            // 来源分布
            Map<String, Object> sourceDistribution = calculateSourceDistribution(filteredLogs);
            stats.put("sourceDistribution", sourceDistribution);
            
            // 级别分布
            Map<String, Object> levelDistribution = calculateLevelDistribution(filteredLogs);
            stats.put("levelDistribution", levelDistribution);
            
            response.put("status", "success");
            response.put("message", "Log stats retrieved successfully");
            response.put("data", stats);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting log stats: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "LOG_STATS_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 清理日志
     */
    @PostMapping("/clear")
    public Map<String, Object> clearLogs(
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) Long beforeTime) {
        log.info("Clear logs requested: level={}, source={}, beforeTime={}", level, source, beforeTime);
        Map<String, Object> response = new HashMap<>();
        
        try {
            int initialSize = logEntries.size();
            
            // 清理内存中的日志
            List<LogEntry> toRemove = new ArrayList<>();
            for (LogEntry entry : logEntries) {
                if ((level == null || entry.getLevel().equals(level)) &&
                    (source == null || entry.getSource().equals(source)) &&
                    (beforeTime == null || entry.getTimestamp() < beforeTime)) {
                    toRemove.add(entry);
                }
            }
            
            toRemove.forEach(logEntries::remove);
            int removedCount = initialSize - logEntries.size();
            
            // 清理日志文件
            if (beforeTime != null) {
                cleanLogFiles(beforeTime);
            }
            
            response.put("status", "success");
            response.put("message", "Logs cleared successfully");
            response.put("removedCount", removedCount);
            response.put("remainingCount", logEntries.size());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error clearing logs: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "LOG_CLEAR_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 导出日志
     */
    @GetMapping("/export")
    public Map<String, Object> exportLogs(
            @RequestParam(required = false) String format,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime) {
        log.info("Export logs requested: format={}, level={}, source={}, startTime={}, endTime={}", 
                format, level, source, startTime, endTime);
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 过滤日志
            List<LogEntry> filteredLogs = filterLogs(level, source, startTime, endTime, null);
            
            // 生成导出文件
            String exportFormat = format != null ? format.toLowerCase() : "json";
            String fileName = generateExportFileName(exportFormat);
            
            boolean success = false;
            switch (exportFormat) {
                case "json":
                    success = exportLogsAsJson(filteredLogs, fileName);
                    break;
                case "csv":
                    success = exportLogsAsCsv(filteredLogs, fileName);
                    break;
                case "txt":
                    success = exportLogsAsText(filteredLogs, fileName);
                    break;
                default:
                    success = exportLogsAsJson(filteredLogs, fileName);
            }
            
            if (success) {
                response.put("status", "success");
                response.put("message", "Logs exported successfully");
                response.put("fileName", fileName);
                response.put("logCount", filteredLogs.size());
                response.put("downloadUrl", "/api/logs/download/" + fileName);
                response.put("timestamp", System.currentTimeMillis());
            } else {
                response.put("status", "error");
                response.put("message", "Failed to export logs");
                response.put("code", "LOG_EXPORT_ERROR");
                response.put("timestamp", System.currentTimeMillis());
            }
            
        } catch (Exception e) {
            log.error("Error exporting logs: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "LOG_EXPORT_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取日志级别分布
     */
    @GetMapping("/levels")
    public Map<String, Object> getLogLevels() {
        log.info("Get log levels requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> levelsInfo = new HashMap<>();
            levelsInfo.put("levels", LOG_LEVELS);
            levelsInfo.put("count", LOG_LEVELS.length);
            
            // 统计每个级别的日志数量
            Map<String, Integer> levelCounts = new HashMap<>();
            for (String level : LOG_LEVELS) {
                levelCounts.put(level, (int) logEntries.stream().filter(e -> e.getLevel().equals(level)).count());
            }
            levelsInfo.put("counts", levelCounts);
            
            response.put("status", "success");
            response.put("message", "Log levels retrieved successfully");
            response.put("data", levelsInfo);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting log levels: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "LOG_LEVELS_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取日志来源
     */
    @GetMapping("/sources")
    public Map<String, Object> getLogSources() {
        log.info("Get log sources requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> sourcesInfo = new HashMap<>();
            sourcesInfo.put("sources", LOG_SOURCES);
            sourcesInfo.put("count", LOG_SOURCES.length);
            
            // 统计每个来源的日志数量
            Map<String, Integer> sourceCounts = new HashMap<>();
            for (String source : LOG_SOURCES) {
                sourceCounts.put(source, (int) logEntries.stream().filter(e -> e.getSource().equals(source)).count());
            }
            sourcesInfo.put("counts", sourceCounts);
            
            response.put("status", "success");
            response.put("message", "Log sources retrieved successfully");
            response.put("data", sourcesInfo);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting log sources: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "LOG_SOURCES_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 过滤日志
     */
    private List<LogEntry> filterLogs(String level, String source, Long startTime, Long endTime, String keyword) {
        return logEntries.stream()
                .filter(entry -> (level == null || entry.getLevel().equals(level)))
                .filter(entry -> (source == null || entry.getSource().equals(source)))
                .filter(entry -> (startTime == null || entry.getTimestamp() >= startTime))
                .filter(entry -> (endTime == null || entry.getTimestamp() <= endTime))
                .filter(entry -> (keyword == null || entry.getMessage().contains(keyword) || entry.getDetails().contains(keyword)))
                .sorted((e1, e2) -> Long.compare(e2.getTimestamp(), e1.getTimestamp()))
                .collect(Collectors.toList());
    }
    
    /**
     * 计算日志统计
     */
    private Map<String, Object> calculateLogStats(List<LogEntry> logs) {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalCount", logs.size());
        stats.put("levelCounts", logs.stream()
                .collect(Collectors.groupingBy(LogEntry::getLevel, Collectors.counting())));
        stats.put("sourceCounts", logs.stream()
                .collect(Collectors.groupingBy(LogEntry::getSource, Collectors.counting())));
        
        if (!logs.isEmpty()) {
            stats.put("firstLogTime", logs.get(logs.size() - 1).getTimestamp());
            stats.put("lastLogTime", logs.get(0).getTimestamp());
        }
        
        return stats;
    }
    
    /**
     * 计算时间分布
     */
    private Map<String, Object> calculateTimeDistribution(List<LogEntry> logs) {
        Map<String, Object> distribution = new HashMap<>();
        
        // 按小时统计
        Map<String, Integer> hourlyCounts = new HashMap<>();
        SimpleDateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH:00");
        
        for (LogEntry entry : logs) {
            String hourKey = hourFormat.format(new Date(entry.getTimestamp()));
            hourlyCounts.put(hourKey, hourlyCounts.getOrDefault(hourKey, 0) + 1);
        }
        
        distribution.put("hourly", hourlyCounts);
        distribution.put("totalHours", hourlyCounts.size());
        
        return distribution;
    }
    
    /**
     * 计算来源分布
     */
    private Map<String, Object> calculateSourceDistribution(List<LogEntry> logs) {
        Map<String, Object> distribution = new HashMap<>();
        
        Map<String, Integer> sourceCounts = logs.stream()
                .collect(Collectors.groupingBy(LogEntry::getSource, Collectors.summingInt(e -> 1)));
        
        distribution.put("sources", sourceCounts);
        distribution.put("totalSources", sourceCounts.size());
        
        return distribution;
    }
    
    /**
     * 计算级别分布
     */
    private Map<String, Object> calculateLevelDistribution(List<LogEntry> logs) {
        Map<String, Object> distribution = new HashMap<>();
        
        Map<String, Integer> levelCounts = logs.stream()
                .collect(Collectors.groupingBy(LogEntry::getLevel, Collectors.summingInt(e -> 1)));
        
        distribution.put("levels", levelCounts);
        distribution.put("totalLevels", levelCounts.size());
        
        return distribution;
    }
    
    /**
     * 生成日志上下文
     */
    private Map<String, Object> generateLogContext(LogEntry entry) {
        Map<String, Object> context = new HashMap<>();
        
        Map<String, Object> sourceInfo = new ConcurrentHashMap<>();
        sourceInfo.put("name", entry.getSource());
        sourceInfo.put("type", getSourceType(entry.getSource()));
        sourceInfo.put("description", getSourceDescription(entry.getSource()));
        context.put("sourceInfo", sourceInfo);
        
        Map<String, Object> levelInfo = new ConcurrentHashMap<>();
        levelInfo.put("name", entry.getLevel());
        levelInfo.put("severity", getLevelSeverity(entry.getLevel()));
        levelInfo.put("description", getLevelDescription(entry.getLevel()));
        context.put("levelInfo", levelInfo);
        
        Map<String, Object> timestampInfo = new ConcurrentHashMap<>();
        timestampInfo.put("timestamp", entry.getTimestamp());
        timestampInfo.put("date", new Date(entry.getTimestamp()).toString());
        timestampInfo.put("timezone", System.getProperty("user.timezone"));
        context.put("timestampInfo", timestampInfo);
        
        return context;
    }
    
    /**
     * 获取相关日志
     */
    private List<Map<String, Object>> getRelatedLogs(LogEntry entry, int limit) {
        return logEntries.stream()
                .filter(e -> !e.getId().equals(entry.getId()) &&
                             e.getSource().equals(entry.getSource()) &&
                             Math.abs(e.getTimestamp() - entry.getTimestamp()) < 3600000) // 1小时内
                .sorted((e1, e2) -> Long.compare(Math.abs(e2.getTimestamp() - entry.getTimestamp()), Math.abs(e1.getTimestamp() - entry.getTimestamp())))
                .limit(limit)
                .map(LogEntry::toMap)
                .collect(Collectors.toList());
    }
    
    /**
     * 清理日志文件
     */
    private void cleanLogFiles(long beforeTime) {
        // 这里可以实现日志文件清理逻辑
        log.info("Log files cleaned up to timestamp: {}", beforeTime);
    }
    
    /**
     * 生成导出文件名
     */
    private String generateExportFileName(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());
        return "logs_export_" + timestamp + "." + format;
    }
    
    /**
     * 导出为JSON
     */
    private boolean exportLogsAsJson(List<LogEntry> logs, String fileName) {
        try {
            File exportFile = new File(LOG_DIR, fileName);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFile)))) {
                writer.write("[");
                for (int i = 0; i < logs.size(); i++) {
                    LogEntry entry = logs.get(i);
                    writer.write(entry.toJson());
                    if (i < logs.size() - 1) {
                        writer.write(",");
                    }
                }
                writer.write("]");
            }
            return true;
        } catch (Exception e) {
            log.error("Error exporting logs as JSON: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 导出为CSV
     */
    private boolean exportLogsAsCsv(List<LogEntry> logs, String fileName) {
        try {
            File exportFile = new File(LOG_DIR, fileName);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFile)))) {
                // 写入表头
                writer.write("id,timestamp,level,source,message,details,ip,user,sourceType,severity\n");
                
                // 写入数据
                for (LogEntry entry : logs) {
                    writer.write(entry.toCsv());
                    writer.write("\n");
                }
            }
            return true;
        } catch (Exception e) {
            log.error("Error exporting logs as CSV: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 导出为文本
     */
    private boolean exportLogsAsText(List<LogEntry> logs, String fileName) {
        try {
            File exportFile = new File(LOG_DIR, fileName);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFile)))) {
                for (LogEntry entry : logs) {
                    writer.write(entry.toString());
                    writer.write("\n\n");
                }
            }
            return true;
        } catch (Exception e) {
            log.error("Error exporting logs as text: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 获取来源类型
     */
    private String getSourceType(String source) {
        Map<String, String> sourceTypes = new HashMap<>();
        sourceTypes.put("system", "core");
        sourceTypes.put("mcp", "core");
        sourceTypes.put("skillcenter", "service");
        sourceTypes.put("skillflow", "service");
        sourceTypes.put("vfs", "service");
        sourceTypes.put("network", "infrastructure");
        sourceTypes.put("security", "infrastructure");
        sourceTypes.put("user", "interaction");
        return sourceTypes.getOrDefault(source, "unknown");
    }
    
    /**
     * 获取来源描述
     */
    private String getSourceDescription(String source) {
        Map<String, String> descriptions = new HashMap<>();
        descriptions.put("system", "系统核心组件");
        descriptions.put("mcp", "MCP Agent服务");
        descriptions.put("skillcenter", "技能中心服务");
        descriptions.put("skillflow", "技能工作流服务");
        descriptions.put("vfs", "虚拟文件系统服务");
        descriptions.put("network", "网络管理组件");
        descriptions.put("security", "安全管理组件");
        descriptions.put("user", "用户交互组件");
        return descriptions.getOrDefault(source, "未知来源");
    }
    
    /**
     * 获取级别严重程度
     */
    private int getLevelSeverity(String level) {
        Map<String, Integer> severities = new HashMap<>();
        severities.put("DEBUG", 0);
        severities.put("INFO", 1);
        severities.put("WARN", 2);
        severities.put("ERROR", 3);
        severities.put("FATAL", 4);
        return severities.getOrDefault(level, 0);
    }
    
    /**
     * 获取级别描述
     */
    private String getLevelDescription(String level) {
        Map<String, String> descriptions = new HashMap<>();
        descriptions.put("DEBUG", "调试信息");
        descriptions.put("INFO", "一般信息");
        descriptions.put("WARN", "警告信息");
        descriptions.put("ERROR", "错误信息");
        descriptions.put("FATAL", "致命错误");
        return descriptions.getOrDefault(level, "未知级别");
    }
    
    // 日志条目类
    private static class LogEntry {
        private final String id;
        private final String level;
        private final String source;
        private final String message;
        private final String details;
        private final long timestamp;
        private final String ip;
        private final String user;
        
        public LogEntry(String id, String level, String source, String message, long timestamp, String ip, String user) {
            this.id = id;
            this.level = level;
            this.source = source;
            this.message = message;
            this.details = message + " - 详细信息: 操作完成，状态正常";
            this.timestamp = timestamp;
            this.ip = ip;
            this.user = user;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("level", level);
            map.put("source", source);
            map.put("message", message);
            map.put("details", details);
            map.put("timestamp", timestamp);
            map.put("date", new Date(timestamp).toString());
            map.put("ip", ip);
            map.put("user", user);
            return map;
        }
        
        public String toJson() {
            return String.format(
                "{\"id\":\"%s\",\"level\":\"%s\",\"source\":\"%s\",\"message\":\"%s\",\"details\":\"%s\",\"timestamp\":%d,\"ip\":\"%s\",\"user\":\"%s\"}",
                id, level, source, message, details, timestamp, ip, user
            );
        }
        
        public String toCsv() {
            return String.format(
                "%s,%d,%s,%s,%s,%s,%s,%s",
                id, timestamp, level, source, message, details, ip, user
            );
        }
        
        @Override
        public String toString() {
            return String.format(
                "[%s] [%s] [%s] %s\nDetails: %s\nIP: %s, User: %s, Time: %s",
                timestamp, level, source, message, details, ip, user, new Date(timestamp).toString()
            );
        }
        
        public String getId() { return id; }
        public String getLevel() { return level; }
        public String getSource() { return source; }
        public String getMessage() { return message; }
        public String getDetails() { return details; }
        public long getTimestamp() { return timestamp; }
        public String getIp() { return ip; }
        public String getUser() { return user; }
    }
}