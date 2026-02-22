package net.ooder.nexus.repository.impl;

import net.ooder.nexus.domain.mcp.model.LogEntry;
import net.ooder.nexus.dto.log.LogExportDTO;
import net.ooder.nexus.dto.log.LogQueryDTO;
import net.ooder.nexus.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * Log repository mock implementation
 * Provides in-memory storage for logs
 */
@Repository
public class LogRepositoryMock implements LogRepository {

    private static final Logger log = LoggerFactory.getLogger(LogRepositoryMock.class);
    private static final int MAX_LOG_ENTRIES = 10000;
    private static final String EXPORT_DIR = "./logs/export/";

    private final Queue<LogEntry> logEntries = new ConcurrentLinkedQueue<>();

    @PostConstruct
    public void init() {
        log.info("Initializing LogRepositoryMock");
        initializeSampleLogs();
        // Create export directory
        File exportDir = new File(EXPORT_DIR);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
    }

    @Override
    public List<LogEntry> findAll(LogQueryDTO query) {
        List<LogEntry> logs = new ArrayList<>(logEntries);

        // Apply filters
        if (query.getLevel() != null) {
            logs = logs.stream()
                    .filter(l -> l.getLevel().equalsIgnoreCase(query.getLevel()))
                    .collect(Collectors.toList());
        }

        if (query.getSource() != null) {
            logs = logs.stream()
                    .filter(l -> l.getSource().contains(query.getSource()))
                    .collect(Collectors.toList());
        }

        if (query.getStartTime() != null) {
            logs = logs.stream()
                    .filter(l -> l.getTimestamp() >= query.getStartTime())
                    .collect(Collectors.toList());
        }

        if (query.getEndTime() != null) {
            logs = logs.stream()
                    .filter(l -> l.getTimestamp() <= query.getEndTime())
                    .collect(Collectors.toList());
        }

        if (query.getKeyword() != null) {
            logs = logs.stream()
                    .filter(l -> l.getMessage().contains(query.getKeyword()))
                    .collect(Collectors.toList());
        }

        // Sort by timestamp descending
        logs.sort((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));

        // Apply limit
        if (query.getLimit() != null && logs.size() > query.getLimit()) {
            logs = logs.subList(0, query.getLimit());
        }

        return logs;
    }

    @Override
    public LogEntry findById(String logId) {
        return logEntries.stream()
                .filter(l -> l.getId().equals(logId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(LogEntry logEntry) {
        logEntries.offer(logEntry);
        // Maintain max size
        while (logEntries.size() > MAX_LOG_ENTRIES) {
            logEntries.poll();
        }
    }

    @Override
    public void deleteAll() {
        logEntries.clear();
    }

    @Override
    public long count() {
        return logEntries.size();
    }

    @Override
    public long count(LogQueryDTO query) {
        return findAll(query).size();
    }

    @Override
    public String export(LogExportDTO exportDTO) {
        try {
            LogQueryDTO query = new LogQueryDTO();
            query.setLevel(exportDTO.getLevel());
            query.setSource(exportDTO.getSource());
            query.setStartTime(exportDTO.getStartTime());
            query.setEndTime(exportDTO.getEndTime());
            if (exportDTO.getMaxRecords() != null) {
                query.setLimit(exportDTO.getMaxRecords());
            }

            List<LogEntry> logs = findAll(query);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "logs_export_" + timestamp + ".json";
            String filePath = exportDTO.getOutputPath() != null ? exportDTO.getOutputPath() : EXPORT_DIR + fileName;

            FileWriter writer = new FileWriter(filePath);
            writer.write("[\n");
            for (int i = 0; i < logs.size(); i++) {
                LogEntry entry = logs.get(i);
                writer.write("  {\n");
                writer.write("    \"id\": \"" + entry.getId() + "\",\n");
                writer.write("    \"level\": \"" + entry.getLevel() + "\",\n");
                writer.write("    \"message\": \"" + entry.getMessage() + "\",\n");
                writer.write("    \"source\": \"" + entry.getSource() + "\",\n");
                writer.write("    \"timestamp\": " + entry.getTimestamp() + "\n");
                writer.write("  }");
                if (i < logs.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }
            writer.write("]\n");
            writer.close();

            return filePath;
        } catch (Exception e) {
            log.error("Failed to export logs", e);
            return null;
        }
    }

    @Override
    public List<LogEntry> findRecent(int limit) {
        List<LogEntry> logs = new ArrayList<>(logEntries);
        logs.sort((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
        if (logs.size() > limit) {
            return logs.subList(0, limit);
        }
        return logs;
    }

    private void initializeSampleLogs() {
        addLogEntry("INFO", "System started successfully", "System");
        addLogEntry("INFO", "Health check service initialized", "HealthCheck");
        addLogEntry("INFO", "Network configuration loaded", "Network");
        addLogEntry("INFO", "MCP protocol adapter registered", "Protocol");
        addLogEntry("INFO", "End protocol adapter registered", "Protocol");
        addLogEntry("INFO", "Route protocol adapter registered", "Protocol");
        addLogEntry("INFO", "P2P network controller enabled", "Network");
        addLogEntry("INFO", "RealNexusService initialized", "Service");
        addLogEntry("INFO", "SystemStatusService initialized", "Service");
        addLogEntry("INFO", "LogService initialized", "Service");
    }

    private void addLogEntry(String level, String message, String source) {
        LogEntry entry = new LogEntry(level, message, source);
        logEntries.offer(entry);
    }
}
