package net.ooder.nexus.provider;

import net.ooder.scene.core.Result;
import net.ooder.scene.core.PageResult;
import net.ooder.scene.core.SceneEngine;
import net.ooder.scene.provider.BaseProvider;
import net.ooder.scene.provider.LogProvider;
import net.ooder.scene.provider.LogEntry;
import net.ooder.scene.provider.LogQuery;
import net.ooder.scene.provider.LogExportResult;
import net.ooder.scene.provider.LogStatistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class NexusLogProvider implements LogProvider {

    private static final Logger log = LoggerFactory.getLogger(NexusLogProvider.class);
    private static final int MAX_LOG_ENTRIES = 10000;
    private static final String EXPORT_DIR = "./logs/export/";

    private SceneEngine sceneEngine;
    private boolean initialized = false;
    private boolean running = false;
    
    private final Queue<LogEntry> logEntries = new ConcurrentLinkedQueue<LogEntry>();
    private final AtomicLong idCounter = new AtomicLong(0);
    private final Map<String, AtomicLong> levelCounters = new ConcurrentHashMap<String, AtomicLong>();
    private final Map<String, AtomicLong> sourceCounters = new ConcurrentHashMap<String, AtomicLong>();

    @Override
    public String getProviderName() {
        return "NexusLogProvider";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public void initialize(SceneEngine engine) {
        this.sceneEngine = engine;
        this.initialized = true;
        
        levelCounters.put("INFO", new AtomicLong(0));
        levelCounters.put("WARNING", new AtomicLong(0));
        levelCounters.put("ERROR", new AtomicLong(0));
        levelCounters.put("DEBUG", new AtomicLong(0));
        
        File exportDir = new File(EXPORT_DIR);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        
        log.info("NexusLogProvider initialized");
    }

    @Override
    public void start() {
        this.running = true;
        log.info("NexusLogProvider started");
    }

    @Override
    public void stop() {
        this.running = false;
        log.info("NexusLogProvider stopped");
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public Result<Boolean> writeLog(String level, String message, String source) {
        return writeLog(level, message, source, null);
    }

    @Override
    public Result<Boolean> writeLog(String level, String message, String source, Map<String, Object> details) {
        log.debug("Writing log: level={}, source={}", level, source);
        
        try {
            LogEntry entry = new LogEntry();
            entry.setLogId("log-" + idCounter.incrementAndGet());
            entry.setLevel(level != null ? level.toUpperCase() : "INFO");
            entry.setSource(source != null ? source : "SYSTEM");
            entry.setMessage(message);
            entry.setTimestamp(System.currentTimeMillis());
            if (details != null) {
                entry.setDetails(details);
            }
            
            logEntries.offer(entry);
            
            while (logEntries.size() > MAX_LOG_ENTRIES) {
                logEntries.poll();
            }
            
            AtomicLong levelCounter = levelCounters.get(entry.getLevel());
            if (levelCounter != null) {
                levelCounter.incrementAndGet();
            }
            
            AtomicLong sourceCounter = sourceCounters.computeIfAbsent(entry.getSource(), k -> new AtomicLong(0));
            sourceCounter.incrementAndGet();
            
            return Result.success(true);
        } catch (Exception e) {
            log.error("Failed to write log: {}", e.getMessage(), e);
            return Result.error("Failed to write log: " + e.getMessage());
        }
    }

    @Override
    public Result<PageResult<LogEntry>> queryLogs(LogQuery query) {
        log.debug("Querying logs: {}", query);
        
        try {
            List<LogEntry> filteredLogs = new ArrayList<LogEntry>();
            
            for (LogEntry entry : logEntries) {
                boolean match = true;
                
                if (query.getLevel() != null && !query.getLevel().isEmpty()) {
                    match = match && query.getLevel().equalsIgnoreCase(entry.getLevel());
                }
                
                if (query.getSource() != null && !query.getSource().isEmpty()) {
                    match = match && entry.getSource().contains(query.getSource());
                }
                
                if (query.getStartTime() != null) {
                    match = match && entry.getTimestamp() >= query.getStartTime().longValue();
                }
                
                if (query.getEndTime() != null) {
                    match = match && entry.getTimestamp() <= query.getEndTime().longValue();
                }
                
                if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
                    match = match && entry.getMessage().contains(query.getKeyword());
                }
                
                if (match) {
                    filteredLogs.add(entry);
                }
            }
            
            Collections.sort(filteredLogs, new Comparator<LogEntry>() {
                @Override
                public int compare(LogEntry a, LogEntry b) {
                    return Long.compare(b.getTimestamp(), a.getTimestamp());
                }
            });
            
            int page = query.getPage();
            int size = query.getSize();
            int start = (page - 1) * size;
            int end = Math.min(start + size, filteredLogs.size());
            
            List<LogEntry> pageData = new ArrayList<LogEntry>();
            if (start < filteredLogs.size()) {
                pageData = filteredLogs.subList(start, end);
            }
            
            PageResult<LogEntry> pageResult = new PageResult<LogEntry>();
            pageResult.setItems(pageData);
            pageResult.setTotal(filteredLogs.size());
            pageResult.setPageNum(page);
            pageResult.setPageSize(size);
            
            return Result.success(pageResult);
        } catch (Exception e) {
            log.error("Failed to query logs: {}", e.getMessage(), e);
            return Result.error("Failed to query logs: " + e.getMessage());
        }
    }

    @Override
    public Result<LogEntry> getLog(String logId) {
        log.debug("Getting log: {}", logId);
        
        for (LogEntry entry : logEntries) {
            if (entry.getLogId().equals(logId)) {
                return Result.success(entry);
            }
        }
        
        return Result.error("Log not found: " + logId);
    }

    @Override
    public Result<Boolean> deleteLog(String logId) {
        log.debug("Deleting log: {}", logId);
        
        Iterator<LogEntry> iterator = logEntries.iterator();
        while (iterator.hasNext()) {
            LogEntry entry = iterator.next();
            if (entry.getLogId().equals(logId)) {
                iterator.remove();
                return Result.success(true);
            }
        }
        
        return Result.error("Log not found: " + logId);
    }

    @Override
    public Result<Long> clearLogs(long beforeTimestamp) {
        log.info("Clearing logs before: {}", beforeTimestamp);
        
        long count = 0;
        Iterator<LogEntry> iterator = logEntries.iterator();
        
        while (iterator.hasNext()) {
            LogEntry entry = iterator.next();
            if (beforeTimestamp <= 0 || entry.getTimestamp() < beforeTimestamp) {
                iterator.remove();
                count++;
            }
        }
        
        return Result.success(Long.valueOf(count));
    }

    @Override
    public Result<LogExportResult> exportLogs(LogQuery query, String format) {
        log.info("Exporting logs with format: {}", format);
        
        try {
            Result<PageResult<LogEntry>> queryResult = queryLogs(query);
            if (!queryResult.isSuccess()) {
                return Result.error(queryResult.getError());
            }
            
            List<LogEntry> logs = queryResult.getData().getItems();
            
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String extension = "json".equalsIgnoreCase(format) ? "json" : "txt";
            String fileName = "logs_export_" + timestamp + "." + extension;
            String filePath = EXPORT_DIR + fileName;
            
            FileWriter writer = new FileWriter(filePath);
            
            if ("json".equalsIgnoreCase(format)) {
                writer.write("[\n");
                for (int i = 0; i < logs.size(); i++) {
                    LogEntry entry = logs.get(i);
                    writer.write("  {\n");
                    writer.write("    \"logId\": \"" + entry.getLogId() + "\",\n");
                    writer.write("    \"level\": \"" + entry.getLevel() + "\",\n");
                    writer.write("    \"message\": \"" + escapeJson(entry.getMessage()) + "\",\n");
                    writer.write("    \"source\": \"" + entry.getSource() + "\",\n");
                    writer.write("    \"timestamp\": " + entry.getTimestamp() + "\n");
                    writer.write("  }");
                    if (i < logs.size() - 1) {
                        writer.write(",");
                    }
                    writer.write("\n");
                }
                writer.write("]\n");
            } else {
                for (LogEntry entry : logs) {
                    writer.write(String.format("[%s] [%s] [%s] %s\n",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(entry.getTimestamp())),
                        entry.getLevel(),
                        entry.getSource(),
                        entry.getMessage()));
                }
            }
            
            writer.close();
            
            LogExportResult result = new LogExportResult();
            result.setFilePath(filePath);
            result.setFileName(fileName);
            result.setRecordCount(logs.size());
            result.setFileSize(new File(filePath).length());
            result.setFormat(format);
            result.setTimestamp(System.currentTimeMillis());
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("Failed to export logs: {}", e.getMessage(), e);
            return Result.error("Failed to export logs: " + e.getMessage());
        }
    }

    @Override
    public Result<LogStatistics> getStatistics(long startTime, long endTime) {
        log.debug("Getting log statistics: {} - {}", startTime, endTime);
        
        try {
            long total = 0;
            long infoCount = 0;
            long warningCount = 0;
            long errorCount = 0;
            long debugCount = 0;
            
            for (LogEntry entry : logEntries) {
                if ((startTime <= 0 || entry.getTimestamp() >= startTime) &&
                    (endTime <= 0 || entry.getTimestamp() <= endTime)) {
                    total++;
                    
                    String level = entry.getLevel();
                    if ("INFO".equals(level)) infoCount++;
                    else if ("WARNING".equals(level)) warningCount++;
                    else if ("ERROR".equals(level)) errorCount++;
                    else if ("DEBUG".equals(level)) debugCount++;
                }
            }
            
            LogStatistics stats = new LogStatistics();
            stats.setTotalCount(total);
            stats.setInfoCount(infoCount);
            stats.setWarnCount(warningCount);
            stats.setErrorCount(errorCount);
            stats.setDebugCount(debugCount);
            stats.setStartTime(startTime);
            stats.setEndTime(endTime);
            
            return Result.success(stats);
        } catch (Exception e) {
            log.error("Failed to get log statistics: {}", e.getMessage(), e);
            return Result.error("Failed to get log statistics: " + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, Long>> getLevelStatistics(long startTime, long endTime) {
        log.debug("Getting level statistics");
        
        Map<String, Long> stats = new HashMap<String, Long>();
        
        for (Map.Entry<String, AtomicLong> entry : levelCounters.entrySet()) {
            stats.put(entry.getKey(), Long.valueOf(entry.getValue().get()));
        }
        
        return Result.success(stats);
    }

    @Override
    public Result<Map<String, Long>> getSourceStatistics(long startTime, long endTime) {
        log.debug("Getting source statistics");
        
        Map<String, Long> stats = new HashMap<String, Long>();
        
        for (Map.Entry<String, AtomicLong> entry : sourceCounters.entrySet()) {
            stats.put(entry.getKey(), Long.valueOf(entry.getValue().get()));
        }
        
        return Result.success(stats);
    }

    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}
